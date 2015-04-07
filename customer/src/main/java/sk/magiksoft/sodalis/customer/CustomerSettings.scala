package sk.magiksoft.sodalis.customer

import java.util
import java.util.Collections

import sk.magiksoft.sodalis.core.settings.Settings
import scala.collection.JavaConversions._

/**
 * @author wladimiiir 
 * @since 3/24/15
 */
object CustomerSettings extends Settings("CustomerSettings") {

  override protected def getDefaultSettingsMap = {
    Map(
      Settings.O_SELECTED_CATEGORIES -> Collections.emptyList
    )
  }
}
