package cn.dogest.api.model;

/**
 * 成绩、学分模型
 * Created by xiaonan.jia on 2017/9/1.
 */
public class PointModel {
    private double grade = 0.0; // 绩点
    private double point = 0.0; // 已修学分(除公选课）
    private double pointTotal = 0.0; // 总学分（除公选课）
    private double elective = 0.0; // 公选课学分

    public PointModel() {
    }

    public PointModel(double grade, double point, double pointTotal, double elective) {
        this.grade = grade;
        this.point = point;
        this.pointTotal = pointTotal;
        this.elective = elective;
    }

    public double getGrade() {
        return grade;
    }

    public double getPoint() {
        return point;
    }

    public double getPointTotal() {
        return pointTotal;
    }

    public double getElective() {
        return elective;
    }
}
