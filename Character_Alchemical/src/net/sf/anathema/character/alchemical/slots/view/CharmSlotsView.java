package net.sf.anathema.character.alchemical.slots.view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.layout.GridDialogLayoutDataUtilities;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.sf.anathema.character.alchemical.slots.model.CharmSlot;

public class CharmSlotsView implements ICharmSlotsView
{
	private final ICharmSlotsViewProperties properties;
	private final JPanel mainPanel = new JPanel(new GridDialogLayout(1, false));
	private final JPanel entryPanel = new JPanel(new GridDialogLayout(2, false));
	private List<ISlotView> views = new ArrayList<ISlotView>();
	private JPanel addSlotsPanel = new JPanel(new GridDialogLayout(2, false));
	
	public CharmSlotsView(ICharmSlotsViewProperties properties)
	{
		this.properties = properties;
	    GridDialogLayoutData entryData = GridDialogLayoutDataUtilities.createHorizontalFillNoGrab();
	    entryData.setVerticalAlignment(GridAlignment.FILL);
	    entryData.setGrabExcessVerticalSpace(true);
	    mainPanel.add(new JScrollPane(entryPanel), entryData);
	}
	
	@Override
	public ISlotView addCharmSlotView(int index, CharmSlot slot, ListCellRenderer renderer)
	{
		Icon icon = slot.isGeneric() ? properties.getGenericSlotIcon() : properties.getDedicatedSlotIcon();
		ISlotView view = new SlotView(renderer, icon);
		view.addContent(entryPanel, index);
		entryPanel.revalidate();
		entryPanel.repaint();
		return view;
	}
	
	public void createAddSlotsPanel(SmartAction newGenericAction, SmartAction newDedicatedAction)
	{
		JButton genericButton = new JButton();
		JButton dedicatedButton = new JButton();
		genericButton.setAction(newGenericAction);
		dedicatedButton.setAction(newDedicatedAction);
		genericButton.setIcon(properties.getNewGenericSlotIcon());
		dedicatedButton.setIcon(properties.getNewDedicatedSlotIcon());
		genericButton.setPreferredSize(new Dimension(24, 24));
		dedicatedButton.setPreferredSize(new Dimension(24, 24));
		addSlotsPanel.add(genericButton);
		addSlotsPanel.add(dedicatedButton);		
	}

	@Override
	public JComponent getComponent() {
		mainPanel.add(addSlotsPanel);
		return mainPanel;
	}

}
