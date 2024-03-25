import java.io.Serializable;

public class Electronics extends Product implements Serializable{
    private String brand; //Brand variable
    private String warranty; //Warrenty variable
    public Electronics(String brand, String warranty, String prodcutID, String productname, int numberofitems, double price){ //Constructor for Electronic product
        super(prodcutID,productname,numberofitems,price);
        this.brand = brand;
        this.warranty = warranty;
    }
    public String getBrand(){
        return brand;
    } //Getter for the brand
    public String getWarranty(){
        return warranty;
    } //Getter for the warranty
    public void setBrand(String brand){
        this.brand= brand;
    } //Setter for the brand
    public void setWarranty(String warranty){
        this.warranty= warranty;
    } //Setter for the warranty

}
