package com.eric.frogjumper.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {

	public Vector2 velocity;
	public Vector2 position;
	public Rectangle bounds;
	public boolean up = false;
	public boolean down = false;
	Texture texture;
	float screen_height = Gdx.graphics.getHeight();
	float screen_width = Gdx.graphics.getWidth();
	SpriteBatch batch;
	public boolean has_hit = false;
	
	public Block(Vector2 position) {
		this.texture = new Texture(Gdx.files.internal("BrokenArrow.png"));
		this.position = position;
		velocity = new Vector2(0, screen_height/ 1000);
		bounds = new Rectangle(position.x, position.y, screen_height / 10, screen_width / 10);
		batch = new SpriteBatch();
	}
	
	/*public void draw() {
		batch.begin();
		batch.draw(this.texture, position.x, position.y, bounds.width, bounds.height);
		batch.end();
	}*/
	
	public void draw(SpriteBatch batch1, float camPosY) {
		batch1.draw(this.texture, position.x, position.y , bounds.width, bounds.height);
	}
}
