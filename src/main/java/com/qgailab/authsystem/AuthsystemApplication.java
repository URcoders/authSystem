package com.qgailab.authsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author qg
 */
@SpringBootApplication
@EnableScheduling
@ServletComponentScan
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class AuthsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthsystemApplication.class, args);
    }

}
