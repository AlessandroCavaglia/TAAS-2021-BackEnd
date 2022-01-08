package esameTAAS.productMicroservice.Controllers;

import esameTAAS.productMicroservice.Models.AccessToken;
import esameTAAS.productMicroservice.Repositories.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Token_Reciever {
    @Autowired
    private AccessTokenRepository accessTokenRepository;
    public void receiveMessage(String message) {
        String[] parameters=message.split(",");
        AccessToken accessToken = new AccessToken();
        accessToken.initAccessToken(parameters[0],parameters[1],parameters[2]);
        accessTokenRepository.save(accessToken);
    }
}