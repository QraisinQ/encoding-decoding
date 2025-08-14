package ie.atu.sw;

public class Encoder {
    private final String SPACES = ", ";
    private String[][] words;
    private String[][] suffixes;

    public Encoder(String[] words) {
        var suffixesCounter = 0;
        for (var word : words) {
            if (word.startsWith("@@"))
                suffixesCounter += 1;
        }

        var wordsCounter = words.length - suffixesCounter;

        this.words = new String[wordsCounter][2];
        this.suffixes = new String[suffixesCounter][2];
        wordsCounter = 0;
        suffixesCounter = 0;

        for (var word : words) {
            if (word.startsWith("@@")) {
                this.suffixes[suffixesCounter][0] = word.substring(2);
                this.suffixes[suffixesCounter][1] = Integer.toString(suffixesCounter + wordsCounter);
                suffixesCounter += 1;
            } else {
                this.words[wordsCounter][0] = word;
                this.words[wordsCounter][1] = Integer.toString(suffixesCounter + wordsCounter);
                wordsCounter += 1;
            }
        }

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

    public void encodeFile(String inputFilePath, String outputFilePath) throws Exception {
        var lines = FileUtility.readFileToArray(inputFilePath);
        var resultLines = new String[lines.length];

        for (var i = 0; i < lines.length; i++) {
            resultLines[i] = encodeLine(lines[i]);
            ProgressMeter.printProgress(i + 1, lines.length);
        }

        FileUtility.writeArrayToFile(outputFilePath, resultLines);
    }

    private String encodeLine(String line) {
        if (line.isEmpty()) {
            return "";
        }

        var lowerCaseLine = line.toLowerCase();
        var elements = new String[lowerCaseLine.length()];
        var currentWord = new StringBuilder();
        var elementsCounter = 0;

        for (int i = 0; i < lowerCaseLine.length(); i++) {
            char ch = lowerCaseLine.charAt(i);

            if (Character.isLetterOrDigit(ch)) {
                currentWord.append(ch);
            } else {
                if (currentWord.length() > 0) {
                    elements[elementsCounter] = currentWord.toString();
                    currentWord.setLength(0);
                    elementsCounter += 1;
                }

                elements[elementsCounter] = String.valueOf(ch);
                elementsCounter += 1;
            }
        }

        if (currentWord.length() > 0) {
            elements[elementsCounter] = currentWord.toString();
            elementsCounter += 1;
        }

        var result = "";
        for (var i = 0; i < elementsCounter; i++) {
            var element = elements[i];
            var codes = encodeElement(element);

            for (var k = 0; k < codes.length; k++) {
                if (result.isEmpty()) {
                    result = codes[k];
                    continue;
                }

                result += SPACES + codes[k];
            }
        }

        return result;
    }

    private String[] encodeElement(String element) {
        if (element.equals(" ")) {
            return new String[0];
        }

        for (var word : words) {
            if (element.equals(word[0])) {
                return new String[] { word[1] };
            }

            if (element.startsWith(word[0])) {
                var currentWordSuffix = element.substring(word[0].length());

                for (var suffix : suffixes) {
                    if (currentWordSuffix.equals(suffix[0])) {
                        return new String[] { word[1], suffix[1] };
                    }
                }
            }
        }

        return new String[] { "0" };
    }
}