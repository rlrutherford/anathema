package net.sf.anathema.character.model.charm.special;

import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;

public interface IUpgradableCharmConfiguration extends IMultipleEffectCharmConfiguration
{
	public int getUpgradeBPCost();
	
	public int getUpgradeXPCost();
	
	public int getCreationCharmCount();
	
	public int getExperiencedCharmCount();
	
	public boolean isComplex();
	
	public ISubeffect[] getCharmEffects();
}
