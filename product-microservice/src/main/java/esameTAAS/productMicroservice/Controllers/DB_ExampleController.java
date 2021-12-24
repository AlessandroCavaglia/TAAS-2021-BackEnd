package esameTAAS.productMicroservice.Controllers;

import esameTAAS.productMicroservice.Models.DB_Example;
import esameTAAS.productMicroservice.Repositories.DB_ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class DB_ExampleController {
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
        return new ResponseEntity<>("Benvenuto",HttpStatus.OK);
    }
}
