package esameTAAS.productMicroservice.Repositories;


import esameTAAS.productMicroservice.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findProductByIdAndUsername(long id,String username);
    @Query("SELECT P FROM Product P WHERE P.username <> :username AND (P.categories LIKE :category) and (P.latitude >= :minLat and P.latitude <= :maxLat and P.longitude >= :minLong and P.longitude <= :maxLong) AND P.enabled = true AND P.id NOT IN (" +
            "SELECT distinct A.product_id FROM Action A WHERE A.username=:username)")
    List<Product> findProducts(@Param("category") String category, @Param("minLat") float minLat, @Param("maxLat") float maxLat, @Param("minLong")float minLong, @Param("maxLong") float maxLong,@Param("username")String username);
    @Query("SELECT P FROM Product P WHERE P.username <> :username AND (P.categories LIKE :category1 OR P.categories LIKE :category2) and P.latitude>=:minLat and P.latitude <=:maxLat and P.longitude>=:minLong and P.longitude <=:maxLong AND P.enabled = true AND P.id NOT IN (" +
            "SELECT distinct A.product_id FROM Action A WHERE A.username=:username)")
    List<Product> findProducts(@Param("category1") String category1,@Param("category2") String category2, @Param("minLat") float minLat, @Param("maxLat") float maxLat, @Param("minLong")float minLong, @Param("maxLong") float maxLong,@Param("username")String username);
    @Query("SELECT P FROM Product P WHERE P.username <> :username AND (P.categories LIKE :category1 OR P.categories LIKE :category2 OR P.categories LIKE :category3) and P.latitude>=:minLat and P.latitude <=:maxLat and P.longitude>=:minLong and P.longitude <=:maxLong AND P.enabled = true AND P.id NOT IN (" +
            "SELECT distinct A.product_id FROM Action A WHERE A.username=:username)")
    List<Product> findProducts(@Param("category1") String category1,@Param("category2") String category2,@Param("category3") String category3, @Param("minLat") float minLat, @Param("maxLat") float maxLat, @Param("minLong")float minLong, @Param("maxLong") float maxLong,@Param("username")String username);
    boolean existsProductByIdAndEnabledIsTrueAndUsernameNot(long id,String ownerUsername);
    List<Product> getProductByUsernameAndEnabledIsTrue(String ownerUsername);
}
