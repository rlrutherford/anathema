package net.sf.anathema.character.presenter.charm;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.disy.commons.core.util.StringUtilities;
import net.disy.commons.swing.action.SmartAction;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.DedicatedCharacterChangeAdapter;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharmConfiguration;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;
import net.sf.anathema.character.model.ICharacterStatistics;
import net.sf.anathema.character.model.charm.CharmLearnAdapter;
import net.sf.anathema.character.model.charm.ICharmConfiguration;
import net.sf.anathema.character.model.charm.ICharmLearnListener;
import net.sf.anathema.character.model.charm.ICombo;
import net.sf.anathema.character.model.charm.IComboConfiguration;
import net.sf.anathema.character.model.charm.IComboConfigurationListener;
import net.sf.anathema.character.model.charm.ILearningCharmGroup;
import net.sf.anathema.character.model.charm.special.IMultiLearnableCharmConfiguration;
import net.sf.anathema.character.model.charm.special.IMultipleEffectCharmConfiguration;
import net.sf.anathema.character.model.charm.special.IUpgradableCharmConfiguration;
import net.sf.anathema.character.view.magic.IComboConfigurationView;
import net.sf.anathema.character.view.magic.IComboView;
import net.sf.anathema.character.view.magic.IComboViewListener;
import net.sf.anathema.character.view.magic.IMagicViewFactory;
import net.sf.anathema.framework.presenter.resources.BasicUi;
import net.sf.anathema.framework.presenter.view.IViewContent;
import net.sf.anathema.framework.presenter.view.SimpleViewContent;
import net.sf.anathema.framework.view.util.ContentProperties;
import net.sf.anathema.lib.control.change.IChangeListener;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.workflow.textualdescription.ITextView;
import net.sf.anathema.lib.workflow.textualdescription.TextualPresentation;

public class ComboConfigurationPresenter implements IContentPresenter {

  private final ICharmConfiguration charmConfiguration;
  private final IComboConfiguration comboConfiguration;
  private final Map<ICombo, IComboView> viewsByCombo = new HashMap<ICombo, IComboView>();
  private final ICharacterStatistics statistics;
  private final IResources resources;
  private final IComboConfigurationView view;
  private final boolean useArrayRules;
  private Object[] allCharms;
  private Object[] comboCharms;

  public ComboConfigurationPresenter(IResources resources, ICharacterStatistics statistics, IMagicViewFactory factory, boolean useArrayRules) {
    this.resources = resources;
    this.statistics = statistics;
    this.charmConfiguration = statistics.getCharms();
    this.comboConfiguration = statistics.getCombos();
    this.view = factory.createCharmComboView();
    this.useArrayRules = useArrayRules;
  }

  public void initPresentation() {
    view.initGui(new ComboViewProperties(resources, comboConfiguration, this));
    initCharmLearnListening(view);
    ITextView nameView = view.addComboNameView(resources.getString("CardView.CharmConfiguration.ComboCreation.NameLabel")); //$NON-NLS-1$);
    ICombo editCombo = comboConfiguration.getEditCombo();
    TextualPresentation textualPresentation = new TextualPresentation();
    textualPresentation.initView(nameView, editCombo.getName());
    ITextView descriptionView = view.addComboDescriptionView(resources.getString("CardView.CharmConfiguration.ComboCreation.DescriptionLabel")); //$NON-NLS-1$);
    textualPresentation.initView(descriptionView, editCombo.getDescription());
    updateCharmListsInView(view);
    initViewListening(view);
    initComboModelListening(view);
    initComboConfigurationListening(view);
    statistics.getCharacterContext().getCharacterListening().addChangeListener(new DedicatedCharacterChangeAdapter() {
      @Override
      public void experiencedChanged(boolean experienced) {
        updateComboButtons();
      }

      @Override
      public void casteChanged() {
        enableCrossPrerequisiteTypeCombos();
      }
    });
    enableCrossPrerequisiteTypeCombos();
    updateComboButtons();
  }

  public IViewContent getTabContent() {
    String header = resources.getString("CardView.CharmConfiguration.ComboCreation.Title"
    		+ (useArrayRules ? "Array" : "")); //$NON-NLS-1$
    return new SimpleViewContent(new ContentProperties(header), view);
  }

  private void enableCrossPrerequisiteTypeCombos() {
    ICasteType caste = statistics.getCharacterConcept().getCaste().getType();
    boolean alienCharms = statistics.getCharacterTemplate().getMagicTemplate().getCharmTemplate().isAllowedAlienCharms(
        caste);
    comboConfiguration.setCrossPrerequisiteTypeComboAllowed(alienCharms);
  }

  private void initComboConfigurationListening(final IComboConfigurationView comboView) {
    comboConfiguration.addComboConfigurationListener(new IComboConfigurationListener() {
      public void comboAdded(ICombo combo) {
        addComboToView(comboView, combo);
      }

      public void comboChanged(ICombo combo) {
        viewsByCombo.get(combo).updateCombo(createComboNameString(combo), convertToHtml(combo));
      }

      public void comboDeleted(ICombo combo) {
        view.deleteView(viewsByCombo.get(combo));
      }

      public void editBegun(ICombo combo) {
        setViewsToNotEditing();
        setViewToEditing(combo);
        comboView.setEditState(true);
      }

      public void editEnded() {
        setViewsToNotEditing();
        comboView.setEditState(false);
      }

    });
    for (ICombo combo : comboConfiguration.getCurrentCombos()) {
      addComboToView(comboView, combo);
    }
  }

  private String createComboNameString(ICombo combo) {
    String comboName = combo.getName().getText();
    if (StringUtilities.isNullOrEmpty(comboName)) {
      comboName = resources.getString("CardView.CharmConfiguration.ComboCreation.UnnamedCombo"); //$NON-NLS-1$
    }
    return comboName;
  }

  private void initCharmLearnListening(final IComboConfigurationView comboView) {
    ICharmLearnListener charmLearnListener = new CharmLearnAdapter() {
      @Override
      public void charmLearned(ICharm charm) {
        updateCharmListsInView(comboView);
      }

      @Override
      public void charmForgotten(ICharm charm) {
        updateCharmListsInView(comboView);
      }
    };
    for (ILearningCharmGroup group : charmConfiguration.getAllGroups()) {
      group.addCharmLearnListener(charmLearnListener);
    }
  }

  private void addComboToView(final IComboConfigurationView comboConfigurationView, final ICombo combo) {
    SmartAction deleteAction = new SmartAction(
        resources.getString("CardView.CharmConfiguration.ComboCreation.DeleteLabel"), new BasicUi(resources).getClearIcon()) { //$NON-NLS-1$
      private static final long serialVersionUID = 3964418545450534344L;

      @Override
      protected void execute(Component parentComponent) {
        comboConfiguration.deleteCombo(combo);
      }
    };
    SmartAction editAction = new SmartAction(
        resources.getString("CardView.CharmConfiguration.ComboCreation.EditLabel"), new BasicUi(resources).getEditIcon()) {//$NON-NLS-1$
      private static final long serialVersionUID = -7491597143093367976L;

      @Override
      protected void execute(Component parentComponent) {
        comboConfiguration.beginComboEdit(combo);
      }
    };
    final IComboView comboView = comboConfigurationView.addComboView(
        createComboNameString(combo),
        convertToHtml(combo),
        deleteAction,
        editAction);
    viewsByCombo.put(combo, comboView);
  }

  private String convertToHtml(ICombo combo) {
    String text = combo.getDescription().getText();
    ICharm[] charms = combo.getCharms();
    String charmList = "<b>"; //$NON-NLS-1$
    Iterator<ICharm> charmIterator = Arrays.asList(charms).iterator();
    if (charmIterator.hasNext()) {
      charmList = charmList.concat(getCharmString(charmIterator.next(), combo));
    }
    while (charmIterator.hasNext()) {
      charmList = charmList.concat(", " + getCharmString(charmIterator.next(), combo)); //$NON-NLS-1$
    }
    charmList += "</b>"; //$NON-NLS-1$
    if (StringUtilities.isNullOrEmpty(text)) {
      return wrapHtml(charmList);
    }
    String converted = text.replace("\n", "<br>"); //$NON-NLS-1$ //$NON-NLS-2$
    return wrapHtml(charmList + " - <i>" + converted + "</i>"); //$NON-NLS-1$//$NON-NLS-2$
  }
  
  private String getCharmString(ICharm charm, ICombo combo)
  {
	  String baseName = resources.getString(charm.getId());
	  ISubeffect[] effectList = combo.getLearnedSubeffects(charm);
	  if (effectList != null && effectList.length > 0)
	  {
		  baseName += " (";
		  for (ISubeffect effect : effectList)
			  baseName += (effect == effectList[0] ? "" : ", ") + resources.getString(effect.getId());
		  baseName += ")";
	  }
	  return baseName;
  }

  private String wrapHtml(String text) {
    return "<html><body>" + text + "</body></html>"; //$NON-NLS-1$//$NON-NLS-2$
  }

  private void updateCharmListsInView(final IComboConfigurationView comboView) {
	allCharms = getCharmList(charmConfiguration.getLearnedCharms(statistics.isExperienced()), false);
	comboCharms = getCharmList(comboConfiguration.getEditCombo().getCharms(), true);
    comboView.setComboCharms(comboCharms);
    comboView.setAllCharms(allCharms);
  }
  
  private Object[] getCharmList(ICharm[] charms, boolean isCombo)
  {
	  List<Object> displayCharmList = new ArrayList<Object>();
      for (ICharm charm : charms)
	  {
    	  displayCharmList.add(charm);
    	  
    	  ISpecialCharmConfiguration config = charmConfiguration.getSpecialCharmConfiguration(charm);
    	  if (config != null)
    	  {
    		  if (comboConfiguration.isUseArrayRules() &&
    			  config instanceof IMultipleEffectCharmConfiguration)
    		  {
    			  IMultipleEffectCharmConfiguration effectConfig = (IMultipleEffectCharmConfiguration)config;
    			  if (hasMultiPicks(effectConfig))
    			  {
	    			  ISubeffect[] effectList = getEffectList(effectConfig, isCombo);
	    			  for (ISubeffect effect : effectList)
	    				  displayCharmList.add(effect);
    				  displayCharmList.remove(charm);
    			  }
	    	  }
	      }
	  }
      return displayCharmList.toArray(new Object[0]);
  }
  
  private boolean hasMultiPicks(IMultipleEffectCharmConfiguration config)
  {
	  if (config instanceof IUpgradableCharmConfiguration)
		  return ((IUpgradableCharmConfiguration)config).getCharmEffects().length > 0;
	  return true;
  }
  
  private ISubeffect[] getEffectList(IMultipleEffectCharmConfiguration config, boolean isCombo)
  {
	  if (isCombo)
		  return comboConfiguration.getEditCombo().getLearnedSubeffects(config.getCharm());
	  else
	  {
		  List<ISubeffect> effectList = new ArrayList<ISubeffect>();
		  ISubeffect[] baseList = config.getEffects();
		  if (config instanceof IUpgradableCharmConfiguration)
			  baseList = ((IUpgradableCharmConfiguration)config).getCharmEffects();
		  for (ISubeffect effect : baseList)
			  if (effect.isLearned())
				  effectList.add(effect);
		  return effectList.toArray(new ISubeffect[0]);		  
	  }
	  
  }

  private void initComboModelListening(final IComboConfigurationView comboView) {
    comboConfiguration.addComboModelListener(new IChangeListener() {
      public void changeOccured() {
        updateCharmListsInView(comboView);
      }
    });
  }

  private void initViewListening(final IComboConfigurationView comboView) {
    comboView.addComboViewListener(new IComboViewListener() {
      public void charmAdded(ICharm addedCharm, ISubeffect effect) {
        comboConfiguration.addCharmToCombo(addedCharm, effect);
      }

      public void charmRemoved(Object[] removedCharms) {
        List<ICharm> removedCharmList = new ArrayList<ICharm>();
        for (Object charmObject : removedCharms) {
          if (charmObject instanceof ISubeffect)
          {
        	  comboConfiguration.removeEffect(((ISubeffect)charmObject).getCharm(), (ISubeffect)charmObject);
        	  if (comboConfiguration.getEffects(((ISubeffect)charmObject).getCharm()).length > 0)
        		  continue;
        	  else
        		  charmObject = ((ISubeffect)charmObject).getCharm();
          }
          removedCharmList.add((ICharm) charmObject);
        }
        comboConfiguration.removeCharmsFromCombo(removedCharmList.toArray(new ICharm[0]));
      }

      public void comboFinalized() {
        comboConfiguration.finalizeCombo();
      }
      
      public void comboFinalizedXP() {
    	  String comboName = comboConfiguration.getEditCombo().getName().getText();
    	  comboName = comboName == null ? resources.getString("CardView.CharmConfiguration.ComboCreation.Combo") : "\"" + comboName + "\"";
          comboConfiguration.finalizeComboUpgrade(comboName);
        }

      public void comboCleared() {
        comboConfiguration.clearCombo();
      }
    });
  }

  private void setViewToEditing(ICombo combo) {
    IComboView comboView = viewsByCombo.get(combo);
    comboView.setEditText(resources.getString("CardView.CharmConfiguration.ComboCreation.RestartEditLabel")); //$NON-NLS-1$
    createComboNameString(combo);
    comboView.updateCombo(createComboNameString(combo) + " (" //$NON-NLS-1$
        + resources.getString("CardView.CharmConfiguration.ComboCreation.EditingLabel") //$NON-NLS-1$
        + ")", convertToHtml(combo)); //$NON-NLS-1$
  }

  private void setViewsToNotEditing() {
    for (ICombo currentCombo : viewsByCombo.keySet()) {
      IComboView comboView = viewsByCombo.get(currentCombo);
      comboView.setEditText(resources.getString("CardView.CharmConfiguration.ComboCreation.EditLabel")); //$NON-NLS-1$
      comboView.updateCombo(createComboNameString(currentCombo), convertToHtml(currentCombo));
    }
  }

  private void updateComboButtons() {
    for (ICombo combo : comboConfiguration.getCurrentCombos()) {
      IComboView comboView = viewsByCombo.get(combo);
      boolean disabled = comboConfiguration.isLearnedOnCreation(combo) && statistics.isExperienced();
      comboView.setEditButtonsVisible(!disabled);
    }
  }
  
  public String getCharmTag(ICharm charm, int index, boolean isCombo)
  {
	  if (!comboConfiguration.isUseArrayRules())
		  return "";
	  
	  ISpecialCharmConfiguration config = charmConfiguration.getSpecialCharmConfiguration(charm.getId());
	  if (config instanceof IMultiLearnableCharmConfiguration)
	  {
		  int remainingCount = comboConfiguration.getAllowedCount(charm, (IMultiLearnableCharmConfiguration) config, true);
		  return remainingCount > 0 ? " (" + remainingCount + ")" : "";
	  }
	  return "";
  }
}