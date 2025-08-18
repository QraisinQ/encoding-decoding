package ie.atu.sw;

import java.util.Arrays;

/**
 * MappingLoader class
 * -------------------
 * Responsible for loading the word-to-code mappings from a CSV file.
 * 
 * The mapping file is expected to have the format:
 * word,code
 * on each line (e.g., "hello,123").
 *
 * This class extracts and stores only the words in an array.
 */
public class MappingLoader {
    private String[] words; // Stores all words loaded from the mapping file

    /**
     * Loads the mapping file and extracts words from each line.
     *
     * @param filePath Path to the CSV mapping file.
     * @throws Exception If a line is not in "word,code" format.
     */
    public void loadMapping(String filePath) throws Exception {

        // Read all lines of the file into an array of strings
        var lines = FileUtility.readFileToArray(filePath);
        var wordCounter = 0; // Counts how many words are loaded
        var workWords = new String[lines.length]; // Temporary array to store words

        // Iterate through each line in the file
        for (var line : lines) {
            if (line.isEmpty()) // Skip empty lines

                continue;

            // Split the line by comma. Expected: [word, code]
            String[] parts = line.split(",");

            // Validate the format
            if (parts.length != 2) {
                throw new Exception("Invalid mapping format at line " + (wordCounter + 1) + ": " + line);
            }

            String word = parts[0]; // Extract the word
            workWords[wordCounter] = word; // Store the word
            wordCounter++; // Increment word counter
        }

        // Copy only the filled portion into the final words array
        words = Arrays.copyOf(workWords, wordCounter);
    }

    /**
     * Returns a copy of the loaded words.
     * 
     * @return Array of words (safe copy, original is not exposed).
     */
    public String[] getWords() {
        return Arrays.copyOf(words, words.length);
    }
}
