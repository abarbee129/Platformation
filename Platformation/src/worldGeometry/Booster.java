package worldGeometry;

import java.awt.Color;
import java.awt.Graphics;

public class Booster {
	
	private double x = 500;
	private double y = 600;
	
	private int width = 40;
	private int height = 40;
	
	private int power = 10;
	private double strength = 800;
	
	public Booster(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect((int)x-width/2, (int)y-height/2, width, height);
	}
	
	public double getXBounceAcceleration(double px, double py) {
		
		double distance = Math.sqrt(Math.pow((px-x),2) + Math.pow((py-y), 2));
		double ratio = Math.atan((py - y) / (px - x));	
		if (px - x < 0) {
			return -1 * power/(power + distance) * strength * Math.cos(ratio);
		}
		else {
		return power/(power + distance) * strength * Math.cos(ratio);
		}
	}
	public double getYBounceAcceleration(double px, double py) {
		
		double distance = Math.sqrt(Math.pow((px-x),2) + Math.pow((py-y), 2));
		double ratio = Math.atan((py - y) / (px - x));	
		if (px - x < 0) {
			return -1 * power/(power + distance) * strength * Math.sin(ratio);
		}
		else {
		return power/(power + distance) * strength * Math.sin(ratio);
		}
	}
	
	public double[] getCBoxDimensions() {
		double[] dims = {x,y,width,height};
		return dims;
	}
	
	
	
}
