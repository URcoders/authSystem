package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.service.QRCodeService;
import com.qgailab.authsystem.utils.QRCodeUtil;
import com.qgailab.authsystem.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName QRCodeServiceImpl
 * @Description TODO
 * @Author huange7
 * @Date 2019-11-17 10:00
 * @Version 1.0
 */
@Service
@Slf4j
public class QRCodeServiceImpl implements QRCodeService {
    @Override
    public String getQRCode(String token) throws IOException {
        if (StringUtils.isEmpty(token)){
            log.info("用户传输的token为空");
            return "";
        }
        // todo 根据token获取二维码
        String dir = "";
        String url = "http://ip:port/static/" + dir + "/" + token;
        QRCodeUtil.create(url, dir, UUID.randomUUID().toString().replace("-", ""));
        return url;
    }
}
