package j_panels;

import p_s_p_challenge.PSPChallenge;
import utils.SpellBook;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelInstalledPrograms extends JPanel {


    JPanel panelAdmin;

    public PanelInstalledPrograms(JPanel panelAdmin) {

        this.panelAdmin = panelAdmin;
        SpellBook.creatingStandardPanelForFrame(this);

        addingButtons();

        addingLabels();

        PSPChallenge.frame.setTitle("Lista de programas instalados");
    }




    private void addingLabels() {

        addingJList();

    }


    private void addingJList() {

        if (PSPChallenge.userConnected != null && PSPChallenge.userConnected.getInstalledPrograms() != null){
            DefaultListModel defaultListModel = new DefaultListModel();
            JList jListInstalledPrograms = new JList();
            jListInstalledPrograms.setModel(defaultListModel);


            //pongo la variable i a 1 para que no me incluya el título name del texto extraído
            for (int i = 1; i < PSPChallenge.userConnected.getInstalledPrograms().size(); i++) {
                defaultListModel.addElement(PSPChallenge.userConnected.getInstalledPrograms().get(i));
            }

            JScrollPane listScroller = new JScrollPane();
            listScroller.setViewportView(jListInstalledPrograms);
            jListInstalledPrograms.setLayoutOrientation(JList.VERTICAL);
            listScroller.setSize(500, 300);
            listScroller.setLocation(this.getWidth() / 2 - listScroller.getWidth() / 2, 20);

            this.add(listScroller);
        }else{
            JLabel lblNoPrograms = new JLabel();
            lblNoPrograms.setText("Todavía no se han cargado los programas instalados del usuario. Vuelva al panel de administración");
            lblNoPrograms.setSize(500, 300);
            lblNoPrograms.setForeground(Color.white);
            lblNoPrograms.setLocation(this.getWidth() / 2 - lblNoPrograms.getWidth() / 2, 20);
            this.add(lblNoPrograms);
        }

    }

    private void addingButtons() {

        SpellBook.addingBackButton(this, panelAdmin);
    }
}
