package cn.dogest.api.utils;

import java.util.ArrayList;
import java.util.List;

import cn.dogest.api.exception.ConnectionException;
import cn.dogest.api.exception.IdNotFoundException;
import cn.dogest.api.exception.ParseHtmlException;
import cn.dogest.api.model.Student;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import cn.dogest.api.model.Course;

/**
 * 转换器类，根据爬虫返回的html内容解析特定数据
 */
public class HtmlConverter {
	
    private NetworkProcesser network = null; // 爬虫
    private int[] ignoreIndex = new int[] {0, 4, 6, 11, 12, 13}; // 爬取成绩信息要忽略的列索引
    private static final String regex = "&[^;]*;"; // 匹配html转义符号的正则式
    private String htmlContent = ""; // html内容

    /**
     * 构造一个转换器，根据html文档解析内容
     * @param network  爬虫
     * @throws ConnectionException 连接错误
     */
    public HtmlConverter(NetworkProcesser network) throws ConnectionException {
        this.network = network;
        try {
            this.htmlContent = this.network.getHtmlContent();
        } catch (Exception e) {
            throw new ConnectionException("从原始服务器获取数据失败，请稍后重试！");
        }
    }

    /**
     * 获取学生信息，此方法只能为绩点成绩爬虫使用
     * @return
     * @throws Exception
     */
    public Student getStudentInfo() throws ParseHtmlException, IdNotFoundException {
        if(network.getType() != NetworkProcesser.TYPE_GRADE) {
            // 检查爬虫类型，不能使用此方法的爬虫应抛出异常
            throw new ParseHtmlException("无法解析Html文档，类型不匹配！");
        }
        // 获取一个解析器
        Parser parser = Parser.createParser(htmlContent, "UTF-8");
        // 构造一个结点过滤器
        NodeFilter tableFilter = new NodeFilter() {

            @Override
            public boolean accept(Node node) {
                String plain = node.toHtml();
                // 过滤学生基本信息table结点
                if(plain.startsWith("<table width=\"100%\" border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"1\">") && plain.indexOf("学生基本信息") != -1) {
                    return true;
                }
                return false;
            }
        };
        try {
            // 根据过滤器获取结点列表
            NodeList tableList = parser.parse(tableFilter);
            // 获取的数量不为1，说明解析出现问题，可能是html内容出错，抛出异常
            if(tableList.size() != 1) {
                throw new ParseHtmlException("无法匹配到学生信息表，请检查后重试！");
            }
            // 获取结点列表的迭代器
            NodeIterator iter = tableList.elements();
            // 由于列表只有一项，不必循环，直接获取迭代器下一项即可
            Node table = iter.nextNode();
            // 获取新的解析器，继续解析表格行，解析内容为table结点的html内容
            Parser pTable = Parser.createParser(table.toHtml(), "UTF-8");
            // 构造一个行内容过滤器
            NodeFilter trFilter = new NodeFilter() {

                @Override
                public boolean accept(Node node) {
                    // 只过滤出tr内容
                    if(node.toHtml().startsWith("<tr>")) {
                        return true;
                    }
                    return false;
                }
            };
            // 获取tr结点列表
            NodeList trList = pTable.parse(trFilter);
            // 学生信息表只有两行，第一行为表头，所以取第二行即为学生信息即索引1
            Node node = trList.elementAt(1);
            // 获取该行所有单元格的迭代器
            NodeIterator tdIter = node.getChildren().elements();
            int idx = 0;
            Student stu = new Student();
            while(tdIter.hasMoreNodes()) {
                Node td = tdIter.nextNode();
                // 如果不是特定格式开头，说明有干扰结点，应当排除
                if(!td.toHtml().startsWith("<td scope=\"col\"")) {
                    continue;
                }
                // 获取单元格内文本
                String pText = td.toPlainTextString();
                // 删除html转义字符
                String text = pText.replaceAll(regex, "").trim();
                // 将该单元格文本写入学生信息模型中
                stu = setStudentInfo(stu, text, idx, 0);
                idx++;
            }
            // 如果学生信息不完整说明构造过程出错，即找不到学生信息
            if(!stu.checkOK()) {
                throw new IdNotFoundException("找不到该学号的学生信息，请输入正确的学号！");
            }
            return stu;
        } catch (ParserException e) {
            throw new ParseHtmlException("解析Html文档出错！请检查学号或稍后重试！", e);
        }
    }

    /**
     * 获取所有课程列表，此方法只能为绩点成绩爬虫使用
     * @return
     * @throws Exception
     */
    @SuppressWarnings("serial")
    public List<Course> getCourseList() throws ParseHtmlException {
        if(network.getType() != NetworkProcesser.TYPE_GRADE) {
            // 检查爬虫类型，不能使用此方法的爬虫应抛出异常
            throw new ParseHtmlException("无法解析Html文档，类型不匹配！");
        }
        List<Course> res = new ArrayList<>();
        // 获取解析器
        Parser parser = Parser.createParser(htmlContent, "UTF-8");
        // 构造table过滤器
        NodeFilter tableFilter = new NodeFilter() {

            @Override
            public boolean accept(Node node) {
                String plain = node.toHtml();
                // 过滤成绩信息表
                if(plain.startsWith("<table width=\"100%\" border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"1\">") && plain.indexOf("成绩信息") != -1) {
                    return true;
                }
                return false;
            }
        };
        try {
            NodeList tableList = parser.parse(tableFilter);
            // 不等于1说明没有匹配到成绩信息表
            if(tableList.size() != 1) {
                throw new ParseHtmlException("无法匹配到成绩信息表！请检查输入是否正确！");
            }
            NodeIterator iter = tableList.elements();
            Node table = iter.nextNode();
            // 将成绩信息表转换成html内容构造新的解析器
            Parser pTable = Parser.createParser(table.toHtml(), "UTF-8");
            // 构造tr行过滤器
            NodeFilter trFilter = new NodeFilter() {

                @Override
                public boolean accept(Node node) {
                    if(node.toHtml().startsWith("<tr>")) {
                        return true;
                    }
                    return false;
                }
            };
            // 获取所有tr
            NodeList trList = pTable.parse(trFilter);
            NodeIterator trIter = trList.elements();
            boolean ignoredTh = false;
            while(trIter.hasMoreNodes()) {
                Node node = trIter.nextNode();
                // 忽略第一行，即表头
                if(!ignoredTh) {
                    ignoredTh = true;
                    continue;
                }
                // 字段列表，为了后面生成构造课程对象的字符串数组参数
                List<String> field = new ArrayList<>();
                // 获取该行的所有列（字段）
                NodeIterator tdIter = node.getChildren().elements();
                int index = 0;
                while(tdIter.hasMoreNodes()) {
                    Node td = tdIter.nextNode();
                    // 有干扰结点则忽略
                    if(!td.toHtml().startsWith("<td scope=\"col\"")) {
                        continue;
                    }
                    // 如果该索引是需要忽略的索引
                    if(checkIgnore(index++)) {
                        continue;
                    }
                    String pText = td.toPlainTextString().trim();
                    // 将字段内容加入列表中
                    field.add(pText.replaceAll(regex, "").trim());
                }
                try {
                    // 构造课程对象
                    Course course = new Course(field.toArray(new String[field.size()]));
                    // 加入课程
                    res.add(course);
                } catch (Exception e) {
                    // 发生异常，说明在构造课程时抛出了NullPointer异常，即数组长度不符合要求，也就是解析出错
                    // do nothing
                }
            }
        } catch (ParserException e) {
            throw new ParseHtmlException("解析Html文档出错！请检查学号或稍后重试！", e);
        }
        return res;
    }

    /**
     * 获取学生信息表，此方法只能为学生信息爬虫使用
     * @return
     * @throws ParseHtmlException
     */
    public List<Student> getStudentList() throws ParseHtmlException {
        if(network.getType() != NetworkProcesser.TYPE_INFO) {
            // 检查爬虫类型，不能使用此方法的爬虫应抛出异常
            throw new ParseHtmlException("无法解析Html文档，类型不匹配！");
        }
        // 学生信息表
        List<Student> list = new ArrayList<>();
        // 构造解析器
        Parser parser = Parser.createParser(htmlContent, "UTF-8");
        // 构造学生基本信息表过滤器
        NodeFilter tableFilter = new NodeFilter() {

            @Override
            public boolean accept(Node node) {
                String plain = node.toHtml();
                // 过滤出table结点
                if(plain.startsWith("<table width=\"100%\" border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"1\">") && plain.indexOf("学生基本信息") != -1) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        try {
            NodeList tableList = parser.parse(tableFilter);
            // 大小不为1说明匹配不到table
            if(tableList.size() != 1) {
                throw new ParseHtmlException("无法匹配到学生基本信息表！请检查输入是否正确！");
            }
            NodeIterator iter = tableList.elements();
            Node table = iter.nextNode();
            // 将table结点转换为html内容并构造新的解析器
            Parser pTable = Parser.createParser(table.toHtml(), "UTF-8");
            // 构造tr行过滤器
            NodeFilter trFilter = new NodeFilter() {

                @Override
                public boolean accept(Node node) {
                    if(node.toHtml().startsWith("<tr>")) {
                        return true;
                    }
                    return false;
                }
            };
            // 获取所有tr结点
            NodeList trList = pTable.parse(trFilter);
            NodeIterator trIter = trList.elements();
            boolean ignoredTh = false;
            while(trIter.hasMoreNodes()) {
                Node node = trIter.nextNode();
                // 忽略表头，即第一行
                if(!ignoredTh) {
                    ignoredTh = true;
                    continue;
                }
                NodeIterator tdIter = node.getChildren().elements();
                int idx = 0;
                Student stu = new Student();
                while(tdIter.hasMoreNodes()) {
                    Node td = tdIter.nextNode();
                    // 干扰结点要忽略
                    if(!td.toHtml().startsWith("<td scope=\"col\" align")) {
                        continue;
                    }
                    String pText = td.toPlainTextString();
                    // 删除文本中的html转义字符
                    String text = pText.replaceAll(regex, "").trim();
                    // 构造学生信息
                    stu = setStudentInfo(stu, text, idx, 1);
                    idx++;
                }
                // 如果学生信息不完整则跳过该条信息
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
     * @param index 索引值
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
     * @param stu 学生信息模型
     * @param text 要赋值的文本
     * @param index 文本所在的索引
     * @param offset 索引偏移量，为了兼容绩点页面和信息页面
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
