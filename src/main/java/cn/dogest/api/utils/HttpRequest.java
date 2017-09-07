package cn.dogest.api.utils;

import cn.dogest.api.exception.ConnectionException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Http操作类
 * Created by xiaonan.jia on 2017/8/31.
 */
public class HttpRequest {
    /**
     * 向指定URL发送GET方法的请求
     * @param url 发送请求的URL
     * @param param  请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return String 所代表远程资源的响应html内容
     */
    public static String sendGet(String url, String param) throws ConnectionException {
        try {
            // 构造带参数的请求url字符串
            String urlNameString = url +( param == null ? "" : ("?" + param));
            // 构造URL对象
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取连接的输入流（响应）
            InputStream in = connection.getInputStream();
            // 构造一个字节数组输出流，以字节流的方式读写
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // 字节缓冲块
            byte[] buffer = new byte[512];
            int len;
            while ((len = in.read(buffer)) != -1) {
                // 将缓冲块内容写入字节数组输出流中
                bos.write(buffer, 0, len);
            }
            // 输出流转换为字节数组并以utf-8编码成字符串，即最终html文本内容
            String result = new String(bos.toByteArray(), "utf-8");
            in.close();
            bos.close();
            return result;
        } catch (IOException e) {
            throw new ConnectionException("网络异常，无法读取或写入数据！", e);
        }
    }
}
