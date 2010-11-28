package net.sf.anathema.character.equipment.item;

import java.awt.Component;

import net.disy.commons.core.message.IMessage;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.dialog.message.MessageUserDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.buttons.AbstractDialogButtonConfiguration;
import net.sf.anathema.lib.gui.list.veto.IVetor;
import net.sf.anathema.lib.gui.wizard.workflow.ICondition;
import net.sf.anathema.lib.resources.IResources;

public class DiscardChangesVetor implements IVetor {

  private final ICondition preCondition;
  private final Component parentComponent;
  private final IResources resources;

  public DiscardChangesVetor(IResources resources, ICondition preCondition, Component parentComponent) {
    this.resources = resources;
    this.preCondition = preCondition;
    this.parentComponent = parentComponent;
  }

  public boolean vetos() {
    if (!preCondition.isFullfilled()) {
      return false;
    }
    String messageText = resources.getString("Equipment.Creation.UnsavedChangesMessage.Text"); //$NON-NLS-1$
    IMessage message = new Message(messageText, MessageType.WARNING);
    MessageUserDialogConfiguration configuration = new MessageUserDialogConfiguration(
        message,
        new AbstractDialogButtonConfiguration() {
          @Override
          public String getOkayButtonText() {
            return resources.getString("Equipment.Creation.UnsavedChangesMessage.OKButton"); //$NON-NLS-1$
          }
        });
    UserDialog userDialog = new UserDialog(parentComponent, configuration);
    userDialog.show();
    return userDialog.isCanceled();
  }
}