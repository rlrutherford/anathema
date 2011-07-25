package net.sf.anathema.character.view.magic;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;

public interface IComboViewListener {

  public void charmAdded(ICharm addedCharm, ISubeffect effect);

  public void charmRemoved(Object[] removedCharms);

  public void comboFinalized();
  
  public void comboFinalizedXP();

  public void comboCleared();
}