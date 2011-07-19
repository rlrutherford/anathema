package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IGenericCombo;

public class CharmSlot
{
	private ICharm charm;
	private IGenericCombo combo;
	private IComboSlotChangeListener listener;
	private final boolean isFixed;
	private SlotState creationState;
	private SlotState experiencedState;
	
	public CharmSlot(ICharm charm, SlotState creationState, SlotState experiencedState, boolean isFixed)
	{
		this.creationState = creationState;
		this.experiencedState = experiencedState;
		this.isFixed = isFixed;
		this.charm = charm;
	}
	
	public ICharm getCharm()
	{
		return charm;
	}
	
	public IGenericCombo getCombo()
	{
		return combo;
	}
	
	public boolean setCharm(ICharm charm)
	{
		if (this.charm == charm) return false;
		if (combo != null)
		{
			listener.comboSlotChange(combo);
			combo = null;
			listener = null;
		}
		this.charm = charm;
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
		return new CharmPick(charm, combo);
	}
	
	public String toString()
	{
		return "[" + charm + ";" + creationState + ";" + experiencedState + ";" + isFixed + "]";
	}
}
