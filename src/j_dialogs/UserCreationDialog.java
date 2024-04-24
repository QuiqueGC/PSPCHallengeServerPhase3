package j_dialogs;

import listeners.UserCreationDialogListener;
import utils.SpellBook;

import javax.swing.*;

public class UserCreationDialog extends JDialog {

    private final JPanel CONTENT_PANE;
    private JTextField nameField;
    private JPasswordField passwdField;
    private JPasswordField passwdField2;
    private final int USER_TYPE;


    public UserCreationDialog(int userType) {

        this.USER_TYPE = userType;

        if (USER_TYPE == 2) {

            SpellBook.creatingStandardJDialog("Crear nuevo usuario", this);

        } else {

            SpellBook.creatingStandardJDialog("Crear nuevo administrador", this);
        }


        CONTENT_PANE = SpellBook.creatingStandardPanelForDialog();

        textFieldsCreation();

        buttonCreation();

        setContentPane(CONTENT_PANE);
    }


    private void buttonCreation() {
        JButton button = new JButton();

        button.setSize(100, 20);
        button.setText("Aceptar");
        button.setLocation(CONTENT_PANE.getWidth() / 2 - button.getWidth() / 2, 120);
        button.addMouseListener(new UserCreationDialogListener(this, nameField, passwdField, passwdField2, USER_TYPE));
        CONTENT_PANE.add(button);

    }

    private void textFieldsCreation() {

        nameCreation();

        passwdCreation();

        secondPasswdCreation();
    }

    private void nameCreation() {
        JLabel labelTxt = SpellBook.textLabelCreation(CONTENT_PANE, "Nombre", 20, 20);
        nameField = SpellBook.txtFieldCreation(CONTENT_PANE, labelTxt.getX() + labelTxt.getWidth(), 20);
    }


    private void passwdCreation() {
        JLabel labelTxt = SpellBook.textLabelCreation(CONTENT_PANE, "Contraseña", 20, 50);
        passwdField = SpellBook.passwdFieldCreation(CONTENT_PANE, labelTxt.getX() + labelTxt.getWidth(), 50);

    }

    private void secondPasswdCreation() {
        JLabel labelTxt = SpellBook.textLabelCreation(CONTENT_PANE, "Repita contraseña", 20, 80);
        passwdField2 = SpellBook.passwdFieldCreation(CONTENT_PANE, labelTxt.getX() + labelTxt.getWidth(), 80);
    }


}
