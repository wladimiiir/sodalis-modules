package sk.magiksoft.sodalis.folkensemble.repertory

import java.util.ResourceBundle
import javax.swing.ImageIcon

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.category.entity.{Categorized, EntityDynamicCategory}
import sk.magiksoft.sodalis.core.SodalisManager
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.core.enumeration.{EnumerationDynamicCategory, Enumerations}
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.folkensemble.member.MemberModule
import sk.magiksoft.sodalis.folkensemble.member.data.EnsembleGroupDynamicCategory
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song
import sk.magiksoft.sodalis.folkensemble.repertory.entity.property.SongPropertyTranslator
import sk.magiksoft.sodalis.folkensemble.repertory.imex.SongImportResolver
import sk.magiksoft.sodalis.folkensemble.repertory.ui.{CategorizedRepertoryInfoPanel, InterpretationInfoPanel, SongInfoPanel}
import sk.magiksoft.sodalis.person.data.PersonWrapperDynamicCategory
import sk.magiksoft.sodalis.person.entity.{Person, PersonWrapper}

import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/4/22
 */
@VisibleModule
class RepertoryModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.folkensemble.locale.repertory"
  private lazy val moduleDescriptor = new ModuleDescriptor(
    new ImageIcon(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/icon/repertory.png")),
    ResourceBundle.getBundle(bundleBaseName).getString("repertory.moduleName")
  )

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
    EntityPropertyTranslatorManager.registerTranslator(classOf[Song], new SongPropertyTranslator)
    ImExManager.registerImportProcessor(classOf[Song], new SongImportResolver)
    ControlPanelRegistry.registerInfoPanels(classOf[Song].getName, List(
      classOf[SongInfoPanel],
      classOf[InterpretationInfoPanel],
      classOf[CategorizedRepertoryInfoPanel],
      classOf[HistoryInfoPanel]
    ))

    initDynamicCategories()
  }

  private def initDynamicCategories(): Unit = {
    val moduleCategory = CategoryManager.getInstance().getRootCategory(classOf[RepertoryModule], false)

    registerDynamicCategory(new PersonWrapperDynamicCategory[Song](LocaleManager.getString("musicComposing"), "select s.composers from Song s") {
      setParentCategory(moduleCategory)
      setId(-10l)

      protected def acceptCategorized(entity: PersonWrapper, categorized: Song) = categorized.getComposers.exists {
        p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId)) ||
          (p.getPersonName == entity.getPersonName)
      }
    })
    registerDynamicCategory(new PersonWrapperDynamicCategory[Song](LocaleManager.getString("pedagogists"), "select s.pedagogists from Song s") {
      setParentCategory(moduleCategory)
      setId(-20l)

      protected def acceptCategorized(entity: PersonWrapper, categorized: Song) = categorized.getPedagogists.exists {
        p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId)) ||
          (p.getPersonName == entity.getPersonName)
      }
    })
    registerDynamicCategory(new PersonWrapperDynamicCategory[Song](LocaleManager.getString("choreography"), "select s.choreographers from Song s") {
      setParentCategory(moduleCategory)
      setId(-30l)

      protected def acceptCategorized(entity: PersonWrapper, categorized: Song) = categorized.getChoreographers.exists {
        p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId)) ||
          (p.getPersonName == entity.getPersonName)
      }
    })
    registerDynamicCategory(new PersonWrapperDynamicCategory[Song](LocaleManager.getString("interpretation"), "select s.interpreters from Song s") {
      setParentCategory(moduleCategory)
      setId(-40l)

      protected def acceptCategorized(entity: PersonWrapper, categorized: Song) = categorized.getInterpreters.exists {
        p => ((p.getPerson ne null) && (entity.getPerson ne null) && (p.getPerson.getId == entity.getPerson.getId)) ||
          (p.getPersonName == entity.getPersonName)
      }
    })
    registerDynamicCategory(new EnsembleGroupDynamicCategory(moduleCategory) {
      setId(-50l)

      override def acceptEnsembleGroup(categorized: Categorized, groupType: Int) = categorized.asInstanceOf[Song].getInterpreters.exists {
        p => (p.getPerson ne null) && super.acceptEnsembleGroup(p.getPerson, groupType)
      }
    })
    registerDynamicCategory(new EntityDynamicCategory[Programme, Song](LocaleManager.getString("programme"),
      "select p from Programme p where size(p.programmeSongs)>0") {
      setParentCategory(moduleCategory)
      setId(-60l)

      protected def acceptCategorized(entity: Programme, categorized: Song) = entity.getProgrammeSongs.exists {
        ps => ps.getSong.getId == categorized.getId
      }
    })
    registerDynamicCategory(new EnumerationDynamicCategory(Enumerations.SONG_GENRE) {
      setParentCategory(moduleCategory)
      setId(-70l)

      def acceptEntryText(entryText: String, categorized: Categorized) =
        entryText.equalsIgnoreCase(categorized.asInstanceOf[Song].getGenre)
    })
    registerDynamicCategory(new EnumerationDynamicCategory(Enumerations.FOLK_REGION) {
      setParentCategory(moduleCategory)
      setId(-80l)

      def acceptEntryText(entryText: String, categorized: Categorized) =
        entryText.equalsIgnoreCase(categorized.asInstanceOf[Song].getRegion)
    })

    val memberModule = SodalisManager.moduleManager.getModuleByClass(classOf[MemberModule])
    if (memberModule != null) {
      val memberModuleCategory = CategoryManager.getInstance().getRootCategory(classOf[MemberModule], false)
      memberModule.registerDynamicCategory(new EntityDynamicCategory[Song, Person](LocaleManager.getString("songInterpretation"), "select s from Song s where size(s.interpreters)>0") {
        id = -1l
        parentCategory = memberModuleCategory

        def acceptCategorized(entity: Song, categorized: Person) =
          entity.getInterpreters.exists {
            pw => (pw.getPerson ne null) && (pw.getPerson.id == categorized.getId)
          }
      })
    }
  }

  def getDataListener = RepertoryContextManager.getInstance()

  def getContextManager = RepertoryContextManager.getInstance()

  def getModuleDescriptor = moduleDescriptor

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/repertory/data/mapping/repertory.hbm.xml"))
  }
}
