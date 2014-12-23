package sk.magiksoft.sodalis.folkensemble.inventory

import java.util.ResourceBundle

import entity.{InventoryHistoryData, BorrowingInventoryItemData, InventoryItem}
import sk.magiksoft.sodalis.core.data.DBManager
import sk.magiksoft.sodalis.core.module.{VisibleModule, AbstractModule, ModuleDescriptor}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.icon.IconManager
import collection.JavaConversions._
import sk.magiksoft.sodalis.folkensemble.inventory.data.BorrowerDynamicCategory
import sk.magiksoft.sodalis.core.factory.EntityFactory

/**
 * @author wladimiiir
 * @since 2010/5/20
 */

@VisibleModule
class InventoryModule extends AbstractModule {
  private lazy val moduleDescriptor = new ModuleDescriptor(
    new ImageIcon(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/icon/inventory.png")),
    LocaleManager.getString("inventory")
  )
  private lazy val dynamicCategories = createDynamicCategories

  private def createDynamicCategories = List(new BorrowerDynamicCategory)


  override def startUp(): Unit = {
    EntityFactory.getInstance.registerEntityProperties(classOf[InventoryItem], classOf[BorrowingInventoryItemData], classOf[InventoryHistoryData])
    LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.item.locale.item")
    LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.locale.inventory")
  }

  def getDataListener = InventoryContextManager

  def getContextManager = InventoryContextManager

  def getModuleDescriptor = moduleDescriptor

  override def getDynamicCategories = {
    dynamicCategories.foreach {
      _.refresh()
    }
    super.getDynamicCategories ++ dynamicCategories
  }

  override def registerDBResources(manager: DBManager): Unit = {
    manager.getConfiguration.addURL(getClass.getResource("/sk/magiksoft/sodalis/item/data/mapping/item.hbm.xml"))
    manager.getConfiguration.addURL(getClass.getResource("data/mapping/inventory.hbm.xml"))
  }
}
