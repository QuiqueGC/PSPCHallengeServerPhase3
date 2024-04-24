package j_panels;

import j_dialogs.UserCreationDialog;
import p_s_p_challenge.PSPChallenge;
import utils.ConnectionThread;
import utils.SocketsManager;
import utils.SpellBook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelAdmin extends JPanel{

    public JLabel lblConnectionTxt;


    public PanelAdmin() {

        SpellBook.creatingStandardPanelForFrame(this);

        addInfoLabel();
        addingButtons();

        PSPChallenge.frame.setTitle("Panel de control de administrador");

        ConnectionThread conThread = new ConnectionThread(lblConnectionTxt);
        conThread.start();
    }

    private void addInfoLabel() {
        lblConnectionTxt = new JLabel();
        lblConnectionTxt.setLocation(20, 220);
        lblConnectionTxt.setSize(500,300);
        lblConnectionTxt.setForeground(Color.WHITE);
        this.add(lblConnectionTxt);
    }


    private void addingButtons() {

        addingProfileButton(this);

        addingLogoutButton();

        addingInstalledPrgButton(this);

        addingExecuteProgButton(this);

        addingNewRegularUserButton();

        addingNewAdminButton();

        addingDeleteOrModifyButton(this);
    }

    private void addingExecuteProgButton(JPanel panelAdmin) {
        JButton executingProgButton = new JButton();
        executingProgButton.setText("Procesos en ejecución");
        executingProgButton.setSize(200, 50);
        executingProgButton.setLocation(
                20,
                250);
        this.add(executingProgButton);
        executingProgButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                PSPChallenge.frame.setContentPane(new PanelExecutingPrograms(panelAdmin));
            }
        });

    }

    private void addingInstalledPrgButton(JPanel panelAdmin) {

        JButton consultButton = new JButton();
        consultButton.setText("Programas instalados");
        consultButton.setSize(200, 50);
        consultButton.setLocation(
                20,
                150);
        this.add(consultButton);
        consultButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                PSPChallenge.frame.setContentPane(new PanelInstalledPrograms(panelAdmin));
            }
        });
    }


    private void addingProfileButton(JPanel panelAdmin) {
        JButton profileButton = new JButton();
        profileButton.setText("Editar perfil");
        profileButton.setSize(200, 50);
        profileButton.setLocation(
                20,
                50);
        this.add(profileButton);
        profileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                PSPChallenge.frame.setContentPane(new PanelEditProfile(panelAdmin));
            }
        });
    }

    private void addingLogoutButton() {

        JButton logoutButton = new JButton();
        logoutButton.setText("Cerrar sesión");
        logoutButton.setSize(150, 20);
        logoutButton.setLocation(
                445,
                420);
        this.add(logoutButton);
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                PSPChallenge.adminLogout = true;
                JOptionPane.showMessageDialog(null, "Solicitud de cierre de sesión en proceso. Se ejecutará al finalizar el ciclo, para no perder información", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void addingDeleteOrModifyButton(JPanel panelAdmin) {
        JButton deleteModifyButton = new JButton();
        deleteModifyButton.setText("Borrar/Modificar usuario");
        deleteModifyButton.setSize(200, 50);
        deleteModifyButton.setLocation(
                400,
                250);
        this.add(deleteModifyButton);
        deleteModifyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                PSPChallenge.frame.setContentPane(new PanelDeleteModifyUsers(panelAdmin));

            }
        });
    }

    private void addingNewAdminButton() {
        JButton newAdminButton = new JButton();
        newAdminButton.setText("Añadir Administrador");
        newAdminButton.setSize(200, 50);
        newAdminButton.setLocation(
                400,
                150);
        this.add(newAdminButton);
        newAdminButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                UserCreationDialog dialog = new UserCreationDialog(1);
                dialog.setVisible(true);
            }
        });
    }


    private void addingNewRegularUserButton() {
        JButton newUserButton = new JButton();
        newUserButton.setText("Añadir usuario");
        newUserButton.setSize(200, 50);
        newUserButton.setLocation(
                400,
                50);
        this.add(newUserButton);
        newUserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                UserCreationDialog dialog = new UserCreationDialog(2);
                dialog.setVisible(true);
            }
        });
    }
}
