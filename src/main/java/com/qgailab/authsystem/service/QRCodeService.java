package com.qgailab.authsystem.service;

import java.io.IOException;

/**
 * @InterfaceName QRCodeService
 * @Description TODO
 * @Author huange7
 * @Date 2019-11-17 9:58
 * @Version 1.0
 */
public interface QRCodeService {

    String getQRCode(String token) throws IOException;

}
