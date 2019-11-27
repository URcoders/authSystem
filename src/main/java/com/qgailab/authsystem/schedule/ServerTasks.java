package com.qgailab.authsystem.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author linxu
 * @date 2019/1/20
 * this is about serverCron tasks management.
 * included some check function and release unneeded resource.
 *
 */
@Component
@Slf4j
public class ServerTasks {
    @Scheduled(cron = "0/2 * * * * ? ")
    public void checkState() {
        //TODO SEND PING TO EVERY NODE AND GET PONG MSG.


    }

}