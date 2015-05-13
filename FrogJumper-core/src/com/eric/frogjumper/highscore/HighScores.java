package com.eric.frogjumper.highscore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
//import com.eric.frogjumper.Saver;

public class HighScores implements java.io.Serializable  {

	private static final long serialVersionUID = 1L;
	//public Saver<HighScores> saver;
	List<HighScore> items;

	public HighScores() {
		items = new ArrayList<HighScore>();
		//saver = new Saver<HighScores>();
	}
	
	public void sort() {
		Collections.sort(this.items, new HighScoreComparator());
		if (this.items.size() > 5) {
			this.items.remove(this.items.size() - 1);
		}
	}
	public List<HighScore> getItems() {
		return this.items;
	}
	
	/*public void add(HighScore hs) {
		this.items.add(hs);
	}*/
	
	public static HighScores readHighScores(String hsFile) throws IOException, ClassNotFoundException {
		HighScores highScores = null;
		FileHandle file = Gdx.files.local(hsFile);
		highScores = (HighScores) deserialize(file.readBytes());
		return highScores;
	}
	
	public static void saveHighScores(HighScores highScores, String hsFile) throws IOException {
		FileHandle file = Gdx.files.local(hsFile);
		OutputStream out = null;
		try {
			file.writeBytes(serialize(highScores), false);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
		} finally {
			if (out != null) { try { out.close(); } catch (Exception ex) {}}
		}
		System.out.println("Saving highscores");
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
