package com.appsdgl.perfectgift.screens;

import com.appsdgl.perfectgift.game.objects.AbstractGameObject;
import com.appsdgl.perfectgift.game.objects.gui.DitsImages;
import com.appsdgl.perfectgift.screens.transitions.ScreenTransition;
import com.appsdgl.perfectgift.screens.transitions.ScreenTransitionSwipe;
import com.appsdgl.perfectgift.util.CameraHelper;
import com.appsdgl.perfectgift.util.Constants;
import com.appsdgl.perfectgift.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class CreditsScreen extends AbstractGameScreen {

	private CameraHelper cameraHelper;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Stage stage;
	private Skin skinMenu;
	
	private AbstractGameObject ditsImages;
	
	private Button btnBack;
	
	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	
	public CreditsScreen(DirectedGame game) {
		super(game);
		
		cameraHelper = new CameraHelper();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		camera.viewportWidth = Constants.VIEWPORT_GUI_WIDTH;
		camera.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		
		batch = new SpriteBatch();
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
		
		cameraHelper.applyTo(camera);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		ditsImages.update(deltaTime);
		ditsImages.render(batch);
		batch.end();
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
		skinMenu.dispose();
		batch.dispose();
	}

	@Override
	public void pause() { }

	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}

	private void rebuildStage() {
		skinMenu = new Skin(Gdx.files.internal(Constants.SKIN_DITS_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

		// build all layers
		Table layerTitle = buildTitleLayer();
		
		buildPhotos();
		Table layerMessage = buildMessageLayer();
		
		Table layerControls = buildControlsLayer();
		
		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerMessage);
		stack.add(layerTitle);
		stack.add(layerControls);
	}

	private Table buildTitleLayer() {
		Table layer = new Table();
		layer.padTop(10f);
		layer.center().top();

		// + Title
		Image imgTitle = new Image(skinMenu, "credits-title");
		imgTitle.setOrigin(imgTitle.getWidth() / 2, imgTitle.getHeight() / 2);
		imgTitle.setScale(0.6f, 0.6f);
		
		layer.add(imgTitle);


		if (debugEnabled) layer.debug();
		return layer;
	}
	
	private void buildPhotos() {
		ditsImages = new DitsImages();
	}
	
	private Table buildMessageLayer() {
		Table layer = new Table();
		layer.right().bottom();
		
		Image imgText = new Image(skinMenu, "credits-text");
		imgText.setOrigin(imgText.getWidth() / 2, imgText.getHeight() / 2);
		imgText.setScale(0.9f, 0.9f);

		layer.add(imgText);
		
		final ScrollPane scroller = new ScrollPane(layer);

        final Table table = new Table();
        table.setFillParent(true);
        table.add(scroller).fill().expand();

		return layer;		
	}	
	
	private Table buildControlsLayer() {
		Table layer = new Table();
		
		layer.pad(0f, 10f, 10f, 0);
		layer.left().bottom();	
		
		// + Back Button
		btnBack = new Button(skinMenu, "back");
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
}
