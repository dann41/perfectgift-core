package com.appsdgl.perfectgift.game.objects;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.util.AudioManager;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class BankNote extends AbstractGameObject {

	private final static float MAX_SPEED_Y = 300;
	private final static float MIN_SPEED_Y = 100;

	private TextureRegion regBankNote;

	public boolean collected = false;
	private boolean visible = true;
	private int score = 100;

	public BankNote() {
		regBankNote = Assets.instance.bill.bill;

		float x = MathUtils.random(0, Constants.VIEWPORT_GUI_WIDTH - regBankNote.getRegionWidth());
		float y = Constants.VIEWPORT_GUI_HEIGHT;

		dimension.set(regBankNote.getRegionWidth(), regBankNote.getRegionHeight());
		origin.set(0, 0);
		position.set(x, y);

		velocity.y = -MathUtils.random(MIN_SPEED_Y, MAX_SPEED_Y);
		terminalVelocity.set(0f, MAX_SPEED_Y);
		acceleration.set(0, 0);

		bounds.set(0, 0, dimension.x, dimension.y);

		rotation = 0;
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

			reg = regBankNote;

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
		
		Sound sound = MathUtils.randomBoolean() ? Assets.instance.sounds.bankNote1 : Assets.instance.sounds.bankNote2;
		AudioManager.instance.play(sound);
	}

	public int getScore() {
		return score;
	}
}
