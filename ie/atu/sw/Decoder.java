package ie.atu.sw;

/**
 * Decoder class
 * -------------
 * Responsible for reversing the encoding process.
 * Takes numeric codes and converts them back into words/suffixes
 * using the mapping provided.
 */
public class Decoder {
    private final String SPACES = ", "; // Separator used between codes in the encoded file
    private String[] words; // Stores the list of words/suffixes (from mapping)

    /**
     * Constructor
     * Initializes the decoder with the mapping of words/suffixes.
     *
     * @param words Array of words and suffixes loaded by MappingLoader.
     */
    public Decoder(String[] words) {
        this.words = words;
    }

    /**
     * Decodes an entire file of encoded lines into plain text.
     *
     * @param inputFilePath  Path to encoded input file.
     * @param outputFilePath Path to save decoded output file.
     * @throws Exception If reading/writing fails.
     */
    public void decodeFile(String inputFilePath, String outputFilePath) throws Exception {
        var encodedLines = FileUtility.readFileToArray(inputFilePath);
        var resultLines = new String[encodedLines.length];

        // Decode each line
        for (var i = 0; i < encodedLines.length; i++) {
            resultLines[i] = decodeLine(encodedLines[i]);
            ProgressMeter.printProgress(i + 1, encodedLines.length); // show progress
        }

        FileUtility.writeArrayToFile(outputFilePath, resultLines);
    }

    /**
     * Decodes a single encoded line into plain text.
     *
     * @param line Encoded line containing numeric codes.
     * @return Decoded plain text string.
     * @throws Exception If decoding fails (e.g., invalid number or index).
     */
    private String decodeLine(String line) throws Exception {
        try {
            if (line.isEmpty())
                return "";

            var result = "";
            var codes = line.split(SPACES); // Split by ", " into separate codes

            for (var code : codes) {
                var codeToInteger = Integer.parseInt(code); // Convert string to number
                var currentWord = words[codeToInteger]; // Lookup word/suffix by index

                // If it's a suffix (starts with "@@"), append without space
                if (currentWord.startsWith("@@")) {
                    result += currentWord.substring(2); // remove "@@" prefix
                } else {
                    // Normal word: add space if not the first word
                    if (result.isEmpty()) {
                        result += currentWord;
                    } else {
                        result += " " + currentWord;
                    }
                }
            }

            return result;
        } catch (Exception e) {
            throw new Exception("Error during Decoding!!!");
        }

    }
}
