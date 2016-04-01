package com.app.finder.service;

import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.app.finder.common.util.MailSender;
import com.app.finder.config.JHipsterProperties;
import com.app.finder.domain.User;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

   /* @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private JavaMailSenderImpl javaMailSender;
    
    
    @Inject
    private MessageSource messageSource;
    */
    @Inject
    private SpringTemplateEngine templateEngine;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
        	
        	//由于用spring自带的代码发送邮件不好用。替换spring发送邮件的代码
        	
           /* MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);*/
        	
        	MailSender mailSender = new MailSender("smtp.kepinzhe.com");
        	mailSender.setUsername("system@kepinzhe.com");
            mailSender.setPassword("KePinZhe1234!@#");
            String from = "system@kepinzhe.com";
            String fromName = "科品者";
            String cc = null;
            String bcc = null;
            mailSender.sendHtmlMail(to, from, fromName, subject, content, cc,bcc);
            
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("activationEmail", context);
     // 用java -jar 命令时由于msg文件被打包到war中就找不到了，不在使用message文件
//        String subject = messageSource.getMessage("email.activation.title", null, locale);
        String subject = "科品者激活账号";
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendCreationEmail(User user, String baseUrl) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("creationEmail", context);
     // 用java -jar 命令时由于msg文件被打包到war中就找不到了，不在使用message文件
//        String subject = messageSource.getMessage("email.activation.title", null, locale);
        String subject = "科品者激活账号";
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("passwordResetEmail", context);
     // 用java -jar 命令时由于msg文件被打包到war中就找不到了，不在使用message文件
//        String subject = messageSource.getMessage("email.reset.title", null, locale);
        String subject = "科品者密码重置";
        sendEmail(user.getEmail(), subject, content, false, true);
    }
    
}
