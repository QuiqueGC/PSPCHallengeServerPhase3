package listeners;

import p_s_p_challenge.PSPChallenge;
import utils.BlowFishManager;
import utils.FilesRW;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelEditProfileListener extends MouseAdapter {

    JTextField nameField;
    JPasswordField newPasswdField;
    JPasswordField newPasswdField2;
    JPasswordField oldPasswdField;
    JPanel panelAdmin;

    public PanelEditProfileListener(JTextField nameField, JPasswordField newPasswdField, JPasswordField newPasswdField2, JPasswordField oldPasswdField, JPanel panelAdmin) {
        this.nameField = nameField;
        this.newPasswdField = newPasswdField;
        this.newPasswdField2 = newPasswdField2;
        this.oldPasswdField = oldPasswdField;
        this.panelAdmin = panelAdmin;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        String name = nameField.getText().trim();
        String newPasswd = String.valueOf(newPasswdField.getPassword()).trim();
        String newPasswd2 = String.valueOf(newPasswdField2.getPassword()).trim();
        String oldPasswd = String.valueOf(oldPasswdField.getPassword()).trim();


        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (!newPasswd.equals(newPasswd2)) {

            JOptionPane.showMessageDialog(null, "La contraseña y la confirmación de contraseña no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (oldPasswd.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Debes rellenar el campo de antigua contraseña para efectuar cualquier cambio", "Error", JOptionPane.ERROR_MESSAGE);

        } else {

            checkingPasswdAndSavingUser(name, newPasswd, oldPasswd);
        }
    }

    private void checkingPasswdAndSavingUser(String name, String newPasswd, String oldPasswd) {

        if (BlowFishManager.checkingPasswd(oldPasswd, PSPChallenge.actualUser)) {

            savingUser(name, newPasswd);

        } else {

            JOptionPane.showMessageDialog(null, "La contraseña antigua no coincide con la registrada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void savingUser(String name, String newPasswd) {

        boolean isChangingPasswd = !newPasswd.isEmpty();

        if (!isChangingPasswd) {

            PSPChallenge.actualUser.setName(name);

        } else {

            String encryptedPasswd = BlowFishManager.encryptingPasswd(newPasswd);
            PSPChallenge.actualUser.setName(name);
            PSPChallenge.actualUser.setPasswd(encryptedPasswd);
        }

        FilesRW.overwritingFile();

        PSPChallenge.frame.setContentPane(panelAdmin);
    }
}
