package net.sf.anathema.character.presenter.magic.thaumaturgy;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.anathema.character.generic.framework.magic.view.AbstractMagicLearnProperties;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.generic.magic.IThaumaturgyVisitor;
import net.sf.anathema.character.model.ICharacterStatistics;
import net.sf.anathema.character.model.IThaumaturgyConfiguration;
import net.sf.anathema.character.view.magic.IThaumaturgyViewProperties;
import net.sf.anathema.framework.presenter.view.IdentificateListCellRenderer;
import net.sf.anathema.lib.gui.list.LegalityCheckListCellRenderer;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.Identificate;

public class ThaumaturgyViewProperties extends AbstractMagicLearnProperties implements IThaumaturgyViewProperties {

  private final IThaumaturgyConfiguration thaumaturgyConfiguration;
  private final ICharacterStatistics statistics;

  public ThaumaturgyViewProperties(IResources resources, ICharacterStatistics statistics) {
    super(resources);
    this.statistics = statistics;
    this.thaumaturgyConfiguration = statistics.getThaumaturgy();
  }

  public String getCircleString() {
    return getResources().getString("CardView.CharmConfiguration.Spells.Circle"); //$NON-NLS-1$
  }

  public String getLearnedSpellString() {
    return getResources().getString("CardView.CharmConfiguration.Spells.LearnedSpells"); //$NON-NLS-1$
  }

  public ListCellRenderer getAvailableMagicRenderer() {
    return new LegalityCheckListCellRenderer(getResources()) {
      private static final long serialVersionUID = 7840060419690645799L;

      @Override
      public Component getListCellRendererComponent(JList list, final Object value,
  			int index, boolean isSelected, boolean cellHasFocus) {
    	  final Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    	  ((IThaumaturgy)value).visitThaumaturgy(new IThaumaturgyVisitor() {

			@Override
			public void visitDegree(IThaumaturgy degree) {
				((JLabel)component).setText(getResources().getString(((IThaumaturgy)value).getRank().name() + "Degree"));
			}

			@Override
			public void visitProcedure(IThaumaturgy procedure) {
				// nothing to do
			}
    		  
    	  });
    	  return component;
    	  
      }
      
      @Override
      protected boolean isLegal(Object object) {
        return thaumaturgyConfiguration.isThaumaturgyAllowed((IThaumaturgy) object);
      }
    };
  }

  public ListCellRenderer getArtSelectionRenderer() {
    return new IdentificateListCellRenderer(getResources());
  }

  public boolean isMagicSelectionAvailable(Object selection) {
    return selection != null && thaumaturgyConfiguration.isThaumaturgyAllowed((IThaumaturgy) selection);
  }

  public int getAvailableListSelectionMode() {
    return ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
  }

  public String getAddButtonToolTip() {
    return getResources().getString("CardView.CharmConfiguration.Spells.AddToolTip"); //$NON-NLS-1$  
  }

  public String getRemoveButtonToolTip() {
    return getResources().getString("CardView.CharmConfiguration.Spells.RemoveToolTip"); //$NON-NLS-1$
  }

  public ListSelectionListener getRemoveButtonEnabledListener(final JButton button, final JList list) {
    return new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        boolean enabled = !list.isSelectionEmpty();
        if (enabled && statistics.isExperienced()) {
          for (Object thaumaturgyObject : list.getSelectedValues()) {
            IThaumaturgy thaumaturgy = (IThaumaturgy) thaumaturgyObject;
            if (thaumaturgyConfiguration.isLearnedOnCreation(thaumaturgy)) {
              enabled = false;
              break;
            }
          }
        }
        button.setEnabled(enabled);
      }
    };
  }

  public String getDetailTitle() {
    return getResources().getString("CardView.CharmConfiguration.Spells.Details.Title"); //$NON-NLS-1$;
  }

  public String getCostString() {
    return getResources().getString("CardView.CharmConfiguration.Spells.Cost"); //$NON-NLS-1$
  }

  public String getSourceString() {
    return getResources().getString("CardView.CharmConfiguration.Spells.Source"); //$NON-NLS-1$
  }

  public String getSelectionTitle() {
    return getResources().getString("CardView.CharmConfiguration.Spells.Selection.Title"); //$NON-NLS-1$
  }

  public String getTargetString() {
    return getResources().getString("CardView.CharmConfiguration.Spells.Target"); //$NON-NLS-1$
  }

  public String getUndefinedString() {
    return getResources().getString("CardView.CharmConfiguration.Spells.Target.Undefined"); //$NON-NLS-1$
  }
}
