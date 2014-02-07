package com.appsdgl.perfectgift.game.objects.gui;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.game.objects.AbstractGameObject;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DitsImages extends AbstractGameObject {

	public DitsImages() {
		super();
		
		setAnimation(Assets.instance.ditsImages.animDit);
		stateTime = 0f;
		
		TextureRegion reg = Assets.instance.ditsImages.animDit.getKeyFrame(0f);
		
		dimension.set(reg.getRegionWidth(), reg.getRegionHeight());
		origin.set(0, 0);
		position.set(20, Constants.VIEWPORT_GUI_HEIGHT / 2 - reg.getRegionHeight()/ 2);
		terminalVelocity.set(0f, 0f);
		
		bounds.set(0, 0, dimension.x, dimension.y);		
		
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = animation.getKeyFrame(stateTime, true);
		
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
