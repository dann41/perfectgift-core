package com.appsdgl.perfectgift.game.objects;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.util.AudioManager;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Dove extends AbstractGameObject {

	private final static float MAX_Y = 100;
	private final static float MIN_Y = 0;

	private Direction direction;	
	private boolean visible = true;
	
	private TextureRegion regDove;
	
	private Array<Poop> poops;
	
	private int numPoops;
	private long timeLastPoop;
	private long millisPerPoop;
	
	private long timeDoveVisible;
	private long nextSound;
	
	public Dove(float minSpeed, float maxSpeed, int shits) {
		
		this.numPoops = shits;

		this.regDove = Assets.instance.dove.dove;
		
		this.direction = MathUtils.randomBoolean() ? Direction.RIGHT : Direction.LEFT;
		int dir = direction == Direction.RIGHT ? 1 : -1;
		this.velocity.x = dir * MathUtils.random(minSpeed, maxSpeed);
		this.terminalVelocity.set(Math.abs(velocity.x), 0f);
		this.acceleration.set(0, 0);
		
		this.position.x = direction == Direction.RIGHT ? 0 - regDove.getRegionWidth() : Constants.VIEWPORT_GUI_WIDTH;
		this.position.y = Constants.VIEWPORT_GUI_HEIGHT - MathUtils.random(MIN_Y, MAX_Y) - regDove.getRegionHeight();
		
		this.dimension.set(regDove.getRegionWidth(), regDove.getRegionHeight());
		this.origin.set(0, 0);
		
		this.bounds.set(0, 0, dimension.x, dimension.y);
		
		this.timeDoveVisible = (long) Math.abs(1000 * Constants.VIEWPORT_GUI_WIDTH / velocity.x);
		
		this.timeLastPoop = System.currentTimeMillis();
		this.millisPerPoop = timeDoveVisible / numPoops - MathUtils.random(50, 300);
		
		this.poops = new Array<Poop>();
		
		this.nextSound = System.currentTimeMillis() + (MathUtils.random(0, 3) * 1000);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (position.x < 0 - regDove.getRegionWidth() && direction == Direction.LEFT 
				|| position.x > Constants.VIEWPORT_GUI_WIDTH && direction == Direction.RIGHT) {
			visible = false;
		} else {
			long now = System.currentTimeMillis();
			if (now > timeLastPoop + millisPerPoop) {
				// same Y velocity as dove X velocity
				Vector2 poopPos = position.cpy();
				if (direction == Direction.LEFT) {
					poopPos.x += regDove.getRegionWidth();
				}
				
				poops.add(new Poop(poopPos, new Vector2(0, - Math.abs(velocity.x))));
				timeLastPoop = now;
			}
			
			if (now > nextSound) {
				playSound();
				nextSound = now + (MathUtils.random(3, 7) * 1000);
			}
		}
		
		for (AbstractGameObject poop : poops) {
			poop.update(deltaTime);
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if (visible) {
			TextureRegion reg = null;
	
			reg = regDove;
			
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
		
		for (AbstractGameObject poop : poops) {
			poop.render(batch);
		}
	}

	public Array<Poop> getPoops() {
		return poops;
	}

	private void playSound() {
		int number = MathUtils.random(0, 2);
		Sound sound;
		switch (number) {
		case 0:
			sound = Assets.instance.sounds.dove1;
			break;
		case 1:
			sound = Assets.instance.sounds.dove2;
			break;
		case 2:
		default:
			sound = Assets.instance.sounds.dove3;
		}
		
		AudioManager.instance.play(sound);
	}
}
