package cn.dogest.api.model.mail;

import cn.dogest.api.model.AppInfo;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public abstract class Mail {

    protected SmtpConfigure smtpConfigure;
    protected boolean debug = false;
    protected String recv = "";
    protected Map<String, String> params = null;

    protected abstract String getMimeContent() throws Exception;
    protected abstract String getSubject();

    public Properties getProperties() {
        // 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", smtpConfigure.getServer());   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

        // enable ssl
        props.setProperty("mail.smtp.port", smtpConfigure.getPort());
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpConfigure.getPort());

        return props;
    }

    public MimeMessage getMimeMessage(Session session) throws Exception {
        // 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // From: 发件人
        message.setFrom(new InternetAddress(smtpConfigure.getUser(), smtpConfigure.getName(), "UTF-8"));

        // To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recv, "", "UTF-8"));

        // Subject: 邮件主题
        message.setSubject(getSubject(), "UTF-8");

        // Content: 邮件正文
        message.setContent(getMimeContent(), "text/html;charset=UTF-8");

        // 设置发件时间
        message.setSentDate(new Date());

        // 保存设置
        message.saveChanges();

        return message;
    }

    public String getSmtpAccUser() {
        return smtpConfigure.getUser();
    }

    public String getSmtpAccPassword() {
        return smtpConfigure.getPassword();
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void send() {

    }
}
