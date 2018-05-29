package entities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;


// add implements damageable once it exists
public class Player extends Sprite implements Damageable{

	public static final int PLAYER_WIDTH = 40;
	public static final int PLAYER_HEIGHT = 60;

	//
	private double baseHP;
	private double currentHP;
	private double techOne;
	private double techTwo;

	private double currentEP;
	private double baseEP;
	private boolean replenishing;
	private boolean shield;
	private boolean regen;
	private double level;
	private int skillPoints;
	private double attackStat;
	private double defStat;
	private double EXP;
	private boolean isAttacking;
	
	private double[] ox,oy;


	public boolean isFlipped;
	// Collision/Physics fields
	private double dx;
	private double dy;
	private double oldDx;
	private double oldDy;
	private double dt = 1 / 60.0;
	private double ddx;
	private double ddy;

	private boolean isTouchingGround;
	private boolean isMoving;
	private boolean isDashing;
	private boolean hasJumped;
	private double ticksFromZeroToHalf = 4.0;
	private double ticksFromHalfToFull = 8.0;
	private double ticksToStop = 1.0;	
	private int maxDx = 400;
	private int maxDy = 480;
	private double fricMod = 0.5;
	private double slow = 1;
	
	private int t1CD = 180;
	private int t2CD = 90;
	private int[] cooldowns;
	
	
	protected boolean isEnemy = false;

	private ArrayList<PImage> pics;  


	public Player(PImage img, int x, int y, PApplet marker) {
		super(img, x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		ox = new double[3];
		oy = new double[3]; 
		cooldowns = new int[4];
		isFlipped = false;

		//pics.add(marker.loadImage("Player.png"));
		//pics.add(marker.loadImage("PlayerAttack.png"));
		//pics.add(marker.loadImage("PlayerShield.png"));
		//pics.add(marker.loadImage("PlayerFlipped.png"));
		//pics.add(marker.loadImage("PlayerAttackFlipped.png"));
		//pics.add(marker.loadImage("PlayerShieldFlipped.png"));





		level = 1;
		shield = false;
		baseHP = 100;
		currentHP = 100;
		baseEP = 200;
		currentEP = 200;
		attackStat = 10;
		defStat =  10;
		EXP = 0; 

		techOne= 10;
		techTwo = 20;
	}

	public Player(PImage img, int x, int y,double slow, PApplet marker) {
		super(img, x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		this.slow = slow;
		ox = new double[3];
		oy = new double[3];
		cooldowns = new int[4];
		level = 1;
		shield = false;
		baseHP = 100;
		currentHP = 100;
		baseEP = 200;
		currentEP = 200;
		attackStat = 10;
		defStat =  10;
		EXP = 0; 
		
	}

	public void walk(int dir) {
		isMoving = true;
		double accelAmt = 0;
		if(dir>0)
		{
			isFlipped = false;
		}
		else if(dir<0)
		{
			isFlipped = true;
		}	

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
		if (accelAmt + dx > maxDx ) {
			if(dx < maxDx) {
				accelAmt = maxDx - dx;
			}
			
			else if(dir*dx > 0) {
				accelAmt = 0;
			}
			else if(dir*dx < 0) {
				accelAmt = (maxDx - dx)/2;
			}
		}
		else if(accelAmt + dx < -maxDx) {
			if(dx > -maxDx) {
				accelAmt = -maxDx - dx;
			}
			else if(dir*dx > 0) {
				accelAmt = 0;
			}
			else if(dir*dx < 0) {
				accelAmt = (-maxDx - dx)/2;
			}
		}
		
		accelerate(accelAmt, 0); 
	}

	public void jump() {
		
		
		
		
		if(isTouchingGround || !hasJumped){
			if(dy > -maxDy) {
				if(isTouchingGround) {
					accelerate(0, -600);
					isTouchingGround = false;
				}
				else if(!hasJumped&&!isEnemy){
						if(dy>0) {
							accelerate(0,-dy);
						}
						accelerate(0, -600);
						hasJumped = true;
				}
			}
			
			
		}
	}

	public boolean getCanJump() {
		return isTouchingGround || !hasJumped;
	}
	public boolean getOnGround() {
		return isTouchingGround;
	}
	
	public void accelerate (double dx, double dy) {
		// This is a simple accelerate method that adds dx and dy to the current velocity.
		this.dx += dx;
		this.dy += dy;

		ddx += dx;
		ddy += dy;
	}

	public double getDx() {
		return dx;
	}
	public double getDy() {
		return dy;
	}


	public void move() {
		double xchange = slow*dt * (oldDx + ((ddx/2) * dt));
		double ychange = slow*dt * (oldDy + ((ddy/2) * dt));
		super.moveByAmount(xchange, ychange);
		oldDx = this.dx;
		oldDy = this.dy;
		ddx = 0;
		ddy = 0;
	}
	public void act(ArrayList<Shape> shapes) {
		
		for(int c = 0; c < cooldowns.length; c++) {
			if(cooldowns[c] > 0) {
				cooldowns[c]--;
			}
		}
		isDashing = cooldowns[0] >= t1CD-10;
	
		
		energyReplenish();
		regen();
		
		
		if(!isTouchingGround&&!isDashing) {
			accelerate(0, 1440*dt);
		}
		isTouchingGround = false;
		for(Shape s : shapes) {
			Rectangle r = s.getBounds();
			if(intersects(r)) {
				double thisCX = x + PLAYER_WIDTH/2;
				double thisCY = y + PLAYER_HEIGHT/2;
				double oldCx = ox[1] + PLAYER_WIDTH/2;
				double oldCy = oy[1] + PLAYER_HEIGHT/2;
				double oxdif = oldCx - (r.x+r.width/2);
				double oydif = oldCy - (r.y+r.height/2);
				double xdif = thisCX - (r.x+r.width/2);
				double ydif = thisCY - (r.y+r.height/2);
				if(ydif < 0 && oydif > ydif) {
					// player is "above" the center of the platform
					super.moveByAmount(0, ydif);

				}
				else if(ydif < 0) {
					// player is "above" the center of the platform
					accelerate(0,-dy);
					super.moveByAmount(0, -(ydif+1)-(PLAYER_HEIGHT/2 + r.height/2));

					isTouchingGround = true;
				}
				else if(ydif > 0 && oydif > ydif) {
					// player is "below" the center of the platform	
					super.moveByAmount(0, r.getHeight()/2-ydif);
				}
				else if(xdif > 0 && oxdif > xdif) {
					// player is "right" of the center of the platform	
					super.moveByAmount(r.getWidth() - xdif, 0);
				}
				else if(xdif < 0 && oxdif < xdif) {
					// player is "left" of the center of the platform	
					super.moveByAmount(-(r.getWidth() + xdif), 0);
				}


			}
			else {
				double thisCX = x + PLAYER_WIDTH/2;
				double thisCY = y + PLAYER_HEIGHT/2;
				double xdif = thisCX - (r.x+r.width/2);
				double ydif = thisCY - (r.y+r.height/2);
				if(Math.abs(-ydif - (PLAYER_HEIGHT/2 + r.height/2))<1) {
					if(Math.abs(xdif)<1 + (PLAYER_WIDTH/2 + r.width/2)) {
						isTouchingGround = true;
					}
				}
				else {

				}
			}

		}
		if(isTouchingGround) {
			hasJumped = false;
		}
		
		
		applyFriction();
		move();
		isMoving = false;
		ox[2] = ox[1];
		oy[2] = oy[1];
		ox[1] = ox[0];
		oy[1] = oy[0];
		ox[0] = x;
		oy[0] = y;

	}

	public boolean doesCollideWith(double rx, double ry, int width, int height) {

		if (x > rx - this.width && x < rx + width ) {

			if (y > ry  - this.height && y < ry + height) {
				return true;
			}

			else {
				return false;
			}
		}

		else {
			return false;
		}

	}
	public double getx() {
		return x;
	}
	public double gety() {
		return y;
	}

	public void applyFriction() {
		
		if(dy > maxDy) {
			accelerate(0,-dy/20*fricMod); 	
		}
		else if(dy < -maxDy) {
			accelerate(0,-dy/20*fricMod);	
		}
		
		
		if (isMoving) {
			if (isTouchingGround) {
				accelerate(-dx/12*fricMod,0); 
			}
			else { 
				accelerate(-dx/12*fricMod,0); 
				
			}
		}
		else {
			if (isTouchingGround&&!isDashing) {
				accelerate(-dx/2*fricMod,0); 
			}
			else {
				accelerate(-dx/12*fricMod,0); 
				
			}
		}
	}

	@Override 
	public void draw(PApplet g) {
		super.draw(g);
		g.pushStyle();
		g.noFill();
		g.rect((float)x-PLAYER_WIDTH/4, (float)y-20, (float)3*PLAYER_WIDTH/2, (float)9);
		g.fill(255,0,0);
		g.rect((float)x-PLAYER_WIDTH/4, (float)y-20, (float)(3*PLAYER_WIDTH/2*currentHP/baseHP), (float)9);
		g.fill(0);
		g.textSize(10);
		g.text("HP: " + getHPPercent(), (float)(3+x-PLAYER_WIDTH/4), (float)y-12);
		g.textSize(10);
		g.text("Lv: " + (int)level, (float)(3+x-PLAYER_WIDTH/4), (float)y-22);
		
		if(skillPoints > 0) {
			g.textSize(10);
			g.fill(0);
			if(skillPoints==1) {
				g.text("You have " + skillPoints + " skillpoint to spend", (float)(x-12-PLAYER_WIDTH), (float)y-42);

			}
			else {
				g.text("You have " + skillPoints + " skillpoints to spend", (float)(x-12-PLAYER_WIDTH), (float)y-42);

			}
			
		}
		
		
		
		
		if(isAttacking)
		{
			g.noFill();
			g.stroke(234,23,23);
			g.rect((float)x, (float)y, (float)PLAYER_WIDTH, (float)PLAYER_HEIGHT);
		}
		
		if(shield)
		{
			g.noFill();
			g.stroke(0,0,220);
			g.ellipse((float)x, (float)y, (float)PLAYER_WIDTH, (float)PLAYER_HEIGHT);
		}
		
		if(!isEnemy) {
			g.noFill();
			g.rect((float)x-PLAYER_WIDTH/4, (float)y-9, (float)3*PLAYER_WIDTH/2, (float)9);
			g.fill(0,255,0);
			g.rect((float)x-PLAYER_WIDTH/4, (float)y-9, (float)(3*PLAYER_WIDTH/2*currentEP/baseEP), (float)9);
			g.fill(0);
			g.textSize(10);
			g.text("EP: " + currentEP, (float)(3+x-PLAYER_WIDTH/4), (float)y-2);
			
			
		}
	
		if(isEnemy && getHP()<=0)
		{
			g.fill(30);
			g.stroke(23,234,23);
			g.rect((float)x, (float)y, (float)PLAYER_WIDTH, (float)PLAYER_HEIGHT);
		}


		g.popStyle();


	}

	//---------------------------------RPG Elements------------------------------------------


	public double damaged(double damageTaken) {

		double damage = damageTaken*(10/(10+defStat));

		
		if(isGameOver() == false && !shield ) 
		{
			currentHP -= damage;
		}

		return damage;

	}


	public void regen() {
		if(baseHP>currentHP)
		{
			currentHP+=0.05;

		}

	}
	public void resetHP()
	{
		currentHP = baseHP;
	}
	
	public void resetEP()
	{
		currentEP = baseEP;
	}
	
	public double getHP() {
		return currentHP;
	}



	public double energyDepletion(double usedEP) {
		currentEP-=usedEP;
		return usedEP;
	}

	/**
	 * 
	 * @return the current HP in Percent
	 */
	public int getHPPercent()
	{
		int hp = (int)((currentHP/baseHP)*100);
		return hp;
	}

	public void setHP(double hp)
	{
		baseHP = hp;
		currentHP = baseHP;
	}	
	
	public void setEP(double ep)
	{
		baseEP = ep;
		currentEP = baseEP;
	}	
	
	public boolean energyReplenish() {
		if(currentEP<baseEP && !shield)
		{
			currentEP+=0.5;
		}
		if(currentEP <= 1) {
			replenishing  = true;
		}
		else if(replenishing && baseEP - currentEP <= 1){
			replenishing = false;
		}
		else {
			
		}
		return replenishing;

	}



	public double getEP() {
		return currentEP;
	}



	public void useTechOne(ArrayList<MeleeEnemy> meleeEnemies) 
	{
		double epCost = 100;


		if(currentEP>epCost && cooldowns[0] == 0 && !replenishing)
		{	
			cooldowns[0] = t1CD;
			shield = true;
			accelerate(0,-dy);
			if(isFlipped)
			{
				accelerate(-1400,0);
			}
			else
			{
				accelerate(1400,0);
			}
			energyDepletion(epCost);
			for(int i = 0; i<meleeEnemies.size(); i++)
			{	
				if(meleeEnemies.get(i).intersects(this))
				{
					meleeEnemies.get(i).damaged((techOne+attackStat/2));
					meleeEnemies.get(i).stunned();
					
				}
			}
		}
	}

	// this tech is a jump that knocks back enemies in the area.
	public void useTechTwo(ArrayList<MeleeEnemy> meleeEnemies) 
	{
		double epCost = 60;
		if(currentEP>epCost && cooldowns[1] == 0 && !replenishing) {
			cooldowns[1] = t2CD;
			jump();
			energyDepletion(epCost);
			for(int i = 0; i<meleeEnemies.size(); i++)
			{	if(meleeEnemies.get(i).intersects(this))
				{
					meleeEnemies.get(i).damaged(2*techTwo+attackStat/2);
					meleeEnemies.get(i).knockedBack(1000, -600, this);
				}
			}
		}
	}


	public void ranged(Enemy e) 
	{
		//double epCost = 5;
		if(e.intersects(this))
		{
			e.damaged(10+attackStat/2);
		}

	}



	public boolean isGameOver()
	{
		if(currentHP <= 0)
		{
			return true;
		}
		return false;
	}

	public void startShield()
	{
		if(currentEP>0 && !replenishing)
		{
			energyDepletion(2);
			shield = true;
		}
		else
		{
			endShield();
		}

	}

	public void endShield()
	{

		shield = false;
	}

	public boolean isShieldActive()
	{
		return shield;
	}

	public void obtainEXP(double exp) 
	{
		if(EXP<(50+(50*level)))
		{
			EXP += exp;
		}
		else
		{
			levelUP();
			EXP = 0;
		}
	}

	private void levelUP()
	{
		level+=1;
		skillPoints+=1;
		baseHP+=5;

		baseEP+=10;
		attackStat+=5;
		defStat+=3;

	}

	public void addPointsToOne()
	{
		if(skillPoints>0)
		{
			techOne+=5;
			skillPoints-=1;
		}
	}
	public void addPointsToTwo()
	{
		if(skillPoints>0)
		{
			techTwo+=5;
			skillPoints-=1;
		}
	}

	public void attack(ArrayList<MeleeEnemy> meleeEnemies) {
		for(int i = 0; i<meleeEnemies.size(); i++)
		{	
			if(meleeEnemies.get(i).intersects(this))
			{
				meleeEnemies.get(i).damaged(techTwo+attackStat/2);
			}
		}
		
		isAttacking = true;
	}
	
	public void stopAttack()
	{
		isAttacking = false;
	}
	
	public int getSkillPoints() {
		return skillPoints;
	}
}
