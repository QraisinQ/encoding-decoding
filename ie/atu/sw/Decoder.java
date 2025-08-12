package ie.atu.sw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Decodes numeric code files back to original text based on mapping arrays.
 */
public class Decoder {

    private String[] words;

    public Decoder(String[] words) {
        this.words = words;
    }

    public void decodeFile(String inputFilePath, String outputFilePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = br.readLine()) != null) {
                String decodedLine = decodeLine(line);
                bw.write(decodedLine);
                bw.newLine();
            }
        }
    }

    /**
     * Decodes a space-separated list of numbers into the original string.
     */
    private String decodeLine(String line) {
        StringBuilder sb = new StringBuilder();
        String[] parts = line.trim().split("\\s+");

        for (String numStr : parts) {
            try {
                int code = Integer.parseInt(numStr);
                String word = codeToWord.get(code);
                if (word != null) {
                    sb.append(word);
                } else {
                    sb.append("<?>"); // placeholder for unknown codes
                }
                sb.append(" ");
            } catch (NumberFormatException e) {
                sb.append("<?> ");
            }
        }

        return sb.toString().trim();
    }
}
