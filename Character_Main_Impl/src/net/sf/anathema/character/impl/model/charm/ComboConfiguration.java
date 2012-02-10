package net.sf.anathema.character.impl.model.charm;

import java.util.ArrayList;
import java.util.List;

import net.disy.commons.core.util.ArrayUtilities;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharmConfiguration;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;
import net.sf.anathema.character.generic.rules.IEditionVisitor;
import net.sf.anathema.character.generic.rules.IExaltedEdition;
import net.sf.anathema.character.impl.model.charm.combo.FirstEditionComboArbitrator;
import net.sf.anathema.character.impl.model.charm.combo.IComboArbitrator;
import net.sf.anathema.character.impl.model.charm.combo.SecondEditionComboArbitrator;
import net.sf.anathema.character.model.ICharacterStatistics;
import net.sf.anathema.character.model.advance.IExperiencePointConfiguration;
import net.sf.anathema.character.model.charm.CharmLearnAdapter;
import net.sf.anathema.character.model.charm.ICharmConfiguration;
import net.sf.anathema.character.model.charm.ICombo;
import net.sf.anathema.character.model.charm.IComboConfiguration;
import net.sf.anathema.character.model.charm.IComboConfigurationListener;
import net.sf.anathema.character.model.charm.learn.IComboLearnStrategy;
import net.sf.anathema.character.model.charm.special.IMultiLearnableCharmConfiguration;
import net.sf.anathema.character.model.charm.special.IMultipleEffectCharmConfiguration;
import net.sf.anathema.lib.control.GenericControl;
import net.sf.anathema.lib.control.IClosure;
import net.sf.anathema.lib.control.change.IChangeListener;

public class ComboConfiguration implements IComboConfiguration {

  private final List<ICombo> creationComboList = new ArrayList<ICombo>();
  private final List<ICombo> experiencedComboList = new ArrayList<ICombo>();
  private final IComboArbitrator rules;
  private final ICombo editCombo = new Combo();
  private final GenericControl<IComboConfigurationListener> control = new GenericControl<IComboConfigurationListener>();
  private final ComboIdProvider idProvider = new ComboIdProvider();
  private final IComboLearnStrategy learnStrategy;
  private ExperienceComboEditingSupport experienceSupport;
  private final ICharacterModelContext context;
  private final boolean useArrayRules;
  private ICombo originalCombo;


  public ComboConfiguration(
          ICharmConfiguration charmConfiguration,
          IComboLearnStrategy learnStrategy,
          IExaltedEdition edition,
          IExperiencePointConfiguration experience,
          ICharacterStatistics characterStatistics) {
    this.learnStrategy = learnStrategy;
    charmConfiguration.addCharmLearnListener(new CharmLearnAdapter() {
      @Override
      public void charmForgotten(ICharm charm) {
    	  if (!useArrayRules)
    		  checkCombos(charm);
      }
    });
    final IComboArbitrator[] editionRules = new IComboArbitrator[1];
    edition.accept(new IEditionVisitor() {
      public void visitFirstEdition(IExaltedEdition visitedEdition) {
        editionRules[0] = new FirstEditionComboArbitrator(ComboConfiguration.this);
      }

      public void visitSecondEdition(IExaltedEdition visitedEdition) {
        editionRules[0] = new SecondEditionComboArbitrator(ComboConfiguration.this);
      }
    });
    this.rules = editionRules[0];
    this.experienceSupport = new ExperienceComboEditingSupport(characterStatistics, experience,
    		editCombo, this);
    this.context = context;
    this.useArrayRules = useArrayRules;
  }

  public void setCrossPrerequisiteTypeComboAllowed(boolean allowed) {
    rules.setCrossPrerequisiteTypeComboAllowed(allowed);
  }

  private void checkCombos(ICharm charm) {
    List<ICombo> deletionList = new ArrayList<ICombo>();
    for (ICombo combo : creationComboList) {
      if (combo.contains(charm)) {
        combo.removeCharms(new ICharm[]{charm});
        if (combo.getCharms().length < 2) {
          deletionList.add(combo);
        }
        fireComboChanged(combo);
      }
    }
    if (editCombo.contains(charm)) {
      removeCharmsFromCombo(new ICharm[]{charm});
    }
    for (ICombo combo : deletionList) {
      deleteCombo(combo);
    }
  }

  public void addCharmToCombo(ICharm charm, boolean experienced) {
    if (rules.canBeAddedToCombo(getEditCombo(), charm)) {
      getEditCombo().addCharm(charm, experienced);
    } else {
      throw new IllegalArgumentException("The charm " + charm.getId() + " is illegal in this combo."); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  public void addComboModelListener(IChangeListener listener) {
    editCombo.addComboModelListener(listener);
  }

  public void removeCharmsFromCombo(ICharm[] charms) {
    editCombo.removeCharms(charms);
  }

  public void finalizeComboUpgrade(String xpMessage) {
    experienceSupport.commitChanges(xpMessage);
    finalizeCombo();
  }

  public void finalizeCombo() {
    learnStrategy.finalizeCombo(this);
  }

  public void finalizeCombo(boolean experienced) {
    ICombo combo = editCombo.clone();
    if (originalCombo != null) originalCombo = originalCombo.clone();
    if (combo.getId() == null) {
      combo.setId(idProvider.createId());
      if (experienced) {
        experiencedComboList.add(combo);
      } else {
        creationComboList.add(combo);
      }
      fireComboAdded(combo);
    } else {
      ICombo editedCombo = getComboById(combo.getId());
      editedCombo.getValuesFrom(combo);
      fireComboChanged(editedCombo);
      fireEndEditEvent();
    }
    if (useArrayRules)
    	removeArrayCharms();
    editCombo.clear();
  }

  public ICombo getEditCombo() {
    return editCombo;
  }

  private ICombo getComboById(int id) {
    for (ICombo combo : getCurrentCombos()) {
      if (combo.getId() == id) {
        return combo;
      }
    }
    return null;
  }

  public void addComboConfigurationListener(IComboConfigurationListener listener) {
    control.addListener(listener);
  }

  private void fireComboAdded(final ICombo combo) {
    control.forAllDo(new IClosure<IComboConfigurationListener>() {
      public void execute(IComboConfigurationListener input) {
        input.comboAdded(combo);
      }
    });
  }

  private void fireComboDeleted(final ICombo combo) {
    control.forAllDo(new IClosure<IComboConfigurationListener>() {
      public void execute(IComboConfigurationListener input) {
        input.comboDeleted(combo);
      }
    });
  }

  private void fireComboChanged(final ICombo combo) {
    control.forAllDo(new IClosure<IComboConfigurationListener>() {
      public void execute(IComboConfigurationListener input) {
        input.comboChanged(combo);
      }
    });
  }

  private void fireBeginEditEvent(final ICombo combo) {
    control.forAllDo(new IClosure<IComboConfigurationListener>() {
      public void execute(IComboConfigurationListener input) {
        input.editBegun(combo);
      }
    });
  }

  private void fireEndEditEvent() {
    control.forAllDo(new IClosure<IComboConfigurationListener>() {
      public void execute(IComboConfigurationListener input) {
        input.editEnded();
      }
    });
  }

  public ICombo[] getCurrentCombos() {
    return learnStrategy.getCurrentCombos(this);
  }

  public ICombo[] getCreationCombos() {
    return creationComboList.toArray(new ICombo[creationComboList.size()]);
  }

  public ICombo[] getExperienceLearnedCombos() {
    return experiencedComboList.toArray(new ICombo[experiencedComboList.size()]);
  }

  public boolean isComboLegal(ICharm charm) {
    return rules.canBeAddedToCombo(getEditCombo(), charm, useArrayRules);
  }

  public void deleteCombo(ICombo combo) {
    experiencedComboList.remove(combo);
    creationComboList.remove(combo);
    fireComboDeleted(combo);
    if (combo.getId().equals(editCombo.getId())) {
      clearCombo();
    }
  }

  public void clearCombo() {
    editCombo.clear();
    experienceSupport.abortChange();
    fireEndEditEvent();
  }

  public void beginComboEdit(ICombo combo) {
    experienceSupport.startChanging(combo);
    editCombo.clear();
    editCombo.getValuesFrom(combo);
    fireBeginEditEvent(combo);
  }

  public boolean isLearnedOnCreation(ICombo combo) {
    return creationComboList.contains(combo);
  }

  @Override
  public boolean isAllowedToRemove(ICharm charm) {
    return experienceSupport.isAllowedToRemove(charm);
  }

  @Override
  public boolean canFinalize() {
    return experienceSupport.canFinalize();
  }
  
  @Override
  public boolean canFinalizeWithXP() {
    return experienceSupport.canFinalizeWithXP();
  }
  
  private void removeArrayCharms()
  {
	  for (ICharm charm : editCombo.getCharms())
	  {
		  if (originalCombo == null ||
			  originalCombo.contains(charm))
		  {
			  ISpecialCharmConfiguration config = charmConfiguration.getSpecialCharmConfiguration(charm);
			  if (config != null)
			  {
				  if (config instanceof IMultiLearnableCharmConfiguration)
				  {
					  int comboCount = originalCombo == null ? -countInCombo(editCombo, charm) :
						  countInCombo(originalCombo, charm) - countInCombo(editCombo, charm);
					  ((IMultiLearnableCharmConfiguration)config).setCurrentLearnCount(config.getCurrentLearnCount()
							  + comboCount);
					  continue;
				  }
				  if (config instanceof IMultipleEffectCharmConfiguration)
				  {
					  IMultipleEffectCharmConfiguration multiConfig = (IMultipleEffectCharmConfiguration)config;
					  for (ISubeffect effect : multiConfig.getEffects())
						  if (effect.isLearned() && ArrayUtilities.contains(editCombo.getEffects(charm), effect) &&
							  (originalCombo == null || !ArrayUtilities.contains(originalCombo.getEffects(charm), effect)))
							  effect.setLearned(false);
					  continue;						  
				  }
			  }
			  charmConfiguration.getGroup(charm).forgetCharm(charm, isExperienceLearned(editCombo, charm));
		  }
	  }
  }
  
  private boolean isExperienceLearned(ICombo combo, ICharm charm)
  {
	  for (ICharm otherCharm : combo.getExperiencedLearnedCharms())
		  if (otherCharm == charm)
			  return true;
	  return false;
  }
  
  public ICharm[] getCreationCharmPicks()
  {
	  if (!useArrayRules)
		  return new ICharm[0];
	  List<ICharm> charms = new ArrayList<ICharm>();
	  List<ICharm> handledMultiEffects = new ArrayList<ICharm>();
	  for (ICombo combo : creationComboList)
	  {
		  for (ICharm charm : combo.getCharms())
			  charms.add(charm);
		  for (ISubeffect effect : combo.getCreationSubeffects())
			  if (handledMultiEffects.contains(effect.getCharm()))
				  charms.add(effect.getCharm());
			  else
				  handledMultiEffects.add(effect.getCharm());
	  }
	  ICharm[] charmArray = new ICharm[charms.size()];
	  charms.toArray(charmArray);
	  return charmArray;
  }
  
  public ICharm[] getExperiencedCharmPicks()
  {
	  if (!useArrayRules)
		  return new ICharm[0];
	  List<ICharm> charms = new ArrayList<ICharm>();
	  for (ICombo combo : creationComboList)
	  {
		  for (ICharm charm : combo.getExperiencedLearnedCharms())
			  charms.add(charm);
		  for (ISubeffect effect : combo.getExperiencedSubeffects())
			  if (!effect.isCreationLearned())
				  charms.add(effect.getCharm());
	  }
	  for (ICombo combo : experiencedComboList)
	  {
		  for (ICharm charm : combo.getExperiencedLearnedCharms())
			  charms.add(charm);
		  for (ISubeffect effect : combo.getExperiencedSubeffects())
			  if (!effect.isCreationLearned())
				  charms.add(effect.getCharm());
	  }
	  ICharm[] charmArray = new ICharm[charms.size()];
	  charms.toArray(charmArray);
	  return charmArray;
  }
  
  public boolean allowRepeats(ICharm charm)
  {
	  ISpecialCharmConfiguration config = charmConfiguration.getSpecialCharmConfiguration(charm.getId());
	  if (config instanceof IMultiLearnableCharmConfiguration)
		  return getAllowedCount(charm, (IMultiLearnableCharmConfiguration)config, true) > 0;
	  if (config instanceof IMultipleEffectCharmConfiguration)
		  return true;
	  return false;
  }
  
  public int getAllowedCount(ICharm charm, IMultiLearnableCharmConfiguration config, boolean countBase)
  {
	  int base = originalCombo == null ? 0 : countInCombo(originalCombo, charm);
	  return (config.getCurrentLearnCount() - countInCombo(editCombo, charm) + (countBase ? 1 : 0) * base);
	  
  }
  
  private int countInCombo(ICombo combo, ICharm charm)
  {
	  int times = 0;
	  for (ICharm otherCharm : combo.getCharms())
		  if (charm == otherCharm)
			  times++;
	  return times;
  }
  
  public boolean isUseArrayRules()
  {
	  return useArrayRules;
  }
  
  public void removeEffect(ICharm charm, ISubeffect effect)
  {
	  editCombo.removeEffect(charm, effect);
  }
  
  public ISubeffect[] getEffects(ICharm charm)
  {
	  return editCombo.getEffects(charm);
  }
}