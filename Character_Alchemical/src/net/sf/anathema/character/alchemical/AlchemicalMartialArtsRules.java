package net.sf.anathema.character.alchemical;

import net.sf.anathema.character.generic.impl.magic.MartialArtsUtilities;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.MartialArtsLevel;
import net.sf.anathema.character.generic.template.magic.IGenericCharmConfiguration;
import net.sf.anathema.character.generic.template.magic.IMartialArtsRules;

public class AlchemicalMartialArtsRules implements IMartialArtsRules
{
  private static final String PERFECTED_LOTUS_MATRIX = "Alchemical.PerfectedLotusMatrix";
	
  public void setHighLevelAtCreation(boolean highLevelAtCreation)
  {
  }

  public MartialArtsLevel getStandardLevel() {
    return MartialArtsLevel.Celestial;
  }

  public boolean isCharmAllowed(
      ICharm martialArtsCharm,
      IGenericCharmConfiguration charmConfiguration,
      boolean isExperienced) {
	  MartialArtsLevel level = MartialArtsUtilities.getLevel(martialArtsCharm);
	  if (level == MartialArtsLevel.Sidereal)
		  return false;
	  for (ICharm charm : charmConfiguration.getLearnedCharms())
		  if (charm.getId().equals(PERFECTED_LOTUS_MATRIX))
			  return true;
	  return false;
  }
}