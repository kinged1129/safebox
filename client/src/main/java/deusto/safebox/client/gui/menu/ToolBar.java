package deusto.safebox.client.gui.menu;

import deusto.safebox.client.gui.TrayIconHandler;
import deusto.safebox.client.gui.component.SearchBox;
import deusto.safebox.client.gui.panel.SettingsDialog;
import deusto.safebox.client.gui.panel.pwdgen.PasswordGenDialog;
import deusto.safebox.client.locale.Message;
import deusto.safebox.client.util.IconType;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar {

    public ToolBar(JFrame mainFrame, Runnable lockAction) {
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));

        add(new ToolBarButton(Message.SIGN_OUT.get(), IconType.LOCK, lockAction));
        add(new ToolBarButton(Message.MINIMIZE.get(), IconType.MINIMIZE, TrayIconHandler::showTrayIcon));
        addSeparator();
        add(new ToolBarButton(Message.PASSWORD_GENERATOR.get(), IconType.PASSWORD_FIELD, () -> new PasswordGenDialog(mainFrame)));
        add(new ToolBarButton(Message.SETTINGS.get(), IconType.GEAR, () -> new SettingsDialog(mainFrame)));
        addSeparator();
        add(Box.createHorizontalGlue());
        add(new SearchBox());
    }

    private static class ToolBarButton extends JButton {

        ToolBarButton(String toolTipText, IconType iconType, Runnable action) {
            super(iconType.getAsIcon());
            setFocusable(false);
            setToolTipText(toolTipText);
            addActionListener(e -> action.run());
        }
    }
}
