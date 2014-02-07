package com.appsdgl.perfectgift.game.objects.gui;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.util.InputUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PlayButton extends AbstractGuiObject {

	private TextureRegion reg;  
	
	public PlayButton(float x, float y) {
		reg = Assets.instance.gui.play;
		
		dimension.set(reg.getRegionWidth(), reg.getRegionHeight());
		origin.set(0, 0);
		position.set(x, y);
		
		velocity.set(0, 0);
		terminalVelocity.set(0, 0);
		acceleration.set(0, 0);
		
		bounds.set(x, y, dimension.x, dimension.y);		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(reg.getTexture(), 
				position.x, position.y,
				origin.x, origin.y, 
				dimension.x, dimension.y, 
				scale.x, scale.y, 
				rotation,
				reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), 
				false, false);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (Gdx.input.isTouched()) {
			if (bounds.contains(InputUtils.screenToViewport(new Vector2(screenX, screenY)))) {
				return true;
			}
		}
		return false;
	}
	
}
