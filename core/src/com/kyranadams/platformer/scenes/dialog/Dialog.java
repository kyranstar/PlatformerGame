package com.kyranadams.platformer.scenes.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kyranadams.platformer.scenes.entity.Entity;

import java.util.ArrayList;
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

            List<DialogTokenizer.DialogToken> tokensToComma = new ArrayList<DialogTokenizer.DialogToken>();
            for (int i = 0; i < parameterList.size(); i++) {
                DialogTokenizer.DialogToken p = parameterList.get(i);
                if (p.type == DialogTokenizer.DialogTokenType.COMMA || i == parameterList.size() - 1) {
                    int equalsIndex = -1;
                    for(int j = 0; j < tokensToComma.size(); j++){
                        if(tokensToComma.get(j).type == DialogTokenizer.DialogTokenType.EQUALS){
                            equalsIndex = j;
                            break;
                        }
                    }
                    if(equalsIndex != -1){
                        if(equalsIndex != 1){
                            throw new RuntimeException("Can only have one argument left of =");
                        }
                        if(tokensToComma.size() != 3){
                            throw new RuntimeException("Can only have one argument right of =");
                        }
                        String varName = tokensToComma.get(0).string;
                        String paramName = tokensToComma.get(2).string;
                        newParameters.put(varName.toLowerCase().trim(), paramName.toLowerCase().trim());
                    }
                    tokensToComma = new ArrayList<DialogTokenizer.DialogToken>();
                } else {
                    tokensToComma.add(p);
                }
            }
            Parameters copy = new Parameters();
            copy.parameters = newParameters;
            return copy;
        }
    }

    public void render(SpriteBatch batch) {

    }

}
