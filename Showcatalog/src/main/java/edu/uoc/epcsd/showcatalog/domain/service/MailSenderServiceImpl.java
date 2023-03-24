package edu.uoc.epcsd.showcatalog.domain.service;

import edu.uoc.epcsd.showcatalog.domain.User;
import es.atrujillo.mjml.config.template.TemplateFactory;
import es.atrujillo.mjml.service.auth.MjmlAuth;
import es.atrujillo.mjml.service.auth.MjmlAuthFactory;
import es.atrujillo.mjml.service.definition.MjmlService;
import es.atrujillo.mjml.service.impl.MjmlRestService;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Log4j2
@Service
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender javaMailSender;
    private final MjmlService mjmlService;
    @Autowired
    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        MjmlAuth MjmlAuth = MjmlAuthFactory.builder()
                .withMemoryCredentials()
                .mjmlCredentials("0a50f730-99bf-4366-b179-8a709c078737", "b49f0735-2ac7-4625-8afb-c078fdb717c1")
                .build();
        mjmlService = new MjmlRestService(MjmlAuth);
    }

    @Override
    @Async
    public void sendWelcomeEmail(User user) {
        try {
        String htmlMail = mjmlService.transpileMjmlToHtml(parseWelcomeTemplate(user.getFirstName(), user.getUsername()));
        sendHtmlMessage(user.getUsername(), "Bienvenido", htmlMail);
        } catch (IOException e) {
            log.error("sendWelcomeEmail",e);

        }
    }

    @Override
    public void sendHtmlMessage(String to, String subject, String htmlContent) {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent(htmlContent, "text/html");
            helper.setFrom("cultureindahouse@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Error sendHtmlMessage",e);
        }
    }

    private String parseWelcomeTemplate(String name, String username) throws IOException {

        var resource = new ClassPathResource("email/welcome.mjml");
        byte[] binaryData = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String templateString  = new String(binaryData, StandardCharsets.UTF_8);
        Context contextVars = new Context();
        contextVars.setVariable("name", name);
        contextVars.setVariable("username", username);
        return TemplateFactory.builder()
                .withStringTemplate()
                .template(templateString)
                .templateContext(contextVars)
                .buildTemplate();
    }

}
