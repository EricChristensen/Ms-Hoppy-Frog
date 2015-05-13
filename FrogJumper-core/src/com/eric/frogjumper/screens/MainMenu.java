package com.eric.frogjumper.screens;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.eric.frogjumper.Acheivements;
import com.eric.frogjumper.Constants;
import com.eric.frogjumper.Game1;
import com.eric.frogjumper.animations.FlyAnimation;
import com.eric.frogjumper.animations.FlyAnimationList;
import com.eric.frogjumper.characters.Fly;
import com.eric.frogjumper.characters.FlyList;

public class MainMenu implements Screen{
	
	Stage stage;
	Label label;
	LabelStyle label_style;
	BitmapFont bit_map_font;
	TextureAtlas button_atlas;
	TextButtonStyle button_text;
	TextButtonStyle button_text2;
	TextButton button;
	TextButton acheivementButton;
	TextButton infinityButton;
	Skin skin;
	SpriteBatch batch;
	Game1 game;
	Constants constants;
	OrthographicCamera cam;
	String faFile = "flyAnimations.dat";
	String flFile = "flyList.dat";
	Vector2 v;
	private BitmapFont bit_map_font2;
	
	
	public MainMenu(Game1 game){
		v = new Vector2(0, 0);
		this.game = game;
		constants = new Constants();
		if (!game.actionResolver.getSignedInGPGS()) {
			game.actionResolver.loginGPGS();
		} else {
			//game.actionResolver.loginGPGS();
		}
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
		//constants.draw(/*batch,*/ 0, cam);
		batch.begin();
		stage.draw();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		this.cam = new OrthographicCamera();
		stage = new Stage();
		bit_map_font = new BitmapFont(Gdx.files.internal("first_font.fnt"), false);
		bit_map_font2 = new BitmapFont(Gdx.files.internal("first_font.fnt"), false);
		skin = new Skin();
		button_atlas = new TextureAtlas("buttons/greenButton.pack");
		skin.addRegions(button_atlas);
		button_text = new TextButtonStyle();
		button_text.up = skin.getDrawable("greenButton");
		button_text.over = skin.getDrawable("greenButtonPressed");
		button_text.down = skin.getDrawable("greenButtonPressed");
		button_text.font = bit_map_font;
		button_text.font.setScale(constants.getRealHeight() / 800f);
		button = new TextButton("arcade", button_text);
		button.setWidth(constants.getRealWidth() / 4);
		button.setHeight(constants.getRealHeight() / 8);
		System.out.println("REAL HEIGHT: " + constants.getRealHeight());
		button.setPosition((constants.getRealWidth() / 2) - (button.getWidth() / 2) + constants.getRealLeftX(), (constants.getRealHeight() / 2) - (button.getHeight() / 2) + constants.getRealBottomY());
		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
		button.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				dispose();
				game.setScreen(new PlayScreen(game, false));
				//return true;
			}
		});
		button_text2 = new TextButtonStyle();
		button_text2.up = skin.getDrawable("greenButton");
		button_text2.over = skin.getDrawable("greenButtonPressed");
		button_text2.down = skin.getDrawable("greenButtonPressed");
		button_text2.font = bit_map_font2;
		button_text2.font.setScale(constants.getRealHeight() / 1400f);
		acheivementButton = new TextButton("Acheivements", button_text2);
		acheivementButton.setWidth(constants.getRealWidth() / 4);
		acheivementButton.setHeight(constants.getRealHeight() / 8);
		acheivementButton.setPosition((constants.getRealWidth() / 2) - (button.getWidth() / 2) + constants.getRealLeftX(), (constants.getRealHeight() / 2) - (button.getHeight() * 3) + constants.getRealBottomY());
		stage.addActor(acheivementButton);
		Gdx.input.setInputProcessor(stage);
		acheivementButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				
				game.setScreen(new AcheivementScreen(game));
				//return true;
			}
		});
		infinityButton = new TextButton("infinity", button_text);
		infinityButton.setWidth(constants.getRealWidth() / 4);
		infinityButton.setHeight(constants.getRealHeight() / 8);
		infinityButton.setPosition((constants.getRealWidth() / 2) - (button.getWidth() / 2) + constants.getRealLeftX(), (constants.getRealHeight() / 2) - (button.getHeight() * 1.75f) + constants.getRealBottomY());
		stage.addActor(infinityButton);
		Gdx.input.setInputProcessor(stage);
		infinityButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				
				game.setScreen(new PlayScreen(game, true));
				//return true;
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
		/*game.fList.flys.clear();
		for (int i = 0; i < game.flyPool.getFree(); i++) {
			Fly fly = game.flyPool.obtain();
			fly.init(v);
			game.fList.flys.add(fly);
		}
		game.fAnimationList.flyAnimations.clear();
		for (int i = 0; i < game.flyAnimationPool.getFree(); i++){
			FlyAnimation fa = game.flyAnimationPool.obtain();
			//Vector2 v = new Vector2(0, 0);
			fa.init(0, 0, v, v);
			game.fAnimationList.flyAnimations.add(fa);
		}*/
		/*try {
			//Acheivements.saveAcheivements(acheivements, acheiveFile);
			//FlyList.saveFlyList(game.fList, flFile);
			//FlyAnimationList.saveFlyAnimationList(game.fAnimationList, faFile);
			//FlyList.saveFlyList(game.fList, flFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

}
