package capprezy.ua.repository;

import capprezy.ua.model.Order;
import capprezy.ua.model.Portion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.sound.sampled.Port;
import java.util.List;

@Repository
public interface PortionRepository extends CrudRepository<Portion, Integer> {
    List<Portion> findAll();
}
