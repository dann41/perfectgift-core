/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package com.appsdgl.perfectgift.game;

import java.io.IOException;
import java.util.Properties;

import com.appsdgl.perfectgift.game.objects.AbstractGameObject;
import com.appsdgl.perfectgift.game.objects.Background;
import com.appsdgl.perfectgift.game.objects.BankNote;
import com.appsdgl.perfectgift.game.objects.Coin;
import com.appsdgl.perfectgift.game.objects.Dit;
import com.appsdgl.perfectgift.game.objects.Dove;
import com.appsdgl.perfectgift.game.objects.Ground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Level {

	public static final String TAG = Level.class.getName();

	private WorldController worldController;
	private int level;

	// LEVEL PROPERTIES
	private long coinsFrequency;
	private long banknotesFrequency;
	private long dovesFrequency;
	
	private float doveMinSpeed;
	private float doveMaxSpeed;
	private int shitsPerDove;
	public int maxSecondsLevel;
	public int targetScore;
	
	// LEVEL OBJECTS
	private AbstractGameObject background;
	public AbstractGameObject ground;
	public AbstractGameObject dit;
	public Array<Coin> coins;
	public Array<BankNote> banks;
	public Array<Dove> doves;

	
	// Level controls
	private long lastCoinCreation;
	private long lastBankNoteCreation;
	private long lastDoveCreation;
	
	public Level (int level, String filename, WorldController worldController) {
		this.level = level;
		this.worldController = worldController;
		init(filename);
	}

	private void init (String filename) {
		Properties prop = new Properties();
		FileHandle fileHandle = Gdx.files.internal(filename);
		try {
			prop.load(fileHandle.read());
			
			coinsFrequency = Long.valueOf(prop.getProperty("coins_frequency"));
			banknotesFrequency = Long.valueOf(prop.getProperty("banknotes_frequency"));
			dovesFrequency = Long.valueOf(prop.getProperty("doves_frequency"));
			
			doveMinSpeed = Float.valueOf(prop.getProperty("dove-min-speed"));
			doveMaxSpeed = Float.valueOf(prop.getProperty("dove-max-speed"));
			shitsPerDove = Integer.valueOf(prop.getProperty("shits-per-dove"));

			maxSecondsLevel = Integer.valueOf(prop.getProperty("time"));
			targetScore = Integer.valueOf(prop.getProperty("min-score"));
			
			coinsFrequency = 60000 / coinsFrequency;
			banknotesFrequency = 60000 / banknotesFrequency;
			dovesFrequency = 60000 / dovesFrequency;
			
			lastCoinCreation = System.currentTimeMillis();
			lastBankNoteCreation = System.currentTimeMillis();
			lastDoveCreation = System.currentTimeMillis();
			
			background = new Background(level);
			ground = new Ground();
			dit = new Dit();
			
			doves = new Array<Dove>();
			coins = new Array<Coin>();
			banks = new Array<BankNote>();
			
		} catch (IOException e) {
			Gdx.app.debug(TAG, "Cannot load level " + filename + ". " + e.getMessage());
			Gdx.app.exit();
		}
	}
	
	public void update (float deltaTime) {
		if (!worldController.isPaused()) {
			dit.update(deltaTime);
		
			if (!worldController.isGameEnded()) {
				long now = System.currentTimeMillis();
				checkDoveCreation(now);
				checkCoinCreation(now);
				checkBankNoteCreation(now);
		
				for (AbstractGameObject object : coins) {
					object.update(deltaTime);
				}
		
				for (AbstractGameObject object : banks) {
					object.update(deltaTime);
				}
				
				for (AbstractGameObject object : doves) {
					object.update(deltaTime);
				}
			}
		}
	}

	public void render (SpriteBatch batch) {
		background.render(batch);

		if (!worldController.isGameEnded()) {
			for (AbstractGameObject object : coins) {
				object.render(batch);
			}
	
			for (AbstractGameObject object : banks) {
				object.render(batch);
			}
			
			for (AbstractGameObject object : doves) {
				object.render(batch);
			}
		}
		ground.render(batch);
		dit.render(batch);
	}
	
	private void checkDoveCreation(long now) {
		if (now > lastDoveCreation + dovesFrequency) {
			doves.add(new Dove(doveMinSpeed, doveMaxSpeed, shitsPerDove));
			lastDoveCreation = now; 
		}
	}
	
	private void checkCoinCreation(long now) {
		if (now > lastCoinCreation + coinsFrequency) {
			coins.add(new Coin());
			lastCoinCreation = now; 
		}
	}

	private void checkBankNoteCreation(long now) {
		if (now > lastBankNoteCreation + banknotesFrequency) {
			banks.add(new BankNote());
			lastBankNoteCreation = now; 
		}
	}

}
