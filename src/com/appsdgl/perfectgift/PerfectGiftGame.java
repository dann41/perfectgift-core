package com.appsdgl.perfectgift;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.screens.DirectedGame;
import com.appsdgl.perfectgift.screens.MenuScreen;
import com.appsdgl.perfectgift.screens.transitions.ScreenTransition;
import com.appsdgl.perfectgift.screens.transitions.ScreenTransitionSlice;
import com.appsdgl.perfectgift.util.AudioManager;
import com.appsdgl.perfectgift.util.GamePreferences;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;

public class PerfectGiftGame extends DirectedGame {

	public AssetManager assetManager;
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// Load assets
		assetManager = new AssetManager();
		Assets.instance.init(assetManager);
		
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);

		// Start game at menu screen
		ScreenTransition transition = ScreenTransitionSlice.init(2, ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
		setScreen(new MenuScreen(this, true), transition);
//		setScreen(new LevelsScreen(this), transition);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		assetManager.dispose();
	}
}
