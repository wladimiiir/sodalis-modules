package sk.magiksoft.sodalis.service

import java.util.ResourceBundle
import javax.swing.ImageIcon

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.category.ui.CategoryInfoPanel
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.icon.IconManager
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.service.entity.Service
import sk.magiksoft.sodalis.service.entity.property.ServicePropertyTranslator
import sk.magiksoft.sodalis.service.ui.{CategorizedServiceInfoPanel, PersonServiceInfoPanel, ServiceInfoPanel}

/**
 * @author wladimiiir
 * @since 2011/3/10
 */
@VisibleModule
class ServiceModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.service.locale.service"
  private lazy val moduleDescriptor = new ModuleDescriptor(
    IconManager.getInstance.getIcon("services").asInstanceOf[ImageIcon],
    ResourceBundle.getBundle(bundleBaseName).getString("services")
  )

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
    EntityPropertyTranslatorManager.registerTranslator(classOf[Service], new ServicePropertyTranslator)
    IconManager.getInstance().registerIcons(getClass.getResource("icon/icons.properties"))

    ControlPanelRegistry.registerInfoPanels(classOf[Service].getName, List(
      classOf[ServiceInfoPanel],
      classOf[CategorizedServiceInfoPanel]
    ))
    ControlPanelRegistry.registerInfoPanels(classOf[Person].getName, List(
      classOf[PersonServiceInfoPanel]
    ))

  }

  def getDataListener = ServiceContextManager

  def getContextManager = ServiceContextManager

  def getModuleDescriptor = moduleDescriptor

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("data/mapping/service.hbm.xml"))
  }
}
