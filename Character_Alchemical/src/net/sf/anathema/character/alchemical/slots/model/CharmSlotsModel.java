package net.sf.anathema.character.alchemical.slots.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.alchemical.AlchemicalUtilities;
import net.sf.anathema.character.alchemical.slots.CharmSlotsTemplate;
import net.sf.anathema.character.generic.additionaltemplate.AdditionalModelType;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModel;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModelBonusPointCalculator;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModelExperienceCalculator;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.ICharacterChangeListener;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IGenericCombo;
import net.sf.anathema.character.generic.magic.charms.ICharmAttribute;
import net.sf.anathema.character.generic.magic.charms.ICharmAttributeRequirement;
import net.sf.anathema.character.generic.traits.IFavorableGenericTrait;
import net.sf.anathema.lib.control.change.ChangeControl;
import net.sf.anathema.lib.control.change.IChangeListener;

public class CharmSlotsModel implements ICharmSlotsModel, IAdditionalModel
{
	private final static String NO_REMOVAL = "NoRemoval";
	private final ChangeControl control = new ChangeControl();
	private final ICharacterModelContext context;
	private final List<CharmSlot> slots = new ArrayList<CharmSlot>();
	private final List<IGenericCombo> installedCombos = new ArrayList<IGenericCombo>();
	private int numGeneric = 0;
	private int numDedicated = 0;
	
	private IComboSlotChangeListener changeListener =
		new IComboSlotChangeListener()
		{
			@Override
			public void comboSlotChange(IGenericCombo combo) {
				removeCombo(combo);
			}
		};
	
	public CharmSlotsModel(
			CharmSlotsTemplate template,
			ICharacterModelContext context)
	{
		this.context = context;
		
		for (int i = 0; i != template.getGeneric() + template.getDedicated(); i++)
		{
			SlotState state = i < template.getGeneric() ? SlotState.Generic : SlotState.Dedicated;
			slots.add(new CharmSlot(null, null, state, state, true));
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
		CharmSlot newSlot = new CharmSlot(null, null, creationState, experiencedState, false);
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
			if (!canSlot(slot.getCharm(), slot))
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
	
	public boolean allowChange(CharmSlot slot)
	{
		if (slot.getCharm() == null)
			return true;
		
		for (ICharmAttribute attribute : slot.getCharm().getAttributes())
			if (attribute.getId().equals(NO_REMOVAL))
				return false;
		return true;
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
	
	public CharmPick[] getValidPicks(CharmSlot slot)
	{
		List<CharmPick> charms = new ArrayList<CharmPick>();
		charms.add(null);
		if (slot.getCombo() != null && meetsComboPrereqs(slot.getCombo()))
			charms.add(slot.getPick());
		for (ICharm charm : context.getCharmContext().getCharmConfiguration().getLearnedCharms())
		{
			boolean isValid = true;
			if (slot.getCombo() == null)
			{
				int allowedInstallations = getAllowedInstallations(charm);
				for (CharmSlot otherSlots : slots)
					if (otherSlots != slot && charm == otherSlots.getCharm() && otherSlots.getCombo() == null &&
						--allowedInstallations == 0)
					{
						isValid = false;
						break;
					}
			}
			if (!isValid)
				continue;
			if (canSlot(charm, slot))
				charms.add(new CharmPick(charm, null, null));
		}
		CharmPick[] array = new CharmPick[charms.size()];
		charms.toArray(array);
		return array;
	}
	
	private int getAllowedInstallations(ICharm charm)
	{
		return context.getMagicCollection().getLearnCount(charm.getId());
	}
	
	public boolean canSlot(ICharm charm, CharmSlot slot)
	{
		if (!isLearned(charm) && slot.getCombo() == null)
			return false;
		for (ICharm prereq : charm.getParentCharms())
			if (!isSlotted(prereq))
				return false;
		for (ICharmAttributeRequirement prereq : charm.getAttributeRequirements())
			if (!isSlotted(prereq, slot.getCombo()))
				return false;
		return true;
	}
	
	public boolean isSlotted(ICharm charm)
	{
		for (CharmSlot slot : slots)
			if (slot.getCharm() == charm)
				return true;
		return false;
	}
	
	public boolean isSlotted(ICharmAttributeRequirement charmRequirement, IGenericCombo combo)
	{
		List<ICharm> slottedCharms = new ArrayList<ICharm>();
		for (CharmSlot slot : slots)
			if (slot.getCharm() != null)
				slottedCharms.add(slot.getCharm());
		if (combo != null)
			for (ICharm charm : combo.getCharms())
				slottedCharms.add(charm);
		ICharm[] charmArray = new ICharm[slottedCharms.size()];
		slottedCharms.toArray(charmArray);
		return charmRequirement.isFulfilled(charmArray);
	}

	@Override
	public IAdditionalModelBonusPointCalculator getBonusPointCalculator() {
		return new CharmSlotBonusPointCalculator(this);
	}

	@Override
	public IAdditionalModelExperienceCalculator getExperienceCalculator() {
		return new CharmSlotExperiencePointCalculator(this);
	}
	
	public int getAttunedMotes()
	{
		int total = 0;
		for (CharmSlot slot : slots)
			total += slot.getCharm() != null  && slot.getCombo() == null
				? AlchemicalUtilities.getAttunementCost(slot.getCharm()) : 0;
		for (IGenericCombo combo : installedCombos)
		{
			int totalCombo = 0;
			for (ICharm charm : combo.getCharms())
				totalCombo += AlchemicalUtilities.getAttunementCost(charm);
			total += Math.ceil(.75 * totalCombo);
		}
		return total;
	}
	
	public String getMaxPersonalMotes()
	{
		//TODO: Get personal motes
		return "0";
		//return context.getPersonalPool();
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
	
	private CharmSlot getFreeGeneralSlot()
	{
		for (CharmSlot slot : slots)
			if (slot.isGeneric() && slot.getCharm() == null)
				return slot;
		return null;
	}
	
	private CharmSlot getFreeDedicatedSlot()
	{
		for (CharmSlot slot : slots)
			if (!slot.isGeneric() && slot.getCharm() == null)
				return slot;
		return null;
	}
	
	public IGenericCombo[] getAvaliableCombos()
	{
		List<IGenericCombo> combos = new ArrayList<IGenericCombo>();
		//TODO: Get Combos
		/*for (IGenericCombo combo : context.getCombos())
			if (hasSlotsForCombo(combo) && meetsComboPrereqs(combo))
				combos.add(combo);*/
		combos.removeAll(installedCombos);
		IGenericCombo[] comboArray = new IGenericCombo[combos.size()];
		combos.toArray(comboArray);
		return comboArray;
	}
	
	private boolean meetsComboPrereqs(IGenericCombo combo)
	{
		for (ICharm charm : combo.getCharms())
		{
			for (ICharm prereq : charm.getParentCharms())
				if (!isSlotted(prereq))
					return false;
			for (ICharmAttributeRequirement prereq : charm.getAttributeRequirements())
				if (!isSlotted(prereq, combo))
					return false;
		}
		return true;
	}
	
	private boolean hasSlotsForCombo(IGenericCombo combo)
	{
		int generalNeeded = 0;
		int dedicatedNeeded = 0;
		int generalAvaliable = 0;
		int dedicatedAvaliable = 0;
		
		for (CharmSlot slot : slots)
			if (slot.getCharm() == null)
			{
				if (slot.isGeneric())
					generalAvaliable++;
				else
					dedicatedAvaliable = 0;
			}
		
		for (ICharm charm : combo.getCharms())
		{
			if (context.getTraitCollection().getFavorableTrait(
					charm.getPrimaryTraitType()).isCasteOrFavored())
			{
				if (dedicatedNeeded == dedicatedAvaliable)
					generalNeeded++;
				else
					dedicatedNeeded++;
			}
			else
				generalNeeded++;
			
			if (generalNeeded > generalAvaliable)
				return false;
		}
		return true;
				
	}

	public void installCombo(IGenericCombo combo)
	{
		for (ICharm charm : combo.getCharms())
		{
			IFavorableGenericTrait trait = context.getTraitCollection().getFavorableTrait(charm.getPrimaryTraitType());
			CharmSlot slot = null;
			if (trait.isCasteOrFavored())
			{
				slot = getFreeDedicatedSlot();
				slot = slot == null ? getFreeGeneralSlot() : slot;
			}
			else
				slot = getFreeGeneralSlot();
			slot.setCharm(charm);
			slot.setCombo(combo, changeListener);
		}
		installedCombos.add(combo);
	}
	
	public void removeCombo(IGenericCombo combo)
	{
		for (CharmSlot slot : slots)
			if (slot.getCombo() == combo)
			{
				slot.setCombo(null, null);
				slot.setCharm(null);
			}
		installedCombos.remove(combo);
		fireChange();
	}
	
}
