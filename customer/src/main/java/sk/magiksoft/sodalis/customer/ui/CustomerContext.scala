package sk.magiksoft.sodalis.customer.ui

import java.awt.event.ActionEvent
import javax.swing._

import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.controlpanel.DefaultControlPanel
import sk.magiksoft.sodalis.core.entity.Entity
import sk.magiksoft.sodalis.core.registry.RegistryManager
import sk.magiksoft.sodalis.customer.action.{RemoveCustomerAction, AddCustomerAction}
import sk.magiksoft.sodalis.customer.entity.CustomerData
import sk.magiksoft.sodalis.customer.{CustomerModule, CustomerContextManager, CustomerSettings}
import sk.magiksoft.sodalis.person.ui.AbstractPersonContext
import java.util.{List => jList}
import scala.collection.JavaConversions._

/**
 * @author wladimiiir 
 * @since 2/7/15
 */
class CustomerContext extends AbstractPersonContext(CustomerContextManager, new CustomerTableModel) {
  def getSettings = CustomerSettings

  protected def createControlPanel = Option(new DefaultControlPanel(classOf[CustomerData].getName))

  protected def getModule = SodalisApplication.get.getModuleManager.getModuleByClass(classOf[CustomerModule])

  protected def createRemovePersonAction = new RemoveCustomerAction

  protected def createAddPersonAction = new AddCustomerAction(this)

  override def preparePopupMenu(entities: jList[_ <: Entity]) {
    val popupMenu = this.popupMenu match {
      case menu: JPopupMenu =>
        menu.removeAll()
        menu
      case _ =>
        this.popupMenu = new JPopupMenu
        this.popupMenu
    }
    val entityList = entities.toList
    val actions = RegistryManager.getPopupActions(entityList)
    for (action <- actions) {
      popupMenu.add(new AbstractAction(action.getName(entityList)) {
        def actionPerformed(e: ActionEvent) {
          action(entityList)
        }
      })
    }
  }
}
