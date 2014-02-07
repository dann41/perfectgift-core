package com.appsdgl.perfectgift.game.objects;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.util.AudioManager;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Coin extends AbstractGameObject {

	private final static float MAX_SPEED_Y = 200;
	private final static float MIN_SPEED_Y = 75;
	
	private TextureRegion regCoin;
	
	public  boolean collected = false;
	private boolean visible = true;
	private int score = 10;
	
	public Coin() {
		regCoin = Assets.instance.coin.coin;
		
		float x = MathUtils.random(0, Constants.VIEWPORT_GUI_WIDTH - regCoin.getRegionWidth());
		float y = Constants.VIEWPORT_GUI_HEIGHT;

		dimension.set(regCoin.getRegionWidth(), regCoin.getRegionHeight());
		origin.set(0, 0);
		position.set(x, y);
		
		velocity.y = - MathUtils.random(MIN_SPEED_Y, MAX_SPEED_Y);
		terminalVelocity.set(0f, MAX_SPEED_Y);
		acceleration.set(0, 0);
		
		bounds.set(0, 0, dimension.x, dimension.y);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (position.y < 0 && visible) {
			visible = false;
			velocity.y = 0;
			position.y = -bounds.height;
			bounds.y = position.y;
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if (visible && !collected) {
			TextureRegion reg = null;
	
			reg = regCoin;
			
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

	public void hit() {
		collected = true;
		position.y = 0 - bounds.height;
		
		Sound sound = MathUtils.randomBoolean() ? Assets.instance.sounds.coin1 : Assets.instance.sounds.coin2;
		AudioManager.instance.play(sound);
	}
	
	public int getScore() {
		return score;
	}	
}
