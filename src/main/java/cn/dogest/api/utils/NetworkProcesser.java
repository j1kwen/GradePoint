package cn.dogest.api.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * 爬虫，爬取指定页面的html文档内容
 */
public class NetworkProcesser {

    private String url = null; // 爬取的url
    private String paramFormat = null; // 参数格式化字符串
    private String param = null; // 格式化后的参数串
    private int type = 0; // 爬虫类型值
    public static final int TYPE_GRADE = 0; // 爬虫类型，成绩页
    public static final int TYPE_INFO = 1; // 爬虫类型，学生信息页

    /**
     * 构造一个总成绩查询页
     * @param id 准确学号
     */
    public NetworkProcesser(String id) {
        this.url = "http://210.44.176.116/cjcx/zcjcx_list.php";
        this.paramFormat = "post_xuehao=%s&Submit=提交";
        this.param = String.format(paramFormat, id);
        this.type = NetworkProcesser.TYPE_GRADE;
    }

    /**
     * 构造一个学生学号查询页
     * @param id 学号，可模糊匹配
     * @param name 姓名，可模糊匹配
     */
    public NetworkProcesser(String id, String name) {
        id = id != null ? id : "";
        name = name != null ? name : "";
        this.url = "http://210.44.176.116/cjcx/xhcx_list.php";
        this.paramFormat = "post_xingming=%s&post_xuehao=%s&Submit=提交";
        this.param = String.format(paramFormat, name, id);
        this.type = NetworkProcesser.TYPE_INFO;
    }
    /**
     * 获取请求的html页面
     * @exception Exception 读写异常或网络异常
     * @return html文档字符串，utf-8编码
     */
    public String getHtmlContent() throws Exception {
        // 构造URL
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求头部属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // 发送POST必须的设置
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        // 写入请求参数
        out.print(this.param);
        // 刷新输出流的缓冲，即发送请求参数
        out.flush();
        // 获取请求的输入流（响应）
        InputStream ips = conn.getInputStream();
        // 构造字节数组输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // 缓冲块
        byte[] buffer = new byte[512];
        int len;
        while((len = ips.read(buffer)) != -1) {
            // 将缓冲块数据写入字节数组输出流
            bos.write(buffer, 0, len);
        }
        // 获取输出流对应的字节数组并转换成utf-8编码的字符串，即html文档
        String result = new String(bos.toByteArray(), "UTF-8");
        if(out != null){
            out.close();
        }
        if(bos != null) {
            bos.close();
        }
        if(ips != null) {
            ips.close();
        }
        return result;
    }

    /**
     * 获取爬虫类型值
     * @return
     */
    public int getType() {
        return type;
    }
}
