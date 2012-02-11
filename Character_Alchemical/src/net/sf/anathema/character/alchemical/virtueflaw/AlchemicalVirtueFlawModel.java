package net.sf.anathema.character.alchemical.virtueflaw;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.generic.framework.additionaltemplate.listening.ICharacterChangeListener;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IExtendedCharmData;
import net.sf.anathema.character.generic.template.additional.IAdditionalTemplate;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.generic.traits.types.VirtueType;
import net.sf.anathema.character.library.trait.visitor.IDefaultTrait;
import net.sf.anathema.character.library.virtueflaw.model.IVirtueFlaw;
import net.sf.anathema.character.library.virtueflaw.model.VirtueFlawModel;

public class AlchemicalVirtueFlawModel extends VirtueFlawModel
{
	private final AlchemicalVirtueFlaw virtueFlaw;
	private final ICharacterModelContext context;

	public AlchemicalVirtueFlawModel(ICharacterModelContext context,
			IAdditionalTemplate additionalTemplate) {
		super(context, additionalTemplate);
		this.context = context;
		virtueFlaw = new AlchemicalVirtueFlaw(context);
		
		context.getCharacterListening().addChangeListener(new ICharacterChangeListener()
		{

			@Override
			public void casteChanged() {
			}

			@Override
			public void characterChanged() {
				computeExemplar();
			}

			@Override
			public void experiencedChanged(boolean experienced) {
			}

			@Override
			public void traitChanged(ITraitType type) {
			}
			
		});
	}
	
	public IVirtueFlaw getVirtueFlaw() {
	    return virtueFlaw;
	  }

	  public ITraitType[] getFlawVirtueTypes() {
		    List<ITraitType> flawVirtues = new ArrayList<ITraitType>();
		    for (VirtueType virtueType : VirtueType.values()) {
		      if (getContext().getTraitCollection().getTrait(virtueType).getCurrentValue() > 2) {
		        flawVirtues.add(virtueType);
		      }
		    }
		    return flawVirtues.toArray(new ITraitType[0]);
		  }
	  
	  private void computeExemplar()
	  {
		  IDefaultTrait trait = virtueFlaw.getLimitTrait();
		  int currentClarity = trait.getCurrentValue();
		  int currentMinimum = trait.getAbsoluteMinValue();
		  int newMinimum = 0;
		  //TODO: Slotted Exemplar charms
		  /*for (ICharm charm : context.getCharmContext().getCharmConfiguration().getLearnedCharms())
		  {
			  int exemplar = 0;
			  if (charm.hasAttribute(IExtendedCharmData.EXEMPLAR_1_ATTRIBUTE)) exemplar = 1;
			  if (charm.hasAttribute(IExtendedCharmData.EXEMPLAR_2_ATTRIBUTE)) exemplar = 2;
			  if (charm.hasAttribute(IExtendedCharmData.EXEMPLAR_3_ATTRIBUTE)) exemplar = 3;
			  if (charm.hasAttribute(IExtendedCharmData.EXEMPLAR_4_ATTRIBUTE)) exemplar = 4;
			  if (charm.hasAttribute(IExtendedCharmData.EXEMPLAR_5_ATTRIBUTE)) exemplar = 5;
			  newMinimum += exemplar;
		  }*/
		  int adjustment = newMinimum - currentMinimum;
		  virtueFlaw.setClarityMinimum(newMinimum);
		  trait.setCurrentValue(Math.min(trait.getMaximalValue(),
				  currentClarity + adjustment));
		  
	  }
}
