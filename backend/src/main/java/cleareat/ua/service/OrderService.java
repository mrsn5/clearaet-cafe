package capprezy.ua.service;

import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.controller.exception.model.NotValidDataException;
import capprezy.ua.controller.exception.model.PermissionException;
import capprezy.ua.model.AppUser;
import capprezy.ua.model.Category;
import capprezy.ua.model.Order;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {
    List<Order> getAll(Pageable pageable);
    Order add(Order order) throws PermissionException, NotValidDataException, AlreadyExistsException;

    List<Order> getAll(Order.OrderStateType[] orderState, Pageable pageable);

    Order findById(Integer id);

    Order update(Order order) throws NotValidDataException;

    long getAllCount(Order.OrderStateType[] orderStates);
    long getMyCount(Order.OrderStateType[] orderStates) throws PermissionException;


    List<Order> getMy(Order.OrderStateType[] orderStates, Pageable pageable) throws PermissionException;

    void delete(Integer id);
}
