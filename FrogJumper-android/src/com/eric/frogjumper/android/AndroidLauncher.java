package com.eric.frogjumper.android;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.eric.frogjumper.Acheivements;
import com.eric.frogjumper.ActionResolver;
import com.eric.frogjumper.Game1;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;


public class AndroidLauncher extends AndroidApplication  implements GameHelperListener, ActionResolver {

	private GameHelper gameHelper;
	Sound taDa;
	private Acheivements acheivements;
	String acheiveFile = "acheivements.dat";
	
	private class AchieveStuff extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.e("THREAD STUFF", "Hi ya!");
			acheivementStuff();
			Log.e("THREAD STUFF", "Done with that shit");
			return "Executed";
		}
		
	}
	
	@Override
	protected void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		 if (gameHelper == null) {
			   gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			   gameHelper.enableDebugLog(true);
		 }
		 gameHelper.setup(this);
		 AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		 initialize(new Game1(this), config);
		 taDa = Gdx.audio.newSound(Gdx.files.internal("TaDa.mp3"));
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
		loadAchievements();
		saveAchievements();
	}
	
	private void saveAchievements() {
		try {
			Acheivements.saveAcheivements(acheivements, acheiveFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadAchievements()  {
	    
	     // load achievements
		boolean fullLoad = false;  // set to 'true' to reload all achievements (ignoring cache)
	    long waitTime = (long) 60.0;    // seconds to wait for achievements to load before timing out
	    PendingResult<LoadAchievementsResult> p = Games.Achievements.load( gameHelper.getApiClient(), fullLoad );
	    Achievements.LoadAchievementsResult r = (Achievements.LoadAchievementsResult)p.await( waitTime, TimeUnit.SECONDS );
	     int status = r.getStatus().getStatusCode();
	     Log.e("LOAD ACHEIVEMENTS", "status_ok = " + GamesStatusCodes.STATUS_OK + "  your status = " + status);
	     if ( status != GamesStatusCodes.STATUS_OK )  {
	    	// taDa.play(.9f);
	        r.release();
	        return;           // Error Occured
	     }

	     // cache the loaded achievements
	     AchievementBuffer buf = r.getAchievements();
	     int bufSize = buf.getCount();
	     for ( int i = 0; i < bufSize; i++ )  {
	        Achievement ach = buf.get( i );
	       // taDa.play(.1f);
	        // here you now have access to the achievement's data
	        String id = ach.getAchievementId();  // the achievement ID string
	        boolean unlocked = ach.getState() == Achievement.STATE_UNLOCKED;  // is unlocked
	        Log.e("LOAD ACHIEVEMENTS:", " " + unlocked);
	        if (unlocked) {
	        	//if (acheivements.getById(id) != null) {
	        		acheivements.setById(true, id);
	        	//}
	        }
	     }
	     buf.close();
	     r.release();
	  }
	
	@Override
	protected void onStart(){
		super.onStart();
		gameHelper.onStart(this);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		gameHelper.onStop();
		
	}
	
	
	
	@Override
	public void onActivityResult(int request, int response, Intent data){
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}
	
	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}
	
	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex){
			
		}
	}
	
	@Override
	public void submitScoreGPGS(int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), "CgkIxc7W1aQbEAIQDw", score);
	}
	
	//public boolean isAcheivementUnlocked(){
		//Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()).
	//}
	
	@Override
	public void submitHeight(int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), "CgkIxc7W1aQbEAIQEA", score);
		
	}
	
	@Override
	public void unlockAchievementGPGS(String achievementId) {
		boolean fullLoad = false;  // set to 'true' to reload all achievements (ignoring cache)
	    long waitTime = (long) 60.0;    // seconds to wait for achievements to load before timing out
		PendingResult p = Games.Achievements.load( gameHelper.getApiClient(), false);
	    Achievements.LoadAchievementsResult r = (Achievements.LoadAchievementsResult)p.await( waitTime, TimeUnit.SECONDS );
		
	    Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
		taDa.play(.25f);
	}
	@Override
	public void getLeaderboardGPGS() {
		//startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
			//	"CgkIiJLhl_IDEAIQDw"), 100);
		 if (gameHelper.isSignedIn()) {
		     startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkIxc7W1aQbEAIQDw"), 100);
		 }
	 	  else if (!gameHelper.isConnecting()) {
	 	    loginGPGS();
	 	  }
	}
	
	public void getHeightGPGS() {
	//	startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
	//			"CgkIiJLhl_IDEAIQEA"), 100);
		 if (gameHelper.isSignedIn()) {
		     startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkIxc7W1aQbEAIQEA"), 100);
		  }
		  else if (!gameHelper.isConnecting()) {
		    loginGPGS();
		  }
	}
	
	
	@Override
	public void getAchievementsGPGS() {
	//	startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		if (gameHelper.isSignedIn()) {
		    startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		  }
		  else if (!gameHelper.isConnecting()) {
		    loginGPGS();
		  }
	}
	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		Log.e("ON SIGN IN FAILED", "sign in failed");
	}
	@Override
	public void onSignInSucceeded() {
		new AchieveStuff().execute();
	}
	
	
}
