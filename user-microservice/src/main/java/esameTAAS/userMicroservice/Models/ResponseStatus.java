package esameTAAS.userMicroservice.Models;

import org.springframework.http.HttpStatus;

public enum ResponseStatus {
    OK(200),
    ERROR(500,"Generic Error"),
    ERROR_FACEBOOK(500,"Error from API facebook"),
    ERROR_REGISTER(500,"Error from server in registration"),
    NOT_FOUND(404,"Route not found"),
    UNAUTHORIZED(401,"You must be logged to see this content"),
    UNAUTHORIZED_TOKEN_EXPIRED(401,"Token expired"),
    GENERIC_BAD_REQUEST(400,"There is a problem in your request"),
    BAD_REQUEST_UKNOWN_CATEGORY(400,"One of the categories of your product is unknown",-1),
    BAD_REQUEST_MISSING_CATEGORIES(400,"Your product doesn't contains categories",-3),
    BAD_REQUEST_BAD_TITLE(400,"Your product title isn't acceptable",-4),
    BAD_REQUEST_BAD_DESCRIPTION(400,"Your product description isn't acceptable",-5),
    BAD_REQUEST_NOT_A_NEW_PRODUCT(400,"Your request contains your username or an id therefore isn't a new product",-6),
    BAD_REQUEST_USERNAME_IS_INVALID(400,"Username invalid"),
    BAD_REQUEST_USER_NOT_EXIST (404,"Username not found in database"),
    BAD_REQUEST_WRONG_PASSWORD (403,"Wrong password for selected user");

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
