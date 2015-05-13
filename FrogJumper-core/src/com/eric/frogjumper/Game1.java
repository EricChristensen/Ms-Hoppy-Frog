package com.eric.frogjumper;

import java.io.IOException;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.eric.frogjumper.animations.FlyAnimation;
import com.eric.frogjumper.animations.FlyAnimationList;
import com.eric.frogjumper.characters.Fly;
import com.eric.frogjumper.characters.FlyList;
import com.eric.frogjumper.screens.MainMenu;

public class Game1 extends Game {

	Game game;
	public FlyList fList;
	public Array<Fly> flyList;
	public Pool<Fly> flyPool;
	public FlyAnimationList fAnimationList;
	public Array<FlyAnimation> flyAnimationList;
	public Pool<FlyAnimation> flyAnimationPool;
	String flFile = "flyList.dat";
	String faFile = "flyAnimations.dat";
	
	public ActionResolver actionResolver;
	
	public Game1(ActionResolver actionResolver){
		this.actionResolver = actionResolver;
	}
	
	
	@Override
	public void create () {
	//	fList = new FlyList();
		//fAnimationList = new FlyAnimationList();
		flyList = new Array<Fly>();
		flyAnimationList = new Array<FlyAnimation>();
		flyPool = new Pool<Fly>() {
			@Override
			protected Fly newObject() {
				return new Fly();
			}
		};
		flyAnimationPool = new Pool<FlyAnimation>() {
			@Override
			protected FlyAnimation newObject() {
				return new FlyAnimation();
			}
		};
		game = this;
		flyListStuff();
		flyAnimationStuff();
		for (Fly fly : fList.flys) {
			flyList.add(fly);
		}
		System.out.println("flyList.size = " + flyList.size);
		flyPool.freeAll(flyList);
		System.out.println("Pool size = " + flyPool.getFree());
		for (FlyAnimation fa : fAnimationList.flyAnimations) {
			flyAnimationList.add(fa);
		}
		flyAnimationPool.freeAll(flyAnimationList);
		
		//fList.flys.clear();
		for (int i = 0; i < flyPool.getFree(); i++) {
		//	fList.flys.add(flyPool.obtain());
		}
		//fAnimationList.flyAnimations.clear();
		for (int i = 0; i < flyAnimationPool.getFree(); i++){
		//	fAnimationList.flyAnimations.add(flyAnimationPool.obtain());
		}
		
		this.setScreen(new MainMenu(this));		
	}
	
	
	
	private void flyListStuff() {
		if (Gdx.files.local(flFile).exists()) {
			try {
				fList = FlyList.readFlyList(flFile);
				//highScores.highScores.add(new HighScore(user, newScore));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("flyList exists, reading flyList");
		} else {
			fList = new FlyList();
			for (int i = 0; i < 50; i++){
				Fly f = flyPool.obtain();
				f.init(new Vector2(0, 0));
				fList.flys.add(f);
			}
			//flyPool.freeAll(flys);
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				FlyList.saveFlyList(fList, flFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("flyList do not exist yet");
		}
		if (fList == null){
			System.out.println("flyList does not exist yet, creating flyList ");
			fList = new FlyList();
			for (int i = 0; i < 50; i++){
				Fly f = flyPool.obtain();
				f.init(new Vector2(0, 0));
				fList.flys.add(f);
			}
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				FlyList.saveFlyList(fList, flFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("flyList does not exist yet, creating flyList ");
		} else if (fList.flys == null){
			System.out.println("flyList does not exist yet, creating flyList ");
			fList = new FlyList();
			for (int i = 0; i < 50; i++){
				Fly f = flyPool.obtain();
				f.init(new Vector2(0, 0));
				fList.flys.add(f);
			}
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				FlyList.saveFlyList(fList, flFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("flyList does not exist yet, creating flyList ");
		}
		int i = 0;
		//fList.flys.clear();
		while (fList.flys.size() < 85) {
			Fly f = flyPool.obtain();
			f.init(new Vector2(0, 0));
			fList.flys.add(f);
			System.out.println("HERERERERERERE " + i);
			i++;
		} try {
			FlyList.saveFlyList(fList, flFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//flyPool.freeAll(fList.flys);
	}
	
	private void flyAnimationStuff() {
		if (Gdx.files.local(faFile).exists()) {
			try {
				fAnimationList = FlyAnimationList.readFlyAnimationList(faFile);
				//highScores.highScores.add(new HighScore(user, newScore));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("flyAnimationList exists, reading flyAnimationList");
		} else {
			fAnimationList = new FlyAnimationList();
			for (int i = 0; i < 12; i++) {
				FlyAnimation fa = flyAnimationPool.obtain();
				fa.init(0, 0, new Vector2(0,0),  new Vector2(0,0));
				this.fAnimationList.flyAnimations.add(fa);
			}
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				FlyAnimationList.saveFlyAnimationList(fAnimationList, faFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("flyAnimationList do not exist yet");
		}
		if (fAnimationList == null){
			System.out.println("flyAnimationList does not exist yet, creating flyAnimationList ");
			fAnimationList = new FlyAnimationList();
			fAnimationList = new FlyAnimationList();
			for (int i = 0; i < 12; i++) {
				FlyAnimation fa = flyAnimationPool.obtain();
				fa.init(0, 0, new Vector2(0,0),  new Vector2(0,0));
				this.fAnimationList.flyAnimations.add(fa);
			}
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				FlyAnimationList.saveFlyAnimationList(fAnimationList, faFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("flyAnimationList does not exist yet, creating flyAnimationList ");
		} else if (fAnimationList.flyAnimations == null){
			System.out.println("flyList does not exist yet, creating flyList 111");
			fAnimationList = new FlyAnimationList();
			for (int i = 0; i < 12; i++) {
				FlyAnimation fa = flyAnimationPool.obtain();
				fa.init(0, 0, new Vector2(0,0),  new Vector2(0,0));
				this.fAnimationList.flyAnimations.add(fa);
			}
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				FlyAnimationList.saveFlyAnimationList(fAnimationList, faFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("flyList does not exist yet, creating flyAnimationList ");
		}
		while (fAnimationList.flyAnimations.size() < 12) {
			FlyAnimation fa = flyAnimationPool.obtain();
			fa.init(0, 0, new Vector2(0,0),  new Vector2(0,0));
			this.fAnimationList.flyAnimations.add(fa);
			System.out.println("222222222");
		}try {
			FlyAnimationList.saveFlyAnimationList(fAnimationList, faFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//flyAnimationPool.freeAll(fAnimationList.flyAnimations);
	}

	@Override
	public void render () {
		super.render();
	}
	
	
}
