package esameTAAS.userMicroservice.Repositories;

import esameTAAS.userMicroservice.Models.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFBRepository extends JpaRepository<PlatformUser,String> {

    PlatformUser findUserByEmail(String email);

    PlatformUser findUserByUsername(String username);

}
