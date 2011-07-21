package net.sf.anathema.character.impl.model.charm;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharmConfiguration;
import net.sf.anathema.character.generic.rules.IEditionVisitor;
import net.sf.anathema.character.generic.rules.IExaltedEdition;
import net.sf.anathema.character.impl.model.charm.combo.FirstEditionComboArbitrator;
import net.sf.anathema.character.impl.model.charm.combo.IComboArbitrator;
import net.sf.anathema.character.impl.model.charm.combo.SecondEditionComboArbitrator;
import net.sf.anathema.character.model.advance.IExperiencePointConfiguration;
import net.sf.anathema.character.model.charm.CharmLearnAdapter;
import net.sf.anathema.character.model.charm.ICharmConfiguration;
import net.sf.anathema.character.model.charm.ICombo;
import net.sf.anathema.character.model.charm.IComboConfiguration;
import net.sf.anathema.character.model.charm.IComboConfigurationListener;
import net.sf.anathema.character.model.charm.learn.IComboLearnStrategy;
import net.sf.anathema.character.model.charm.special.IMultiLearnableCharmConfiguration;
import net.sf.anathema.lib.control.GenericControl;
import net.sf.anathema.lib.control.IClosure;
import net.sf.anathema.lib.control.change.IChangeListener;

public class ComboConfiguration implements IComboConfiguration {

  private final List<ICombo> creationComboList = new ArrayList<ICombo>();
  private final List<ICombo> experiencedComboList = new ArrayList<ICombo>();
  private final IExperiencePointConfiguration experiencePoints;
  private final IComboArbitrator rules;
  private final ICombo editCombo = new Combo();
  private final GenericControl<IComboConfigurationListener> control = new GenericControl<IComboConfigurationListener>();
  private final ICharmConfiguration charmConfiguration;
  private final ComboIdProvider idProvider = new ComboIdProvider();
  private final IComboLearnStrategy learnStrategy;
  private final ICharacterModelContext context;
  private final boolean useArrayRules;
  private ICombo originalCombo;

  public ComboConfiguration(
      final ICharmConfiguration charmConfiguration,
      IComboLearnStrategy learnStrategy,
      IExaltedEdition edition,
      IExperiencePointConfiguration experiencePoints,
      ICharacterModelContext context,
      final boolean useArrayRules) {
    this.charmConfiguration = charmConfiguration;
    this.learnStrategy = learnStrategy;
    this.charmConfiguration.addCharmLearnListener(new CharmLearnAdapter() {
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
    this.experiencePoints = experiencePoints;
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
        combo.removeCharms(new ICharm[] { charm });
        if (combo.getCharms().length < 2) {
          deletionList.add(combo);
        }
        fireComboChanged(combo);
      }
    }
    if (editCombo.contains(charm)) {
      removeCharmsFromCombo(new ICharm[] { charm });
    }
    for (ICombo combo : deletionList) {
      deleteCombo(combo);
    }
  }

  public void addCharmToCombo(ICharm charm) {
    if (rules.canBeAddedToCombo(getEditCombo(), charm, useArrayRules)) {
      getEditCombo().addCharm(charm, context.getBasicCharacterContext().isExperienced());
    }
    else {
      throw new IllegalArgumentException("The charm " + charm.getId() + " is illegal in this combo."); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  public void addComboModelListener(IChangeListener listener) {
    editCombo.addComboModelListener(listener);
  }

  public void removeCharmsFromCombo(ICharm[] charms) {
    editCombo.removeCharms(charms);
  }

  public void finalizeCombo() {
    learnStrategy.finalizeCombo(this);
  }
  
  public void finalizeComboXP(String xpMessage)
  {
	  experiencePoints.addEntry(xpMessage, -1);
	  finalizeCombo();
  }

  public void finalizeCombo(boolean experienced) {
    ICombo combo = editCombo.clone();
    if (combo.getId() == null) {
      combo.setId(idProvider.createId());
      if (experienced) {
        experiencedComboList.add(combo);
      }
      else {
        creationComboList.add(combo);
      }
      fireComboAdded(combo);
    }
    else {
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
    return creationComboList.toArray(new ICombo[0]);
  }

  public ICombo[] getExperienceLearnedCombos() {
    return experiencedComboList.toArray(new ICombo[0]);
  }

  public boolean isComboLegal(ICharm charm) {
    return rules.canBeAddedToCombo(getEditCombo(), charm, useArrayRules);
  }

  public void deleteCombo(ICombo combo) {
    experiencedComboList.remove(combo);
    creationComboList.remove(combo);
    fireComboDeleted(combo);
    if (combo.getId() == editCombo.getId()) {
      clearCombo();
    }
  }

  public void clearCombo() {
    editCombo.clear();
    originalCombo = null;
    fireEndEditEvent();
  }

  public void beginComboEdit(ICombo combo) {
    editCombo.clear();
    editCombo.getValuesFrom(combo);
    originalCombo = combo;
    fireBeginEditEvent(combo);
  }

  public boolean isLearnedOnCreation(ICombo combo) {
    return creationComboList.contains(combo);
  }
  
  public boolean isAllowedToRemove(ICharm charm)
  {
	  if (originalCombo != null &&
		  (!creationComboList.contains(originalCombo) || experiencePoints.getTotalExperiencePoints() != 0) &&
		  originalCombo.contains(charm))
		  return false;
	  return true;
  }
  
  public boolean canFinalizeWithXP()
  {
	  if (originalCombo == null || !context.getBasicCharacterContext().isExperienced()) return false;
	  ICombo testCombo = new Combo();
	  testCombo.getValuesFrom(editCombo);
	  testCombo.removeCharms(originalCombo.getCharms());
	  return testCombo.getCharms().length > 0;
  }
  
  private void removeArrayCharms()
  {
	  for (ICharm charm : editCombo.getCharms())
	  {
		  if (originalCombo == null ||
			  originalCombo.contains(charm))
			  charmConfiguration.getGroup(charm).forgetCharm(charm, isExperienceLearned(editCombo, charm));
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
	  for (ICombo combo : creationComboList)
		  for (ICharm charm : combo.getCharms())
			  charms.add(charm);
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
		  for (ICharm charm : combo.getExperiencedLearnedCharms())
			  charms.add(charm);
	  for (ICombo combo : experiencedComboList)
		  for (ICharm charm : combo.getExperiencedLearnedCharms())
			  charms.add(charm);
	  ICharm[] charmArray = new ICharm[charms.size()];
	  charms.toArray(charmArray);
	  return charmArray;
  }
  
  public String getAvaliableCharmDetail(ICharm charm)
  {
	  ISpecialCharmConfiguration config = charmConfiguration.getSpecialCharmConfiguration(charm.getId());
	  if (config instanceof IMultiLearnableCharmConfiguration)
	  {
		  int remainingCount = config.getCurrentLearnCount() - countInCombo(charm);
		  return remainingCount > 0 ? "(" + remainingCount + ")" : "";
	  }
	  return "";
  }
  
  public boolean allowRepeats(ICharm charm)
  {
	  ISpecialCharmConfiguration config = charmConfiguration.getSpecialCharmConfiguration(charm.getId());
	  if (config instanceof IMultiLearnableCharmConfiguration)
		  return (config.getCurrentLearnCount() - countInCombo(charm)) > 0;
	  return false;
  }
  
  private int countInCombo(ICharm charm)
  {
	  int times = 0;
	  for (ICharm otherCharm : editCombo.getCharms())
		  if (charm == otherCharm)
			  times++;
	  return times;
  }
  
  public boolean isUseArrayRules()
  {
	  return useArrayRules;
  }
}