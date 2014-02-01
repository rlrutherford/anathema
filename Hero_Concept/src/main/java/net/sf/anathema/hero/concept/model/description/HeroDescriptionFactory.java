package net.sf.anathema.hero.concept.model.description;

import net.sf.anathema.hero.description.HeroDescription;
import net.sf.anathema.hero.experience.ExperienceModel;
import net.sf.anathema.hero.initialization.SimpleModelTreeEntry;
import net.sf.anathema.hero.model.HeroModelFactory;
import net.sf.anathema.hero.template.TemplateFactory;

public class HeroDescriptionFactory extends SimpleModelTreeEntry implements HeroModelFactory {

  public HeroDescriptionFactory() {
    super(HeroDescription.ID, ExperienceModel.ID);
  }

  @Override
  public HeroDescriptionImpl create(TemplateFactory templateFactory, String templateId) {
    return new HeroDescriptionImpl();
  }
}
