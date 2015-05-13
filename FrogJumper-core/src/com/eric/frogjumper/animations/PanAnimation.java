package com.eric.frogjumper.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.eric.frogjumper.listeners.MyGestureListener;

public class PanAnimation extends Animation{

	Vector2 startPosition;
	Vector2 endPosition;
	float magnitude;
	float angleBetween;
	MyGestureListener myGestureListener;
	public Rectangle bounds;
	TextureRegion region;
	Texture top;
	Texture base;
	TextureRegion topRegion;
	TextureRegion baseRegion;
	
	public PanAnimation(MyGestureListener myGestureListener) {
		this.myGestureListener = myGestureListener;
		//this.texture = new Texture(Gdx.files.internal("UpArraow.png"));
		this.top = new Texture(Gdx.files.internal("PointerTop.png"));
		this.base = new Texture(Gdx.files.internal("PointerBase.png"));
		this.topRegion = new TextureRegion(top);
		this.baseRegion = new TextureRegion(base);
		//this.region = new TextureRegion(texture);
		bounds = new Rectangle(0, 0, Gdx.graphics.getHeight() / 20, 0);
		startPosition = new Vector2(0, 0);
		endPosition = new Vector2();
		magnitude = magnitude();
		angleBetween = angleBetween();
	}

	private float angleBetween() {
		float x = (float) (this.endPosition.x - this.startPosition.x);
		float y = (float) (this.endPosition.y - this.startPosition.y);
		if (x == 0) {
			return 0;
		} else {
			float angle = (float) Math.atan(y / x) * (180 / (float) Math.PI);
			angle += 90;
			if (x < 0) {
				angle += 180;
			}
			angle += 180;
			//System.out.println(angle);
			return angle;
		}
	}

	private float magnitude() {
		return (float) Math.sqrt(Math.pow(startPosition.x - endPosition.x, 2) + Math.pow(startPosition.y - endPosition.y, 2));
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (!done){
			//batch.draw(this.baseRegion, startPosition.x, startPosition.y, bounds.width / 2, 0,
				//	bounds.width, bounds.height, 1, 1, this.angleBetween);
		//	batch.draw(this.topRegion, endPosition.x/* + (Gdx.graphics.getHeight() / 20)*/, endPosition.y - (Math.signum(endPosition.y - startPosition.y) * Gdx.graphics.getHeight() / 50), bounds.width / 2, 0,
			//		bounds.width, bounds.width, 1, 1, this.angleBetween);
			
			//batch.draw(texture, startPosition.x, startPosition.y, Gdx.graphics.getHeight() / 20, Gdx.graphics.getHeight() / 20);
			//batch.draw(texture, endPosition.x, endPosition.y, Gdx.graphics.getHeight() / 20, Gdx.graphics.getHeight() / 20);
		}
	}

	@Override
	public boolean check() {
		return myGestureListener.stopped;
	}

	@Override
	public void action() {
		this.startPosition.set(myGestureListener.position);
		this.endPosition.set(myGestureListener.panPosition);
		magnitude = magnitude();
		angleBetween = angleBetween();
		bounds.height = magnitude;
	}
	
	@Override
	public void start() {
		
	}
}
