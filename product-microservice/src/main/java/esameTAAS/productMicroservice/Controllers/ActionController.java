package esameTAAS.productMicroservice.Controllers;


import esameTAAS.productMicroservice.Models.Action;
import esameTAAS.productMicroservice.Models.Comunication.ResponseStatus;
import esameTAAS.productMicroservice.Models.Match;
import esameTAAS.productMicroservice.Repositories.AccessTokenRepository;
import esameTAAS.productMicroservice.Repositories.ActionRepository;
import esameTAAS.productMicroservice.Repositories.MatchRepository;
import esameTAAS.productMicroservice.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class ActionController {
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @GetMapping("/actions/all")    //TODO REMOVE, is just for testing purposes
    public List<Action> getAllActions(){return actionRepository.findAll();}

    @PostMapping("/actions/create")
    public ResponseEntity createAction(@RequestBody Action action,@RequestHeader("access-token") String token){
        ResponseStatus result=Action.checkNewAction(action);
        TokenController tokenController = new TokenController();
        String username=tokenController.getUsernameFromToken(token,accessTokenRepository);
        if(username==null){
            result= ResponseStatus.UNAUTHORIZED;
            return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
        }
        action.setUsername(username);
        if(result.equals(ResponseStatus.OK)){
            if(productRepository.existsProductByIdAndEnabledIsTrueAndUsernameNot(action.getProduct_id(),action.getUsername())){
                actionRepository.save(action);
                if(action.isLike_action()){
                    Optional<List<Action>> matchingActions=actionRepository.getMatchingAction(action.getProduct_id(),action.getUsername());
                    if(matchingActions.isPresent()){
                        Match m=new Match();
                        m.setAction1(action.getId());
                        m.setAction2(matchingActions.get().get(0).getId());
                        m.setEnabled(true);
                        m.setUsername1(action.getUsername());
                        m.setUsername2(matchingActions.get().get(0).getUsername());
                        matchRepository.save(m);
                    }
                }

            }else{
                result=ResponseStatus.NOT_FOUND;
            }
        }
        return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
    }

}
