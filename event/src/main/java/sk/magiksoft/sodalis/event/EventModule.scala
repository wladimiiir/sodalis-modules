package sk.magiksoft.sodalis.event

import java.util.ResourceBundle
import javax.swing.ImageIcon

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.event.entity.Event
import sk.magiksoft.sodalis.event.entity.property.EventPropertyTranslator
import sk.magiksoft.sodalis.event.ui.{CategorizedEventInfoPanel, EventInfoPanel}
import sk.magiksoft.sodalis.icon.IconManager

/**
 * @author wladimiiir
 * @since 2011/2/26
 */
@VisibleModule
class EventModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.event.locale.event"
  private lazy val moduleDescriptor = new ModuleDescriptor(
    new ImageIcon(getClass.getResource("/sk/magiksoft/sodalis/event/icon/events2.png")),
    ResourceBundle.getBundle(bundleBaseName).getString("events")
  )

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
    EntityPropertyTranslatorManager.registerTranslator(classOf[Event], new EventPropertyTranslator)
    IconManager.getInstance().registerIcons(getClass.getResource("/sk/magiksoft/sodalis/event/icon/icons.properties"))

    ControlPanelRegistry.registerInfoPanels(classOf[Event].getName, List(
      classOf[EventInfoPanel],
      classOf[CategorizedEventInfoPanel],
      classOf[HistoryInfoPanel]
    ))
  }

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/event/data/mapping/event.hbm.xml"))
  }

  def getModuleDescriptor = moduleDescriptor

  def getContextManager = EventContextManager

  def getDataListener = EventContextManager
}
