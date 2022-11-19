package com.cf.cabinescaperoom.controllers;

import com.cf.cabinescaperoom.models.Contact;
import com.cf.cabinescaperoom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @GetMapping("contact")
    public String contactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact";
    }

    @PostMapping("sendEmail")
    public String sendContactForm(Contact contact, Model model)
            throws MessagingException, UnsupportedEncodingException {

        sendEmail(contact);

        return "redirect:/home";
    }

    public void sendEmail(Contact contact)
            throws MessagingException, UnsupportedEncodingException {
        //Get current date time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-yyyy HH:mm:ss");
        String formatDateTime = now.format(formatter);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(contact.getEmail(), "Cabin Contact Page");
        helper.setTo("cabin.escape.room.web.app@gmail.com");

        String subject = "Email from Cabin Web App";

        helper.setSubject(subject);

        String content = "<div class=''>"
                + "<div id=':x2' class='ii gt' jslog='20277; u014N:xr6bB; 4:W251bGwsbnVsbCxbXV0.'>"
                + "<div id=':x1' class='a3s aiL msg-2458848946273030031'><u></u>"
                + "<div style='margin:0;padding:0;background-color:#ffffff'>"
                + "<div style='display:none;max-height:0px;overflow:hidden'>"
                + "You've received a new <span class='il'>form</span> submission. --"
                + "</div>"
                + "<table bgcolor='#ffffff' cellpadding='0' cellspacing='0' role='presentation' style='table-layout:fixed;vertical-align:top;min-width:320px;border-spacing:0;border-collapse:collapse;background-color:#ffffff;width:100%' valign='top' width='100%'>"
                + "<tbody>"
                + "<tr style='vertical-align:top' valign='top'>"
                + "<td style='word-break:break-word;vertical-align:top' valign='top'>"
                + "<div style='background-color:transparent'>"
                + "<div class='m_-2458848946273030031block-grid'"
                + "style='min-width:320px;max-width:600px;word-wrap:break-word;word-break:break-word;Margin:0 auto;background-color:#ffffff'>"
                + "<div"
                + "style='border-collapse:collapse;display:table;width:100%;background-color:#ffffff'>"
                + "<div class='m_-2458848946273030031col'"
                + "style='min-width:320px;max-width:600px;display:table-cell;vertical-align:top;width:600px'>"
                + "<div class='m_-2458848946273030031col_cont' style='width:100%!important'>"
                + "<div style='border-top:0px solid transparent;border-left:0px solid transparent;border-bottom:0px solid transparent;border-right:0px solid transparent;padding-top:5px;padding-bottom:5px;padding-right:0px;padding-left:0px'>"
                + "<div style='color:#555555;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif;line-height:1.2;padding-top:30px;padding-right:25px;padding-bottom:0px;padding-left:25px'>"
                + "<div style='line-height:1.2;font-size:12px;color:#555555;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif'>"
                + "<p style='margin:0;font-size:24px;line-height:1.2;word-break:break-word;margin-top:0;margin-bottom:0'>"
                + "<span style='font-size:24px'><strong>New <span"
                + "class='il'>form</span> submission on "
                + " Cabin Web App</strong></span>"
                + "</p>"
                + "</div>"
                + "</div>"
                + "<div style='color:#555555;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif;line-height:1.2;padding-top:10px;padding-right:25px;padding-bottom:30px;padding-left:25px'>"
                + "<div"
                + "style='line-height:1.2;font-size:12px;color:#555555;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif'>"
                + "<p"
                + "style='margin:0;font-size:14px;line-height:1.2;word-break:break-word;margin-top:0;margin-bottom:0'>"
                + "Someone just submitted a <span"
                + "class='il'>form</span> on localhost:8080/contact"
                + "Here's what they had to say:"
                + "</p>"
                + "</div>"
                + "</div>"
                + "<div style='color:#000000;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif;line-height:1.5;padding-top:10px;padding-right:25px;padding-bottom:10px;padding-left:25px'>"
                + "<div style='line-height:1.5;font-size:12px;color:#000000;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif'>"
                + "<p style='margin:0;font-size:14px;line-height:1.5;word-break:break-word;margin-top:0;margin-bottom:0'>"
                + "<span style='color:#999999'>name</span>"
                + "</p>"
                + "<span style='margin:0;font-size:16px;line-height:1.5;word-break:break-word;margin-top:0;margin-bottom:0;font-size:16px'>" + contact.getName() + " "
                + "</span>"
                + "</div>"
                + "</div>"
                + "<div style='color:#000000;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif;line-height:1.5;padding-top:10px;padding-right:25px;padding-bottom:10px;padding-left:25px'>"
                + "<div style='line-height:1.5;font-size:12px;color:#000000;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif'>"
                + "<p style='margin:0;font-size:14px;line-height:1.5;word-break:break-word;margin-top:0;margin-bottom:0'>"
                + "<span style='color:#999999'>email</span>"
                + "</p>"
                + "<span style='margin:0;font-size:16px;line-height:1.5;word-break:break-word;margin-top:0;margin-bottom:0;font-size:16px'>"
                + "<a href='mailto:" + contact.getEmail() + "' target='_blank'> " + contact.getEmail() + " </a>"
                + "</span>"
                + "</div>"
                + "</div>"
                + "<div style='color:#000000;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif;line-height:1.5;padding-top:10px;padding-right:25px;padding-bottom:10px;padding-left:25px'>"
                + "<div style='line-height:1.5;font-size:12px;color:#000000;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif'>"
                + "<p style='margin:0;font-size:14px;line-height:1.5;word-break:break-word;margin-top:0;margin-bottom:0'>"
                + "<span style='color:#999999'>phone</span>"
                + "</p>"
                + "<span style='margin:0;font-size:16px;line-height:1.5;word-break:break-word;margin-top:0;margin-bottom:0;font-size:16px'>"
                + " " + contact.getPhone() + " "
                + "</span>"
                + "</div>"
                + "</div>"
                + "<div style='color:#000000;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif;line-height:1.5;padding-top:10px;padding-right:25px;padding-bottom:10px;padding-left:25px'>"
                + "<div style='line-height:1.5;font-size:12px;color:#000000;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif'>"
                + "<p style='margin:0;font-size:14px;line-height:1.5;word-break:break-word;margin-top:0;margin-bottom:0'>"
                + "<span style='color:#999999'>comments</span>"
                + "</p>"
                + "<span style='margin:0;font-size:16px;line-height:1.5;word-break:break-word;margin-top:0;margin-bottom:0;font-size:16px'>"
                + " " + contact.getComments() + " "
                + "</span>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "<div style='background-color:transparent'>"
                + "<div class='m_-2458848946273030031block-grid' style='min-width:320px;max-width:600px;word-wrap:break-word;word-break:break-word;Margin:0 auto;background-color:#ffffff'>"
                + "<div style='border-collapse:collapse;display:table;width:100%;background-color:#ffffff'>"
                + "<div class='m_-2458848946273030031col m_-2458848946273030031num8' style='display:table-cell;vertical-align:top;max-width:320px;min-width:400px;width:400px'>"
                + "<div class='m_-2458848946273030031col_cont' style='width:100%!important'>"
                + "<div style='border-top:0px solid transparent;border-left:0px solid transparent;border-bottom:0px solid transparent;border-right:0px solid transparent;padding-top:15px;padding-bottom:5px;padding-right:15px;padding-left:15px'>"
                + "<div style='color:#999;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif;line-height:1.2;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px'>"
                + "<div style='line-height:1.2;font-size:12px;color:#999;font-family:Open Sans,Helvetica Neue,Helvetica,Arial,sans-serif'>"
                + "<p style='margin:0;font-size:14px;line-height:1.2;word-break:break-word;margin-top:0;margin-bottom:0'>"
                + "Submitted " + formatDateTime
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</td>"
                + "</tr>"
                + "</tbody>"
                + "</table>"
                + "</div>"
                + "</div>"
                + "</div>"
        ;
        helper.setText(content, true);

        mailSender.send(message);
    }

}
