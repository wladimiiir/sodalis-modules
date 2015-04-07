package sk.magiksoft.sodalis.folkensemble.programme

import java.util.ResourceBundle
import javax.swing.ImageIcon

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.category.entity.EntityDynamicCategory
import sk.magiksoft.sodalis.core.SodalisManager
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.folkensemble.member.MemberModule
import sk.magiksoft.sodalis.folkensemble.programme.entity.property.ProgrammePropertyTranslator
import sk.magiksoft.sodalis.folkensemble.programme.entity.{Programme, ProgrammeSong}
import sk.magiksoft.sodalis.folkensemble.programme.ui.{CategorizedProgrammeInfoPanel, ProgrammeEventInfoPanel, ProgrammeInfoPanel, ProgrammeSongInfoPanel}
import sk.magiksoft.sodalis.person.data.PersonWrapperDynamicCategory
import sk.magiksoft.sodalis.person.entity.{Person, PersonWrapper}

import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/4/22
 */
@VisibleModule
class ProgrammeModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.folkensemble.locale.programme"
  private lazy val moduleDescriptor = new ModuleDescriptor(
    new ImageIcon(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/icon/programme.png")),
    ResourceBundle.getBundle(bundleBaseName).getString("programme.moduleName")
  )

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
    EntityPropertyTranslatorManager.registerTranslator(classOf[Programme], new ProgrammePropertyTranslator)
    ControlPanelRegistry.registerInfoPanels(classOf[Programme].getName, List(
      classOf[ProgrammeInfoPanel],
      classOf[ProgrammeSongInfoPanel],
      classOf[CategorizedProgrammeInfoPanel],
      classOf[ProgrammeEventInfoPanel],
      classOf[HistoryInfoPanel]
    ))

    initDynamicCategories()
  }

  private def initDynamicCategories(): Unit = {
    val moduleCategory = CategoryManager.getInstance().getRootCategory(classOf[ProgrammeModule], false)

    registerDynamicCategory(new PersonWrapperDynamicCategory[Programme](LocaleManager.getString("authors"), "select p.authors from Programme p") {
      setParentCategory(moduleCategory)
      setId(-10l)

      protected def acceptCategorized(entity: PersonWrapper, programme: Programme) = {
        programme.getAuthors.exists(p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId))
          || (p.getPersonName == entity.getPersonName))
      }
    })
    registerDynamicCategory(new PersonWrapperDynamicCategory[Programme](LocaleManager.getString("choreography"), "select p.choreographers from Programme p") {
      setParentCategory(moduleCategory)
      setId(-20l)

      protected def acceptCategorized(entity: PersonWrapper, programme: Programme) = {
        programme.getChoreographers.exists(p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId))
          || (p.getPersonName == entity.getPersonName))
      }
    })
    registerDynamicCategory(new PersonWrapperDynamicCategory[Programme](LocaleManager.getString("musicComposing"), "select p.composers from Programme p") {
      setParentCategory(moduleCategory)
      setId(-30l)

      protected def acceptCategorized(entity: PersonWrapper, categorized: Programme) = {
        categorized.getComposers.exists(p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId))
          || (p.getPersonName == entity.getPersonName))
      }
    })
    registerDynamicCategory(new PersonWrapperDynamicCategory[Programme](LocaleManager.getString("interpretation"), "select ps.interpreters from Programme p left join p.programmeSongs as ps") {
      setParentCategory(moduleCategory)
      setId(-40l)

      protected def acceptCategorized(entity: PersonWrapper, categorized: Programme) = {
        categorized.getInterpreters.exists(p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId))
          || (p.getPersonName == entity.getPersonName))
      }
    })
    registerDynamicCategory(new EntityDynamicCategory[ProgrammeSong, Programme](LocaleManager.getString("song"), "select p.programmeSongs from Programme p") {
      setParentCategory(moduleCategory)
      setId(-50l)

      override protected def getEntityString(entity: ProgrammeSong) = entity.getSong.getName

      override protected def getWrappedEntity(entity: ProgrammeSong) = entity.getSong

      protected def acceptCategorized(entity: ProgrammeSong, categorized: Programme) = categorized.getProgrammeSongs.exists {
        ps => ps.getSong.getId == entity.getSong.getId
      }
    })

    val memberModule = SodalisManager.moduleManager.getModuleByClass(classOf[MemberModule])
    if (memberModule != null) {
      val memberModuleCategory = CategoryManager.getInstance().getRootCategory(classOf[MemberModule], false)
      memberModule.registerDynamicCategory(new EntityDynamicCategory[Programme, Person](LocaleManager.getString("programme"), "select p from Programme p left join p.programmeSongs as ps where size(p.programmeSongs)>0 and size(ps.interpreters)>0") {
        id = -2l
        parentCategory = memberModuleCategory

        def acceptCategorized(entity: Programme, categorized: Person) =
          entity.getInterpreters.exists {
            pw => (pw.getPerson ne null) && (pw.getPerson.id == categorized.getId)
          }
      })
    }
  }

  def getDataListener = ProgrammeContextManager.getInstance()

  def getContextManager = ProgrammeContextManager.getInstance()

  def getModuleDescriptor = moduleDescriptor

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/programme/data/mapping/programme.hbm.xml"))
  }
}
