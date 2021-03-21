package capprezy.ua.repository;

import capprezy.ua.model.Dish;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends CrudRepository<Dish, Integer> {
    List<Dish> findAll(Pageable pageable);
    Dish findByUid(Integer uid);
    Dish findByNameIgnoreCase(String name);


    @Query(value = "select * from caprezzy._dish d " +
            "where ((?1) is null or d.uid in (select dc.dish_uid from caprezzy._dish_category dc " +
            "                                 where dc.category_uid in (?1))) " +
            "  and ((?2) is null or d.uid not in (select dc.dish_uid from caprezzy._dish_category dc " +
            "                                 where dc.category_uid in (?2))) " +
            "  and ((?3) is null or d.uid in (select di.dish_uid from caprezzy._dish_ingredient di " +
            "                                 where di.ingredient_uid in (?3))) " +
            "  and ((?4) is null or d.uid not in (select di.dish_uid from caprezzy._dish_ingredient di " +
            "                                 where di.ingredient_uid in (?4))) " +
            "  and ( ?5 <= 0 or d.price <= ?5  )" +
            "  and ( lower(d.name) like %?6% )",
            nativeQuery = true)
    List<Dish> findByCriteria(List<Integer> categoriesIn,
                              List<Integer> categoriesNotIn,
                              List<Integer> ingredientsIn,
                              List<Integer> ingredientsNotIn,
                              Double maxPrice,
                              String query,
                              Pageable pageable);



//    @Query("select d from _dish d where d.movie = :movie")
//    List<Dish> findBySorted(
//            @Param("name") String name, Sort sort);
}
