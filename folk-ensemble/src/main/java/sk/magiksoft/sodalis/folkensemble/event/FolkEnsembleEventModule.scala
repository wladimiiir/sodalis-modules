package sk.magiksoft.sodalis.folkensemble.event

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.data.{DBManager, DataListener}
import sk.magiksoft.sodalis.core.factory.EntityFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module._
import sk.magiksoft.sodalis.event.EventModule
import sk.magiksoft.sodalis.event.entity.Event
import sk.magiksoft.sodalis.folkensemble.event.entity.EnsembleEventData

/**
 * @author wladimiiir 
 * @since 2014/12/22
 */
@VisibleModule
@OverridesModule(classOf[EventModule])
class FolkEnsembleEventModule extends EventModule {
  override def initConfiguration(configuration: Configuration): Unit = {
    super.initConfiguration(configuration)
    configuration.addURL(getClass.getResource("data/mapping/ensemble_event.hbm.xml"))
  }

  override def startUp(): Unit = {
    super.startUp()
    LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.locale.event")
    EntityFactory.getInstance.registerEntityProperties(classOf[Event], classOf[EnsembleEventData])
  }

  override def install(classLoader: ClassLoader, dbManager: DBManager): Unit = {
    dbManager.createDBSchema(this)
  }
}
