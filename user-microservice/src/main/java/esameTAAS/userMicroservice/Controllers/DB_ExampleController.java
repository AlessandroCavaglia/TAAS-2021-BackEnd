package esameTAAS.userMicroservice.Controllers;

import esameTAAS.userMicroservice.Models.DB_Example;
import esameTAAS.userMicroservice.Repositories.DB_ExampleRepository;
import esameTAAS.userMicroservice.UserMicroServiceApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class DB_ExampleController {
    private final RabbitTemplate rabbitTemplate;

    public DB_ExampleController( RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Autowired
    private DB_ExampleRepository repo;

    @GetMapping("/all")
    public List<DB_Example> list(){
        return repo.findAll();
    }
    @GetMapping("/greeting")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity create() throws UnknownHostException {
        DB_Example tmp=new DB_Example();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        tmp.setText("Ricevuto richiesta alle "+dtf.format(now)+ "sul server "+ InetAddress.getLocalHost());
        repo.save(tmp);
        rabbitTemplate.convertAndSend(UserMicroServiceApplication.topicExchangeName, "token-exchange", "BingBong");
        return new ResponseEntity<>("Benvenuto",HttpStatus.OK);
    }
}
