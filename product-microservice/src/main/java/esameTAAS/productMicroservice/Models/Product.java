package esameTAAS.productMicroservice.Models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import esameTAAS.productMicroservice.Models.Comunication.ResponseStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"}) //Annulla il carciamento lazy dei dati
public class Product {
    public static final List<String> CATEGORIES_LIST = Arrays.asList("Informatica", "Giardinaggio", "Fai da te");
    public static final int TITLE_MAX_LENGHT= 20;
    public static final int DESCRIPTION_MAX_LENGHT= 200;
    public static final int CATEGORIES_MAX_NUMBER= 3;
    public static final int MAX_PRICE_RANGE= 5;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String title;
    private String description;
    private Float longitude;
    private Float latitude;
    private String categories;
    private Short priceRange;
    private Boolean enabled;
    private byte[] image1;
    private byte[] image2;
    private byte[] image3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Short getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(Short priceRange) {
        this.priceRange = priceRange;
    }

    public byte[] getImage1() {
        return image1;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public byte[] getImage2() {
        return image2;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public byte[] getImage3() {
        return image3;
    }

    public void setImage3(byte[] image3) {
        this.image3 = image3;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public static ResponseStatus checkProduct(Product prod){
        //Check Categories
        if(prod.getCategories()==null){
            return ResponseStatus.BAD_REQUEST_MISSING_CATEGORIES;
        }else{
            String[] categories=prod.getCategories().split(",");
            if(categories.length>Product.CATEGORIES_MAX_NUMBER){
                return ResponseStatus.BAD_REQUEST_TOO_MANY_CATEGORIES;
            }
            for (String cat:
                 categories) {
                if(!CATEGORIES_LIST.contains(cat)){
                    return ResponseStatus.BAD_REQUEST_UKNOWN_CATEGORY;
                }
            }
        }
        //Check Title
        if(prod.getTitle().length()<=0 || prod.getTitle().length()>Product.TITLE_MAX_LENGHT){
            return ResponseStatus.BAD_REQUEST_BAD_TITLE;
        }
        //Check Description
        if(prod.getDescription().length()<=0 || prod.getDescription().length()>Product.DESCRIPTION_MAX_LENGHT){
            return ResponseStatus.BAD_REQUEST_BAD_DESCRIPTION;
        }
        //Check priceRange
        if(prod.getPriceRange()<=0 || prod.getPriceRange()>Product.MAX_PRICE_RANGE){
            return ResponseStatus.BAD_REQUEST_INVALID_PRICE_RANGE;
        }
        //Check images
        if(prod.getImage1()==null && prod.getImage2()==null && prod.getImage3()==null){
            return ResponseStatus.BAD_REQUEST_NO_IMAGES_GIVEN;
        }
        if((prod.getImage1()==null && (prod.getImage2()!=null || prod.getImage3()!=null)) || (prod.getImage2()==null && prod.getImage3()!=null)){
            return ResponseStatus.BAD_REQUEST_INVALID_IMAGE_ORDER;
        }
        //Check coordinates
        if((prod.getLatitude()==null || prod.getLongitude()==null) || (prod.getLatitude()>90f || prod.getLatitude()<-90f) || (prod.getLongitude()>180f || prod.getLongitude()<-180f)){
            return ResponseStatus.BAD_REQUEST_INVALID_GEO_POSITION;
        }
        return ResponseStatus.OK;
    }

    public static ResponseStatus checkNewProduct(Product prod){
        if(prod.getId()!=null || prod.getUsername()!=null){
            return ResponseStatus.BAD_REQUEST_NOT_A_NEW_PRODUCT;
        }
        return ResponseStatus.OK;
    }

    public static ResponseStatus checkExistingProduct(Product prod){
        if(prod.getUsername()==null || prod.getId()==null || prod.getEnabled()==null){
            return ResponseStatus.BAD_REQUEST_INVALID_EXISTING_PRODUCT;
        }
        return ResponseStatus.OK;
    }

    public static Product sanitize_product(Product prod){
        prod.setTitle(Character.toUpperCase(prod.getTitle().charAt(0)) + prod.getTitle().substring(1));
        prod.setDescription(Character.toUpperCase(prod.getDescription().charAt(0)) + prod.getDescription().substring(1));
        return prod;
    }
}
