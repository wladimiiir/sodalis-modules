package sk.magiksoft.sodalis.folkensemble.member

import javax.swing.ImageIcon

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.controlpanel.impl.NoteInfoPanel
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.event.ui.EventTableInfoPanel
import sk.magiksoft.sodalis.folkensemble.member.data.EnsembleGroupDynamicCategory
import sk.magiksoft.sodalis.folkensemble.member.entity.property.MemberPropertyTranslator
import sk.magiksoft.sodalis.folkensemble.member.ui._
import sk.magiksoft.sodalis.icon.IconManager
import sk.magiksoft.sodalis.person.PersonModule
import sk.magiksoft.sodalis.person.data.SexDynamicCategory
import sk.magiksoft.sodalis.person.entity.{Person, PersonWrapper}
import sk.magiksoft.sodalis.person.imex.{PersonImportResolver, PersonWrapperImportResolver}

/**
 * @author wladimiiir
 * @since 2011/3/22
 */

@VisibleModule
class MemberModule extends AbstractModule with PersonModule {
  private lazy val moduleDescriptor = new ModuleDescriptor(
    new ImageIcon(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/icon/folkMember2.png")),
    LocaleManager.getString("members")
  )

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.locale.member")

    initDynamicCategories()
    EntityPropertyTranslatorManager.registerTranslator(classOf[Person], new MemberPropertyTranslator)
    ImExManager.registerImportProcessor(classOf[Person], new PersonImportResolver)
    ImExManager.registerImportProcessor(classOf[PersonWrapper], new PersonWrapperImportResolver)
    IconManager.getInstance().registerIcons(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/icon/icons.properties"))

    ControlPanelRegistry.registerInfoPanels(MemberControlPanel.KEY, List(
      classOf[MemberPersonalDataInfoPanel],
      classOf[UniversityDataInfoPanel],
      classOf[EnsembleDataPanel],
      classOf[CategorizedMemberInfoPanel],
      classOf[MemberSongInfoPanel],
      classOf[MemberInventoryItemInfoPanel],
      classOf[NoteInfoPanel],
      classOf[EventTableInfoPanel],
      classOf[MemberHistoryInfoPanel]
    ))
  }

  private def initDynamicCategories(): Unit = {
    val rootCategory = CategoryManager.getInstance.getRootCategory(classOf[MemberModule], false)

    registerDynamicCategory(new SexDynamicCategory(rootCategory))
    registerDynamicCategory(new EnsembleGroupDynamicCategory(rootCategory))
  }

  def getDataListener = MemberContextManager

  def getContextManager = MemberContextManager

  def getModuleDescriptor = moduleDescriptor

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("/sk/magiksoft/sodalis/folkensemble/member/data/mapping/ensemble.hbm.xml"))
  }
}
