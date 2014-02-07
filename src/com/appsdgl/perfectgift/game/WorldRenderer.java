package com.appsdgl.perfectgift.game;

import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {

	protected static final String TAG = WorldRenderer.class.getName();

	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private WorldController worldController;

	public WorldRenderer (WorldController worldController) {
		this.worldController = worldController;
		init();
	}
	
	private void init () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		camera = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		camera.update();

		cameraGUI = new OrthographicCamera();
		cameraGUI.setToOrtho(true);
		cameraGUI.viewportWidth = Constants.VIEWPORT_GUI_WIDTH;
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.update();
	}

	public void render () {
		renderWorld(batch);
		renderGui(batch);
	}

	private void renderWorld (SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}

	private void renderGui (SpriteBatch batch) {
		worldController.cameraHelper.applyTo(cameraGUI);
		
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();

		renderGuiScore(batch);
		renderGuiTimeLeft(batch);
		renderGuiLive(batch);
		
		renderGuiPauseButton(batch);
		renderGuiPauseMenu(batch);
		
		batch.end();
	}

	private void renderGuiScore (SpriteBatch batch) {
		float x = 30;
		float y = 25;
		
		TextureRegion reg = Assets.instance.coin.coin;
		
		batch.draw(reg.getTexture(), 
				x, y,
				0, 0, 
				reg.getRegionWidth(), reg.getRegionHeight(), 
				1, 1, 
				0,
				reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), 
				false, true);
		
		Assets.instance.fonts.defaultBig.draw(batch, "" + (int)worldController.score, x + 45, y + 2);
	}

	private void renderGuiTimeLeft(SpriteBatch batch) {
		if (!worldController.isGameEnded()) {
			float x = Constants.VIEWPORT_GUI_WIDTH / 2;
			float y = 20;
			
			long secondsLeft = worldController.getTimeLeft() / 1000;
			int minutes = (int) secondsLeft / 60;
			int seconds = (int) secondsLeft % 60;
			String timeLeft = minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
			
			BitmapFont font = Assets.instance.fonts.defaultBig;
			font.drawMultiLine(batch, timeLeft, x, y, 1, BitmapFont.HAlignment.CENTER);
			
		}
	}
	
	private void renderGuiLive (SpriteBatch batch) {
		float x = Constants.VIEWPORT_GUI_WIDTH - 50 - Constants.LIVES_START * 50;
		float y = -15;
		
		for (int i = 0; i < Constants.LIVES_START; i++) {
			if (worldController.lives <= i) batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			batch.draw(Assets.instance.lives.lives, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
		}
	}

	private void renderGuiPauseButton(SpriteBatch batch) {
		if (!worldController.isPaused() && !worldController.isGameEnded()) {
			worldController.pause.render(batch);
		}
	}
	
	private void renderGuiPauseMenu(SpriteBatch batch) {
		if (worldController.menuPause != null) {
			batch.end();
			
			Gdx.gl.glEnable(GL10.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		    
		    shapeRenderer.setProjectionMatrix(cameraGUI.combined);

		    shapeRenderer.begin(ShapeType.Filled);
		    shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));
		    shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		    shapeRenderer.end();
		    
		    Gdx.gl.glDisable(GL10.GL_BLEND);
			
		    batch.begin();
		    
			worldController.menuPause.render(batch);
		}
	}
	
	public void resize (int width, int height) {

	}

	@Override
	public void dispose () {
		batch.dispose();
	}

}
