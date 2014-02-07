package com.appsdgl.perfectgift.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

import com.appsdgl.perfectgift.screens.transitions.ScreenTransition;
import com.appsdgl.perfectgift.screens.transitions.ScreenTransitionSwipe;
import com.appsdgl.perfectgift.util.AudioManager;
import com.appsdgl.perfectgift.util.Constants;
import com.appsdgl.perfectgift.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;

public class MenuScreen extends AbstractGameScreen {

	public static final String TAG = MenuScreen.class.getName();

	private Stage stage;
	private Skin skinDits;
	private Skin skinLibgdx;

	// menu
//	private Image imgBackground;
	private Image imgTitle;
	private Image imgDits;
	
	private Button btnMenuPlay;
	private Button btnMenuOptions;
	private Button btnMenuCredits;

	// options
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;

	private boolean animDits;
	
	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	public MenuScreen (DirectedGame game, boolean animDits) {
		super(game);
		this.animDits = animDits;
	}

	@Override
	public void render (float deltaTime) {
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
	public void resize (int width, int height) {
		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, false);
	}

	@Override
	public void show () {
		stage = new Stage() {
			@Override
			public boolean keyDown(int keyCode) {
				if(keyCode == Keys.BACK || keyCode == Keys.ESCAPE){
					Gdx.app.exit();
					return true;
				}
				return super.keyDown(keyCode);
			}
		};
		rebuildStage();
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide () {
		stage.dispose();
		skinDits.dispose();
		skinLibgdx.dispose();
	}

	@Override
	public void pause () {
	}

	private void rebuildStage () {
		skinDits = new Skin(Gdx.files.internal(Constants.SKIN_DITS_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

		// build all layers
//		Table layerBackground = buildBackgroundLayer();
		Table layerObjects = buildObjectsLayer();
		Table layerTitle = buildTitleLayer();
		Table layerControls = buildControlsLayer();
		Table layerOptionsWindow = buildOptionsWindowLayer();

		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
//		stack.add(layerBackground);
		stack.add(layerObjects);
		stack.add(layerTitle);
		stack.add(layerControls);
		stage.addActor(layerOptionsWindow);
	}

	private Table buildObjectsLayer () {
		Table layer = new Table();

		// + Bunny
		imgDits = new Image(skinDits, "dits");
		
		if (animDits) {
			imgDits.addAction(
				sequence(
						moveTo(750 - imgDits.getWidth(), - imgDits.getHeight()), delay(1.5f), 
						moveBy(0, 70, 0.5f, Interpolation.linear),
						moveBy(0, -50, 0.5f, Interpolation.linear),
						moveBy(0, imgDits.getHeight() - 20, 1.5f, Interpolation.linear)));
		} else {
			imgDits.setPosition(750 - imgDits.getWidth(), 0);
		}
		layer.addActor(imgDits);
		return layer;
	}

	private Table buildTitleLayer () {
		Table layer = new Table();
		layer.padTop(30f);
		layer.center().top();

		// + Game Logo
		imgTitle = new Image(skinDits, "title");
		layer.add(imgTitle);
		layer.row().expandY();

		if (debugEnabled) layer.debug();
		return layer;
	}

	private Table buildControlsLayer () {
		Table layer = new Table();

		layer.left().bottom().align(Align.bottom|Align.left);
		layer.pad(30f, 30f, 30f, 0);
		
		// + Play Button
		btnMenuPlay = new Button(skinDits, "play");
		btnMenuPlay.align(Align.left);
		btnMenuPlay.left();
		btnMenuPlay.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onPlayClicked();
			}
		});
		Cell<?> cell = layer.add(btnMenuPlay);
		cell.align(Align.left);
		layer.row();
				
		// + Options Button
		btnMenuOptions = new Button(skinDits, "options");
		btnMenuOptions.align(Align.left);
		btnMenuOptions.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onOptionsClicked();
			}
		});
		layer.add(btnMenuOptions);		
		layer.row();
		
		// + Credits Button
		btnMenuCredits = new Button(skinDits, "credits");
		btnMenuCredits.align(Align.left);
		btnMenuCredits.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onCreditsClicked();
			}
		});
		layer.add(btnMenuCredits);
		
		if (debugEnabled) layer.debug();
		return layer;
	}

	private Table buildOptWinAudioSettings () {
		Table tbl = new Table();
		tbl.align(Align.center);
		
		// + Title: "Audio"
		tbl.pad(10, 10, 0, 10);
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", skinLibgdx);
		tbl.add(chkSound);
		tbl.add(new Label("Sonido", skinLibgdx));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldSound);
		tbl.row();
		// + Checkbox, "Music" label, music volume slider
		chkMusic = new CheckBox("", skinLibgdx);
		tbl.add(chkMusic);
		tbl.add(new Label("Musica", skinLibgdx));
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldMusic);
		tbl.row();
		return tbl;
	}

	private Table buildOptWinButtons () {
		Table tbl = new Table();
		// + Separator
		Label lbl = null;
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tbl.row();
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();
		// + Save Button with event handler
		btnWinOptSave = new TextButton("Guardar", skinLibgdx);
		tbl.add(btnWinOptSave).padRight(30);
		btnWinOptSave.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onSaveClicked();
			}
		});
		// + Cancel Button with event handler
		btnWinOptCancel = new TextButton("Cancelar", skinLibgdx);
		tbl.add(btnWinOptCancel);
		btnWinOptCancel.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onCancelClicked();
			}
		});
		return tbl;
	}

	private Table buildOptionsWindowLayer () {
		winOptions = new Window("Opciones", skinLibgdx);
		
		// + Audio Settings: Sound/Music CheckBox and Volume Slider
		winOptions.add(buildOptWinAudioSettings()).row();
		// + Separator and Buttons (Save, Cancel)
		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

		// Make options window slightly transparent
		winOptions.setColor(1, 1, 1, 0.8f);
		// Hide options window by default
		showOptionsWindow(false, false);
		if (debugEnabled) winOptions.debug();
		// Let TableLayout recalculate widget sizes and positions
		winOptions.pack();
		// Move options window to center right corner
		winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH / 2 - winOptions.getWidth()/2, 50);
		winOptions.align(Align.center);
		
		return winOptions;
	}

	private void onPlayClicked () {
		ScreenTransition transition = ScreenTransitionSwipe.init(0.75f, ScreenTransitionSwipe.LEFT);
		game.setScreen(new LevelsScreen(game), transition);
	}

	private void onOptionsClicked () {
		loadSettings();
		showMenuButtons(false);
		showOptionsWindow(true, true);
	}

	private void onCreditsClicked() {
		ScreenTransition transition = ScreenTransitionSwipe.init(0.75f, ScreenTransitionSwipe.LEFT);
		game.setScreen(new CreditsScreen(game), transition);
	}
	
	private void onSaveClicked () {
		saveSettings();
		onCancelClicked();
		AudioManager.instance.onSettingsUpdated();
	}

	private void onCancelClicked () {
		showMenuButtons(true);
		showOptionsWindow(false, true);
		AudioManager.instance.onSettingsUpdated();
	}

	private void loadSettings () {
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
	}

	private void saveSettings () {
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		prefs.save();
	}

	private void showMenuButtons (boolean visible) {
		float moveDuration = 1.0f;
		float delayOptionsButton = 0.25f;

		final Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;

		SequenceAction seq = sequence();
		if (visible) seq.addAction(delay(delayOptionsButton + moveDuration));
		seq.addAction(run(new Runnable() {
			public void run () {
				btnMenuPlay.setTouchable(touchEnabled);
				btnMenuOptions.setTouchable(touchEnabled);
				btnMenuCredits.setTouchable(touchEnabled);
			}
		}));
		stage.addAction(seq);
	}

	private void showOptionsWindow (boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		winOptions.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
	}

	@Override
	public InputProcessor getInputProcessor () {
		return stage;
	}
	
}
