package esameTAAS.userMicroservice.Controllers;


import esameTAAS.userMicroservice.Models.AccessToken;
import esameTAAS.userMicroservice.Models.Comunication.FacebookInfoUser;
import esameTAAS.userMicroservice.Models.UserFB;
import esameTAAS.userMicroservice.Repositories.AccessTokenRepository;
import esameTAAS.userMicroservice.Repositories.UserFBRepository;
import esameTAAS.userMicroservice.UserMicroServiceApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import esameTAAS.userMicroservice.Models.ResponseStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class UserFBController {
    private final RabbitTemplate rabbitTemplate;

    public UserFBController( RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Autowired
    private UserFBRepository userFBRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @GetMapping("/users")   //TODO testing api
    public List<UserFB> list(){
        return userFBRepository.findAll();
    }

    @GetMapping("/user/token/{username}") //TODO testing api
    public List<AccessToken> getLastToken(@PathVariable("username") String username){
        return accessTokenRepository.findLastAccessTokenByUsername(username);
    }

    @GetMapping("/token")   //TODO testing api
    public List<AccessToken> getToken(){
        return accessTokenRepository.findAll();
    }

    @GetMapping("/user/{value}")
    public UserFB userFromUsername(@PathVariable("value") String value){
       return userFBRepository.findUserFBByUsername(value);
    }

    @PostMapping("/loginFB")
    public ResponseEntity<String> login(@RequestBody FacebookInfoUser facebookInfoUser) {
        UserFB userFB = new UserFB();
        AccessToken accessToken;
        ResponseStatus result;
        result = UserFB.checkUsername(facebookInfoUser.getUsername());
        if (!result.equals(ResponseStatus.OK)) //Check username
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        try {
            accessToken = userFB.initUserFB(facebookInfoUser.getToken(), facebookInfoUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ResponseStatus.ERROR_FACEBOOK.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (userFBRepository.findUserFBByMail(userFB.getEmail()) == null) { //Check if user already insert into DB
            if (userFBRepository.findUserFBByUsername(facebookInfoUser.getUsername()) == null) { //Check if username is correct
                userFBRepository.save(userFB);
            } else {
                return new ResponseEntity(ResponseStatus.ERROR_FACEBOOK.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if(accessTokenRepository.findAccessTokenByToken(facebookInfoUser.getToken()) == null) //Check if the token already exist
            accessTokenRepository.save(accessToken);
        rabbitTemplate.convertAndSend(UserMicroServiceApplication.topicExchangeName, "token-exchange", accessToken.serialize());
        return new ResponseEntity<>(userFB.toString() + accessToken.toString(), HttpStatus.OK);
    }


}
