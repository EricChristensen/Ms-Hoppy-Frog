package com.eric.frogjumper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.eric.frogjumper.Game1;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 1280 / 2;
		config.width = 800 / 2;
		new LwjglApplication(new Game1(new ActionResolverDesktop()), config);
	}
}
