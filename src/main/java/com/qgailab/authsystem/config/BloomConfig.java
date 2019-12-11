package com.qgailab.authsystem.config;

import io.rebloom.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

/**
 * @author linxu
 * @date 2019/11/13
 * <tip>take care of yourself.everything is no in vain.</tip>
 * redis的bloom配置
 */
@Configuration
@PropertySource("classpath:reclient.properties")
public class BloomConfig {
    @Value("${ip}")
    private String ip;
    @Value("${port}")
    private Integer port;
    private static Client client;
    public static final String NAMESPACE="tokensNameSpace";

   /* @PostConstruct
    public void init() {
        client = new Client(ip, port);
        //默认创建口令命令空间
        client.delete(NAMESPACE);
        client.createFilter(NAMESPACE, 1000, 0.001);
    }*/

    public static Client getClient() {
        return client;
    }
}
