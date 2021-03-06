package sk.magiksoft.sodalis.form.action

import java.awt.event.ActionEvent
import java.util

import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.form.FormDataManager
import sk.magiksoft.sodalis.form.entity.Form
import sk.magiksoft.sodalis.form.ui.FormInfoPanel
import sk.magiksoft.sodalis.icon.IconManager

import scala.swing.Swing

/**
 * @author wladimiiir
 * @since 2010/8/6
 */

class AddFormAction extends MessageAction(null, IconManager.getInstance.getIcon("add")) {
  private var formDialog: Option[OkCancelDialog] = None
  private var formInfoPanel: Option[FormInfoPanel] = None

  def getActionMessage(objects: util.List[_]) = new ActionMessage(true, LocaleManager.getString("addForm"))

  def actionPerformed(e: ActionEvent) = {
    val form = new Form()

    formInfoPanel match {
      case Some(infoPanel) => {
        infoPanel.initLayout()
        infoPanel.setupPanel(form)
        infoPanel.initData
      }
      case None => {
        val dialog: OkCancelDialog = new OkCancelDialog(SodalisApplication.get.getMainFrame)
        val infoPanel: FormInfoPanel = new FormInfoPanel

        dialog.setMainPanel(infoPanel)
        dialog.setSize(400, 300)
        dialog.setModal(true)
        dialog.setTitle(LocaleManager.getString("addForm"))
        dialog.setLocationRelativeTo(SodalisApplication.get().getMainFrame)
        dialog.getOkButton.addActionListener(Swing.ActionListener(e => {
          infoPanel.setupObject(form)
          FormDataManager.addDatabaseEntity(form)
        }))
        UIUtils.makeISDialog(dialog)

        formInfoPanel = Option(infoPanel)
        formDialog = Option(dialog)
        infoPanel.initLayout()
      }
    }

    formDialog.get.setVisible(true)
  }
}
