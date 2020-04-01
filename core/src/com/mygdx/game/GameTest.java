package com.mygdx.game;

import com.badlogic.gdx.Game;

public class GameTest extends Game {
	
	public static float WIDTH;
	public static float HEIGHT;
	
	
	@Override
	public void create() {
		WIDTH = 864;
		HEIGHT = 486;
		
		setScreen(new EngineTests());
//		setScreen(new PrototypeCombatScreen());
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

	public void resize(int width, int height)
	{super.resize(width, height);}
}