package net.sf.anathema.character.alchemical.slots.persistence;

import net.sf.anathema.character.generic.framework.additionaltemplate.persistence.IAdditionalPersister;
import net.sf.anathema.character.generic.framework.additionaltemplate.persistence.IAdditionalPersisterFactory;
import net.sf.anathema.framework.messaging.IAnathemaMessaging;

public class CharmSlotsPersisterFactory implements IAdditionalPersisterFactory {

  public IAdditionalPersister createPersister(IAnathemaMessaging messaging) {
    return new CharmSlotsPersister();
  }
}