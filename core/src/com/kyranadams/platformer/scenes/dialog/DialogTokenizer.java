package com.kyranadams.platformer.scenes.dialog;


import java.util.ArrayList;
import java.util.List;

public class DialogTokenizer {

    public static List<DialogToken> tokenize(String line) {
        List<DialogToken> tokens = new ArrayList<DialogToken>();
        DialogTokenType currentType = null;
        String build = "";
        char[] chars = line.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (currentType == DialogTokenType.STRING) {
                if (c == '"') {
                    // if ending a string
                    tokens.add(new DialogToken(DialogTokenType.STRING, build));
                    currentType = null;
                    build = "";
                } else {
                    // if continuing a string
                    build += c;
                }
            } else if (currentType == DialogTokenType.WORD) {
                if (Character.isLetterOrDigit(c)) {
                    // if continuing a word
                    build += c;
                } else {
                    // if ending a word
                    tokens.add(new DialogToken(DialogTokenType.WORD, build.toLowerCase().trim()));
                    currentType = null;
                    build = "";
                    i--;
                }
            } else {
                assert currentType == null;
                if (c == '"') {
                    // if starting a string
                    currentType = DialogTokenType.STRING;
                } else if (Character.isLetterOrDigit(c)) {
                    // if starting a word
                    currentType = DialogTokenType.WORD;
                    build += c;
                } else if (c == ',') {
                    tokens.add(new DialogToken(DialogTokenType.COMMA, ","));
                } else if (c == '=') {
                    tokens.add(new DialogToken(DialogTokenType.EQUALS, "="));
                }
            }
        }
        return tokens;
    }

    public enum DialogTokenType {
        STRING, COMMA, EQUALS, WORD
    }

    public static class DialogToken {
        DialogTokenType type;
        String string;

        public DialogToken(DialogTokenType type, String string) {
            this.type = type;
            this.string = string;
        }
    }

}
