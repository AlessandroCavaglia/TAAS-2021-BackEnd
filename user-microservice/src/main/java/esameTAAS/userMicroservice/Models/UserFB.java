package esameTAAS.userMicroservice.Models;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserFB {
    @Id
    private String username;
    private String firstName;
    private String lastName;
    private String birthday;

    private String email;


    public AccessToken initUser(String token, String username) throws Exception{
        AccessToken accessToken = new AccessToken();
        FacebookClient fb = new DefaultFacebookClient(token);
        accessToken.initAccessToken(token,username,fb.debugToken(token).getExpiresAt().toString());
        User user = fb.fetchObject("me", User.class,  Parameter.with("fields", "id,email,name,birthday,last_name,first_name"));
        this.username = username;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        return  accessToken;

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

    public String getName() {
        return firstName;
    }


    public String getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UserFB{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}