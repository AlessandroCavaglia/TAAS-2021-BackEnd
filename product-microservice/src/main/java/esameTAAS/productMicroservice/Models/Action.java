package esameTAAS.productMicroservice.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import esameTAAS.productMicroservice.Models.Comunication.ResponseStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"}) //Annulla il carciamento lazy dei dati
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private boolean like_action;
    private String timestamp;
    private Long product_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLike_action() {
        return like_action;
    }

    public void setLike_action(boolean like) {
        this.like_action = like;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public static ResponseStatus checkNewAction(Action a){
        if (a.getId()!=null || a.getUsername()!=null){
            return ResponseStatus.BAD_REQUEST_NOT_A_NEW_ACTION;
        }
        //Check date validity
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(a.getTimestamp());
        } catch (ParseException e) {
            return ResponseStatus.BAD_REQUEST_IVALID_ACTION;
        }
        return ResponseStatus.OK;
    }
}
