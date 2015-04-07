package sk.magiksoft.sodalis.event.ui;

import sk.magiksoft.sodalis.core.controlpanel.DefaultControlPanel;
import sk.magiksoft.sodalis.event.entity.Event;

/**
 * @author wladimiiir
 */
public class EventControlPanel extends DefaultControlPanel {
    public EventControlPanel() {
        super(Event.class.getName());
    }
}
