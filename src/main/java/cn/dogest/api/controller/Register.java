package cn.dogest.api.controller;

import cn.dogest.api.service.RegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("register")
public class Register {

    @Resource
    RegisterService registerService;

    @ResponseBody
    @RequestMapping(value = "/verify")
    public Map<String, Object> verify(@RequestParam(required = false) String rid,
                                      @RequestParam(required = false) String token) {
        return registerService.verify(rid, token);
    }

    @ResponseBody
    @RequestMapping(value = "/register")
    public Map<String, Object> register(@RequestParam(required = false) String user,
                                        @RequestParam(required = false) String password,
                                        @RequestParam(required = false) String token) {
        return registerService.register(user, password, token);
    }
}
