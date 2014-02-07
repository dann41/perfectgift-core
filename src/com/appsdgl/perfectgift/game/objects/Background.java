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


package com.appsdgl.perfectgift.game.objects;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background extends AbstractGameObject {

	private TextureRegion regBackground;

	private int level;

	public Background (int level) {
		super();
		this.level = level;
		init();
	}

	private void init () {

		regBackground = null;
		
		switch(level) {
		case 1:
			regBackground = Assets.instance.levelBackground.bg1;
			break;
		case 2:
			regBackground = Assets.instance.levelBackground.bg2;
			break;
		case 3:
			regBackground = Assets.instance.levelBackground.bg3;
			break;
		case 4:
			regBackground = Assets.instance.levelBackground.bg4;
			break;
		case 5:
			regBackground = Assets.instance.levelBackground.bg5;
			break;
		case 6: 
			regBackground = Assets.instance.levelBackground.bg6;
			break;
		}
		
		dimension.set(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
	}

	@Override
	public void render (SpriteBatch batch) {
		TextureRegion reg = regBackground;
//		Gdx.app.debug("BCKG", "OBJ: " + position.toString() + " " + dimension.toString() + " " + origin.toString());
//		Gdx.app.debug("BCKG", "TXT: " + reg.getRegionX() + ":" + reg.getRegionY() + " " + reg.getRegionWidth()+":"+reg.getRegionHeight());
		
		batch.draw(reg, 
				position.x, position.y, 
				dimension.x, dimension.y);

	}

}
