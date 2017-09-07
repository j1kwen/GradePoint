package cn.dogest.api.service;

import cn.dogest.api.exception.ConnectionException;
import cn.dogest.api.utils.HttpRequest;
import org.springframework.stereotype.Service;

/**
 * ip信息服务类
 * Created by xiaonan.jia on 2017/8/31.
 */
@Service
public class IpQueryService {

    public String getIpInfo(String ip) {
        String url = "http://ip.taobao.com/service/getIpInfo.php";
        String param = "ip=" + ip;
        try {
            String html = HttpRequest.sendGet(url, param);
            return html;
        } catch (ConnectionException e) {
            return "{\"code\":1,\"data\":\""+ e.getMessage() +"\"}";
        }
    }
}
