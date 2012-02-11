package net.sf.anathema.character.alchemical.slots.view;

import net.sf.anathema.character.alchemical.slots.model.ICharmSlotsModel;
import net.sf.anathema.character.alchemical.slots.presenter.CharmSlotsPresenter;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModel;
import net.sf.anathema.character.generic.framework.additionaltemplate.IAdditionalViewFactory;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.resources.IResources;

public class CharmSlotsViewFactory implements IAdditionalViewFactory {

  public IView createView(final IAdditionalModel model, final IResources resources, ICharacterType type) {
    ICharmSlotsViewProperties properties = new CharmSlotsViewProperties(resources);
    
   	ICharmSlotsView view = new CharmSlotsView(properties);
    new CharmSlotsPresenter(resources, view,
        		(ICharmSlotsModel) model).initPresentation();
    
    return view;
  }
}