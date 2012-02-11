package net.sf.anathema.character.alchemical.slots.view;

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import net.disy.commons.swing.action.SmartAction;
import net.sf.anathema.character.alchemical.slots.model.CharmPick;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;
import net.sf.anathema.lib.gui.selection.ObjectSelectionView;

public class SlotView implements ISlotView
{
	private final ObjectSelectionView<CharmPick> selection;
	private final JButton typeButton;
	private final JButton removeButton;
	private final JLabel dummy = new JLabel();
	private boolean canRemove;
	private boolean isChanging = false;
	private JPanel contentPanel;
	
	public SlotView(ListCellRenderer renderer,
			Icon toggleIcon,
			SmartAction toggleAction,
			Icon removeIcon,
			SmartAction removeAction,
			boolean canRemove)
	{
		selection = new ObjectSelectionView<CharmPick>("", renderer);
		selection.getComboBox().setPreferredSize(new Dimension(400, 20));
		this.typeButton = new JButton();
		this.typeButton.setPreferredSize(new Dimension(24, 24));
		this.typeButton.setAction(toggleAction);
		this.removeButton = new JButton();
		this.removeButton.setPreferredSize(new Dimension(24, 24));
		this.removeButton.setAction(removeAction);
		this.removeButton.setIcon(removeIcon);
		this.dummy.setPreferredSize(new Dimension(24, 24));
		
		setIcon(toggleIcon);
		setRemoveEnabled(canRemove);		
	}
	
	public void addContent(JPanel panel, int position) {
	   this.contentPanel = panel;
	   panel.add(typeButton, 3 * position);
	   panel.add(selection.getComboBox(), 3 * position + 1);
	   panel.add(canRemove ? removeButton : dummy, 3 * position + 2);
	   panel.revalidate();
	}
	
	public void setRemoveEnabled(boolean canRemove)
	{
		this.canRemove = canRemove;
		
		if (contentPanel == null) return;
			
		contentPanel.remove(removeButton);
		contentPanel.remove(dummy);
		int index = contentPanel.getComponentZOrder(selection.getComboBox()) + 1;
		
		if (canRemove)
			contentPanel.add(removeButton, index);
		else
			contentPanel.add(dummy, index);
	}
	
	public void remove()
	{
		contentPanel.remove(typeButton);
		contentPanel.remove(selection.getComboBox());
		contentPanel.remove(removeButton);
		contentPanel.remove(dummy);
		contentPanel.invalidate();
		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	public void setIcon(Icon icon)
	{
		this.typeButton.setIcon(icon);
		this.typeButton.setDisabledIcon(icon);
	}
	
	public void setToggleEnabled(boolean enabled)
	{
		typeButton.setEnabled(enabled);
	}
	
	public void setChangeEnabled(boolean enabled)
	{
		selection.setEnabled(enabled);
	}
	
	public CharmPick getSelectionValue()
	{
		return selection.getSelectedObject();
	}
	
	public void setSelectionValue(CharmPick pick)
	{
		selection.setSelectedObject(pick);
	}
	
	public void setPicks(CharmPick[] picks)
	{
		isChanging = true;
		selection.setObjects(picks);
		isChanging = false;
	}
	
	public boolean isNull()
	{
		return (getSelectionValue() == null) && !isChanging;
	}
	
	public boolean isChanging()
	{
		return isChanging;
	}
	
	public void addObjectValueChangedListener(IObjectValueChangedListener<CharmPick> listener)
	{
		selection.addObjectSelectionChangedListener(listener);
	}
}
