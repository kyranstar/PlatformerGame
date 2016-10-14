package com.kyranadams.platformer.scenes.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kyranadams.platformer.scenes.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyranadams.platformer.scenes.dialog.DialogTokenizer.DialogToken;
import static com.kyranadams.platformer.scenes.dialog.DialogTokenizer.DialogTokenType;

public class Dialog {

    private Map<String, Entity> entities;
    private List<DialogToken>[] lines;
    private final Stage stage;
    private Label renderText;
    public boolean isDone;

    public Dialog(String dialog, Map<String, Entity> entities, Stage stage) {
        this.entities = entities;
        this.stage = stage;
        lexDialog(dialog.split("\n"));
        renderText = new Label(null, new Label.LabelStyle());
        renderText.setVisible(false);
        stage.addActor(renderText);
    }
    private void lexDialog(String[] lines){
        this.lines = new List[lines.length];
        for (int i = 0; i < lines.length; i++){
            this.lines[i] = DialogTokenizer.tokenize(lines[i]);
        }
    }
    public void activate() {
        Parameters p = new Parameters();
        for (List<DialogToken> tokens : lines) {
            if (tokens.size() == 0) continue;
            if (tokens.get(0).type != DialogTokenType.WORD) {
                throw new RuntimeException("Expected command (" + tokens.get(0).string + ") to be word, was " + tokens.get(0).type);
            }
            String command = tokens.get(0).string;
            p.update(tokens);

            if (command.equals("speak")) {
                speak(p, tokens.get(0), tokens.get(tokens.size() - 1));
            }
        }
    }

    private void speak(Parameters p, final DialogToken character, final DialogToken dialog) {
        if (character.type != DialogTokenType.WORD) {
            throw new DialogParseException("Speak command must act on a character");
        }
        if (dialog.type != DialogTokenType.STRING) {
            throw new DialogParseException("Speak command must end with a string");
        }
        stage.addAction(Actions.after(Actions.sequence(
                startDialog(entities.get(character.string)),
                createAction(new Runnable(){
                    public void run(){
                        renderText.setText(dialog.string);
                    }
                }),
                endDialog()
        )));
    }

    private Action startDialog(final Actor actor) {
        return Actions.parallel(
                createAction(new Runnable() {
                    public void run() {
                        isDone = false;
                        renderText.setOrigin(actor.getX(), actor.getY());
                        renderText.setVisible(true);
                    }
                }),
                createAction(new Runnable() {
                    public void run() {
                        stage.getViewport().setScreenPosition((int) actor.getX() - stage.getViewport().getScreenWidth(), (int) actor.getY() - stage.getViewport().getScreenWidth());
                    }
                })
        );
    }

    private Action createAction(Runnable r) {
        RunnableAction ac = new RunnableAction();
        ac.setRunnable(r);
        return ac;
    }

    private Action endDialog() {
        return Actions.sequence(createAction(new Runnable() {
            public void run() {
                isDone = true;
                renderText.setVisible(false);
            }
        }));
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
                    for (int j = 0; j < tokensToComma.size(); j++) {
                        if (tokensToComma.get(j).type == DialogTokenizer.DialogTokenType.EQUALS) {
                            equalsIndex = j;
                            break;
                        }
                    }
                    if (equalsIndex != -1) {
                        if (equalsIndex != 1) {
                            throw new RuntimeException("Can only have one argument left of =");
                        }
                        if (tokensToComma.size() != 3) {
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
