package j_panels;

import j_dialogs.LoginDialog;
import p_s_p_challenge.PSPChallenge;
import utils.SocketsManager;
import utils.SpellBook;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelMain extends JPanel {

    public PanelMain() {

        SpellBook.creatingStandardPanelForFrame(this);

        addingLabels();

        PSPChallenge.frame.setTitle("Servidor");
    }


    private void addingLabels() {

        addingLoginButton();

        addingExitButton();
    }

    private void addingExitButton() {
        JButton exitButton = new JButton();
        exitButton.setText("Salir");
        exitButton.setSize(200, 50);
        exitButton.setLocation(
                this.getWidth() / 2 - exitButton.getWidth() / 2,
                300);
        this.add(exitButton);

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                SocketsManager.closeServer();
                JOptionPane.showMessageDialog(null, "Hasta la próxima!");

                System.exit(0);
            }
        });
    }

    private void addingLoginButton() {
        JButton loginButton = new JButton();
        loginButton.setText("Acceder");
        loginButton.setSize(200, 50);
        loginButton.setLocation(
                this.getWidth() / 2 - loginButton.getWidth() / 2,
                200);
        this.add(loginButton);

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                LoginDialog dialog = new LoginDialog();
                dialog.setVisible(true);
            }
        });
    }
}
