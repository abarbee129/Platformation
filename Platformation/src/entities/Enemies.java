package entities;

import javax.management.timer.Timer;

import processing.core.PImage;

public class Enemies extends Sprite implements DamageAble {

	private double HP;
	private double currentEP;
	private double baseEP;
	private boolean replenishing;

	private double level;
	private double attackStat;
	private double defStat;
	private double EXP;


	public Enemies(PImage img, int x, int y, int w, int h, double level, double statPoints) {
		super(img, x, y, w, h);
		// TODO Auto-generated constructor stub
		replenishing = false;
		HPCalculation(statPoints);
		defCalculation(statPoints);
		attackCalculation(statPoints);
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
	
	public void stunned()
	{	
		this.moveByAmount(0, 0);
	}

	@Override
	public double damaged(double damageTaken) {
		// TODO Auto-generated method stub
		
		double damage = damageTaken*(10/(10+defStat));
		
		HP-=damage;
		
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
	public void energyReplenish() {
		// TODO Auto-generated method stub
		
	
			while(currentEP<baseEP)
			{
				replenishing = true;
				currentEP+=0.1;
			}
		
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

}
