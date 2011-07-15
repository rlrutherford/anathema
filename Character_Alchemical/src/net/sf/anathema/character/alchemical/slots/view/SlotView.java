package net.sf.anathema.character.alchemical.slots.view;

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.sf.anathema.character.alchemical.slots.model.CharmSlot;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.library.trait.view.GroupedTraitView;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;
import net.sf.anathema.lib.gui.selection.ObjectSelectionView;

public class SlotView implements ISlotView
{
	private final ObjectSelectionView<ICharm> selection;
	private final JButton button;
	private JPanel contentPanel;
	private Icon icon;
	
	public SlotView(ListCellRenderer renderer, Icon icon)
	{
		selection = new ObjectSelectionView<ICharm>("", renderer);
		selection.getComboBox().setPreferredSize(new Dimension(200, 20));
		this.button = new JButton(icon);
		this.button.setPreferredSize(new Dimension(24, 24));
		this.icon = icon;
	}
	
	public void addContent(JPanel panel, int position) {
	   this.contentPanel = panel;
	   panel.add(button, 2 * position);
	   panel.add(selection.getComboBox(), 2 * position + 1);
	   panel.revalidate();
	}
	
	public ICharm getSelectionValue()
	{
		return selection.getSelectedObject();
	}
	
	public void setCharms(ICharm[] charms)
	{
		selection.setObjects(charms);
	}
	
	public void addObjectValueChangedListener(IObjectValueChangedListener<ICharm> listener)
	{
		selection.addObjectSelectionChangedListener(listener);
	}
}
