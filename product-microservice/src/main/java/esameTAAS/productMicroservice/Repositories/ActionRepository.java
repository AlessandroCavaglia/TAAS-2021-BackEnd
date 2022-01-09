package esameTAAS.productMicroservice.Repositories;


import esameTAAS.productMicroservice.Models.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action,Long> {
    @Query("Select A From Action A where A.username IN (SELECT P.username From Product P where  P.id = :productId) AND A.like_action=true AND A.product_id IN" +
            "(Select Pall.id  From Product Pall where Pall.username = :username) ")
    public Optional<List<Action>> getMatchingAction(@Param("productId") Long productId, @Param("username") String username);
}
