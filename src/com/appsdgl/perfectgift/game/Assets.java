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

import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManager;

	public AssetFonts fonts;
	public AssetDitsImages ditsImages;
	
	public AssetDit dit;
	public AssetGift gift;
	public AssetCoin coin;
	public AssetBill bill;
	public AssetDove dove;
	public AssetLives lives;
	
	public AssetLevelDecoration levelDecoration;
	public AssetLevelBackground levelBackground;
	
	public AssetGui gui;
	
	public AssetSounds sounds;
	public AssetMusic music;

	// singleton: prevent instantiation from other classes
	private Assets () {
	}

	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;

		// set asset manager error handler
		assetManager.setErrorListener(this);
		
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_BACKGROUND, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_GAMEUI, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_DITS, TextureAtlas.class);
		assetManager.load("sounds/applause.wav", Sound.class);
		assetManager.load("sounds/bank_note_1.wav", Sound.class);
		assetManager.load("sounds/bank_note_2.wav", Sound.class);
		assetManager.load("sounds/coins_1.wav", Sound.class);
		assetManager.load("sounds/coins_2.wav", Sound.class);
		assetManager.load("sounds/dove_1.wav", Sound.class);
		assetManager.load("sounds/dove_2.wav", Sound.class);
		assetManager.load("sounds/dove_3.wav", Sound.class);
		assetManager.load("sounds/fart.wav", Sound.class);
		assetManager.load("sounds/gameover.wav", Sound.class);
		assetManager.load("sounds/ouch.wav", Sound.class);
		
		// load music
		assetManager.load("music/game_theme.ogg", Music.class);
		
		// start loading assets and wait until finished
		assetManager.finishLoading();

		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		TextureAtlas bgAtlas = assetManager.get(Constants.TEXTURE_ATLAS_BACKGROUND);
		TextureAtlas gameuiAtlas = assetManager.get(Constants.TEXTURE_ATLAS_GAMEUI);
		TextureAtlas ditsAtlas = assetManager.get(Constants.TEXTURE_ATLAS_DITS);
		
		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		// enable texture filtering for pixel smoothing
		for (Texture t : bgAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		for (Texture t : gameuiAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		for (Texture t : ditsAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		// create game resource objects
		fonts = new AssetFonts();
		dit = new AssetDit(atlas);
		gift = new AssetGift(atlas);
		coin = new AssetCoin(atlas);
		bill = new AssetBill(atlas);
		dove = new AssetDove(atlas);
		lives = new AssetLives(atlas);

		levelDecoration = new AssetLevelDecoration(atlas);
		levelBackground = new AssetLevelBackground(bgAtlas);

		gui = new AssetGui(gameuiAtlas);
		
		ditsImages = new AssetDitsImages(ditsAtlas);
		
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
		
		
	}

	@Override
	public void dispose () {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
		fonts.defaultHuge.dispose();
		
		fonts.menuFontText.dispose();
		fonts.menuFontTitle.dispose();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName  + "'", (Exception)throwable);
	}

	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		public final BitmapFont defaultHuge;

		public final BitmapFont menuFontTitle;
		public final BitmapFont menuFontText;
		
		public final BitmapFont guiFont;
		
		public AssetFonts () {
			// create three fonts using Libgdx's built-in 15px bitmap font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultHuge = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);

			// set font sizes
			defaultSmall.setScale(0.75f);
			defaultNormal.setScale(1.0f);
			defaultBig.setScale(2.0f);
			defaultHuge.setScale(4f);

			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultHuge.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);			
			
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/wonderful.ttf"));

			menuFontTitle = generator.generateFont(72, FreeTypeFontGenerator.DEFAULT_CHARS, false);
			menuFontText = generator.generateFont(32, FreeTypeFontGenerator.DEFAULT_CHARS, false);
			guiFont = generator.generateFont(72, FreeTypeFontGenerator.DEFAULT_CHARS, true);

			generator.dispose(); // don't forget to dispose to avoid memory leaks!
			
		}
	}

	public class AssetDitsImages {
		public final Animation animDit;

		public AssetDitsImages (TextureAtlas atlas) {
			Array<AtlasRegion> regions = atlas.findRegions("dit");
			animDit = new Animation(3f, regions, Animation.LOOP);
		}
	}
	
	public class AssetDit {
		public final AtlasRegion dit;
		public final Animation animDit;

		public AssetDit (TextureAtlas atlas) {
			dit = atlas.findRegion("dit1");

			Array<AtlasRegion> regions = atlas.findRegions("dit");
			animDit = new Animation(1f / 4f, regions, Animation.LOOP);
		}
	}

	public class AssetGift {
		public final AtlasRegion[] gift;

		public AssetGift (TextureAtlas atlas) {
			gift = new AtlasRegion[4];
			for (int i = 0; i < gift.length; i++) {
				gift[i] = atlas.findRegion("gift" + (i + 1));
			}
		}
	}

	public class AssetCoin {
		public final AtlasRegion coin;
//		public final Animation animCoin;

		public AssetCoin (TextureAtlas atlas) {
			coin = atlas.findRegion("euro");

			// Animation: Gold Coin
//			Array<AtlasRegion> regions = atlas.findRegions("anim_gold_coin");
//			AtlasRegion region = regions.first();
//			for (int i = 0; i < 10; i++)
//				regions.insert(0, region);
//			animCoin = new Animation(1.0f / 20.0f, regions, Animation.LOOP_PINGPONG);
		}
	}
	
	public class AssetDove {
		public final AtlasRegion dove;
		
//		public final Animation animDove;
		public final AtlasRegion poop;
//		public final Animation animPoop;
		
		public AssetDove (TextureAtlas atlas) {
			dove = atlas.findRegion("dove");

			// Animation: Gold Coin
//			Array<AtlasRegion> regions = atlas.findRegions("anim_gold_coin");
//			AtlasRegion region = regions.first();
//			for (int i = 0; i < 10; i++)
//				regions.insert(0, region);
//			animDove = new Animation(1.0f / 20.0f, regions, Animation.LOOP_PINGPONG);
			
			poop = atlas.findRegion("caca");
			
			// Animation: Gold Coin
//			Array<AtlasRegion> regions2 = atlas.findRegions("anim_gold_coin");
//			AtlasRegion region2 = regions2.first();
//			for (int i = 0; i < 10; i++)
//				regions2.insert(0, region2);
//			animPoop = new Animation(1.0f / 20.0f, regions2, Animation.LOOP_PINGPONG);
		}
	}	

	public class AssetBill {
		public final AtlasRegion bill;
//		public final Animation animBill;

		public AssetBill (TextureAtlas atlas) {
			bill = atlas.findRegion("billete");

			// Animation: Gold Coin
//			Array<AtlasRegion> regions = atlas.findRegions("anim_gold_coin");
//			AtlasRegion region = regions.first();
//			for (int i = 0; i < 10; i++)
//				regions.insert(0, region);
//			animBill = new Animation(1.0f / 20.0f, regions, Animation.LOOP_PINGPONG);
		}
	}	
	
	public class AssetLevelBackground {
		public final AtlasRegion bg1;
		public final AtlasRegion bg2;
		public final AtlasRegion bg3;
		public final AtlasRegion bg4;
		public final AtlasRegion bg5;
		public final AtlasRegion bg6;
		
		public AssetLevelBackground(TextureAtlas atlas) {
			bg1 = atlas.findRegion("bglevel1");
			bg2 = atlas.findRegion("bglevel2");
			bg3 = atlas.findRegion("bglevel3");
			bg4 = atlas.findRegion("bglevel4");
			bg5 = atlas.findRegion("bglevel5");
			bg6 = atlas.findRegion("bglevel6");
		}
	}

	public class AssetLevelDecoration {
		public final AtlasRegion ground;
		public final AtlasRegion groundRight;
		public final TextureRegion groundLeft;
		
		public AssetLevelDecoration (TextureAtlas atlas) {
			ground = atlas.findRegion("ground");
			groundRight = atlas.findRegion("ground-right");
			groundLeft = new TextureRegion(groundRight);
			groundLeft.flip(true, false);
		}
	}

	public class AssetLives {
		public final AtlasRegion lives;
		
		public AssetLives(TextureAtlas atlas) {
			lives = atlas.findRegion("lives");
		}
	}
	
	public class AssetGui {
		public final AtlasRegion pause;
		public final AtlasRegion play;
		public final AtlasRegion reset;
		public final AtlasRegion menu;
		public final AtlasRegion gift;
		
		public AssetGui(TextureAtlas atlas) {
			pause = atlas.findRegion("pause");
			play = atlas.findRegion("playcircle");
			reset = atlas.findRegion("refresh");
			menu = atlas.findRegion("tomenu");
			gift = atlas.findRegion("gift");
		}
	}
	
	
	public class AssetSounds {
		public final Sound applause;
		public final Sound bankNote1;
		public final Sound bankNote2;
		public final Sound coin1;
		public final Sound coin2;
		public final Sound dove1;
		public final Sound dove2;
		public final Sound dove3;
		public final Sound fart;
		public final Sound ouch;
		public final Sound gameOver;
		

		public AssetSounds (AssetManager am) {
			applause = am.get("sounds/applause.wav", Sound.class);
			bankNote1 = am.get("sounds/bank_note_1.wav", Sound.class);
			bankNote2 = am.get("sounds/bank_note_2.wav", Sound.class);
			coin1 = am.get("sounds/coins_1.wav", Sound.class);
			coin2 = am.get("sounds/coins_2.wav", Sound.class);
			dove1 = am.get("sounds/dove_1.wav", Sound.class);
			dove2 = am.get("sounds/dove_2.wav", Sound.class);
			dove3 = am.get("sounds/dove_3.wav", Sound.class);
			fart = am.get("sounds/fart.wav", Sound.class);
			ouch = am.get("sounds/ouch.wav", Sound.class);
			gameOver = am.get("sounds/gameover.wav", Sound.class);
		}
	}

	public class AssetMusic {
		public final Music song01;

		public AssetMusic (AssetManager am) {
			song01 = am.get("music/game_theme.ogg", Music.class);
		}
	}
	
}
