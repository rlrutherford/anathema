package net.sf.anathema.character.impl.view;

import net.sf.anathema.character.library.intvalue.IIconToggleButtonProperties;
import net.sf.anathema.character.library.intvalue.IToggleButtonTraitView;
import net.sf.anathema.character.library.trait.Trait;
import net.sf.anathema.character.library.trait.view.GroupedTraitView;
import net.sf.anathema.character.library.trait.view.SimpleTraitView;
import net.sf.anathema.character.view.IGroupedFavorableTraitConfigurationView;
import net.sf.anathema.framework.swing.IView;
import net.sf.anathema.framework.value.IntegerViewFactory;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class GroupedFavorableTraitConfigurationView implements IGroupedFavorableTraitConfigurationView, IView {

  private GroupedTraitView groupedTraitView;
  private final IntegerViewFactory markerIntValueDisplayFactory;
  private final JComponent parent = new JPanel();

  public GroupedFavorableTraitConfigurationView(IntegerViewFactory factoryWithMarker) {
    this.markerIntValueDisplayFactory = factoryWithMarker;
  }

  @Override
  public void initGui(int columnCount) {
    this.groupedTraitView = new GroupedTraitView(parent, columnCount);
  }

  @Override
  public JComponent getComponent() {
    return parent;
  }

  @Override
  public IToggleButtonTraitView<SimpleTraitView> addTraitView(String labelText, int value, int maxValue, Trait trait, boolean selected,
                                                              IIconToggleButtonProperties properties) {
    return groupedTraitView.addTraitView(labelText, value, maxValue, trait, selected, properties, markerIntValueDisplayFactory);
  }

  @Override
  public void startNewTraitGroup(String groupLabel) {
    groupedTraitView.startNewGroup(groupLabel);
  }
}