package sk.magiksoft.sodalis.folkensemble.inventory

import java.net.URL

import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.context.AbstractContextManager
import sk.magiksoft.sodalis.core.utils.Utils
import sk.magiksoft.sodalis.folkensemble.inventory.data.InventoryDataManager
import sk.magiksoft.sodalis.folkensemble.inventory.ui.InventoryItemContext
import sk.magiksoft.sodalis.item.entity.Item
import sk.magiksoft.sodalis.item.factory.ItemPropertiesFactory

/**
 * @author wladimiiir
 * @since 2010/5/20
 */

object InventoryContextManager extends AbstractContextManager {
  def getDataManager = InventoryDataManager.getInstance

  def getDefaultQuery = "from " + classOf[Item].getName

  def isFullTextActive = false

  override def getFilterConfigFileURL: URL = getClass.getResource("filter/InventoryFilter.xml")

  def createContext = {
    val propertiesFactory: Option[ItemPropertiesFactory] = (SodalisApplication.get.getLicenseManager.getLicense.isRestricted("ItemDefinitionPanel")
      && !SodalisApplication.get.getLicenseManager.getLicense.isDebugMode) match {
      case true => None
      case false => Option(new ItemPropertiesFactory(getClass.getResource("config/InventoryItemProperties.xml")))
    }
    new InventoryItemContext(propertiesFactory)
  }
}
