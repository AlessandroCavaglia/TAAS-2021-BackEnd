package esameTAAS.productMicroservice.Repositories;


import esameTAAS.productMicroservice.Models.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action,Long> {
    @Query("SELECT A FROM Action  A, Product P,Product P2 WHERE P.id=:productId AND A.username=P.username AND A.username <> :username AND A.product_id=P2.id AND P2.username=:username AND A.like_action=true")
    public Optional<Action> getMatchingAction(@Param("productId") Long productId, @Param("username") String username);
}
