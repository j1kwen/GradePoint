package cn.dogest.api.controller;

import cn.dogest.api.service.EnergyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 能源相关接口
 * Created by xiaonan.jia on 2017/9/8.
 */
@Controller
@RequestMapping("energy")
public class Energy {

    @Resource
    private EnergyService energyService;

    /**
     * 获取指定寝室的能源信息
     * @param room 寝室号x#xxx
     * @param id 学号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/query")
    public Map<String, Object> getEnergy(@RequestParam(required = false) String room,
                                         @RequestParam(required = false) String id) {
        // 设置响应头部，使客户端可以通过ajax跨域请求该数据
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Connection","Keep-Alive");
        response.addHeader("Content-Type","application/json;charset=UTF-8");
        return energyService.getEnergy(room, id);
    }
}
