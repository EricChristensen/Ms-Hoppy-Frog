package com.eric.frogjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Constants implements java.io.Serializable {

	float ratio = 1.6f;
	private transient Texture border;
	public transient Texture blueTexture;
	private Rectangle leftRectangle;
	private Rectangle rightRectangle;
	private Rectangle topRectangle;
	private Rectangle bottomRectangle;
	//private SpriteBatch batch;
	//OrthographicCamera cam;
	private float heightFactor = 1f;
	private float widthFactor = 1f;
	
	
	public Constants() {
		this.border = new Texture(Gdx.files.internal("blackSquare.png"));
		this.blueTexture = new Texture(Gdx.files.internal("blueSquare.png"));
		this.leftRectangle = new Rectangle(0, 0, 0, 0);
		this.rightRectangle = new Rectangle(0, 0, 0, 0);
		this.topRectangle = new Rectangle(0, 0, 0, 0);
		this.bottomRectangle = new Rectangle(0, 0, 0, 0);
		//batch = new SpriteBatch();
		//cam = new OrthographicCamera();
	}
	
	public Texture getTexture(){
		return this.border;
	}
	
	public float getWidth(){
		////////System.out.println("Width: " + Gdx.graphics.getWidth() + " Height: " + Gdx.graphics.getHeight() + " ratio: " + ((float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth()));
		if ((float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth() >= ratio) {
			widthFactor = .95f;
			return 0;
		} else {
			widthFactor = .95f;
			return (float) Gdx.graphics.getHeight() / ratio;
		}
	}
	
	public float getHeight(){
		if ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth() <= ratio){
		//	heightFactor = .9f;
			return 0;
		} else {
		//	heightFactor = 1.1f;
			return ratio * (float) Gdx.graphics.getWidth();
		}
	}
	
	public Rectangle getLeft(){ 
		float leftWidth = 0;
		if (getWidth() != 0){
			leftWidth = ((float)Gdx.graphics.getWidth() - getWidth()) / 2f;
		} else { 
			leftWidth = Gdx.graphics.getHeight() / 50f;
		}
		this.leftRectangle.set(0, 0, leftWidth, Gdx.graphics.getHeight());
		//////System.out.println("Left Width: " + leftWidth);
		return this.leftRectangle;
	}
	
	public Rectangle getRight(){
		float rightWidth = 0;
		if (getWidth() != 0){
			rightWidth = ((float)Gdx.graphics.getWidth() - getWidth()) / 2f;
		} else { 
			rightWidth = Gdx.graphics.getHeight() / 50f;
		}
		this.rightRectangle.set((float)Gdx.graphics.getWidth() - rightWidth * widthFactor, 0, rightWidth, Gdx.graphics.getHeight());
		//////System.out.println("Right x: " + ((float)Gdx.graphics.getWidth() - rightWidth) + "  Right Width: " + rightWidth +
			//	" total width: " + Gdx.graphics.getWidth());
		return this.rightRectangle;
	}
	
	public Rectangle getBottom(){
		float bottomHeight = 0;
		if (getHeight() != 0){
			bottomHeight = ((float)Gdx.graphics.getHeight() - getHeight()) / 2f;
		}  else { 
			bottomHeight = Gdx.graphics.getHeight() / 50f;
		}
		////////System.out.println("Bottom Height: " + bottomHeight);
		////////System.out.println("X: " + Gdx.input.getX() + "   Y: " + Gdx.input.getY());
		this.bottomRectangle.set(0, /*bottomHeight*/0, Gdx.graphics.getWidth(), bottomHeight);
		return this.bottomRectangle;
	}
	
	public Rectangle getTop(){
		float topHeight = 0;
		if (getHeight() != 0){
			topHeight = ((float)Gdx.graphics.getHeight() - getHeight()) / 2f;
		}  else { 
			topHeight = Gdx.graphics.getHeight() / 50f;
		}
		//////System.out.println("pointer x: " + Gdx.input.getX());
		this.topRectangle.set(0, (float)Gdx.graphics.getHeight() - topHeight * heightFactor, Gdx.graphics.getWidth(), topHeight *1f);
		return this.topRectangle;
	}
	
	public float getRealBottomY(){
		float bottomHeight = 0;
		if (getHeight() != 0){
			bottomHeight = ((float)Gdx.graphics.getHeight() - getHeight()) / 2f;
		} else { 
			bottomHeight = Gdx.graphics.getHeight() / 50f;
		}
		return bottomHeight;
	}
	
	public float getRealTopY(){
		float topHeight = 0;
		if (getHeight() != 0){
			topHeight = ((float)Gdx.graphics.getHeight() - getHeight()) / 2f;
		} else { 
			topHeight = Gdx.graphics.getHeight() / 50f;
		}
		return (float)Gdx.graphics.getHeight() - topHeight;
	}
	
	public float getRealRightX(){
		float rightWidth = 0;
		if (getWidth() != 0){
			rightWidth = ((float)Gdx.graphics.getWidth() - getWidth()) / 2f;
		}
		return getRight().x - 2*getRight().width;//getRealWidth() + getRealLeftX();
	}
	
	public float getRealLeftX(){
		float leftWidth = 0;
		if (getWidth() != 0){
			leftWidth = ((float)Gdx.graphics.getWidth() - getWidth()) / 2f;
		} else {
			leftWidth = Gdx.graphics.getHeight() / 50f;
		}
		return leftWidth;
	}
	
	public float getRealWidth(){
		if (getWidth() == 0) {
			//System.out.println("True");
			return Gdx.graphics.getWidth() - (Gdx.graphics.getHeight() / 25);//Gdx.graphics.getWidth();
		} else {
			return getWidth();
		}
	}
	
	public float getRealHeight(){
		if (getHeight() == 0) {
			return Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 25);
		} else {
			return getHeight();
		}
	}
}
