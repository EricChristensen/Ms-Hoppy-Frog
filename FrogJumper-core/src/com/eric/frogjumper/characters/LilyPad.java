package com.eric.frogjumper.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.eric.frogjumper.Constants;
import com.eric.frogjumper.listeners.MyGestureListener;

public class LilyPad {
	
	Texture texture;
	public Vector2 position;
	public Vector2 velocity;
	public Rectangle bounds;
	float screen_height = Gdx.graphics.getHeight();
	float screen_width = Gdx.graphics.getWidth();
	float pvx = 0;
	float pvy = 0;
	public MyGestureListener myGestureListener;
	float distanceTraveled = 0;
	public int counter = 0;
	public int counter2 = 0;
	boolean newPad = false;
	public boolean broken = false;
	int cycles = 0;
	Sound wallBounce;
	public int collisionsWithSamePad = 0;
	Constants constants;
	
	public LilyPad(Vector2 position, float camPos) {
		constants = new Constants();
		screen_height = constants.getRealHeight();
		screen_width = constants.getRealWidth();
		this.position = position;
		this.velocity = new Vector2(0, 0);
		this.texture = new Texture(Gdx.files.internal("cartoonLilyPad.png"));
		this.bounds = new Rectangle(/*this.position.x*/ -100, this.position.y, this.screen_height / 10f, this.screen_width / 15f);
		myGestureListener = new MyGestureListener(camPos, this.bounds.width);
		Gdx.input.setInputProcessor(new GestureDetector(myGestureListener));
		wallBounce = Gdx.audio.newSound(Gdx.files.internal("bounce.mp3"));
	}
	
	public void update(float camPos, float camVel) {
		if (myGestureListener.newPad) {
			counter = 0;
			myGestureListener.newPad = false;
		}
		myGestureListener.setCamPosition(camPos, camVel);
		this.position = myGestureListener.position;
		if (this.myGestureListener.velocity.x != this.pvx || this.myGestureListener.velocity.y != this.pvy){
			this.velocity.x = myGestureListener.velocity.x;
			this.velocity.y = myGestureListener.velocity.y;
			broken = false;
		}
		pvx = this.myGestureListener.velocity.x;
		pvy = this.myGestureListener.velocity.y;
		
		if (!myGestureListener.stopped && Gdx.input.isTouched()){
			float deltaX = /*myGestureListener.panPosition.x*/ (Gdx.input.getX() - this.bounds.width / 2) - this.position.x;
			float deltaY = /*myGestureListener.panPosition.y*/ (screen_height - Gdx.input.getY() + camPos - screen_height / 2) - this.position.y;
			this.velocity.x = Math.signum(deltaX) * (float) Math.sqrt(Math.signum(deltaX) * deltaX *(screen_height / 1280f)) / 1/*(float)Math.sqrt(20)*/;
			this.velocity.y = Math.signum(deltaY) * (float) Math.sqrt(Math.signum(deltaY) * deltaY *(screen_height / 1280f)) / 1/*float) Math.sqrt(20)*/ + camVel;
		}
		
		this.position.x += this.velocity.x;
		this.position.y += this.velocity.y;
		this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
		
		if (position.x < constants.getRealLeftX()){
			position.x = constants.getRealLeftX() + 1;
			velocity.x *= -.6f;
			if (myGestureListener.stopped) {
				wallBounce.play(.1f, 2f, 1f);
			}
		}
		if (position.x > constants.getRealRightX() - this.bounds.width){
			position.x = constants.getRealRightX() - this.bounds.width - 1;
			velocity.x *= -.6f;
			if (myGestureListener.stopped) {
				wallBounce.play(.1f, 2f, 1f);
			}
		}
		this.distanceTraveled = this.position.y - myGestureListener.startPosition.y;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(this.texture, this.position.x, this.position.y, this.bounds.width, this.bounds.height);
	}
	
	public void dispose() {
		
	}
}
