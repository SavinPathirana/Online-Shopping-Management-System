import java.io.Serializable;

public class Product implements Serializable {
    private String productID; //Product id variable
    private String productname; //Product name variable
    private int numberofitems; //number of items variable
    private double price; //price variable
    public Product(String proID, String proName, int numofitems, double price){ //Constructor for Product
        this.productID = proID;
        this.productname= proName;
        this.numberofitems= numofitems;
        this.price= price;
    }
    public String getProductID(){
        return productID;
    } //Getter for the product id
    public String getProductname(){
        return productname;
    } //Getter for the product name
    public int getNumberofitems(){
        return numberofitems;
    } //Getter for the number of items
    public double getPrice(){
        return price;
    } //getter for the price
    public void setProductID(String proID){
        this.productID = proID;
    } //Setter for the product id
    public void setProductname(String proName){
        this.productname = proName;
    } //Setter for the product name
    public void setNumberofitems(int numofitems){
        this.numberofitems = numofitems;
    } //Setter for the number of items
    public void setPrice(double Price){
        this.price = Price;
    } //Setter for the price

    public void decrementAvailableItems() { //Method for reducing available items
        if (numberofitems > 0) {
            numberofitems--;
        }
    }
}
