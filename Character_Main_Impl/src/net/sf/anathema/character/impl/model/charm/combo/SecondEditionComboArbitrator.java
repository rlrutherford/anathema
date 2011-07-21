package net.sf.anathema.character.impl.model.charm.combo;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IExtendedCharmData;
import net.sf.anathema.character.generic.magic.charms.type.CharmType;
import net.sf.anathema.character.model.charm.IComboConfiguration;

public class SecondEditionComboArbitrator extends ComboArbitrator {

  public SecondEditionComboArbitrator(IComboConfiguration config)
  {
	  super(config);
  }
	
  @Override
  protected boolean isCharmLegalByRules(ICharm charm, boolean arrayRules) {
    boolean comboBasic = isComboBasic(charm);
    boolean comboOk = charm.hasAttribute(IExtendedCharmData.COMBO_OK_ATTRIBUTE);
    boolean noArray = charm.hasAttribute(IExtendedCharmData.NO_ARRAY_ATTRIBUTE);
    return comboBasic || comboOk || (arrayRules && !noArray);
  }

  @Override
  protected boolean specialRestrictionsApply(ICharm charm1, ICharm charm2) {
    if (isComboBasic(charm1) && charm2.getCharmTypeModel().getCharmType() != CharmType.Reflexive) {
      return true;
    }
    return super.specialRestrictionsApply(charm1, charm2);
  }

  private boolean isComboBasic(ICharm charm) {
    return charm.hasAttribute(IExtendedCharmData.COMBO_BASIC_ATTRIBUTE);
  }
}