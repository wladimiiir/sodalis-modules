package sk.magiksoft.sodalis.customer

import java.util.ResourceBundle
import javax.swing.ImageIcon

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.category.{CategoryManager, CategoryModule}
import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.controlpanel.impl.NoteInfoPanel
import sk.magiksoft.sodalis.core.data.DataListener
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.customer.entity.CustomerData
import sk.magiksoft.sodalis.icon.IconManager
import sk.magiksoft.sodalis.person.PersonModule
import sk.magiksoft.sodalis.person.data.SexDynamicCategory
import sk.magiksoft.sodalis.person.ui.PersonalDataInfoPanel

/**
 * @author wladimiiir 
 * @since 2/7/15
 */
@VisibleModule
class CustomerModule extends AbstractModule with PersonModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.customer.locale.customer"
  private lazy val moduleDescriptor = new ModuleDescriptor(IconManager.getInstance.getIcon("customers").asInstanceOf[ImageIcon],
    ResourceBundle.getBundle(bundleBaseName).getString("customers"))

  override def getModuleDescriptor: ModuleDescriptor = moduleDescriptor

  override def getDataListener: DataListener = CustomerContextManager

  override def getContextManager: ContextManager = CustomerContextManager

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/customer/data/mapping/customer.hbm.xml"))
  }

  override def startUp(): Unit = {
    initDynamicCategories()

    LocaleManager.registerBundleBaseName(bundleBaseName)

    ControlPanelRegistry.registerInfoPanels(classOf[CustomerData].getName, List(
      classOf[PersonalDataInfoPanel],
      classOf[NoteInfoPanel],
      classOf[HistoryInfoPanel]
    ))
  }

  private def initDynamicCategories() = {
    val rootCategory = CategoryManager.getInstance().getRootCategory(classOf[CategoryModule], false)
    registerDynamicCategory(new SexDynamicCategory(rootCategory))
  }
}
