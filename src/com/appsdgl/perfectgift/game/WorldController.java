package com.appsdgl.perfectgift.game;

import com.appsdgl.perfectgift.game.objects.AbstractGameObject;
import com.appsdgl.perfectgift.game.objects.BankNote;
import com.appsdgl.perfectgift.game.objects.Coin;
import com.appsdgl.perfectgift.game.objects.Dove;
import com.appsdgl.perfectgift.game.objects.Poop;
import com.appsdgl.perfectgift.game.objects.gui.AbstractGuiObject;
import com.appsdgl.perfectgift.game.objects.gui.Gift;
import com.appsdgl.perfectgift.game.objects.gui.PauseButton;
import com.appsdgl.perfectgift.game.objects.gui.PausedMenu;
import com.appsdgl.perfectgift.screens.DirectedGame;
import com.appsdgl.perfectgift.screens.GameScreen;
import com.appsdgl.perfectgift.screens.LevelsScreen;
import com.appsdgl.perfectgift.screens.transitions.ScreenTransition;
import com.appsdgl.perfectgift.screens.transitions.ScreenTransitionSlide;
import com.appsdgl.perfectgift.screens.transitions.ScreenTransitionSwipe;
import com.appsdgl.perfectgift.util.AudioManager;
import com.appsdgl.perfectgift.util.CameraHelper;
import com.appsdgl.perfectgift.util.Constants;
import com.appsdgl.perfectgift.util.GamePreferences;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class WorldController extends InputAdapter implements Disposable {

	public static final String TAG = WorldController.class.getName();

	private DirectedGame game;
	public int levelId;
	public Level level;
	public int lives;
	public float livesVisual;
	public int score;
	public float scoreVisual;
	
	public long gameStarted;
	
	public long elapsedTimeInPause;
	public long gamePaused;
	
	public CameraHelper cameraHelper;

	// Rectangles for collision detection
	private Rectangle rDit 	  = new Rectangle();
	private Rectangle rObject = new Rectangle();

	public AbstractGuiObject pause;
	public AbstractGuiObject menuPause; 
	private AbstractGameObject gift;
	
	private boolean paused;
	
	private boolean timeUp = false;
	
	public WorldController (int level, DirectedGame game) {
		this.levelId = level - 1;
		this.game = game;
		init();
	}

	private void init () {
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		paused = false;
		
		pause = new PauseButton();
		menuPause = null;
		gift = new Gift();
		
		initLevel();
	}

	private void initLevel () {
		score = 0;
		scoreVisual = score;
		level = new Level(levelId + 1, Constants.LEVEL_01, this);
		gameStarted = System.currentTimeMillis();
		elapsedTimeInPause = 0;
	}

	public void update (float deltaTime) {
		level.update(deltaTime);
		cameraHelper.update(deltaTime);
		
		pause.update(deltaTime);

		if (!isGameEnded() && !isPaused()) {
			testCollisions();
		}
		
		if (isGameCompleted() && !GamePreferences.instance.games[levelId]) {
			GamePreferences.instance.games[levelId] = true;
			GamePreferences.instance.save();
		}
	}

	private void testCollisions () {
		rDit.set(level.dit.position.x, level.dit.position.y, level.dit.bounds.width, level.dit.bounds.height);
		float limitY = level.ground.position.y + level.ground.bounds.height;
		
		for (Dove dove : level.doves) {
			for (Poop poop : dove.getPoops()) {
				rObject.set(poop.position.x, poop.position.y, poop.bounds.width, poop.bounds.height);
				if (!rDit.overlaps(rObject)) continue;
				if ((poop.position.y + poop.bounds.height) <= limitY) continue; 
				
				onCollisionWithPoop(poop);	
			}
		}

		for (Coin coin : level.coins) {
			if (coin.collected) continue;
			rObject.set(coin.position.x, coin.position.y, coin.bounds.width, coin.bounds.height);
			if (!rDit.overlaps(rObject)) continue;
			if ((coin.position.y + coin.bounds.height) <= limitY) continue;
			
			onCollisionWithCoin(coin);
		}

		for (BankNote bankNote : level.banks) {
			if (bankNote.collected) continue;
			rObject.set(bankNote.position.x, bankNote.position.y, bankNote.bounds.width, bankNote.bounds.height);
			if (!rDit.overlaps(rObject)) continue;
			if ((bankNote.position.y + bankNote.bounds.height) <= limitY) continue;
			
			onCollisionWithBankNote(bankNote);
		}
	}

	private void onCollisionWithPoop(Poop poop) {
		poop.hit();
		lives--;
		
		if (isGameOver()) {
			menuPause = new PausedMenu(false, true, true, null, "GAME OVER", this);
			AudioManager.instance.play(Assets.instance.sounds.gameOver);
		}
	}

	private void onCollisionWithCoin(Coin coin) {
		coin.hit();
		addScore(coin.getScore());
	}
	
	private void onCollisionWithBankNote(BankNote bankNote) {
		bankNote.hit();
		addScore(bankNote.getScore());
	}	
	
	private void addScore(int score) {
		this.score += score;
		
		if (isGameCompleted()) {
			menuPause = new PausedMenu(true, false, true, gift, "¡HAS GANADO UN REGALO!", this);
			AudioManager.instance.play(Assets.instance.sounds.applause);
		}
	}
	
	public boolean isGameOver () {
		return lives <= 0;
	}

	public boolean isTimeUp() {
		boolean isTimeUp = getPlayTime() > level.maxSecondsLevel * 1000;
		if (isTimeUp && !this.timeUp) {
			this.timeUp = isTimeUp;
			menuPause = new PausedMenu(false, true, true, null, "SE HA ACABADO EL TIEMPO!", this);
			AudioManager.instance.play(Assets.instance.sounds.gameOver);
		}
		return isTimeUp;
	}
	
	public long getPlayTime() {
		return (System.currentTimeMillis() - gameStarted) - getTimePaused();
	}
	
	public long getTimeLeft() {
		return level.maxSecondsLevel * 1000 - getPlayTime();
	}
	
	public long getTimePaused() {
		return elapsedTimeInPause + (isPaused() ? System.currentTimeMillis() - gamePaused : 0);
	}
	
	public boolean isGameCompleted () {
		return score >= level.targetScore;
	}

	public boolean isGameEnded() {
		return isGameCompleted() || isTimeUp() || isGameOver();
	}
	
	public void pausePressed() {
		paused = true;
		gamePaused = System.currentTimeMillis();
		menuPause = new PausedMenu(true, true, true, null, "PAUSA", this);
	}
	
	public void continuePressed() {
		paused = false;
		elapsedTimeInPause += (System.currentTimeMillis() - gamePaused);
		gamePaused = 0;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void toLevelSelectionPressed() {
		backToMenu();
	}
	
	@Override
	public boolean keyUp (int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			if (isPaused() || isGameEnded()) {
				backToMenu();	
			} else {
				pausePressed();
			}			
		} else if (keycode == Keys.P) {
			if (isPaused()) {
				continuePressed();
			} else {
				pausePressed();
			}
		}
		return false;
	}

	public void backToMenu () {
		// switch to menu screen
		ScreenTransition transition = ScreenTransitionSwipe.init(0.75f, ScreenTransitionSlide.RIGHT);
		game.setScreen(new LevelsScreen(game), transition);
	}
	
	public void nextGame() {
		if (levelId != 5) {
			game.setScreen(new GameScreen(levelId + 2, game));
		} else {
			backToMenu();
		}
	}

	public void reset() {
		init();
	}
	
	@Override
	public void dispose () {

	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pause != null && pause.touchDown(screenX, screenY, pointer, button)) {
			pausePressed();
			return true;
		} else if (menuPause != null && menuPause.touchDown(screenX, screenY, pointer, button)) {
			menuPause = null; // hide menuPause
			return true;
		}

		return false;
	}
}
