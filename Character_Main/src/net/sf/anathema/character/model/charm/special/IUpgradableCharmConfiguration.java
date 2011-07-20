package net.sf.anathema.character.model.charm.special;

public interface IUpgradableCharmConfiguration extends IMultipleEffectCharmConfiguration
{
	public int getUpgradeBPCost();
	
	public int getUpgradeXPCost();
	
	public int getCreationCharmCount();
	
	public int getExperiencedCharmCount();
}
