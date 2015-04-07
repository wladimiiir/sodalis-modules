package sk.magiksoft.sodalis.customer

import java.util.ResourceBundle
import javax.swing.ImageIcon

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.controlpanel.impl.NoteInfoPanel
import sk.magiksoft.sodalis.core.data.{DataListener, DBManager}
import sk.magiksoft.sodalis.core.factory.EntityFactory
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{VisibleModule, ModuleDescriptor}
import sk.magiksoft.sodalis.customer.entity.CustomerData
import sk.magiksoft.sodalis.icon.IconManager
import sk.magiksoft.sodalis.person.AbstractPersonModule
import sk.magiksoft.sodalis.person.entity.{Person, PersonHistoryData, PrivatePersonData}
import sk.magiksoft.sodalis.person.ui.PersonalDataInfoPanel

/**
 * @author wladimiiir 
 * @since 2/7/15
 */
@VisibleModule
class CustomerModule extends AbstractPersonModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.customer.locale.customer"
  private lazy val moduleDescriptor = new ModuleDescriptor(IconManager.getInstance.getIcon("customers").asInstanceOf[ImageIcon],
    ResourceBundle.getBundle(bundleBaseName).getString("customers"))

  override def getModuleDescriptor: ModuleDescriptor = moduleDescriptor

  override def getDataListener: DataListener = CustomerContextManager

  override def getContextManager: ContextManager = CustomerContextManager

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/customer/data/mapping/customer.hbm.xml"))
  }

  override def getDynamicCategories: List[Category] = List()

  override def registerDynamicCategory(dynamicCategory: Category): Unit = {}

  override def prepareDB(dbManager: DBManager): Unit = {
    dbManager.createDBSchema(this)
  }

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)

    EntityFactory.getInstance().registerEntityProperties(classOf[Person], classOf[PrivatePersonData], classOf[PersonHistoryData])
    ControlPanelRegistry.registerInfoPanels(classOf[CustomerData].getName, List(
      classOf[PersonalDataInfoPanel],
      classOf[NoteInfoPanel],
      classOf[HistoryInfoPanel]
    ))
  }
}
