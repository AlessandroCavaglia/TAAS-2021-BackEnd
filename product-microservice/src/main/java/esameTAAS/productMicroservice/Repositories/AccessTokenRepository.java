package esameTAAS.productMicroservice.Repositories;

import esameTAAS.productMicroservice.Models.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccessTokenRepository extends JpaRepository<AccessToken,String> {
    @Query("SELECT T FROM AccessToken T WHERE T.username = :username ORDER BY T.expiring_date DESC") //TODO: aggiungere limit 1
    List<AccessToken> findLastAccessTokenByUsername(@Param("username") String username);

    @Query("SELECT T FROM AccessToken T WHERE T.token = :token")
    AccessToken findAccessTokenByToken(@Param("token") String token);




}
