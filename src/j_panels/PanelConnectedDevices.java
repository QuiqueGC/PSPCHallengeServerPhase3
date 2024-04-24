package j_panels;

import data_classes.UserConnected;
import p_s_p_challenge.PSPChallenge;
import utils.SpellBook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelConnectedDevices extends JPanel {

    UserConnected userConnected;
    JPanel panelAdmin;
    JLabel lblConnectionTxt;

    public PanelConnectedDevices(JPanel panelAdmin, JLabel lblConnectionTxt) {

        this.lblConnectionTxt = lblConnectionTxt;
        this.panelAdmin = panelAdmin;
        SpellBook.creatingStandardPanelForFrame(this);


        addingButtons();

        addingJTable();

        PSPChallenge.frame.setTitle("Lista de dispositivos instalados");
    }


    private void addingJTable() {

        // Convertir la lista de objetos a un arreglo bidimensional para la JTable
        Object[][] tableData = new Object[PSPChallenge.usersConnected.size()][2];

        for (int i = 0; i < PSPChallenge.usersConnected.size(); i++) {
            UserConnected uc = PSPChallenge.usersConnected.get(i);

            tableData[i][0] = uc.getIp();
            tableData[i][1] = uc.getName();
        }

        // Nombres de las columnas
        String[] columns = {"IP", "Nombre usuario"};

        // Crear el modelo de la tabla
        DefaultTableModel devicesTable = new DefaultTableModel(tableData, columns);

        // Crear la tabla con el modelo
        JTable table = new JTable(devicesTable);

        // Crear un JScrollPane y agregar la tabla a él
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setSize(500, 300);
        scrollPane.setLocation(this.getWidth() / 2 - scrollPane.getWidth() / 2, 20);
        this.add(scrollPane);


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                userConnected = PSPChallenge.usersConnected.get(table.getSelectedRow());
            }
        });

    }


    private void addingButtons() {

        addingBackButton();

        addingSelectionButton();

    }

    private void addingSelectionButton() {
        JButton selectionButton = new JButton();

        selectionButton.setSize(220, 50);
        selectionButton.setText("Seleccionar dispositivo");
        selectionButton.setLocation(80, 360);
        this.add(selectionButton);
        selectionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                selectUserConnected();
            }
        });
    }

    private void selectUserConnected() {

        if(userConnected != null){
            PSPChallenge.userConnected = this.userConnected;
            lblConnectionTxt.setText(PSPChallenge.userConnected.showData());
            PSPChallenge.frame.setContentPane(panelAdmin);
        }else{
            JOptionPane.showMessageDialog(null, "Debes escoger un dispositivo primero", "Error", JOptionPane.ERROR_MESSAGE);

        }
    }


    private void addingBackButton() {
        JButton backButton = new JButton();

        backButton.setSize(200, 50);
        backButton.setText("Volver");
        backButton.setLocation(350, 360);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                PSPChallenge.frame.setContentPane(panelAdmin);
            }
        });
        this.add(backButton);
    }


}
