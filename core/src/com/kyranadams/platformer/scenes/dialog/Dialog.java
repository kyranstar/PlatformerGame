package com.kyranadams.platformer.scenes.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kyranadams.platformer.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dialog {

    private Map<String, Entity> entities;
    private Stage stage;

    public Dialog(String dialogFile, Map<String, Entity> entities, Stage stage) {
        this.entities = entities;
        this.stage = stage;
        parseDialog(Gdx.files.internal(dialogFile).readString().split("\n"));
    }

    public void parseDialog(String[] lines) {
        Parameters p = new Parameters();
        for (String line : lines) {
            List<DialogTokenizer.DialogToken> tokens = DialogTokenizer.tokenize(line);
            if (tokens.get(0).type != DialogTokenizer.DialogTokenType.WORD) {
                throw new RuntimeException("Expected command (" + tokens.get(0).string + ") to be word, was " + tokens.get(0).type);
            }
            String command = tokens.get(0).string;
            p.update(tokens);

            if (command.equals("speak")) {
                //speak(p, parameters[0], parameters[parameters.length - 1]);
            }
        }
    }

    private void speak(Parameters p, String character, String dialog) {

    }

    private Action startDialog(Actor actor) {
        return Actions.sequence();
    }

    private static class Parameters {
        private Map<String, String> parameters = new HashMap<String, String>();

        public Parameters update(List<DialogTokenizer.DialogToken> parameterList) {
            Map<String, String> newParameters = new HashMap<String, String>(parameters);
//            for (DialogTokenizer.DialogToken p : parameterList) {
//                if (!p.contains("=")) {
//                    continue;
//                }
//                String[] k = p.split("=");
//                newParameters.put(k[0].toLowerCase().trim(), k[1].toLowerCase().trim());
//            }
            Parameters copy = new Parameters();
            copy.parameters = newParameters;
            return copy;
        }
    }

    public void render(SpriteBatch batch) {

    }

}
