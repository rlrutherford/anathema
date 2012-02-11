package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModelExperienceCalculator;

public class CharmSlotExperiencePointCalculator implements IAdditionalModelExperienceCalculator
{
	private final ICharmSlotsModel model;
	
	public CharmSlotExperiencePointCalculator(ICharmSlotsModel model)
	{
		this.model = model;
	}

	@Override
	public int calculateCost() {
		int total = 0;
		for (CharmSlot slot : model.getCharmSlots())
			total += slot.getXPCost();
		return total;
	}

	@Override
	public int calculateGain() {
		return 0;
	}

}
