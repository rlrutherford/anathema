package net.sf.anathema.character.alchemical.slots.view;

import javax.swing.Icon;
import javax.swing.JPanel;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;

public interface ISlotView
{
	public void addContent(JPanel panel, int index);
	
	public void addObjectValueChangedListener(IObjectValueChangedListener<ICharm> listener);
	
	public ICharm getSelectionValue();
	
	public void setSelectionValue(ICharm charm);
	
	public void setCharms(ICharm[] charms);
	
	public void setToggleEnabled(boolean enabled);
	
	public void setRemoveEnabled(boolean enabled);
	
	public void setIcon(Icon icon);
	
	public void remove();
	
	public boolean isNull();
}
