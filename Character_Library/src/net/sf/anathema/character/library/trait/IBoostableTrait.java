package net.sf.anathema.character.library.trait;

public interface IBoostableTrait
{
	public void applyBoost(int modifier);
	
	public int getUnboostedValue();
}
