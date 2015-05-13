package com.eric.frogjumper.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class MyGestureListener implements GestureListener {

	public Vector2 velocity;
	public Vector2 position;
	public Vector2 startPosition;
	public Vector2 panPosition;
	private float camPos;
	float screen_height = Gdx.graphics.getHeight();
	float max_height = screen_height / 6;
	public boolean stopped = true;
	public boolean newPad = false;
	private float camVel;
	float lilWidth;
	public int totalTouches;
	
	public MyGestureListener(float camPos, float lilWidth){
		this.lilWidth = lilWidth;
		this.totalTouches = 0;
		this.velocity = new Vector2(0, 0);
		this.startPosition = new Vector2(0, 0);
		this.position = new Vector2(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight());
		this.panPosition = new Vector2(position);
		this.camPos = camPos - Gdx.graphics.getHeight() / 2;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		//System.out.println("touched down");
		totalTouches++;
		this.position.x = x - this.lilWidth / 2;
		this.position.y = Gdx.graphics.getHeight() - y + camPos;
		this.velocity.x = 0;
		this.velocity.y = 0;
		//System.out.println(" " + (Gdx.graphics.getHeight() - y) + "  " + (screen_height - (screen_height / 16)));
		if (Gdx.graphics.getHeight() - y < screen_height - (screen_height / 16)) {
			newPad = true;
			this.startPosition.x = this.position.x;
			this.startPosition.y = this.position.y;
		}
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		//System.out.println("Tapped");
		if (stopped == false) {
			this.position.x = x - this.lilWidth / 2;
			this.position.y = Gdx.graphics.getHeight() - y + camPos;
			this.velocity.x = 0;
			this.velocity.y = 0;
			//System.out.println("Tapped");
			stopped = false;
		}
		this.velocity.x = 0;
		this.velocity.y = 0;
		//System.out.println("semi tapped");
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		//this.tap(x, y, 0, 0);
		//System.out.println("Long pressed");
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		this.tap(Gdx.input.getX(), Gdx.input.getY(), 0, 0);
		this.velocity.x = ((velocityX * (screen_height/ 2000.0f)) + velocityX * (1.0f / 80000.0f)) / (/*screen_height*/640f / 6);
		this.velocity.y = Math.min(((velocityY * (screen_height/ 2000.0f)) + velocityY * (1.0f / 80000.0f)) / -(/*screen_height*/640f / 6), screen_height / 25f);
		//System.out.println("Flung " + this.velocity.x + " " + this.velocity.y);
		return true;
	}
	

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		//System.out.println(Math.sqrt(deltaY));
		//this.velocity.x = 0;// Math.signum(deltaX) * (float)Math.sqrt(Math.signum(deltaX)* deltaX);//deltaX * Gdx.graphics.getHeight() / 10000;
		//this.velocity.y = 0;//-1 * Math.signum(deltaY) * (float)Math.sqrt(Math.signum(deltaY)* deltaY) + camVel;//-1* deltaY * Gdx.graphics.getHeight() / 10000;
		this.panPosition.set(x, Gdx.graphics.getHeight() - y + camPos);
		stopped = false;
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		this.velocity.x = 0;
		this.velocity.y = 0;
		//System.out.println("PAN STOP");
		//System.out.println(this.velocity.x + " " + this.velocity.y);
		this.panPosition.set(x, Gdx.graphics.getHeight() - y + camPos);
		stopped = true;
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setCamPosition(float camPos, float camVel){
		this.camVel = camVel;
		this.camPos = camPos - Gdx.graphics.getHeight() / 2;
		max_height = this.camPos + screen_height / 6;
	}

}
