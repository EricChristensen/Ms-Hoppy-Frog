package com.eric.frogjumper.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.layout.BackgroundRepeat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.eric.frogjumper.Acheivements;
import com.eric.frogjumper.Constants;
import com.eric.frogjumper.Game1;
import com.eric.frogjumper.animations.BallonAnimation;
import com.eric.frogjumper.animations.FlyAnimation;
import com.eric.frogjumper.animations.FlyAnimationList;
import com.eric.frogjumper.animations.LilyAnimation;
import com.eric.frogjumper.animations.PanAnimation;
import com.eric.frogjumper.animations.RocketAnimation;
import com.eric.frogjumper.animations.StarAnimation;
import com.eric.frogjumper.blocks.Block;
import com.eric.frogjumper.blocks.DownBlock;
import com.eric.frogjumper.blocks.UpBlock;
import com.eric.frogjumper.characters.Bottom;
import com.eric.frogjumper.characters.Fly;
import com.eric.frogjumper.characters.FlyList;
import com.eric.frogjumper.characters.Frog;
import com.eric.frogjumper.characters.LilyPad;
import com.eric.frogjumper.highscore.HighScores;

public class PlayScreen implements Screen {

	Game1 game;
	SpriteBatch batch;
	OrthographicCamera cam;
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	Frog frog;
	LilyPad lilyPad;
	ShapeRenderer sr;
	int hits = 0;
	boolean has_hit = false;
	float screen_width = 0;//constants.getRealWidth();
	float screen_height = 0;//constants.getRealHeight();
	List<Block> blocks;
	Color color;
	Color color2;
	int lilPrev = 0;
	int updateCycle = 0;
	BitmapFont font;
	float distanceFromScore = 100;
	boolean resetDistance = false;
	float score = 0;
	private String greenScore = " ";
	Rectangle top;
	Rectangle pause;
	Rectangle resume;
	Rectangle reset;
	float initialTopY;
	boolean once = true;
	boolean deadOnce = false;
	Random random;
	Bottom bottom;
	Bottom bottom2;
	Bottom bottom3;
	Bottom currentBottom;
	PauseScreen pauseScreen;
	
	Sound taDa;
	Sound yahoo;
	Sound whoosh3;
	Sound rulerTwang;
	Sound balloonUp;
	Sound smallApplause;
	Sound largeApplause;
	Sound shootingStar;
	
	public enum State {play, pause, resume, dead}
	private State state = State.play;
	private float rotPrev;
	private float xVal;
	private Vector2 lilyPosPrev;
	float camVel = 0;
	
	Fly fly;
	List<Fly> startFlys;
	private int consequtiveFlys = 0;
	
	PanAnimation panAnimation;
	BallonAnimation ballonAnimation;
	LilyAnimation lilyAnimation;
	RocketAnimation rocketAnimation;
	
	//Pool<FlyAnimation> flyAnimationPool;
	Array<FlyAnimation> flyAnimations;
	//Pool<Fly> bottomFlyPool;
	//Array<Fly> bottomFlys;
	private Vector2 greenPos;
	private float deltaCamVel;
	private Acheivements acheivements;
	String acheiveFile = "acheivements.dat";
	String flFile = "flyList.dat";
	String faFile = "flyAnimations.dat";
	private int prevTouches;
	private boolean twoFlies = false;
	private boolean threeFlies = false;
	private int counterPrev = 0;
	int flysGone = 0;
	int counterPrev2 = 0;
	int conseqFlyPrev = 0;
	Constants constants;
	private Vector2[] newPosition;
	private Vector2[] newFlyPosition;
	private float distance;
	private Texture flyTexture;
	private Vector2 startFlyVector;
	
	Texture currentStar;
	Texture currentFlyStar;
	Texture star1;
	Texture star2;
	Texture star3;
	Texture star4;
	Texture star5;
	Texture star6;
	
	StarAnimation starAnimation;
	StarAnimation starFlyAnimation;
	private int curScore;
	private int curFlyScore = 0;
	private int flyScore = 0;
	private float flyDistanceFromScore = 0;
	private float flyDeltaCamVel;
	private String flyGreenScore= "";
	Vector2 flyStarPosition;
	private Vector2 flyGreenPosition;
	private boolean flyJustHit;
	boolean infinityMode = false;
	boolean aboveMaxPos = false;
	enum mode {infinity, arcade};
	
	float realScore;
	String realScoreString;
	String heightString;
	String scoreString;
	
	Texture background0;
	float backgroundRatio0;
	Texture background1;
	float backgroundRatio1;
	Texture background2;
	float backgroundRatio2;
	
	
	public PlayScreen(Game1 game, boolean infinityMode){
		constants = new Constants();
		this.infinityMode = infinityMode;
		System.out.println("Play screen");
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		if (state == State.play){
			if (top.contains(Gdx.input.getX() - screen_width / 1.22f, screen_height - Gdx.input.getY()/* - top.getHeight()*/) && Gdx.input.justTouched()){
				this.pause();
				// System.out.println("                   Pausing!");
				
			}  else {
				lilyPosPrev.set(lilyPad.position);
				update();
				draw();
			}
		} else if (state == State.pause) {
			lilyPad.position.set(lilyPosPrev);
			draw();
			pauseDraw();
			pauseUpdate();
		} else if (state == State.resume) {
			state = State.play;
		} else if (state == State.dead) {
			this.dispose();
			if (infinityMode){
				game.setScreen(new InfinityDeadScreen(game, realScore, this, smallApplause, largeApplause));
			} else {
				game.setScreen(new DeadScreen(game, realScore, this, smallApplause, largeApplause));
			}
		}
	}
	
	private void pauseDraw() {
		pauseScreen.draw(batch);
	}

	private void pauseUpdate() {
		pauseScreen.update();
	}

	public void update() {
		//System.out.println(game.flyList.size);
		updateCycle++;
		panAnimation.update();
		for (Block block : blocks) {
			if (block.position.y < cam.position.y - screen_height / 1.8) {
				block.position.y += screen_height * 2f;
			}
		}
		lilyPad.update(cam.position.y, this.camVel);
		frog.update(lilyPad, this.blocks, cam.position.y);
		if (lilyPad.broken == true) {
			lilyAnimation.setLilyPos(lilyPad.position);
			lilyAnimation.setLilyVel(lilyPad.velocity);
			lilyAnimation.start();
			lilyPad.position.set(0, 0);
			lilyPad.velocity.set(0, 0);
			lilyPad.broken = false;
			lilyAnimation.done = false;
		}
		lilyAnimation.update();
		collisions();
		
		if (frog.position.y < cam.position.y - screen_height / 1.5){
			state = State.dead;
		}
		if (updateCycle == 4){
		//	score++;
			//updateCycle = 0;
		}
		multipleBounces();
		ballonAnimation.setCamVel(camVel);
		ballonAnimation.setFactor(frog.factor);
		ballonAnimation.setFrogPos(frog.position);
		ballonAnimation.update();
		rocketAnimation.setCamVel(camVel);
		rocketAnimation.setFrogPos(frog.position);
		//rocketAnimation.setFrogPos(frog.velocity);
		rocketAnimation.setJustGotHit(frog.justGotBoast);
		rocketAnimation.update();
		extraPoints();
		if (!acheivements.twentyFiveThousand.value && score > 25000) {
			acheivements.twentyFiveThousand.value = true;
			//taDa.play(.25f);
			if (game.actionResolver.getSignedInGPGS()) {
				game.actionResolver.unlockAchievementGPGS(this.acheivements.twentyFiveThousand.getKeyName());
				
			}
		}
		if (!acheivements.fiftyThousand.value && score > 50000) {
			acheivements.fiftyThousand.value = true;
			//taDa.play(.25f);
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.unlockAchievementGPGS(this.acheivements.fiftyThousand.getKeyName());
		}
		if (!acheivements.hundredThousand.value && score > 100000) {
			acheivements.hundredThousand.value = true;
			//taDa.play(.25f);
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.unlockAchievementGPGS(this.acheivements.hundredThousand.getKeyName());
		}
		chooseStar();
		camAlign();
		finishGame();
		if (infinityMode) {
			realScore = frog.maxHeight;
			//if (frog.position.y > 1000 * (screen_height / 640) && frog.position.y < 2000 * (screen_height / 640)) {
			//	frog.justGotBoast = true;
			//}
		} else {
			realScore = score;
		}
	}

	private void finishGame() {
		if (frog.position.y >= 40000 * (screen_height / 640)) {
			aboveMaxPos = true;
			frog.aboveMaxPos = true;
		}
		if (infinityMode == false) {
			if (aboveMaxPos == true) {
				if (!acheivements.beatArcade.value && game.actionResolver.getSignedInGPGS()) {
					acheivements.beatArcade.value = true;
					game.actionResolver.unlockAchievementGPGS(acheivements.beatArcade.getKeyName());
				}
				state = State.dead;
			}
		}
	}

	private void chooseStar() {
		if (curScore < 150) {
			currentStar = star6;
		} else if (curScore < 600) {
			currentStar = star5;
		} else if (curScore < 2000) {
			currentStar = star4;
		} else if (curScore < 6000) {
			currentStar = star3;
		} else if (curScore < 14000) {
			currentStar = star2;
		} else {
			currentStar = star1;
		}
		starAnimation.setTexture(currentStar);
		starAnimation.setLilyPos(lilyPad.position);
		starAnimation.setLilyVel(lilyPad.velocity);
		starAnimation.setCamVel(camVel);
		if (frog.has_hit) {
			System.out.println("frog.has_hit = true");
			starAnimation.deltaY = 0;
			greenPos.set(lilyPad.position);
			starAnimation.done = false;
		}
		starAnimation.update();
		if (flyScore == 100) {
			currentFlyStar = star6;
		} else if (flyScore == 500) {
			currentFlyStar = star5;
		} else if (flyScore == 1000) {
			currentFlyStar = star4;
		} else if (flyScore == 2000) {
			currentFlyStar = star3;
		} else if (flyScore == 4000) {
			currentFlyStar = star2;
		} else if (flyScore == 8000) {
			currentFlyStar = star1;
		}
		starFlyAnimation.setTexture(currentFlyStar);
		starFlyAnimation.setLilyPos(flyStarPosition);
		//starFlyAnimation.setLilyVel(new Vector2(0, screen_height / 300f));
		starFlyAnimation.setCamVel(camVel);
		starFlyAnimation.update();
		if (flyJustHit == true) {
			starFlyAnimation.done = false;
			starFlyAnimation.deltaY = 0;
			flyGreenPosition.set(flyStarPosition);
			flyJustHit = false;
		}
		flyScore = 0;
	}

	private void extraPoints() {
		if (frog.score != 0) {
			updateCycle = 0;
			distanceFromScore = 0;
			deltaCamVel = 0f;
			if (!lilyPad.myGestureListener.stopped) {
				frog.score /= 10;
			}
			greenScore = " " + frog.score;
			curScore = frog.score;
		}
		deltaCamVel += camVel;
		distanceFromScore += screen_height / 750 + camVel;
		if (distanceFromScore > (screen_height / 750) * 180 + deltaCamVel) {//(distanceFromScore > (screen_height / 750) * 180 + camVel * 180) {
			distanceFromScore = screen_height * 10 + camVel;
		}
		if (getBottomByLevel(0).score == 2000 && !acheivements.escapesBottom.value){
			acheivements.escapesBottom.value = true;
			//taDa.play(.25f);
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.unlockAchievementGPGS(this.acheivements.escapesBottom.getKeyName());
		}
		frog.score += getBottomByLevel(0).score;
		score += frog.score;

		if (flyScore != 0) {
			updateCycle = 0;
			flyDistanceFromScore = 0;
			flyDeltaCamVel = 0f;
			flyGreenScore = " " + flyScore;
			curFlyScore = flyScore;
		}
		flyDeltaCamVel += camVel;
		flyDistanceFromScore += screen_height / 750 + camVel;
		if (flyDistanceFromScore > (screen_height / 750) * 180 + flyDeltaCamVel) {//(distanceFromScore > (screen_height / 750) * 180 + camVel * 180) {
			flyDistanceFromScore = screen_height * 10 + camVel;
		}
		score += flyScore;
	}

	private void multipleBounces() {
		/*if (Gdx.input.isKeyPressed(Keys.M)){
			frog.position.y += 1000;
			cam.position.y += 1000;
		}*/
		if (!lilyPad.myGestureListener.stopped){
			lilyPad.counter = 0;
		}
		/*if (aboveMaxPos) {
			frog.factorAdder += .0000002f;
		}*/
		if (lilPrev != lilyPad.counter) {
			//System.out.println("Bing!");
			if (infinityMode) {//(frog.factorAdder > .001f){
				if (lilyPad.counter >= 2){
					frog.justGotBoast = true;
					frog.velocity.y += screen_height / 64000;
				}
			} else { 
				if ((conseqFlyPrev == consequtiveFlys - 1 || consequtiveFlys == 3) && counterPrev2 == lilyPad.counter2 - 1 && lilyPad.counter > 1){
					if (!acheivements.doubleHitWithFlyInBetween.value){
						acheivements.doubleHitWithFlyInBetween.value = true;
						//taDa.play(.25f);
						if (game.actionResolver.getSignedInGPGS())
							game.actionResolver.unlockAchievementGPGS(this.acheivements.doubleHitWithFlyInBetween.getKeyName());
						// System.out.println("Double hit with fly in between");
					}
					flyScore = 2000;
				}
				if (lilyPad.counter == 2) {
					frog.score *= 4;
					frog.factor *= .8f;
					balloonUp.play(.3f, 1.4f, 1f);
					if (!acheivements.doubleBounce.value) {
						acheivements.doubleBounce.value = true;
						//taDa.play(.25f);
						if (game.actionResolver.getSignedInGPGS())
							game.actionResolver.unlockAchievementGPGS(this.acheivements.doubleBounce.getKeyName());
					}
				} else if (lilyPad.counter == 3) {
					frog.score *= 8;
					frog.factor *= .8f;
					balloonUp.play(.3f, 1.75f, 1f);
					if (!acheivements.tripleBounce.value) {
						acheivements.tripleBounce.value = true;
						//taDa.play(.25f);
						if (game.actionResolver.getSignedInGPGS())
							game.actionResolver.unlockAchievementGPGS(this.acheivements.tripleBounce.getKeyName());
					}
				} else if(lilyPad.counter == 4) {
					frog.score *= 16;
					frog.factor *= .8f;
					balloonUp.play(.3f, 2f, 1f);
					if (!acheivements.quadrupleBounce.value) {
						acheivements.quadrupleBounce.value = true;
						//taDa.play(.25f);
						if (game.actionResolver.getSignedInGPGS())
							game.actionResolver.unlockAchievementGPGS(this.acheivements.quadrupleBounce.getKeyName());
					}
				}
			}
			lilPrev = lilyPad.counter;
			if (frog.factor < .49f) {
				frog.factor = .49f;
			}
			conseqFlyPrev = consequtiveFlys;
		}
		counterPrev2 = lilyPad.counter2;
	}
	
	public void camAlign() {
		xVal = Math.signum(frog.velocity.x);
		//System.out.println(cam.viewportHeight);
		float middle_of_screen = cam.viewportHeight + cam.position.y - screen_height /*+ constants.getBottom().height*/;
		float frog_tracking_point = middle_of_screen + (screen_height / 5) - (screen_height / 16) - constants.getTop().height;
		float real_frog_tracking_point = frog_tracking_point;
		if (frog.justGotBoast) {
			real_frog_tracking_point -= constants.getHeight() / 5f;
		}
		camVel = 0;
		if (frog.position.y > real_frog_tracking_point &&
				frog.velocity.y > speedToGetTo(frog_tracking_point + frog.bounds.height* 4, frog.position.y)&& !Gdx.input.isKeyPressed(Keys.H)) {
			camVel = frog.velocity.y;
		} else if (frog.position.y >= middle_of_screen + (screen_height / 2) - frog.bounds.height - screen_height / 16  - constants.getTop().height &&
				frog.velocity.y > 0 && !Gdx.input.isKeyPressed(Keys.H)) {
			camVel = frog.velocity.y;
		} else if (frog.position.y > real_frog_tracking_point + screen_height / 20 && 
				frog.velocity.y < speedToGetTo(frog_tracking_point + frog.bounds.height* 4, frog.position.y)&& !Gdx.input.isKeyPressed(Keys.H)) {
			frog.rotateDown = true;
		}
		
		if (frog.position.y > middle_of_screen + (screen_height / 2) - screen_height / 16 - constants.getTop().height &&
				frog.velocity.y > 0 && once) {
			frog.score += 5000;
			if (!acheivements.escapesTop.value){
				acheivements.escapesTop.value = true;
				//taDa.play(.25f);
				if (game.actionResolver.getSignedInGPGS())
					game.actionResolver.unlockAchievementGPGS(this.acheivements.escapesTop.getKeyName());
			}
			yahoo.play();
			once = false;
		} else if (frog.position.y < middle_of_screen + (screen_height / 2) - (frog.bounds.height * 5) - (screen_height / 16)){
			once = true;
		}	
		this.distance = constants.getTop().y + cam.position.y  - constants.getRealHeight() / 2f - 2*constants.getTop().height - top.height / 4f;
		cam.update();
	}

	private void collisions() {
		if (frog.bounds.overlaps(getBottomByLevel(0).bigBounds) && deadOnce == false) {
			getBottomByLevel(0).drawableFlies = 0;
			if(Math.signum(frog.velocity.y) < 0){
				frog.velocity.y *= -1.1f;
			}
			frog.velocity.x /= 5;
			for (Fly fly : getBottomByLevel(0).flys){
				//bottomFlyPool.free(fly);
				fly.reset();
				this.game.flyPool.free(fly);
			}
			getBottomByLevel(0).clear();
			getBottomByLevel(0).level = 4;
			getBottomByLevel(1).level = 0;
			getBottomByLevel(2).level = 1;
			getBottomByLevel(4).level = 2;
			
			rulerTwang.play();
			deadOnce = true;
		} else if (!frog.bounds.overlaps(getBottomByLevel(0).bigBounds)) {
			deadOnce = false;
		}
		cam.position.y += camVel;
		bottom.update(cam.position.y, camVel);
		bottom2.update(cam.position.y, camVel);
		bottom3.update(cam.position.y, camVel);
		fly.update(camVel);
		for (int i = 0; i < game.flyAnimationList.size; i++) {
			game.flyAnimationList.get(i).setEnd(game.flyAnimationList.get(i).endPosition.x, getBottomByLevel(game.flyAnimationList.get(i).level).bigBounds.y + getBottomByLevel(game.flyAnimationList.get(i).level).bigBounds.height / 2);
			//game.flyAnimationList.get(i).setStart(game.flyAnimationList.get(i).position1);
			game.flyAnimationList.get(i).setCamVel(camVel);
			game.flyAnimationList.get(i).update();
			
			FlyAnimation fa = game.flyAnimationList.get(i);
			if (fa.done == true) {
				game.flyAnimationList.removeIndex(i);
				fa.reset();
				game.flyAnimationPool.free(fa);
				if (currentBottom.level > 0){
					if (getBottomByLevel(currentBottom.level - 1).drawableFlies < 15){
						getBottomByLevel(currentBottom.level - 1).drawableFlies += 1;
					} else {
						currentBottom.drawableFlies += 1;
					}
				} else {
					currentBottom.drawableFlies += 1;
				}
				// System.out.println("Currentlevel: " + currentBottom.level + " Drawable flies: " + currentBottom.drawableFlies);
			}
		}

		if (startFlys.size() - flysGone > 0) {
			for (int i = 0; i < startFlys.size() - flysGone; i++) {
				//Fly fly1 = startFlys.get(i);
				startFlys.get(i).update(camVel);
				if (frog.bounds.overlaps(startFlys.get(i).bounds)) {
					//flysGone += 1;
					
					//bottomFlyPool.free(startFlys.get(i));
					//startFlys.get(i).position.x = -screen_width;
					consequtiveFlys++;
					if (consequtiveFlys > 3){
						consequtiveFlys = 3;
					}
					// System.out.println("HERE");
					newFly();
					startFlys.get(i).reset();
					game.flyPool.free(startFlys.get(i));
					startFlys.remove(i);
					//bottomFlyPool.free(startFlys.get(i));
					//whoosh3.play();
				}
			}
			if (startFlys.size() == 0) {
				consequtiveFlys = 0;
			}
		} 
				
		if (frog.bounds.overlaps(fly.bounds)){
			flyScore = 100;
			flyStarPosition.set(fly.position);
			flyJustHit = true;
			consequtiveFlys++;
			if (consequtiveFlys > 3){
				consequtiveFlys = 3;
			}
			if (consequtiveFlys > 1) {
				if (this.prevTouches == lilyPad.myGestureListener.totalTouches - 1 && twoFlies == false) {
					twoFlies = true;
					if (acheivements.twoConsequtiveFlys.value == false) {
						//taDa.play(.25f);
						this.acheivements.twoConsequtiveFlys.value = true;
						if (game.actionResolver.getSignedInGPGS())
							game.actionResolver.unlockAchievementGPGS(this.acheivements.twoConsequtiveFlys.getKeyName());
						// System.out.println("two ConseqFlies");
					}
					flyScore = 500;
					// System.out.println("twice ConseqFlies");
				} else if (this.prevTouches == lilyPad.myGestureListener.totalTouches - 1 && twoFlies == true && threeFlies == false) {
					threeFlies = true;
					if (!acheivements.threeConsequtiveFlys.value){
						acheivements.threeConsequtiveFlys.value = true;
						//taDa.play(.25f);
						if (game.actionResolver.getSignedInGPGS())
							game.actionResolver.unlockAchievementGPGS(this.acheivements.threeConsequtiveFlys.getKeyName());
						// System.out.println("three ConseqFlies");
					}
					flyScore = 1000;
					// System.out.println("three ConseqFlies");
				} else if (this.prevTouches == lilyPad.myGestureListener.totalTouches - 1 && twoFlies == true && threeFlies == true) {
					if (!acheivements.fourConsequtiveFlys.value) {
						acheivements.fourConsequtiveFlys.value = true;
						//taDa.play(.25f);
						if (game.actionResolver.getSignedInGPGS())
							game.actionResolver.unlockAchievementGPGS(this.acheivements.fourConsequtiveFlys.getKeyName());
						// System.out.println("four ConseqFlies");
					}
					flyScore = 2000;
					// System.out.println("four ConseqFlies");
				} else if (this.prevTouches != lilyPad.myGestureListener.totalTouches - 1 &&
						this.prevTouches != lilyPad.myGestureListener.totalTouches) {
					twoFlies = false;
					threeFlies = false;
					// System.out.println("Reset Counter");
				}
				if (this.prevTouches == lilyPad.myGestureListener.totalTouches) {
					if (this.counterPrev  == lilyPad.counter2) {
						if (!acheivements.twoFliesInARow.value) {
							//taDa.play(.25f);
							acheivements.twoFliesInARow.value = true;
							if (game.actionResolver.getSignedInGPGS())
								game.actionResolver.unlockAchievementGPGS(this.acheivements.twoFliesInARow.getKeyName());
							// System.out.println("twoFliesInARow");
						}
						flyScore = 8000;
						// System.out.println("twoFliesInARow");
					}
				}
			}
			if (lilyPad.counter > 1) {/* this.counterPrev == lilyPad.counter2 - 1 || this.counterPrev == lilyPad.counter2 - 2
					&& this.prevTouches == lilyPad.myGestureListener.totalTouches){*/
				if (!acheivements.doubleHitWithFlyAfter.value) {
					acheivements.doubleHitWithFlyAfter.value = true;
					//taDa.play(.25f);
					if (game.actionResolver.getSignedInGPGS())
						game.actionResolver.unlockAchievementGPGS(this.acheivements.doubleHitWithFlyAfter.getKeyName());
					// System.out.println("doubleHitWithFlyAfter");
				}		
				flyScore = 4000;
				// System.out.println("doubleHitWithFlyAfter");
			}
			newFly();
			//System.out.println("Two flies: " + twoFlies + "  Three flies: " + threeFlies);
			//System.out.println("Conseq flies: " + consequtiveFlys);
			//System.out.println("Counter prev: " + counterPrev + "  currentCounter: " + lilyPad.counter2);
			//System.out.println("Prev Touches: " + prevTouches + "  current Touches: " + lilyPad.myGestureListener.totalTouches);
			this.counterPrev = lilyPad.counter2;
			this.prevTouches = lilyPad.myGestureListener.totalTouches;
		}
		if (fly.position.y < cam.position.y - screen_height / 2) {
			consequtiveFlys = 0;
			twoFlies = false;
			threeFlies = false;
			newFly();
		}
		if (frog.rotateDown && frog.rotation != 180) {
			rotPrev = frog.rotation;
			float trueAccel;
			if (frog.justGotBoast){
				trueAccel = frog.acceleration.y;//frog.inbetweenAccel;//frog.boastAccel;
			} else {
				trueAccel = frog.acceleration.y;
			}
			frog.rotation += xVal * (12 * ((screen_height / 30000) * (640 / screen_height) + (960 * (640f / 14000000))) + (Math.abs(8 * trueAccel)/(screen_height  / (640))));

			if (((frog.rotation >= 180 && rotPrev <= 180) || (frog.rotation <= 180 && rotPrev >= 180)) ||
					((frog.rotation <= -180 && rotPrev >= -180) || (frog.rotation <= -180 && rotPrev >= -180))) {
				frog.rotation = 180;
				frog.rotateDown = false;
			}
		}
	}
	
	private Bottom getBottomByLevel(int level) {
		if (bottom.level == level) {
			return bottom;
		} else if (bottom2.level == level) {
			return bottom2;
		} else if (bottom3.level == level) {
			return bottom3;
		} else {
			return bottom;
		}
	}

	private void newFly() {
		Fly fly1;
		FlyAnimation fa;
		System.out.println("New Fly. Consequitive Flys: " + consequtiveFlys);
		for (int j = 0; j <consequtiveFlys; j++) {
			if (!getBottomByLevel(0).full()) {
				Vector2 newPosition = new Vector2(getBottomByLevel(0).flys.size() * fly.bounds.width + getBottomByLevel(0).bigBounds.x, getBottomByLevel(0).bigBounds.y);//newPosition[j].set(getBottomByLevel(0).flys.size() * fly.bounds.width, getBottomByLevel(0).bigBounds.y);
				//fly1 = bottomFlyPool.obtain();
				fly1 = this.game.flyPool.obtain();
				fly1.init(newPosition);
				getBottomByLevel(0).flys.add(fly1);
				//getBottomByLevel(0).drawableFlies += 1;
				/*FlyAnimation*/ fa = game.flyAnimationPool.obtain();
				//Vector2 newFlyPosition = new Vector2(newPosition.x, newPosition.y + getBottomByLevel(0).bigBounds.height / 2);
				fa.init(0, j, frog.position, new Vector2(newPosition.x, newPosition.y + getBottomByLevel(0).bigBounds.height / 2)/*newFlyPosition[j]*/);
				game.flyAnimationList.add(fa);
				currentBottom = getBottomByLevel(0);
			} else if (!getBottomByLevel(1).full()) {
				Vector2 newPosition = new Vector2(getBottomByLevel(1).flys.size() * fly.bounds.width + getBottomByLevel(0).bigBounds.x, getBottomByLevel(1).bigBounds.y);
				///*Fly*/ fly1 = bottomFlyPool.obtain();
				fly1 = this.game.flyPool.obtain();
				fly1.init(newPosition);
				getBottomByLevel(1).flys.add(fly1);
				/*FlyAnimation*/ fa = game.flyAnimationPool.obtain();
				fa.init(1, j, frog.position, new Vector2(newPosition.x, newPosition.y + getBottomByLevel(1).bigBounds.height / 2));
				game.flyAnimationList.add(fa);
				currentBottom = getBottomByLevel(1);
			} else if (!getBottomByLevel(2).full()) {
				Vector2 newPosition = new Vector2(getBottomByLevel(2).flys.size() * fly.bounds.width + getBottomByLevel(0).bigBounds.x, getBottomByLevel(2).bigBounds.y);
				///*Fly*/ fly1 = bottomFlyPool.obtain();
				fly1 = this.game.flyPool.obtain();
				fly1.init(newPosition);
				getBottomByLevel(2).flys.add(fly1);
				/*FlyAnimation*/ fa = game.flyAnimationPool.obtain();
				fa.init(2, j, frog.position, new Vector2(newPosition.x, newPosition.y + getBottomByLevel(2).bigBounds.height / 2));
				game.flyAnimationList.add(fa);
				currentBottom = getBottomByLevel(2);
			}
		}
		
		if (consequtiveFlys == 0) {
			if (!getBottomByLevel(0).full()) {
				Vector2 newPosition = new Vector2(getBottomByLevel(0).flys.size() * fly.bounds.width + getBottomByLevel(0).bigBounds.x, getBottomByLevel(0).bigBounds.y);//
				//fly1 = bottomFlyPool.obtain();
				fly1 = this.game.flyPool.obtain();
				fly1.init(newPosition);
				getBottomByLevel(0).flys.add(fly1);
				getBottomByLevel(0).drawableFlies += 1;
			} else if (!getBottomByLevel(1).full()) {
				Vector2 newPosition = new Vector2(getBottomByLevel(1).flys.size() * fly.bounds.width + getBottomByLevel(0).bigBounds.x, getBottomByLevel(1).bigBounds.y);//
				//fly1 = bottomFlyPool.obtain();
				fly1 = this.game.flyPool.obtain();
				fly1.init(newPosition);
				getBottomByLevel(1).flys.add(fly1);
				getBottomByLevel(1).drawableFlies += 1;
			} else if (!getBottomByLevel(2).full()) {
				Vector2 newPosition = new Vector2(getBottomByLevel(2).flys.size() * fly.bounds.width + getBottomByLevel(0).bigBounds.x, getBottomByLevel(2).bigBounds.y);//
				//fly1 = bottomFlyPool.obtain();
				fly1 = this.game.flyPool.obtain();
				fly1.init(newPosition);
				getBottomByLevel(2).flys.add(fly1);
				getBottomByLevel(2).drawableFlies += 1;
			}
			whoosh3.play(1f, .875f, 1f);
		} else if (consequtiveFlys == 1) {
			whoosh3.play(1f, .875f, 1f);
		} else if (consequtiveFlys == 2) {
			whoosh3.play(1f, 1.25f, 1f);
		} else if (consequtiveFlys == 3) {
			whoosh3.play(1f, 2f, 1f);
		}
		
		fly.position.y = cam.position.y + screen_height / 2;
		fly.position.x = constants.getRealLeftX() + screen_width / 20 + random.nextInt((int) screen_width - 2*(int)constants.getRight().width - (int)screen_width / 10);
		fly.initialX = fly.position.x;
	}
	
	private double speedToGetTo(float pointTo, float pointFrom) {
		double deltaX = pointTo - pointFrom;
		double trueAccel;
		if (frog.justGotBoast) {
			trueAccel = frog.acceleration.y;//frog.boastAccel;
		} else {
			trueAccel = frog.acceleration.y;
		}
		double v = Math.sqrt(2 * deltaX * -trueAccel);
		return v;
	}
	
	public void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.enableBlending();
		batch.setProjectionMatrix(cam.combined);
		//drawRecs();
		
		batch.begin();
		float aFactor = 5.5f;
		batch.draw(background0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * backgroundRatio0 * aFactor);
		batch.draw(background1, 0, Gdx.graphics.getWidth() * backgroundRatio0 * aFactor, Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * backgroundRatio1 * aFactor);
		batch.draw(background2, 0, Gdx.graphics.getWidth() * backgroundRatio0 *aFactor + Gdx.graphics.getWidth() * backgroundRatio1* aFactor, Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * backgroundRatio1 * aFactor);
		for (Block block : this.blocks) {
			block.draw(batch, cam.position.y);
		}
		//batch.draw(currentStar, greenPos.x, greenPos.y + distanceFromScore - screen_height / 16f,
			//	screen_height / 8f, screen_height / 8f);
		starAnimation.draw(batch);
		starFlyAnimation.draw(batch);
		fly.draw(batch, flyTexture);
		if (startFlys.size() > 0) {
			
			for (Fly fly1 : startFlys) {
				fly1.draw(batch, flyTexture);
			}
		}
		panAnimation.draw(batch);
		//flyAnimation.draw(batch);
		for (FlyAnimation fa : game.flyAnimationList) {
			fa.draw(batch, flyTexture);
		}
		/*for (Fly fly2 : bottomFlys) {
			fly2.draw(batch);
		}*/
		bottom.draw(batch);
		bottom2.draw(batch);
		bottom3.draw(batch);
		ballonAnimation.draw(batch);
		rocketAnimation.draw(batch);
		lilyPad.draw(batch);
		lilyAnimation.draw(batch);
		frog.draw(batch);
		//batch.end();
		//sr.begin(ShapeType.Filled);
		//sr.setColor(Color.BLACK);
		batch.draw(constants.getTexture(), top.x + constants.getRealLeftX(), initialTopY + cam.position.y - constants.getRealHeight() / 2f - constants.getTop().height/* - constants.getTop().height*/,
				top.width, top.height);
		//sr.end();
		//batch.begin();
		font.setScale(screen_height / 500f);
		font.setColor(Color.WHITE);
		
				//this.cam.position.y + screen_height / 2.1f;
		font.draw(batch, realScoreString + "   " +  (int)realScore /*+ "   fontFactorAdder: " + frog.factorAdder + "  aboveMaxPos " + aboveMaxPos*/, screen_width / 10f, distance);
		font.draw(batch, "pause", screen_width - (screen_width / 6f), distance);
		font.setColor(Color.BLACK);
		//float aDis = (6 - greenScore.length()) * (screen_height / 200);
		font.setScale(screen_height / 750f);
		if (!infinityMode) {
			font.draw(batch, greenScore, greenPos.x + screen_height / 30f /*+ aDis lilyPad.position.x + aDis*/, greenPos.y + distanceFromScore)/*lilyPad.position.y + lilyPad.bounds.height / 1.5f)*/;
			font.draw(batch, flyGreenScore, flyGreenPosition.x + screen_height / 30f, flyGreenPosition.y + flyDistanceFromScore);
		} //batch.end();
		//constants.draw(/*batch,*/ this.cam.position.y  - Gdx.graphics.getHeight() / 2, cam);
		//batch.begin();
		batch.draw(constants.getTexture(), constants.getLeft().x, constants.getLeft().y + cam.position.y - constants.getRealHeight() / 2f,
				constants.getLeft().width, constants.getLeft().height);
		batch.draw(constants.getTexture(), constants.getRight().x  - 2*constants.getRight().width, constants.getRight().y + cam.position.y - constants.getRealHeight() / 2f,
				constants.getRight().width, constants.getRight().height);
		batch.draw(constants.getTexture(), constants.getTop().x  + cam.position.x  - constants.getRealWidth() * .5f - 0*constants.getTop().width, constants.getTop().y + cam.position.y  - constants.getRealHeight() / 2f - 2.1f*constants.getTop().height,
				constants.getTop().width, constants.getTop().height);
		batch.draw(constants.getTexture(), constants.getTop().x  + cam.position.x  - constants.getRealWidth() * .5f - 0*constants.getTop().width, constants.getTop().y + cam.position.y  - constants.getRealHeight() / 2f - 1.5f*constants.getTop().height,
				constants.getTop().width, constants.getTop().height);
		batch.draw(constants.getTexture(), constants.getBottom().x  + cam.position.x  - constants.getRealWidth() * .5f - 0*constants.getBottom().width, constants.getBottom().y + cam.position.y  - constants.getRealHeight() / 2f,
				constants.getBottom().width, constants.getBottom().height);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		if (infinityMode){
			realScoreString = "Height: ";
			//frog.justGotBoast = true;
		} else {
			realScoreString = "Score: ";
		}
		background0 = new Texture(Gdx.files.internal("Background_00.png"));
		background1 = new Texture(Gdx.files.internal("Background_01.png"));
		background2 = new Texture(Gdx.files.internal("Background_02.png"));
		backgroundRatio0 = background0.getHeight() / background0.getWidth();
		backgroundRatio1 = background1.getHeight() / background1.getWidth();
		backgroundRatio2 = background0.getHeight() / background2.getWidth();
		aboveMaxPos = false;
		flyGreenPosition = new Vector2(0, 0);
		flyStarPosition = new Vector2(0, 0);
		starAnimation = new StarAnimation();
		starFlyAnimation = new StarAnimation();
		star1 = new Texture(Gdx.files.internal("Stars/SuperStar.png"));
		star2 = new Texture(Gdx.files.internal("Stars/HighestStart.png"));
		star3 = new Texture(Gdx.files.internal("Stars/SecondStar.png"));
		star4 = new Texture(Gdx.files.internal("Stars/ThirdStar.png"));
		star5 = new Texture(Gdx.files.internal("Stars/FourthStar.png"));
		star6 = new Texture(Gdx.files.internal("Stars/FifthStar.png"));
		currentStar = star6;
		currentFlyStar = star6;
		startFlyVector = new Vector2(0, 0);
		flyTexture = new Texture(Gdx.files.internal("fly.png"));
		screen_height = constants.getRealHeight();
		screen_width = constants.getRealWidth();
		random = new Random();
		bottom = new Bottom(0, infinityMode);
		bottom2 = new Bottom(1, infinityMode);
		bottom3 = new Bottom(2, infinityMode);
		currentBottom = new Bottom(3, infinityMode);
		getBottomByLevel(0).drawableFlies = 0;
		currentBottom = getBottomByLevel(0);
		currentBottom.drawableFlies = 0;
		getBottomByLevel(0).drawableFlies = 0;
		this.score = 0;
		greenPos = new Vector2(0, 0);
		consequtiveFlys = 0;
		cam = new OrthographicCamera(constants.getRealWidth(), constants.getRealHeight());
		cam.position.x = constants.getRealWidth() / 2;
		cam.position.y = constants.getRealHeight() * 1.5f;
		sr = new ShapeRenderer();
		lilyPad = new LilyPad(new Vector2(0, cam.position.y - screen_height / 3), cam.position.y);
		panAnimation = new PanAnimation(lilyPad.myGestureListener);
		rocketAnimation = new RocketAnimation();
		ballonAnimation = new BallonAnimation();
		//flyAnimation = new FlyAnimation();
		
		/*bottomFlyPool = new Pool<Fly>() {
			@Override
			protected Fly newObject() {
				return new Fly();
			}
		};
		/*bottomFlys = new Array<Fly>();
		for (int i = 0; i < 50; i++){
			Fly f = bottomFlyPool.obtain();
			f.init(new Vector2(0, 0));
			bottomFlys.add(f);
		}
		
		bottomFlyPool.freeAll(bottomFlys);*/
		/*flyAnimationPool = new Pool<FlyAnimation>() {
			@Override
			protected FlyAnimation newObject(){
				return new FlyAnimation();
			}
		};
		Array<FlyAnimation> fas = new Array<FlyAnimation>();
		for (int i = 0; i < 12; i++) {
			fas.add(flyAnimationPool.obtain());
		}
		flyAnimationPool.freeAll(fas);*/
		//flyAnimations = new Array<FlyAnimation>();
		lilyPosPrev = new Vector2(lilyPad.position.x, lilyPad.position.y);
		frog = new Frog(new Vector2(constants.getRealWidth() / 2, constants.getRealHeight() * 1.5f));
		if (infinityMode) {
			//frog.acceleration.y *= 12;
			//frog.velocity.y *= 12;
			frog.position.y += 20000 * (screen_height / 640);
			cam.position.y += 20000 * (screen_height / 640);
			//frog.justGotBoast = true;
		}
		lilyAnimation = new LilyAnimation(frog);
		fly = new Fly(new Vector2(random.nextInt((int) screen_width), frog.position.y + screen_height));
		for (int i = 0; i < game.flyAnimationPool.getFree(); i++) {
			FlyAnimation fa = game.flyAnimationPool.obtain();
			fa.reset();
			game.flyAnimationList.add(fa);
		}
		for (FlyAnimation fa : game.flyAnimationList) {
			fa.reset();
			game.flyAnimationPool.free(fa);
		}
		for (int i = 0; i < game.flyPool.getFree(); i++) {
			Fly fa = game.flyPool.obtain();
			fa.reset();
			game.flyList.add(fa);
		}
		for (Fly fa : game.flyList) {
			fa.reset();
			game.flyPool.free(fa);
		}
		startFlys = new ArrayList<Fly>();
		for (int i = 0; i < 5; i++) {
			Fly fly1 = game.flyPool.obtain();//new Fly(new Vector2(frog.position.x, frog.position.y + (screen_height / 4) * (i + 1)));
			startFlyVector.set(frog.position.x, frog.position.y + (screen_height / 4) * (i + 1));
			fly1.init(new Vector2(frog.position.x, frog.position.y + (screen_height / 4) * (i + 1)));
			fly1.originalV -= screen_height / 240;
			startFlys.add(fly1);
		}
		font = new BitmapFont();
		batch = new SpriteBatch();
		blocks = new ArrayList<Block>();
		for (int i = 0; i < 2; i++) {
			Block block;
			if (i % 2 == 0) {
				block = new UpBlock(new Vector2(screen_width / 2, screen_height * i));
			} else {
				block = new DownBlock(new Vector2(screen_width / 2, screen_height * i));
			}
			blocks.add(block);
		}
		top = new Rectangle(0, screen_height - (screen_height / 16)/* + cam.position.y - screen_height / 2*/ , screen_width, screen_height / 16);
		initialTopY = screen_height - (screen_height / 16);
		resume = new Rectangle(screen_width  /2, screen_height / 2, screen_width / 4, screen_width / 6 );
		reset = new Rectangle(screen_width  / 2, screen_height / 3, screen_width / 4, screen_width / 6);
		state = State.play;
		pauseScreen = new PauseScreen(this);
		taDa = Gdx.audio.newSound(Gdx.files.internal("TaDa.mp3"));
		whoosh3 = Gdx.audio.newSound(Gdx.files.internal("woosh_3.mp3"));
		rulerTwang = Gdx.audio.newSound(Gdx.files.internal("ruler_twang.mp3"));
		balloonUp = Gdx.audio.newSound(Gdx.files.internal("BalloonBlow.mp3"));
		yahoo = Gdx.audio.newSound(Gdx.files.internal("yahoo.mp3"));
		smallApplause = Gdx.audio.newSound(Gdx.files.internal("SmallApplause.mp3"));
		largeApplause = Gdx.audio.newSound(Gdx.files.internal("LargeApplause.mp3"));
		shootingStar = Gdx.audio.newSound(Gdx.files.internal("shooting_star.mp3"));
		acheivementStuff();
	}
	
	private void acheivementStuff() {
		if (Gdx.files.local(acheiveFile).exists()) {
			try {
				acheivements = Acheivements.readAcheivements(acheiveFile);
				//highScores.highScores.add(new HighScore(user, newScore));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			 System.out.println("acheivements exists, reading acheivements");
		} else {
			acheivements = new Acheivements();
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				Acheivements.saveAcheivements(acheivements, acheiveFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("acheivements do not exist yet");
		}
		if (acheivements == null){
			System.out.println("acheivements does not exist yet, creating acheivements ");
			acheivements = new Acheivements();
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				Acheivements.saveAcheivements(acheivements, acheiveFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("acheivements does not exist yet, creating acheivements ");
		} else if (acheivements.items == null){
			System.out.println("acheivements does not exist yet, creating acheivements ");
			acheivements = new Acheivements();
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				Acheivements.saveAcheivements(acheivements, acheiveFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("acheivements does not exist yet, creating acheivements ");
		}
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		Gdx.input.setInputProcessor(pauseScreen.stage);
		state = State.pause;		
		//System.out.println("paused");
	}

	@Override
	public void resume() {
		Gdx.input.setInputProcessor(new GestureDetector(lilyPad.myGestureListener));
		state = State.play;
	}

	@Override
	public void dispose() {
		//bottomFlyPool.freeAll(bottomFlys);
		//game.flyPool.freeAll(game.flyList.flys);
		/*game.fList.flys.clear();
		for (int i = 0; i < game.flyPool.getFree(); i++) {
			game.fList.flys.add(game.flyPool.obtain());
		}
		game.fAnimationList.flyAnimations.clear();
		for (int i = 0; i < game.flyAnimationPool.getFree(); i++){
			game.fAnimationList.flyAnimations.add(game.flyAnimationPool.obtain());
		}*/
		//Vector2 v = new Vector2(0, 0);
		for (FlyAnimation fa : game.flyAnimationList) {
			fa.reset();
		}
		game.flyAnimationPool.freeAll(game.flyAnimationList);
		for (Fly f : startFlys) {
			f.reset();
			game.flyPool.free(f);
		}
		for (Fly f : getBottomByLevel(0).flys){
			f.reset();
			game.flyPool.free(f);
		}
		try {
			Acheivements.saveAcheivements(acheivements, acheiveFile);
			//FlyList.saveFlyList(game.fList, flFile);
			//FlyAnimationList.saveFlyAnimationList(game.fAnimationList, faFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("Made it to dispose!");
	}

}
