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
 * Created by xiaonan.jia on 2017/8/31.
 */
@Controller
@RequestMapping("ip")
public class IpQuery {

    @Resource
    private IpQueryService ipQueryService;

    @ResponseBody
    @RequestMapping(value = "/query")
    public String getIpInfo(@RequestParam String ip) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Connection","Keep-Alive");
        response.addHeader("Content-Type","application/json;charset=UTF-8");
        return ipQueryService.getIpInfo(ip);
    }
}
