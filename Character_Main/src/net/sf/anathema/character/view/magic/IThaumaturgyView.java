package net.sf.anathema.character.view.magic;

import javax.swing.JLabel;

import net.sf.anathema.character.generic.framework.magic.view.IMagicLearnView;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;
import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.util.IIdentificate;
import net.sf.anathema.lib.workflow.labelledvalue.IValueView;

public interface IThaumaturgyView extends IView, IMagicLearnView {
	void initGui(IIdentificate[] arts);

	void addArtSelectionListener(IObjectValueChangedListener<IIdentificate> listener);

	IValueView<String> addDetailValueView(String label);

	JLabel addDetailTitleView();
}
