import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingInterfaceGUI extends JFrame {
    private JComboBox<String> TypeComboBox; //ComboBox for Sorting products by their category
    private JTable productListTable; //Table for displaying products
    private JTextArea productDetailsTextArea; //Text area for displaying product details
    private JButton addToCartButton; //Add to shopping Cart button
    private JButton viewShoppingCartButton; // View shopping cart button
    private static ArrayList<Product> productlist = WestminsterShoppingManager.getProductlist(); //Getting product list from WestminsterShoppingManager
    private ShoppingCart shoppingCart;
    private Map<String, Color> highlightedRows;


    public ShoppingInterfaceGUI() { //Method for setting up the GUI interface

        setTitle("Westminster Shopping Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothes"}); //Adding Categories to the ComboBox
        productListTable = new JTable();
        productDetailsTextArea = new JTextArea();
        addToCartButton = new JButton("Add to Shopping Cart"); //Naming add to shopping cart button
        viewShoppingCartButton = new JButton("Shopping Cart"); //Naming view shopping cart button
        shoppingCart = new ShoppingCart();
        highlightedRows = new HashMap<>();
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH); // Creating Panels
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) productListTable.getModel()); //Sorting Table in Alphabetical order
        productListTable.setRowSorter(sorter);
        updateTable();//Updating the table with product data
        setVisible(true);//Setting the frame visible

    }


    private JPanel createTopPanel() { //Method for creating the top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();//Setting up the "Shopping Cart" button and positioning it
        buttonPanel.add(viewShoppingCartButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));//Setting up the ComboBox and positioning it
        comboBoxPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); //20px top padding
        comboBoxPanel.add(new JLabel("Select Product Category: ")); //Labeling the Combobox
        comboBoxPanel.add(TypeComboBox);
        topPanel.add(comboBoxPanel, BorderLayout.CENTER);
        TypeComboBox.addActionListener(new ActionListener() {//Action listener to update the table
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });
        viewShoppingCartButton.addActionListener(new ActionListener() {//Action listener to open the shopping cart
            @Override
            public void actionPerformed(ActionEvent e) {
                shoppingCart.setVisible(true);
            }
        });
        return topPanel;

    }


    private JPanel createCenterPanel() { //Method for creating center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        Object[][] data = {};
        String[] columns = {"Product ID", "Name", "Category", "Price (£)", "Info"}; //Naming Columns of the Table
        DefaultTableModel model = new DefaultTableModel(data, columns) { //Creating the Table with above columns
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //Make all cells uneditable
            }
        };
        productListTable.setModel(model); //Adding a scroll pane and positioning the table
        JScrollPane scrollPane = new JScrollPane(productListTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); //20px padding on both sides
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        productDetailsTextArea.setEditable(false); //Making text area uneditable
        productDetailsTextArea.setFocusable(false);
        productDetailsTextArea.setPreferredSize(new Dimension(0, 150));
        centerPanel.add(new JScrollPane(productDetailsTextArea), BorderLayout.SOUTH);
        productListTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {//Choosing double click to select a product
                if (e.getClickCount() == 2) {
                    int selectedRow = productListTable.getSelectedRow();
                    if (selectedRow != -1) {
                        displayProductDetails(selectedRow);
                    }
                }
            }
        });
        addToCartButton.addActionListener(new ActionListener() { //Adding action listener to add to shopping cart button
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productListTable.getSelectedRow();
                if (selectedRow != -1) {
                    String productID = (String) productListTable.getValueAt(selectedRow, 0);
                    Product selectedProduct = findProductById(productID);
                    if (selectedProduct != null && selectedProduct.getNumberofitems() > 0) {
                        shoppingCart.addProductToCart(selectedProduct);
                        updateAvailableItems(selectedProduct.getProductID());
                    } else {
                        JOptionPane.showMessageDialog(null, "This item is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE); //Out of stock message when product runs out of stock
                    }
                }
            }
        });
        return centerPanel;

    }


    private JPanel createBottomPanel() { //Method for creating bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addToCartButton); //Adding the add to shopping cart button
        return bottomPanel;

    }


    private void updateTable() { //Method for adding data to the table
        DefaultTableModel model = (DefaultTableModel) productListTable.getModel();
        model.setRowCount(0); //Clearing existing data
        highlightedRows.clear(); //Clearing highlighted rows
        for (Product product : productlist) { //Updating the table with selected type of products
            if (TypeComboBox.getSelectedItem().equals("All") ||
                    (TypeComboBox.getSelectedItem().equals("Electronics") && product instanceof Electronics) ||
                    (TypeComboBox.getSelectedItem().equals("Clothes") && product instanceof Clothing)) {
                String[] rowData = {
                        product.getProductID(),
                        product.getProductname(),
                        (product instanceof Electronics) ? "Electronics" : "Clothes",
                        String.valueOf(product.getPrice()),
                        getProductInfo(product)
                };
                model.addRow(rowData);
                if (product.getNumberofitems() < 4) { //Highlighting rows with less than 4 available items
                    highlightedRows.put(product.getProductID(), Color.RED);
                }
            }
        }
        productListTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

    }


    private String getProductInfo(Product product) { //Method for getting product info
        if (product instanceof Electronics) {
            Electronics electronics = (Electronics) product;
            return "Warranty: " + electronics.getWarranty() + "\nBrand: " + electronics.getBrand(); //Printing Electronic product info
        } else if (product instanceof Clothing) {
            Clothing clothing = (Clothing) product;
            return "Size: " + clothing.getSize() + "\nColor: " + clothing.getColor(); //Printing Clothing product info
        } else {
            return "";
        }

    }


    private void displayProductDetails(int selectedRow) { //Method for displaying product details
        DefaultTableModel model = (DefaultTableModel) productListTable.getModel();
        String productID = (String) model.getValueAt(selectedRow, 0); //Getting product id
        String category = (String) model.getValueAt(selectedRow, 2); //Getting product category
        String name = (String) model.getValueAt(selectedRow, 1); //Getting product name
        double price = Double.parseDouble((String) model.getValueAt(selectedRow, 3)); //Getting product price
        String info = (String) model.getValueAt(selectedRow, 4); //Getting product info
        int availableItems = 0;
        for (Product product : productlist) {
            if (product.getProductID().equals(productID)) {
                availableItems = product.getNumberofitems(); ////Getting available number of items
                break;
            }
        }
        String details = String.format(
                "Selected Product\n" +
                        "Product ID: %s\n" +
                        "Category: %s\n" +
                        "Name: %s\n" +
                        "Price: %.2f £\n" +
                        "%s\n" +
                        "Available Items: %d",
                productID, category, name, price, info, availableItems); //printing product details
        productDetailsTextArea.setText(details);

    }


    private void updateAvailableItems(String productID) {//Updating the available items of the Table
        for (Product product : productlist) {
            if (product.getProductID().equals(productID)) {
                product.decrementAvailableItems();
                break;
            }
        }
        updateTable();

    }


    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String productID = (String) table.getValueAt(row, 0);
            if (highlightedRows.containsKey(productID)) {
                component.setBackground(highlightedRows.get(productID));
            } else {
                component.setBackground(table.getBackground());
            }
            return component;
        }

    }


    private Product findProductById(String productID) { //Method for finding products
        for (Product product : productlist) {
            if (product.getProductID().equals(productID)) {
                return product;
            }
        }
        return null;

    }
}
