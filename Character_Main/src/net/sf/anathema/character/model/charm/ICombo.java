package net.sf.anathema.character.model.charm;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;
import net.sf.anathema.lib.control.change.IChangeListener;
import net.sf.anathema.lib.workflow.textualdescription.ITextualDescription;

public interface ICombo extends Cloneable {

  public void addComboModelListener(IChangeListener listener);

  public void removeCharms(ICharm[] charm);

  public ICombo clone();

  public void clear();

  public ITextualDescription getName();

  public ITextualDescription getDescription();

  public ICharm[] getCharms();
  
  public ICharm[] getCreationCharms();
  
  public ICharm[] getExperiencedCharms();

  public boolean contains(ICharm charm);

  public Integer getId();

  public void setId(Integer id);

  public void getValuesFrom(ICombo combo);

  public void addCharm(ICharm charm, boolean experienced);
  
  public ISubeffect[] getLearnedSubeffects(ICharm charm);
  
  public ISubeffect[] getCreationSubeffects();
  
  public ISubeffect[] getExperiencedSubeffects();
  
  public void addEffect(ICharm charm, ISubeffect effect, boolean experienced);
  
  public void removeEffect(ICharm charm, ISubeffect effect);
  
  public ISubeffect[] getEffects(ICharm charm);
}