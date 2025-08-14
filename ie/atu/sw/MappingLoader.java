package ie.atu.sw;

import java.util.Arrays;

public class MappingLoader {
    private String[] words;

    public void loadMapping(String filePath) throws Exception {
        var lines = FileUtility.readFileToArray(filePath);
        var wordCounter = 0;
        var workWords = new String[lines.length];

        for (var line : lines) {
            if (line.isEmpty())
                continue;

            String[] parts = line.split(",");

            if (parts.length != 2) {
                throw new Exception("Invalid mapping format at line " + (wordCounter + 1) + ": " + line);
            }

            String word = parts[0];
            workWords[wordCounter] = word;
            wordCounter++;
        }

        words = Arrays.copyOf(workWords, wordCounter);
    }

    public String[] getWords() {
        return Arrays.copyOf(words, words.length);
    }
}
