package capprezy.ua.service.impl;

import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.controller.exception.model.NotValidDataException;
import capprezy.ua.controller.exception.model.PermissionException;
import capprezy.ua.model.AppUser;
import capprezy.ua.model.Dish;
import capprezy.ua.model.Order;
import capprezy.ua.model.Portion;
import capprezy.ua.repository.DishRepository;
import capprezy.ua.repository.OrderRepository;
import capprezy.ua.repository.PortionRepository;
import capprezy.ua.service.AppUserService;
import capprezy.ua.service.MailService;
import capprezy.ua.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service("orderService")
public class DefaultOrderService implements OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private DishRepository dishRepository;
    @Autowired private AppUserService appUserService;
    @Autowired private PortionRepository portionRepository;
    @Autowired private MailService mailService;

    @Override
    public List<Order> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order add(Order order) throws PermissionException, NotValidDataException, AlreadyExistsException {
        AppUser user = appUserService.getCurrentUser();
        if (user == null) throw PermissionException.createWith("You have to be logged in");

        Double total = .0;
        for (Portion p: order.getPortions()) {
            Dish d = dishRepository.findByUid(p.getDish().getUid());
            if (d == null) throw NotValidDataException.createWith("No such dish");
            Double price = p.getQuantity() * d.getPrice();
            p.setPrice(price);
            total += price;
        }

        order.setClient(user);
        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        for (Portion p: savedOrder.getPortions()) {
            p.setOrder(savedOrder);
            portionRepository.save(p);
        }
        return savedOrder;
    }

    @Override
    public List<Order> getAll(Order.OrderStateType[] orderState, Pageable pageable) {
        if (orderState == null || orderState.length == 0) return orderRepository.findAll(pageable);
        return orderRepository.findByOrderStateIn(
                Arrays.asList(orderState),
                pageable);
    }

    @Override
    public Order findById(Integer id) {
        return orderRepository.findByUid(id);
    }

    @Override
    public Order update(Order order) throws NotValidDataException {
        Order updatedOrder = orderRepository.findByUid(order.getUid());
        if (updatedOrder == null) throw NotValidDataException.createWith("Order with uid " + order.getUid() + " doesn't exsist");
        Order.OrderStateType orderState = order.getOrderState();
        if (orderState != null) {
            updatedOrder.setOrderState(order.getOrderState());

            if (orderState == Order.OrderStateType.confirmed) {
                mailService.sendOrder(updatedOrder);
            }

            if (orderState == Order.OrderStateType.ready) {
                updatedOrder.setReadyTime(new Timestamp(System.currentTimeMillis()));
            }

            if (orderState == Order.OrderStateType.took_away) {
                updatedOrder.setPaid(updatedOrder.getTotal());
                updatedOrder.setPaymentState(Order.PaymentStateType.fully_paid);
            }
        }

        if (order.getPreferences() != null) {
            updatedOrder.setPreferences(order.getPreferences());
        }

        if(order.getPortions() != null && !order.getPortions().isEmpty()) {
            portionRepository.deleteAll(updatedOrder.getPortions());

            updatedOrder.getPortions().clear();
            Double total = 0.0;
            for (Portion p: order.getPortions()) {
                p.setOrder(order);

                Dish d = dishRepository.findByUid(p.getDish().getUid());
                if (d == null) throw NotValidDataException.createWith("No such dish");
                Double price = p.getQuantity() * d.getPrice();
                p.setPrice(price);
                total += price;
                updatedOrder.getPortions().add(portionRepository.save(p));
            }
            updatedOrder.setTotal(total);
            updatedOrder.setPaymentState(total == updatedOrder.getPaid() ? Order.PaymentStateType.fully_paid : (updatedOrder.getPaid() != 0.0 ? Order.PaymentStateType.part_paid : Order.PaymentStateType.not_paid));
        }

        if (order.getPrefTime() != null) {
            updatedOrder.setPrefTime(order.getPrefTime());
        }

        return orderRepository.save(updatedOrder);
    }

    @Override
    public long getAllCount(Order.OrderStateType[] orderStates) {
        if (orderStates == null) return orderRepository.count();
        return orderRepository.countAllByOrderStateIn(Arrays.asList(orderStates));
    }

    @Override
    public long getMyCount(Order.OrderStateType[] orderStates) throws PermissionException {
        AppUser user = appUserService.getCurrentUser();
        if (user == null) throw PermissionException.createWith("You have to be logged in");

        if (orderStates == null) return orderRepository.countByClient(user);
        return orderRepository.countMyByOrderStateInAndClient(Arrays.asList(orderStates), user);
    }

    @Override
    public List<Order> getMy(Order.OrderStateType[] orderStates, Pageable pageable) throws PermissionException {
        AppUser user = appUserService.getCurrentUser();
        if (user == null) throw PermissionException.createWith("You have to be logged in");

        if (orderStates == null || orderStates.length == 0) return orderRepository.findAllByClient(user, pageable);
        return orderRepository.findAllByClientAndOrderStateIn(user, Arrays.asList(orderStates), pageable);
    }

    @Override
    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }
}
