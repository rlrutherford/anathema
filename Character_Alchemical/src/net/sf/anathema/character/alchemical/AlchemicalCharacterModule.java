package net.sf.anathema.character.alchemical;

import net.sf.anathema.character.generic.backgrounds.IBackgroundTemplate;
import net.sf.anathema.character.generic.framework.ICharacterGenerics;
import net.sf.anathema.character.generic.framework.additionaltemplate.IAdditionalViewFactory;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.IAdditionalModelFactory;
import net.sf.anathema.character.generic.framework.additionaltemplate.persistence.IAdditionalPersisterFactory;
import net.sf.anathema.character.generic.framework.magic.FirstExcellency;
import net.sf.anathema.character.generic.framework.magic.SecondExcellency;
import net.sf.anathema.character.generic.framework.magic.ThirdExcellency;
import net.sf.anathema.character.generic.framework.module.NullObjectCharacterModuleAdapter;
import net.sf.anathema.character.generic.impl.backgrounds.CharacterTypeBackgroundTemplate;
import net.sf.anathema.character.generic.impl.backgrounds.EditionSpecificCharacterTypeBackgroundTemplate;
import net.sf.anathema.character.generic.impl.backgrounds.TemplateTypeBackgroundTemplate;
import net.sf.anathema.character.generic.impl.caste.CasteCollection;
import net.sf.anathema.character.generic.impl.magic.persistence.CharmCache;
import net.sf.anathema.character.generic.impl.rules.ExaltedEdition;
import net.sf.anathema.character.generic.impl.rules.ExaltedSourceBook;
import net.sf.anathema.character.generic.impl.traits.EssenceTemplate;
import net.sf.anathema.character.generic.magic.IMagicStats;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharm;
import net.sf.anathema.character.generic.template.ITemplateType;
import net.sf.anathema.character.generic.template.TemplateType;
import net.sf.anathema.character.generic.traits.LowerableState;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.character.reporting.CharacterReportingModule;
import net.sf.anathema.character.reporting.CharacterReportingModuleObject;
import net.sf.anathema.character.reporting.sheet.PdfEncodingRegistry;
import net.sf.anathema.character.reporting.sheet.page.IPdfPartEncoder;
import net.sf.anathema.character.alchemical.caste.AlchemicalCaste;
import net.sf.anathema.lib.registry.IIdentificateRegistry;
import net.sf.anathema.lib.registry.IRegistry;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.Identificate;

public class AlchemicalCharacterModule extends NullObjectCharacterModuleAdapter {
  private static final int ESSENCE_MAX = EssenceTemplate.SYSTEM_ESSENCE_MAX;
  
  public static final String BACKGROUND_ID_CLASS = "Class"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_COMMAND = "Command"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_EIDOLIN = "Eidolin"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_SAVANT = "AlchemicalSavant"; //$NON-NLS-1$
  
  @Override
  public void registerCommonData(ICharacterGenerics characterGenerics)
  {
	    characterGenerics.getCasteCollectionRegistry().register(
	            CharacterType.ALCHEMICAL,
	            new CasteCollection(AlchemicalCaste.values()));
  }

  @Override
  public void addCharacterTemplates(ICharacterGenerics characterGenerics) {
    CharmCache charmProvider = CharmCache.getInstance();
    registerParsedTemplate(characterGenerics, "template/Alchemical2nd.template"); //$NON-NLS-1$
  }

  @Override
  public void addBackgroundTemplates(ICharacterGenerics generics) {
    IIdentificateRegistry<IBackgroundTemplate> backgroundRegistry = generics.getBackgroundRegistry();
    //ITemplateType[] defaultTemplateType = new ITemplateType[] { defaultType, revisedType };
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_CLASS, CharacterType.ALCHEMICAL));
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_COMMAND, CharacterType.ALCHEMICAL));
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_EIDOLIN, CharacterType.ALCHEMICAL));
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_SAVANT, CharacterType.ALCHEMICAL));
    
  }

  @Override
  public void addAdditionalTemplateData(ICharacterGenerics characterGenerics) {
    IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry = characterGenerics.getAdditionalModelFactoryRegistry();
    IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry = characterGenerics.getAdditionalViewFactoryRegistry();
    IRegistry<String, IAdditionalPersisterFactory> persisterFactory = characterGenerics.getAdditonalPersisterFactoryRegistry();
    //registerSiderealColleges(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
    //registerFlawedFate(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
    //registerParadox(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
  }
  
  private void registerSiderealColleges(
		  IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry,
	      IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry,
	      IRegistry<String, IAdditionalPersisterFactory> persisterFactory)
  {
	/*  String templateId = SiderealCollegeTemplate.ID;
	  additionalModelFactoryRegistry.register(templateId, new SiderealCollegeModelFactory());  
	  additionalViewFactoryRegistry.register(templateId, new SiderealCollegeViewFactory());
	  persisterFactory.register(templateId, new SiderealCollegePersisterFactory());*/
  }
  
  /*@Override
  public void addReportTemplates(ICharacterGenerics generics, IResources resources) {
    CharacterReportingModuleObject moduleObject = generics.getModuleObjectMap().getModuleObject(
        CharacterReportingModule.class);
    PdfEncodingRegistry registry = moduleObject.getPdfEncodingRegistry();
    IPdfPartEncoder firstEditionEncoder = new FirstEditionSiderealPartEncoder(resources, registry, ESSENCE_MAX);
    IPdfPartEncoder secondEditionEncoder = new SecondEditionSiderealPartEncoder(resources, registry, ESSENCE_MAX);
    registry.setPartEncoder(CharacterType.SIDEREAL, ExaltedEdition.FirstEdition, firstEditionEncoder);
    registry.setPartEncoder(CharacterType.SIDEREAL, ExaltedEdition.SecondEdition, secondEditionEncoder);
  }*/
}