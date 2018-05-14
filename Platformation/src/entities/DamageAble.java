package entities;

public interface DamageAble {
	

	
	
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
	 * The method returns back the current HP of the DamageAble object 
	 * @return Hit Point
	 */
	public double getHP(); 
	
	
	/**
	 * This method calculates the energy points reduced when techniques are used
	 *  @return Energy Points lost
	 */
	public double energyDepletion();
	
	
	
	/**
	 * This method replenished energy points if the energy of the object is depleted
	 */
	public void energyReplenish();
	
	/**
	 * The method returns back the current Energy of the DamageAble object 
	 * @return EP
	 */
	public double getEP(); 
	
}
