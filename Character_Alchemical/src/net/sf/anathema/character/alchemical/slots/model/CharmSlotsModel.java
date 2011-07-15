package net.sf.anathema.character.alchemical.slots.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.alchemical.slots.CharmSlotsTemplate;
import net.sf.anathema.character.generic.additionaltemplate.AdditionalModelType;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModel;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModelBonusPointCalculator;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModelExperienceCalculator;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.ICharacterChangeListener;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.lib.control.change.ChangeControl;
import net.sf.anathema.lib.control.change.IChangeListener;

public class CharmSlotsModel implements ICharmSlotsModel, IAdditionalModel
{
	private final ChangeControl control = new ChangeControl();
	private final ICharacterModelContext context;
	private final List<CharmSlot> slots = new ArrayList<CharmSlot>();
	private int numGeneric = 0;
	private int numDedicated = 0;
	
	public CharmSlotsModel(
			CharmSlotsTemplate template,
			ICharacterModelContext context)
	{
		this.context = context;
		
		for (int i = 0; i != template.getGeneric() + template.getDedicated(); i++)
		{
			SlotState state = i < template.getGeneric() ? SlotState.Generic : SlotState.Dedicated;
			slots.add(new CharmSlot(null, state, state, true));
		}
		
		numGeneric = template.getGeneric();
		numDedicated = template.getDedicated();
	}

	@Override
	public void addChangeListener(IChangeListener listener) {
		control.addChangeListener(listener);
	}
	
	public void addCharacterChangeListener(ICharacterChangeListener listener)
	{
		context.getCharacterListening().addChangeListener(listener);
	}
	
	public void fireChange()
	{
		control.fireChangedEvent();
	}
	
	public void clearSlots()
	{
		slots.clear();
		numGeneric = 0;
		numDedicated = 0;
	}
	
	public CharmSlot addNewCharmSlot(boolean generic)
	{
		boolean experienced = context.getBasicCharacterContext().isExperienced();
		SlotState creationState = experienced ? SlotState.Absent :
			(generic ? SlotState.Generic : SlotState.Dedicated);
		SlotState experiencedState = experienced ? (generic ? SlotState.Generic : SlotState.Dedicated) :
			SlotState.Absent;
		CharmSlot newSlot = new CharmSlot(null, creationState, experiencedState, false);
		if (generic)
			slots.add(numGeneric++ - 1, newSlot);
		else
			slots.add(numGeneric + numDedicated++ - 1, newSlot);
		
		return newSlot;
	}
	
	public void restoreSlot(CharmSlot slot)
	{
		slots.add(slot);
		if (slot.isGeneric())
			numGeneric++;
		else
			numDedicated++;
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
	
	public boolean allowToggle(CharmSlot slot)
	{
		if (context.getBasicCharacterContext().isExperienced())
		{
			return slot.getCreationState() != SlotState.Generic;
		}
		return !slot.isFixed();			
	}
	
	public boolean allowRemoval(CharmSlot slot)
	{
		if (context.getBasicCharacterContext().isExperienced())
			return !slot.isFixed() && slot.getCreationState() == SlotState.Absent;
		return !slot.isFixed();
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
	
	public void toggleSlot(CharmSlot slot)
	{
		if (slot.isGeneric())
		{
			numGeneric--;
			numDedicated++;
		}
		else
		{
			numGeneric++;
			numDedicated--;
		}
		
		if (context.getBasicCharacterContext().isExperienced())
			slot.setExperiencedState(slot.getExperiencedState() == SlotState.Dedicated ?
						SlotState.Generic : SlotState.Dedicated);
		else
			slot.setCreationState(slot.isGeneric() ? SlotState.Dedicated : SlotState.Generic);
		
	}
	
	public ICharm getCharmByName(String name)
	{
		for (ICharm charm : context.getCharmContext().getCharmConfiguration().getLearnedCharms())
			if (charm.getId().equals(name))
				return charm;
		return null;
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

	@Override
	public IAdditionalModelBonusPointCalculator getBonusPointCalculator() {
		return new CharmSlotBonusPointCalculator(this);
	}

	@Override
	public IAdditionalModelExperienceCalculator getExperienceCalculator() {
		return new CharmSlotExperiencePointCalculator(this);
	}

	@Override
	public void removeSlot(CharmSlot slot)
	{
		if (slot.isGeneric())
			numGeneric--;
		else
			numDedicated--;
		
		slots.remove(slot);
	}

}
