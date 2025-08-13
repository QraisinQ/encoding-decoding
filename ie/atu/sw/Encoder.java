package ie.atu.sw;

import java.util.Arrays;

public class Encoder {
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
    }

    public void encodeFile(String inputFilePath, String outputFilePath) throws Exception {
        var lines = FileUtility.readFileToArray(inputFilePath);
        var resultLines = new String[lines.length];

        for (var i = 0; i < lines.length; i++) {
            resultLines[i] = Arrays.toString(encodeLine(lines[i]));
        }

        FileUtility.writeArrayToFile(outputFilePath, resultLines);
    }

    private String[] encodeLine(String line) {
        return new String[] { "42" };
    }
}