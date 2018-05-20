package entities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import worldGeometry.Platform;

// add implements damageable once it exists
public class Player extends Sprite{

	public static final int PLAYER_WIDTH = 40;
	public static final int PLAYER_HEIGHT = 60;

	//
	private double baseHP;
	private double currentHP;

	private double currentEP;
	private double baseEP;
	private boolean replenishing;

	private boolean regen;
	private double level;
	private int skillPoints;
	private double attackStat;
	private double defStat;
	private double EXP;
	
	private double[] ox,oy;


	// Collision/Physics fields
	private double dx;
	private double dy;
	private double oldDx;
	private double oldDy;
	private double dt = 1 / 60.0;
	private double ddx;
	private double ddy;

	private boolean isTouchingGround;
	private boolean isMoving;
	private double ticksFromZeroToHalf = 4.0;
	private double ticksFromHalfToFull = 8.0;
	private double ticksToStop = 1.0;	
	private int maxDx = 400;
	private int maxDy = 240;
	private double fricMod = 0.5;
	double slow = 1;


	public Player(PImage img, int x, int y) {
		super(img, x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		ox = new double[3];
		oy = new double[3]; 
		baseHP = 100;
		currentHP = 100;
		baseEP = 200;
		currentEP = 200;
		attackStat = 10;
		defStat =  10;
		EXP = 0; 
	}
	public Player(PImage img, int x, int y,double slow) {
		super(img, x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		this.slow = slow;
		ox = new double[3];
		oy = new double[3]; 
		baseHP = 100;
		currentHP = 100;
		baseEP = 200;
		currentEP = 200;
		attackStat = 10;
		defStat =  10;
		EXP = 0; 
	}

	public void walk(int dir) {
		isMoving = true;
		double accelAmt = 0;
		if (dir*dx >= 0 && dir*dx <=maxDx/2) {
			accelAmt = maxDx / ticksFromZeroToHalf;
		}
		else if (dir*dx >= maxDx/2 && dir*dx <= maxDx) {
			double accelerationFromZeroToHalf = maxDx / 2 / ticksFromZeroToHalf;
			double accelerationCoefficient = accelerationFromZeroToHalf / ticksFromHalfToFull;
			double tickCount = ticksFromHalfToFull + Math.sqrt(Math.pow(ticksFromHalfToFull, 2) / (maxDx / 2) * (maxDx-dx));        
			accelAmt =  (accelerationCoefficient * tickCount) + accelerationFromZeroToHalf;
			accelAmt *= dir;
		}
		else if (dir*dx >= -maxDx && dir* dx <=0) {
			accelAmt = maxDx / ticksToStop;
			accelAmt *= dir;
		}
		else {

		}
		if (!isTouchingGround) {
			accelAmt = accelAmt * 2 / 3;
		}
		else {

		}
		if (accelAmt + dx > maxDx) {accelAmt = maxDx - dx;}
		else if(accelAmt + dx < -maxDx) {accelAmt = -maxDx - dx;}
		accelerate(accelAmt, 0); 
	}

	public void jump() {
		if(isTouchingGround){
			if(dy > -maxDy && dy < maxDy) {
				accelerate(0, -640);
			}
			isTouchingGround = false;
		}
	}

	public void accelerate (double dx, double dy) {
		// This is a simple accelerate method that adds dx and dy to the current velocity.
		this.dx += dx;
		this.dy += dy;

		ddx += dx;
		ddy += dy;
	}

	public double getDx() {
		return dx;
	}
	public double getDy() {
		return dy;
	}
	
	
	public void move() {
		double xchange = slow*dt * (oldDx + ((ddx/2) * dt));
		double ychange = slow*dt * (oldDy + ((ddy/2) * dt));
		super.moveByAmount(xchange, ychange);
		oldDx = this.dx;
		oldDy = this.dy;
		ddx = 0;
		ddy = 0;
	}
	public void act(ArrayList<Shape> shapes) {


		if(!isTouchingGround) {
			accelerate(0, 1440*dt);
		}
		isTouchingGround = false;
		for(Shape s : shapes) {
			Rectangle r = s.getBounds();
			if(intersects(r)) {
				double thisCX = x + PLAYER_WIDTH/2;
				double thisCY = y + PLAYER_HEIGHT/2;
				double oldCx = ox[1] + PLAYER_WIDTH/2;
				double oldCy = oy[1] + PLAYER_HEIGHT/2;
				double oxdif = oldCx - (r.x+r.width/2);
				double oydif = oldCy - (r.y+r.height/2);
				double xdif = thisCX - (r.x+r.width/2);
				double ydif = thisCY - (r.y+r.height/2);
				if(ydif < 0 && oydif > ydif) {
					// player is "above" the center of the platform
					super.moveByAmount(0, ydif);

				}
				else if(ydif < 0) {
					// player is "above" the center of the platform
					accelerate(0,-dy);
					super.moveByAmount(0, -(ydif+1)-(PLAYER_HEIGHT/2 + r.height/2));

					isTouchingGround = true;
				}
				else if(ydif > 0 && oydif > ydif) {
					// player is "below" the center of the platform	
					super.moveByAmount(0, r.getHeight()/2-ydif);
				}
				else if(xdif > 0 && oxdif > xdif) {
					// player is "right" of the center of the platform	
					super.moveByAmount(r.getWidth() - xdif, 0);
				}
				else if(xdif < 0 && oxdif < xdif) {
					// player is "left" of the center of the platform	
					super.moveByAmount(-(r.getWidth() + xdif), 0);
				}


			}
			else {
				double thisCX = x + PLAYER_WIDTH/2;
				double thisCY = y + PLAYER_HEIGHT/2;
				double xdif = thisCX - (r.x+r.width/2);
				double ydif = thisCY - (r.y+r.height/2);
				if(Math.abs(-ydif - (PLAYER_HEIGHT/2 + r.height/2))<1) {
					if(Math.abs(xdif)<1 + (PLAYER_WIDTH/2 + r.width/2)) {
						isTouchingGround = true;
					}
				}
				else {
					
				}
			}

		}
		applyFriction();
		move();
		isMoving = false;
		ox[2] = ox[1];
		oy[2] = oy[1];
		ox[1] = ox[0];
		oy[1] = oy[0];
		ox[0] = x;
		oy[0] = y;

	}
	
	public boolean doesCollideWith(double rx, double ry, int width, int height) {

		if (x > rx - this.width && x < rx + width ) {

			if (y > ry  - this.height && y < ry + height) {
				return true;
			}

			else {
				return false;
			}
		}

		else {
			return false;
		}

	}
	public double getx() {
		return x;
	}
	public double gety() {
		return y;
	}
	
	public void applyFriction() {
		if (isMoving) {
			if (isTouchingGround) {
				accelerate(-dx/12*fricMod,0); 
			}
			else {
				accelerate(-dx/12*fricMod,0); 	
			}
		}
		else {
			if (isTouchingGround) {
				accelerate(-dx/2*fricMod,0); 
			}
			else {
				accelerate(-dx/12*fricMod,0); 	
			}
		}
	}

	@Override
	public void draw(PApplet g) {
		super.draw(g);
		g.pushStyle();
		g.noFill();
		g.rect((float)x, (float)y, (float)PLAYER_WIDTH, (float)PLAYER_HEIGHT);
		g.popStyle();


	}

	//---------------------------------RPG Elements------------------------------------------


	public double damaged(double damageTaken) {
		// TODO Auto-generated method stub
		double damage = damageTaken*(10/(10+defStat));

		currentHP-=damage;

		return damage;

	}


	public void regen() {
		// TODO Auto-generated method stub
		if(baseHP>currentHP)
		{
			currentHP+=0.1;
		}

	}


	public double getHP() {
		// TODO Auto-generated method stub
		return currentHP;
	}



	public double energyDepletion(double usedEP) {
		// TODO Auto-generated method stub

		currentEP-=usedEP;
		return usedEP;
	}



	public boolean energyReplenish() {
		// TODO Auto-generated method stub
		while(currentEP<baseEP)
		{
			replenishing = true;
			currentEP+=0.2;
		}

		return replenishing;
	}



	public double getEP() {
		// TODO Auto-generated method stub
		return currentEP;
	}


	public void useTechOne(Enemies e) 
	{
		double epCost = 5;

		if(e.intersects(this))
		{
			e.damaged(10+attackStat/2);
			e.stunned();
		}

	}


	public void useTechTwo(Enemies e) 
	{
		double epCost = 30;
		if(e.intersects(this))
		{
			e.damaged(10+attackStat/2);
			e.knockedBack(100, 600);;
		}

	}


	public void useTechThree(Enemies e) 
	{
		double epCost = 15;
		if(e.intersects(this))
		{
			e.damaged(10+attackStat/2);
		}

	}


	public void useTechFour(Enemies e) 
	{
		double epCost = 20;

	}



}
