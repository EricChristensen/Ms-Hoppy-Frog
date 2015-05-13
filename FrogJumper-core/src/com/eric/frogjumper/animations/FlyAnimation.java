package com.eric.frogjumper.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.eric.frogjumper.Constants;

public class FlyAnimation extends Animation implements Poolable, java.io.Serializable{

	int consequitiveFlys = 0;
	int conseqCurr = 0;
	int conseqPrev = 0;
	float camPos = 0;
	float camVel = 0;
	int which = 0;
	public int level = 0;
	public Vector2 position1 = new Vector2(0, 0);
	Vector2 velocity1 = new Vector2(0, 0);
	boolean calledFromInit = false;
	
	Vector2 startPosition = new Vector2(0,0);
	public Vector2 endPosition = new Vector2(0,0);
	Constants constants;
	
	public FlyAnimation() {
		constants = new Constants();
		this.texture = new Texture(Gdx.files.internal("cartoonFly.png"));
	}
	
	public void init(int level, int which, Vector2 startPos, Vector2 endPos) {
		calledFromInit = true;
		System.out.println("called From Init");
		this.which = which;
		this.setStart(startPos);
		this.setEnd(endPos.x, endPos.y);
		this.start();
		this.done = false;
		this.level = level;
	}
	
	public void setConsequitiveFlys(int cF) {
		conseqPrev = conseqCurr;
		this.consequitiveFlys = cF;
		conseqCurr = this.consequitiveFlys;
	}
	@Override
	public void start() {
		Exception e = new Exception();
	//	if (calledFromInit == false) {
		//	System.out.println("\nNot called from init!");
	//		e.printStackTrace();
	//		System.out.println("Not called from init!\n");
		//	this.reset();
		//} else {
			
		//	e.printStackTrace();
			System.out.println("Starting");
			position1.set(this.startPosition);
			velocity1.x = (((this.endPosition.x - this.startPosition.x) * (constants.getRealHeight() / 640)) / (constants.getRealHeight() / 10)) * (1.0f - (.2f * which));
			velocity1.y = (((this.endPosition.y - this.startPosition.y) * (constants.getRealHeight() / 640)) / (constants.getRealHeight() / 10)) * (1.0f - (.2f * which));
			System.out.println("Which fly: " + this.which + " level: " + this.level);
			System.out.println("startPosition: " + this.startPosition);
			System.out.println("endPosition: " + this.endPosition + "\n");
			//calledFromInit = false;
		//}
	}
	
	public void setStart(Vector2 sP) {
		this.startPosition.set(sP);
	}
	
	public void setEnd(float x, float y) {
		this.endPosition.set(x, y);
	}
	
	public void setCamVel(float camVel) {
		this.camVel = camVel;
		//System.out.println(this.camVel);
	}
	
	public void draw(SpriteBatch batch, Texture texture) {
		
		batch.draw(texture, position1.x, position1.y, constants.getRealWidth() / 16, constants.getRealWidth() / 25);
	}
	@Override
	public void draw(SpriteBatch batch) {
		if (this.position1.y < this.endPosition.y){
			batch.draw(texture, position1.x, position1.y, constants.getRealWidth() / 16, constants.getRealWidth() / 25);
		}
	}

	@Override
	public boolean check() { // done gets set to return value
		return  (this.position1.y < this.endPosition.y /*- constants.getHeight() / 10f*/); //((conseqCurr != conseqPrev ) || (conseqCurr == conseqPrev && conseqCurr == 3));
	}

	@Override
	public void action() {
		//this.endPosition.y += camVel;
		this.position1.add(velocity1.x , velocity1.y + camVel);
	}

	@Override
	public void reset() {
		consequitiveFlys = 0;
		conseqCurr = 0;
		conseqPrev = 0;
		camPos = 0;
		camVel = 0;
		position1.set(0, 0);
		velocity1.set(0, 0);
		which = 0;
		
		startPosition.set(0, 0);
		endPosition.set(0, 0);
		this.done = true;
	}

}
