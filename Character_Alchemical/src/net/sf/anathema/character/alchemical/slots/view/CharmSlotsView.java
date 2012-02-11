package net.sf.anathema.character.alchemical.slots.view;

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataBuilder;
import net.sf.anathema.character.alchemical.slots.model.CharmSlot;
import net.sf.anathema.character.generic.magic.IGenericCombo;
import net.sf.anathema.lib.gui.selection.ObjectSelectionView;

public class CharmSlotsView implements ICharmSlotsView
{
	private final ICharmSlotsViewProperties properties;
	private final JPanel mainPanel = new JPanel(new GridDialogLayout(1, false));
	private final JPanel entryPanel = new JPanel(new GridDialogLayout(3, false));
	private ObjectSelectionView<IGenericCombo> comboSelection;
	private JPanel controlPanel = new JPanel(new GridDialogLayout(4, false));
	private JLabel statusLabel = new JLabel();
	
	public CharmSlotsView(ICharmSlotsViewProperties properties)
	{
		this.properties = properties;
	    GridDialogLayoutData entryData = new GridDialogLayoutDataBuilder().filledHorizontal().grabExcessHorizontalSpace().get();
	    entryData.setVerticalAlignment(GridAlignment.FILL);
	    entryData.setGrabExcessVerticalSpace(true);
	    mainPanel.add(statusLabel);
	    mainPanel.add(new JScrollPane(entryPanel), entryData);
	}
	
	@Override
	public ISlotView addCharmSlotView(int index, CharmSlot slot, ListCellRenderer renderer,
			SmartAction toggleAction, SmartAction removeAction,
			boolean canRemove)
	{
		Icon icon = getIconForSlot(slot);
		ISlotView view = new SlotView(renderer, icon, toggleAction,
				properties.getRemoveIcon(), removeAction, canRemove);
		view.addContent(entryPanel, index);
		entryPanel.revalidate();
		entryPanel.repaint();
		return view;
	}
	
	public void updateSlotView(ISlotView view, CharmSlot slot)
	{
		Icon icon = getIconForSlot(slot);
		view.setIcon(icon);
	}
	
	private Icon getIconForSlot(CharmSlot slot)
	{
		return slot.isGeneric() ? properties.getGenericSlotIcon() : properties.getDedicatedSlotIcon();
	}
	
	public void createControlPanel(
			SmartAction newGenericAction,
			SmartAction newDedicatedAction,
			ObjectSelectionView<IGenericCombo> comboSelection,
			SmartAction addComboAction)
	{
		JButton genericButton = new JButton();
		JButton dedicatedButton = new JButton();
		JButton comboButton = new JButton();
		genericButton.setAction(newGenericAction);
		dedicatedButton.setAction(newDedicatedAction);
		comboButton.setAction(addComboAction);
		genericButton.setIcon(properties.getNewGenericSlotIcon());
		dedicatedButton.setIcon(properties.getNewDedicatedSlotIcon());
		comboButton.setIcon(properties.getAddIcon());
		genericButton.setPreferredSize(new Dimension(24, 24));
		dedicatedButton.setPreferredSize(new Dimension(24, 24));
		comboButton.setPreferredSize(new Dimension(24, 24));
		comboSelection.getComboBox().setPreferredSize(new Dimension(200, 20));
		controlPanel.add(genericButton);
		controlPanel.add(dedicatedButton);
		controlPanel.add(comboSelection.getComboBox());
		controlPanel.add(comboButton);
	}

	@Override
	public JComponent getComponent() {
		mainPanel.add(controlPanel);
		return mainPanel;
	}

	@Override
	public void updateAttunementString(int attuned, String personal) {
		statusLabel.setText(properties.getAttunementString() + ": " + attuned
				+ "   " + properties.getPersonalString() + ": " + personal);
	}
	
	public void setComboChoices(IGenericCombo[] combos)
	{
		comboSelection.setObjects(combos);
	}

}
