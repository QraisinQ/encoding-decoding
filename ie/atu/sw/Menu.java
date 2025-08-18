package ie.atu.sw;

import java.util.Scanner;

/**
 * Menu class
 * -----------------
 * Handles user interaction through a text-based menu.
 * Allows users to configure file paths, switch between encoding/decoding modes,
 * and start the encoding/decoding process.
 */
public class Menu {
    private Scanner scanner;
    private String mappingFilePath = "./encodings-10000.csv"; // Default mapping file
    private String inputFilePath = "./test.txt"; // Default input file
    private String outputFilePath = "./out.txt"; // Default output file
    private boolean isEncodingMode = true; // Starts in encoding mode
    private String[] words; // Stores loaded mapping words

    /**
     * Clears the console screen using ANSI escape codes.
     * Works in most terminals that support ANSI sequences.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Constructor.
     * Initializes the scanner for user input.
     */
    public Menu() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu in a loop and processes user selections.
     * User can configure file paths, toggle modes, start encoding/decoding, or
     * exit.
     */
    public void showMainMenu() {

        while (true) {
            Menu.clearScreen();
            System.out.println(ConsoleColour.YELLOW);
            System.out.println("************************************************************");
            System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
            System.out.println("*                                                          *");
            System.out.println("*              Encoding Words with Suffixes                *");
            System.out.println("*                                                          *");
            System.out.println("************************************************************");
            System.out.println("\n=== Text Encoding Application ===");
            System.out.println("1) Specify Mapping File (current: " + mappingFilePath + ")");
            System.out.println(
                    "2) Specify Input File (current: " + inputFilePath + ")");
            System.out.println("3) Specify Output File (current: " + outputFilePath + ")");
            System.out.println("4) Switch to " + (isEncodingMode ? "Decoding" : "Encoding") + " Mode");
            System.out.println("5) Start " + (isEncodingMode ? "Encoding" : "Decoding"));
            System.out.println("6) Quit");

            System.out.print(ConsoleColour.BLUE_BOLD);
            System.out.print("Select option [1-6]: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    specifyMappingFile();
                    break;
                case "2":
                    specifyInputFile();
                    break;
                case "3":
                    specifyOutputFile();
                    break;
                case "4":
                    Menu.clearScreen();
                    toggleMode();

                    System.out.println();
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;
                case "5":
                    Menu.clearScreen();
                    processFile();

                    System.out.println();
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;
                case "6":
                    scanner.close();
                    Menu.clearScreen();
                    System.out.println("Exiting application. Goodbye!");

                    return;
                default:
                    System.out.println("Invalid option. Please select a number from 1 to 6.");
                    break;
            }
        }
    }

    /**
     * Prompts the user to enter a path for the mapping file.
     * Keeps the default value if input is blank.
     */
    private void specifyMappingFile() {
        Menu.clearScreen();
        System.out.print("Enter path to mapping CSV file or press Enter for default value: ");
        String path = scanner.nextLine();

        if (!path.isBlank()) {
            mappingFilePath = path;
        }
    }

    /**
     * Prompts the user to enter a path for the input text file.
     * Keeps the default value if input is blank.
     */
    private void specifyInputFile() {
        Menu.clearScreen();
        System.out.print("Enter path to input text file or press Enter for default value: ");
        String path = scanner.nextLine();

        if (!path.isBlank()) {
            inputFilePath = path;
        }
    }

    /**
     * Prompts the user to enter a path for the output file.
     * Keeps the default value if input is blank.
     */
    private void specifyOutputFile() {
        Menu.clearScreen();
        System.out.print("Enter path to output file or press Enter for default value: ");
        String path = scanner.nextLine();

        if (!path.isBlank()) {
            outputFilePath = path;
        }
    }

    /**
     * Toggles between Encoding and Decoding mode.
     */
    private void toggleMode() {
        isEncodingMode = !isEncodingMode;

        System.out.println("Switched to " + (isEncodingMode ? "Encoding" : "Decoding") + " mode.");
    }

    /**
     * Loads the mapping file and starts encoding or decoding the input file.
     * Results are written to the output file.
     * Displays an error message if something goes wrong.
     */
    private void processFile() {
        try {
            var mappingLoader = new MappingLoader();
            mappingLoader.loadMapping(mappingFilePath);
            words = mappingLoader.getWords();

            System.out.println("Start " + (isEncodingMode ? "Encoding" : "Decoding") + " process...");
            System.out.println();

            if (isEncodingMode) {
                Encoder encoder = new Encoder(words);
                encoder.encodeFile(inputFilePath, outputFilePath);
            } else {
                Decoder decoder = new Decoder(words);
                decoder.decodeFile(inputFilePath, outputFilePath);
            }

            System.out.println();
            System.out.println();
            System.out.println((isEncodingMode ? "Encoding" : "Decoding") + " completed successfully.");
        } catch (Exception e) {
            System.out.println("Error during processing: " + e.getMessage());
            words = null;
        }
    }
}
