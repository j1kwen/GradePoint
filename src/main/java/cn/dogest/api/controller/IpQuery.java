package cn.dogest.api.controller;

import cn.dogest.api.service.IpQueryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * ip相关控制器
 * Created by xiaonan.jia on 2017/8/31.
 */
@Controller
@RequestMapping("ip")
public class IpQuery {

    @Resource
    private IpQueryService ipQueryService;

    /**
     * ip归属地查询
     * @param ip 待查询的ip地址
     * @return json格式的ip地址信息
     */
    @ResponseBody
    @RequestMapping(value = "/query")
    public String getIpInfo(@RequestParam String ip) {
        // 设置响应头部，使客户端可以通过ajax跨域请求该数据
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Connection","Keep-Alive");
        response.addHeader("Content-Type","application/json;charset=UTF-8");
        return ipQueryService.getIpInfo(ip);
    }
}
