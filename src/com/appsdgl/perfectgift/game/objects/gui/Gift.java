package com.appsdgl.perfectgift.game.objects.gui;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.game.objects.AbstractGameObject;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gift extends AbstractGameObject {

	private TextureRegion reg;  
	
	public Gift() {
		reg = Assets.instance.gui.gift;

		dimension.set(reg.getRegionWidth(), reg.getRegionHeight());
		origin.set(0, 0);
		position.set(Constants.VIEWPORT_GUI_WIDTH / 2 - reg.getRegionWidth() / 2, Constants.VIEWPORT_GUI_HEIGHT / 2 - reg.getRegionHeight() / 2);
		
		velocity.set(0, 0);
		terminalVelocity.set(0, 0);
		acceleration.set(0, 0);
		
		bounds.set(position.x, position.y, dimension.x, dimension.y);		
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
				false, true);
	}
}
