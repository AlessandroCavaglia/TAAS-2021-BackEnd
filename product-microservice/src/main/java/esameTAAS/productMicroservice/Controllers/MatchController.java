package esameTAAS.productMicroservice.Controllers;



import esameTAAS.productMicroservice.Models.AccessToken;
import esameTAAS.productMicroservice.Models.Match;
import esameTAAS.productMicroservice.Repositories.AccessTokenRepository;
import esameTAAS.productMicroservice.Repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class MatchController {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @GetMapping("/matches")    //TODO REMOVE, is just for testing purposes
    public List<Match> getAllMatches(){return matchRepository.findAll();}
    @GetMapping("/tokens")    //TODO REMOVE, is just for testing purposes
    public List<AccessToken> getAllTokens(){return accessTokenRepository.findAll();}
}
