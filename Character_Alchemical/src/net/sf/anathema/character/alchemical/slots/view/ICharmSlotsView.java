package net.sf.anathema.character.alchemical.slots.view;

import javax.swing.ListCellRenderer;

import net.disy.commons.swing.action.SmartAction;
import net.sf.anathema.character.alchemical.slots.model.CharmSlot;
import net.sf.anathema.lib.gui.IView;

public interface ICharmSlotsView extends IView
{
	public ISlotView addCharmSlotView(int index, CharmSlot slot, ListCellRenderer renderer);
	
	public void createAddSlotsPanel(SmartAction newGenericAction, SmartAction newDedicatedAction);
}
