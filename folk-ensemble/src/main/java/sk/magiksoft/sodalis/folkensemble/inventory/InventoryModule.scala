package sk.magiksoft.sodalis.folkensemble.inventory

import javax.swing.ImageIcon

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.core.factory.EntityFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.folkensemble.inventory.data.BorrowerDynamicCategory
import sk.magiksoft.sodalis.folkensemble.inventory.entity.{BorrowingInventoryItemData, InventoryHistoryData, InventoryItem}

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

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/item/data/mapping/item.hbm.xml"))
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/inventory/data/mapping/inventory.hbm.xml"))
  }
}
