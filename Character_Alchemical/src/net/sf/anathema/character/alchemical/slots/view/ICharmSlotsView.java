package net.sf.anathema.character.alchemical.slots.view;

import javax.swing.ListCellRenderer;

import net.disy.commons.swing.action.SmartAction;
import net.sf.anathema.character.alchemical.slots.model.CharmSlot;
import net.sf.anathema.character.generic.magic.IGenericCombo;
import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.gui.selection.ObjectSelectionView;

public interface ICharmSlotsView extends IView
{
	public ISlotView addCharmSlotView(int index, CharmSlot slot, ListCellRenderer renderer, SmartAction toggleAction, SmartAction removeAction, boolean canRemove);
	
	public void createControlPanel(
			SmartAction newGenericAction,
			SmartAction newDedicatedAction,
			ObjectSelectionView<IGenericCombo> comboSelection,
			SmartAction addComboAction);
	
	public void updateSlotView(ISlotView view, CharmSlot slot);
	
	public void updateAttunementString(int attuned, String personal);
}
