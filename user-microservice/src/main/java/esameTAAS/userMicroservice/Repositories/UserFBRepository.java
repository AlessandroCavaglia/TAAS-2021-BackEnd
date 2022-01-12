package esameTAAS.userMicroservice.Repositories;

import esameTAAS.userMicroservice.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserFBRepository extends JpaRepository<User,String> {
    @Query("SELECT U FROM User U WHERE U.email = :email")
    User findUserFBByMail(@Param("email") String email);

    @Query("SELECT U FROM User U WHERE U.username = :username")
    User findUserFBByUsername(@Param("username") String username);

}
