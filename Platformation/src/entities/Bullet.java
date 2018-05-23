package entities;

import processing.core.PApplet;

public class Bullet {

	private double x,y;
	private double direction;
	private double dwidth, dheight;
	private boolean isDead;
	private double damage;
	
	public Bullet(double x, double y, double direction, double damage) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.damage = damage;
		dwidth = 8;
		dheight = 3;
	}
	
	
	public boolean getIsDead() {
		return isDead;
	}
	public void setIsDead(boolean dead) {
		isDead = dead;
	}
	
	
	public double getx() {
		return y;
	}
	public double gety() {
		return x;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public void draw(PApplet g) {
		g.pushStyle();
		g.fill(255,0,0);
		g.rect((float)x, (float)y, (float)dwidth, (float)dheight);
		g.popStyle();
	}
	public void move() {
		x += direction;
	}
	
	
	
}
