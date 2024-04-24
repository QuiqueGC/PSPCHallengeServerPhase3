package j_panels;

import data_classes.User;
import p_s_p_challenge.PSPChallenge;
import utils.BlowFishManager;
import utils.FilesRW;
import utils.SpellBook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelDeleteModifyUsers extends JPanel {

    User userSelected;
    JPanel panelAdmin;

    public PanelDeleteModifyUsers(JPanel panelAdmin) {

        this.panelAdmin = panelAdmin;
        SpellBook.creatingStandardPanelForFrame(this);


        addingButtons();

        addingUsersTable();

        PSPChallenge.frame.setTitle("Panel de modificación o borrado de usuario");
    }

    private void addingButtons() {

        addingBackButton(panelAdmin);

        addingDeleteButton();

        addingModifyButton();

        addingPasswdButton();

    }

    private void addingModifyButton() {
        JButton deleteButton = new JButton();

        deleteButton.setSize(220, 40);
        deleteButton.setText("Modificar seleccionado");
        deleteButton.setLocation(80, 340);
        this.add(deleteButton);
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                checkingAndModifyingUser();
            }
        });
    }


    private void checkingAndModifyingUser() {

        if (userSelected != null &&
                userSelected.getUserType() != 0 &&
                !userSelected.equals(PSPChallenge.actualUser)) {

            changingUserType();

            FilesRW.overwritingFile();

            PSPChallenge.frame.setContentPane(new PanelDeleteModifyUsers(panelAdmin));

        } else if (userSelected == null) {

            JOptionPane.showMessageDialog(null, "No has seleccionado ningún usuario.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (userSelected.getUserType() == 0) {

            JOptionPane.showMessageDialog(null, "No puedes modificar al administrador principal.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (userSelected.equals(PSPChallenge.actualUser)) {

            userSelected.setUserType(2);

            FilesRW.overwritingFile();

            JOptionPane.showMessageDialog(null, "Se ha modificado el tipo de usuario.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            PSPChallenge.frame.setContentPane(panelAdmin);
        }
    }


    private void addingDeleteButton() {
        JButton deleteButton = new JButton();

        deleteButton.setSize(220, 40);
        deleteButton.setText("Borrar seleccionado");
        deleteButton.setLocation(80, 400);
        this.add(deleteButton);

        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);


                checkingAndDeletingUser();
            }
        });
    }

    private void addingBackButton(JPanel panelAdmin) {
        JButton backButton = new JButton();

        backButton.setSize(220, 40);
        backButton.setText("Volver");
        backButton.setLocation(350, 400);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                PSPChallenge.frame.setContentPane(panelAdmin);
            }
        });
        this.add(backButton);
    }

    private void addingPasswdButton() {
        JButton passwdButton = new JButton();

        passwdButton.setSize(220, 40);
        passwdButton.setText("Cambiar contraseña");
        passwdButton.setLocation(350, 340);
        //passwdButton.addMouseListener(new ChangePasswdListener());
        this.add(passwdButton);
        passwdButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                changingPasswd();
            }
        });
    }

    private void changingPasswd() {
        if (userSelected != null) {
           String newPasswd = JOptionPane.showInputDialog(null, "Introduce nueva contraseña", "Crear nueva contraseña", JOptionPane.OK_CANCEL_OPTION);

           if(newPasswd != null){
               String encryptedPasswd = BlowFishManager.encryptingPasswd(newPasswd);
               userSelected.setPasswd(encryptedPasswd);
               FilesRW.overwritingFile();
               JOptionPane.showMessageDialog(null, "La contraseña se ha modificado con éxito.", "Error", JOptionPane.INFORMATION_MESSAGE);
           }

        } else {
            JOptionPane.showMessageDialog(null, "No has seleccionado ningún usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkingAndDeletingUser() {

        if (userSelected != null &&
                userSelected.getUserType() != 0 &&
                !userSelected.equals(PSPChallenge.actualUser)) {

            deletingUser();

        } else if (userSelected == null) {

            JOptionPane.showMessageDialog(null, "No has seleccionado ningún usuario.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (userSelected.getUserType() == 0) {

            JOptionPane.showMessageDialog(null, "No puedes borrar al administrador principal.", "Error", JOptionPane.ERROR_MESSAGE);

        } else if (userSelected.equals(PSPChallenge.actualUser)) {

            deletingActualUser();

        }
    }

    private void deletingActualUser() {

        PSPChallenge.actualUser = null;

        PSPChallenge.usersList.remove(userSelected);

        FilesRW.overwritingFile();

        JOptionPane.showMessageDialog(null, "Usuario borrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        PSPChallenge.frame.setContentPane(new PanelMain());
    }

    private void deletingUser() {

        PSPChallenge.usersList.remove(userSelected);

        FilesRW.overwritingFile();

        JOptionPane.showMessageDialog(null, "Usuario borrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        PSPChallenge.frame.setContentPane(new PanelDeleteModifyUsers(panelAdmin));
    }


    private void addingUsersTable() {
        // Convertir la lista de objetos a un arreglo bidimensional para la JTable
        Object[][] tableData = new Object[PSPChallenge.usersList.size()][2];

        for (int i = 0; i < PSPChallenge.usersList.size(); i++) {
            User user = PSPChallenge.usersList.get(i);

            tableData[i][0] = user.getName();
            tableData[i][1] = user.getUserType() == 2 ? "Usuario" : "Administrador";
        }

        // Nombres de las columnas
        String[] columns = {"Nombre", "Tipo de usuario"};

        // Crear el modelo de la tabla
        DefaultTableModel defaultTableModel = new DefaultTableModel(tableData, columns);

        // Crear la tabla con el modelo
        JTable table = new JTable(defaultTableModel);

        // Crear un JScrollPane y agregar la tabla a él
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setSize(500, 300);
        scrollPane.setLocation(this.getWidth() / 2 - scrollPane.getWidth() / 2, 20);
        this.add(scrollPane);


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                userSelected = PSPChallenge.usersList.get(table.getSelectedRow());
            }
        });
    }

    private void changingUserType() {

        switch (userSelected.getUserType()) {

            case 1:
                userSelected.setUserType(2);
                break;
            case 2:
                userSelected.setUserType(1);
                break;
        }
        JOptionPane.showMessageDialog(null, "Se ha modificado el tipo de usuario.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
