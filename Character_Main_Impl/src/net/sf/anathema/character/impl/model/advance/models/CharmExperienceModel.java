package net.sf.anathema.character.impl.model.advance.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharm;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharmConfiguration;
import net.sf.anathema.character.impl.model.advance.IPointCostCalculator;
import net.sf.anathema.character.model.ICharacterStatistics;
import net.sf.anathema.character.model.charm.ICharmConfiguration;
import net.sf.anathema.character.model.charm.special.ISubeffectCharmConfiguration;
import net.sf.anathema.character.model.charm.special.IUpgradableCharmConfiguration;
import net.sf.anathema.character.model.traits.ICoreTraitConfiguration;

public class CharmExperienceModel extends AbstractIntegerValueModel {
  private final ICoreTraitConfiguration traitConfiguration;
  private final IPointCostCalculator calculator;
  private final ICharacterStatistics statistics;
  private final IBasicCharacterData basicCharacter;
  private List<ICharm> comboPicks = new ArrayList<ICharm>();

  public CharmExperienceModel(
      ICoreTraitConfiguration traitConfiguration,
      IPointCostCalculator calculator,
      ICharacterStatistics statistics,
      IBasicCharacterData basicCharacter) {
    super("Experience", "Charms"); //$NON-NLS-1$//$NON-NLS-2$
    this.traitConfiguration = traitConfiguration;
    this.calculator = calculator;
    this.statistics = statistics;
    this.basicCharacter = basicCharacter;
  }

  public Integer getValue() {
    return getCharmCosts();
  }

  private int getCharmCosts() {
    int experienceCosts = 0;
    ICharmConfiguration charmConfiguration = statistics.getCharms();
    Set<ICharm> charmsCalculated = new HashSet<ICharm>();
    for (ICharm charm : charmConfiguration.getLearnedCharms(true)) {
      int charmCosts = calculateCharmCost(charmConfiguration, charm, charmsCalculated, true);
      if (charmConfiguration.isAlienCharm(charm)) {
        charmCosts *= 2;
      }
      experienceCosts += charmCosts;
      charmsCalculated.add(charm);
    }
    ICharm[] comboXPPicks = statistics.getCombos().getExperiencedCharmPicks();
    comboPicks.addAll(Arrays.asList(comboXPPicks));
    for (ICharm charm : comboXPPicks) {
        int charmCosts = calculateCharmCost(charmConfiguration, charm, charmsCalculated, false);
        if (charmConfiguration.isAlienCharm(charm)) {
          charmCosts *= 2;
        }
        experienceCosts += charmCosts;
        charmsCalculated.add(charm);
      }
    for (ISpecialCharm specialCharm : charmConfiguration.getSpecialCharms())
    	if (specialCharm instanceof IUpgradableCharm)
    	{
    		ICharm charm = charmConfiguration.getCharmIdMap().getCharmById(specialCharm.getCharmId());
    		if (!charmsCalculated.contains(charm))
    			experienceCosts += calculateCharmCost(charmConfiguration, charm, charmsCalculated, true);
    	}
    			
    return experienceCosts;
  }

  private int calculateCharmCost(ICharmConfiguration charmConfiguration,
		  ICharm charm,
		  Set<ICharm> charmsCalculated,
		  boolean requireLearning) {
    ISpecialCharmConfiguration specialCharm = charmConfiguration.getSpecialCharmConfiguration(charm);
    int charmCost = calculator.getCharmCosts(
        charm,
        basicCharacter,
        traitConfiguration,
        statistics.getCharacterTemplate().getMagicTemplate().getFavoringTraitType());
    if (specialCharm != null) {
      int timesLearnedWithExperience = requireLearning ? (specialCharm.getCurrentLearnCount() - specialCharm.getCreationLearnCount())
    		  : 1;
      final int specialCharmCost = timesLearnedWithExperience * charmCost;
      if (specialCharm instanceof IUpgradableCharmConfiguration)
      {
    	  if (comboPicks.contains(charm))
    	  {
    		  comboPicks.remove(charm);
    		  return charmCost;
    	  }
    	  IUpgradableCharmConfiguration config = (IUpgradableCharmConfiguration)specialCharm;
    	  return (charmConfiguration.getGroup(charm).isLearned(charm) || !requireLearning ? charmCost * config.getExperiencedCharmCount() : 0) +
    	  		+ config.getUpgradeXPCost();
      }
      if (!(specialCharm instanceof ISubeffectCharmConfiguration)) {
        return specialCharmCost;
      }
      final ISubeffectCharmConfiguration subeffectCharmConfiguration = (ISubeffectCharmConfiguration) specialCharm;
      final int count = Math.max(
          0,
          (subeffectCharmConfiguration.getExperienceLearnedSubeffectCount() - (subeffectCharmConfiguration.getCreationLearnedSubeffectCount() == 0
              ? 1
              : 0)));
      int subeffectCost = (int) Math.ceil(count * subeffectCharmConfiguration.getPointCostPerEffect() * 2);
      return subeffectCost + specialCharmCost;
    }
    else if (charmConfiguration.getGroup(charm).isLearned(charm, true) || !requireLearning) {
      for (ICharm mergedCharm : charm.getMergedCharms()) {
        if (charmsCalculated.contains(mergedCharm) && !isSpecialCharm(charm)) {
          return 0;
        }
      }
      return charmCost;
    }
    return 0;
  }
  
  private boolean isSpecialCharm(ICharm charm)
  {
	  return statistics.getCharms().getSpecialCharmConfiguration(charm) != null;
  }
}