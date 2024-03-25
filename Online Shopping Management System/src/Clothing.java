import java.io.Serializable;
public class Clothing extends Product implements Serializable{
   private String size; //Size variable
   private String color;//Color Variable

   public Clothing(String size, String color, String prodcutID, String productname, int numberofitems, double price){ //Constructor for Clothing Product
       super(prodcutID,productname,numberofitems,price);
       this.size= size;
       this.color= color;
   }
   public String getSize(){
       return size;
   } //Getter for size
   public String getColor(){
       return color;
   } //Getter for color
   public void setSize(String size){
       this.size = size;
   } //Setter for size
   public void setColor(String color){
       this.color = color;
   } //Setter for color

}
