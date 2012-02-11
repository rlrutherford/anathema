package net.sf.anathema.character.alchemical.slots.view;

import javax.swing.Icon;
import javax.swing.JPanel;

import net.sf.anathema.character.alchemical.slots.model.CharmPick;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;

public interface ISlotView
{
	public void addContent(JPanel panel, int index);
	
	public void addObjectValueChangedListener(IObjectValueChangedListener<CharmPick> listener);
	
	public CharmPick getSelectionValue();
	
	public void setSelectionValue(CharmPick charm);
	
	public void setPicks(CharmPick[] charms);
	
	public void setToggleEnabled(boolean enabled);
	
	public void setRemoveEnabled(boolean enabled);
	
	public void setChangeEnabled(boolean enabled);
	
	public void setIcon(Icon icon);
	
	public boolean isChanging();
	
	public void remove();
	
	public boolean isNull();
}
