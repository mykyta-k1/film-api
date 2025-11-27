package com.filmapi.film.application.service;

import com.filmapi.film.application.contract.EmailService;
import com.filmapi.film.model.Film;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;
  private final String fromEmail;

  public EmailServiceImpl(
      JavaMailSender mailSender,
      TemplateEngine templateEngine,
      @Value("${api.mail.from}") String fromEmail) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.fromEmail = fromEmail;
  }

  @Override
  public void sendFilm(String recipientEmail, String subject, Film film) {
    Context thymeleafContext = new Context();
    thymeleafContext.setVariable("film", film);

    String htmlContent = templateEngine.process("film-email-template", thymeleafContext);

    sendHtmlEmail(recipientEmail, htmlContent, subject);
  }

  private void sendHtmlEmail(String recipientEmail, String htmlContent, String subject) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      // Main params
      helper.setFrom(fromEmail);
      helper.setTo(recipientEmail);
      helper.setSubject(subject);

      // Set html content
      helper.setText(htmlContent, true);

      mailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
