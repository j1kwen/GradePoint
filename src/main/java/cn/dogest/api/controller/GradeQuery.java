package cn.dogest.api.controller;

import cn.dogest.api.service.GradeQueryService;
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
 * 绩点、学生信息相关控制器
 * Created by xiaonan.jia on 2017/8/29.
 */
@Controller
@RequestMapping("grade")
public class GradeQuery {

    @Resource
    private GradeQueryService gradeQueryService;

    /**
     * 绩点查询
     * @param id 学号
     * @return json格式的学生信息以及绩点与成绩信息
     */
    @ResponseBody
    @RequestMapping(value = "/query")
    public Map<String, Object> getGradePoint(@RequestParam(required = false) String id) {
        // 设置响应头部，使客户端可以通过ajax跨域请求该数据
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        response.addHeader("Access-Control-Allow-Origin","*");
        return gradeQueryService.getGradePoint(id);
    }

    /**
     * 学生姓名学号查询，可模糊匹配
     * @param id 学号
     * @param name 姓名
     * @param page 页号
     * @param size 每页的条目数
     * @return json格式的学生信息数据
     */
    @ResponseBody
    @RequestMapping(value = "/info")
    public Map<String, Object> getStudentInfo(@RequestParam(required = false) String id,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(defaultValue = "1") String page,
                                              @RequestParam(defaultValue = "20") String size) {
        // 设置响应头部，使客户端可以通过ajax跨域请求该数据
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        response.addHeader("Access-Control-Allow-Origin","*");
        return gradeQueryService.getStudentInfo(id, name, page, size);
    }
}
