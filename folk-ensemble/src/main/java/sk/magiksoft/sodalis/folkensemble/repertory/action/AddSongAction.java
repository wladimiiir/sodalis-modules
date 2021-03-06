package sk.magiksoft.sodalis.folkensemble.repertory.action;

import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.OkCancelDialog;
import sk.magiksoft.sodalis.core.utils.UIUtils;
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.folkensemble.repertory.settings.RepertorySettings;
import sk.magiksoft.sodalis.folkensemble.repertory.ui.SongInfoPanel;
import sk.magiksoft.sodalis.icon.IconManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author wladimiiir
 */
public class AddSongAction extends MessageAction {

    private Song song;
    private OkCancelDialog dialog;
    private SongInfoPanel songInfoPanel;

    public AddSongAction() {
        super("", IconManager.getInstance().getIcon("add"));
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("addSong"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (dialog == null) {
            initDialog();
        }

        song = new Song();
        songInfoPanel.setupPanel(song);
        songInfoPanel.initData();

        dialog.setLocationRelativeTo(SodalisApplication.get().getMainFrame());
        dialog.setVisible(true);
    }

    private void initDialog() {
        dialog = new OkCancelDialog();

        songInfoPanel = new SongInfoPanel();
        songInfoPanel.initLayout();
        dialog.setModal(true);
        dialog.setTitle(LocaleManager.getString("addSong"));
        dialog.setMainPanel(songInfoPanel);
        dialog.setOkAction(new SaveAction());
        UIUtils.makeISDialog(dialog);
        dialog.setSize(650, 350);
    }

    private List<Category> getCategories() {
        final List<Long> ids = RepertorySettings.getInstance().getValue(RepertorySettings.O_SELECTED_CATEGORIES);

        return CategoryDataManager.getInstance().getCategories(ids);
    }

    private class SaveAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            songInfoPanel.setupObject(song);
            song.setCategories(getCategories());
            RepertoryDataManager.getInstance().addDatabaseEntity(song);
            dialog.setVisible(false);
        }
    }
}
