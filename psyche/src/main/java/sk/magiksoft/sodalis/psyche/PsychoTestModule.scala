package sk.magiksoft.sodalis.psyche

import java.util.ResourceBundle

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}

/**
 * @author wladimiiir
 * @since 2011/5/13
 */
@VisibleModule
class PsychoTestModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.psyche.locale.psyche"
  private lazy val descriptor = new ModuleDescriptor(null, ResourceBundle.getBundle(bundleBaseName).getString("psychoTests"))

  override def getDataListener = PsycheContextManager

  override def getContextManager = PsycheContextManager


  override def getModuleDescriptor = descriptor

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
  }

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("data/mapping/psyche.hbm.xml"))
  }
}
