package com.eric.frogjumper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.eric.frogjumper.highscore.HighScores;

public class Acheivements implements Serializable {

	public Acheivement doubleBounce = new Acheivement(false, "Double Bounce", "CgkIxc7W1aQbEAIQAQ");
	public Acheivement tripleBounce = new Acheivement(false, "Triple Bounce", "CgkIxc7W1aQbEAIQAg");
	public Acheivement quadrupleBounce = new Acheivement(false, "Quadruple Bounce", "CgkIxc7W1aQbEAIQAw");
	public Acheivement escapesBottom = new Acheivement(false, "Flies Escape The Bottom", "CgkIxc7W1aQbEAIQBA");
	public Acheivement escapesTop = new Acheivement(false, "Frog Escapes the Top", "CgkIxc7W1aQbEAIQBQ");
	public Acheivement twoConsequtiveFlys = new Acheivement(false, "Two flies in two lily pads", "CgkIxc7W1aQbEAIQBg");
	public Acheivement threeConsequtiveFlys = new Acheivement(false, "Three flies with three lily pads", "CgkIxc7W1aQbEAIQBw");
	public Acheivement fourConsequtiveFlys = new Acheivement(false, "Four flies with four lily pads", "CgkIxc7W1aQbEAIQCA");
	public Acheivement doubleHitWithFlyInBetween = new Acheivement(false, "Lily pad then fly then Same Lily pad", "CgkIxc7W1aQbEAIQCQ");
	public Acheivement doubleHitWithFlyAfter = new Acheivement(false, "Double Bounce then fly", "CgkIxc7W1aQbEAIQCg");
	public Acheivement twoFliesInARow = new Acheivement(false, "Two flies, no lilies in between (Good Luck)", "CgkIxc7W1aQbEAIQCw");
	public Acheivement twentyFiveThousand = new Acheivement(false, "25,000 Points", "CgkIxc7W1aQbEAIQDA");
	public Acheivement fiftyThousand = new Acheivement(false, "50,000 Points", "CgkIxc7W1aQbEAIQDQ");
	public Acheivement hundredThousand = new Acheivement(false, "100,000 Points", "CgkIxc7W1aQbEAIQDg");
	public Acheivement beatArcade = new Acheivement(false, "Beat Arcade", "CgkIxc7W1aQbEAIQEQ");
	
	public Acheivement items[];
	
	public Acheivements() {
		items = new Acheivement[15];
		items[0] = doubleBounce;
		items[1] = tripleBounce;
		items[2] = quadrupleBounce;
		items[3] = escapesTop;
		items[4] = escapesBottom;
		items[5] = twoConsequtiveFlys;
		items[6] = threeConsequtiveFlys;
		items[7] = fourConsequtiveFlys;
		items[8] = doubleHitWithFlyInBetween;
		items[9] = doubleHitWithFlyAfter;
		items[10] = twentyFiveThousand;
		items[11] = fiftyThousand;
		items[12] = hundredThousand;
		items[13] = twoFliesInARow;
		items[14] = beatArcade;
	}
	
	
	public static Acheivements readAcheivements(String acheiveFile) throws IOException, ClassNotFoundException {
		Acheivements acheivements = null;
		FileHandle file = Gdx.files.local(acheiveFile);
		acheivements = (Acheivements) deserialize(file.readBytes());
		return acheivements;
	}
	
	public static void saveAcheivements(Acheivements acheivements, String acheiveFile) throws IOException {
		FileHandle file = Gdx.files.local(acheiveFile);
		OutputStream out = null;
		try {
			file.writeBytes(serialize(acheivements), false);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
		} finally {
			if (out != null) { try { out.close(); } catch (Exception ex) {}}
		}
		System.out.println("Saving acheivements");
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
	public Acheivement getById(String id) {
		for (Acheivement ach : this.items) {
			if (ach.keyName == id) {
				return ach;
			}
		}
		return null;
	}
	
	public void setById(boolean value, String id) {
		System.out.println("HEREY HEREY");
		for (int i = 0; i < items.length; i++) {
			System.out.println("Real key: " + id + " this key: " + items[i].keyName + " "+ value);
			if (items[i].keyName.equals(id)) {
				System.out.println("FO SHOW FO SHOW!!!!!");
				items[i].value = value;
			}
		}
	}
	
	public class Acheivement implements Serializable {
		
		public boolean value;
		public String name;
		private CharSequence fullName;
		private String keyName;
		
		public Acheivement(boolean value, String name, String keyName) {
			this.value = value;
			this.name = name;
			this.fullName = name + " " + value;
			this.keyName = keyName;
		}
		
		public String getKeyName() {
			return this.keyName;
		}
		
		public CharSequence getFullName() {
			this.fullName = name + " " + value;
			return this.fullName;
		}
	}
}

