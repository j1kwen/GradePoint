package cn.dogest.api.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * json返回数据模型
 * Created by xiaonan.jia on 2017/8/29.
 */
public class ResultModel {

    private BigDecimal grade; // 绩点
    private BigDecimal point; // 已修学分
    private BigDecimal totalPoint; // 总学分
    private BigDecimal elective; // 已修公选课学分
    private List<Course> list;  // 课程信息及成绩列表

    public ResultModel(BigDecimal grade, BigDecimal point, BigDecimal totalPoint, BigDecimal elective, List<Course> list) {
        this.grade = grade;
        this.point = point;
        this.totalPoint = totalPoint;
        this.elective = elective;
        this.list = list;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public BigDecimal getTotalPoint() {
        return totalPoint;
    }

    public List<Course> getList() {
        return list;
    }

    public BigDecimal getElective() {
        return elective;
    }
}
