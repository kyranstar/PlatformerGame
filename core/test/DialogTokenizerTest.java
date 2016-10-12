import com.kyranadams.platformer.scenes.dialog.DialogTokenizeException;
import com.kyranadams.platformer.scenes.dialog.DialogTokenizer;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.kyranadams.platformer.scenes.dialog.DialogTokenizer.DialogToken;
import static com.kyranadams.platformer.scenes.dialog.DialogTokenizer.DialogTokenType;
import static org.junit.Assert.assertTrue;

public class DialogTokenizerTest {

    @Test
    public void testDialogParse() {
        List<DialogTokenizer.DialogToken> result = DialogTokenizer.tokenize("asfl a=3, \"asdasf\"");
        List<DialogTokenizer.DialogToken> expected = Arrays.asList(new DialogToken(DialogTokenType.WORD, "asfl"),
                new DialogToken(DialogTokenType.WORD, "a"),
                new DialogToken(DialogTokenType.EQUALS, "="),
                new DialogToken(DialogTokenType.WORD, "3"),
                new DialogToken(DialogTokenType.COMMA, ","),
                new DialogToken(DialogTokenType.STRING, "asdasf"));
        assertListEquals(result, expected);
    }

    @Test
    public void testDialogParseNoList() {
        List<DialogTokenizer.DialogToken> result = DialogTokenizer.tokenize("asfl \"asdasf\"");
        List<DialogTokenizer.DialogToken> expected = Arrays.asList(new DialogToken(DialogTokenType.WORD, "asfl"),
                new DialogToken(DialogTokenType.STRING, "asdasf"));
        assertListEquals(result, expected);
    }

    @Test
    public void testDialogParseLongList() {
        List<DialogTokenizer.DialogToken> result = DialogTokenizer.tokenize("asfl a = 4, b = word1, c = \"XD!\", \"asdasf\"");
        List<DialogTokenizer.DialogToken> expected = Arrays.asList(new DialogToken(DialogTokenType.WORD, "asfl"),
                new DialogToken(DialogTokenType.WORD, "a"),
                new DialogToken(DialogTokenType.EQUALS, "="),
                new DialogToken(DialogTokenType.WORD, "4"),
                new DialogToken(DialogTokenType.COMMA, ","),
                new DialogToken(DialogTokenType.WORD, "b"),
                new DialogToken(DialogTokenType.EQUALS, "="),
                new DialogToken(DialogTokenType.WORD, "word1"),
                new DialogToken(DialogTokenType.COMMA, ","),
                new DialogToken(DialogTokenType.WORD, "c"),
                new DialogToken(DialogTokenType.EQUALS, "="),
                new DialogToken(DialogTokenType.STRING, "XD!"),
                new DialogToken(DialogTokenType.COMMA, ","),
                new DialogToken(DialogTokenType.STRING, "asdasf"));
        assertListEquals(result, expected);
    }

    @Test(expected = DialogTokenizeException.class)
    public void testUnfinishedString() {
        DialogTokenizer.tokenize("asfl \"asdadf ");
    }

    private void assertListEquals(List<DialogTokenizer.DialogToken> result, List<DialogTokenizer.DialogToken> expected) {
        assertTrue("Expected lists to be equal." +
                        "\n  'result'        = " + result +
                        "\n  'expected' = " + expected,
                expected.equals(result));
    }

}
