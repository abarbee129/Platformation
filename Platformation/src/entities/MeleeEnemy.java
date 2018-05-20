package entities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import processing.core.PImage;
import worldGeometry.Platform;

public class MeleeEnemy extends Enemies {
	
	private Rectangle2D sightBox;
	private boolean moved;
	public MeleeEnemy( PImage img ,int x, int y, int lvl, int statPoint)
	{
		super(img, x, y, lvl, statPoint);
		
		sightBox = new Rectangle(x-100,y, x+100, PLAYER_HEIGHT);
		
	}
	
	public void actions(Player p, Shape plat)
	{
		if(sightBox.contains(p) )
		{
			this.moveToPlayer(p);
		}
		else
		{
			this.idleWalk(plat);
		}
	}
	
	private void moveToPlayer(Player p)
	{
		if(p.getCenterX()+2 < this.getCenterX())
		{
			this.walk(-1);
		}
		else if(p.getCenterX()+p.width+2 > this.getCenterX())
		{
			this.walk(1);
		}
		else 
		{
			this.walk(0);
		}
	}
	
	private void idleWalk(Shape plat)
	{
		
		Rectangle platform = plat.getBounds();
		
		double xPlat = platform.getX();
		double width = platform.getWidth();
		
		
		if(this.getX() > xPlat+4 && moved==false)
		{
			walk(1);
			
		}
		else if(this.getX() < width-4 && moved == true)
		{
			walk(-1);
		}
		else if(this.getX() <= xPlat)
		{
			moved = true;
		}
		else if(this.getX() >= width)
		{
			moved = false;
		}
		
		
	}
	
	
	
	

}
