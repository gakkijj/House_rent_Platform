package com.javaclimb.houserent.common.util;

import io.github.biezhi.ome.OhMyEmail;

import javax.mail.MessagingException;
import java.util.Properties;

/**
 * 发送邮件工具类
 */
public class MailUtil {
    /*发送邮件的邮件服务器*/
    private static String host = "smtp.qq.com";
    /*发送邮件的账号*/
    private static String username = "1036603324@qq.com";
    /*发送邮件的密码（授权码）*/
    private static String pwd = "gdtvacoqijpabfdh";
    /*发送者昵称*/
    private static String fromname = "Jarvis";

    /**
     * 配置邮件
     */
    public static void configMail(String smtpHost,String userName, String password){
        Properties properties = OhMyEmail.defaultConfig(false);
        properties.setProperty("mail.smtp.host",smtpHost);
        OhMyEmail.config(properties,userName,password);
    }

    /**
     * 发送邮件的方法
     * @param to        接收者
     * @param title     标题
     * @param content   内容
     */
    public static void sendEmail(String to, String title, String content) throws MessagingException {
        configMail(host,username,pwd);
        OhMyEmail.subject(title).from(fromname).to(to).html(content).send();

    }


    public static void main(String[] args) throws MessagingException {
        sendEmail(username,"四三二一","邮件发送");
    }
}
