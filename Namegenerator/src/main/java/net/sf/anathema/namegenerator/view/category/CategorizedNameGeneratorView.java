package net.sf.anathema.namegenerator.view.category;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class CategorizedNameGeneratorView implements ICategorizedNameGeneratorView {

  private JComboBox[] comboBoxes;
  private JPanel content;

  @Override
  public void initGui(Object[] categories, int columnCount, ListCellRenderer renderer) {
    content = new JPanel(new MigLayout(new LC().wrapAfter(columnCount)));
    comboBoxes = new JComboBox[columnCount];
    for (int index = 0; index < comboBoxes.length; index++) {
      comboBoxes[index] = new JComboBox<>(categories);
      comboBoxes[index].setRenderer(renderer);
      content.add(comboBoxes[index]);
    }
  }

  @Override
  public JComponent getComponent() {
    return content;
  }

  @Override
  public Object[] getSelectedCategories() {
    Object[] categories = new Object[comboBoxes.length];
    for (int index = 0; index < categories.length; index++) {
      categories[index] = comboBoxes[index].getSelectedItem();
    }
    return categories;
  }
}