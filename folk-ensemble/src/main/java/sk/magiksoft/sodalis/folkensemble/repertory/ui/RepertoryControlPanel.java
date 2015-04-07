package sk.magiksoft.sodalis.folkensemble.repertory.ui;

import sk.magiksoft.sodalis.core.controlpanel.DefaultControlPanel;
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;

/**
 * @author wladimiiir
 */
public class RepertoryControlPanel extends DefaultControlPanel {

    public RepertoryControlPanel() {
        super(Song.class.getName());
    }

    @Override
    protected void saveObject(Object object) {
        RepertoryDataManager.getInstance().updateSong((Song) object);
    }
}
