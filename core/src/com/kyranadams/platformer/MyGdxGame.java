package com.kyranadams.platformer;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {

    @Override
    public void create() {
        setScreen(new SplashScreen(this));
    }

}
