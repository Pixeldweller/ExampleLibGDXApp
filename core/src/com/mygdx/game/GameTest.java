package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.ui.example.ConfigScreen;

public class GameTest extends Game {
	
	public static float WIDTH;
	public static float HEIGHT;
	private static float CENTER_X;
	private static float CENTER_Y;
	
	
	@Override
	public void create() {
		WIDTH = 864;
		HEIGHT = 486;
		
//		setScreen(new EngineTests());
		setScreen(new ConfigScreen());
	}
	
	public void dispose()
	{super.dispose();}
	
	public void render()
	{
		try
		{
			super.render();			
		}
		catch (Exception e) {e.printStackTrace();}		
	}
	
	@Override
	public void pause() 
	{super.pause();}

	@Override
	public void resume()
	{super.resume();}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		CENTER_X = WIDTH / 2f;
		CENTER_Y = HEIGHT / 2f;
	}

	
	public static float centerX() {
		return CENTER_X;
	}

	public static float centerY() {
		return CENTER_Y;
	}
}