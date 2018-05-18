package entities;

import java.awt.geom.Rectangle2D;

import processing.core.PImage;

public class MeleeEnemy extends Enemies {
	
	private Rectangle2D hitBox;
	
	public MeleeEnemy( PImage img ,int lvl, int statPoint)
	{
		super(img, 85, 85, lvl, statPoint);
		
		
	}
	
	
	
	

}
