package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.generic.magic.ICharm;

public class CharmSlot
{
	private ICharm charm;
	private final boolean isGeneric;
	private final boolean isCreationLearned;
	
	public CharmSlot(ICharm charm, boolean isGeneric, boolean isCreationLearned)
	{
		this.isGeneric = isGeneric;
		this.isCreationLearned = isCreationLearned;
	}
	
	public ICharm getCharm()
	{
		return charm;
	}
	
	public boolean setCharm(ICharm charm)
	{
		if (this.charm == charm) return false;
		this.charm = charm;
		return true;
	}
	
	public boolean isGeneric()
	{
		return isGeneric;
	}
	
	public boolean isCreationLearned()
	{
		return isCreationLearned;
	}
	 
}
