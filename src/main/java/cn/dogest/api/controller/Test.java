package cn.dogest.api.controller;

import cn.dogest.api.dao.AdminMapper;
import cn.dogest.api.model.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by xiaonan.jia on 2017/11/14.
 */
@Controller
@RequestMapping("test")
public class Test {

    @Resource
    private AdminMapper adminMapper;

    @ResponseBody
    @RequestMapping("/test")
    public String test() {
        Admin admin = adminMapper.selectByUser("admin");
        return admin.toString();
    }
}
