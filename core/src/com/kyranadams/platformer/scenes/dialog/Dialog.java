package com.kyranadams.platformer.scenes.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kyranadams.platformer.CameraController;
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
    private CameraController cameraController;
    public boolean isDone;

    public Dialog(String dialog, Map<String, Entity> entities, Stage stage, CameraController cameraController) {
        this.entities = entities;
        this.stage = stage;
        lexDialog(dialog.split("\n"));
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/munro/munroNarrow.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        renderText = new Label(null, new Label.LabelStyle(font, Color.RED));
        renderText.setVisible(false);
        stage.addActor(renderText);
        this.cameraController = cameraController;
    }
    private void lexDialog(String[] lines){
        this.lines = new List[lines.length];
        for (int i = 0; i < lines.length; i++){
            this.lines[i] = DialogTokenizer.tokenize(lines[i]);
        }
    }
    public void activate() {
        DialogParameters p = new DialogParameters();
        for (List<DialogToken> tokens : lines) {
            if (tokens.size() == 0) continue;
            if (tokens.get(0).type != DialogTokenType.WORD) {
                throw new RuntimeException("Expected command (" + tokens.get(0).string + ") to be word, was " + tokens.get(0).type);
            }
            String command = tokens.get(0).string;
            p = p.update(tokens);

            if (command.equals("speak")) {
                speak(p, tokens.get(1), tokens.get(tokens.size() - 1));
            }
        }
    }

    private void speak(DialogParameters p, final DialogToken character, final DialogToken dialog) {
        float timeWait = Float.valueOf(p.getParameter("timeAfterSpeech"));
        float timePerLetter = Float.valueOf(p.getParameter("timePerLetter"));

        if (character.type != DialogTokenType.WORD) {
            throw new DialogParseException("Speak command must act on a character");
        }
        if (dialog.type != DialogTokenType.STRING) {
            throw new DialogParseException("Speak command must end with a string");
        }
        if(!entities.containsKey(character.string)){
            throw new DialogParseException("Character \"" + character.string + "\" not specified in entities");
        }
        // add each character by itself
        Action[] textActions = new Action[dialog.string.length() * 2];
        for(int i = 0; i < dialog.string.length(); i++){
            final int j = i;
            textActions[2*i] = createAction(new Runnable() {
                @Override
                public void run() {
                    renderText.getText().append(dialog.string.charAt(j));
                    renderText.invalidate();
                }
            });
            textActions[2 * i + 1] = Actions.delay(timePerLetter);
        }
        stage.addAction(Actions.after(Actions.sequence(
                startDialog(entities.get(character.string)),
                Actions.sequence(textActions),
                Actions.delay(timeWait),
                endDialog()
        )));
    }

    private Action startDialog(final Actor actor) {
        return Actions.parallel(
                createAction(new Runnable() {
                    public void run() {
                        isDone = false;
                        renderText.setPosition(actor.getX(), actor.getY() + actor.getHeight() + renderText.getHeight());
                        renderText.setVisible(true);
                    }
                }),
                createAction(new Runnable() {
                    public void run() {
                        cameraController.centerCamera(actor.getX(), actor.getY(), 1);
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


}
