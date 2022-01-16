package esameTAAS.userMicroservice.Models;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PlatformUser {
    @Id
    private String username;
    private String firstName;
    private String lastName;
    private String birthday;
    private String password;
    private String email;


    public AccessToken initUserFB(String token, String username) throws Exception{
        AccessToken accessToken = new AccessToken();
        FacebookClient fb = new DefaultFacebookClient(token);
        accessToken.initAccessToken(token,username,fb.debugToken(token).getExpiresAt().toString());
        com.restfb.types.User user = fb.fetchObject("me", com.restfb.types.User.class,  Parameter.with("fields", "id,email,name,birthday,last_name,first_name"));
        this.username = username;
        this.password = null;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        return  accessToken;

    }

    public static String getMailFromToken(String token) throws Exception{
        FacebookClient fb = new DefaultFacebookClient(token);
        com.restfb.types.User user = fb.fetchObject("me", com.restfb.types.User.class,  Parameter.with("fields", "id,email,name,birthday,last_name,first_name"));
        return user.getEmail();
    }


    public static ResponseStatus checkUsername(String username){
        if(username==null){
            return ResponseStatus.BAD_REQUEST_USERNAME_IS_INVALID;
        }
        if(!username.matches("^[a-zA-Z0-9._-]{3,}$")){
            return ResponseStatus.BAD_REQUEST_USERNAME_IS_INVALID;
        }
        return ResponseStatus.OK;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserFB{" +
                "\"username\": \"" + username + '\"' +
                ", \"firstName\": \"" + firstName + '\"' +
                ", \"lastName\" : \"" + lastName + '\"' +
                ", \"birthday\" : \"" + birthday + '\"' +
                ", \"email\" : \"" + email + '\"' +
                '}';
    }
}