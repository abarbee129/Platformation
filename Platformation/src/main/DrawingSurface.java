package main;


import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;

import entities.Enemies;
import entities.Mario;
import entities.MeleeEnemy;
import entities.Player;
import worldGeometry.Booster;
import worldGeometry.Platform;
import processing.core.PApplet;
import processing.core.PImage;

public class DrawingSurface extends PApplet {

	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 600;

	public static final String fileSeparator = System.getProperty("file.separator");

	private Rectangle screenRect;
	private Booster[] booster = new Booster[4];

	private Player player;
	private ArrayList<Shape> obstacles;
	private ArrayList<Platform> platforms;
	private ArrayList<Integer> keys;
	private MeleeEnemy e;

	private ArrayList<PImage> assets;

	public DrawingSurface() {
		super();
		assets = new ArrayList<PImage>();
		keys = new ArrayList<Integer>();
		screenRect = new Rectangle(0,0,DRAWING_WIDTH,DRAWING_HEIGHT);
		obstacles = new ArrayList<Shape>();
		platforms = new ArrayList<Platform>(); 
		initLevel("Levels" + fileSeparator + "Level1.txt");
		/*
		obstacles.add(new Rectangle(200,400,400,50));
		obstacles.add(new Rectangle(0,250,100,50));
		obstacles.add(new Rectangle(700,250,100,50));
		obstacles.add(new Rectangle(375,300,50,100));
		obstacles.add(new Rectangle(300,250,200,50));
		booster[0] = new Booster(290,230); 
		booster[1] = new Booster(290,500);
		booster[2] = new Booster(290,500); 
		booster[3] = new Booster(290,500);
		 */
		// cool
		for(Shape s : obstacles) {
			platforms.add(new Platform(s));
		}
	}

	public void initLevel(String fileName) {
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader bReader = new BufferedReader(reader);
			char[] chars = null;
			double yoff = 0;
			double xoff = 0;
			int rows = 24;
			double pHeight = DRAWING_HEIGHT/rows;


			while(bReader.ready()) {
				chars = bReader.readLine().toCharArray();
				for(char c : chars) {
					if(c == '#') {
						System.out.println(xoff);
						obstacles.add(new Rectangle((int)xoff,(int)yoff,(int)pHeight,(int)pHeight));
					}
					else {

					}
					xoff+=pHeight/2;
				}
				yoff+= pHeight;
				xoff = 0;


			}


		} 
		catch (IOException e) {
			e.printStackTrace();
		} 

	}
	public void spawnNewMario() {
		player = new Player(assets.get(0), DRAWING_WIDTH/2-Mario.MARIO_WIDTH/2,50);
	}

	public void spawnNewEnemy() {
		e = new MeleeEnemy(assets.get(1),4,250-85, 2,1);
	}

	public void runMe() {
		runSketch();
	}

	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {
		//size(0,0,PApplet.P3D);
		assets.add(loadImage("Player.png"));
		assets.add(loadImage("Melee.png"));

		spawnNewMario();
		spawnNewEnemy();
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
			if(b != null) {
				b.draw(this);
			}
		}

		player.draw(this);
		e.draw(this);


		popMatrix();


		// modifying stuff

		if (isPressed(KeyEvent.VK_LEFT))
			player.walk(-1);
		if (isPressed(KeyEvent.VK_RIGHT))
			player.walk(1);
		if (isPressed(KeyEvent.VK_UP))
			player.jump();

		// check for booster collisions and accelerate
		for (int i = 0; i < booster.length; i++) {
			if(booster[i]!=null) {
				double[] dims = booster[i].getCBoxDimensions();
				if (player.doesCollideWith(dims[0], dims[1], (int)dims[2], (int)dims[3])) {
					player.accelerate(booster[i].getXBounceAcceleration(player.getx(), player.gety()), booster[i].getYBounceAcceleration(player.getx(), player.gety()));
				}
				else {}
			}
		}
		player.act(obstacles);
		e.act(obstacles);

		e.action(player, obstacles.get(1));


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

