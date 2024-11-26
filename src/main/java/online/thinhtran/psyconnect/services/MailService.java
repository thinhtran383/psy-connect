package online.thinhtran.psyconnect.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.mail.MailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.username}")
    private String fromMail;

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(MailDto mailDto) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromMail);
        helper.setTo(mailDto.getTo());
        helper.setSubject(mailDto.getSubject());

        String htmlContent = generateHtmlContent(mailDto.getTemplateName(), mailDto.getPlaceholders());
        helper.setText(htmlContent, true);

        System.out.println("Email Content: " + htmlContent);

        mailSender.send(mimeMessage);
    }

    private String generateHtmlContent(String templateName, Map<String, Object> placeholders) {
        Context context = new Context();
        placeholders.forEach(context::setVariable);

        return templateEngine.process(templateName, context);
    }

}
