package sk.magiksoft.sodalis.folkensemble.event

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.data.DBManager
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module._
import sk.magiksoft.sodalis.event.EventModule
import sk.magiksoft.sodalis.event.data.EventDataManager
import sk.magiksoft.sodalis.event.entity.Event
import sk.magiksoft.sodalis.event.ui.{CategorizedEventInfoPanel, EventInfoPanel}
import sk.magiksoft.sodalis.folkensemble.event.ui.{ParticipantsInfoPanel, ProgrammeInfoPanel}

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
