package cn.dogest.api.model;

import java.util.List;

/**
 * json返回数据模型
 * Created by xiaonan.jia on 2017/8/29.
 */
public class ResultModel {

    private double grade; // 绩点
    private double point; // 已修学分
    private double totalPoint; // 总学分
    private double elective; // 已修公选课学分
    private List<Course> list;  // 课程信息及成绩列表

    public ResultModel(double grade, double point, double totalPoint, double elective, List<Course> list) {
        this.grade = grade;
        this.point = point;
        this.totalPoint = totalPoint;
        this.elective = elective;
        this.list = list;
    }

    public double getGrade() {
        return grade;
    }

    public double getPoint() {
        return point;
    }

    public double getTotalPoint() {
        return totalPoint;
    }

    public List<Course> getList() {
        return list;
    }

    public double getElective() {
        return elective;
    }
}
