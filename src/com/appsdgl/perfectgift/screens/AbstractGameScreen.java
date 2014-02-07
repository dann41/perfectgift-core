package com.appsdgl.perfectgift.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public abstract class AbstractGameScreen implements Screen {

	protected DirectedGame game;

	public AbstractGameScreen (DirectedGame game) {
		this.game = game;
	}

	public abstract void render (float deltaTime);

	public abstract void resize (int width, int height);

	public abstract void show ();

	public abstract void hide ();

	public abstract void pause ();

	public abstract InputProcessor getInputProcessor ();

	public void resume () {
		System.out.println("Resume: " + this.game.getClass().getName());
	}

	public void dispose () {
		System.out.println("Dispose: " + this.game.getClass().getName());
	}

}
