package net.sf.anthema.hero.traits;

import net.sf.anathema.character.main.library.trait.DefaultTrait;
import net.sf.anathema.character.main.library.trait.FriendlyValueChangeChecker;
import net.sf.anathema.character.main.library.trait.favorable.IncrementChecker;
import net.sf.anathema.character.main.library.trait.rules.FavorableTraitRules;
import net.sf.anathema.hero.dummy.DummyCasteType;
import net.sf.anathema.hero.dummy.DummyHero;
import net.sf.anathema.hero.dummy.models.DummyHeroConcept;
import net.sf.anathema.hero.dummy.models.DummySpiritualTraitModel;
import net.sf.anathema.hero.dummy.models.DummyTraitModel;
import net.sf.anathema.character.main.traits.ITraitTemplate;
import net.sf.anathema.character.main.traits.SimpleTraitTemplate;
import net.sf.anathema.character.main.traits.context.CreationTraitValueStrategy;
import net.sf.anathema.character.main.traits.context.ExperiencedTraitValueStrategy;
import net.sf.anathema.character.main.traits.context.ProxyTraitValueStrategy;
import net.sf.anathema.character.main.traits.types.AbilityType;
import net.sf.anathema.character.main.traits.types.OtherTraitType;
import net.sf.anathema.hero.concept.CasteType;
import net.sf.anathema.hero.model.Hero;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class FavorableTrait_HighEssenceTest {

  private IncrementChecker incrementChecker = Mockito.mock(IncrementChecker.class);
  private ProxyTraitValueStrategy valueStrategy;
  private DefaultTrait trait;

  @Before
  public void createTrait() throws Exception {
    this.valueStrategy = new ProxyTraitValueStrategy(new CreationTraitValueStrategy());
    DummyTraitModel traits = new DummyTraitModel();
    traits.valueStrategy = valueStrategy;
    DummySpiritualTraitModel otherTraitModel = new DummySpiritualTraitModel();
    DummyHero hero = new DummyHero();
    hero.addModel(otherTraitModel);
    hero.addModel(new DummyHeroConcept());
    hero.addModel(traits);
    otherTraitModel.getTrait(OtherTraitType.Essence).setCurrentValue(6);
    this.trait = createObjectUnderTest(hero);
  }

  @Test
  public void testSetValueTo6OnExperiencedCharacterWithHighEssence() throws Exception {
    valueStrategy.setStrategy(new ExperiencedTraitValueStrategy());
    trait.setCurrentValue(6);
    assertEquals(6, trait.getCurrentValue());
  }

  private DefaultTrait createObjectUnderTest(Hero hero) {
    ITraitTemplate template = SimpleTraitTemplate.createEssenceLimitedTemplate(0);
    FavorableTraitRules rules = new FavorableTraitRules(AbilityType.Archery, template, hero);
    return new DefaultTrait(hero, rules, new CasteType[]{new DummyCasteType()}, new FriendlyValueChangeChecker(), incrementChecker);
  }
}