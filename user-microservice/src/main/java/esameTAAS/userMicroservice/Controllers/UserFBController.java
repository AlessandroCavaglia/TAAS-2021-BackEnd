package esameTAAS.userMicroservice.Controllers;


import esameTAAS.userMicroservice.Models.AccessToken;
import esameTAAS.userMicroservice.Models.Comunication.BasicInfoUser;
import esameTAAS.userMicroservice.Models.Comunication.FacebookInfoUser;
import esameTAAS.userMicroservice.Models.User;
import esameTAAS.userMicroservice.Repositories.AccessTokenRepository;
import esameTAAS.userMicroservice.Repositories.UserFBRepository;
import esameTAAS.userMicroservice.UserMicroServiceApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import esameTAAS.userMicroservice.Models.ResponseStatus;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
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
    public List<User> list(){
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
    public User userFromUsername(@PathVariable("value") String value){
       return userFBRepository.findUserFBByUsername(value);
    }

    @PostMapping("/user")
    public ResponseEntity<String> getUser(@RequestBody String token) {
        User user = accessTokenRepository.findUserByToken(token);
        AccessToken accessToken;
        Date parsedDate = null;
        if(user == null){
            return new ResponseEntity<>(ResponseStatus.BAD_REQUEST_USER_NOT_EXIST.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        accessToken = accessTokenRepository.findAccessTokenByToken(token);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            parsedDate = dateFormat.parse(accessToken.getExpiring_date());
            if(parsedDate.after(new Date(System.currentTimeMillis()))){
                return new ResponseEntity<>(user.toString() + accessToken.toString(), HttpStatus.OK);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(ResponseStatus.UNAUTHORIZED_TOKEN_EXPIRED.defaultDescription, HttpStatus.OK);
    }

    @PostMapping("/loginFB")
    public ResponseEntity<String> loginFB(@RequestBody FacebookInfoUser facebookInfoUser) {
        User user = new User();
        AccessToken accessToken;
        ResponseStatus result;
        result = User.checkUsername(facebookInfoUser.getUsername());
        if (!result.equals(ResponseStatus.OK)) //Check username
            return new ResponseEntity<>(result.defaultDescription, HttpStatus.BAD_REQUEST);
        try {
            accessToken = user.initUserFB(facebookInfoUser.getToken(), facebookInfoUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResponseStatus.ERROR_FACEBOOK.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (userFBRepository.findUserFBByMail(user.getEmail()) == null) { //Check if user already insert into DB
            if (userFBRepository.findUserFBByUsername(facebookInfoUser.getUsername()) == null) { //Check if username is correct
                userFBRepository.save(user);
            } else {
                return new ResponseEntity<>(ResponseStatus.ERROR_FACEBOOK.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
        if(accessTokenRepository.findAccessTokenByToken(facebookInfoUser.getToken()) == null) //Check if the token already exist
            accessTokenRepository.save(accessToken);
        rabbitTemplate.convertAndSend(UserMicroServiceApplication.topicExchangeName, "token-exchange", accessToken.serialize());
        return new ResponseEntity<>(user.toString() + accessToken.toString(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody BasicInfoUser basicInfoUser) {
        if (userFBRepository.findUserFBByUsername(basicInfoUser.getUsername()) == null) { //Check if user exist
            return new ResponseEntity<>(ResponseStatus.BAD_REQUEST_USER_NOT_EXIST.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user = userFBRepository.findUserFBByUsername(basicInfoUser.getUsername());
        if(Objects.equals(user.getPassword(), basicInfoUser.getPassword())){
            AccessToken accessToken = new AccessToken();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH); //Declare date format
            Calendar expiringDate = Calendar.getInstance();
            expiringDate.setTime(new Date());
            if(basicInfoUser.getRemember_me())
                expiringDate.add(Calendar.DATE, 30); //set the expiring Date
            else
                expiringDate.add(Calendar.DATE, 1); //set the expiring Date
            accessToken.initAccessToken(UUID.randomUUID().toString(),user.getUsername(),dateFormat.format(expiringDate.getTime())); // Init Access token
            if(accessTokenRepository.findAccessTokenByToken(accessToken.getToken()) == null) //Check if the token already exist
                accessTokenRepository.save(accessToken);
            rabbitTemplate.convertAndSend(UserMicroServiceApplication.topicExchangeName, "token-exchange", accessToken.serialize());
            return new ResponseEntity<>(user.toString() + accessToken.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseStatus.BAD_REQUEST_WRONG_PASSWORD.defaultDescription, HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        ResponseStatus result = User.checkUsername(user.getUsername());
        if (!result.equals(ResponseStatus.OK)) //Check username
            return new ResponseEntity<>(result.defaultDescription, HttpStatus.BAD_REQUEST);
        if (userFBRepository.findUserFBByMail(user.getEmail()) == null) { //Check if user already insert into DB
            if (userFBRepository.findUserFBByUsername(user.getUsername()) == null) { //Check if username is correct
                userFBRepository.save(user);
                login(new BasicInfoUser(user.getUsername(),user.getPassword(),false));
            } else {
                return new ResponseEntity<>(ResponseStatus.ERROR_FACEBOOK.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
        return new ResponseEntity<>(ResponseStatus.ERROR_REGISTER.defaultDescription, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    }
