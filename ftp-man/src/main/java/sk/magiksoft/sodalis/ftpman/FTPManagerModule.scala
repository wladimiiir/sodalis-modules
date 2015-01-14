package sk.magiksoft.sodalis.ftpman

import java.util.ResourceBundle

import action.RetrieveFileAction
import entity.FTPEntry
import javax.swing.ImageIcon
import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.core.data.DBManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.registry.RegistryManager
import sk.magiksoft.sodalis.core.module.{VisibleModule, AbstractModule, ModuleDescriptor}
import sk.magiksoft.sodalis.category.entity.PropertyDynamicCategory
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.icon.IconManager

/**
 * @author wladimiiir
 * @since 2011/5/6
 */
@VisibleModule
class FTPManagerModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.ftpman.locale.ftpman"
  private lazy val descriptor = new ModuleDescriptor(IconManager.getInstance().getIcon("").asInstanceOf[ImageIcon],
    ResourceBundle.getBundle(bundleBaseName).getString("ftpDirectory"))
  private lazy val dynamicCategories = createDynamicCategories

  private def createDynamicCategories = {
    val moduleCategory = CategoryManager.getInstance().getRootCategory(classOf[FTPManagerModule], false)
    List(
      new PropertyDynamicCategory[FTPEntry, String](classOf[FTPEntry], "host", LocaleManager.getString("host")) {
        setParentCategory(moduleCategory)
        setId(-10l)

        protected def acceptProperty(entity: FTPEntry, value: String) = entity.host == value
      },
      new PropertyDynamicCategory[FTPEntry, String](classOf[FTPEntry], "path", LocaleManager.getString("path")) {
        setParentCategory(moduleCategory)
        setId(-20l)

        protected def acceptProperty(entity: FTPEntry, value: String) = entity.path == value
      }
    )
  }

  override def startUp(): Unit = {
    RegistryManager.registerPopupAction(classOf[FTPEntry], new RetrieveFileAction)
    LocaleManager.registerBundleBaseName(bundleBaseName)
    IconManager.getInstance().registerIcons(getClass.getResource("icon/icon.properties"))
  }

  def getDataListener = FTPManager

  def getContextManager = FTPManager

  def getModuleDescriptor = descriptor

  override def getDynamicCategories = {
    dynamicCategories.foreach {
      _.refresh()
    }
    super.getDynamicCategories ++ dynamicCategories
  }

  override def initConfiguration(configuration: Configuration): Unit = {
    configuration.addURL(getClass.getResource("data/mapping/ftpman.hbm.xml")
  }
}
