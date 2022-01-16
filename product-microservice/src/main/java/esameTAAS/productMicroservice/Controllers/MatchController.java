package esameTAAS.productMicroservice.Controllers;



import esameTAAS.productMicroservice.Models.AccessToken;
import esameTAAS.productMicroservice.Models.Comunication.ResponseStatus;
import esameTAAS.productMicroservice.Models.Match;
import esameTAAS.productMicroservice.Models.Product;
import esameTAAS.productMicroservice.Repositories.AccessTokenRepository;
import esameTAAS.productMicroservice.Repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
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
    @GetMapping("/matches/my-matches")
    public ResponseEntity getMyMatches(@RequestHeader("access-token") String token){
        ResponseStatus result;
        TokenController tokenController = new TokenController();
        String username=tokenController.getUsernameFromToken(token,accessTokenRepository);
        if(username==null){
            result= ResponseStatus.UNAUTHORIZED;
            return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
        }
        List<Match> resultList=matchRepository.getMatchByUsername1(username);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }
    @PutMapping("/matches/edit-match")
    public ResponseEntity editMatch(@RequestBody Match newMatch, @RequestHeader("access-token") String token){
        ResponseStatus result;
        TokenController tokenController = new TokenController();
        String username=tokenController.getUsernameFromToken(token,accessTokenRepository);
        if(username==null){
            result= ResponseStatus.UNAUTHORIZED;
            return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
        }
        if(newMatch.getUsername1().equals(username) || newMatch.getUsername2().equals(username)){
            matchRepository.save(newMatch);
            result=ResponseStatus.OK;
        }else{
            result= ResponseStatus.UNAUTHORIZED;
            return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
        }
        return new ResponseEntity<>(result.defaultDescription, HttpStatus.OK);
    }
}
