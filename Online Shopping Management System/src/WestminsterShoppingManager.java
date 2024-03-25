import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

class WestminsterShoppingManager implements ShoppingManager {
    static int max = 49;//Setting up max count for the arraylist
    private static ArrayList<Product> productlist = new ArrayList<>(max); //Making the arraylist for products
    protected static ArrayList<User> userlist = new ArrayList<>(); //Making the arraylist for users
    public static boolean customerExists = false; //The variable for checking a new customer

    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        shoppingManager.displaymenu();
    }


    public void displaymenu() { //Display menu function
        Scanner option = new Scanner(System.in); //Setting up the scanner
        boolean menu = true;
        loadData(); //Loading data from the text file
        while (menu) {
            System.out.println("\n -----------Menu-----------"); //Printing menu
            System.out.println(
                    """
                            1 - Add a new product
                            2 - Delete a product
                            3 - Print the list of the products
                            4 - Save in a file
                            5 - Exit
                            6 - Display GUI
                            """);
            System.out.print("Select your option:");

            try{
            String chosenOption = option.next(); //Getting user input for the menu

            switch (chosenOption) { //Switch case
                case "1":
                    addProduct(option);
                    break;
                case "2":
                    deleteProduct(option);
                    break;
                case "3":
                    printProducts();
                    break;
                case "4":
                    saveData();
                    break;
                case "5":
                    System.out.println("Thank you for using this program."); //Exit message
                    menu = false;
                    break;
                case "6":
                    openGUI(option);
                    break;
                default:
                    System.out.println("Please enter a valid option.");
            }
        } catch (InputMismatchException e){
                System.out.println("Please enter a valid option.");
                }
        }
    }


    public void addProduct(Scanner option) { //Add products method

        if (productlist.size() <= 50) { //Checking arraylist size
            try {
                System.out.println("What type of product do you want to add?\n" +
                        "press 1 for Electronics\n" +
                        "press 2 for Clothing");
                int type = option.nextInt();
                if (type == 1) {
                    System.out.print("Enter the product id:");
                    String proID = option.next();
                    for (Product objcheck : productlist) {
                        if (objcheck.getProductID().equals(proID)) { //Checking whether product id already exists
                            System.out.println("A Product with this ID already exists.");
                            return;
                        }
                    }
                    System.out.print("Enter the product name:"); //Getting product details
                    String proName = option.next();
                    System.out.print("Enter the number of available items:");
                    int numofitems = option.nextInt();
                    System.out.print("Enter the price of the product:");
                    double price = option.nextDouble();
                    System.out.print("Enter the brand of the product:");
                    String brand = option.next();
                    System.out.print("Enter the warranty period of the product:");
                    String warranty = option.next();
                    productlist.add(new Electronics(brand, warranty, proID, proName, numofitems, price));
                    System.out.println("Product has been added.");
                } else if (type == 2) {
                    System.out.print("Enter the product id:");
                    String proID = option.next();
                    for (Product objcheck : productlist) {
                        if (objcheck.getProductID().equals(proID)) {
                            System.out.println("A Product with this ID already exists.");
                            return;
                        }
                    }
                    System.out.print("Enter the product name:");
                    String proName = option.next();
                    System.out.print("Enter the number of available items:");
                    int numofitems = option.nextInt();
                    System.out.print("Enter the price of the product:");
                    double price = option.nextDouble();
                    System.out.print("Enter the size of the product:");
                    String size = option.next();
                    System.out.print("Enter the colour of the product:");
                    String color = option.next();
                    productlist.add(new Clothing(size, color, proID, proName, numofitems, price));
                    System.out.println("Product has been added.");
                } else {
                    System.out.println("Please enter a valid type of product.");
                }
            } catch (Exception Correction) {
                System.out.println("Please Enter Inputs in Correct Formats!");
            }
        } else {
            System.out.println("Max Product limit Reached."); //Message to display when arraylist is full
        }
    }


    public void deleteProduct(Scanner option) { //Delete products method

        boolean exist = false;
        try {
            if (productlist.isEmpty()) {
                System.out.println("There is nothing to delete. The stock is empty."); //The message to display when there is nothing to delete
            } else {
                System.out.print("Enter the product id of the product you want to remove:");
                String remid = option.next();
                Iterator<Product> iterator = productlist.iterator();
                while (iterator.hasNext()) {
                    Product obj = iterator.next();
                    if (obj.getProductID().equals(remid)) {
                        exist = true;
                        System.out.println(obj.getProductname() + " has been removed.");
                        if (obj instanceof Electronics) {
                            System.out.println("Category : Electronics");
                            System.out.println(obj.getNumberofitems() + " items of the deleted product are left in the stock.");
                        } else if (obj instanceof Clothing) {
                            System.out.println("Category : Clothing");
                            System.out.println(obj.getNumberofitems() + " items of the deleted product are left in the stock.");
                        }
                        iterator.remove();
                    }
                }
                if (!exist) {
                    System.out.println("Invalid Product ID."); //Message to display when user inputs wrong id
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void printProducts() { //Print products method

        sortProducts(); //Sorting products before printing
        if (productlist.isEmpty()) { //Checking if the product list is empty
            System.out.println("There is nothing to print. The stock is empty.");
        }
        for (Product obj : productlist) {
            if (obj instanceof Electronics) {
                System.out.print("\n" + "Product Name: " + obj.getProductname() +
                        "\n" + "Product ID: " + obj.getProductID() + "\n" + "Number of items: "
                        + obj.getNumberofitems() + "\n" + "Price: " + obj.getPrice() + "\n");
                System.out.println("Brand: " + ((Electronics) obj).getBrand());
                System.out.println("Warranty: " + ((Electronics) obj).getWarranty());
                System.out.println("\n");
            } else if (obj instanceof Clothing) {
                System.out.print("Product Name: " + obj.getProductname() +
                        "\n" + "Product ID: " + obj.getProductID() + "\n" + "Number of items: "
                        + obj.getNumberofitems() + "\n" + "Price: " + obj.getPrice() + "\n");
                System.out.println("Size: " + ((Clothing) obj).getSize());
                System.out.println("Colour: " + ((Clothing) obj).getColor());
                System.out.println("\n");
            }
        }
    }

    public void sortProducts() { //Product details sorting method

        Collections.sort(productlist, Comparator.comparing(Product::getProductID));
    }


    public void saveData() { //Save data to a text file method

        try {
            File file = new File("Save.txt");
            FileWriter textwrite = new FileWriter("Save.txt");
            textwrite.write("--------------Products-------------- \n");
            for (Product obj : productlist) {
                if (obj instanceof Electronics) {
                    Electronics objE = (Electronics) obj;
                    textwrite.write("Type:Electronics\n");
                    textwrite.write("ID:" + obj.getProductID() + "\n");
                    textwrite.write("Name:" + obj.getProductname() + "\n");
                    textwrite.write("No of Products:" + obj.getNumberofitems() + "\n");
                    textwrite.write("Price:" + obj.getPrice() + "\n");
                    textwrite.write("Brand:" + ((Electronics) obj).getBrand() + "\n");
                    textwrite.write("Warranty:" + ((Electronics) obj).getWarranty() + "\n");
                } else if (obj instanceof Clothing) {
                    Clothing objC = (Clothing) obj;
                    textwrite.write("Type:Clothing\n");
                    textwrite.write("ID:" + obj.getProductID() + "\n");
                    textwrite.write("Name:" + obj.getProductname() + "\n");
                    textwrite.write("No of Products:" + obj.getNumberofitems() + "\n");
                    textwrite.write("Price:" + obj.getPrice() + "\n");
                    textwrite.write("Size:" + ((Clothing) obj).getSize() + "\n");
                    textwrite.write("Colour:" + ((Clothing) obj).getColor() + "\n");
                }
                textwrite.write("\n");
            }
            textwrite.close();
            System.out.println(file.getName() + " has been made. Products are saved in this file."); //Informing the user about the text file being made
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void loadData() { //Load data from a text file method

        try (BufferedReader reader = new BufferedReader(new FileReader("Save.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("Type:Electronics")) {
                    loadElectronics(reader);
                } else if (line.equals("Type:Clothing")) {
                    loadClothing(reader);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadElectronics(BufferedReader reader) throws IOException { //Method for loading electronic products

        String proID = reader.readLine().split(":")[1].trim();
        String proName = reader.readLine().split(":")[1].trim();
        int numofitems = Integer.parseInt(reader.readLine().split(":")[1].trim());
        double price = Double.parseDouble(reader.readLine().split(":")[1].trim());
        String brand = reader.readLine().split(":")[1].trim();
        String warranty = reader.readLine().split(":")[1].trim();
        productlist.add(new Electronics(brand, warranty, proID, proName, numofitems, price));
        reader.readLine(); // Read the empty line between products
    }


    private void loadClothing(BufferedReader reader) throws IOException { //Method for loading Clothing products

        String proID = reader.readLine().split(":")[1].trim();
        String proName = reader.readLine().split(":")[1].trim();
        int numofitems = Integer.parseInt(reader.readLine().split(":")[1].trim());
        double price = Double.parseDouble(reader.readLine().split(":")[1].trim());
        String size = reader.readLine().split(":")[1].trim();
        String color = reader.readLine().split(":")[1].trim();
        productlist.add(new Clothing(size, color, proID, proName, numofitems, price));
        reader.readLine(); // Read the empty line between products
    }


    public void openGUI(Scanner option) { //Method to open the GUI

        loadCustomerdetails(); // Loading customer details
        System.out.println("Enter username:"); //Getting user credentials
        String username = option.next();
        System.out.println("Enter Password:");
        String password = option.next();
        for (User objcheck : userlist) { //Checking whether it is a new user
            if (objcheck.getUsername().equals(username)) {
                customerExists = true;
                break;
            }
        }

        if (!customerExists) {
            userlist.add(new User(username, password));
            writecustomer(); //Adding customer as a new customer to the textfile
        }

        new ShoppingInterfaceGUI();
    }


    private void writecustomer() { //Method to add customers to a text file

        try (FileWriter textwrite = new FileWriter("Customer.txt", false)) {
            for (User obj : userlist) {
                textwrite.write("Username:" + obj.getUsername() + "\n");
                textwrite.write("Password:" + obj.getPassword() + "\n");
                textwrite.write("\n");
            }
            System.out.println("Customer details have been added to Customer.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadCustomerdetails() {

        BufferedReader cusreader = null;
        try {
            cusreader = new BufferedReader(new FileReader("Customer.txt"));
            loadCustomer(cusreader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (cusreader != null) {
                try {
                    cusreader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void loadCustomer(BufferedReader reader) throws IOException { //Method to load existing customers from a text file

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Username:")) {
                String username = line.split(":")[1].trim();
                String password = reader.readLine().split(":")[1].trim();
                reader.readLine(); // Read the empty line between users
                userlist.add(new User(username, password));
            }
        }
    }


    public static ArrayList<Product> getProductlist() { //getter for the productlist

        return productlist;
    }


}
