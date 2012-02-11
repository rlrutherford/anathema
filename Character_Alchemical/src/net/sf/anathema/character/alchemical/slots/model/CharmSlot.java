package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IGenericCombo;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;

public class CharmSlot
{
	private ICharm charm;
	private IGenericCombo combo;
	private ISubeffect subeffect;
	private IComboSlotChangeListener listener;
	private final boolean isFixed;
	private SlotState creationState;
	private SlotState experiencedState;
	
	public CharmSlot(ICharm charm, ISubeffect effect, SlotState creationState, SlotState experiencedState, boolean isFixed)
	{
		this.creationState = creationState;
		this.experiencedState = experiencedState;
		this.isFixed = isFixed;
		this.charm = charm;
		this.subeffect = effect;
	}
	
	public ICharm getCharm()
	{
		return charm;
	}
	
	public ISubeffect getEffect()
	{
		return subeffect;
	}
	
	public IGenericCombo getCombo()
	{
		return combo;
	}
	
	public boolean setCharm(ICharm charm)
	{
		return setCharm(charm, null);
	}
	
	public boolean setCharm(ICharm charm, ISubeffect effect)
	{
		if (this.charm == charm) return false;
		if (combo != null)
		{
			listener.comboSlotChange(combo);
			combo = null;
			listener = null;
		}
		this.charm = charm;
		this.subeffect = effect;
		return true;
	}
	
	public void setCombo(IGenericCombo combo, IComboSlotChangeListener listener)
	{
		this.combo = combo;
		this.listener = listener;
	}
	
	public boolean setPick(CharmPick pick)
	{
		return setCharm(pick == null ? null : pick.getCharm());
	}
	
	public boolean isGeneric()
	{
		return experiencedState == SlotState.Generic ||
				creationState == SlotState.Generic;
	}
	
	public SlotState getCreationState()
	{
		return creationState;
	}
	
	public SlotState getExperiencedState()
	{
		return experiencedState;
	}
	
	public void setCreationState(SlotState state)
	{
		creationState = state;
	}
	
	public void setExperiencedState(SlotState state)
	{
		experiencedState = state;
	}
	
	public boolean isCreationLearned()
	{
		return creationState != SlotState.Absent;
	}
	
	public boolean isFixed()
	{
		return isFixed;
	}
	
	public int getXPCost()
	{
		switch (creationState)
		{
		case Absent:
			if (experiencedState == SlotState.Dedicated) return 4;
			if (experiencedState == SlotState.Generic) return 6;
			break;
		case Dedicated:
			if (experiencedState == SlotState.Generic) return 2;
			break;
		}
		return 0;
	}
	
	public CharmPick getPick()
	{
		return new CharmPick(charm, combo, subeffect);
	}
	
	public String toString()
	{
		return "[" + charm + ";" + creationState + ";" + experiencedState + ";" + isFixed + "]";
	}
}
