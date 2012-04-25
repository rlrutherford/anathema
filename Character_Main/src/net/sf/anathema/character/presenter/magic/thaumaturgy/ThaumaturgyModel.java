package net.sf.anathema.character.presenter.magic.thaumaturgy;

import net.sf.anathema.character.generic.template.magic.ISpellMagicTemplate;
import net.sf.anathema.character.model.ICharacterStatistics;
import net.sf.anathema.lib.util.IIdentificate;

public class ThaumaturgyModel {

  private ICharacterStatistics statistics;

  protected ThaumaturgyModel(ICharacterStatistics statistics) {
    this.statistics = statistics;
  }
  
  public IIdentificate[] getArts() {
	return statistics.getThaumaturgy().getArts();
  }

  protected final ISpellMagicTemplate getSpellMagicTemplate() {
    return statistics.getCharacterTemplate().getMagicTemplate().getSpellMagic();
  }
}
