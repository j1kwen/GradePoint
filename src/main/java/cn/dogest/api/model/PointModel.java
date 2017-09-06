package cn.dogest.api.model;

/**
 * Created by xiaonan.jia on 2017/9/1.
 */
public class PointModel {
    private double grade = 0.0;
    private double point = 0.0;
    private double pointTotal = 0.0;
    private double elective = 0.0;

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
