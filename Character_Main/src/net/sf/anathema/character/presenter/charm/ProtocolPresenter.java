package net.sf.anathema.character.presenter.charm;

import net.sf.anathema.character.generic.magic.spells.CircleType;
import net.sf.anathema.character.model.ICharacterStatistics;
import net.sf.anathema.character.view.magic.IMagicViewFactory;
import net.sf.anathema.lib.resources.IResources;

public class ProtocolPresenter extends AbstractSpellPresenter {

  public ProtocolPresenter(ICharacterStatistics statistics, IResources resources, IMagicViewFactory factory) {
    super(statistics, resources, factory);
  }

  @Override
  protected String getTabTitleResourceKey() {
    return "CardView.CharmConfiguration.Protocol.Title"; //$NON-NLS-1$
  }

  @Override
  protected CircleType[] getCircles() {
    return getCharacterTemplate().getMagicTemplate().getSpellMagic().getProtocolCircles();
  }
}