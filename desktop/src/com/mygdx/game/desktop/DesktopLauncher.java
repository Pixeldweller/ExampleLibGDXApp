package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.GameTest;
import com.mygdx.game.Triton;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.height = 700;
		config.width = 1200;
		
		//new LwjglApplication(new GameTest(), config);
		new LwjglApplication(new Triton(), config);
	}
}
