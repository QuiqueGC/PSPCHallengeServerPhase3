package listeners;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import data_classes.User;
import p_s_p_challenge.PSPChallenge;
import utils.BlowFishManager;
import utils.FilesRW;

import static utils.SpellBook.checkingIfUserExist;
import static utils.SpellBook.creatingNewUser;

public class UserCreationDialogListener extends MouseAdapter {

    private final JTextField NAME_FIELD;
    private final JPasswordField PASSWD_FIELD;
    private final JPasswordField PASSWD_FIELD_2;

    private final JDialog DIALOG;

    private final int USER_TYPE;

    private boolean alreadyExist;

    public UserCreationDialogListener(JDialog dialog, JTextField nameField, JPasswordField passwdField, JPasswordField passwdField2, int userType) {
        this.NAME_FIELD = nameField;
        this.PASSWD_FIELD = passwdField;
        this.PASSWD_FIELD_2 = passwdField2;
        this.USER_TYPE = userType;
        this.DIALOG = dialog;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        alreadyExist = false;
        String name = NAME_FIELD.getText().trim();
        String passwd = String.valueOf(PASSWD_FIELD.getPassword()).trim();
        String passwd2 = String.valueOf(PASSWD_FIELD_2.getPassword()).trim();


        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(null, "No has introducido nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (passwd.isEmpty()) {

            JOptionPane.showMessageDialog(null, "No has introducido contraseña.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (passwd2.isEmpty()) {

            JOptionPane.showMessageDialog(null, "No has introducido la confirmación de contraseña.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (!passwd.equals(passwd2)) {

            JOptionPane.showMessageDialog(null, "La contraseña y la confirmación de contraseña no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);

        } else {

            alreadyExist = checkingIfUserExist(name);

            if (!alreadyExist) {

                creatingNewUser(name, passwd, USER_TYPE);
                DIALOG.dispose();

            } else {

                JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}
