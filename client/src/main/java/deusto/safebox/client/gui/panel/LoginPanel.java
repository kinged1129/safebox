package deusto.safebox.client.gui.panel;

import static deusto.safebox.client.gui.GridBagBuilder.Anchor;
import static deusto.safebox.client.gui.GridBagBuilder.Fill;
import static deusto.safebox.client.util.IconManager.IconType;

import deusto.safebox.client.gui.GridBagBuilder;
import deusto.safebox.client.gui.component.ChangingToggleButton;
import deusto.safebox.client.gui.component.ShowPasswordField;
import deusto.safebox.client.gui.component.SimpleButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class LoginPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    LoginPanel() {
        super(new GridBagLayout());

        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        final JTextField emailField = new JTextField();
        final ShowPasswordField passwordField = new ShowPasswordField(false);
        final JButton loginBtn = new SimpleButton("Login");
        final JCheckBox rememberEmail = new JCheckBox("Remember Email");
        rememberEmail.setFocusPainted(false);
        final ChangingToggleButton showPasswordBtn = new ChangingToggleButton(
                IconType.EYE, IconType.EYE_CLOSED, false) {
            @Override
            public void on() {
                passwordField.showPassword();
            }

            @Override
            public void off() {
                passwordField.hidePassword();
            }
        };

        gbb.setInsets(4, 4, 4, 4);
        gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        addGB(new JLabel("Email"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        addGB(emailField);

        gbb.setGridWidthAndWeightX(1, 0);
        addGB(new JLabel("Password"));
        gbb.setWeightX(1);
        addGB(passwordField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        addGB(showPasswordBtn);

        gbb.setGridWidth(1);
        addGB(rememberEmail);

        gbb.incrementGridX();
        gbb.setGridWidth(GridBagConstraints.REMAINDER);
        gbb.setFillAndAnchor(Fill.NONE, Anchor.SOUTH);
        addGB(loginBtn);
    }

    private void addGB(JComponent component) {
        add(component, gbb.getConstraints());
    }
}