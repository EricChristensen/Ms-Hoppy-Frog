package com.eric.frogjumper.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class StarAnimation extends Animation{

	float screen_height = Gdx.graphics.getHeight();
	//Texture star;
	Vector2 position1;
	Vector2 position2;
	Vector2 velocity1;
	Vector2 velocity2;
	Vector2 acceleration1;
	Vector2 acceleration2;
	
	Vector2 lilyPos;
	private Vector2 lilyVel;
	private float camVel;
	public float deltaY = -screen_height;
	
	public StarAnimation() {
		//this.texture = new Texture();
		position1 = new Vector2(0, 0);
		position2 = new Vector2(0, 0);
		velocity1 = new Vector2(0, 0);
		velocity2 = new Vector2(0, 0);
		acceleration1 = new Vector2(0, 0);
		acceleration2 = new Vector2(0, 0);
		acceleration1.y = -screen_height / 3840;
		acceleration2.y = -screen_height / 3840;
		lilyPos = new Vector2(0, 0);
		lilyVel = new Vector2(0, 0);
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (!done) {
			batch.draw(this.texture, position1.x, position1.y, screen_height / 40, screen_height / 40);
			batch.draw(this.texture, position2.x, position1.y, screen_height / 40, screen_height / 40);
		}
	}

	@Override
	public void start() {
		//done = false;
		this.position1.set(lilyPos);
		this.position2.set(lilyPos);
		this.position1.x += screen_height / 25f;
		this.position2.x += screen_height / 25f;
		this.velocity1.y = screen_height / 250f;
		this.velocity2.y = screen_height / 250f;
		this.velocity1.x =(float) Math.sqrt(lilyVel.y) + screen_height / 2000f;
		this.velocity2.x =(float) Math.sqrt(lilyVel.y) * -1f - screen_height / 2000f;
		deltaY = 0;
	}

	public void setLilyPos(Vector2 lilyPos) {
		this.lilyPos = lilyPos;
	}
	
	public void setLilyVel(Vector2 lilyVel) {
		this.lilyVel = lilyVel;
	}
	
	public void setTexture(Texture starTexture) {
		this.texture = starTexture;
	}
	
	public void setCamVel(float camVel) {
		//System.out.println(this.camVel);
		this.camVel = camVel;
		//System.out.println(this.camVel);
	}
	
	@Override
	public boolean check() {
		//System.out.println("made it here and deltaY = " + deltaY);
		if (deltaY < -screen_height / 10){
			//deltaY = 0;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void action() {
		//System.out.println("Star action");
		this.position1.add(velocity1);
		this.position2.add(velocity2);
		this.position1.y += camVel;
		this.position2.y += camVel;
		this.velocity1.add(acceleration1);
		this.velocity2.add(acceleration2);
		deltaY += this.velocity1.y;
	}

}
