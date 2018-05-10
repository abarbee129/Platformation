package entities;

public interface DamageAble {
	
	double HP = 0.0;
	double EP = 0.0;
	
	/**
	 * This method calculates the hit points reduced when the damage is taken
	 *  @return Hit Points lost
	 */
	public double damaged(double damageTaken);
	
	
	/**
	 * This method replenished hit points when depleted but only when player is outside of combat
	 *  
	 */
	public void regen();
	
	/**
	 * This method calculates the energy points reduced when techniques are used
	 *  @return Energy Points lost
	 */
	public double energyDepletion();
	
	
	
	/**
	 * This method replenished energy points when depleted
	 */
	public void energyReplenish();
	
	
}
