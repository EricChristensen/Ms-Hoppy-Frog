package com.eric.frogjumper.screens;

import java.awt.Font;
import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.eric.frogjumper.Acheivements;
import com.eric.frogjumper.Acheivements.Acheivement;
import com.eric.frogjumper.Constants;
import com.eric.frogjumper.Game1;

public class AcheivementScreen implements Screen {

	Acheivements acheivements;
	String acheiveFile = "acheivements.dat";
	Game1 game;
	
	Stage stage;
	Label label;
	LabelStyle label_style;
	BitmapFont bit_map_font;
	TextureAtlas button_atlas;
	TextButtonStyle button_text;
	TextButton button;
	TextButton clearButton;
	TextButton acheivementButton;
	Skin skin;
	SpriteBatch batch;
	BitmapFont font;
	Constants constants;
	
	public AcheivementScreen(Game1 game) {
		constants = new Constants();
		this.game = game;
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}
	
	public void update(){
		stage.act();
	}
	
	public void draw(){
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		stage.draw();
		//System.out.println(acheivements.items.length);
		batch.end();
		batch.begin();
		batch.draw(constants.getTexture(), constants.getLeft().x, constants.getLeft().y,
				constants.getLeft().width, constants.getLeft().height);
		batch.draw(constants.getTexture(), constants.getRight().x, constants.getRight().y,
				constants.getRight().width, constants.getRight().height);
		batch.draw(constants.getTexture(), constants.getTop().x, constants.getTop().y,
				constants.getTop().width, constants.getTop().height);
		batch.draw(constants.getTexture(), constants.getBottom().x, constants.getBottom().y,
				constants.getBottom().width, constants.getBottom().height);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		font = new BitmapFont();
		stage = new Stage();
		bit_map_font = new BitmapFont(Gdx.files.internal("first_font.fnt"), false);
		label_style = new LabelStyle(bit_map_font, Color.BLUE);
		label = new Label("Acheivements", label_style);
		label.setPosition(constants.getRealWidth() / 2 + constants.getRealLeftX(), constants.getRealHeight() / 1.1f + constants.getRealBottomY());
		stage.addActor(label);
		
		skin = new Skin();
		button_atlas = new TextureAtlas("buttons/greenButton.pack");
		skin.addRegions(button_atlas);
		button_text = new TextButtonStyle();
		button_text.up = skin.getDrawable("greenButton");
		button_text.over = skin.getDrawable("greenButtonPressed");
		button_text.down = skin.getDrawable("greenButtonPressed");
		button_text.font = bit_map_font;
		button = new TextButton("Main Menu", button_text);
		button.setWidth(constants.getRealWidth() / 4);
		button.setHeight(constants.getRealHeight() / 8);
		button.setPosition((constants.getRealWidth() / 2) - (button.getWidth() * 2) + constants.getRealLeftX(), (constants.getRealHeight()) - (button.getHeight()) + constants.getRealBottomY());
		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
		button.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				dispose();
				game.setScreen(new MainMenu(game));
				return true;
			}
		});
		clearButton = new TextButton("Clear", button_text);
		clearButton.setWidth(constants.getRealWidth() / 4);
		clearButton.setHeight(constants.getRealHeight() / 8);
		clearButton.setPosition((constants.getRealWidth() / 2) - (button.getWidth() * 2) + constants.getRealLeftX(), (constants.getRealHeight()) - (button.getHeight() *2) + constants.getRealBottomY());
		stage.addActor(clearButton);
		Gdx.input.setInputProcessor(stage);
		clearButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				for (Acheivement acheivement : acheivements.items) {
					//if (!acheivement.name.equals("Lily pad then fly then Same Lily pad")){
						//acheivement.value = true;
					//} else {
						acheivement.value = false;
					//}
				}
				return true;
			}
		});
		acheivementButton = new TextButton("Acheivement API", button_text);
		acheivementButton.setWidth(constants.getRealWidth() / 4);
		acheivementButton.setHeight(constants.getRealHeight() / 8);
		acheivementButton.setPosition((constants.getRealWidth() / 2) - (acheivementButton.getWidth() * 2) + constants.getRealLeftX(), (constants.getRealHeight()) - (acheivementButton.getHeight()*3) + constants.getRealBottomY());
		stage.addActor(acheivementButton);
		Gdx.input.setInputProcessor(stage);
		acheivementButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				dispose();
				game.actionResolver.getAchievementsGPGS();
				return true;
			}
		});
		batch = new SpriteBatch();
		acheivementStuff();
		int i = 2;
		label_style.font.scale(constants.getRealHeight() / 5000f);
		label_style.font.setScale(constants.getRealHeight() / 1200f);
		for (Acheivement acheivement : acheivements.items) {
			Label newLabel = new Label(acheivement.getFullName(), label_style);
			newLabel.setPosition(constants.getRealWidth() / 5f + constants.getRealLeftX(), constants.getRealHeight() / 1.3f - 25 * i + constants.getRealBottomY());
			stage.addActor(newLabel);
			i++;
		}
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
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		try {
			Acheivements.saveAcheivements(acheivements, acheiveFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Made it to dispose!");
	}

}
