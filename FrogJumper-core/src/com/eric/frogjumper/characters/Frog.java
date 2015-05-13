package com.eric.frogjumper.characters;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.eric.frogjumper.Constants;
import com.eric.frogjumper.blocks.Block;

public class Frog {
	
	public float realAccelY = 0;
	public Vector2 position;
	public Vector2 velocity;
	public Vector2 acceleration;
	Texture texture;
	TextureRegion region;
	public Rectangle bounds;
	public float screen_height = Gdx.graphics.getHeight();
	public float screen_width = Gdx.graphics.getWidth();
	int hits = 0;
	public boolean has_hit = false;
	Vector2 lilyPos;
	float vPrev = 0;
	public int score = 0;
	private int height;
	public float factor = 1;
	public float rotation = 0;
	boolean rotateUp = false;
	public boolean rotateDown = false;
	float ABS_MAX_XV = screen_height * 35 / 2500;
	public float MAX_YA = -(screen_height / 30000) + ((screen_height * 42) * -(640f / 14000000));
	private float rotatePrev;
	public float maxHeight;
	Sound wallBounce;
	public float factorAdder = .0004f;
	float posPrev = 0;
	float posCurr = 0;
	public boolean justGotBoast = false;
	public float boastAccel = -(screen_height / 30000) + ((8 * screen_height) * -(540f / 14000000));
	public boolean aboveMaxPos = false;
	
	Sound boing;
	Constants constants;
	public float inbetweenAccel = 0;
	private float normalAccel;
	private float boastVel;
	//float MAX_XV = screen_height / 200;
	
	public Frog(Vector2 position) {
		maxHeight = 0;
		constants = new Constants();
		screen_height = constants.getRealHeight();
		screen_width = constants.getRealWidth();
		this.position = position;
		this.velocity = new Vector2(- screen_height / 8000, screen_height / 50);
		this.acceleration = new Vector2(0,  -(screen_height / 30000) + ((position.y) * -(540f / 14000000)));
		this.bounds = new Rectangle(this.position.x, this.position.y, screen_height / 15, screen_height / 15);
		this.texture = new Texture(Gdx.files.internal("TransFrog.jpg"));
		region = new TextureRegion(texture);
		lilyPos = new Vector2(0, 0);
		boing = Gdx.audio.newSound(Gdx.files.internal("bounce_boing.mp3"));
		wallBounce = Gdx.audio.newSound(Gdx.files.internal("bounce.mp3"));
	}
	
	public void update(LilyPad lilyPad, List<Block> blocks, float camPos) {
		this.score = 0;
		lilyPos.x = lilyPad.bounds.x;
		lilyPos.y = lilyPad.bounds.y;
		this.bounds = new Rectangle(this.position.x, this.position.y, screen_height / 20, screen_height / 20);
		performCollisions(lilyPad, blocks, camPos);
		checkBounds();
		vectorUpdates();
		reset();
		realAccelY = this.acceleration.y / factor;
	}
	
	private void performCollisions(LilyPad lilyPad, List<Block> blocks, float camPos) {
		
		if (this.bounds.overlaps(lilyPad.bounds)  && this.velocity.y <= 0 && !has_hit){
			this.rotation = 0;
			lilyPad.counter2 += 1;
			System.out.println("counter = " + lilyPad.counter);
			this.rotateDown = false;
			rotateUp = true;
			if (has_hit == false){
				hits += 1;
				has_hit = true;
			}
			calculateScore(lilyPad, camPos);
			calculateSpeeds(lilyPad, camPos);
			maintainSpeedLimits();
		} else if (this.bounds.overlaps(lilyPad.bounds) && (lilyPad.bounds.y > this.bounds.y + this.bounds.height / 2) && this.velocity.y > 0 && !has_hit){
			lilyPad.broken = true;
			System.out.println("Lily pos: " + lilyPad.position);
			System.out.println("Frog pos: " + this.position);
		} else if (!this.bounds.overlaps(lilyPad.bounds)) {
			has_hit = false;
		}
		
		rotations(); 
		
		for (Block block : blocks) {
			if (this.bounds.overlaps(block.bounds) && !block.has_hit) {
				//this.velocity.y += block.velocity.y;
				block.has_hit = true;
			} else if (this.bounds.overlaps(block.bounds)) {
				block.has_hit = false;
			}
		}
	}

	private void calculateSpeeds(LilyPad lilyPad, float camPos) {
		if (this.velocity.y < 0 && lilyPad.myGestureListener.stopped) {
			this.velocity.y *= - 1.25;
			lilyPad.counter++;
		}
		if (!lilyPad.myGestureListener.stopped) {
			this.velocity.y *= -.9f;
		}
		if (lilyPad.myGestureListener.stopped) {
			velocity.y += lilyPad.velocity.y;
		}
		float xPos = this.bounds.x + (this.bounds.width / 2) - lilyPad.bounds.x;
		velocity.x -= (lilyPad.bounds.width / 2f - xPos);
		velocity.x += lilyPad.velocity.x;
		int k = 4;
		float pitch = 1;
		float yPos = position.y - (camPos - screen_height / 2); 
		 if ( yPos > screen_height / 1.2f) {
				pitch = 1.7f;
		} else if (yPos > screen_height / 1.4f) {
			pitch = 1.6f;
		}else if (yPos > screen_height / 1.6f) {
			pitch = 1.5f;
		}else if (yPos > screen_height / 1.8f) {
			pitch = 1.4f;
		}else if (yPos > screen_height / 2f) {
			pitch = 1.3f;
		} else if (yPos > screen_height / 2.2f) {
			pitch = 1.2f;
		} else if (yPos > screen_height / 2.4f) {
			pitch = 1.1f;
		}else if (yPos > screen_height / 2.6f) {
			pitch = 1.0f;
		}	 else if (yPos > screen_height / 2.8f) {
			pitch = .9f;
		}   else if (yPos > screen_height / 3f) {
			pitch = .8f;
		}
		boing.play(.2f, pitch, 1f);
	}

	private void calculateScore(LilyPad lilyPad, float camPos) {
		if (lilyPad.counter < 2)
			height = (int)Math.pow((int)(screen_height)-(int)(this.position.y - camPos + screen_height / 2) + 1, 2) / (((int)screen_height * (int)screen_height) / (4* 1280));
		height += lilyPad.distanceTraveled ;
		if (lilyPad.velocity.y > 1)
			score = (int) Math.sqrt(lilyPad.velocity.y) * height;
		else
			score = height;
		score /= 20;
	}
	

	private void maintainSpeedLimits() {
		float MAX_XV = (position.y / 2500) * factor;
		if (velocity.x > MAX_XV || velocity.x > ABS_MAX_XV * factor) {
			velocity.x = Math.min(MAX_XV, ABS_MAX_XV * factor);				
		}
		if (velocity.x < -MAX_XV || velocity.x < -1*ABS_MAX_XV * factor) {
			velocity.x = -1 * Math.min(MAX_XV, ABS_MAX_XV * factor);
		}
		if (this.velocity.y < -75 * (screen_height / 1280)){
			this.velocity.y = -75 * (screen_height / 1280);
		}
	}

	private void rotations() {
		float xVal = Math.signum(this.velocity.x);
		//System.out.println(xVal);
		
		rotatePrev = rotation;
		if (rotateUp && rotation != 360) {
			rotation += 10 * xVal;
			/*if (rotation < 0) {
				rotation = 360 + rotation;
			}*/
			if (rotation >= 360 || rotation <= -360 || (rotatePrev < 0 && rotation > 0) || 
					(rotatePrev > 0 && rotation < 0)){
				rotateUp = false;
				rotation = 0;
			}
		}
	}
	
	private void checkBounds(){
		if (position.y < 0){
			position.y = 1;
			velocity.y *= -1;
		}
		if (position.x < constants.getRealLeftX()){
			position.x = constants.getRealLeftX() + 1;
			velocity.x *= -.9f;
			wallBounce.play(.1f, 1f, 1f);
		}
		if (position.x > constants.getRealRightX() - this.bounds.width){
			position.x = constants.getRealRightX() - this.bounds.width - 1;
			velocity.x *= -.9f;
			wallBounce.play(.1f, 1f, 1f);
		}
	}

	private void vectorUpdates() {
		this.position.x += this.velocity.x;
		this.position.y += this.velocity.y;
		vPrev = this.velocity.y;
		if (!justGotBoast) {
			this.velocity.y += this.acceleration.y;
			normalAccel = this.acceleration.y;
			inbetweenAccel = boastAccel;
			boastVel = this.velocity.y;
		} else if (justGotBoast && !rotateDown){
			this.velocity.y += this.boastAccel;
			boastVel = this.velocity.y;
		} else {
			inbetweenAccel += (boastAccel - normalAccel) * (boastAccel - normalAccel) * -1f / (.75f * boastVel);
			this.velocity.y += inbetweenAccel;//this.acceleration.y;
			
		}
				
		this.velocity.x += this.acceleration.x;
		if (vPrev >= 0 && this.velocity.y <= 0) {
			posCurr = this.position.y;
			this.maxHeight = this.position.y * (640f / screen_height);
			this.acceleration.y = Math.max((-(screen_height / 30000) + ((position.y) * -(540f / 14000000f))) * this.factor, MAX_YA * factor) ;
			if (aboveMaxPos && !this.justGotBoast) {
				this.MAX_YA += (posCurr - posPrev) * -(540f / 14000000f);
			}
			justGotBoast = false;
			posPrev = posCurr;
		}
		if (this.factor < 1) {
			this.factor += factorAdder;
		} else if (this.factor > 1) {
			this.factor = 1;
		}
		// For testing only. Not for final gameplay
		/*if (Gdx.input.isKeyPressed(Keys.A)){
			velocity.x = -10;
		}
		if (Gdx.input.isKeyPressed(Keys.W)){
			velocity.y = 1;
		}
		if (Gdx.input.isKeyPressed(Keys.D)){
			velocity.x = 10;
		}
		if (Gdx.input.isKeyPressed(Keys.S)){
			velocity.y = -10;
		}
		if (!Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)){
			velocity.x = 0;
		}
		if (!Gdx.input.isKeyPressed(Keys.W) && !Gdx.input.isKeyPressed(Keys.S)) {
			velocity.y = 0;
		}
		position.add(velocity);*/
	}
	
	private void reset() {
		if (Gdx.input.isKeyPressed(Input.Keys.R)){
			this.position = new Vector2(screen_width / 2, screen_height / 2);
			this.velocity = new Vector2(0, 0);
		}
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(this.region, this.position.x, this.position.y, this.bounds.width / 2, this.bounds.height / 2,//this.position.y,
				this.bounds.width, this.bounds.height, 1, 1, -1 * rotation);
		//System.out.println("Frog bounds: " + this.bounds.height);
//		batch.draw(this.texture, this.position.x, this.position.y, this.bounds.width, this.bounds.height);
	}
}
