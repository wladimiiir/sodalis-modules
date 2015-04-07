package sk.magiksoft.sodalis.folkensemble.event

import org.hibernate.cfg.Configuration
import org.hibernate.internal.util.ClassLoaderHelper
import org.hibernate.tool.hbm2ddl.{Target, SchemaUpdate}
import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.data.{DBManager, DataListener}
import sk.magiksoft.sodalis.core.factory.EntityFactory
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module._
import sk.magiksoft.sodalis.event.EventModule
import sk.magiksoft.sodalis.event.data.EventDataManager
import sk.magiksoft.sodalis.event.entity.Event
import sk.magiksoft.sodalis.event.ui.{CategorizedEventInfoPanel, EventInfoPanel}
import sk.magiksoft.sodalis.folkensemble.event.entity.EnsembleEventData
import sk.magiksoft.sodalis.folkensemble.event.ui.{ProgrammeInfoPanel, ParticipantsInfoPanel}
import scala.collection.JavaConversions._

/**
 * @author wladimiiir 
 * @since 2014/12/22
 */
@VisibleModule
@OverridesModule(classOf[EventModule])
class FolkEnsembleEventModule extends EventModule {
  override def initConfiguration(configuration: Configuration): Unit = {
    super.initConfiguration(configuration)
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/event/data/mapping/ensemble_event.hbm.xml"))
  }

  override def startUp(): Unit = {
    super.startUp()
    LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.locale.event")
    EntityFactory.getInstance.registerEntityProperties(classOf[Event], classOf[EnsembleEventData])
    ControlPanelRegistry.registerInfoPanels(classOf[Event].getName, List(
      classOf[EventInfoPanel],
      classOf[ParticipantsInfoPanel],
      classOf[ProgrammeInfoPanel],
      classOf[CategorizedEventInfoPanel],
      classOf[HistoryInfoPanel]
    ))
  }

  override def install(dbManager: DBManager): Unit = {
    EventDataManager.getInstance().addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("/sk/magiksoft/sodalis/event/imex/event_types.xml")))
    EventDataManager.getInstance().addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("/sk/magiksoft/sodalis/folkensemble/event/imex/event_types.xml")))
  }
}
