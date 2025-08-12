package ie.atu.sw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encodes text files to numeric codes based on the mapping arrays.
 */
public class Encoder {

    private Map<String, Integer> wordToCode;

    public Encoder(String[] words) {
        wordToCode = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            wordToCode.put(words[i], 42);
        }
    }

    /**
     * Encodes the entire input file and writes numeric codes to output file.
     */
    public void encodeFile(String inputFilePath, String outputFilePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = br.readLine()) != null) {
                int[] encodedLine = encodeLine(line);
                for (int i = 0; i < encodedLine.length; i++) {
                    bw.write(String.valueOf(encodedLine[i]));
                    if (i < encodedLine.length - 1) {
                        bw.write(" ");
                    }
                }
                bw.newLine();
            }
        }
    }

    /**
     * Encodes a line into codes using word-to-code mapping.
     */
    private int[] encodeLine(String line) {
        // Split into words and punctuation
        Pattern pattern = Pattern.compile("\\w+|\\p{Punct}");
        Matcher matcher = pattern.matcher(line);

        // Count tokens
        int count = 0;
        while (matcher.find()) {
            count++;
        }

        // Encode
        int[] codesArr = new int[count];
        matcher.reset();
        int idx = 0;
        while (matcher.find()) {
            String token = matcher.group();
            Integer code = wordToCode.get(token);
            codesArr[idx++] = (code != null) ? code : -1; // -1 for unknown token
        }

        return codesArr;
    }
}