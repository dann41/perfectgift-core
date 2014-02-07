package com.appsdgl.perfectgift.game.objects.gui;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.game.WorldController;
import com.appsdgl.perfectgift.game.objects.AbstractGameObject;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PausedMenu extends AbstractGuiObject {

	private WorldController worldController;
	
	private AbstractGuiObject play;
	private AbstractGuiObject reset;
	private AbstractGuiObject menu;
	
	private AbstractGameObject image;
	private AbstractGameObject title;
	
	public PausedMenu(boolean showPlay, boolean showReset, boolean showMenu, AbstractGameObject image, String title, WorldController worldController) {
		this.worldController = worldController;
		
		int buttons = 0;
		buttons += showPlay  ? 1 : 0;
		buttons += showReset ? 1 : 0;
		buttons += showMenu  ? 1 : 0;
		
		float y = Constants.VIEWPORT_GUI_HEIGHT / 2 + 150;

		int i = 0;
		
		if (showReset) {
			float x = getXPosition(i++, buttons);
			reset = new ResetButton(x, y);
		}
		
		if (showMenu) {
			float x = getXPosition(i++, buttons);
			menu = new MenuButton(x, y);
		}

		if (showPlay) {
			float x = getXPosition(i++, buttons); 
			play = new PlayButton(x, y);
		}
		
		this.image = image;

		if (title != null) {
			if (image != null) {
				this.title = new Title(title, 50);
			} else {
				this.title = new Title(title, 150);
			}
		}
	}

	private float getXPosition(int button, int totalButtons) {
		int width = Assets.instance.gui.play.getRegionWidth();
		int space = 15;
		float x = 0f;
		if (totalButtons % 2 == 0) {
			float numSpaces = Math.abs(totalButtons/2 - button);
			if (button < totalButtons / 2) {
				x = Constants.VIEWPORT_GUI_WIDTH / 2 - (width + space) * numSpaces + 0.5f * space;
			} else {
				x = Constants.VIEWPORT_GUI_WIDTH / 2 + (width + space) * numSpaces + 0.5f * space;
			}
		} else {
			int numSpaces = Math.abs(totalButtons/2 - button);
			if (button < totalButtons / 2) {
				x = Constants.VIEWPORT_GUI_WIDTH / 2 - (width / 2) - (width + space) * numSpaces;
			} else if (button > totalButtons / 2) {
				x = Constants.VIEWPORT_GUI_WIDTH / 2 - (width / 2) + (width + space) * numSpaces;
			} else {
				x = Constants.VIEWPORT_GUI_WIDTH / 2 - (width / 2);
			}
		}
		return x;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if (title != null) {
			title.render(batch);
		}
		
		if (image != null) {
			image.render(batch);
		}
		
		if (play != null) {
			play.render(batch);
		}
		
		if (reset != null) {
			reset.render(batch);
		}
		
		if (menu != null) {
			menu.render(batch);
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (play != null && play.touchDown(screenX, screenY, pointer, button)) {
			if (worldController.isPaused()) {
				worldController.continuePressed();
			} else {
				worldController.nextGame();
			}
			return true;
		} else if (reset != null && reset.touchDown(screenX, screenY, pointer, button)) {
			worldController.reset();
			return true;
		} else if (menu != null && menu.touchDown(screenX, screenY, pointer, button)) {
			worldController.backToMenu();
			return true;
		}
		return false;
	}
	
}
