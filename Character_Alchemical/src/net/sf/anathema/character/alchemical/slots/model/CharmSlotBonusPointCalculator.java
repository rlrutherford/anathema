package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModelBonusPointCalculator;

public class CharmSlotBonusPointCalculator implements IAdditionalModelBonusPointCalculator
{
	ICharmSlotsModel model;
	int cost = 0;
	
	public CharmSlotBonusPointCalculator(ICharmSlotsModel model)
	{
		this.model = model;
	}

	@Override
	public int getBonusPointCost()
	{
		return cost;
	}

	@Override
	public int getBonusPointsGranted() {
		return 0;
	}

	@Override
	public void recalculate()
	{
		cost = 0;
		for (CharmSlot slot : model.getCharmSlots())
			if (!slot.isFixed())
				cost += slot.isGeneric() ? 4 : 3;
	}

}
