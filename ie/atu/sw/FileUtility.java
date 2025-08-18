package ie.atu.sw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * FileUtility class
 * -----------------
 * Provides helper methods to read and write files as arrays of strings.
 * 
 * This utility is used throughout the project to simplify file I/O.
 */
public class FileUtility {

    /**
     * Reads all lines from a file into a String array.
     *
     * @param fileName Path to the input file.
     * @return Array of strings, where each element represents one line of the file.
     * @throws IOException If the file cannot be read.
     */
    public static String[] readFileToArray(String fileName) throws IOException {

        // Files.readAllLines returns a List<String>, convert it to String[]
        return Files.readAllLines(Paths.get(fileName)).toArray(new String[0]);
    }

    /**
     * Writes an array of strings into a file.
     * Each element of the array becomes one line in the output file.
     *
     * @param fileName Path to the output file.
     * @param data     Array of strings to write.
     * @throws IOException If the file cannot be written.
     */
    public static void writeArrayToFile(String fileName, String[] data) throws IOException {
        Path path = Path.of(fileName);
        // Convert array to a List<String> and write to file
        Files.write(path, Arrays.asList(data));
    }
}
