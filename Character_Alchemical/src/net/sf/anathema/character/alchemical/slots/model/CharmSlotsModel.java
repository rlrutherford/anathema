package net.sf.anathema.character.alchemical.slots.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.alchemical.slots.CharmSlotsTemplate;
import net.sf.anathema.character.generic.additionaltemplate.AbstractAdditionalModelAdapter;
import net.sf.anathema.character.generic.additionaltemplate.AdditionalModelType;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.ICharacterChangeListener;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.lib.control.change.IChangeListener;

public class CharmSlotsModel extends AbstractAdditionalModelAdapter implements ICharmSlotsModel
{
	private final CharmSlotsTemplate template;
	private final ICharacterModelContext context;
	private final List<CharmSlot> slots = new ArrayList<CharmSlot>();
	private int numGeneric = 0;
	private int numDedicated = 0;
	
	public CharmSlotsModel(
			CharmSlotsTemplate template,
			ICharacterModelContext context)
	{
		this.template = template;
		this.context = context;
		
		for (int i = 0; i != template.getGeneric() + template.getDedicated(); i++)
			slots.add(new CharmSlot(null, i < template.getGeneric(), true));
		
		numGeneric = template.getGeneric();
		numDedicated = template.getDedicated();
	}

	@Override
	public void addChangeListener(IChangeListener listener) {
		// TODO Auto-generated method stub
		
	}
	
	public void addCharacterChangeListener(ICharacterChangeListener listener)
	{
		context.getCharacterListening().addChangeListener(listener);
	}
	
	public CharmSlot addNewCharmSlot(boolean generic)
	{
		CharmSlot newSlot = new CharmSlot(null, generic,
				context.getBasicCharacterContext().isExperienced());
		if (generic)
			slots.add(numGeneric++ - 1, newSlot);
		else
			slots.add(numGeneric + numDedicated++ - 1, newSlot);
		
		return newSlot;
	}

	@Override
	public AdditionalModelType getAdditionalModelType() {
		return AdditionalModelType.Magic;
	}

	@Override
	public String getTemplateId() {
		return CharmSlotsTemplate.TEMPLATE_ID;
	}
	
	public int getGenericSlotCount()
	{
		return numGeneric;
	}
	
	public int getDedicatedSlotCount()
	{
		return numDedicated;
	}
	
	public CharmSlot[] getCharmSlots()
	{
		CharmSlot[] array = new CharmSlot[slots.size()];
		slots.toArray(array);
		return array;
	}
	
	public void validateSlots()
	{
		for (CharmSlot slot : slots)
			if (!isLearned(slot.getCharm()))
				slot.setCharm(null);
	}
	
	private boolean isLearned(ICharm charm)
	{
		if (charm == null)
			return false;
		for (ICharm otherCharm : context.getCharmContext().getCharmConfiguration().getLearnedCharms())
			if (otherCharm == charm)
				return true;
		return false;
	}
	
	public ICharm[] getValidCharms(CharmSlot slot)
	{
		List<ICharm> charms = new ArrayList<ICharm>();
		charms.add(null);
		for (ICharm charm : context.getCharmContext().getCharmConfiguration().getLearnedCharms())
		{
			boolean isValid = true;
			for (CharmSlot otherSlots : slots)
				if (otherSlots != slot && charm == otherSlots.getCharm())
				{
					isValid = false;
					break;
				}
			if (!isValid)
				continue;
			charms.add(charm);
		}
		ICharm[] array = new ICharm[charms.size()];
		charms.toArray(array);
		return array;
	}

}
