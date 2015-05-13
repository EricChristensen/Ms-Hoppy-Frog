package com.eric.frogjumper.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Animation implements java.io.Serializable{

	public boolean done = true;
	transient Texture texture;
	int i = 0;

	
	public Animation() {
		
	}
	
	public void update() {
		done = check();
		if (!done) {
			if (i == 0){
				start();
			}
			action();
			i++;
		} else {
			i = 0;
		}
	}
	
	public abstract void draw(SpriteBatch batch);
	
	public abstract void start();
	
	public abstract boolean check();

	public abstract void action();

}
