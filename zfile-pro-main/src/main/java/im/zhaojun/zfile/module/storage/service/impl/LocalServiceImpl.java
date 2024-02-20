package im.zhaojun.zfile.module.storage.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.drew.imaging.ImageProcessingException;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import im.zhaojun.zfile.core.constant.ZFileConstant;
import im.zhaojun.zfile.core.exception.file.init.InitializeStorageSourceException;
import im.zhaojun.zfile.core.util.CodeMsg;
import im.zhaojun.zfile.core.util.ImgUtil;
import im.zhaojun.zfile.core.util.RequestHolder;
import im.zhaojun.zfile.core.util.StringUtils;
import im.zhaojun.zfile.module.config.model.dto.SystemConfigDTO;
import im.zhaojun.zfile.module.config.service.SystemConfigService;
import im.zhaojun.zfile.module.storage.model.bo.ThumbnailInfo;
import im.zhaojun.zfile.module.storage.model.enums.FileTypeEnum;
import im.zhaojun.zfile.module.storage.model.enums.StorageTypeEnum;
import im.zhaojun.zfile.module.storage.model.param.LocalParam;
import im.zhaojun.zfile.module.storage.model.result.FileItemResult;
import im.zhaojun.zfile.module.storage.service.StorageTaskSerice;
import im.zhaojun.zfile.module.storage.service.base.AbstractProxyTransferService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhaojun
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class LocalServiceImpl extends AbstractProxyTransferService<LocalParam> {

    private static final String PREVIEW_PARAM_NAME = "preview";

    @Value("${zfile.img.thumbnail.path}")
    private String thumbnail;

    @Value("${zfile.img.thumbnail.min.size:512000}")
    private long MIN_IMAGE_LEN;
    @Value("${map.baidu.apiKey}")
    private String MAP_BAIDU_APIKEY;
    private List<String> imgSuffixs;
    private List<String> audioSuffix;
    private List<String> videoSuffix;
    private List<String> textSuffix;
    private List<String> officeSuffix;
    @Autowired
    StorageTaskSerice storageTaskSerice;
    @Autowired
    SystemConfigService systemConfigService;

    @Override
    public void init() {
        // 初始化存储源
        File file = new File(param.getFilePath());
        // 校验文件夹是否存在
        if (!file.exists()) {
            String errMsg = StrUtil.format("文件路径:「{}」不存在, 请检查是否填写正确.", file.getAbsolutePath());
            throw new InitializeStorageSourceException(CodeMsg.STORAGE_SOURCE_INIT_FAIL,
                    storageId, errMsg).setResponseExceptionMessage(true);
        }

        // 初始化生成所有子目录的缩略图
        // initThumbnail("/");

        SystemConfigDTO systemConfig = systemConfigService.getSystemConfig();
        imgSuffixs = Arrays.asList(Optional.ofNullable(systemConfig.getCustomImageSuffix()).orElse("").split(","));
        audioSuffix = Arrays.asList(Optional.ofNullable(systemConfig.getCustomAudioSuffix()).orElse("").split(","));
        textSuffix = Arrays.asList(Optional.ofNullable(systemConfig.getCustomTextSuffix()).orElse("").split(","));
        videoSuffix = Arrays.asList(Optional.ofNullable(systemConfig.getCustomVideoSuffix()).orElse("").split(","));
        officeSuffix = Arrays.asList(Optional.ofNullable(systemConfig.getCustomOfficeSuffix()).orElse("").split(","));
    }


    @Override
    public List<FileItemResult> fileList(String folderPath) throws IOException {
        checkPathSecurity(folderPath);

        List<FileItemResult> fileItemList = new ArrayList<>();

        String fullPath = StringUtils.concat(param.getFilePath() + folderPath);
        File file = new File(fullPath);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }

        File[] files = file.listFiles();
        if (files == null) {
            return fileItemList;
        }
        for (File f : files) {
            FileItemResult item = fileToFileItem(f, folderPath);

            fileItemList.add(item);
        }
        // 检查缩略图
        if(param.getEnableThumbnail()){
            initThumbnail(folderPath, false);
        }

        return fileItemList;
    }

    // 生成缩略图
    public boolean createThumbnail(File f, String thumbnailFullPath) {
        String suffixName = f.getName().substring(f.getName().lastIndexOf(".") + 1);
        if (imgSuffixs.contains(suffixName) && f.length()>MIN_IMAGE_LEN) {// 文件大于200k
            File imgThumbnail = new File(thumbnailFullPath, f.getName());//压缩到50kb
            if(!imgThumbnail.exists()){
                ThumbnailInfo info = new ThumbnailInfo(f, imgThumbnail);
                storageTaskSerice.offer(info);
            }
        }
        return  false;
    }
    // 项目启动时初始化生成缩略图
    @Override
    public void initThumbnail(String folderPath, Boolean includeChildDir){
        checkPathSecurity(folderPath);
        String fullPath = StringUtils.concat(param.getFilePath() + folderPath);
        File file = new File(fullPath);
        if (!file.exists()) {
            return;
        }
        int num = 0;
        if( file.isDirectory() ) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }

            String thumbnailFullPath = StringUtils.concat(thumbnail , this.getName(), folderPath);
            File thumbnailFile = new File(thumbnailFullPath);

            for (File f : files) {
                if(f.isFile()){
                    boolean isCreated = createThumbnail(f, thumbnailFullPath);
                    if(isCreated) {
                        num++;
                    }
                }
                else{
                    if(includeChildDir) {
                        initThumbnail(StringUtils.concat(folderPath, f.getName()), includeChildDir);
                    }
                }
            }
        } else {
            String thumbnailFullPath = StringUtils.concat(thumbnail , this.getName(), folderPath);
            File thumbnailFile = new File(thumbnailFullPath);
            boolean isCreated = createThumbnail(file, thumbnailFullPath);
            if(isCreated) {
                num++;
            }
        }

        if(num > 0){
            log.info("启动时初始化缩略图, 存储源 id: [{}], 存储源类型: [{}], 存储源名称: [{}], 路径: [{}], 生成数量: [{}]",
                    getStorageId(), getStorageTypeEnum().getValue(), getName(), folderPath, num);
        }
    }

    // 定时清理过期缩略图
    @Override
    public void clearOldThumbnail(String folderPath, Boolean includeChildDir){
        checkPathSecurity(folderPath);
        String fullPath = StringUtils.concat(param.getFilePath() + folderPath);
        File file = new File(fullPath);//存储源文件夹

        String thumbnailFullPath = StringUtils.concat(thumbnail , this.getName(), folderPath);
        File thumbnailFile = new File(thumbnailFullPath);
        if (!thumbnailFile.exists()) {
            return;
        }
        // 未开启缩略图就删除
        if(!param.getEnableThumbnail()) {
            log.info("定时清理过期缩略图, 存储源 id: [{}], 存储源类型: [{}], 存储源名称: [{}], 路径: [{}]",
                    getStorageId(), getStorageTypeEnum().getValue(), getName(), folderPath);
            thumbnailFile.delete();
            return;
        }
        int num = 0;
        if(thumbnailFile.isDirectory()) {
            File[] files = thumbnailFile.listFiles();
            if (files == null) {
                return;
            }

            for (File f : files) {
                if(f.isFile()) {
                    File protoFile = new File(fullPath, f.getName());
                    if (!protoFile.exists()) {//原始文件不存在
                        f.delete();
                        num++;
                    }
                }else{//搜索子文件夹
                    this.clearOldThumbnail(StringUtils.concat(folderPath, f.getName()), includeChildDir);
                }
            }
        }else{
            File protoFile = new File(fullPath, thumbnailFile.getName());
            if (!protoFile.exists()) {//原始文件不存在
                thumbnailFile.delete();
                num++;
            }
        }

        if(num > 0){
            log.info("定时清理过期缩略图, 存储源 id: [{}], 存储源类型: [{}], 存储源名称: [{}], 路径: [{}], 数量: [{}]",
                    getStorageId(), getStorageTypeEnum().getValue(), getName(), folderPath, num);
        }
    }

    // 清空缩略图
    @Override
    public void destroy(){
        // 删除缩略图
        String thumbnailFullPath = StringUtils.concat(thumbnail , this.getName(), "/");
        File thumbnailFile = new File(thumbnailFullPath);
        String fullPath = StringUtils.concat(param.getFilePath() + "/");
        // 清理缩略图，缩略图文件夹未包含原始文件夹
        if (thumbnailFile.exists() && !thumbnailFullPath.contains(fullPath)) {
            thumbnailFile.delete();
        }
    }

    @Override
    public FileItemResult getFileItem(String pathAndName) {
        checkPathSecurity(pathAndName);

        String fullPath = StringUtils.concat(param.getFilePath(), pathAndName);

        File file = new File(fullPath);

        if (!file.exists()) {
            throw ExceptionUtil.wrapRuntime(new FileNotFoundException("文件不存在"));
        }

        String folderPath = StringUtils.getParentPath(pathAndName);
        return fileToFileItem(file, folderPath);
    }


    @Override
    public boolean newFolder(String path, String name) {
        checkPathSecurity(path);
        checkNameSecurity(name);

        String fullPath = StringUtils.concat(param.getFilePath(), path, name);
        return FileUtil.mkdir(fullPath) != null;
    }


    @Override
    public boolean deleteFile(String path, String name) {
        checkPathSecurity(path);
        checkNameSecurity(name);

        String fullPath = StringUtils.concat(param.getFilePath(), path, name);
        return FileUtil.del(fullPath);
    }


    @Override
    public boolean deleteFolder(String path, String name) {
        checkPathSecurity(path);
        checkNameSecurity(name);

        return deleteFile(path, name);
    }


    @Override
    public boolean renameFile(String path, String name, String newName) {
        checkPathSecurity(path);
        checkNameSecurity(name, newName);

        // 如果文件名没变，不做任何操作.
        if (StrUtil.equals(name, newName)) {
            return true;
        }

        String srcPath = StringUtils.concat(param.getFilePath(), path, name);
        File file = new File(srcPath);

        boolean srcExists = file.exists();
        if (!srcExists) {
            throw ExceptionUtil.wrapRuntime(new FileNotFoundException("文件夹不存在."));
        }

        FileUtil.rename(file, newName, true);
        return true;
    }


    @Override
    public boolean renameFolder(String path, String name, String newName) {
        checkPathSecurity(path);
        checkNameSecurity(name, newName);

        return renameFile(path, name, newName);
    }


    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.LOCAL;
    }


    @Override
    public void uploadFile(String pathAndName, InputStream inputStream) {
        checkPathSecurity(pathAndName);

        String baseFilePath = param.getFilePath();
        String uploadPath = StringUtils.removeDuplicateSlashes(baseFilePath + ZFileConstant.PATH_SEPARATOR + pathAndName);
        // 如果目录不存在则创建
        String parentPath = FileUtil.getParent(uploadPath, 1);
        if (!FileUtil.exist(parentPath)) {
            FileUtil.mkdir(parentPath);
        }

        File uploadToFileObj = new File(uploadPath);
        BufferedOutputStream outputStream = FileUtil.getOutputStream(uploadToFileObj);
        IoUtil.copy(inputStream, outputStream);
        IoUtil.close(outputStream);
        IoUtil.close(inputStream);
    }


    @Override
    public ResponseEntity<Resource> downloadToStream(String pathAndName, Boolean isThumbnail) {
        checkPathSecurity(pathAndName);
        File file = null;
        if(isThumbnail!=null && isThumbnail) {
            String thumbnailFullPath = StringUtils.concat(this.thumbnail , this.getName(), pathAndName);
            file = new File(StringUtils.removeDuplicateSlashes(thumbnailFullPath));
            if(!file.exists()) {
                file = new File(StringUtils.removeDuplicateSlashes(param.getFilePath() + ZFileConstant.PATH_SEPARATOR + pathAndName));
            }
        }else{
            file = new File(StringUtils.removeDuplicateSlashes(param.getFilePath() + ZFileConstant.PATH_SEPARATOR + pathAndName));
        }
        if (!file.exists()) {
            ByteArrayResource byteArrayResource = new ByteArrayResource("文件不存在或异常，请联系管理员.".getBytes(StandardCharsets.UTF_8));
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(byteArrayResource);
        }

        HttpServletRequest request = RequestHolder.getRequest();
        String type = request.getParameter("type");

        MediaType mimeType = MediaType.APPLICATION_OCTET_STREAM;
        if (StrUtil.equals(type, PREVIEW_PARAM_NAME)) {
            mimeType = MediaTypeFactory.getMediaType(file.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM);
        }

        HttpHeaders headers = new HttpHeaders();

        if (ObjectUtil.equals(mimeType, MediaType.APPLICATION_OCTET_STREAM)) {
            String fileName = file.getName();
            headers.setContentDispositionFormData("attachment", StringUtils.encodeAllIgnoreSlashes(fileName));
        }

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mimeType)
                .body(new FileSystemResource(file));
    }


    private FileItemResult fileToFileItem(File file, String folderPath) {
        FileItemResult fileItemResult = new FileItemResult();
        fileItemResult.setType(file.isDirectory() ? FileTypeEnum.FOLDER : FileTypeEnum.FILE);
        fileItemResult.setTime(new Date(file.lastModified()));
        fileItemResult.setSize(file.length());
        fileItemResult.setName(file.getName());
        fileItemResult.setPath(folderPath);

        if (fileItemResult.getType() == FileTypeEnum.FILE) {
            fileItemResult.setUrl(getDownloadUrl(StringUtils.concat(folderPath, file.getName())));
            fileItemResult.setThumbnail(fileItemResult.getUrl() + "&thumbnail=true");
        } else {
            fileItemResult.setSize(null);
        }
        return fileItemResult;
    }


    /**
     * 检查路径合法性：
     *  - 只有以 . 开头的允许通过，其他的如 ./ ../ 的都是非法获取上层文件夹内容的路径.
     *
     * @param   paths
     *          文件路径
     *
     * @throws IllegalArgumentException    文件路径包含非法字符时会抛出此异常
     */
    private static void checkPathSecurity(String... paths) {
        for (String path : paths) {
            // 路径中不能包含 .. 不然可能会获取到上层文件夹的内容
            if (StrUtil.startWith(path, "/..") || StrUtil.containsAny(path, "../", "..\\")) {
                throw new IllegalArgumentException("文件路径存在安全隐患: " + path);
            }
        }
    }


    /**
     * 检查路径合法性：
     *  - 不为空，且不包含 \ / 字符
     *
     * @param   names
     *          文件路径
     *
     * @throws IllegalArgumentException    文件名包含非法字符时会抛出此异常
     */
    private static void checkNameSecurity(String... names) {
        for (String name : names) {
            // 路径中不能包含 .. 不然可能会获取到上层文件夹的内容
            if (StrUtil.containsAny(name, "\\", "/")) {
                throw new IllegalArgumentException("文件名存在安全隐患: " + name);
            }
        }
    }

    @Override
    public boolean copyFile(String path, String name, String targetPath, String targetName, Boolean overwrite) {
        checkPathSecurity(path, targetPath);
        checkNameSecurity(name, targetName);
        String srcFileStr = StringUtils.concat(param.getFilePath() + path + ZFileConstant.PATH_SEPARATOR + name);
        File srcFile = new File(srcFileStr);
        String targetFileStr = StringUtils.concat(param.getFilePath() + targetPath + ZFileConstant.PATH_SEPARATOR + targetName);
        File targetFile = new File(targetFileStr);
        if (!srcFile.exists() || !targetFile.getParentFile().exists()) {
            return false;
        }
        if(!overwrite && targetFile.exists()) {
            return false;
        }
        try {
            FileUtils.copyFile(srcFile, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件复制异常，源目录：[{}],源文件名：[{}],目标目录：[{}],目标文件：[{}]", path, name, targetPath, targetName, e);
            return false;
        }
        log.info("文件复制成功，源目录：[{}],源文件名：[{}],目标目录：[{}],目标文件：[{}]", path, name, targetPath, targetName);
        return true;
    }

    @Override
    public boolean copyFolder(String path, String name, String targetPath, String targetName, Boolean overwrite) {
        checkPathSecurity(path, targetPath);
        checkNameSecurity(name, targetName);
        String srcFileStr = StringUtils.concat(param.getFilePath() + path + ZFileConstant.PATH_SEPARATOR + name);
        File srcFile = new File(srcFileStr);
        String targetFileStr = StringUtils.concat(param.getFilePath() + targetPath + ZFileConstant.PATH_SEPARATOR + targetName);
        File targetFile = new File(targetFileStr);
        if (!srcFile.exists() || !targetFile.getParentFile().exists()) {
            return false;
        }
        if(!overwrite && targetFile.exists()) {
            return false;
        }
        if(!targetFile.exists()) {
            targetFile.mkdir();
        }
        File[] flist = srcFile.listFiles();
        if(flist == null) {
            return true;
        }
        boolean ret = true;
        for(File f : flist) {
            try {
                if(f.isFile()) {
                    File dstFile = new File(targetFileStr, f.getName());
                    FileUtils.copyFile(f, dstFile);
                }else{
                    String childSrcPath = StringUtils.concat(path + ZFileConstant.PATH_SEPARATOR + name);
                    String childDstPath = StringUtils.concat(targetPath + ZFileConstant.PATH_SEPARATOR + targetName);
                    copyFolder(childSrcPath, f.getName(), childDstPath, f.getName(), overwrite);
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("文件复制异常，源目录：[{}],源文件名：[{}],目标目录：[{}],目标文件：[{}]", path, name, targetPath, targetName, e);
                ret = false;
            }
        }
        log.info("目录复制成功，源目录：[{}],源文件名：[{}],目标目录：[{}],目标文件：[{}]", path, name, targetPath, targetName);
        return ret;
    }

    @Override
    public boolean moveFile(String path, String name, String targetPath, String targetName, Boolean overwrite) {
        checkPathSecurity(path, targetPath);
        checkNameSecurity(name, targetName);
        String srcFileStr = StringUtils.concat(param.getFilePath() + path + ZFileConstant.PATH_SEPARATOR + name);
        File srcFile = new File(srcFileStr);
        String targetFileStr = StringUtils.concat(param.getFilePath() + targetPath + ZFileConstant.PATH_SEPARATOR + targetName);
        File targetFile = new File(targetFileStr);
        if (!srcFile.exists() || !targetFile.getParentFile().exists()) {
            return false;
        }
        if(targetFile.exists()) {
            if(overwrite) {// 覆盖
                targetFile.delete();// 删除再移动
                srcFile.renameTo(targetFile);
            }else{ // 不覆盖
                return false;
            }
        }else{
            srcFile.renameTo(targetFile);
        }
        log.info("文件移动成功，源目录：[{}],源文件名：[{}],目标目录：[{}],目标文件：[{}]", path, name, targetPath, targetName);
        return true;
    }

    @Override
    public boolean moveFolder(String path, String name, String targetPath, String targetName, Boolean overwrite) {
        checkPathSecurity(path, targetPath);
        checkNameSecurity(name, targetName);
        String srcFileStr = StringUtils.concat(param.getFilePath() + path + ZFileConstant.PATH_SEPARATOR + name);
        File srcFile = new File(srcFileStr);
        String targetFileStr = StringUtils.concat(param.getFilePath() + targetPath + ZFileConstant.PATH_SEPARATOR + targetName);
        File targetFile = new File(targetFileStr);
        if (!srcFile.exists() || !targetFile.getParentFile().exists()) {
            return false;
        }
        if(targetFile.exists()) {
            if(overwrite) {
                // 移动子文件夹和子文件
                File[] flist = srcFile.listFiles();
                if(flist == null) {
                    return true;
                }
                for(File f : flist) {
                    if(f.isFile()) {
                        File dstFile = new File(targetFileStr, f.getName());
                        if(dstFile.exists()) {
                            dstFile.delete();// 删除再移动
                        }
                        f.renameTo(dstFile);
                    }else{
                        String childSrcPath = StringUtils.concat(path + ZFileConstant.PATH_SEPARATOR + name);
                        String childDstPath = StringUtils.concat(targetPath + ZFileConstant.PATH_SEPARATOR + targetName);
                        moveFolder(childSrcPath, f.getName(), childDstPath, f.getName(), overwrite);
                    }
                }
                // 移动子文件夹和子文件后，删除原文件夹
                srcFile.delete();
            }else{ // 不覆盖
                return false;
            }
        }else{
            srcFile.renameTo(targetFile);
        }
        log.info("目录移动成功，源目录：[{}],源文件名：[{}],目标目录：[{}],目标文件：[{}]", path, name, targetPath, targetName);
        return true;
    }


    // 按照年月日文件整理
    @Override
    public boolean unFolder(String srcPath, String targetPath) {
        checkPathSecurity(srcPath, targetPath);
        String srcFileStr = StringUtils.concat(param.getFilePath() + srcPath);
        File srcFile = new File(srcFileStr);
        String targetFileStr = StringUtils.concat(param.getFilePath() + targetPath);
        File targetFile = new File(targetFileStr);
        if (!srcFile.exists() || !targetFile.exists()) {
            return false;
        }
        File[] flist = srcFile.listFiles();
        if(flist == null) {
            return true;
        }
        boolean ret = true;
        for(File f : flist) {
            if(f.isFile()) {
                if(!srcPath.equals(targetPath)) {
                    File dstFile = new File(targetFileStr, f.getName());
                    if(dstFile.exists()){
                        Boolean same = checkSameFile(f, dstFile);
                        // 文件相同
                        if(same) {
                            f.delete();
                        }else{
                            // 文件不相同，则加后缀并移动
                            String suffixName = f.getName().lastIndexOf(".")<0 ? "" :
                                    f.getName().substring(f.getName().lastIndexOf(".") + 1);
                            String newNameTemplate = f.getName().substring(0,suffixName.length()<1?f.getName().length():f.getName().lastIndexOf("."))
                                    + "(%d)." +suffixName;
                            for(int i=1; i<=1000; i++){
                                String newName = String.format(newNameTemplate, i);
                                File dstFile2 = new File(targetFileStr, newName);
                                if(!dstFile2.exists()) {
                                    f.renameTo(dstFile2);
                                    break;
                                }
                            }
                        }
                    }else{
                        f.renameTo(dstFile);
                    }
                }
            }else{
                String childSrcPath = StringUtils.concat(srcPath + ZFileConstant.PATH_SEPARATOR + f.getName());
                unFolder(childSrcPath, targetPath);
                f.delete();//删除解散后的文件夹
            }
        }
        log.info("目录解散成功，源目录：[{}],目标目录：[{}]", srcPath, targetPath);
        return ret;
    }

    // 按照年月日整理文件
    @Override
    public boolean arrangeFolderByTime(String path, String formatStr) throws IOException {
        checkPathSecurity(path);
        if(!formatStr.matches("[yMdHms/]+")) {//格式不对
            throw new IllegalArgumentException("格式化字符串不正确: " + formatStr);
        }
        String srcFileStr = StringUtils.concat(param.getFilePath() + path);
        File srcFile = new File(srcFileStr);
        if (!srcFile.exists()) {
            return false;
        }
        File[] flist = srcFile.listFiles();
        if(flist == null) {
            return true;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        boolean ret = true;
        for(File f : flist) {
            if(f.isFile()) {
                Path abspath = Paths.get(f.getAbsolutePath());
                BasicFileAttributes attrs = Files.readAttributes(abspath, BasicFileAttributes.class);
                // 获取修改时间和创建时间中的最小值
                long captureTime = Math.min(attrs.creationTime().toMillis(), attrs.lastModifiedTime().toMillis());
                String suffixName = f.getName().lastIndexOf(".")<0 ? "" :
                        f.getName().substring(f.getName().lastIndexOf(".") + 1);
                if (imgSuffixs.contains(suffixName)) {
                    try {
                        Date cdate = ImgUtil.readImgCaptrueTime(f);
                        if(cdate != null) {
                            captureTime = cdate.getTime();
                        }
                    } catch (ImageProcessingException e) {
                        e.printStackTrace();
                    }
                }
                String datestr = format.format(new Date(captureTime));
                String targetPathStr = StringUtils.concat(param.getFilePath() + path + ZFileConstant.PATH_SEPARATOR + datestr);
                File targetPath = new File(targetPathStr);
                if(!targetPath.exists()) {
                    targetPath.mkdirs();
                }
                File dstFile = new File(targetPath, f.getName());
                f.renameTo(dstFile);
            }
        }
        log.info("目录整理成功，目录：[{}]", path);
        return ret;
    }

    // 按照地点整理文件
    @Override
    public boolean arrangeFolderByAddress(String path, String format) throws IOException {
        checkPathSecurity(path);
        String [] formatArr = {"国", "国省", "省", "省市", "省市县"};
        if(!new HashSet<>(Arrays.asList(formatArr)).contains(format)) {
            throw new IllegalArgumentException("格式化字符串不正确: " + format);
        }
        String srcFileStr = StringUtils.concat(param.getFilePath() + path);
        File srcFile = new File(srcFileStr);
        if (!srcFile.exists()) {
            return false;
        }
        File[] flist = srcFile.listFiles();
        if(flist == null) {
            return true;
        }
        boolean ret = true;
        for(File f : flist) {
            if(f.isFile()) {
                String suffixName = f.getName().lastIndexOf(".")<0 ? "" :
                        f.getName().substring(f.getName().lastIndexOf(".") + 1);
                if (imgSuffixs.contains(suffixName)) {
                    try {
                        Map infoMap = ImgUtil.parseAddressByImg(f, MAP_BAIDU_APIKEY);
                        if(infoMap != null) {
                            String country = (String) infoMap.get("country");
                            String province = (String) infoMap.get("province");
                            String city = (String) infoMap.get("city");
                            String district = (String) infoMap.get("district");
                            String addrPath = province + ZFileConstant.PATH_SEPARATOR + city;
                            if(format.equals("国")) {
                                addrPath = country;
                            }else if(format.equals("省")) {
                                addrPath = province;
                            }else if(format.equals("国省")) {
                                addrPath = country + ZFileConstant.PATH_SEPARATOR + province;
                            }else if(format.equals("省市县")) {
                                addrPath = province + ZFileConstant.PATH_SEPARATOR + city + ZFileConstant.PATH_SEPARATOR + district;
                            }
                            String targetPathStr = StringUtils.concat(param.getFilePath() + path + ZFileConstant.PATH_SEPARATOR + addrPath);
                            File targetPath = new File(targetPathStr);
                            if(!targetPath.exists()) {
                                targetPath.mkdirs();
                            }
                            File dstFile = new File(targetPath, f.getName());
                            f.renameTo(dstFile);
                        }
                    } catch (ImageProcessingException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        log.info("按照地点整理文件成功，目录：[{}]", path);
        return ret;
    }
    @Override
    // 按照文件类型整理文件
    public boolean arrangeFolderByFileType(String path) {
        checkPathSecurity(path);
        String srcFileStr = StringUtils.concat(param.getFilePath() + path);
        File srcFile = new File(srcFileStr);
        if (!srcFile.exists()) {
            return false;
        }
        File[] flist = srcFile.listFiles();
        if(flist == null) {
            return true;
        }
        boolean ret = true;
        String imageFolderName = "图片";
        String audioFolderName = "音乐";
        String videoFolderName = "视频";
        String textFolderName = "文本";
        String docmentFolderName = "文档";
        for(File f : flist) {
            if(f.isFile()) {
                String suffixName = f.getName().lastIndexOf(".")<0 ? "" :
                        f.getName().substring(f.getName().lastIndexOf(".") + 1);
                String fileTypeFolder = "其他";
                if (imgSuffixs.contains(suffixName)) {
                    fileTypeFolder = imageFolderName;
                }else if (audioSuffix.contains(suffixName)) {
                    fileTypeFolder = audioFolderName;
                }else if (videoSuffix.contains(suffixName)) {
                    fileTypeFolder = videoFolderName;
                }else if (textSuffix.contains(suffixName)) {
                    fileTypeFolder = textFolderName;
                }else if (officeSuffix.contains(suffixName)) {
                    fileTypeFolder = docmentFolderName;
                }
                String targetPathStr = StringUtils.concat(param.getFilePath() + path + ZFileConstant.PATH_SEPARATOR + fileTypeFolder);
                File targetPath = new File(targetPathStr);
                if(!targetPath.exists()) {
                    targetPath.mkdirs();
                }
                File dstFile = new File(targetPath, f.getName());
                f.renameTo(dstFile);
            }
        }
        log.info("按照文件类型整理文件成功，目录：[{}]", path);
        return ret;
    }
    @Override
    // 查找重复文件
    public List<List<FileItemResult>> findRepeatFile(String path) {
        checkPathSecurity(path);

        List<FileItemResult> fileList = new ArrayList<>();
        listFiles(path, fileList);
        HashMap<Long, List<FileItemResult>> map = new HashMap<>();
        for(FileItemResult file1 : fileList) {
            List<FileItemResult> oneSizeList = map.get(file1.getSize());
            if(oneSizeList == null) {
                oneSizeList = new ArrayList<>(1);// 减小内存
                map.put(file1.getSize(), oneSizeList);
            }
            oneSizeList.add(file1);
        }
        // 删除单个文件
        Iterator<Map.Entry<Long, List<FileItemResult>>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Long, List<FileItemResult>> next = iterator.next();
            List<FileItemResult> oneSizeList = next.getValue();
            if(oneSizeList.size() < 2) {
                iterator.remove();
            }
        }
        // 遍历size相同的文件
        List<List<FileItemResult>> retList = new ArrayList<>();
        Set<Map.Entry<Long, List<FileItemResult>>> entrys = map.entrySet();
        for(Map.Entry<Long, List<FileItemResult>> entry : entrys) {
            // 获取size相同的文件列表
            List<FileItemResult> oneSizeList = entry.getValue();
            // 用于判断是否检索过
            HashSet<String> samedSet = new HashSet<>();
            for(FileItemResult file1 : oneSizeList) {
                String file1Path = file1.getPath()+ZFileConstant.PATH_SEPARATOR+file1.getName();

                if(samedSet.contains(file1Path)){// 已经查找过
                    continue;
                }

                String file1AbsPath = StringUtils.concat(param.getFilePath() + file1Path);
                File f1 = new File(file1AbsPath);
                List<FileItemResult> oneSameList = new ArrayList<>();
                oneSameList.add(file1);

                // 判断其他size相同文件是否一样
                for(FileItemResult file2 : oneSizeList) {
                    String file2Path = file2.getPath()+ZFileConstant.PATH_SEPARATOR+file2.getName();
                    if(file1 == file2 || samedSet.contains(file2Path)){// 已经查找过
                        continue;
                    }
                    String file2AbsPath = StringUtils.concat(param.getFilePath() + file2Path);
                    File f2 = new File(file2AbsPath);
                    Boolean same = checkSameFile(f1, f2);
                    if(same){
                        oneSameList.add(file2);
                        samedSet.add(file2Path);
                    }
                }
                samedSet.add(file1Path);
                if(oneSameList.size() > 1){// 有相同文件
                    retList.add(oneSameList);
                }
            }
        }

        log.info("获取重复文件列表成功，目录：[{}]", path);
        return retList;
    }


    // 列出所有文件，包括子文件夹的文件
    private void listFiles(String path, List<FileItemResult> result) {
        checkPathSecurity(path);
        String absFileStr = StringUtils.concat(param.getFilePath() + path);
        File absFile = new File(absFileStr);
        if (!absFile.exists()) {
            return ;
        }
        File[] flist = absFile.listFiles();
        if(flist == null) {
            return ;
        }

        for(File f : flist) {
            if(f.isFile()) {
                FileItemResult item = fileToFileItem(f, path);
                result.add(item);
            } else{
                String childFilePath = StringUtils.concat(path + ZFileConstant.PATH_SEPARATOR + f.getName());
                listFiles(childFilePath, result);
            }
        }
    }

    // 判断是否为相同文件
    private Boolean checkSameFile(File f1, File f2){
        if(f1 == null && f2 == null){
            return true;
        }
        if(f1 == null || f2 == null){
            return false;
        }
        boolean same = false;
        if(f2.length() == f1.length()){
            try {
                HashCode distHash = com.google.common.io.Files.asByteSource(f2).hash(Hashing.sha256());
                HashCode srcHash = com.google.common.io.Files.asByteSource(f1).hash(Hashing.sha256());
                if(distHash.equals(srcHash)) {
                    same = true;
                }
            } catch (IOException e) {
                log.error(String.valueOf(e.getStackTrace()));
            }
        }
        return same;
    }
}