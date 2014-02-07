package com.appsdgl.perfectgift.game.objects;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ground extends AbstractGameObject {

	private TextureRegion regGround;
	private TextureRegion regGroundRight;
	private TextureRegion regGroundLeft;
	
	public Ground() {
		super();
		regGround = Assets.instance.levelDecoration.ground;
		regGroundRight = Assets.instance.levelDecoration.groundRight;
		regGroundLeft = Assets.instance.levelDecoration.groundLeft;
		
		position.set(0, 0);
		origin.set(0, 0);
		
		bounds.set(position.x, position.y, Gdx.graphics.getWidth(), regGround.getRegionHeight());
	}

	@Override
	public void render(SpriteBatch batch) {
		float currentX = position.x;
		int width = regGround.getRegionWidth();
		boolean firstTime = true;
		TextureRegion reg;
		
		while (currentX < Constants.VIEWPORT_GUI_WIDTH - width) {
			if (firstTime) {
				reg = regGroundRight;
				firstTime = false;
			} else {
				reg = regGround;
			}
			
			batch.draw(reg, currentX, position.y, width, reg.getRegionHeight());
			
			currentX += width;
		}
		
		// Draw left ground aligned to left
		reg = regGroundLeft;
		batch.draw(reg, Constants.VIEWPORT_GUI_WIDTH - width, position.y, width, reg.getRegionHeight());
 	}
	
}
