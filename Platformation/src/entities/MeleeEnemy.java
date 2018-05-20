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

		sightBox = new Rectangle(x-200,y-10,PLAYER_WIDTH+400, PLAYER_HEIGHT+10);

	}

	public void actions(Player p, Shape plat)
	{
		sightBox = new Rectangle((int)x-200,(int)y-30,(int)PLAYER_WIDTH+400, (int)PLAYER_HEIGHT+60);
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
		if(p.getx() < this.getx())
		{
			
			walk(-1);
			accelerate(-super.getDx()/4,0);
			
			
		}
		else if(p.getx() > this.getx())
		{
			walk(1);
			accelerate(-super.getDx()/4,0);
		}
		else 
		{
			//walk(0);
			
			
		}
	}

	private void idleWalk(Shape plat)
	{
		
		Rectangle platform = plat.getBounds();

		double xPlat = platform.getX();
		double width = platform.getWidth();


		if(this.getX() > xPlat+4 && moved==false)
		{
			//walk(1);
			
		}
		else if(this.getX() < width-4 && moved == true)
		{
			//walk(-1);
			
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
