package net.sf.anathema.character.alchemical;

import net.sf.anathema.character.generic.backgrounds.IBackgroundTemplate;
import net.sf.anathema.character.generic.framework.ICharacterGenerics;
import net.sf.anathema.character.generic.framework.additionaltemplate.IAdditionalViewFactory;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.IAdditionalModelFactory;
import net.sf.anathema.character.generic.framework.additionaltemplate.persistence.IAdditionalPersisterFactory;
import net.sf.anathema.character.generic.framework.module.CharacterModule;
import net.sf.anathema.character.generic.framework.module.NullObjectCharacterModuleAdapter;
import net.sf.anathema.character.generic.impl.backgrounds.CharacterTypeBackgroundTemplate;
import net.sf.anathema.character.generic.impl.caste.CasteCollection;
import net.sf.anathema.character.generic.impl.rules.ExaltedEdition;
import net.sf.anathema.character.generic.impl.traits.EssenceTemplate;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.character.library.virtueflaw.persistence.DefaultVirtueFlawPersisterFactory;
import net.sf.anathema.character.reporting.CharacterReportingModule;
import net.sf.anathema.character.reporting.CharacterReportingModuleObject;
import net.sf.anathema.character.alchemical.caste.AlchemicalCaste;
import net.sf.anathema.character.alchemical.reporting.SecondEditionAlchemicalPartEncoder;
import net.sf.anathema.character.alchemical.slots.CharmSlotsTemplate;
import net.sf.anathema.character.alchemical.slots.CharmSlotsTemplateParser;
import net.sf.anathema.character.alchemical.slots.model.CharmSlotsModelFactory;
import net.sf.anathema.character.alchemical.slots.persistence.CharmSlotsPersisterFactory;
import net.sf.anathema.character.alchemical.slots.view.CharmSlotsViewFactory;
import net.sf.anathema.character.alchemical.virtueflaw.AlchemicalVirtueFlawModelFactory;
import net.sf.anathema.character.alchemical.virtueflaw.AlchemicalVirtueFlawParser;
import net.sf.anathema.character.alchemical.virtueflaw.AlchemicalVirtueFlawTemplate;
import net.sf.anathema.character.alchemical.virtueflaw.AlchemicalVirtueFlawViewFactory;
import net.sf.anathema.lib.registry.IIdentificateRegistry;
import net.sf.anathema.lib.registry.IRegistry;
import net.sf.anathema.lib.resources.IResources;

@CharacterModule
public class AlchemicalCharacterModule extends NullObjectCharacterModuleAdapter {
  
  public static final String BACKGROUND_ID_CLASS = "Class"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_COMMAND = "Command"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_EIDOLIN = "Eidolin"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_SAVANT = "AlchemicalSavant"; //$NON-NLS-1$
  
  @Override
  public void registerCommonData(ICharacterGenerics characterGenerics)
  {
	    characterGenerics.getAdditionalTemplateParserRegistry().register(
	            CharmSlotsTemplate.TEMPLATE_ID,
	            new CharmSlotsTemplateParser());
	    characterGenerics.getAdditionalTemplateParserRegistry().register(
	            AlchemicalVirtueFlawTemplate.TEMPLATE_ID,
	            new AlchemicalVirtueFlawParser());
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
    IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry = characterGenerics.getAdditionalModelFactoryRegistry();
    IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry = characterGenerics.getAdditionalViewFactoryRegistry();
    IRegistry<String, IAdditionalPersisterFactory> persisterFactory = characterGenerics.getAdditonalPersisterFactoryRegistry();
    registerCharmSlots(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
    registerVirtueFlaw(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
  }
  
  private void registerCharmSlots(
	      IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry,
	      IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry,
	      IRegistry<String, IAdditionalPersisterFactory> persisterFactory) {
	    String templateId = CharmSlotsTemplate.TEMPLATE_ID;
	    additionalModelFactoryRegistry.register(templateId, new CharmSlotsModelFactory());
	    additionalViewFactoryRegistry.register(templateId, new CharmSlotsViewFactory());
	    persisterFactory.register(templateId, new CharmSlotsPersisterFactory());
	  }
  
  private void registerVirtueFlaw(
	      IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry,
	      IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry,
	      IRegistry<String, IAdditionalPersisterFactory> persisterFactory) {
	    String templateId = AlchemicalVirtueFlawTemplate.TEMPLATE_ID;
	    additionalModelFactoryRegistry.register(templateId, new AlchemicalVirtueFlawModelFactory());
	    additionalViewFactoryRegistry.register(templateId, new AlchemicalVirtueFlawViewFactory());
	    persisterFactory.register(templateId, new DefaultVirtueFlawPersisterFactory());
	  }
  
  @Override
  public void addReportTemplates(ICharacterGenerics generics, IResources resources)
  {
	//TODO: Printing
    /*CharacterReportingModuleObject moduleObject = generics.getModuleObjectMap().getModuleObject(
        CharacterReportingModule.class);
    PdfEncodingRegistry registry = moduleObject.getPdfEncodingRegistry();
    IPdfPartEncoder secondEditionEncoder = new SecondEditionAlchemicalPartEncoder(resources, registry, EssenceTemplate.SYSTEM_ESSENCE_MAX);
    registry.setPartEncoder(CharacterType.ALCHEMICAL, ExaltedEdition.SecondEdition, secondEditionEncoder);*/
  }
}