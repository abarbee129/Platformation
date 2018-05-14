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

	
	public Player(PImage img, int x, int y) {
		super(img, x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
	}
	
	public void walk(int dir) {
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
		// This is a simple acceleate method that adds dx and dy to the current velocity.
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
					super.moveByAmount(0, (ydif-1) + (PLAYER_WIDTH/2 + r.height/2));
					accelerate(0,-dy);
					isTouchingGround = true;
				}
				else if(ydif > 0) {
					// player is "below" the center of the platform	
					super.moveByAmount(0, ydif);
				}
				if(xdif > 0) {
					// player is "right" of the center of the platform	
					super.moveByAmount(r.getWidth()/2 - xdif, 0);
				}
				else if(xdif < 0) {
					// player is "left" of the center of the platform	
					super.moveByAmount(-(r.getWidth()/2 + xdif), 0);
				}
				
				
			}
			
		}
		
		if(!isTouchingGround) {
			accelerate(0, 1440*dt);
		}
		move();
		
		
		
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

	public double damaged(double damageTaken) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void regen() {
		// TODO Auto-generated method stub
		
	}

	public double getHP() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double energyDepletion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean energyReplenish() {
		// TODO Auto-generated method stub
		return false;
	}

	public double getEP() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	
}
