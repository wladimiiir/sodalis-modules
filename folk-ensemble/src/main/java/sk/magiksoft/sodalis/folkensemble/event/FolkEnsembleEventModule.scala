package sk.magiksoft.sodalis.folkensemble.event

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.data.{DBManager, DataListener}
import sk.magiksoft.sodalis.core.factory.EntityFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{InvisibleModule, Module, ModuleDescriptor}
import sk.magiksoft.sodalis.event.entity.Event
import sk.magiksoft.sodalis.folkensemble.event.entity.EnsembleEventData

/**
 * @author wladimiiir 
 * @since 2014/12/22
 */
@InvisibleModule
class FolkEnsembleEventModule extends Module {
  override def getModuleDescriptor: ModuleDescriptor = null

  override def getDataListener: DataListener = null

  override def getContextManager: ContextManager = null

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/event/data/mapping/event.hbm.xml"))
    configuration.addURL(getClass.getResource("data/mapping/ensemble_event.hbm.xml"))
  }

  override def getDynamicCategories: List[Category] = List()

  override def registerDynamicCategory(dynamicCategory: Category): Unit = {}

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.locale.event")
    EntityFactory.getInstance.registerEntityProperties(classOf[Event], classOf[EnsembleEventData])
  }

  override def install(classLoader: ClassLoader, dbManager: DBManager): Unit = {
    dbManager.createDBSchema(this)
  }
}
