package com.qgailab.authsystem.constance;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author linxu
 * @date 2019/11/12
 * <tip>take care of yourself.everything is no in vain.</tip>
 */
public enum Status {
    /**
     * 用户未注册，无法登录
     */
    UN_REGISTER(0x01),
    /**
     * 用户已注册，直接登录
     */
    REGISTER(0x02),
    /**
     * 身份证获取信息失败，不能进行下一步操作
     */
    GET_ID_CARD_ERROR(0x03),
    /**
     * 身份证获取信息成功，允许下一步操作
     */
    GET_ID_CARD_OK(0x04),
    /**
     * 指纹验证失败
     */
    FINGERPRINT_ERROR(0x05),
    /**
     * 指纹验证成功，允许下一步操作
     */
    FINGERPRINT_OK(0x06),
    /**
     * 摄像头对比失败，重新操作
     */
    PHOTO_ERROR(0x07),
    /**
     * 摄像头对比成功，允许下一步操作
     */
    PHOTO_OK(0x08),
    /**
     * 签名失败
     */
    SIGNATURE_ERROR(0x09),
    /**
     * 签名成功
     */
    SIGNATURE_OK(0x0a),
    /**
     * 登录成功
     */
    LOGIN_OK(0x0b),
    /**
     * 硬件故障，无法进行下一步操作
     */
    BROKEN(0x0c),
    /**
     * 硬件启动
     */
    HEALTH(0x0d);
    private static final Status[] FOR_BITS = {UN_REGISTER, REGISTER,
            GET_ID_CARD_ERROR, GET_ID_CARD_OK, FINGERPRINT_ERROR,
            FINGERPRINT_OK, PHOTO_ERROR, PHOTO_OK, SIGNATURE_ERROR,
            SIGNATURE_OK, LOGIN_OK, BROKEN, HEALTH};
    private final int bits;

    public int getStatus() {
        return bits;
    }

    public static Status forBits(int bits) {
        if (bits > FOR_BITS.length || bits < 0) {
            throw new IllegalArgumentException();
        }
        return FOR_BITS[bits];
    }

    Status(int bits) {
        this.bits = bits;
    }
}
