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


	public Player(PImage img, int x, int y) {
		super(img, x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
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

	public void move() {
		double xchange = dt * (oldDx + ((ddx/2) * dt));
		double ychange = dt * (oldDy + ((ddy/2) * dt));
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
				double xdif = thisCX - (r.x+r.width/2);
				double ydif = thisCY - (r.y+r.height/2);
				if(ydif < 0) {
					// player is "above" the center of the platform
					accelerate(0,-dy);
					super.moveByAmount(0, -(ydif+1)-(PLAYER_HEIGHT/2 + r.height/2));

					isTouchingGround = true;
				}
				else if(ydif > 0) {
					// player is "below" the center of the platform	
					super.moveByAmount(0, ydif);
				}
				else if(xdif > 0) {
					// player is "right" of the center of the platform	
					super.moveByAmount(r.getWidth()/2 - xdif, 0);
				}
				else if(xdif < 0) {
					// player is "left" of the center of the platform	
					super.moveByAmount(-(r.getWidth()/2 + xdif), 0);
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



	}
	
	public boolean doesCollideWith(double rx, double ry, int width, int height) {

		if (x > rx - width/2 - this.width/2 && x < rx + width/2 + this.width / 2) {

			if (y > ry - height/2 - this.height/2 && y < ry + height/2 + this.height / 2) {
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
		g.fill(255,0,0);
		g.rect((float)x+PLAYER_WIDTH/2, (float)y+PLAYER_HEIGHT/2, (float)5, (float)5);
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
