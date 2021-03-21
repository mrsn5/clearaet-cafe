package capprezy.ua.repository;

import capprezy.ua.model.AppUser;
import capprezy.ua.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    Order findByUid(Integer id);
    List<Order> findAll(Pageable pageable);

//    @Query(value = "select * from caprezzy._order o " +
//            "where ((?1) is null or o.order_state in (?1)) ",
//            nativeQuery = true)
    List<Order> findByOrderStateIn(List<Order.OrderStateType> orderState, Pageable pageable);


    List<Order> findAllByClient(AppUser user, Pageable pageable);
    List<Order> findAllByClientAndOrderStateIn(AppUser user, List<Order.OrderStateType> orderState, Pageable pageable);

    long countAllByOrderStateIn(List<Order.OrderStateType> orderState);
    long count();

    long countMyByOrderStateInAndClient(List<Order.OrderStateType> orderState, AppUser user);
    long countByClient(AppUser user);

    Order findByPaymentId(String paymentId);
}
