package net.sf.anathema.character.impl.model.charm.special;

import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.ICharmLearnableArbitrator;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;
import net.sf.anathema.character.generic.magic.charms.special.IUpgradableCharm;
import net.sf.anathema.character.model.charm.special.IUpgradableCharmConfiguration;

public class UpgradableCharmConfiguration extends MultipleEffectCharmConfiguration implements IUpgradableCharmConfiguration {
  private final IUpgradableCharm upgrade;

  public UpgradableCharmConfiguration(ICharacterModelContext context,
                                      ICharm charm, IUpgradableCharm visited,
                                      ICharmLearnableArbitrator arbitrator) {
    super(context, charm, visited, arbitrator);
    upgrade = visited;
  }
	
	@Override
	public void learn(boolean experienced)
	{
		if (upgrade.isComplex() && upgrade.getCreationCharmCount() == 0
				&& upgrade.getExperiencedCharmCount() == 0)
			upgrade.learnFirst();
	}

	@Override
	public void forget()
	{
		upgrade.forgetCharms();
	}
	  
	public int getUpgradeBPCost()
	{
		return upgrade.getUpgradeBPCost();
	}
	
	public int getUpgradeXPCost()
	{
		return upgrade.getUpgradeXPCost();
	}
	
	public int getCreationCharmCount()
	{
		return upgrade.getCreationCharmCount();
	}
	
	public int getExperiencedCharmCount()
	{
		return upgrade.getExperiencedCharmCount();
	}
	
	public boolean forgetAtZero()
	  {
		  return upgrade.getCreationCharmCount() == 0 &&
		  		 upgrade.getExperiencedCharmCount() == 0;
	  }
	
	public boolean isComplex()
	{
		return upgrade.isComplex();
	}
	
	public ISubeffect[] getCharmEffects()
	{
		return upgrade.getCharmEffects();
	}
}
