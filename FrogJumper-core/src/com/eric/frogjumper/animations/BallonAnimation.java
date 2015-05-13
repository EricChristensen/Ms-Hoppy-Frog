package com.eric.frogjumper.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class BallonAnimation extends Animation{

	public float factor = 1;
	public float trueFactor = 1;
	public float camVel = 0;
	float width = Gdx.graphics.getHeight() / 20;
	float height = Gdx.graphics.getHeight() / 20;
	float origWidth = Gdx.graphics.getHeight() / 20;
	float origHeight = Gdx.graphics.getHeight() / 20;
	float frogWidth = Gdx.graphics.getHeight() / 20;
	public Vector2 frogPos;
	public Vector2 position;
	public Vector2 startLine;
	public Vector2 endLine;
	ShapeRenderer sr;
	Texture stringTexture;
	
	public BallonAnimation() {
		this.texture = new Texture(Gdx.files.internal("Balloon.png"));
		this.stringTexture = new Texture(Gdx.files.internal("BalloonString.png"));
		this.frogPos = new Vector2(0, 0);
		this.position = new Vector2(0, 0);
		this.startLine = new Vector2(0, 0);
		this.endLine = new Vector2(0, 0);
		sr = new ShapeRenderer();
	}
	
	public void setFrogPos(Vector2 fp) {
		this.frogPos.set(fp);
	}
	
	public void setCamVel(float camVel) {
		this.camVel = camVel;
	}
	
	public void setFactor(float factor) {
		this.factor = factor;
	}

	@Override
	public void draw(SpriteBatch batch) {
		/*sr.begin(ShapeType.Filled);
		sr.rectLine(startLine, endLine, Gdx.graphics.getHeight() / 640);
		sr.end();*/
		if (!done){
			batch.draw(texture, position.x, position.y, width, height);
			batch.draw(stringTexture, startLine.x, startLine.y, (endLine.y - startLine.y) / 4 , (endLine.y - startLine.y));// * (stringTexture.getWidth() / stringTexture.getHeight()));
		}
	}

	@Override
	public void start() {
		
	}

	@Override
	public boolean check() {
		//System.out.println(factor);
		return factor >= 1f;
	}

	@Override
	public void action() {
		if (trueFactor > factor) {
			trueFactor -= .004;
		} else if (trueFactor < factor) {
			trueFactor += .002;
		}
		this.width = origWidth * (1.0f / (float)(Math.pow(trueFactor, 2.0f)));
		this.height = origHeight * (1.0f / (float)(Math.pow(trueFactor, 2.0f)));
		if (factor <= .9f) {
			position.y = frogPos.y + Gdx.graphics.getHeight() / 7;
			position.x = frogPos.x - this.width / 2 + frogWidth / 2;
			startLine.x = frogPos.x + frogWidth / 2;
			startLine.y = frogPos.y + frogWidth;
			endLine.set(startLine.x, position.y);
		} else {
			float vel = frogWidth / 10 - camVel;
			position.y -= vel;
			startLine.y -= vel;
			endLine.y -= vel;
		}
	}

}
