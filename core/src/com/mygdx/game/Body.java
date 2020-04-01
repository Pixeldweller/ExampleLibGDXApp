package com.mygdx.game;

public class Body {
	
	public float mass, bounce = 0.75f;
	public float x, velX;
	public float y, velY;
	
	public boolean fixed, updateBlocked;
	
	
	public Body tmp, constraint[];
	
	public Body(float m, float x,float y)
	{
		mass=m;
		this.x=x;
		this.y=y;
	}
}
