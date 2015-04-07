package sk.magiksoft.sodalis.psyche

import java.util.ResourceBundle

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.data.DBManager
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import sk.magiksoft.sodalis.psyche.entity.PsychoTest
import sk.magiksoft.sodalis.psyche.ui.PsychoTestInfoPanel
import scala.collection.JavaConversions._

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
    ControlPanelRegistry.registerInfoPanels(classOf[PsychoTest].getName, List(
      classOf[PsychoTestInfoPanel]
    ))
  }


  override def install(dbManager: DBManager): Unit = {
    PsycheDataManager.addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("imex/aperceptions.xml")))
    PsycheDataManager.addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("imex/contents.xml")))
    PsycheDataManager.addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("imex/determinants.xml")))
    PsycheDataManager.addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("imex/original_answers.xml")))
    PsycheDataManager.addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("imex/rorschach_blots.xml")))
    PsycheDataManager.addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("imex/special_signs.xml")))
    PsycheDataManager.addOrUpdateEntities(ImExManager.importFromStream(getClass.getResourceAsStream("imex/vulgar_answers.xml")))
  }

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("data/mapping/psyche.hbm.xml"))
  }
}
