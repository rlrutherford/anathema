package net.sf.anathema.herotype.solar.model.curse;

import net.sf.anathema.hero.initialization.SimpleModelTreeEntry;
import net.sf.anathema.hero.model.HeroModelFactory;
import net.sf.anathema.hero.spiritual.SpiritualTraitModel;
import net.sf.anathema.hero.template.TemplateFactory;
import net.sf.anathema.lib.util.Identifier;
import net.sf.anathema.lib.util.SimpleIdentifier;


public class SolarVirtueFlawModelFactory extends SimpleModelTreeEntry implements HeroModelFactory {

  private static final Identifier FACTORY_ID = new SimpleIdentifier("SolarVirtueFlaw");

  public SolarVirtueFlawModelFactory() {
    super(FACTORY_ID, SpiritualTraitModel.ID);
  }

  @SuppressWarnings("unchecked")
  @Override
  public DescriptiveVirtueFlawModelImpl create(TemplateFactory templateFactory, String templateId) {
    return new DescriptiveVirtueFlawModelImpl();
  }
}