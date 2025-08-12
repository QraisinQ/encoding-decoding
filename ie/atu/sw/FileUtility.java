package ie.atu.sw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileUtility {
    public static String[] readFileToArray(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName)).toArray(new String[0]);
    }

    public static void writeArrayToFile(String fileName, String[] data) throws IOException {
        Path path = Path.of(fileName);
        Files.write(path, Arrays.asList(data));
    }
}
