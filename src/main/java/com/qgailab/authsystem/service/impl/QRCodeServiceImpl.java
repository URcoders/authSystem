package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.service.CacheService;
import com.qgailab.authsystem.service.QRCodeService;
import com.qgailab.authsystem.utils.QRCodeUtil;
import com.qgailab.authsystem.utils.TokenUtil;
import com.qgailab.authsystem.utils.VerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
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

    @Autowired
    private CacheService cacheService;

    private static String QRCODE_PATH = "http://ip:port/project/static/QRCode/token.png";

    @Override
    public String getQRCode(String token) {
        if (StringUtils.isEmpty(token)) {
            log.info("用户传输的token为空");
            return "";
        }
        String idCard = cacheService.queryIdCard(token);
        if (StringUtils.isEmpty(idCard)){
            log.info("用户执行查询二维码连接时传输了错误的token值");
            return "";
        }
        String url = QRCODE_PATH;
        QRCodeUtil.create(url);
        return url;
    }

}
