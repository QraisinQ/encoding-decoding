package ie.atu.sw;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Handles the command-line menu interaction with the user.
 * Gets input for file paths, mode selection, and calls encoding/decoding.
 */
public class Menu {

    private Scanner scanner;
    private String mappingFilePath = "./encodings-10000.csv";
    private String inputFilePath = "./test.txt";
    private String outputFilePath = "./out.txt";
    private boolean isEncodingMode = true;

    private String[] words; // Loaded from MappingLoader

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public Menu() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu and processes user choices.
     */
    public void showMainMenu() {
        // loadMappingFile();

        while (true) {
            Menu.clearScreen();
            System.out.println("\n=== Text Encoding Application ===");
            System.out.println("1) Specify Mapping File (current: " + mappingFilePath + ")");
            System.out.println(
                    "2) Specify Input File (current: " + inputFilePath + ")");
            System.out.println("3) Specify Output File (current: " + outputFilePath + ")");
            System.out.println("4) Switch to " + (isEncodingMode ? "Decoding" : "Encoding") + " Mode");
            System.out.println("5) Start " + (isEncodingMode ? "Encoding" : "Decoding"));
            System.out.println("6) Quit");
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
                    toggleMode();
                    break;
                case "5":
                    processFile();
                    break;
                case "6":
                    Menu.clearScreen();
                    System.out.println("Exiting application. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please select a number from 1 to 6.");
            }
        }
    }

    private void specifyMappingFile() {
        Menu.clearScreen();
        System.out.print("Enter path to mapping CSV file or press Enter for default value: ");
        String path = scanner.nextLine();

        if (!path.isBlank()) {
            mappingFilePath = path;
        }
    }

    private void specifyInputFile() {
        Menu.clearScreen();
        System.out.print("Enter path to input text file or press Enter for default value: ");
        String path = scanner.nextLine();

        if (!path.isBlank()) {
            inputFilePath = path;
        }
    }

    private void specifyOutputFile() {
        Menu.clearScreen();
        System.out.print("Enter path to output file or press Enter for default value: ");
        String path = scanner.nextLine();

        if (!path.isBlank()) {
            outputFilePath = path;
        }
    }

    private void toggleMode() {
        isEncodingMode = !isEncodingMode;

        Menu.clearScreen();
        System.out.println("Switched to " + (isEncodingMode ? "Encoding" : "Decoding") + " mode.");
        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private void processFile() {
        try {
            var mappingLoader = new MappingLoader();
            mappingLoader.loadMapping(mappingFilePath);
            words = mappingLoader.getWords();

            if (isEncodingMode) {
                Encoder encoder = new Encoder(words);
                encoder.encodeFile(inputFilePath, outputFilePath);
            } else {
                Decoder decoder = new Decoder(words);
                decoder.decodeFile(inputFilePath, outputFilePath);
            }

            System.out.println((isEncodingMode ? "Encoding" : "Decoding") + " completed successfully.");
            System.out.println(Arrays.toString(words));
        } catch (Exception e) {
            System.out.println("Error during processing: " + e.getMessage());
            words = null;
        }
    }
}
