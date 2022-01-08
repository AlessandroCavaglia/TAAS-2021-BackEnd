package esameTAAS.userMicroservice.Controllers;


import esameTAAS.userMicroservice.Models.AccessToken;
import esameTAAS.userMicroservice.Models.UserFB;
import esameTAAS.userMicroservice.Repositories.AccessTokenRepository;
import esameTAAS.userMicroservice.Repositories.UserFBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import esameTAAS.userMicroservice.Models.ResponseStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserFBController {
    @Autowired
    private UserFBRepository userFBRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @GetMapping("/users")
    public List<UserFB> list(){
        return userFBRepository.findAll();
    }

    @GetMapping("/user/token/{username}")
    public List<AccessToken> getLastToken(@PathVariable("username") String username){
        return accessTokenRepository.findLastAccessTokenByUsername(username);
    }

    @GetMapping("/token")
    public List<AccessToken> getToken(){
        return accessTokenRepository.findAll();
    }

    @GetMapping("/user/{value}")
    public UserFB userFromUsername(@PathVariable("value") String value){
       return userFBRepository.findUserFBByUsername(value);
    }

    @GetMapping("/login/{token}")
    public ResponseEntity<String> login(@PathVariable("token") String token) {
        UserFB userFB = new UserFB();
        AccessToken accessToken;
        String username = "ciccio";
        ResponseStatus result;
        result = UserFB.checkUsername(username);
        if (!result.equals(ResponseStatus.OK)) //Check username
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        try {
            accessToken = userFB.initUser(token, username);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ResponseStatus.ERROR_FACEBOOK.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (userFBRepository.findUserFBByMail(userFB.getEmail()) == null) { //Check if user already insert into DB
            if (userFBRepository.findUserFBByUsername(username) == null) { //Check if username is correct
                userFBRepository.save(userFB);
            } else {
                return new ResponseEntity(ResponseStatus.ERROR_FACEBOOK.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
        System.out.println(accessToken.toString());
        accessTokenRepository.save(accessToken);
        return new ResponseEntity<>(userFB.toString() + accessToken.toString(), HttpStatus.OK);
    }


}
