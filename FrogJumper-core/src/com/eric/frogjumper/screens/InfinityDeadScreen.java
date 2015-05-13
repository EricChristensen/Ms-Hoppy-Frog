package com.eric.frogjumper.screens;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.eric.frogjumper.Constants;
import com.eric.frogjumper.Game1;
import com.eric.frogjumper.highscore.HighScore;
import com.eric.frogjumper.highscore.HighScores;
import com.eric.frogjumper.listeners.MyTextInputListener;

public class InfinityDeadScreen implements Screen {

	String leaderBoard = "CgkIiJLhl_IDEAIQEA";
	Stage stage;
	Label label;
	LabelStyle label_style;
	BitmapFont bit_map_font;
	TextureAtlas button_atlas;
	TextButtonStyle button_text;
	TextButtonStyle button_text_main;
	TextButton button;
	TextButton mainMenuButton;
	TextButton leaderButton;
	Skin skin;
	SpriteBatch batch;
	float newScore;
	Game1 game;
	HighScores highScores;
	private MyTextInputListener listener;
	private String user;
	String hsFile = "infinityScores.dat";
	String faFile = "flyAnimations.dat";
	String flFile = "flyList.dat";
	private boolean enteredName;
	PlayScreen oldScreen;
	ShapeRenderer sr;
	Sound smallApplause;
	Sound largeApplause;
	AssetManager assetManager;
	Constants constants;
	private int count = 0;
	
	public InfinityDeadScreen(Game1 game, float score, PlayScreen oldScreen, Sound smallApplause, Sound largeApplause) {
		this.game = game;
		this.oldScreen = oldScreen;
		this.newScore = score;
		this.smallApplause = smallApplause;
		this.largeApplause = largeApplause;
		this.constants = new Constants();
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	private void update() {
		stage.act();
		if (this.count == 0) { 
			if (highScores.getItems().size() > 0){
				if (highScores.getItems().get(0).getScore() < this.newScore) {
					smallApplause.play(.7f);
					largeApplause.play(.2f);
					if (game.actionResolver.getSignedInGPGS()) {
						game.actionResolver.submitHeight((int)this.newScore);
						System.out.println("Submit height success");
					}
				} else {
					smallApplause.play(.2f);
				}
			} else {
				smallApplause.play(.7f);
				largeApplause.play(.2f);
				if (game.actionResolver.getSignedInGPGS()) {
					game.actionResolver.submitHeight((int)this.newScore);
					System.out.println("Submit height success");
				}
			}
			count += 1;
		}
		if (Gdx.input.isKeyPressed(Keys.M)) {
			game.setScreen(new MainMenu(game));
		}
		if (user != null && !enteredName){
			System.out.println("USER = " + user);
			if ("Don't call me poopsie!".equals(user)){
				highScores.getItems().clear();
			}
			HighScore hs = new HighScore(user, newScore);
			highScores.getItems().add(hs);
			highScores.sort();
			Label newLabel = new Label(hs.getHighScore(), label_style);
			newLabel.setFontScale(constants.getRealHeight() / 1200f);
			newLabel.setPosition(constants.getRealWidth() / 2 + constants.getRealLeftX(), stage.getActors().peek().getHeight() - 15 + constants.getRealBottomY());
			stage.addActor(newLabel);

			enteredName = true;
		} else if (user == null) {
			user = listener.getInput();
			//System.out.println("I'm here!");
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		oldScreen.draw();
		/*sr.begin(ShapeType.Line);
		sr.setColor(Color.BLUE);
		sr.rect(stage.getViewport().getScreenX(), stage.getViewport().getScreenY(), stage.getWidth(), stage.getHeight());
		sr.end();*/
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		this.count = 0;
		listener = new MyTextInputListener();
		sr = new ShapeRenderer();
		Gdx.input.getTextInput(listener, "Enter your name", "Poopsie");
		user = listener.getInput();
		highscoreStuff();
		/*if (highScores.getItems().get(0).getScore() < this.newScore) {
			largeApplause.play();
		} else {
			smallApplause.play();
		}*/
		stageStuff();
	}

	private void highscoreStuff() {
		if (Gdx.files.local(hsFile).exists()) {
			System.out.println("It exists!!!");
			try {
				highScores = HighScores.readHighScores(hsFile);
				//highScores.highScores.add(new HighScore(user, newScore));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("highscores exists, reading highscores");
		} else {
			System.out.println("It doesn't exist :(");
			highScores = new HighScores();
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				HighScores.saveHighScores(highScores, hsFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("highscores does not exist yet, creating highscores " + newScore);
		}
		if (highScores == null){
			System.out.println("highscores does not exist yet, creating highscores " + newScore);
			highScores = new HighScores();
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				HighScores.saveHighScores(highScores, hsFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("highscores does not exist yet, creating highscores " + newScore);
		} else if (highScores.getItems() == null){
			System.out.println("highscores does not exist yet, creating highscores " + newScore);
			highScores = new HighScores();
			//highScores.highScores.add(new HighScore(user, newScore));
			try {
				HighScores.saveHighScores(highScores, hsFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("highscores does not exist yet, creating highscores " + newScore);
		}
	}
	
	private void stageStuff() {
		stage = new Stage();
		/*stage.getViewport().setScreenHeight((int)(constants.getRealHeight() / 20f));
		stage.getViewport().setScreenWidth((int)(constants.getRealWidth() / 20f));
		stage.getViewport().setScreenX(constants.getRealWidth() / 5);
		stage.getViewport().setScreenY(constants.getRealHeight() / 5);*/
		int vx = stage.getViewport().getScreenX();
		int vy = stage.getViewport().getScreenY();
		bit_map_font = new BitmapFont(Gdx.files.internal("first_font.fnt"), false);
		label_style = new LabelStyle(bit_map_font, Color.BLUE);
		label_style.font.setScale(constants.getRealHeight() / 1200f);
		label = new Label("Highest Score: Infinity Mode", label_style);
		label.setPosition(constants.getRealWidth() / 2 + vx + constants.getRealLeftX(), constants.getRealHeight() / 2 + vy + constants.getRealBottomY());
		int i = 2;
		for (HighScore score : this.highScores.getItems()){
			Label newLabel = new Label(score.getHighScore(), label_style);
			newLabel.setFontScale(constants.getRealHeight() / 1200f);
			newLabel.setPosition(constants.getRealWidth() / 2 + constants.getRealLeftX(), constants.getRealHeight() / 2 - 25 * i + constants.getRealBottomY());
			i++;
			stage.addActor(newLabel);
		}
		stage.addActor(label);
		skin = new Skin();
		button_atlas = new TextureAtlas("buttons/greenButton.pack");
		skin.addRegions(button_atlas);
		button_text = new TextButtonStyle();
		button_text.up = skin.getDrawable("greenButton");
		button_text.over = skin.getDrawable("greenButtonPressed");
		button_text.down = skin.getDrawable("greenButtonPressed");
		button_text.font = bit_map_font;
		button = new TextButton("Play Again", button_text);
		button.setWidth(constants.getRealWidth() / 4);
		button.setHeight(constants.getRealHeight() / 8);
		button.setPosition((constants.getRealWidth() / 4) - (button.getWidth() / 2) + constants.getRealLeftX(), (constants.getRealHeight() / 2 + constants.getRealBottomY()) - (button.getHeight() / 2));
		stage.addActor(button);
		button.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				game.setScreen(new PlayScreen(game, true));
				dispose();
				return true;
			}
		});
		leaderButton = new TextButton("Leader Board", button_text);
		leaderButton.setWidth(constants.getRealWidth() / 4);
		leaderButton.setHeight(constants.getRealHeight() / 8);
		leaderButton.setPosition((constants.getRealWidth() / 4) - (leaderButton.getWidth() / 2) + constants.getRealLeftX(), (constants.getRealHeight() / 2 + constants.getRealBottomY()) - (button.getHeight() * 2.5f));
		stage.addActor(leaderButton);
		leaderButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				game.actionResolver.getHeightGPGS();
				dispose();
				return true;
			}
		});
		button_text.font.setScale(constants.getRealHeight() / 1200f);
		mainMenuButton = new TextButton("Main Menu", button_text);
		mainMenuButton.setWidth(constants.getRealWidth() / 4);
		mainMenuButton.setHeight(constants.getRealHeight() / 8);
		mainMenuButton.setPosition((constants.getRealWidth() / 4f) - (button.getWidth() / 2) + constants.getRealLeftX(), (constants.getRealHeight() / 2) - (button.getHeight() * 1.5f) + constants.getRealBottomY());
		stage.addActor(mainMenuButton);
		Gdx.input.setInputProcessor(stage);
		System.out.println("Show. processor = " + Gdx.input.getInputProcessor().toString());
		mainMenuButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				game.setScreen(new MainMenu(game));
				dispose();
				return true;
			}
		});
		batch = new SpriteBatch();
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
			HighScores.saveHighScores(highScores, hsFile);
		//	FlyList.saveFlyList(game.flyList, flFile);
			//FlyAnimationList.saveFlyAnimationList(game.flyAnimationList, faFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Made it to dispose!");
	}

}
