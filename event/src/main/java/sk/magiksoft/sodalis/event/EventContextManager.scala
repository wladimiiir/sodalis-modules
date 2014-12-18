package sk.magiksoft.sodalis.event

import action.ActionFactory
import data.EventDataManager
import ui.{EventUI, EventStatusPanel}
import sk.magiksoft.sodalis.core.context.AbstractContextManager
import sk.magiksoft.sodalis.core.utils.Utils
import sk.magiksoft.sodalis.core.filter.action.FilterEvent
import scala.collection.JavaConversions

 /**
  * @author wladimiiir
  * @since 2011/2/26
  */
object EventContextManager extends AbstractContextManager {
  private val contextActions = List(
    ActionFactory.getInstance.getAction(ActionFactory.ACTION_TOGGLE_SNAP),
    ActionFactory.getInstance.getAction(ActionFactory.ACTION_CHANGE_COLOR),
    ActionFactory.getInstance.getAction(ActionFactory.ACTION_DEFAULT_EVENT),
    ActionFactory.getInstance.getAction(ActionFactory.ACTION_EVENT_DURATION)
  )
  private val statusPanel = new EventStatusPanel
  private var eventUI: Option[EventUI] = None

  override def getStatusPanel = statusPanel

  def createContext = {
    eventUI = Option(new EventUI)
    eventUI.get
  }

  def getInstance = this

  def isFullTextActive = false

  override def reloadData() = eventUI match {
    case Some(ui) => ui.reload()
    case None =>
  }

  override def getFilterConfigFileURL = getClass.getResource("filter/EventFilter.xml")

  def addToCalendar(field: Int, value: Int) = eventUI match {
    case Some(ui) => ui.addToCalendar(field, value)
    case None =>
  }

  override def queryChanged(event: FilterEvent) = eventUI match {
    case Some(ui) => ui.filter(event)
    case None =>
  }

  def getContextActions = contextActions

  def getDefaultQuery = null

  def getDataManager = EventDataManager.getInstance
}
