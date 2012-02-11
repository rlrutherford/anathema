package net.sf.anathema.character.alchemical.slots.persistence;

import org.dom4j.Element;

import net.sf.anathema.character.alchemical.slots.model.CharmSlot;
import net.sf.anathema.character.alchemical.slots.model.ICharmSlotsModel;
import net.sf.anathema.character.alchemical.slots.model.SlotState;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModel;
import net.sf.anathema.character.generic.framework.additionaltemplate.persistence.IAdditionalPersister;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.lib.exception.PersistenceException;
import net.sf.anathema.lib.xml.ElementUtilities;

public class CharmSlotsPersister implements IAdditionalPersister
{
	private final static String TAG_CHARM_SLOTS = "charmSlots";
	private final static String TAG_SLOT = "slot";
	private final static String ATTRIB_CHARM = "charm";
	private final static String ATTRIB_CREATION_STATE = "creationState";
	private final static String ATTRIB_EXPERIENCED_STATE = "experiencedState";
	//private final static String ATTRIB_SUBEFFECT = "subeffect";
	private final static String ATTRIB_FIXED = "fixed";
	
	@Override
	public void save(Element parent, IAdditionalModel model)
	{
		ICharmSlotsModel slotsModel = (ICharmSlotsModel) model;
		Element slotsElement = parent.addElement(TAG_CHARM_SLOTS);
		for (CharmSlot slot : slotsModel.getCharmSlots())
		{
			Element slotElement = slotsElement.addElement(TAG_SLOT);
			
			if (slot.getCharm() != null)
				slotElement.addAttribute(ATTRIB_CHARM, slot.getCharm().getId());
			if (slot.isFixed())
				slotElement.addAttribute(ATTRIB_FIXED, "" + slot.isFixed());
			
			slotElement.addAttribute(ATTRIB_CREATION_STATE, slot.getCreationState().name());
			slotElement.addAttribute(ATTRIB_EXPERIENCED_STATE, slot.getExperiencedState().name());
		}
	}

	@Override
	public void load(Element parent, IAdditionalModel model)
			throws PersistenceException {
		ICharmSlotsModel slotsModel = (ICharmSlotsModel) model;
		slotsModel.clearSlots();
		Element slotsElement = parent.element(TAG_CHARM_SLOTS);
		for (Object slotObject : slotsElement.elements(TAG_SLOT))
		{
			Element slotElement = (Element)slotObject;
			
			ICharm charm = slotsModel.getCharmByName(slotElement.attributeValue(ATTRIB_CHARM, null));
			boolean fixed = ElementUtilities.getBooleanAttribute(slotElement, ATTRIB_FIXED, false);
			SlotState creationState = SlotState.valueOf(slotElement.attributeValue(ATTRIB_CREATION_STATE));
			SlotState experiencedState = SlotState.valueOf(slotElement.attributeValue(ATTRIB_EXPERIENCED_STATE));
			
			slotsModel.restoreSlot(new CharmSlot(charm, null, creationState, experiencedState, fixed));
		}
		
	}

}
