package com.appsdgl.perfectgift.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class InputUtils {

	public static Vector2 screenToViewport(Vector2 screenPoint) {
		return screenToViewport(screenPoint, false);
	}
	
	public static Vector2 screenToViewport(Vector2 screenPoint, boolean flippedY) {
		int flip = flippedY ? -1 : 1;

		Vector2 viewportPoint = new Vector2();
		viewportPoint.x = screenPoint.x * (Constants.VIEWPORT_GUI_WIDTH / Gdx.graphics.getWidth());
		viewportPoint.y = screenPoint.y * (Constants.VIEWPORT_GUI_HEIGHT / Gdx.graphics.getHeight()) * flip;
		
		return viewportPoint;
	}
	
	public static Vector2 viewportToScreen(Vector2 viewportPoint) {
		return viewportToScreen(viewportPoint, false);
	}
	
	public static Vector2 viewportToScreen(Vector2 viewportPoint, boolean flippedY) {
		int flip = flippedY ? -1 : 1;

		Vector2 screenPoint = new Vector2();
		screenPoint.x = viewportPoint.x * (Gdx.graphics.getWidth() / Constants.VIEWPORT_GUI_WIDTH);
		screenPoint.y = viewportPoint.y * (Gdx.graphics.getHeight() / Constants.VIEWPORT_GUI_HEIGHT) * flip;
		
		return screenPoint;
	}
	
	public static Vector2 getScreenPointer() {
		return new Vector2(Gdx.input.getX(), Gdx.input.getY());
	}
	
	public static Vector2 getViewportPointer() {
		return getViewportPointer(false); 
	}
	
	public static Vector2 getViewportPointer(boolean flipY) {
		return screenToViewport(getScreenPointer(), flipY); 
	}
}
