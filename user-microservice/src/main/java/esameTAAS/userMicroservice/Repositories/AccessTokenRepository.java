package esameTAAS.userMicroservice.Repositories;

import esameTAAS.userMicroservice.Models.AccessToken;
import esameTAAS.userMicroservice.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccessTokenRepository extends JpaRepository<AccessToken,String> {
    @Query("SELECT T FROM AccessToken T WHERE T.username = :username ORDER BY T.expiring_date DESC") //TODO: aggiungere limit 1
    List<AccessToken> findLastAccessTokenByUsername(@Param("username") String username);

    @Query("SELECT T FROM AccessToken T WHERE T.token = :token")
    AccessToken findAccessTokenByToken(@Param("token") String token);

    @Query("SELECT U FROM User U JOIN AccessToken T ON U.username = T.username WHERE T.token = :token")
    User findUserByToken(@Param("token") String token);




}
