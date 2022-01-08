package esameTAAS.productMicroservice.Models.Comunication;


import esameTAAS.productMicroservice.Models.Product;
import org.springframework.http.HttpStatus;

public enum ResponseStatus {
    OK(200,"OK",200),
    ERROR(500,"Generic Error"),
    NOT_FOUND(404,"Route not found"),
    UNAUTHORIZED(401,"You must be logged to see this content"),
    GENERIC_BAD_REQUEST(400,"There is a problem in your request",0),
    BAD_REQUEST_UKNOWN_CATEGORY(400,"One of the categories of your product is unknown",-1),
    BAD_REQUEST_TOO_MANY_CATEGORIES(400,"Your product contains too many categories, the maximum is "+ Product.CATEGORIES_MAX_NUMBER,-2),
    BAD_REQUEST_MISSING_CATEGORIES(400,"Your product doesn't contains categories",-3),
    BAD_REQUEST_BAD_TITLE(400,"Your product title isn't acceptable",-4),
    BAD_REQUEST_BAD_DESCRIPTION(400,"Your product description isn't acceptable",-5),
    BAD_REQUEST_NOT_A_NEW_PRODUCT(400,"Your request contains your username or an id therefore isn't a new product",-6),
    BAD_REQUEST_INVALID_PRICE_RANGE(400,"The price range of the product is out of border",-7),
    BAD_REQUEST_NO_IMAGES_GIVEN(400,"The product must contain at least an image",-8),
    BAD_REQUEST_INVALID_EXISTING_PRODUCT(400,"The product must be findable so it needs an id and a username",-9),
    BAD_REQUEST_INVALID_IMAGE_ORDER(400,"The product contains an invalid image order",-10),
    BAD_REQUEST_INVALID_GEO_POSITION(400,"The Position doesn't contain a valid longitude - latitude couple",-11),
    BAD_REQUEST_INVALID_RADIUS(400,"The given radius is invalid",-12),
    BAD_REQUEST_INVALID_CATEGORIES(400,"The given categories array is invalid",-13),
    BAD_REQUEST_NOT_A_NEW_ACTION(400,"Your request contains your username or an id therefore isn't a new action",-14),
    BAD_REQUEST_IVALID_ACTION(400,"The action you sent isn't a valid one",-15);

    public String defaultDescription;
    public HttpStatus httpStatus;
    public int responseNumber;
    ResponseStatus(){}
    ResponseStatus(int httpStatus) {this.httpStatus=HttpStatus.resolve(httpStatus);}
    ResponseStatus(int httpStatus, String description, int responsenumber) {
        this.httpStatus=HttpStatus.resolve(httpStatus);
        this.defaultDescription = description;
        this.responseNumber=responsenumber;
    }
    ResponseStatus(int httpStatus, String description) {
        this.httpStatus=HttpStatus.resolve(httpStatus);
        this.defaultDescription = description;
        this.responseNumber=httpStatus;
    }
}
