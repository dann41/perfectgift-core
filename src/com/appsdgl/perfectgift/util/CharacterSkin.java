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

package com.appsdgl.perfectgift.util;

import com.badlogic.gdx.graphics.Color;

public enum CharacterSkin {

	WHITE("White", 1.0f, 1.0f, 1.0f),

	GRAY("Gray", 0.7f, 0.7f, 0.7f),

	BROWN("Brown", 0.7f, 0.5f, 0.3f);

	private String name;
	private Color color = new Color();

	private CharacterSkin(String name, float r, float g, float b) {
		this.name = name;
		color.set(r, g, b, 1.0f);
	}

	@Override
	public String toString() {
		return name;
	}

	public Color getColor() {
		return color;
	}

}
