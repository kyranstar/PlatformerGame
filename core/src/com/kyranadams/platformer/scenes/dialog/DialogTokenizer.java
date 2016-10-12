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
        if (currentType == DialogTokenType.STRING) {
            throw new DialogTokenizeException("Unfinished string");
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
            if (type == null || string == null) {
                throw new IllegalArgumentException("Arguments can't be null");
            }

            this.type = type;
            this.string = string;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DialogToken that = (DialogToken) o;

            if (type != that.type) return false;
            return string.equals(that.string);

        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + string.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "DialogToken{" +
                    "type=" + type +
                    ", val=" + string +
                    '}';
        }
    }

}
