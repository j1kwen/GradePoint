package cn.dogest.api.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class NetworkProcesser {

	private String url = null;
	private String paramFormat = null;
	private String param = null;
	private int type = 0;
	public static final int TYPE_GRADE = 0;
	public static final int TYPE_INFO = 1;

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
	 * 获取返回的html页面
	 * @return
	 */
	public String getHtmlContent() throws Exception {
		PrintWriter out = null;
        BufferedReader in = null;
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        out.print(this.param);
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        InputStream ips = conn.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int len;
        while((len = ips.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        String result = new String(bos.toByteArray(), "UTF-8");
        if(out!=null){
            out.close();
        }
        if(in!=null){
            in.close();
        }
        return result;
	}

    public int getType() {
        return type;
    }
}
