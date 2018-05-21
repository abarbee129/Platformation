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

import entities.Enemy;
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
	private ArrayList<Booster> boosters;

	private Player player;
	private ArrayList<Shape> obstacles;
	private ArrayList<Platform> platforms;
	private ArrayList<Integer> keys;
	private ArrayList<MeleeEnemy> meleeEnemies; 
	

	private ArrayList<PImage> assets;

	public DrawingSurface() {
		
		
		super();
		assets = new ArrayList<PImage>();
		keys = new ArrayList<Integer>();
		boosters = new ArrayList<Booster>();
		screenRect = new Rectangle(0,0,DRAWING_WIDTH,DRAWING_HEIGHT);
		obstacles = new ArrayList<Shape>();
		platforms = new ArrayList<Platform>();
		meleeEnemies = new ArrayList<MeleeEnemy>(); 
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
						obstacles.add(new Rectangle((int)xoff,(int)yoff,(int)pHeight,(int)pHeight));
						
					}
					else if( c == 'b') {
						boosters.add(new Booster(xoff,yoff,(int)pHeight,(int)pHeight));
					}
					else if(c == 'm') {
						meleeEnemies.add(new MeleeEnemy(assets.get(1),(int)xoff,(int)yoff,50,10));
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

	public void runMe() {
		runSketch();
	}

	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {
		//size(0,0,PApplet.P3D);
		assets.add(loadImage("Player.png"));
		assets.add(loadImage("Melee.png"));
		initLevel("Levels" + fileSeparator + "Level1.txt");
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
		double xoff = DRAWING_WIDTH/2 - player.getx();
		this.translate((float)(xoff), 0);
		screenRect = new Rectangle((int)(player.getx()-DRAWING_WIDTH/2),0,DRAWING_WIDTH,DRAWING_HEIGHT);
		fill(100);
		for (Shape s : obstacles) {
			if (s instanceof Rectangle && s.intersects(screenRect)) {
				Rectangle r = (Rectangle)s;
				
				rect(r.x,r.y,r.width,r.height);
			}
		}
		pushStyle();
		fill(0,0,255);
		for(Booster b : boosters) {
			if(b != null) {
				b.draw(this);
			}
		}
		popStyle();

		player.draw(this);
		for(MeleeEnemy me : meleeEnemies) {
			me.actions(player, obstacles.get(1));
			me.act(obstacles);
			me.draw(this);

		}


		popMatrix();


		// modifying stuff

		if (isPressed(KeyEvent.VK_LEFT))
			player.walk(-1);
		if (isPressed(KeyEvent.VK_RIGHT))
			player.walk(1);
		if (isPressed(KeyEvent.VK_UP))
			player.jump();
		if (isPressed(KeyEvent.VK_DOWN)) {
			player.startShield();
		}
		else {
			player.endShield();
		} 
		
		

		// check for booster collisions and accelerate
		for (Booster b : boosters) {
			if(b!=null) {
				double[] dims = b.getCBoxDimensions();
				if (player.doesCollideWith(dims[0], dims[1], (int)dims[2], (int)dims[3])) {
					player.accelerate(b.getXBounceAcceleration(player.getx(), player.gety()), b.getYBounceAcceleration(player.getx(), player.gety()));
				}
				else {}
			}
		}
		
		player.act(obstacles);
		if(player.gety() > DRAWING_HEIGHT) {
			spawnNewMario();
		}


	
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

