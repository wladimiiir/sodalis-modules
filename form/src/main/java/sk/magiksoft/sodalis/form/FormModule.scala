package sk.magiksoft.sodalis.form

import java.util.ResourceBundle

import sk.magiksoft.sodalis.core.controlpanel.ControlPanelRegistry
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.module.{VisibleModule, ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.form.entity.Form
import sk.magiksoft.sodalis.form.ui.{CategorizedFormInfoPanel, FormEditorInfoPanel, FormInfoPanel}
import sk.magiksoft.sodalis.icon.IconManager

/**
 * @author wladimiiir
 * @since 2010/4/13
 */
@VisibleModule
class FormModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.form.locale.form"
  private lazy val moduleDescriptor = new ModuleDescriptor(IconManager.getInstance().getIcon("eventsModule") match {
    case e: ImageIcon => e
    case _ => null
  }, ResourceBundle.getBundle(bundleBaseName).getString("forms"))

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
    ControlPanelRegistry.registerInfoPanels(classOf[Form].getName, List(
      classOf[FormInfoPanel],
      classOf[FormEditorInfoPanel],
      classOf[CategorizedFormInfoPanel],
      classOf[HistoryInfoPanel]
    ))
  }

  def getDataListener = null

  def getContextManager = FormContextManager

  def getModuleDescriptor = moduleDescriptor

}
