package com.appsdgl.perfectgift.game.objects.gui;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.game.objects.AbstractGameObject;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MenuImage extends AbstractGameObject {

	private TextureRegion reg;  
	
	public MenuImage() {
		reg = Assets.instance.gui.pause;
		
		float x = Constants.VIEWPORT_GUI_WIDTH - reg.getRegionWidth() - 15;
		float y = Constants.VIEWPORT_GUI_HEIGHT - reg.getRegionHeight() - 15;

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

}
