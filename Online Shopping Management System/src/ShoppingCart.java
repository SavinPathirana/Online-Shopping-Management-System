import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart extends JFrame {
    private JTable shoppingCartTable; //Table for shopping cart
    private JTextArea discountInfoTextArea; //text area for showing discounts and prices
    private Map<String, Integer> cartItems;
    private static final double FisrtPurchaseDiscount = 0.10; //Discount for new customers
    private static final double CatergoryDiscount = 0.20; //Discount for Buying 3 items from same category
    private static int Electronic = 0; //Counting Electronic products when adding to cart
    private static int Cloth= 0; //Counting Clothing products when adding to cart


    public ShoppingCart() { //Method for setting up the shopping cart GUI interface

        setTitle("Shopping Cart");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); //Hide on close to keep the program running when closed
        shoppingCartTable = new JTable();
        discountInfoTextArea = new JTextArea();
        setLayout(new BorderLayout()); //Setting up the Layout
        add(createCenterPanel(), BorderLayout.CENTER); //Adding Panels
        add(createBottomPanel(), BorderLayout.SOUTH);
        cartItems = new HashMap<>();
        setVisible(false);

    }


    private JPanel createCenterPanel() { //Method for creating the center panel

        JPanel centerPanel = new JPanel(new BorderLayout());
        Object[][] data = {};
        String[] columns = {"Product", "Quantity", "Price (£)"};//Naming Columns of the Table
        DefaultTableModel model = new DefaultTableModel(data, columns) { //Creating the Table with above columns
            @Override
            public boolean isCellEditable(int row, int column) {//Making cells uneditable
                return false;
            }
        };
        shoppingCartTable.setModel(model);
        JScrollPane scrollPane = new JScrollPane(shoppingCartTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        return centerPanel;

    }


    private JPanel createBottomPanel() {//Method for creating the bottom panel

        JPanel bottomPanel = new JPanel(new BorderLayout());
        discountInfoTextArea.setEditable(false); //Making the text area uneditable
        discountInfoTextArea.setFocusable(false);
        bottomPanel.add(new JScrollPane(discountInfoTextArea), BorderLayout.CENTER);
        return bottomPanel;

    }


    public void addProductToCart(Product product) {
        updateShoppingCart(product);
    }


    private void updateShoppingCart(Product product) { //Method for adding  items to the shopping cart

        String productID = product.getProductID();
        if(product instanceof Electronics){
                Electronic = Electronic+1; //Counting the Electronic product count
        }
        else if (product instanceof  Clothing) {
                Cloth = Cloth+1; //Counting the Clothing product count
        }
        int quantity = cartItems.getOrDefault(productID, 0) + 1;
        cartItems.put(productID, quantity); //Updating quantity of products
        updateTable(); //Updating the shopping cart table
        calculateDiscounts(WestminsterShoppingManager.customerExists);

    }


    private void updateTable() { //Method for updating the table

        DefaultTableModel model = (DefaultTableModel) shoppingCartTable.getModel();
        model.setRowCount(0); //Clearing the existing data
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            String productID = entry.getKey();
            int quantity = entry.getValue();
            Product product = getProductByID(productID);
            if (product != null) {
                String[] rowData = getProductInfoArray(product);
                model.addRow(rowData);
            }
        }

    }


    private String[] getProductInfoArray(Product product) { //Method for getting product info for the table in shopping cart

        String productName = "";
        if (product instanceof Electronics) {
            productName = String.format("%s (%s, %s)", product.getProductname(), ((Electronics) product).getBrand(), ((Electronics) product).getWarranty());
        } else if (product instanceof Clothing) {
            productName = String.format("%s (%s, %s)", product.getProductname(), ((Clothing) product).getSize(), ((Clothing) product).getColor());
        }
        return new String[]{
                productName,
                String.valueOf(cartItems.get(product.getProductID())),
                String.valueOf(cartItems.get(product.getProductID()) * product.getPrice())
        };

    }

    private Product getProductByID(String productID) { //Method for getting products from id

        for (Product product : WestminsterShoppingManager.getProductlist()) {
            if (product.getProductID().equals(productID)) {
                return product;
            }
        }
        return null;

    }

    private void calculateDiscounts(boolean newCustomer) { //Method for calculating discount

        double total = 0; //Total
        double firstPurchaseDiscount= 0; //Discount for new customers
        double finalTotal = 0; //Final Total after discounts
        double categoryDiscount =0; //Discount for 3 products of same category discount
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            String productID = entry.getKey();
            int quantity = entry.getValue();
            Product product = getProductByID(productID);

            if (product != null) {
                total += quantity * product.getPrice(); //Calculating total
            }
        }
        if(!WestminsterShoppingManager.customerExists){ //Calculating discounts
        firstPurchaseDiscount = total * FisrtPurchaseDiscount;
        }
        if(Electronic>2){
            categoryDiscount = total * CatergoryDiscount;
        } else if (Cloth>2){
            categoryDiscount = total * CatergoryDiscount;
        }
        finalTotal = total - firstPurchaseDiscount - categoryDiscount; //Calculating final total
        discountInfoTextArea.setText(String.format("Total: %.2f £\n", total));//Displaying discounts and final total in the text area
        discountInfoTextArea.append(String.format("First Purchase Discount (10%%): - %.2f £\n", firstPurchaseDiscount));
        discountInfoTextArea.append(String.format("Three Items in same Category Discount (20%%): - %.2f £\n", categoryDiscount));
        discountInfoTextArea.append(String.format("Final Total: %.2f £", finalTotal));
    }
}
