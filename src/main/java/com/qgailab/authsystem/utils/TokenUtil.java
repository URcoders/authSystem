package com.qgailab.authsystem.utils;

import com.qgailab.authsystem.config.BloomConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @author linxu
 * @date 2019/11/13
 * <tip>take care of yourself.everything is no in vain.</tip>
 */
public class TokenUtil {
    /**
     * 使用身份证号码加UUID生成一个唯一的TOKEN
     * 使用Bloom校验全局的唯一性。
     *
     * @param idCard id card
     * @return digested id card
     */
    public static String createToken(String idCard) {
        if (idCard == null || idCard.length() == 0) {
            throw new IllegalArgumentException();
        }
        String digested;
        while (!checkIfExist((digested = DigestUtil.digest(idCard + UUID.randomUUID(), 5)))) {
            return digested;
        }
        return null;
    }

    /**
     * bloom check
     *
     * @param token token
     * @return t or f
     */
    private static boolean checkIfExist(String token) {
        return BloomConfig.getClient().exists(BloomConfig.NAMESPACE, token);
    }
}
