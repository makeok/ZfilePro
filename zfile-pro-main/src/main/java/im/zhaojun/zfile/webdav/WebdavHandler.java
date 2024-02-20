package im.zhaojun.zfile.webdav;

import im.zhaojun.zfile.module.storage.model.dto.StorageSourceDTO;
import im.zhaojun.zfile.module.storage.model.entity.StorageSource;
import im.zhaojun.zfile.module.storage.model.enums.StorageTypeEnum;
import im.zhaojun.zfile.module.storage.service.StorageSourceService;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.io.File;
import java.util.List;

@Service
public class WebdavHandler {
    private WebDavSupport webDavSupport;
    private WebResourceRoot webResourceRoot;

    @Resource
    private StorageSourceService storageSourceService;

    public WebDavSupport getWebDavSupport() {
        return webDavSupport;
    }

    public void setWebDavSupport(WebDavSupport webDavSupport) {
        this.webDavSupport = webDavSupport;
    }

    public WebResourceRoot getWebResourceRoot() {
        return webResourceRoot;
    }

    public void setWebResourceRoot(WebResourceRoot webResourceRoot) {
        this.webResourceRoot = webResourceRoot;
    }

    public void initDavPath() {
        StandardRoot root = (StandardRoot)webResourceRoot;
        List<WebResourceSet> arr = root.getPreResourcesObj();
        arr.clear();

        List<StorageSource> list = storageSourceService.findAllOrderByOrderNum();

        for(StorageSource source : list){
            if(source.getEnable() && source.getType().equals(StorageTypeEnum.LOCAL)){
                String webpath = StringUtils.isEmpty(source.getKey())?source.getName() : source.getKey();
                StorageSourceDTO config = storageSourceService.findDTOById(source.getId());
                File filePath = new File(config.getStorageSourceAllParam().getFilePath());
                if(config.getStorageSourceAllParam().getEnableWebdav()!=null && config.getStorageSourceAllParam().getEnableWebdav()
                        && filePath.exists()){
                    DirResourceSet res = new DirResourceSet(webResourceRoot, "/"+webpath, filePath
                            .getAbsolutePath(), "/");
                    root.addPreResources(res);
                }
            }
        }
    }
}
