package im.zhaojun.zfile.module.storage.service;

import im.zhaojun.zfile.module.storage.context.StorageSourceContext;
import im.zhaojun.zfile.module.storage.model.bo.ThumbnailInfo;
import im.zhaojun.zfile.module.storage.model.dto.StorageSourceDTO;
import im.zhaojun.zfile.module.storage.model.entity.StorageSource;
import im.zhaojun.zfile.module.storage.model.enums.StorageTypeEnum;
import im.zhaojun.zfile.module.storage.model.param.IStorageParam;
import im.zhaojun.zfile.module.storage.service.base.AbstractBaseFileService;
import im.zhaojun.zfile.module.storage.service.base.AbstractProxyTransferService;
import im.zhaojun.zfile.module.storage.service.impl.LocalServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class StorageTaskSerice {
    @Resource
    StorageSourceService storageSourceService;
    @Resource
    private StorageSourceContext storageSourceContext;

    @Value("${zfile.img.thumbnail.after.size:51200}")
    private long THUMBNAIL_AFTER_LEN;

    private ArrayBlockingQueue<ThumbnailInfo> queue = new ArrayBlockingQueue(10240);

    // 每小时检查缩略图是否需要生成
    @Async
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void initThumbnail() {
        List<StorageSource> list = storageSourceService.findAllOrderByOrderNum();
        for (StorageSource storageSource : list) {
            try {
                Integer storageId = storageSource.getId();
                String storageName = storageSource.getName();
                StorageTypeEnum storageTypeEnum = storageSourceService.findStorageTypeById(storageId);
                AbstractBaseFileService<IStorageParam> abstractBaseFileService = storageSourceContext.getByStorageId(storageId);
                StorageSourceDTO config = storageSourceService.findDTOById(storageSource.getId());
                if(config.getStorageSourceAllParam().getEnableThumbnail()){
//                    AbstractProxyTransferService abstractProxyTransferService = (AbstractProxyTransferService)abstractBaseFileService;
//                    LocalServiceImpl localService = (LocalServiceImpl)abstractProxyTransferService;
                    abstractBaseFileService.initThumbnail("/", true);
                    log.info("定时生成缩略图成功, 存储源 id: [{}], 存储源类型: [{}], 存储源名称: [{}]",
                            storageSource.getId(), storageSource.getType().getDescription(), storageSource.getName());
                }
            } catch (Exception e) {
                log.error("定时生成缩略图失败, 存储源 id: {}, 存储源类型: {}, 存储源名称: {}",
                        storageSource.getId(), storageSource.getType().getDescription(), storageSource.getName(), e);
            }
        }
    }

    // 每天9点检查缩略图是否需要清理
    @Async
    @Scheduled(cron = "0 0 9/24 * * ? ")
    public void clearThumbnail() {
        List<StorageSource> list = storageSourceService.findAllOrderByOrderNum();
        for (StorageSource storageSource : list) {
            try {
                Integer storageId = storageSource.getId();
                String storageName = storageSource.getName();
                StorageTypeEnum storageTypeEnum = storageSourceService.findStorageTypeById(storageId);
                AbstractBaseFileService<IStorageParam> abstractBaseFileService = storageSourceContext.getByStorageId(storageId);
                StorageSourceDTO config = storageSourceService.findDTOById(storageSource.getId());
                // 本地存储源配置了开启缩略图
                if(StorageTypeEnum.LOCAL == storageTypeEnum && config.getStorageSourceAllParam().getEnableThumbnail()){
                    AbstractProxyTransferService abstractProxyTransferService = (AbstractProxyTransferService)abstractBaseFileService;
                    LocalServiceImpl localService = (LocalServiceImpl)abstractProxyTransferService;
                    localService.clearOldThumbnail("/", true);
                    log.info("定时清理缩略图成功, 存储源 id: [{}], 存储源类型: [{}], 存储源名称: [{}]",
                            storageSource.getId(), storageSource.getType().getDescription(), storageSource.getName());
                }
            } catch (Exception e) {
                log.error("定时清理缩略图失败, 存储源 id: {}, 存储源类型: {}, 存储源名称: {}",
                        storageSource.getId(), storageSource.getType().getDescription(), storageSource.getName(), e);
            }
        }
    }

    // 加入缩略图任务到队列中
    public boolean offer(ThumbnailInfo info){
        boolean ret = queue.offer(info);
        return ret;
    }

    // 多线程生成缩略图
    public void handleQueue(){
        Thread thumbnailThread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    ThumbnailInfo next = queue.peek();
                    if (next == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    } else {
                        List<ThumbnailInfo> messages = new ArrayList<>();
                        int messagesCount = queue.drainTo(messages);
                        ExecutorService executorService = Executors.newFixedThreadPool(8);
                        List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
                        Callable<Boolean> task = null;
                        for (ThumbnailInfo info : messages) {
                            task = new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    File f = info.getSrc();
                                    File imgThumbnail = info.getDst();
                                    long tLen = THUMBNAIL_AFTER_LEN / 4;
                                    double scale = f.length() < tLen ? 1.0 : Math.sqrt(tLen * 1.0D / f.length());
                                    scale = scale < 0.01 ? 0.01 : scale;
                                    scale = Math.ceil(scale * 100) * 1.0D / 100;//取2位小数
                                    if (scale > 0.9) {// 压缩比不高的，不压缩
                                        // FileUtils.copyFile(f, imgThumbnail);
                                        return false;
                                    } else {
                                        try {
                                            if (!imgThumbnail.getParentFile().exists()) {
                                                imgThumbnail.getParentFile().mkdirs();
                                            }
                                            Thumbnails.of(f)
                                                    .scale(scale).outputQuality(1)
                                                    //将生成的缩略图写入文件
                                                    .toFile(imgThumbnail);
                                            return true;
                                        } catch (IOException e) {
                                            log.error("定时生成缩略图失败, 原图: {}, 缩略图: {}, ",
                                                    f.getAbsolutePath(), imgThumbnail.getAbsolutePath(), e);
                                        }
                                    }
                                    return false;
                                }
                            };
                            tasks.add(task);
                        }
                        try {
                            List<Future<Boolean>> results = executorService.invokeAll(tasks);
                            try {
                                if (!executorService.awaitTermination(3000, TimeUnit.MILLISECONDS)) {
                                    executorService.shutdownNow();
                                }
                            } catch (InterruptedException e) {
                                executorService.shutdownNow();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            log.error("定时生成缩略图失败,", e);
                        }
                    }
                }
            };
        };
        thumbnailThread.setName("thumbnailThread");
        thumbnailThread.start();
    }
}
