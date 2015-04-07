package sk.magiksoft.sodalis.customer

import sk.magiksoft.sodalis.core.context.{Context, AbstractContextManager}
import sk.magiksoft.sodalis.core.data.remote.server.DataManager
import sk.magiksoft.sodalis.customer.data.CustomerDataManager
import sk.magiksoft.sodalis.customer.entity.CustomerData
import sk.magiksoft.sodalis.customer.ui.CustomerContext
import sk.magiksoft.sodalis.person.entity.Person

/**
 * @author wladimiiir 
 * @since 2/7/15
 */
object CustomerContextManager extends AbstractContextManager {
  override protected def isFullTextActive: Boolean = true

  override protected def getDefaultQuery: String = "from " + classOf[Person].getName

  override protected def getDataManager: DataManager = CustomerDataManager

  override protected def createContext(): Context = new CustomerContext
}
