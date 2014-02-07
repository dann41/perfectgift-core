package com.appsdgl.perfectgift.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {

	public static final String TAG = GamePreferences.class.getName();

	public static final int LEVELS = 6;

	
	public static final GamePreferences instance = new GamePreferences();

	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;

	public boolean[] games;
	
	private Preferences prefs;

	// singleton: prevent instantiation from other classes
	private GamePreferences () {
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	}

	public void load () {
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.3f), 0.0f, 1.0f);
		volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.8f), 0.0f, 1.0f);
		
		String game = prefs.getString("games", "");
		String[] gameResult = game.split(":");
		if (gameResult.length != LEVELS) {
			games = new boolean[LEVELS];
		} else {
			games = new boolean[LEVELS];
			int i = 0;
			for (String value : gameResult) {
				games[i++] = Boolean.valueOf(value);
			}
		}
	}

	public void save () {
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("volSound", volSound);
		prefs.putFloat("volMusic", volMusic);
		
		StringBuilder sb = new StringBuilder();
		
		for (boolean game : games) {
			sb.append(String.valueOf(game)).append(":");
		}
		prefs.putString("games", sb.substring(0, sb.length() - 1));
		
		prefs.flush();
	}

}
