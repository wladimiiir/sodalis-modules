package sk.magiksoft.sodalis.event.action

import java.awt.event.ActionEvent
import java.util
import java.util.Calendar

import sk.magiksoft.sodalis.category.CategoryDataManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.core.controlpanel.InfoPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.settings.Settings
import sk.magiksoft.sodalis.core.ui.wizard.{Page, Wizard, WizardFinished}
import sk.magiksoft.sodalis.core.utils.Conversions._
import sk.magiksoft.sodalis.event.data.EventDataManager
import sk.magiksoft.sodalis.event.entity.{Event, EventType}
import sk.magiksoft.sodalis.event.settings.EventSettings
import sk.magiksoft.sodalis.event.ui.EventControlPanel
import sk.magiksoft.sodalis.icon.IconManager

import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/2/18
 */
class AddEventAction extends MessageAction(null, IconManager.getInstance.getIcon("add")) {
  private var event: Event = _
  private var wizard: Option[Wizard] = None

  private val pages = {
    val infoPanels = new EventControlPanel().getAllInfoPanels.filter(_.isWizardSupported)
    infoPanels.map {
      ip => new EventPage(infoPanels.indexOf(ip), ip)
    }
  }

  def getActionMessage(objects: util.List[_]) = new ActionMessage(true, LocaleManager.getString("addEvent"))


  def actionPerformed(e: ActionEvent) = {
    val wizard = this.wizard match {
      case Some(wizard) => wizard
      case None => createWizard
    }
    event = createEvent
    event.getEndTime.add(Calendar.MINUTE, EventSettings.getInstance.getInt(EventSettings.I_EVENT_DURATION))
    event.setEventType(EventDataManager.getInstance.getDatabaseEntities(classOf[EventType])(0))
    for (page <- pages) {
      page.infoPanel.setupPanel(event)
      page.infoPanel.initData()
    }
    wizard.showWizard
  }

  private def createWizard = {
    val wizard = new Wizard(SodalisApplication.get.getMainFrame, LocaleManager.getString("addEvent"), pages(0))
    wizard.setSize(740, 300)
    wizard.setLocationRelativeTo(SodalisApplication.get().getMainFrame)
    wizard.reactions += {
      case WizardFinished(_) =>
        pages(0).infoPanel.setupObject(event)
        pages.subList(1, pages.size).filter {
          _.infoPanel.acceptObject(event)
        }.foreach {
          _.infoPanel.setupObject(event)
        }
        EventDataManager.getInstance.addDatabaseEntity(event)
    }
    this.wizard = Option(wizard)
    pages.foreach {
      _.infoPanel.initLayout
    }
    wizard
  }

  private def createEvent = {
    val ids = EventSettings.getInstance.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[util.List[java.lang.Long]]
    val categories = CategoryDataManager.getInstance.getCategories(ids)
    val event = new Event()
    event.setCategories(categories)
    pages(0).infoPanel.setupObject(event)
    event
  }

  private class EventPage(val index: Int, val infoPanel: InfoPanel) extends Page {
    def getComponent = infoPanel.getComponentPanel

    def getNextPage = pages.subList(index + 1, pages.size).find {
      _.infoPanel.acceptObject(event)
    }

    def getPreviousPage = pages.subList(0, index).reverse.find {
      _.infoPanel.acceptObject(event)
    }
  }

}
