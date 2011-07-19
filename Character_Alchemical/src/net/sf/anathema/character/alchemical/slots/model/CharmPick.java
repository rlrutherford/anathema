package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IGenericCombo;

public class CharmPick
{
	private ICharm charm;
	private IGenericCombo combo;
	
	public CharmPick(ICharm charm, IGenericCombo combo)
	{
		this.charm = charm;
		this.combo = combo;
	}
	
	public ICharm getCharm()
	{
		return charm;
	}
	
	public IGenericCombo getCombo()
	{
		return combo;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof CharmPick)
		return this.charm == ((CharmPick)obj).charm &&
			   this.combo == ((CharmPick)obj).combo;
		return false;
	}
}
