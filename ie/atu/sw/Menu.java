package ie.atu.sw;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private String mappingFilePath = "./encodings-10000.csv";
    private String inputFilePath = "./test.txt";
    private String outputFilePath = "./out.txt";
    private boolean isEncodingMode = true;
    private String[] words;

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public Menu() {
        scanner = new Scanner(System.in);
    }

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

        System.out.println("Switched to " + (isEncodingMode ? "Encoding" : "Decoding") + " mode.");
    }

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
