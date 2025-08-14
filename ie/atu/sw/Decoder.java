package ie.atu.sw;

public class Decoder {
    private final String SPACES = ", ";
    private String[] words;

    public Decoder(String[] words) {
        this.words = words;
    }

    public void decodeFile(String inputFilePath, String outputFilePath) throws Exception {
        var encodedLines = FileUtility.readFileToArray(inputFilePath);
        var resultLines = new String[encodedLines.length];

        for (var i = 0; i < encodedLines.length; i++) {
            resultLines[i] = decodeLine(encodedLines[i]);
            ProgressMeter.printProgress(i + 1, encodedLines.length);
        }

        FileUtility.writeArrayToFile(outputFilePath, resultLines);
    }

    private String decodeLine(String line) throws Exception {
        try {
            if (line.isEmpty())
                return "";

            var result = "";
            var codes = line.split(SPACES);

            for (var code : codes) {
                var codeToInteger = Integer.parseInt(code);
                var currentWord = words[codeToInteger];

                if (currentWord.startsWith("@@")) {
                    result += currentWord.substring(2);
                } else {
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
