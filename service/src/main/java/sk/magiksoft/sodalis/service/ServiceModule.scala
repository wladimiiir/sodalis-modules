package sk.magiksoft.sodalis.service

import java.util.ResourceBundle

import entity.property.ServicePropertyTranslator
import entity.Service
import sk.magiksoft.sodalis.core.data.DBManager
import sk.magiksoft.sodalis.core.module.{VisibleModule, AbstractModule, ModuleDescriptor}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.icon.IconManager

/**
 * @author wladimiiir
 * @since 2011/3/10
 */
@VisibleModule
class ServiceModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.service.locale.service"
  private lazy val moduleDescriptor = new ModuleDescriptor(IconManager.getInstance.getIcon("services").asInstanceOf[ImageIcon],
    ResourceBundle.getBundle(bundleBaseName).getString("services"))

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
    EntityPropertyTranslatorManager.registerTranslator(classOf[Service], new ServicePropertyTranslator)
    IconManager.getInstance().registerIcons(getClass.getResource("icon/icon.properties"))
  }

  def getDataListener = ServiceContextManager

  def getContextManager = ServiceContextManager

  def getModuleDescriptor = moduleDescriptor

  override def registerDBResources(manager: DBManager): Unit = {
    manager.getConfiguration.addURL(getClass.getResource("data/mapping/service.hbm.xml")
  }
}
