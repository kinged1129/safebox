package deusto.safebox.client.gui.menu;

import static deusto.safebox.client.util.IconManager.IconType;

import deusto.safebox.client.gui.ButtonAction;
import deusto.safebox.client.gui.component.SearchBox;
import deusto.safebox.client.gui.panel.SettingsDialog;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public abstract class ToolBar extends JToolBar {

    public ToolBar(JFrame mainFrame) {
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));

        JButton newEntryBtn = new ToolBarButton(
                "Add a new entry", IconType.NEW_FILE_20) {
            @Override
            public void action() {
                // TODO
            }
        };

        JButton lockBtn = new ToolBarButton(
                "Lock database", IconType.LOCK) {
            @Override
            public void action() {
                lock();
            }
        };

        JToggleButton pwdGenBtn = new ToolBarToggleButton(
                "Password generator", IconType.PASSWORD_FIELD) {
            @Override
            public void action() {
                // TODO
            }
        };

        JButton settingsBtn = new ToolBarButton(
                "Settings", IconType.GEAR) {
            @Override
            public void action() {
                new SettingsDialog(mainFrame);
            }
        };

        SearchBox searchBox = new SearchBox();

        add(newEntryBtn);
        addSeparator();
        add(pwdGenBtn);
        add(lockBtn);
        addSeparator();
        add(settingsBtn);
        addSeparator();
        add(Box.createHorizontalGlue());
        add(searchBox);
    }

    protected abstract void lock();

    private abstract static class ToolBarButton extends JButton implements ButtonAction {

        ToolBarButton(String toolTipText, IconType iconType) {
            super(iconType.getAsIcon());
            setFocusPainted(false);
            setRequestFocusEnabled(false);
            setToolTipText(toolTipText);
            addActionListener(e -> action());
        }
    }

    private abstract static class ToolBarToggleButton extends JToggleButton implements ButtonAction {

        ToolBarToggleButton(String toolTipText, IconType iconType) {
            super(iconType.getAsIcon());
            setFocusPainted(false);
            setRequestFocusEnabled(false);
            setToolTipText(toolTipText);
            addActionListener(e -> action());
        }
    }
}
