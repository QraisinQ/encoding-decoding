package ie.atu.sw;

/**
 * Runner class
 * This is the entry point of the application.
 * It creates a Menu object and launches the main menu loop.
 */
public class Runner {

    public static void main(String[] args) {
        Menu myMenu = new Menu(); // Create a Menu object
        myMenu.showMainMenu(); // Start the menu interaction
    }

}