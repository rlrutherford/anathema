package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.generic.framework.additionaltemplate.listening.ICharacterChangeListener;
import net.sf.anathema.character.generic.magic.ICharm;

public interface ICharmSlotsModel
{
	public void addCharacterChangeListener(ICharacterChangeListener listener);
	
	public CharmSlot addNewCharmSlot(boolean generic);
	
	public void restoreSlot(CharmSlot slot);
	
	public CharmSlot[] getCharmSlots();
	
	public void validateSlots();
	
	public int getGenericSlotCount();
	
	public int getDedicatedSlotCount();
	
	public ICharm[] getValidCharms(CharmSlot slot);
	
	public ICharm getCharmByName(String name);
	
	public boolean allowToggle(CharmSlot slot);
	
	public boolean allowRemoval(CharmSlot slot);
	
	public void toggleSlot(CharmSlot slot);
	
	public void removeSlot(CharmSlot slot);
	
	public void clearSlots();
	
	public int getAttunedMotes();
	
	public String getMaxPersonalMotes();
	
	public void fireChange();
}
