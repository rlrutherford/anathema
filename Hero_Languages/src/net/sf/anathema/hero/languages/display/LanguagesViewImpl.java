package net.sf.anathema.hero.languages.display;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import net.sf.anathema.character.main.library.overview.IOverviewCategory;
import net.sf.anathema.character.main.library.overview.OverviewCategory;
import net.sf.anathema.framework.swing.IView;
import net.sf.anathema.lib.file.RelativePath;
import net.sf.anathema.lib.gui.AgnosticUIConfiguration;
import net.sf.anathema.lib.gui.icon.ImageProvider;
import net.sf.anathema.lib.gui.ui.ConfigurableListCellRenderer;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import static net.sf.anathema.lib.gui.layout.LayoutUtils.withoutInsets;

public class LanguagesViewImpl implements IView, LanguagesView {

  private final JPanel selectionPanel = new JPanel(new MigLayout(withoutInsets().fillX()));
  private final JPanel entryPanel = new JPanel(new MigLayout(withoutInsets().wrapAfter(2).fillX()));
  private final JPanel mainPanel = new JPanel(new MigLayout(withoutInsets().wrapAfter(1)));
  private final JPanel overviewPanel = new JPanel(new MigLayout(withoutInsets()));
  private final JPanel panel = new JPanel(new MigLayout(withoutInsets()));

  @Override
  public JComponent getComponent() {
    mainPanel.add(selectionPanel);
    mainPanel.add(new JScrollPane(entryPanel), new CC().grow().pushY());
    panel.add(mainPanel, new CC().grow().pushY());
    panel.add(overviewPanel, new CC().alignY("top"));
    return panel;
  }

  @Override
  public IRemovableEntryView addEntryView(RelativePath removeIcon, String string) {
    RemovableStringView view = new RemovableStringView(new ImageProvider().getImageIcon(removeIcon), string);
    view.addContent(entryPanel);
    panel.revalidate();
    return view;
  }

  @Override
  public void removeEntryView(IRemovableEntryView removableView) {
    removableView.delete();
    entryPanel.repaint();
  }

  @SuppressWarnings("unchecked")
  @Override
  public ObjectSelectionViewWithTool<Object> addSelectionView(String labelText, AgnosticUIConfiguration uiConfiguration) {
    ListCellRenderer renderer = new ConfigurableListCellRenderer(uiConfiguration);
    SwingSelectionViewWithTool objectSelectionView = new SwingSelectionViewWithTool(renderer, labelText);
    objectSelectionView.addComponents(selectionPanel);
    return objectSelectionView;
  }

  @Override
  public IOverviewCategory addOverview(String border) {
    return new OverviewCategory(overviewPanel, border, false);
  }
}