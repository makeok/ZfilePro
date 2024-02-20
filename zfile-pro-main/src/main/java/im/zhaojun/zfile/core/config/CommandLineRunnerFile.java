package im.zhaojun.zfile.core.config;


import im.zhaojun.zfile.module.storage.service.StorageTaskSerice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandLineRunnerFile implements CommandLineRunner {

    @Autowired
    StorageTaskSerice storageTaskSerice;

    @Override
    public void run(String... args) throws Exception {
        log.info("CommandLineRunner初始化开始：{}", args);
        storageTaskSerice.initThumbnail();
        storageTaskSerice.handleQueue();
        log.info("CommandLineRunner初始化结束：{}", args);
    }
}