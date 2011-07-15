package net.sf.anathema.character.alchemical.slots.model;

import net.sf.anathema.character.generic.framework.additionaltemplate.listening.ICharacterChangeListener;
import net.sf.anathema.character.generic.magic.ICharm;

public interface ICharmSlotsModel
{
	public void addCharacterChangeListener(ICharacterChangeListener listener);
	
	public CharmSlot addNewCharmSlot(boolean generic);
	
	public CharmSlot[] getCharmSlots();
	
	public void validateSlots();
	
	public int getGenericSlotCount();
	
	public int getDedicatedSlotCount();
	
	public ICharm[] getValidCharms(CharmSlot slot);
}
