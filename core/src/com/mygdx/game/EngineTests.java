package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class EngineTests implements Screen {
	
	Body[] bodies;
	
	private OrthographicCamera camera;
	private float xOffset, yOffset;
	public float xSpeed,ySpeed;
	private float zoomW, zoomH;
	
	private Sprite blue;
	private int spectated;
	
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;

	private Body[] rope;

	private BitmapFont font;
	

	@Override
	public void render(float delta)
	{
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.position.x = xOffset;
		camera.position.y = yOffset;		
		
		camera.update();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(camera.combined);		
		
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(-1000, -15, 2000, 15);
		
		shapeRenderer.rect(-500, 0, 15, 2000);
		shapeRenderer.rect(500, 0, 15, 2000);
		
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(0, 0, 5);
		
		if(Gdx.input.justTouched()) //bodies[0].velY+=3f;
		{
			for(int i = 0; i<10000; i++)
			{
				if(bodies[i] == null)
				{
					Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
			        camera.unproject(touchPos);
			            
					bodies[i] = new Body(15, touchPos.x, touchPos.y);
					//bodies[i].updateBlocked = true;
					//bodies[i].velX = new Random().nextInt(4)-2;
					break;
				}
			
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)) //bodies[0].velY+=3f;
		{
			for(int i = 0; i<10000; i++)
			{
				if(bodies[i] == null)
				{
					Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
			        camera.unproject(touchPos);
			            
					bodies[i] = new Body(5, touchPos.x, touchPos.y);	
					//bodies[i].velX = new Random().nextInt(40)-20;
					//bodies[i].velY = new Random().nextInt(2)-1;
					//bodies[i].fixed = true;
				
					break;
				}
			
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.ENTER)) //bodies[0].velY+=3f;
		{
			for(int i = 0; i<10000; i++)
			{
				if(bodies[i] == null)
				{
					Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
			        camera.unproject(touchPos);
			            
					bodies[i] = new Body(25, touchPos.x, touchPos.y);					
					bodies[i].fixed = true;
				
					break;
				}
			
			}
		}		
		
		for(Body b : bodies)
		{	
			if(b!=null)
			{
				
				shapeRenderer.setColor(Color.DARK_GRAY);
								
				if(!b.fixed)
				{				
					updateGravity(b);
					
					checkCollision(b);					
					
					updatePosition(b);
					
					//b.velX*=0.5f;
					//b.velY*=0.5f;
					
					updateGravity(b);
					
					checkCollision(b);
					
					updatePosition(b);
						
				}
				
				shapeRenderer.circle(b.x, b.y, b.mass);		
			}
		}
		
		shapeRenderer.end();
		
		batch.begin();
		batch.setProjectionMatrix(camera.combined);

		font.draw(batch, "X:"+bodies[spectated].x,-camera.viewportWidth/2+10, camera.viewportHeight-50);
		font.draw(batch, "Y:"+bodies[spectated].y, -camera.viewportWidth/2+10, camera.viewportHeight-65);
		font.draw(batch, "VelX:+"+bodies[spectated].velX, -camera.viewportWidth/2+10, camera.viewportHeight-80);
		font.draw(batch, "VelY:"+bodies[spectated].velY, -camera.viewportWidth/2+10, camera.viewportHeight-95);
		
		if(bodies[spectated] == null)
		System.out.println(bodies[spectated].x);
		
		for(Body b : bodies)
		{	
			if(b!=null)
			{
				//batch.draw(blue, b.x-13, b.y-13,26,26);
				
			}
		}
		
		batch.end();
		
		
		
		
		updateCam();
		
	}

	private void checkCollision(Body b) 
	{
		float dist = 0;
		
		for(Body b2 : bodies)
		{
			if(b2!=null)
			{
				if(b2 != b)
				{
					float d = (float) Math.sqrt((b2.x-b.x)*(b2.x-b.x) + (b2.y-b.y)*(b2.y-b.y));
					
					if(d < b.mass+b2.mass)
					{
						shapeRenderer.setColor(Color.RED);
						
						//if(dist == 0)
						{
							dist=d;
							b.tmp = b2;
						}
						//else if(d <= dist)
						{
							dist=d;						
							b.tmp = b2;
							break;
						}
						//Collision(b);
					}
				}
			}
		}
	}
	
	private void Collision(Body b)
	{
		Body b2 = b.tmp;
	
		float deltaX = (b2.x - b.x) *0.1f;
		float deltaY = (b2.y - b.y) *0.1f;
							
		float mass = b.mass, mass2 = b2.mass;		
		float newVelX1 = 0, newVelY1 = 0, newVelX2 = 0,newVelY2 = 0; 		
		
		float t = (float) Math.sqrt(b.velX*b.velX +b.velY*b.velY);
		
		if(b2.fixed || b2.updateBlocked || b2.constraint != null)
		{			
			b2.velX = -(b2.x - b.x);
			b2.velY = -(b2.y - b.y);			
			
			float f = (float) Math.sqrt(b2.velX*b2.velX +b2.velY*b2.velY);
			
			b2.velX*=t/f;
			b2.velY*=t/f;
			
			b.velX*= b.bounce;
			b.velY*=b.bounce;			
			b2.velX*=b.bounce;
			b2.velY*=b.bounce;					
					
			mass2 = mass;
		}
									
		newVelX1 = (b.velX * (mass-mass2) + (2f * mass2 * b2.velX)) / (mass + mass2);
		newVelY1 = (b.velY * (mass-mass2) + (2f * mass2 * b2.velY)) / (mass + mass2);
		newVelX2 = (b2.velX * (mass2-mass) + (2f * mass * b.velX)) / (mass + mass2);
		newVelY2 = (b2.velY * (mass2-mass) + (2f * mass * b.velY)) / (mass + mass2);	
								
		
		
		{
			b.velX=newVelX1;
			b.velY=newVelY1;	
		}
		
	
		//b2.x+=deltaX/5;
		//b2.y+=deltaY/5;
		
		
		if(b2.fixed || b2.updateBlocked)
		{
			//b.velX-=newVelX2*b.bounce;
			//b.velY-=newVelY2*b.bounce;	
			
			b2.velX = 0f;
			b2.velY = 0f;
		}
		
		if(!b2.fixed && !b2.updateBlocked)
		{			
			b2.velX=newVelX2;
			b2.velY=newVelY2;
		}
		
	
		float d = (float) Math.sqrt((b2.x-b.x)*(b2.x-b.x) + (b2.y-b.y)*(b2.y-b.y));
		int i = 0;
		
		while(d < b.mass+b2.mass)
		{
			i++;
			
			if(!b.fixed)
			{			
				b.x-=deltaX/10f;
				b.y-=deltaY/10f;
			}
			
			if(!b2.fixed && b2.constraint != null)
			{	
				
				b2.x+=deltaX/10f;
				b2.y+=deltaY/10f;
				
				checkCollision(b2);
				
				if(b2.tmp != null)
				{
					b2.x-=deltaX/10f;
					b2.y-=deltaY/10f;
					
					b.x-=deltaX/10f;
					b.y-=deltaY/10f;
					
					b2.tmp = null;
				}
			}
			else
			{
				b.x-=deltaX/10f;
				b.y-=deltaY/10f;
				
			}
			
			d = (float) Math.sqrt((b2.x-b.x)*(b2.x-b.x) + (b2.y-b.y)*(b2.y-b.y));
			
			if(i > 100) break;
		}		
	}

	private void updatePosition(Body b)
	{
		if(b.tmp != null)
			Collision(b);
		
		b.tmp = null;
		
		if(b.constraint != null)
		{
			constrain(b);
		}
		
		
		if(!b.fixed && !b.updateBlocked)
		{			
			if(b.x-b.mass+b.velX < -500 || b.x+b.mass+b.velX > 500)
			{
				if(b.x < 0)
					b.x = -500+b.mass;
				else
					b.x = 500-b.mass;
				
				
				b.velX = -b.velX * b.bounce;
			}
			else
				b.x+=b.velX;	
		
			if(b.y-b.mass+b.velY <= 0)
			{
				b.y=0+b.mass;
				b.velY = -b.velY * b.bounce;
				//b.velX = b.velX * 0.995f;			
			}
			else if(b.y-b.mass+b.velY >= 1000)
			{
				b.y=1000-b.mass;
				b.velY = -b.velY * b.bounce;
				//b.velX = b.velX * 0.995f;	
			}
			else
				b.y+=b.velY;
			
		}
		
		//System.out.println(b.y);
	}

	private void constrain(Body b) {
		
		Body b2;
		
		for(int i = 0; i < b.constraint.length; i++)
		{
			if(b.constraint[i] == b)
			{
				if(i > 0)
				{
					b2 = bodies[i-1];
					
					float d = (float) Math.sqrt((b2.x-b.x)*(b2.x-b.x) + (b2.y-b.y)*(b2.y-b.y));
					
					if(d > 15) 
					{						
						push(b, b2, d-15);
					}
						
					
				}
				
				if(i<b.constraint.length-1)
				{
					b2 = bodies[i+1];
					
					float d = (float) Math.sqrt((b2.x-b.x)*(b2.x-b.x) + (b2.y-b.y)*(b2.y-b.y));
					
					if(d > 15) 
					{						
						push(b, b2, d-15);
					}
				}
			}
		}
	}
	
	private void push(Body b, Body b2, float strength) 
	{		
		float t = (float) Math.sqrt(b.velX*b.velX +b.velY*b.velY);
			
		float deltaX = (b2.x - b.x);
		float deltaY = (b2.y - b.y);
		
		
		strength*=b.bounce;
		
		float s = (float) Math.sqrt(deltaX*deltaX+deltaY*deltaY);
		
		
		if(s == 0)
		{
			s = 1f;
		}
		
		deltaX*=strength/s;
		deltaY*=strength/s;
				
		if(b2.fixed)
		{
			b.x+=deltaX;
			b.y+=deltaY;
		}
		else
		{
			b.x+=deltaX/2;
			b.y+=deltaY/2;
			
			b2.x-=deltaX/2;
			b2.y-=deltaY/2;			
			
		}
		
		b.velX += deltaX/2;
		b.velY += deltaY/2;
		
		b2.velX -= deltaX/2;
		b2.velY -= deltaY/2;
		
	}

	private void updateGravity(Body b) 
	{
		if(b.y >= 0+b.mass)
			b.velY -= 0.097f;		
	}

	@Override
	public void resize(int width, int height)
	{
		camera.position.x = 0;
		camera.position.y = 0;
		
		camera.viewportWidth = width / zoomW;
		camera.viewportHeight = height /  zoomH;
	}
	
	public void updateCam()
	{
		xOffset+=xSpeed;
		yOffset+=ySpeed;
		
		xSpeed*=0.95f;
		ySpeed*=0.95f;
		
		if(Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT))
			xSpeed+=1f;
		if(Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT))
			xSpeed-=1f;			
		
		if(Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP))
			ySpeed+=1f;
		if(Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))
			ySpeed-=1f;		
		
		
		bodies[0].x+=xSpeed;
		bodies[0].y+=ySpeed;
		
	}

	@Override
	public void show() 
	{
		camera = new OrthographicCamera();
		
		zoomW = Gdx.graphics.getWidth() / GameTest.WIDTH;
		zoomH = Gdx.graphics.getHeight() /  GameTest.HEIGHT;
		
		zoomW*=.7f;
		zoomH*=.7f;
		
		yOffset = 100;
		
		blue = new Sprite(new Texture(Gdx.files.internal("test.png")));
		
		blue.setSize(5, 5);
		
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		
		
		bodies = new Body[10000];
		
		rope = new Body[30];
		
		for(int i = 0; i<30; i++)
		{
			bodies[i] = new Body(4, -400+i*10, 300);
			
			rope[i] = bodies[i];
			
			bodies[i].constraint = rope;
			
			bodies[i].velY = new Random().nextInt(4)-2;
		}		
		
		spectated = 1;
		
		
		bodies[0].fixed = true;
		bodies[29].fixed = true;
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
		// TODO Auto-generated method stub
		
	}

}
