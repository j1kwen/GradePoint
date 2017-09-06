package cn.dogest.api.utils;

import java.util.*;

import cn.dogest.api.exception.CalculateException;
import cn.dogest.api.model.Course;
import cn.dogest.api.model.CourseType;
import cn.dogest.api.model.PointModel;

public class GradeCalculator {

	private PointModel major = null;
	private PointModel minor = null;
	private List<Course> majorCourse = null;
	private List<Course> minorCourse = null;
	
	public GradeCalculator(List<Course> courses) throws CalculateException {
		major = new PointModel();
		minor = new PointModel();
		majorCourse = new ArrayList<>();
		minorCourse = new ArrayList<>();
		calculate(courses);
	}

	/**
	 * 计算与处理
	 * @param courses
	 * @throws CalculateException
	 */
	private void calculate(List<Course> courses) throws CalculateException {
		// 获取主修和辅修专业课程总列表
		List<Course> tMajor = new ArrayList<>();
		List<Course> tMinor = new ArrayList<>();
		for(Course course : courses) {
			if(course.getCourseType() == CourseType.MINOR) {
				tMinor.add(course);
				continue;
			}
			tMajor.add(course);
		}
		majorCourse.addAll(tMajor);
		minorCourse.addAll(tMinor);
		major = calcPoint(tMajor);
		minor = calcPoint(tMinor);
	}

	/**
	 * 根据课程列表计算学分信息
	 * @param list
	 * @return
	 * @throws CalculateException
	 */
	private PointModel calcPoint(List<Course> list) throws CalculateException {
		double sumPoint = 0.0; // 除公选课外的总学分
		double sumProduct = 0.0; // 除公选课外的总学分绩点 SUM(学分*成绩)

		double c_point = 0.0; // 已修学分，不包括公选课
		double elective = 0.0; // 已修公选课学分

		//Collections.reverse(list); // 逆置 为了从后往前遍历，有重修的取最后一次（废弃）
		List<Course> set = new ArrayList<>();
		try {
			// 生成计算绩点的课程成绩集合
			for(Course course : list) {
				// 检查集合是否存在某课程
				if(set.contains(course)) {
					// 存在，更新最大值
					int idx = set.indexOf(course);
					Course inset = set.get(idx);
					//System.out.println(String.format("已存在：%s => %s / %s", inset.getName(), inset.getOrigGrade(), inset.getReGrade()));
					//System.out.println(String.format("重修：%s => %s / %s", course.getName(), course.getOrigGrade(), course.getReGrade()));
					set.set(idx, inset.getReplacedCourse(course));
				} else {
					// 不存在，直接放入集合
					set.add(course);
					//System.out.println(String.format("添加课程：%s => %s / %s", course.getName(), course.getOrigGrade(), course.getReGrade()));
				}
			}

			for(Course course : set) {
				//System.out.println(String.format("待计算课程：%s => %s / %s", course.getName(), course.getOrigGrade(), course.getReGrade()));
				// 计算课程学分绩点，0则不合格
				double tmp = course.getProductValue();

				if(course.getCourseType() == CourseType.ELECTIVE) {
					// 如果是公选课
					if(tmp > 0.0) {
						// 公选课合格，学分计入公选课学分
						elective += course.getPointValue(); // 计入公选课学分
					} else {
						// 公选课不合格无影响
						//System.out.println(String.format("不合格的公选课：%s => %s / %s", course.getName(), course.getOrigGrade(), course.getReGrade()));
					}
				} else {
					// 如果是必修或实践环节，需要计算绩点

					// 计算分母 - 总学分
					sumPoint += course.getPointValue();
					// 计算分子 - 学分绩点和
					sumProduct += tmp;
					if(tmp > 0) {
						// 必修合格，计入已修学分
						c_point += course.getPointValue(); // 计入已修学分
					}
				}
			}
			// 计算
			double c_grade = sumPoint == 0 ? 0 : sumProduct / sumPoint;
			return new PointModel(c_grade, c_point, sumPoint, elective);
		} catch (Exception e) {
			throw new CalculateException("计算过程发生错误！请稍后重试！", e);
		}
	}

	/**
	 * 获取主修专业已修公选课学分
	 * @return
	 */
	public double getMajorElective() {
		return major.getElective();
	}

	/**
	 * 获取辅修专业已修公选课学分
	 * @return
	 */
	public double getMinorElective() {
		return minor.getElective();
	}
	/**
	 * 获取主修专业绩点
	 * @return
	 */
	public double getMajorGradePoint() {
		return major.getGrade();
	}
	/**
	 * 获取辅修专业绩点
	 * @return
	 */
	public double getMinorGradePoint() {
		return minor.getGrade();
	}
	/**
	 * 获取主修专业已修学分
	 * @return
	 */
	public double getMajorPointPassed() {
		return major.getPoint();
	}
	/**
	 * 获取主修专业总学分
	 * @return
	 */
	public double getMajorPointTotal() {
		return major.getPointTotal();
	}
	/**
	 * 获取辅修专业已修学分
	 * @return
	 */
	public double getMinorPointPassed() {
		return minor.getPoint();
	}
	/**
	 * 获取辅修专业总学分
	 * @return
	 */
	public double getMinorPointTotal() {
		return minor.getPointTotal();
	}
	/**
	 * 获取主修课程成绩列表
	 * @return
	 */
	public List<Course> getMajorCourses() {
		return majorCourse;
	}
	/**
	 * 获取辅修课程成绩列表
	 * @return
	 */
	public List<Course> getMinorCourses() {
		return minorCourse;
	}
	
}
