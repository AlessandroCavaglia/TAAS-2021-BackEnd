package esameTAAS.productMicroservice.Models.Comunication;

public class Filter {
    private Float latitudePoint;
    private Float longitudePoint;
    private Float radius;
    private Integer categoryNumber;
    private String category1;
    private String category2;
    private String category3;

    public Filter(Float latitudePoint, Float longitudePoint, Float radius, int categoryNumber, String category1, String category2, String category3) {
        this.latitudePoint = latitudePoint;
        this.longitudePoint = longitudePoint;
        this.radius = radius;
        this.categoryNumber = categoryNumber;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
    }

    public Filter(Float latitudePoint, Float longitudePoint, Float radius, int categoryNumber, String category1) {
        this.latitudePoint = latitudePoint;
        this.longitudePoint = longitudePoint;
        this.radius = radius;
        this.categoryNumber = categoryNumber;
        this.category1 = category1;
    }

    public Filter(Float latitudePoint, Float longitudePoint, Float radius, int categoryNumber, String category1, String category2) {
        this.latitudePoint = latitudePoint;
        this.longitudePoint = longitudePoint;
        this.radius = radius;
        this.categoryNumber = categoryNumber;
        this.category1 = category1;
        this.category2 = category2;
    }

    public Filter(){}

    public Float getLatitudePoint() {
        return latitudePoint;
    }

    public void setLatitudePoint(Float latitudePoint) {
        this.latitudePoint = latitudePoint;
    }

    public Float getLongitudePoint() {
        return longitudePoint;
    }

    public void setLongitudePoint(Float longitudePoint) {
        this.longitudePoint = longitudePoint;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Integer getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(Integer categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public static ResponseStatus checkFilter(Filter f){
        //Check coordinates
        if((f.getLatitudePoint()==null || f.getLongitudePoint()==null) || (f.getLatitudePoint()>90f || f.getLatitudePoint()<-90f) || (f.getLongitudePoint()>180f || f.getLongitudePoint()<-180f)){
            return ResponseStatus.BAD_REQUEST_INVALID_GEO_POSITION;
        }
        if(f.getRadius()==null || f.getRadius()<=0){
            return ResponseStatus.BAD_REQUEST_INVALID_RADIUS;
        }
        if(f.getCategoryNumber()==null ||f.getCategoryNumber()>3 || f.getCategoryNumber()<=0){
            return ResponseStatus.BAD_REQUEST_INVALID_CATEGORIES;
        }
        if(f.getCategoryNumber()==1 && (f.getCategory1()==null)){
            return ResponseStatus.BAD_REQUEST_INVALID_CATEGORIES;
        }
        if(f.getCategoryNumber()==2 && (f.getCategory1()==null || f.getCategory2()==null)){
            return ResponseStatus.BAD_REQUEST_INVALID_CATEGORIES;
        }
        if(f.getCategoryNumber()==2 && (f.getCategory1()==null || f.getCategory2()==null || f.getCategory3()==null)){
            return ResponseStatus.BAD_REQUEST_INVALID_CATEGORIES;
        }
        return ResponseStatus.OK;
    }
}
