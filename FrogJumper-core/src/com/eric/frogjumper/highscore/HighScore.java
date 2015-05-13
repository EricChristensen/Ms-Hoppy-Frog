package com.eric.frogjumper.highscore;

public class HighScore implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	/**
	 * 
	 */
	
	String name;
	CharSequence highscore;
	float score;
	
	public HighScore(String name, float newScore) {
		this.name = name;
		this.score = newScore;
		this.highscore = name + " " + (int) newScore;
	}
	
	public CharSequence getHighScore() {
		return this.highscore;
	}
	
	public String getName() {
		return this.name;
	}
	
	public float getScore() {
		return this.score;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setScore(long score) {
		this.score = score;
	}
}

