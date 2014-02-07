/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package com.appsdgl.perfectgift.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenTransitionSwipe implements ScreenTransition {

	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;
	public static final int DOWN = 4;

	private static final ScreenTransitionSwipe instance = new ScreenTransitionSwipe();

	private float duration;
	private int direction;

	public static ScreenTransitionSwipe init (float duration, int direction) {
		instance.duration = duration;
		instance.direction = direction;
		return instance;
	}

	@Override
	public float getDuration () {
		return duration;
	}

	@Override
	public void render (SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {
		float w = currScreen.getWidth();
		float h = currScreen.getHeight();
		float x = 0;
		float x2 = 0;
		float y = 0;
		float y2 = 0;

		// calculate position offset
		switch (direction) {
		case LEFT:
			x = -w * alpha;
			x2 = x + w;
			y2 = y;
			break;
		case RIGHT:
			x = w * alpha;
			x2 = x - w;
			y2 = y;
			break;
		case UP:
			y = h * alpha;
			x2 = x;
			y2 = y - h;
			break;
		case DOWN:
			y = -h * alpha;
			x2 = x;
			y2 = y + h;
			break;
		}


		// finally, draw both screens
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(currScreen, x, y, 0, 0, w, h, 1, 1, 0, 0, 0, currScreen.getWidth(), currScreen.getHeight(), false, true);
		batch.draw(nextScreen, x2, y2, 0, 0, w, h, 1, 1, 0, 0, 0, nextScreen.getWidth(), nextScreen.getHeight(), false, true);
		batch.end();
	}

}
