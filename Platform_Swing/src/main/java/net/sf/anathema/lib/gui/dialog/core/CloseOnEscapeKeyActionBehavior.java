package net.sf.anathema.lib.gui.dialog.core;

import com.google.common.base.Preconditions;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public final class CloseOnEscapeKeyActionBehavior {

  public static void attachTo(final AbstractDialog dialog) {
    Preconditions.checkNotNull(dialog);
    KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    JRootPane rootPane = dialog.getDialog().getRootPane();
    ActionListener listener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dialog.performCancel();
      }
    };
    rootPane.registerKeyboardAction(listener, keyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
  }
}