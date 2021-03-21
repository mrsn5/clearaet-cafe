package capprezy.ua.service;

import capprezy.ua.model.AppUser;
import capprezy.ua.model.Order;

public interface MailService {
    void sendGreeting(AppUser receiver);
    void sendOrder(Order order);
}
