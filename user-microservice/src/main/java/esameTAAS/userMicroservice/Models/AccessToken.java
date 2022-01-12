package esameTAAS.userMicroservice.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccessToken {

    @Id
    @Column(length = 500)
    private String token;
    private String username;
    private String expiring_date;

    public void initAccessToken(String token, String username, String expiring_date) {
        this.token = token;
        this.username = username;
        this.expiring_date = expiring_date;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getExpiring_date() {
        return expiring_date;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "\"token\" : \"" + token + '\"' +
                ", \"username\" : \"" + username + '\"' +
                ", \"expiring_date\" : \"" + expiring_date + '\"' +
                '}';

    }
    public String serialize(){
        return token+","+username+","+expiring_date;
    }
}
