package cn.dogest.api.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SecurityUtils {

    public static String encodeMD5String(String content) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(content.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String base64Encode(String content) {
        try {
            if(content.length() % 3 != 0) {
                content += mutliChar(" ", 3 - content.length() % 3);
            }
            byte[] bytes = Base64.getEncoder().encode(content.getBytes("utf-8"));
            String str = new String(bytes, "utf-8");
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    public static String base64Decode(String base) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base);
            return new String(bytes, "utf-8").trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static String mutliChar(String str, int count) {
        StringBuilder sb = new StringBuilder();
        while(count-- > 0) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String getProperty(String content, String key) {
        try {
            key = key.replaceAll("\\.", "\\.");
            Pattern pattern = Pattern.compile("(?<=" + key + "=).*");
            Matcher matcher = pattern.matcher(content);
            if(matcher.find()) {
                return matcher.group();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String str = "http://api.dogest.cn/verify?rid=1234";
        if(str.length() % 3 != 0) {
            str += mutliChar(" ", 3 - str.length() % 3);
        }
        String base = SecurityUtils.base64Encode(str);
        String content = SecurityUtils.base64Decode(base);
        System.out.println(base);
        System.out.println(content);
    }
}
