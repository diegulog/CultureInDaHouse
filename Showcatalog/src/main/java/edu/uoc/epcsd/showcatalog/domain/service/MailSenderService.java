package edu.uoc.epcsd.showcatalog.domain.service;

import edu.uoc.epcsd.showcatalog.domain.User;

public interface MailSenderService {
    void sendWelcomeEmail(User user);
    void sendHtmlMessage(String to, String subject, String htmlContent);
}
