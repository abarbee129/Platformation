package entities;

import java.awt.Rectangle;
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
	
	public void actions(Player p, Platform plat)
	{
		if(sightBox.contains(p))
		{
			moveToPlayer(p);
		}
		else
		{
			idleWalk(plat);
		}
	}
	
	public void moveToPlayer(Player p)
	{
		if(p.getCenterX()+2 < this.getCenterX())
		{
			this.walk(-1);
		}
		else if(p.getCenterX()+2 > this.getCenterX())
		{
			this.walk(1);
		}
	}
	
	public void idleWalk(Platform p)
	{
		double[] vars = p.getCBoxDimensions();
		
		double xPlat = vars[0];
		double width = vars[2];
		
		
		if(this.getX() > xPlat+4 && !moved)
		{
			walk(-1);
		}
		else if(this.getX() < width-4 && moved)
		{
			walk(1);
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
