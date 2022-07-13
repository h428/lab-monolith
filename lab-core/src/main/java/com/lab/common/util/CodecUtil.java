package com.lab.common.util;


import cn.hutool.crypto.SecureUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class CodecUtil {

    public static String md5Hex(String data,String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }
        return DigestUtils.md5Hex(data + salt);
    }

    public static String sha256Hex(String data, String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }
        return DigestUtils.sha256Hex(data + salt);
    }

    public static String sha512Hex(String data, String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }
        return DigestUtils.sha512Hex(data + salt);
    }

    public static String generateSalt256(){
        return DigestUtils.sha256Hex(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
    }


    public static void main(String[] args) {
        System.out.println(sha256Hex("cat", "77af778b51abd4a3c51c5ddd97204a9c3ae614ebccb75a606c3b6865aed6744e"));
        System.out.println(SecureUtil.sha256(
            "cat" + "77af778b51abd4a3c51c5ddd97204a9c3ae614ebccb75a606c3b6865aed6744e"));
    }
}
