package esameTAAS.productMicroservice.Controllers;

import esameTAAS.productMicroservice.Models.Comunication.Filter;
import esameTAAS.productMicroservice.Models.Comunication.ResponseStatus;
import esameTAAS.productMicroservice.Models.Product;
import esameTAAS.productMicroservice.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public List<Product> list(){
        return productRepository.findAll();
    }

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
    @GetMapping("/products/filter")
    public ResponseEntity filterList(@RequestBody Filter f){
        ResponseStatus result=Filter.checkFilter(f);
        if(result.equals(ResponseStatus.OK)){
            return new ResponseEntity(getProductsFromFilter(f),result.httpStatus);
        }
        return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
    }
    @GetMapping("/products/filter_no_img")      //Filter function but the result items don't contain the imgs to enable async loading of heavy images
    public ResponseEntity filterListNoImg(@RequestBody Filter f){
        ResponseStatus result= Filter.checkFilter(f);
        if(result.equals(ResponseStatus.OK)){
            List<Product> resultList=getProductsFromFilter(f);
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
    public ResponseEntity create(@RequestBody Product product){ //TODO ADD SECURITY TOKEN CONTROL AND SET USERNAME BASED ON TOKEN
        ResponseStatus result;
        result=Product.checkProduct(product);
        if(result.equals(ResponseStatus.OK)) {
            result=Product.checkNewProduct(product);
            if(result.equals(ResponseStatus.OK)){
                productRepository.save(Product.sanitize_product(product));
            }
        }
        return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
    }
    @PutMapping("products/edit")
    public ResponseEntity edit(@RequestBody Product newProduct){ //TODO ADD SECURITY TOKEN CONTROL AND CHECK USERNAME IS THE SAME OF THE REQUEST BASED ON TOKEN
        ResponseStatus result;
        result=Product.checkProduct(newProduct);
        if(result.equals(ResponseStatus.OK)) {
            result=Product.checkExistingProduct(newProduct);
            if(result.equals(ResponseStatus.OK)){
                Optional<Product> oldProduct = productRepository.findById(newProduct.getId());
                if (oldProduct.isPresent()){
                    if(oldProduct.get().getUsername().equals(newProduct.getUsername()))
                        productRepository.save(newProduct);
                }else{
                    result= ResponseStatus.NOT_FOUND;
                }
            }
        }
        return new ResponseEntity<>(result.responseNumber+","+result.defaultDescription,result.httpStatus);
    }

    private List<Product> getProductsFromFilter(Filter f){
        List<Product> resultList;
        Float minLatitude,maxLatitude,minLongitude,maxLongitude;
        minLatitude=f.getLatitudePoint() - (f.getRadius()/6378) * (float)(180 / Math.PI);
        maxLatitude=f.getLatitudePoint() + (f.getRadius()/6378) * (float)(180 / Math.PI);
        minLongitude=f.getLongitudePoint() - (f.getRadius()/6378) * (float)(180 / Math.PI) / (f.getLatitudePoint()*(float)(Math.PI/180));
        maxLongitude=f.getLongitudePoint() + (f.getRadius()/6378) * (float)(180 / Math.PI) / (f.getLatitudePoint()*(float)(Math.PI/180));
        switch (f.getCategoryNumber()){
            case 1:
                resultList=productRepository.findProducts(f.getCategory1(),minLatitude,maxLatitude,minLongitude,maxLongitude);
                System.out.println(resultList);
                break;
            case 2:
                resultList=productRepository.findProducts(f.getCategory1(),f.getCategory2(),minLatitude,maxLatitude,minLongitude,maxLongitude);
                break;
            case 3:
                resultList=productRepository.findProducts(f.getCategory1(),f.getCategory2(),f.getCategory3(),minLatitude,maxLatitude,minLongitude,maxLongitude);
                break;
            default:
                resultList=new ArrayList<>();
                break;
        }
        return resultList;
    }
}
