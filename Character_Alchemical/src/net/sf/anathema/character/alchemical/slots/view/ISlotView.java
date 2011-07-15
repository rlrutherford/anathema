package net.sf.anathema.character.alchemical.slots.view;

import javax.swing.JPanel;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;

public interface ISlotView
{
	public void addContent(JPanel panel, int index);
	
	public void addObjectValueChangedListener(IObjectValueChangedListener<ICharm> listener);
	
	public ICharm getSelectionValue();
	
	public void setCharms(ICharm[] charms);
}
