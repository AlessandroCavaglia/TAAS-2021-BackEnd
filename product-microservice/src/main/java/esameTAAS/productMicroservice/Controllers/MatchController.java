package esameTAAS.productMicroservice.Controllers;



import esameTAAS.productMicroservice.Models.Match;
import esameTAAS.productMicroservice.Repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MatchController {
    @Autowired
    private MatchRepository matchRepository;
    @GetMapping("/matches")    //TODO REMOVE, is just for testing purposes
    public List<Match> getAllMatches(){return matchRepository.findAll();}
}
