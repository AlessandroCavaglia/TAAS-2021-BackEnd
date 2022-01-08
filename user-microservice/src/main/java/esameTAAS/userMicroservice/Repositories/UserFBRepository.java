package esameTAAS.userMicroservice.Repositories;

import esameTAAS.userMicroservice.Models.UserFB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserFBRepository extends JpaRepository<UserFB,String> {
    @Query("SELECT U FROM UserFB U WHERE U.email = :email")
    UserFB findUserFBByMail(@Param("email") String email);

    @Query("SELECT U FROM UserFB U WHERE U.username = :username")
    UserFB findUserFBByUsername(@Param("username") String username);

}
