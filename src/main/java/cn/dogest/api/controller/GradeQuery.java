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
 * Created by xiaonan.jia on 2017/8/29.
 */
@Controller
@RequestMapping("grade")
public class GradeQuery {

    @Resource
    private GradeQueryService gradeQueryService;

    @ResponseBody
    @RequestMapping(value = "/query")
    public Map<String, Object> getGradePoint(@RequestParam(required = false) String id) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        response.addHeader("Access-Control-Allow-Origin","*");
        return gradeQueryService.getGradePoint(id);
    }

    @ResponseBody
    @RequestMapping(value = "/info")
    public Map<String, Object> getStudentInfo(@RequestParam(required = false) String id,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(defaultValue = "1") String page,
                                              @RequestParam(defaultValue = "20") String size) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        response.addHeader("Access-Control-Allow-Origin","*");
        return gradeQueryService.getStudentInfo(id, name, page, size);
    }
}
