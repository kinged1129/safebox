package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.client.gui.component.ChangingToggleButton;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.gui.GridBagBuilder;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TopPassGenPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    public TopPassGenPanel() {

        super(new GridBagLayout());

        final PasswordField passwordField = new PasswordField(100, false);
        final ChangingToggleButton showPasswordBtn = new ChangingToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                passwordField::showPassword,
                passwordField::hidePassword
        );
        final JProgressBar progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 30);
        passwordField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                progressBar.setValue(passwordField.getPassword().length);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        final JLabel quality = new JLabel("Quality: ");
        final JLabel entropy = new JLabel("Entropy: ");
        final JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        //TODO: Adds listeners of quality and entropy to change their values

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Password: "));
        gbb.setGridWidthAndWeightX(2, 1);
        put(passwordField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(showPasswordBtn);

        gbb.setGridX(1)
                .setGridWidthAndWeightX(2,0)
                .setAnchor(Anchor.NORTH);
        put(progressBar);

        gbb.setGridX(1)
                .setGridWidthAndWeightX(1, 1);
        put(quality);
        p.add(entropy);
        gbb.setGridX(2)
                .setGridWidthAndWeightX(1,0);
        put(p);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(new Dimension(500,400));
        f.add(new TopPassGenPanel());
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
