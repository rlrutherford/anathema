package net.sf.anathema.character.generic.impl.magic.charm.special;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.ICharmLearnableArbitrator;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharmVisitor;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;
import net.sf.anathema.character.generic.magic.charms.special.IUpgradableCharm;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.lib.gui.wizard.workflow.ICondition;

public class UpgradableCharm extends MultipleEffectCharm implements IUpgradableCharm
{
	private final int NO_BP_UPGRADE = -1;
	private final int NO_XP_UPGRADE = -1;
	
	List<Upgrade> upgradeList = new ArrayList<Upgrade>();
	private ISubeffect[] charms;
	private final Map<String, Integer> bpCosts;
	private final Map<String, Integer> xpCosts;
	private final Map<String, Integer> essenceMins;
	private final Map<String, Integer> traitMins;
	private final Map<String, ITraitType> traits;
	private final boolean requiresBase;
	private final boolean isComplex;
	
	public UpgradableCharm(String charmId, boolean complex, String[] effectIds, boolean requiresBase,
			Map<String, Integer> bpCosts, Map<String, Integer> xpCosts,
			Map<String, Integer> essenceMins, Map<String, Integer> traitMins,
			Map<String, ITraitType> traits)
	{
		super(charmId, effectIds);
		this.bpCosts = bpCosts;
		this.xpCosts = xpCosts;
		this.essenceMins = essenceMins;
		this.traitMins = traitMins;
		this.traits = traits;
		this.requiresBase = requiresBase;
		this.isComplex = complex;
	}
	
	  public void accept(ISpecialCharmVisitor visitor) {
		    visitor.visitUpgradableCharm(this);
		  }
	  
	  public ISubeffect[] buildSubeffects(IBasicCharacterData data,
			  IGenericTraitCollection traitCollection,
			  ICharmLearnableArbitrator arbitrator,
			  ICharm charm) {
		upgradeList.clear();
		List<ISubeffect> charmList = new ArrayList<ISubeffect>();
	    for (String id : effectIds) {
	      Integer bpCost = bpCosts.get(id);
	      Integer xpCost = xpCosts.get(id);
	      Integer essenceMin = essenceMins.get(id);
	      Integer traitMin = traitMins.get(id);
	      ITraitType trait = traits.get(id);
	      Upgrade upgrade = new Upgrade(id, charm, data,
	    		  buildLearnCondition(arbitrator, data, traitCollection,
	    				  charm, bpCost != null, bpCost == null && xpCost == null,
	    				  essenceMin, traitMin, trait),
	    		  bpCost == null ? NO_BP_UPGRADE : bpCost,
	    		  xpCost == null ? NO_XP_UPGRADE : xpCost);
	      upgradeList.add(upgrade);
	      if (upgrade.isCharm())
	    	  charmList.add(upgrade);
	    }
	    charms = charmList.toArray(new ISubeffect[0]);
	    return upgradeList.toArray(new ISubeffect[upgradeList.size()]);
	  }

	  private ICondition buildLearnCondition(final ICharmLearnableArbitrator arbitrator,
			  final IBasicCharacterData data,
			  final IGenericTraitCollection traitCollection,
			  final ICharm charm,
			  final boolean bpUpgradeAllowed,
			  final boolean isCharm,
			  final Integer essenceMin,
			  final Integer traitMin,
			  final ITraitType trait) {
	    return new ICondition() {
	      public boolean isFulfilled() {
	        boolean learnable = arbitrator.isLearnable(charm) &&
	        	(isCharm || (bpUpgradeAllowed || data.isExperienced()));
	        learnable = !learnable ? learnable :
	        	(essenceMin == null ||
	        	 traitCollection.getTrait(OtherTraitType.Essence).getCurrentValue() >= essenceMin);
	        learnable = !learnable ? learnable :
	        	(traitMin == null || trait == null ||
	        	 traitCollection.getTrait(trait).getCurrentValue() >= essenceMin);
	        return learnable;
	        	 
	      }
	    };
	  }
	  
	  public int getUpgradeBPCost()
	  {
		  int total = 0;
		  for (Upgrade upgrade : upgradeList)
			  total += upgrade.isCreationLearned() && !upgrade.isCharm() ? upgrade.getBPCost() : 0;
		  return total;
	  }
	  
	  public int getUpgradeXPCost()
	  {
		  int total = 0;
		  for (Upgrade upgrade : upgradeList)
			  total += upgrade.isLearned() && !upgrade.isCreationLearned() &&
			  		   !upgrade.isCharm() ? upgrade.getXPCost() : 0;
		  return total;
	  }
	  
	  public int getCreationCharmCount()
	  {
		  int total = 0;
		  for (Upgrade upgrade : upgradeList)
			  total += upgrade.isCharm() && upgrade.isCreationLearned() ?
					  1 : 0;
		  return total;
	  }
	  
	  public int getExperiencedCharmCount()
	  {
		  int total = 0;
		  for (Upgrade upgrade : upgradeList)
			  total += upgrade.isCharm() && upgrade.isLearned() && !upgrade.isCreationLearned() ?
					  1 : 0;
		  return total;
	  }
	  
	  public void learnFirst()
	  {
		  for (Upgrade upgrade : upgradeList)
			  if (upgrade.isCharm())
			  {
				  upgrade.setLearned(true);
				  return;
			  }
	  }
	  
	  public void forgetCharms()
	  {
		  for (Upgrade upgrade : upgradeList)
			  if (upgrade.isCharm())
				  upgrade.setLearned(false);
	  }
	  
	  public boolean requiresBase()
	  {
		  return requiresBase;
	  }
	  
	  public boolean isComplex()
	  {
		  return isComplex;
	  }
	  
	  public ISubeffect[] getCharmEffects()
	  {
		  return charms;
	  }
	  
	  private class Upgrade extends Subeffect
	  {
		  private int bpCost;
		  private int xpCost;
		  
		public Upgrade(String subeffectId, ICharm charm, IBasicCharacterData data,
				ICondition learnable, int bpCost, int xpCost) {
			super(subeffectId, charm, data, learnable);
			this.bpCost = bpCost;
			this.xpCost = xpCost;
		}
		
		public int getBPCost()
		{
			return bpCost;
		}
		
		public int getXPCost()
		{
			return xpCost;
		}
		
		public boolean isCharm()
		{
			return bpCost == NO_BP_UPGRADE && xpCost == NO_XP_UPGRADE;
		}
		  
	  }
}
