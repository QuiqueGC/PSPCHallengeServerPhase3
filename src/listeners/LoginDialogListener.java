package listeners;

import data_classes.User;
import j_panels.PanelAdmin;
import p_s_p_challenge.PSPChallenge;
import utils.BlowFishManager;
import utils.SpellBook;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Listener para el botón de login de usuario
 */
public class LoginDialogListener extends MouseAdapter {

    private final JTextField NAME_FIELD;
    private final JPasswordField PASSWD_FIELD;
    private final JDialog DIALOG;


    public LoginDialogListener(JTextField nameField, JPasswordField paswdField, JDialog dialog) {
        this.NAME_FIELD = nameField;
        this.PASSWD_FIELD = paswdField;
        this.DIALOG = dialog;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        String name = NAME_FIELD.getText().trim();
        String passwd = String.valueOf(PASSWD_FIELD.getPassword()).trim();

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(null, "No has introducido nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (passwd.isEmpty()) {

            JOptionPane.showMessageDialog(null, "No has introducido contraseña.", "Error", JOptionPane.ERROR_MESSAGE);

        } else {

            User foundUser = SpellBook.lookingForUser(name);

            if (foundUser != null) {

                loginAdmin(passwd, foundUser);
                DIALOG.dispose();

            } else {

                JOptionPane.showMessageDialog(null, "El nombre de usuario no está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void loginAdmin(String passwd, User foundUser) {

        if(foundUser.getUserType() != 2){

            if (BlowFishManager.checkingPasswd(passwd, foundUser)) {

                PSPChallenge.actualUser = foundUser;


                PSPChallenge.frame.setContentPane(new PanelAdmin());


            } else {
                JOptionPane.showMessageDialog(null, "La contraseña no coincide con la del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Este usuario no tiene permiso para entrar en la aplicación.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
