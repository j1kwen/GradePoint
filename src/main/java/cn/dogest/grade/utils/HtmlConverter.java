package cn.dogest.grade.utils;

import java.util.ArrayList;
import java.util.List;

import cn.dogest.grade.exception.ConnectionException;
import cn.dogest.grade.exception.IdNotFoundException;
import cn.dogest.grade.exception.ParseHtmlException;
import cn.dogest.grade.model.StatusCode;
import cn.dogest.grade.model.Student;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import cn.dogest.grade.model.Course;

public class HtmlConverter {
	
	private NetworkProcesser network = null;
	private int[] ignoreIndex = new int[] {0, 4, 6, 11, 12, 13};
	private static final String regex = "&[^;]*;";
	private String htmlContent = "";
	
	public HtmlConverter(NetworkProcesser network) throws ConnectionException {
		this.network = network;
		try {
			this.htmlContent = this.network.getHtmlContent();
		} catch (Exception e) {
			throw new ConnectionException("从原始服务器获取数据失败，请稍后重试！");
		}
	}

	/**
	 * 获取学生信息
	 * @return
	 * @throws Exception
	 */
	public Student getStudentInfo() throws ParseHtmlException, IdNotFoundException {
		if(network.getType() != NetworkProcesser.TYPE_GRADE) {
			throw new ParseHtmlException("无法解析Html文档，类型不匹配！");
		}
		Parser parser = Parser.createParser(htmlContent, "UTF-8");
		NodeFilter tableFilter = new NodeFilter() {

			@Override
			public boolean accept(Node node) {
				String plain = node.toHtml();
				if(plain.startsWith("<table width=\"100%\" border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"1\">") && plain.indexOf("学生基本信息") != -1) {
					return true;
				}
				return false;
			}
		};
		try {
			NodeList tableList = parser.parse(tableFilter);
			if(tableList.size() != 1) {
				throw new ParseHtmlException("无法匹配到学生信息表，请检查后重试！");
			}
			NodeIterator iter = tableList.elements();
			Node table = iter.nextNode();

			Parser pTable = Parser.createParser(table.toHtml(), "UTF-8");
			NodeFilter trFilter = new NodeFilter() {

				@Override
				public boolean accept(Node node) {
					if(node.toHtml().startsWith("<tr>")) {
						return true;
					}
					return false;
				}
			};
			NodeList trList = pTable.parse(trFilter);
			Node node = trList.elementAt(1);
			NodeIterator tdIter = node.getChildren().elements();
			int idx = 0;
			Student stu = new Student();
			while(tdIter.hasMoreNodes()) {
				Node td = tdIter.nextNode();
				if(!td.toHtml().startsWith("<td scope=\"col\"")) {
					continue;
				}
				String pText = td.toPlainTextString();
				String text = pText.replaceAll(regex, "").trim();
				stu = setStudentInfo(stu, text, idx, 0);
				idx++;
			}
			if(!stu.checkOK()) {
				throw new IdNotFoundException("找不到该学号的学生信息，请输入正确的学号！");
			}
			return stu;
		} catch (ParserException e) {
			throw new ParseHtmlException("解析Html文档出错！请检查学号或稍后重试！", e);
		}
	}
	/**
	 * 获取所有课程列表
	 * @return
	 * @throws
	 */
	@SuppressWarnings("serial")
	public List<Course> getCourseList() throws ParseHtmlException {
		if(network.getType() != NetworkProcesser.TYPE_GRADE) {
			throw new ParseHtmlException("无法解析Html文档，类型不匹配！");
		}
		List<Course> res = new ArrayList<>();
		Parser parser = Parser.createParser(htmlContent, "UTF-8");
		NodeFilter tableFilter = new NodeFilter() {
			
			@Override
			public boolean accept(Node node) {
				String plain = node.toHtml();
				if(plain.startsWith("<table width=\"100%\" border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"1\">") && plain.indexOf("成绩信息") != -1) {
					return true;
				}
				return false;
			}
		};
		try {
			NodeList tableList = parser.parse(tableFilter);
			if(tableList.size() != 1) {
				throw new ParseHtmlException("无法匹配到成绩信息表！请检查输入是否正确！");
			}
			NodeIterator iter = tableList.elements();
			Node table = iter.nextNode();

			Parser pTable = Parser.createParser(table.toHtml(), "UTF-8");
			NodeFilter trFilter = new NodeFilter() {

				@Override
				public boolean accept(Node node) {
					if(node.toHtml().startsWith("<tr>")) {
						return true;
					}
					return false;
				}
			};
			NodeList trList = pTable.parse(trFilter);
			NodeIterator trIter = trList.elements();
			boolean ignoredTh = false;
			while(trIter.hasMoreNodes()) {
				Node node = trIter.nextNode();
				if(!ignoredTh) {
					ignoredTh = true;
					continue;
				}
				List<String> field = new ArrayList<>();
				NodeIterator tdIter = node.getChildren().elements();
				int index = 0;
				while(tdIter.hasMoreNodes()) {
					Node td = tdIter.nextNode();
					if(!td.toHtml().startsWith("<td scope=\"col\"")) {
						continue;
					}
					if(checkIgnore(index++)) {
						continue;
					}
					String pText = td.toPlainTextString().trim();
					field.add(pText.replaceAll(regex, "").trim());
				}
				try {
					Course course = new Course(field.toArray(new String[field.size()]));
					res.add(course);
				} catch (Exception e) {
					// do nothing
				}
			}
		} catch (ParserException e) {
			throw new ParseHtmlException("解析Html文档出错！请检查学号或稍后重试！", e);
		}
		return res;
	}

	/**
	 * 获取学生信息表
	 * @return
	 * @throws ParseHtmlException
	 */
	public List<Student> getStudentList() throws ParseHtmlException {
		if(network.getType() != NetworkProcesser.TYPE_INFO) {
			throw new ParseHtmlException("无法解析Html文档，类型不匹配！");
		}
		List<Student> list = new ArrayList<>();
		Parser parser = Parser.createParser(htmlContent, "UTF-8");
		NodeFilter tableFilter = new NodeFilter() {

			@Override
			public boolean accept(Node node) {
				String plain = node.toHtml();
				if(plain.startsWith("<table width=\"100%\" border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"1\">") && plain.indexOf("学生基本信息") != -1) {
					return true;
				} else {
					return false;
				}
			}
		};
		try {
			NodeList tableList = parser.parse(tableFilter);
			if(tableList.size() != 1) {
				throw new ParseHtmlException("无法匹配到学生基本信息表！请检查输入是否正确！");
			}
			NodeIterator iter = tableList.elements();
			Node table = iter.nextNode();

			Parser pTable = Parser.createParser(table.toHtml(), "UTF-8");
			NodeFilter trFilter = new NodeFilter() {

				@Override
				public boolean accept(Node node) {
					if(node.toHtml().startsWith("<tr>")) {
						return true;
					}
					return false;
				}
			};
			NodeList trList = pTable.parse(trFilter);
			NodeIterator trIter = trList.elements();
			boolean ignoredTh = false;
			while(trIter.hasMoreNodes()) {
				Node node = trIter.nextNode();
				if(!ignoredTh) {
					ignoredTh = true;
					continue;
				}
				NodeIterator tdIter = node.getChildren().elements();
				int idx = 0;
				Student stu = new Student();
				while(tdIter.hasMoreNodes()) {
					Node td = tdIter.nextNode();
					if(!td.toHtml().startsWith("<td scope=\"col\" align")) {
						continue;
					}
					String pText = td.toPlainTextString();
					String text = pText.replaceAll(regex, "").trim();
					stu = setStudentInfo(stu, text, idx, 1);
					idx++;
				}
				if(!stu.checkOK()) {
					continue;
				}
				list.add(stu);
			}
		} catch (ParserException e) {
			throw new ParseHtmlException("解析Html文档出错！请检查输入或稍后重试！", e);
		}
		return list;
	}
	/**
	 * 检查该索引是否可以忽略
	 * @param index
	 * @return
	 */
	private boolean checkIgnore(int index) {
		for(int i : ignoreIndex) {
			if(i == index) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置Student的信息
	 * @param stu
	 * @param text
	 * @param index
	 * @return
	 */
	private Student setStudentInfo(Student stu, String text, int index, int offset) {
	    int idx = index - offset;
		if(idx == 0) {
			stu.setId(text);
		} else if(idx == 1) {
			stu.setName(text);
		} else if(idx == 2) {
			stu.setGender(text);
		} else if(idx == 3) {
			stu.setGrade(text);
		} else if(idx == 4) {
			stu.setCollege(text);
		} else if(idx == 5) {
			stu.setMajor(text);
		} else if(idx == 6) {
			stu.setCls(text);
		} else if(idx == 8) {
			stu.setLevel(text);
		} else if(idx == 9) {
			stu.setStm(text);
		}
		return stu;
	}
}
