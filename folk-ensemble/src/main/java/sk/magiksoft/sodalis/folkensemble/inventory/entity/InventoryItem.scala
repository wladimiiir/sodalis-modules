package sk.magiksoft.sodalis.folkensemble.inventory.entity

import java.lang.Long
import java.util

import sk.magiksoft.sodalis.category.entity.{Categorized, Category}
import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import sk.magiksoft.sodalis.core.history.{Historizable, HistoryEvent}
import sk.magiksoft.sodalis.item.entity.Item

import scala.beans.BeanProperty
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/7/1
 */

class InventoryItem extends Item with Historizable with Categorized {
  var categories: util.List[Category] = new util.ArrayList[Category]
  @BeanProperty var inventoryItemDatas: util.Map[Class[_], InventoryItemData] = new util.HashMap[Class[_], InventoryItemData]

  def getInventoryItemData[T <: InventoryItemData](clazz: Class[T]): T = {
    inventoryItemDatas.containsKey(clazz) match {
      case true =>
        inventoryItemDatas.get(clazz).asInstanceOf[T]

      case false =>
        val data = clazz.newInstance
        inventoryItemDatas.put(clazz, data)
        data
    }
  }

  override def updateFrom(entity: DatabaseEntity): Unit = {
    if (entity != this) {
      super.updateFrom(entity)

      entity match {
        case inventoryItem: InventoryItem => {
          this.categories.clear()
          this.categories.addAll(inventoryItem.categories)
          for (inventoryItemData <- inventoryItemDatas.values) {
            inventoryItemData.updateFrom(inventoryItem.getInventoryItemData(inventoryItemData.getClass))
          }
        }
      }
    }
  }

  def getCategories: util.List[Category] = {
    categories
  }


  def setCategories(categories: util.List[Category]): Unit = {
    this.categories = categories
  }


  def addHistoryEvent(event: HistoryEvent): Unit = {
    val historyEvents: util.List[HistoryEvent] = getInventoryItemData(classOf[InventoryHistoryData]).getHistoryEvents
    historyEvents.add(event)
  }

  def getHistoryEvents(entityID: Long) = getInventoryItemData(classOf[InventoryHistoryData]).getHistoryEvents
}
