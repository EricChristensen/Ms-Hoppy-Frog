package com.eric.frogjumper.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class DownBlock extends Block {

	public DownBlock(Vector2 position) {
		super(position);
		this.texture = new Texture(Gdx.files.internal("DownArrow.png"));
		this.up = true;
		this.down = true;
		this.velocity.y *= -1;
	}

}
