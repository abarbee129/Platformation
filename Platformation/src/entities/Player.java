package entities;

import java.awt.Shape;
import java.util.ArrayList;

import processing.core.PImage;
import worldGeometry.Platform;

public class Player extends Sprite{

	public static final int MARIO_WIDTH = 40;
	public static final int MARIO_HEIGHT = 60;
	
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
		super(img, x, y, MARIO_WIDTH, MARIO_HEIGHT);
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
		
		
		
		accelerate(0, 1440*dt);
		move();
		
		
		
	}
	
	
	
	
}
