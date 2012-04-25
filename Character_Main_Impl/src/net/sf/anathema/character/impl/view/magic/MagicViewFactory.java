package net.sf.anathema.character.impl.view.magic;

import net.sf.anathema.character.presenter.magic.spells.SpellViewProperties;
import net.sf.anathema.character.presenter.magic.thaumaturgy.ThaumaturgyViewProperties;
import net.sf.anathema.character.view.magic.IComboConfigurationView;
import net.sf.anathema.character.view.magic.IMagicViewFactory;
import net.sf.anathema.character.view.magic.ISpellView;
import net.sf.anathema.character.view.magic.IThaumaturgyView;
import net.sf.anathema.charmtree.presenter.view.ICharmView;
import net.sf.anathema.platform.svgtree.presenter.view.ISvgTreeViewProperties;

public class MagicViewFactory implements IMagicViewFactory {

  public ICharmView createCharmSelectionView(final ISvgTreeViewProperties properties) {
    return new CharmView(properties);
  }

  public IComboConfigurationView createCharmComboView() {
    return new ComboConfigurationView();
  }

  public ISpellView createSpellView(final SpellViewProperties properties) {
    return new SpellView(properties);
  }

  public IThaumaturgyView createThaumaturgyView(ThaumaturgyViewProperties properties) {
	return new ThaumaturgyView(properties);
  }
}
