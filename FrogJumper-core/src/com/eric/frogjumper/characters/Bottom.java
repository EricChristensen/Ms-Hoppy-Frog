package com.eric.frogjumper.characters;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.eric.frogjumper.Constants;

public class Bottom {

	public Rectangle bigBounds;
	public List<Fly> flys;
	int i = 0;
	public int level;
	public int drawableFlies = 0;
	Sound yahoo;
	public int score;
	private boolean once = false;
	Constants constants;
	Texture flyTexture;
	float catchUp = .975f;
	
	public Bottom(int level, boolean infinityMode) {
		if (infinityMode) {
			catchUp = .990f;
		}
		flyTexture = new Texture(Gdx.files.internal("cartoonFly.png"));
		drawableFlies = 0;
		constants = new Constants();
		once = false;
		flys = new ArrayList<Fly>();
		bigBounds = new Rectangle(0, 0, 0, 0);
		i = 0;
		this.level = level;
		yahoo = Gdx.audio.newSound(Gdx.files.internal("yahoo.mp3"));
	}
	
	public void update(float camPos, float camVel) {
		//System.out.println("Bottom level " + level + " has " + flys.size() + " flies");
		this.bigBounds.height = constants.getRealHeight() / 20;
		this.bigBounds.width = flys.size() * constants.getRealHeight() / 25;
		this.bigBounds.x = constants.getLeft().x + constants.getLeft().width;
		if (i <= 0) {
			this.bigBounds.y = camPos - (bigBounds.height * level) - constants.getRealHeight() / 3f;
			i++;
		}
		if (this.bigBounds.y < camPos - (bigBounds.height * level) - constants.getRealHeight() / 2.6f + constants.getRealBottomY() && camVel > 0) {
			this.bigBounds.y += camVel * catchUp;
			//System.out.println("Here1");
		} else if (this.bigBounds.y < camPos -  (bigBounds.height * level) - constants.getRealHeight() / 2.6f + constants.getRealBottomY() && camVel <= 0){
			this.bigBounds.y += constants.getRealHeight() / 440;
			//System.out.println("Here2");
		} else {
			this.bigBounds.y = camPos - (bigBounds.height * level) - constants.getRealHeight() / 2.6f + constants.getRealBottomY();
			//System.out.println("Here3");
		}
		//System.out.println("Big Bounds: " + bigBounds.y);
		this.score = 0;
		if (level == 0) {
		//	System.out.println("cur Pos: " + (bigBounds.y + bigBounds.height));
		//	System.out.println("Needed Pos: " + (camPos - constants.getRealHeight() / 2));
		}
		if (bigBounds.y + bigBounds.height < camPos - constants.getRealHeight() / 2 && this.once == false && level == 0){
			yahoo.play();
			this.score = 2000;
			this.once = true;
		}
	//	this.bigBounds.y = camPos - constants.getRealHeight() / 3f;
		for (Fly fly : flys) {
			fly.position.y = bigBounds.y + bigBounds.height / 2;
		}
	}
	
	public void draw(SpriteBatch batch) {
		int i = 0;
		for (Fly fly : flys) {
			if (drawableFlies > i) {
				fly.draw(batch, flyTexture);
				//System.out.println("fly: " + i + " Position: " + fly.position);
			}
			i++;
		}
	}
	
	public void clear() {
		flys = new ArrayList<Fly>();
		drawableFlies = 0;
	}
	
	public boolean full() {
		return this.flys.size() >= 15;
	}

}
