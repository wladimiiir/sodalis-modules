package sk.magiksoft.sodalis.customer.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import sk.magiksoft.sodalis.person.entity.{PersonData, Person}

import scala.beans.BeanProperty

/**
 * @author wladimiiir 
 * @since 2/7/15
 */
class CustomerData extends AbstractDatabaseEntity with PersonData {
  @BeanProperty
  var rating: Long = 0

  override def updateFrom(entity: DatabaseEntity): Unit = entity match {
    case customer: CustomerData =>
      this.rating = customer.rating
      this.note = customer.note

    case _ =>
  }
}
