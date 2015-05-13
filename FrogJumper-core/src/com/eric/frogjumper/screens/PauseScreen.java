package com.eric.frogjumper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.eric.frogjumper.Constants;

public class PauseScreen implements Screen {

	Stage stage;
	BitmapFont bit_map_font;
	LabelStyle label_style;
	Label label;
	Skin skin;
	TextureAtlas button_atlas;
	TextButtonStyle button_text;
	TextButton resetButton;
	TextButton resumeButton;
	PlayScreen playScreen;
	Constants constants;
	
	public PauseScreen(PlayScreen playScreen) {
		constants = new Constants();
		this.playScreen = playScreen;
		this.show();
	}

	@Override
	public void render(float delta) {

	}
	
	public void update() {
		stage.act();
	}
	
	public void draw(SpriteBatch batch) {
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		stage = new Stage();
		/*stage.getViewport().setScreenHeight((int)(constants.getRealHeight() / 20f));
		stage.getViewport().setScreenWidth((int)(Gdx.graphics.getWidth() / 20f));
		stage.getViewport().setScreenX(Gdx.graphics.getWidth() / 5);
		stage.getViewport().setScreenY(constants.getRealHeight() / 5);*/
		int vx = stage.getViewport().getScreenX();
		int vy = stage.getViewport().getScreenY();
		bit_map_font = new BitmapFont(Gdx.files.internal("first_font.fnt"), false);
		label_style = new LabelStyle(bit_map_font, Color.BLUE);
		label = new Label("paused", label_style);
		label.setPosition(constants.getRealWidth() / 2 + vx + constants.getRealLeftX(), constants.getRealHeight() / 2 + vy + constants.getRealBottomY());
		stage.addActor(label);
		skin = new Skin();
		button_atlas = new TextureAtlas("buttons/greenButton.pack");
		skin.addRegions(button_atlas);
		button_text = new TextButtonStyle();
		button_text.up = skin.getDrawable("greenButton");
		button_text.over = skin.getDrawable("greenButtonPressed");
		button_text.down = skin.getDrawable("greenButtonPressed");
		button_text.font = bit_map_font;
		resetButton = new TextButton("reset", button_text);
		resetButton.setWidth(constants.getRealWidth() / 4);
		resetButton.setHeight(constants.getRealHeight() / 8);
		resetButton.setPosition((constants.getRealWidth() / 4) - (resetButton.getWidth() / 2) + constants.getRealLeftX(), (constants.getRealHeight() / 2) - (resetButton.getHeight() / 2) + constants.getRealBottomY());
		stage.addActor(resetButton);
		resetButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				playScreen.dispose();
				playScreen.show();
				return true;
			}
		});
		button_text.font.setScale(constants.getRealHeight() / 800f);
		resumeButton = new TextButton("resume", button_text);
		resumeButton.setWidth(constants.getRealWidth() / 4);
		resumeButton.setHeight(constants.getRealHeight() / 8);
		System.out.println("PAUSE REAL HEIGHT: " + constants.getRealHeight());
		resumeButton.setPosition((constants.getRealWidth() / 4f) - (resetButton.getWidth() / 2) + constants.getRealLeftX(), (constants.getRealHeight() / 2) - (resetButton.getHeight() * 1.5f) - constants.getRealBottomY());
		stage.addActor(resumeButton);
		resumeButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				playScreen.resume();
				return true;
			}
		});
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		
	}

}
