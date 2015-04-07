package sk.magiksoft.sodalis.folkensemble.inventory

import javax.swing.ImageIcon

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.data.DBManager
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.folkensemble.inventory.data.{BorrowerDynamicCategory, InventoryDataManager}
import sk.magiksoft.sodalis.folkensemble.inventory.entity.InventoryItem
import sk.magiksoft.sodalis.folkensemble.inventory.ui.{BorrowingInfoPanel, CategorizedInventoryInfoPanel}
import sk.magiksoft.sodalis.item.ui.ItemInfoPanel

import scala.collection.JavaConversions._

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
    LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.item.locale.item")
    LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.locale.inventory")
    ControlPanelRegistry.registerInfoPanels(classOf[InventoryItem].getName, List(
      classOf[ItemInfoPanel],
      classOf[CategorizedInventoryInfoPanel],
      classOf[BorrowingInfoPanel],
      classOf[HistoryInfoPanel]
    ))
  }

  def getDataListener = InventoryContextManager

  def getContextManager = InventoryContextManager

  def getModuleDescriptor = moduleDescriptor


  override def install(dBManager: DBManager): Unit = {
    InventoryDataManager.getInstance().addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("/sk/magiksoft/sodalis/folkensemble/inventory/imex/inventory_item_types.xml")))
  }

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
