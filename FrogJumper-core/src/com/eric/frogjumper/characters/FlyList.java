package com.eric.frogjumper.characters;

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
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.Pool;

public class FlyList implements java.io.Serializable {

	public ArrayList<Fly> flys;
	//public Pool<Fly> flyPool;
	
	
	public FlyList() {
		/*flyPool = new Pool<Fly>() {
			@Override
			protected Fly newObject() {
				return new Fly();
			}
		};*/
		flys = new ArrayList<Fly>();
		/*for (int i = 0; i < 50; i++){
			Fly f = flyPool.obtain();
			f.init(new Vector2(0, 0));
			flys.add(f);
		}
		flyPool.freeAll(flys);*/
	}

	public static FlyList readFlyList(String flFile) throws IOException, ClassNotFoundException {
		FlyList flyList = null;
		FileHandle file = Gdx.files.local(flFile);
		flyList = (FlyList) deserialize(file.readBytes());
		return flyList;
	}
	
	public static void saveFlyList(FlyList flyList, String flFile) throws IOException {
		FileHandle file = Gdx.files.local(flFile);
		OutputStream out = null;
		try {
			file.writeBytes(serialize(flyList), false);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
		} finally {
			if (out != null) { try { out.close(); } catch (Exception ex) {}}
		}
		System.out.println("Saving flyList");
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
