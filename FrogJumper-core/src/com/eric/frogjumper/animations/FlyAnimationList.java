package com.eric.frogjumper.animations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.Pool;

public class FlyAnimationList implements java.io.Serializable {

	public ArrayList<FlyAnimation> flyAnimations;
	//public Pool<FlyAnimation> flyAnimationPool;
	
	public FlyAnimationList() {
		/*flyAnimationPool = new Pool<FlyAnimation>() {
			@Override
			protected FlyAnimation newObject(){
				return new FlyAnimation();
			}
		};*/
		flyAnimations = new ArrayList<FlyAnimation>();
		/*for (int i = 0; i < 12; i++) {
			flyAnimations.add(flyAnimationPool.obtain());
		}
		flyAnimationPool.freeAll(flyAnimations);*/
	}
	
	public static FlyAnimationList readFlyAnimationList(String flFile) throws IOException, ClassNotFoundException {
		FlyAnimationList flyAnimationList = null;
		FileHandle file = Gdx.files.local(flFile);
		flyAnimationList = (FlyAnimationList) deserialize(file.readBytes());
		return flyAnimationList;
	}
	
	public static void saveFlyAnimationList(FlyAnimationList flyAnimationList, String flFile) throws IOException {
		FileHandle file = Gdx.files.local(flFile);
		OutputStream out = null;
		try {
			file.writeBytes(serialize(flyAnimationList), false);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
		} finally {
			if (out != null) { try { out.close(); } catch (Exception ex) {}}
		}
		System.out.println("Saving flyAnimationList");
	}
	
	private static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}
	
	public static Object deserialize(byte bytes[]) throws IOException, ClassNotFoundException  {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}
}
