package j_panels;

import listeners.PanelEditProfileListener;
import p_s_p_challenge.PSPChallenge;
import utils.SpellBook;

import javax.swing.*;

public class PanelEditProfile extends JPanel {

    JTextField nameField;
    JPasswordField newPasswdField;
    JPasswordField newPasswdField2;
    JPasswordField oldPasswdField;
    JPanel panelAdmin;


    public PanelEditProfile(JPanel panelAdmin) {

        this.panelAdmin = panelAdmin;
        SpellBook.creatingStandardPanelForFrame(this);

        addingLabels();

        addingButtons();

        PSPChallenge.frame.setTitle("Panel de modificación de información de usuario");
    }

    private void addingButtons() {

        addingConfirmationButton();

        SpellBook.addingBackButton(this, panelAdmin);
    }


    private void addingConfirmationButton() {
        JButton button = new JButton();

        button.setSize(200, 50);
        button.setText("Aceptar cambios");
        button.setLocation(this.getWidth() / 2 - button.getWidth() / 2, 300);
        button.addMouseListener(new PanelEditProfileListener(nameField, newPasswdField, newPasswdField2, oldPasswdField, panelAdmin));
        this.add(button);
    }

    private void addingLabels() {

        addingNameLabels();

        addingNewPasswdLabels();

        addingNewPasswdRepetitionLabels();

        addingOldPasswdLabel();


    }

    private void addingOldPasswdLabel() {
        JLabel labelTxt = SpellBook.textLabelCreation(this, "Antigua contraseña", 140, 200);
        oldPasswdField = SpellBook.passwdFieldCreation(this, labelTxt.getX() + labelTxt.getWidth(), 200);
    }

    private void addingNewPasswdRepetitionLabels() {
        JLabel labelTxt = SpellBook.textLabelCreation(this, "Repita nueva contraseña", 140, 160);
        newPasswdField2 = SpellBook.passwdFieldCreation(this, labelTxt.getX() + labelTxt.getWidth(), 160);
    }

    private void addingNewPasswdLabels() {
        JLabel labelTxt = SpellBook.textLabelCreation(this, "Nueva contraseña", 140, 120);
        newPasswdField = SpellBook.passwdFieldCreation(this, labelTxt.getX() + labelTxt.getWidth(), 120);
    }


    private void addingNameLabels() {
        JLabel labelTxt = SpellBook.textLabelCreation(this, "Nombre", 140, 80);
        nameField = SpellBook.txtFieldCreation(this, labelTxt.getX() + labelTxt.getWidth(), 80);
        nameField.setText(PSPChallenge.actualUser.getName());
    }
}
