package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.core.controlpanel.DefaultControlPanel;
import sk.magiksoft.sodalis.person.entity.Person;

/**
 * @author wladimiiir
 */
public class MemberControlPanel extends DefaultControlPanel {
    public static final String KEY = "member";

    public MemberControlPanel() {
        super(KEY, Person.class.getName());
    }

}
