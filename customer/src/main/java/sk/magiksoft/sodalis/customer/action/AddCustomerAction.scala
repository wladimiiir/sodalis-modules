package sk.magiksoft.sodalis.customer.action

import java.awt.event.ActionEvent
import java.util

import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.core.controlpanel.{DefaultControlPanel, InfoPanel}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.customer.entity.CustomerData
import sk.magiksoft.sodalis.icon.IconManager
import sk.magiksoft.sodalis.person.action.AddPersonAbstractAction
import sk.magiksoft.sodalis.person.ui.AbstractPersonContext
import scala.collection.JavaConversions._

/**
 * @author wladimiiir 
 * @since 3/24/15
 */
class AddCustomerAction(personContext: AbstractPersonContext) extends AddPersonAbstractAction(personContext) {
  override protected def getActionName: String = LocaleManager.getString("addCustomer")

  override protected def createInfoPanels: Array[InfoPanel] = new DefaultControlPanel(classOf[CustomerData].getName)
    .getAllInfoPanels
    .filter(_.isWizardSupported).toArray

  override protected def getWizardTitle: String = LocaleManager.getString("addCustomer")
}
