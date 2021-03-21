package capprezy.ua.service.impl;

import capprezy.ua.model.AppUser;
import capprezy.ua.model.Order;
import capprezy.ua.model.other.Mail;
import capprezy.ua.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("mailService")
@EnableAsync
public class DefaultMailService implements MailService {

    private static final String UTF_8 = "UTF-8";
    private static final String TEMPLATES_PATH = "/templates/";
    private static final String GREETING_TEMPLATE_NAME = "register-confirm.ftl";
    private static final String ORDER_CONFIRMED_TEMPLATE_NAME = "order-confirmed.ftl";

    @Value("${host.name}")
    private String hostName;

    @Value("${spring.mail.username}")
    private String sender;

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private Configuration getFreeMarkerConfiguration;

    @Override
    public void sendGreeting(AppUser receiver) {
        Mail mail = prepareMail(receiver.getMail(), "Greeting");
        Map<String, Object> model = new HashMap<>();
        model.put("login_url", "http://" + hostName + "#/login");
        model.put("name", receiver.getFullName());
        mail.setModel(model);
        sendEmail(mail, GREETING_TEMPLATE_NAME);
    }

    @Override
    public void sendOrder(Order order) {
        Mail mail = prepareMail(order.getClient().getMail(), "Order #" + order.getUid());
        Map<String, Object> model = new HashMap<>();
        model.put("url", "http://" + hostName + "#/order/" + order.getUid());
        model.put("name", order.getClient().getFullName());
        mail.setModel(model);
        sendEmail(mail, ORDER_CONFIRMED_TEMPLATE_NAME);
    }

    @Async
    public void sendEmail(Mail mail, String template) {
        MimeMessagePreparator messagePreparator = mimeMessage -> prepareMimeMessage(template, mail, mimeMessage);
        try {
            javaMailSender.send(messagePreparator);

        } catch (MailException e) {
            throw new MailSendException(e.getMessage());
        }
    }

    private void prepareMimeMessage(String templateName, Mail mail, MimeMessage mimeMessage)
            throws IOException, TemplateException, MessagingException {

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, UTF_8);

        getFreeMarkerConfiguration.setClassForTemplateLoading(this.getClass(), TEMPLATES_PATH);
        Template template = getFreeMarkerConfiguration.getTemplate(templateName, UTF_8);
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());

        helper.setFrom(mail.getMailFrom());
        helper.setTo(mail.getMailTo());
        helper.setText(text, true);
        helper.setSubject(mail.getMailSubject());
    }



    private Mail prepareMail(String email, String subject) {
        Mail mail = new Mail();
        mail.setMailFrom(sender);
        mail.setMailTo(email);
        mail.setMailSubject(subject);
        return mail;
    }
}
