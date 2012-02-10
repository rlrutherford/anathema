package net.sf.anathema.character.model.charm;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;
import net.sf.anathema.character.model.charm.special.IMultiLearnableCharmConfiguration;
import net.sf.anathema.lib.control.change.IChangeListener;

public interface IComboConfiguration extends ComboLearnTime, ComboEditingRules {

  void addCharmToCombo(ICharm charm, boolean experienced);

  void addComboModelListener(IChangeListener listener);

  void removeCharmsFromCombo(ICharm[] charms);

  void finalizeCombo();
  
  void finalizeComboUpgrade(String xpMessage);

  ICombo getEditCombo();

  void addComboConfigurationListener(IComboConfigurationListener listener);

  ICombo[] getCurrentCombos();

  boolean isComboLegal(ICharm charm);

  void deleteCombo(ICombo combo);

  void clearCombo();

  public boolean isLearnedOnCreation(ICombo combo);
  void beginComboEdit(ICombo combo);

  ICombo[] getCreationCombos();

  ICombo[] getExperienceLearnedCombos();

  void finalizeCombo(boolean experienced);

  public boolean isAllowedToRemove(ICharm charm);
  
  public boolean canFinalizeWithXP();
  void setCrossPrerequisiteTypeComboAllowed(boolean allowed);
  public void addCharmToCombo(ICharm charm, ISubeffect effect);

  public void addComboModelListener(IChangeListener listener);

  public void removeCharmsFromCombo(ICharm[] charms);

  public void finalizeCombo();
  
  public void finalizeComboXP(String xpMessage);

  public ICombo getEditCombo();

  public void addComboConfigurationListener(IComboConfigurationListener listener);

  public ICombo[] getCurrentCombos();

  public boolean isComboLegal(ICharm charm);

  public void deleteCombo(ICombo combo);

  public void clearCombo();

  public void beginComboEdit(ICombo combo);

  public boolean isLearnedOnCreation(ICombo combo);

  public ICombo[] getCreationCombos();

  public ICombo[] getExperienceLearnedCombos();

  public void finalizeCombo(boolean experienced);

  public void setCrossPrerequisiteTypeComboAllowed(boolean allowed);
  
  public boolean isAllowedToRemove(ICharm charm);
  
  public boolean canFinalizeWithXP();
  
  public ICharm[] getCreationCharmPicks();
  
  public ICharm[] getExperiencedCharmPicks();
  
  public boolean isUseArrayRules();
  
  public boolean allowRepeats(ICharm charm);
  
  public void removeEffect(ICharm charm, ISubeffect effect);
  
  public ISubeffect[] getEffects(ICharm charm);
  
  public int getAllowedCount(ICharm charm, IMultiLearnableCharmConfiguration config, boolean removeBase);
}