package com.eric.frogjumper.highscore;

import java.util.Comparator;

public class HighScoreComparator implements Comparator<HighScore> {

	public HighScoreComparator() {
		
	}

	@Override
	public int compare(HighScore arg0, HighScore arg1) {
		if (arg0.score > arg1.score) {
			return -1;
		} else if (arg0.score < arg1.score) {
			return 1;
		} else {
			return 0;
		}
	}

}
