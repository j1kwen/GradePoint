package cn.dogest.api.utils;

import cn.dogest.api.model.mail.Mail;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public final class MailUtils {

    public static void send(Mail mail) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建参数配置, 用于连接邮件服务器的参数配置
                    Properties props = mail.getProperties();

                    // 根据配置创建会话对象, 用于和邮件服务器交互
                    Session session = Session.getInstance(props);
                    session.setDebug(mail.isDebug());

                    // 创建一封邮件
                    MimeMessage message = mail.getMimeMessage(session);

                    // 根据 Session 获取邮件传输对象
                    Transport transport = session.getTransport();

                    transport.connect(mail.getSmtpAccUser(), mail.getSmtpAccPassword());

                    // 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
                    transport.sendMessage(message, message.getAllRecipients());

                    // 关闭连接
                    transport.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
