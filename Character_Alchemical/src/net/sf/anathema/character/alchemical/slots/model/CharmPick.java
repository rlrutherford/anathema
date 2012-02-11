package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IGenericCombo;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;

public class CharmPick
{
	private ICharm charm;
	private IGenericCombo combo;
	private ISubeffect subeffect;
	
	public CharmPick(ICharm charm, IGenericCombo combo, ISubeffect effect)
	{
		this.charm = charm;
		this.combo = combo;
		this.subeffect = effect;
	}
	
	public ICharm getCharm()
	{
		return charm;
	}
	
	public ISubeffect getSubeffect()
	{
		return subeffect;
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
			   this.combo == ((CharmPick)obj).combo &&
			   this.subeffect == ((CharmPick)obj).subeffect;
		return false;
	}
}
