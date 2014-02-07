package com.appsdgl.perfectgift.game.objects;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.util.AudioManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Poop extends AbstractGameObject {

	private TextureRegion regPoop;
	private boolean visible = true;
	
	public Poop(Vector2 position, Vector2 velocity) {
		super();
		
		AudioManager.instance.play(Assets.instance.sounds.fart);
		
		regPoop = Assets.instance.dove.poop;
		
		this.dimension.set(regPoop.getRegionWidth(), regPoop.getRegionHeight());
		this.position = position;
		this.origin.set(0, 0);
		this.velocity = velocity;
		
		terminalVelocity.set(0f, Math.abs(velocity.y));
		acceleration.set(0, 0);
		
		this.bounds.set(0, 0, dimension.x, dimension.y);
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
		TextureRegion reg = regPoop;
		
		if (visible) {
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
		visible = false;
		position.y = 0 - bounds.height;
		Gdx.input.vibrate(200);
		AudioManager.instance.play(Assets.instance.sounds.ouch);
	}
}
