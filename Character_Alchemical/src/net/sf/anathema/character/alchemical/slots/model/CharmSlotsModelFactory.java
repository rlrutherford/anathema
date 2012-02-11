package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.alchemical.slots.CharmSlotsTemplate;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModel;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.IAdditionalModelFactory;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.template.additional.IAdditionalTemplate;

public class CharmSlotsModelFactory implements IAdditionalModelFactory {

  public IAdditionalModel createModel(IAdditionalTemplate additionalTemplate, ICharacterModelContext context) {
	  return new CharmSlotsModel((CharmSlotsTemplate) additionalTemplate, context);	
  }
}