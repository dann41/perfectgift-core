package com.appsdgl.perfectgift.screens;

import static com.appsdgl.perfectgift.util.GamePreferences.LEVELS;

import com.appsdgl.perfectgift.screens.transitions.ScreenTransition;
import com.appsdgl.perfectgift.screens.transitions.ScreenTransitionSwipe;
import com.appsdgl.perfectgift.util.Constants;
import com.appsdgl.perfectgift.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;

public class LevelsScreen extends AbstractGameScreen {

	private Stage stage;
	private Skin skinSelectLevel;
	private Skin skinBackgroundLevel;
	
	private Image imgTitle;
	
	private Button btnBack;
	private Button[] btnLevels;
	
	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	
	public LevelsScreen(DirectedGame game) {
		super(game);
		GamePreferences.instance.load();
	}

	boolean changePending = true;
	
	@Override
	public void render(float deltaTime) {
		
		Gdx.gl.glClearColor(0x92 / 255.0f, 0xD2 / 255.0f, 0xE7 / 255.0f, 1.0f);		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (debugEnabled) {
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0) {
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		stage.act(deltaTime);
		stage.draw();
		Table.drawDebug(stage);		
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, false);
	}

	@Override
	public void show() {
		stage = new Stage() {
			@Override
			public boolean keyDown(int keyCode) {
				if(keyCode == Keys.BACK || keyCode == Keys.ESCAPE){
					onBackClicked();
					return true;
				}
				return super.keyDown(keyCode);
			}
		};
		rebuildStage();
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		stage.dispose();
		skinBackgroundLevel.dispose();
		skinSelectLevel.dispose();
	}

	@Override
	public void pause() { }

	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}

	private void rebuildStage() {
		skinSelectLevel = new Skin(Gdx.files.internal(Constants.SKIN_DITS_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinBackgroundLevel = new Skin(Gdx.files.internal(Constants.SKIN_THUMBNAILS_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_THUMBNAILS));

		// build all layers
		Table layerTitle = buildTitleLayer();
		
		Table layerLevels = buildLevelsLayer(LEVELS);
		Table layerBackgrounds = buildBackgroundLevelsLayer(LEVELS);
		
		Table layerControls = buildControlsLayer();
		
		
		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackgrounds);
		stack.add(layerLevels);
		stack.add(layerTitle);
		stack.add(layerControls);
	}

	private Table buildTitleLayer() {
		Table layer = new Table();
		layer.padTop(10f);
		layer.center().top();

		// + Title
		imgTitle = new Image(skinSelectLevel, "title-world");
		imgTitle.setOrigin(imgTitle.getWidth() / 2, imgTitle.getHeight() / 2);
		imgTitle.setScale(0.6f, 0.6f);
		
		layer.add(imgTitle);

		if (debugEnabled) layer.debug();
		return layer;
	}
	
	private Table buildLevelsLayer(int levels) {
		Table layer = new Table();

		int ROW_COLUMNS = 3;
		btnLevels = new Button[levels];
		
		ChangeListener listener = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int level = Integer.valueOf(actor.getName());
				onWorldClicked(level);
			}
		}; 
		
		int rows = (levels / ROW_COLUMNS) + (levels % ROW_COLUMNS != 0 ? 1 : 0); 
		int row = 0;
		int column = 0;
		
		for (int i = 0; i < btnLevels.length; i++) {
			int level = i + 1;
			Button btn = new Button(skinSelectLevel, "level" + level);
			btn.setName(level + "");
			btn.addListener(listener);
			
			if (GamePreferences.instance.games[i]) {
				ButtonStyle style = btn.getStyle();
				style.up = style.down;
				btn.setStyle(style);
			}
			
			//btn.setBackground("level-completed");
			
			
			Cell<?> cell = layer.add(btn);

			if (column != ROW_COLUMNS - 1) {
				cell.padRight(25f);
			}
			
			if (row != rows - 1) {
				cell.padBottom(25f);
			}
			
			column++;
			if (column == ROW_COLUMNS) {
				column = 0;
				row++;
				layer.row();
			}
			
			btnLevels[i] = btn;
		}
		
		return layer;
	}
	
	private Table buildBackgroundLevelsLayer(int levels) {
		Table layer = new Table();
		layer.center();
		
		int ROW_COLUMNS = 3;
		Image[] imgLevels = new Image[levels];
		
		int rows = (levels / ROW_COLUMNS) + (levels % ROW_COLUMNS != 0 ? 1 : 0); 
		int row = 0;
		int column = 0;
		
		for (int i = 0; i < imgLevels.length; i++) {
			int level = i + 1;
			Image imgAux = new Image(skinBackgroundLevel, "bglevel" + level);
			Cell<?> cell = layer.add(imgAux);
			
			imgLevels[i] = imgAux;
			
			if (column != ROW_COLUMNS - 1) {
				cell.padRight(25f);
			}
			
			if (row != rows - 1) {
				cell.padBottom(25f);
			}
			
			column++;
			if (column == ROW_COLUMNS) {
				column = 0;
				row++;
				layer.row();
			}
		}
		
		return layer;		
	}	
	
	private Table buildControlsLayer() {
		Table layer = new Table();
		
		layer.pad(0f, 10f, 10f, 0);
		layer.left().bottom();	
		
		// + Back Button
		btnBack = new Button(skinSelectLevel, "back");
		btnBack.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onBackClicked();
			}
		});
		layer.add(btnBack);
		
		if (debugEnabled) layer.debug();
		return layer;
	}
	
	private void onBackClicked() {
		ScreenTransition transition = ScreenTransitionSwipe.init(0.75f, ScreenTransitionSwipe.RIGHT);
		game.setScreen(new MenuScreen(game, false), transition);
	}
	
	private void onWorldClicked(int world) {
		Gdx.app.debug("LEVEL", world + "");
		ScreenTransition transition = ScreenTransitionSwipe.init(0.75f, ScreenTransitionSwipe.LEFT);
		game.setScreen(new GameScreen(world, game), transition);
	}

}
