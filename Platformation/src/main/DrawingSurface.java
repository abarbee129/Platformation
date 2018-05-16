package main;


import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import entities.Mario;
import entities.Player;
import worldGeometry.Booster;
import worldGeometry.Platform;
import processing.core.PApplet;
import processing.core.PImage;

public class DrawingSurface extends PApplet {

	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 600;

	private Rectangle screenRect;
	private Booster[] booster = new Booster[2];

	private Player player;
	private ArrayList<Shape> obstacles;
	private ArrayList<Platform> platforms;
	private ArrayList<Integer> keys;
	
	private ArrayList<PImage> assets;

	public DrawingSurface() {
		super();
		assets = new ArrayList<PImage>();
		keys = new ArrayList<Integer>();
		screenRect = new Rectangle(0,0,DRAWING_WIDTH,DRAWING_HEIGHT);
		obstacles = new ArrayList<Shape>();
		platforms = new ArrayList<Platform>();
		obstacles.add(new Rectangle(200,400,400,50));
		obstacles.add(new Rectangle(0,250,100,50));
		obstacles.add(new Rectangle(700,250,100,50));
		obstacles.add(new Rectangle(375,300,50,100));
		obstacles.add(new Rectangle(300,250,200,50));
		booster[0] = new Booster(200,275);
		booster[1] = new Booster(-100,-100);
		
		for(Shape s : obstacles) {
			platforms.add(new Platform(s));
		}
	}


	public void spawnNewMario() {
		player = new Player(assets.get(0), DRAWING_WIDTH/2-Mario.MARIO_WIDTH/2,50);
	}
	
	public void runMe() {
		runSketch();
	}

	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {
		//size(0,0,PApplet.P3D);
		assets.add(loadImage("mario.png"));
		
		spawnNewMario();
	}

	// The statements in draw() are executed until the 
	// program is stopped. Each statement is executed in 
	// sequence and after the last line is read, the first 
	// line is executed again.
	public void draw() {

		// drawing stuff
		background(0,255,255);   

		pushMatrix();

		float ratioX = (float)width/DRAWING_WIDTH;
		float ratioY = (float)height/DRAWING_HEIGHT;

		scale(ratioX, ratioY);

		fill(100);
		for (Shape s : obstacles) {
			if (s instanceof Rectangle) {
				Rectangle r = (Rectangle)s;
				rect(r.x,r.y,r.width,r.height);
				pushStyle();
				fill(255,0,0);
				rect((float)r.x+r.width/2,(float)r.y+r.height/2,(float)5,(float)5);
				popStyle();
			}
		}
		for(Booster b : booster) {
			b.draw(this);
		}

		player.draw(this);

		popMatrix();


		// modifying stuff

		if (isPressed(KeyEvent.VK_LEFT))
			player.walk(-1);
		if (isPressed(KeyEvent.VK_RIGHT))
			player.walk(1);
		if (isPressed(KeyEvent.VK_UP))
			player.jump();

		player.act(obstacles);

		if (!screenRect.intersects(player))
			spawnNewMario();
	}


	public void keyPressed() {
		keys.add(keyCode);
	}

	public void keyReleased() {
		while(keys.contains(keyCode))
			keys.remove(new Integer(keyCode));
	}

	public boolean isPressed(Integer code) {
		return keys.contains(code);
	}


}

