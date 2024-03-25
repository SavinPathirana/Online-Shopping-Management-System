import java.util.Scanner;

    interface ShoppingManager {
        void displaymenu();

        void addProduct(Scanner option);

        void deleteProduct(Scanner option);

        void printProducts();

        void saveData();

        void loadData();

        void openGUI(Scanner option);
    }

