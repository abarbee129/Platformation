package entities;

import java.awt.Shape;

import javax.management.timer.Timer;

import processing.core.PApplet;
import processing.core.PImage;

public class Enemy extends Player implements Damageable {

	
	private double HP;
	private double currentEP;
	private double baseEP;
	private boolean replenishing;

	private double level;
	private double attackStat;
	private double defStat;
	private double EXP;
	private int stunTicks = 0;
	private int c = 0;


	public Enemy(PImage img, int x, int y, double level, double statPoints) {
		super(img, x, y,0.5);
		// TODO Auto-generated constructor stub
		replenishing = false;
		HPCalculation(statPoints);
		defCalculation(statPoints);
		attackCalculation(statPoints);
		EPCalculation(statPoints);
		this.level = level;
		
		EXP = level*15;
	}
	
	
	
	private void HPCalculation(double statPoints)
	{
		HP = statPoints + (level*5);
		
	}
	
	
	
	private void EPCalculation(double statPoints)
	{
		baseEP = statPoints + (level*20);
		currentEP = baseEP;
	}
	
	
	
	
	private void defCalculation(double statPoints)
	{
		defStat = statPoints + (level*3);
	}
	
	
	
	private void attackCalculation(double statPoints)
	{
		 attackStat = statPoints + (level*10);
		
	}
	

	public void knockedBack(double dx, double dy)
	{
		super.accelerate(dx, dy); 
	}
	

	public void stunned()
	{	
		if(stunTicks > 0) {
			stunTicks--;
			if(c%2 == 0) {
				this.moveByAmount(1, 1);
			}
			
			else{
				this.moveByAmount(-1, -1);
			}
			
		}
		else
		{
			stunTicks = 5;
		}
	
			
	}
	
	public void attack(Player p)
	{
		p.damaged(attackStat);
		System.out.println(p.getHP());
	}

	@Override
	public double damaged(double damageTaken) {
		// TODO Auto-generated method stub
		
		double damage = damageTaken*(10/(10+defStat));
		
		HP-=damage;
		
		if(HP<=0) {
			disapear();
		}
		
		return damage;
	}

	
	
	@Override
	public void regen() {
		// TODO Auto-generated method stub
	}

	
	
	@Override
	public double getHP() {
		// TODO Auto-generated method stub
		return HP;
	}

	
	
	@Override
	public double energyDepletion(double usedEP) {
		// TODO Auto-generated method stub
		
		currentEP-=usedEP;
		
		return usedEP;
	}

	
	
	
	@Override
	public boolean energyReplenish() {
		// TODO Auto-generated method stub
		
	
			while(currentEP<baseEP)
			{
				replenishing = true;
				currentEP+=0.1;
			}
			return replenishing;
		
	}
	
	

	@Override
	public double getEP() {
		// TODO Auto-generated method stub
		return currentEP;
	}
	
	
	
	public boolean ifReplenishing()
	{
		return replenishing;
	}

	public void draw(PApplet g)
	{
		super.draw(g);
	}

	public void action(Player player, Shape shape) {
		// TODO Auto-generated method stub
		//walk(0);
	}
	
	public void disapear()
	{
		
	}



}
