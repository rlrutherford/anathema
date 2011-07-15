package net.sf.anathema.character.alchemical;

import net.sf.anathema.character.generic.backgrounds.IBackgroundTemplate;
import net.sf.anathema.character.generic.framework.ICharacterGenerics;
import net.sf.anathema.character.generic.framework.module.NullObjectCharacterModuleAdapter;
import net.sf.anathema.character.generic.impl.backgrounds.CharacterTypeBackgroundTemplate;
import net.sf.anathema.character.generic.impl.caste.CasteCollection;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.character.alchemical.caste.AlchemicalCaste;
import net.sf.anathema.lib.registry.IIdentificateRegistry;

public class AlchemicalCharacterModule extends NullObjectCharacterModuleAdapter {
  
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
    //CharmCache charmProvider = CharmCache.getInstance();
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
    //IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry = characterGenerics.getAdditionalModelFactoryRegistry();
    //IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry = characterGenerics.getAdditionalViewFactoryRegistry();
    //IRegistry<String, IAdditionalPersisterFactory> persisterFactory = characterGenerics.getAdditonalPersisterFactoryRegistry();
    //registerSiderealColleges(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
    //registerFlawedFate(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
    //registerParadox(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
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