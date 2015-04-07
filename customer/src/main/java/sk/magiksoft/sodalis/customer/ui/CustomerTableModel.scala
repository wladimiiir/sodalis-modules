package sk.magiksoft.sodalis.customer.ui

import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.person.entity.Person

/**
 * @author wladimiiir 
 * @since 2/7/15
 */
class CustomerTableModel extends ObjectTableModel[Person](Array(
  LocaleManager.getString("firstName"),
  LocaleManager.getString("lastName")
)) {
  override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = columnIndex match {
    case 0 => getObject(rowIndex).getFirstName
    case 1 => getObject(rowIndex).getLastName
  }
}
