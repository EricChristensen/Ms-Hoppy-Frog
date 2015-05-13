package com.eric.frogjumper.listeners;

import com.badlogic.gdx.Input.TextInputListener;

public class MyTextInputListener implements TextInputListener {

	String input;
	
	public MyTextInputListener() {
		// TODO Auto-generated constructor stub
	}

	public String getInput() {
		return input;
	}
	
	@Override
	public void input(String text) {
		this.input = text;
	}

	@Override
	public void canceled() {
		// TODO Auto-generated method stub

	}

}
