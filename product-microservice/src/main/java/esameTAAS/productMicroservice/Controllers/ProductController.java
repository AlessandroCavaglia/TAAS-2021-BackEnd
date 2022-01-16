package esameTAAS.productMicroservice.Controllers;

import esameTAAS.productMicroservice.Models.Comunication.Filter;
import esameTAAS.productMicroservice.Models.Comunication.ResponseStatus;
import esameTAAS.productMicroservice.Models.Product;
import esameTAAS.productMicroservice.Repositories.AccessTokenRepository;
import esameTAAS.productMicroservice.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")    //TODO TEsting to remove
    public List<Product> list(){
        return productRepository.findAll();
    }

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @GetMapping("products/productImages")
    public ResponseEntity productImages(@RequestBody Product prodId){
        Optional<Product> result=productRepository.findById(prodId.getId());
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("null", HttpStatus.NOT_FOUND);
        }
    }
    //TODO Choose if enhance this function
    @PostMapping("/products/filter")
    public ResponseEntity filterList(@RequestBody Filter f,@RequestHeader("access-token") String token){
        ResponseStatus result=Filter.checkFilter(f);
        TokenController tokenController = new TokenController();
        String username=tokenController.getUsernameFromToken(token,accessTokenRepository);
        if(username==null){
           username="$";
        }
        System.out.println(username);
        if(result.equals(ResponseStatus.OK)){
            return new ResponseEntity(getProductsFromFilter(f,username),result.httpStatus);
        }
        return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
    }
    @GetMapping("/products/filter_no_img")      //Filter function but the result items don't contain the imgs to enable async loading of heavy images
    public ResponseEntity filterListNoImg(@RequestBody Filter f,@RequestHeader("access-token") String token){
        ResponseStatus result= Filter.checkFilter(f);
        TokenController tokenController = new TokenController();
        String username=tokenController.getUsernameFromToken(token,accessTokenRepository);
        if(username==null){
            username="$";
        }
        if(result.equals(ResponseStatus.OK)){
            List<Product> resultList=getProductsFromFilter(f,username);
            for (Product p:
                 resultList) {
                p.setImage1(null);
                p.setImage2(null);
                p.setImage3(null);
            }
            return new ResponseEntity(resultList,result.httpStatus);
        }
        return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
    }

    @PostMapping("products/create")
    public ResponseEntity create(@RequestBody Product product,@RequestHeader("access-token") String token){
        ResponseStatus result;
        TokenController tokenController = new TokenController();
        String username=tokenController.getUsernameFromToken(token,accessTokenRepository);
        if(username==null){
            result= ResponseStatus.UNAUTHORIZED;
            return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
        }
        result=Product.checkProduct(product);
        if(result.equals(ResponseStatus.OK)) {
            result=Product.checkNewProduct(product);
            if(result.equals(ResponseStatus.OK)){
                product.setUsername(username);
                productRepository.save(Product.sanitize_product(product));
            }
        }
        return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
    }
    @PutMapping("products/edit")
    public ResponseEntity edit(@RequestBody Product newProduct,@RequestHeader("access-token") String token){
        ResponseStatus result;
        TokenController tokenController = new TokenController();
        String username=tokenController.getUsernameFromToken(token,accessTokenRepository);
        if(username==null){
            result= ResponseStatus.UNAUTHORIZED;
            return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
        }
        result=Product.checkProduct(newProduct);
        if(result.equals(ResponseStatus.OK)) {
            result=Product.checkExistingProduct(newProduct);
            if(result.equals(ResponseStatus.OK)){
                if(newProduct.getUsername().equals(username)){
                    Optional<Product> oldProduct = productRepository.findById(newProduct.getId());
                    if (oldProduct.isPresent()){
                        if(oldProduct.get().getUsername().equals(newProduct.getUsername()))
                            productRepository.save(newProduct);
                    }else{
                        result= ResponseStatus.NOT_FOUND;
                    }
                }else{
                    result=ResponseStatus.UNAUTHORIZED;
                }
            }
        }
        return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
    }
    @GetMapping("products/myProducts")
    private ResponseEntity getUserProducts(@RequestHeader("access-token") String token){
        ResponseStatus result;
        TokenController tokenController = new TokenController();
        String username=tokenController.getUsernameFromToken(token,accessTokenRepository);
        if(username==null){
            return new ResponseEntity("",HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(productRepository.getProductByUsernameAndEnabledIsTrue(username),HttpStatus.OK);
    }

    private List<Product> getProductsFromFilter(Filter f,String username){
        List<Product> resultList;
        Float minLatitude,maxLatitude,minLongitude,maxLongitude;
        minLatitude=f.getLatitudePoint() - (f.getRadius()/6378) * (float)(180 / Math.PI);
        maxLatitude=f.getLatitudePoint() + (f.getRadius()/6378) * (float)(180 / Math.PI);
        minLongitude=f.getLongitudePoint() - (f.getRadius()/6378) * (float)(180 / Math.PI) / (f.getLatitudePoint()*(float)(Math.PI/180));
        maxLongitude=f.getLongitudePoint() + (f.getRadius()/6378) * (float)(180 / Math.PI) / (f.getLatitudePoint()*(float)(Math.PI/180));
        switch (f.getCategoryNumber()){
            case 1:
                resultList=productRepository.findProducts(f.getCategory1(),minLatitude,maxLatitude,minLongitude,maxLongitude,username);
                System.out.println(resultList);
                break;
            case 2:
                resultList=productRepository.findProducts(f.getCategory1(),f.getCategory2(),minLatitude,maxLatitude,minLongitude,maxLongitude,username);
                break;
            case 3:
                resultList=productRepository.findProducts(f.getCategory1(),f.getCategory2(),f.getCategory3(),minLatitude,maxLatitude,minLongitude,maxLongitude,username);
                break;
            default:
                resultList=new ArrayList<>();
                break;
        }
        return resultList;
    }
}
