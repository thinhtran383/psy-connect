package online.thinhtran.psyconnect.services;

import jakarta.annotation.PreDestroy;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import online.thinhtran.psyconnect.dto.mail.MailDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.username}")
    private String fromMail;

    private final JavaMailSender mailSender;

    @Async
    public void sendMail(MailDto mailStructure) throws MessagingException, IOException {
        mailStructure.setSubject("Change password");


        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromMail);
        helper.setTo(mailStructure.getTo());
        helper.setSubject(mailStructure.getSubject());

        String htmlContent = getHtmlContent(mailStructure.getContent());
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    private String getHtmlContent(String newPassword) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/change_password_template.html");
        InputStream inputStream = resource.getInputStream();
        String htmlTemplate = new String(FileCopyUtils.copyToByteArray(inputStream), StandardCharsets.UTF_8);


        return htmlTemplate.replace("{{newPassword}}", newPassword);
    }


}