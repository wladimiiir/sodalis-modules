package sk.magiksoft.sodalis.customer.action

import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.person.action.RemovePersonAbstractAction
import sk.magiksoft.sodalis.person.entity.Person

/**
 * @author wladimiiir 
 * @since 3/25/15
 */
class RemoveCustomerAction extends RemovePersonAbstractAction {
  override protected def filterPersons: (Person) => Boolean = _ => true

  override protected def getDeleteConfirmMultipleSelectionMessage: String = LocaleManager.getString("removeCustomersConfirm")

  override protected def getNoSelectionMessage: String = LocaleManager.getString("noCustomerSelected")

  override protected def getSingleSelectionMessage: String = LocaleManager.getString("removeCustomer")

  override protected def getMultipleSelectionMessage: String = LocaleManager.getString("removeCustomers")

  override protected def getDeleteConfirmSingleSelectionMessage: String = LocaleManager.getString("removeCustomerConfirm")

  override protected def setupDeletedPerson(person: Person): Unit = {
  }
}
