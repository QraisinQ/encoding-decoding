package ie.atu.sw;

/**
 * Encoder class
 * -------------
 * Responsible for encoding text lines into numerical codes
 * based on word/suffix mappings provided.
 */
public class Encoder {
    private final String SPACES = ", "; // Separator used between codes
    private String[][] words; // Stores words and their codes
    private String[][] suffixes; // Stores suffixes (marked with @@ in mapping) and their codes

    /**
     * Constructor
     * Splits provided word list into "words" and "suffixes".
     * Assigns each entry a code (as a string).
     * 
     * @param words Array of words and suffixes (from MappingLoader).
     */
    public Encoder(String[] words) {

        // Count how many entries are suffixes (prefix "@@")
        var suffixesCounter = 0;
        for (var word : words) {
            if (word.startsWith("@@"))
                suffixesCounter += 1;
        }

        // Words = all others
        var wordsCounter = words.length - suffixesCounter;

        // Initialize arrays [][2] → [0] = word/suffix, [1] = code
        this.words = new String[wordsCounter][2];
        this.suffixes = new String[suffixesCounter][2];
        wordsCounter = 0;
        suffixesCounter = 0;

        // Assign codes
        for (var word : words) {
            if (word.startsWith("@@")) { // suffix
                this.suffixes[suffixesCounter][0] = word.substring(2); // remove "@@"
                this.suffixes[suffixesCounter][1] = Integer.toString(suffixesCounter + wordsCounter);
                suffixesCounter += 1;
            } else { // regular word
                this.words[wordsCounter][0] = word;
                this.words[wordsCounter][1] = Integer.toString(suffixesCounter + wordsCounter);
                wordsCounter += 1;
            }
        }

        // Sort words by length (descending), so longer words match first
        var isChanged = true;
        while (isChanged) {
            isChanged = false;

            for (var i = 1; i < this.words.length; i++) {
                if (this.words[i][0].length() > this.words[i - 1][0].length()) {
                    var temp = this.words[i];
                    this.words[i] = this.words[i - 1];
                    this.words[i - 1] = temp;

                    isChanged = true;
                }
            }
        }
    }

    /**
     * Encodes the entire input file into codes and writes to output file.
     *
     * @param inputFilePath  Path to input text file.
     * @param outputFilePath Path to save encoded result.
     * @throws Exception If reading/writing fails.
     */
    public void encodeFile(String inputFilePath, String outputFilePath) throws Exception {
        var lines = FileUtility.readFileToArray(inputFilePath);
        var resultLines = new String[lines.length];

        // Encode each line
        for (var i = 0; i < lines.length; i++) {
            resultLines[i] = encodeLine(lines[i]);
            ProgressMeter.printProgress(i + 1, lines.length); // show progress
        }

        FileUtility.writeArrayToFile(outputFilePath, resultLines);
    }

    /**
     * Encodes a single line of text.
     * Splits it into words/symbols, converts each into codes.
     *
     * @param line Input text line.
     * @return Encoded string (list of codes separated by commas).
     */
    private String encodeLine(String line) {
        if (line.isEmpty()) {
            return "";
        }

        var lowerCaseLine = line.toLowerCase(); // Normalize
        var elements = new String[lowerCaseLine.length()];
        var currentWord = new StringBuilder();
        var elementsCounter = 0;

        // Break line into words and symbols
        for (int i = 0; i < lowerCaseLine.length(); i++) {
            char ch = lowerCaseLine.charAt(i);

            if (Character.isLetterOrDigit(ch)) {
                currentWord.append(ch); // part of a word
            } else {
                if (currentWord.length() > 0) { // save word
                    elements[elementsCounter] = currentWord.toString();
                    currentWord.setLength(0);
                    elementsCounter += 1;
                }

                elements[elementsCounter] = String.valueOf(ch); // store punctuation/symbol
                elementsCounter += 1;
            }
        }

        // Add last word if exists
        if (currentWord.length() > 0) {
            elements[elementsCounter] = currentWord.toString();
            elementsCounter += 1;
        }

        // Encode each element
        var result = "";
        for (var i = 0; i < elementsCounter; i++) {
            var element = elements[i];
            var codes = encodeElement(element);

            for (var k = 0; k < codes.length; k++) {
                if (result.isEmpty()) {
                    result = codes[k]; // first element → no comma
                    continue;
                }

                result += SPACES + codes[k]; // add with separator
            }
        }

        return result;
    }

    /**
     * Encodes a single element (word or symbol).
     *
     * @param element Word or symbol from line.
     * @return Array of codes (may be multiple if word + suffix).
     */
    private String[] encodeElement(String element) {
        if (element.equals(" ")) {
            return new String[0]; // ignore plain spaces
        }

        // Match against words
        for (var word : words) {
            if (element.equals(word[0])) {
                return new String[] { word[1] }; // exact word match
            }

            // Check for word + suffix case
            if (element.startsWith(word[0])) {
                var currentWordSuffix = element.substring(word[0].length());

                for (var suffix : suffixes) {
                    if (currentWordSuffix.equals(suffix[0])) {
                        return new String[] { word[1], suffix[1] }; // word + suffix
                    }
                }
            }
        }

        return new String[] { "0" }; // default if no match
    }
}