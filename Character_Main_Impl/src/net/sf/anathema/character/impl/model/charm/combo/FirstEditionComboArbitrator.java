package net.sf.anathema.character.impl.model.charm.combo;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.duration.SimpleDuration;
import net.sf.anathema.character.model.charm.IComboConfiguration;

public class FirstEditionComboArbitrator extends ComboArbitrator {

  public FirstEditionComboArbitrator(IComboConfiguration config)
  {
	  super(config);
  }
	
  @Override
  protected boolean isCharmLegalByRules(ICharm charm, boolean arrayRules) {
    return charm.getDuration() == SimpleDuration.INSTANT_DURATION;
  }
}