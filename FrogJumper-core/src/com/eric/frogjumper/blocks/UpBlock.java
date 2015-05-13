package com.eric.frogjumper.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class UpBlock extends Block {
	
	public UpBlock(Vector2 position) {
		super(position);
		this.texture = new Texture(Gdx.files.internal("UpArraow.png"));
		this.up = true;
		this.down = true;
	}

}
