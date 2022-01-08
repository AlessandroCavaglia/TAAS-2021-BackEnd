package esameTAAS.productMicroservice.Controllers;

import esameTAAS.productMicroservice.Models.AccessToken;
import esameTAAS.productMicroservice.Repositories.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TokenController {

    public String  getUsernameFromToken(String token,AccessTokenRepository repository){
        String username=null;
        AccessToken accesToken = repository.findAccessTokenByToken(token);
        if(accesToken!=null) {
            try{
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date parsedDate = dateFormat.parse(accesToken.getExpiring_date());
                if(parsedDate.after(new Date(System.currentTimeMillis()))){
                    username=accesToken.getUsername();
                }
            }catch (ParseException ex){
                accesToken=null;
            }

        }
        return username;
    }
}
