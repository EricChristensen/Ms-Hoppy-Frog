package com.eric.frogjumper.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.eric.frogjumper.Constants;
import com.eric.frogjumper.characters.Frog;

public class LilyAnimation extends Animation {

	Texture leftTex;
	Texture rightTex;
	Vector2 leftPos;
	Vector2 rightPos;
	Vector2 leftVel;
	Vector2 rightVel;
	Vector2 lilyPos;
	Vector2 lilyVel;
	float width = Gdx.graphics.getHeight() / 20f;
	float height = Gdx.graphics.getHeight() / 30f;
	Sound tear;
	Frog frog;
	Constants constants;
	
	public LilyAnimation(Frog frog) {
		leftTex = new Texture(Gdx.files.internal("cartoonLeftPad.png"));
		rightTex = new Texture(Gdx.files.internal("cartoonRightPad.png"));
		leftPos = new Vector2(0, 0);
		rightPos = new Vector2(width * 7, 0);
		leftVel = new Vector2(0, 0);
		rightVel = new Vector2(0, 0);
		lilyPos = new Vector2(0, 0);
		lilyVel = new Vector2(0, 0);
		tear = Gdx.audio.newSound(Gdx.files.internal("tearing.mp3"));
		this.frog = frog;
		constants = new Constants();
		width = constants.getRealHeight() / 20;
		height = constants.getRealWidth() / 15;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (!done) {
			//System.out.println("Left: + " + leftPos + "  right + " + rightPos);
			batch.draw(leftTex, leftPos.x, leftPos.y, this.width, this.height);
			batch.draw(rightTex, rightPos.x, rightPos.y, this.width, this.height);
		}
	}
	
	public void setLilyPos(Vector2 lp){
		this.lilyPos.set(lp);
	}
	
	public void setLilyVel(Vector2 lv){
		this.lilyVel.set(lv);
	}

	@Override
	public void start() {
		tear.play(.5f, 1f, 1f);
		this.leftPos.set(lilyPos);
		this.rightPos.set(lilyPos.x + this.width, lilyPos.y);
		this.leftVel.set(- (this.width / 10), this.lilyVel.y / 1.5f);
		this.rightVel.set((this.width / 10), this.lilyVel.y / 1.5f);
	}

	@Override
	public boolean check() {
		//System.out.println("Done?   " + (rightPos.x - leftPos.x) + "   " + (width * 4));
		return (rightPos.x - leftPos.x > width * 6);
	}

	@Override
	public void action() {
		leftPos.add(leftVel);
		rightPos.add(rightVel);
	}

}
