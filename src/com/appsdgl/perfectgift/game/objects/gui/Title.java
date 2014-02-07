package com.appsdgl.perfectgift.game.objects.gui;

import com.appsdgl.perfectgift.game.Assets;
import com.appsdgl.perfectgift.game.objects.AbstractGameObject;
import com.appsdgl.perfectgift.util.Constants;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Title extends AbstractGameObject {

	private BitmapFont font;
	private String text;
	
	public Title(String text, float y) {
		
		float x = Constants.VIEWPORT_GUI_WIDTH / 2;
		
		this.text = text;
		this.font = Assets.instance.fonts.guiFont;
		
		position.set(x, y);
	}

	@Override
	public void render(SpriteBatch batch) {
		font.setColor(1f, 1f, 1f, 1);
		font.drawMultiLine(batch, text, position.x, position.y, 1, BitmapFont.HAlignment.CENTER);
		font.setColor(1, 1, 1, 1);
	}
}
