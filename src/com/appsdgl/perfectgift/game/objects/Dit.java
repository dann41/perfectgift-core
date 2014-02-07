package com.appsdgl.perfectgift.game.objects;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Dit extends AbstractGameObject {
	
	private static final float SPEED = 300f;
	
	private Direction direction;
	
	private TextureRegion regDit;
	
	private boolean accelerometerAvailable;
	
	public Dit() {
		super();
		
		direction = Direction.RIGHT;
		regDit = Assets.instance.dit.dit;
		
		setAnimation(Assets.instance.dit.animDit);
		stateTime = MathUtils.random(0.0f, 1.0f);
		
		accelerometerAvailable = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
		
		dimension.set(regDit.getRegionWidth(), regDit.getRegionHeight());
		origin.set(0, 0);
		position.set((Constants.VIEWPORT_GUI_WIDTH - regDit.getRegionWidth()) / 2, 25);
		terminalVelocity.set(SPEED, 0f);
		
		bounds.set(0, 0, dimension.x, dimension.y);
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;

		reg = animation.getKeyFrame(stateTime, true);
		
		if (direction == Direction.RIGHT) {
			batch.draw(reg.getTexture(), 
					position.x, position.y, 
					origin.x, origin.y,
					dimension.x, dimension.y,
					scale.x, scale.y,
					rotation,
					reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
		} else {
			batch.draw(reg.getTexture(), 
					position.x, position.y, 
					origin.x, origin.y,
					dimension.x, dimension.y,
					scale.x, scale.y,
					rotation,
					reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(),
					true, false);
		}
	}

	@Override
	public void update(float deltaTime) {
		handleInputGame(deltaTime);
		clampPosition();
		
		super.update(deltaTime);
	}
	
	private void handleInputGame (float deltaTime) {
		if (accelerometerAvailable) {
			float accelerometer = Gdx.input.getAccelerometerY() / 10;
			
			// ignore accelerometer if has low values
			if (accelerometer < 0.02f && accelerometer > -0.02f)
				return;
			
			int speedDirection = accelerometer > 0 ? 1 : -1;
			velocity.x = SPEED * speedDirection * (0.5f + Math.abs(accelerometer));
			
			this.direction = speedDirection == 1 ? Direction.RIGHT : Direction.LEFT;
		} else {
			int touchedX = Gdx.input.getX();

			if (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isTouched() && touchedX < Constants.VIEWPORT_GUI_WIDTH / 2)) {
				velocity.x = -SPEED;
				this.direction = Direction.LEFT;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isTouched() && touchedX > Constants.VIEWPORT_GUI_WIDTH / 2)) {
				velocity.x = SPEED;
				this.direction = Direction.RIGHT;
			} else {
				velocity.x = 0;
			}
		}
	}
	
	private void clampPosition() {
		position.x = MathUtils.clamp(position.x, 0, Constants.VIEWPORT_GUI_WIDTH - regDit.getRegionWidth());
	}
}
