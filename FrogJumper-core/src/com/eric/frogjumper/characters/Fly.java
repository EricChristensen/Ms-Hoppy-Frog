package com.eric.frogjumper.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.eric.frogjumper.Constants;

public class Fly implements Poolable, java.io.Serializable {

	transient Texture texture;
	public Vector2 position;
	public Vector2 velocity;
	Vector2 acceleration;
	public Rectangle bounds;
	float screen_height = 0;//Gdx.graphics.getHeight();
	float screen_width = 0;
	public float initialX;
	public float originalV;
	public boolean shouldDraw = false;
	Constants constants;
	
	public Fly(Vector2 position) {
		constants = new Constants();
		screen_width = constants.getRealWidth();
		screen_height = constants.getRealHeight();
		this.texture = new Texture(Gdx.files.internal("cartoonFly.png"));
		this.position = position;
		initialX = position.x;
		originalV = -screen_height / 640;
		this.velocity = new Vector2(screen_height / 1280, originalV);
		acceleration = new Vector2(-.02f * (screen_height / 640), 0); 
		this.bounds = new Rectangle(position.x, position.y, screen_width / 16, screen_width / 25);
	}
	
	public Fly(){
		constants = new Constants();
		screen_width = constants.getRealWidth();
		screen_height = constants.getRealHeight();
		this.texture = new Texture(Gdx.files.internal("cartoonFly.png"));
		originalV = -screen_height / 640;
		this.velocity = new Vector2(screen_height / 1280, originalV);
		acceleration = new Vector2(-.02f * (screen_height / 640), 0); 
	}
	
	public void init(Vector2 position/*, float originalV*/){
		//this.originalV = originalV;
		this.position = position;
		initialX = position.x;
		this.bounds = new Rectangle(position.x, position.y, screen_width / 16, screen_width / 25);
	}
	
	public void update(float camVel) {
		this.bounds.set(position.x, position.y, bounds.width, bounds.height);
		velocity.y = originalV + camVel;
		if (position.x < initialX && acceleration.x < 0) {
			acceleration.x *= -1;
		}
		if (position.x > initialX && acceleration.x > 0) {
			acceleration.x *= -1;
		}
		velocity.add(acceleration);
		this.position.add(velocity);
	}
	
	public void draw(SpriteBatch batch, Texture texture){
		batch.draw(texture, position.x, position.y, bounds.width, bounds.height);
	}

	@Override
	public void reset() {
		this.originalV = 0;
		this.position.set(0, 0);
		this.velocity.set(0, 0);
		this.acceleration.set(0,0);
		this.initialX = 0;
		this.bounds.setPosition(0, 0);
	}

}
