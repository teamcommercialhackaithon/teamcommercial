package com.teamcommercial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@teamcommercial.com}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String resetToken, String userName) {
        try {
            System.out.println("=================================================");
            System.out.println("Attempting to send password reset email...");
            System.out.println("From: " + fromEmail);
            System.out.println("To: " + toEmail);
            System.out.println("Reset Token: " + resetToken);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request - Team Commercial");
            
            String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
            
            String emailBody = String.format(
                "Hello %s,\n\n" +
                "We received a request to reset your password for your Team Commercial account.\n\n" +
                "Click the link below to reset your password:\n" +
                "%s\n\n" +
                "This link will expire in 1 hour.\n\n" +
                "If you didn't request a password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Team Commercial Support",
                userName, resetLink
            );
            
            message.setText(emailBody);
            
            System.out.println("Sending email through SMTP...");
            mailSender.send(message);
            
            System.out.println("✓ Password reset email sent successfully to: " + toEmail);
            System.out.println("Reset link: " + resetLink);
            System.out.println("=================================================");
        } catch (Exception e) {
            System.err.println("=================================================");
            System.err.println("✗ Failed to send email to: " + toEmail);
            System.err.println("Error type: " + e.getClass().getName());
            System.err.println("Error message: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            e.printStackTrace();
            System.err.println("=================================================");
            System.err.println("NOTE: Email sending failed, but the reset token has been created.");
            System.err.println("You can still use the reset link manually:");
            System.err.println(frontendUrl + "/reset-password?token=" + resetToken);
            System.err.println("=================================================");
            
            // Don't throw exception - allow password reset to continue
            // The user can still get the token from logs if needed
        }
    }

    public void sendWelcomeEmail(String toEmail, String userName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to Team Commercial!");
            
            String emailBody = String.format(
                "Hello %s,\n\n" +
                "Welcome to Team Commercial!\n\n" +
                "Your account has been successfully created.\n" +
                "You can now log in using your credentials.\n\n" +
                "Best regards,\n" +
                "Team Commercial Support",
                userName
            );
            
            message.setText(emailBody);
            mailSender.send(message);
            
            System.out.println("Welcome email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
            // Don't throw exception for welcome email failure
        }
    }
}

