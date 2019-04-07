package site.btrainbow.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import site.btrainbow.server.model.InitInfoDo;
import site.btrainbow.server.model.ResponseVo;
import site.btrainbow.server.service.InitService;
import site.btrainbow.server.utils.ResultUtil;

@Slf4j
@Component
public class ScheduleJob {
    @Autowired
    private InitService initService;

    /**
     * 定时方法入口
     */
    @Scheduled(cron = "0 40 02 * * ?")
    @CachePut("initData")
    public ResponseVo cronJob() {
        InitInfoDo initInfoDo = initService.init();
        log.error(">>>>> cache: " + initInfoDo.toString());
        log.error(">>>>> Cache has been updated <<<<<");
        return ResultUtil.success(initInfoDo);
    }

    @Bean
    public TaskScheduler scheduledExecutorService() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("scheduled-thread-");
        return scheduler;
    }
}
