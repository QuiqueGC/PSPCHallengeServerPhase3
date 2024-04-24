package p_s_p_challenge;

import data_classes.User;
import data_classes.UserConnected;
import listeners.FrameWindowListener;
import j_panels.PanelMain;
import utils.SocketsManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import static utils.SocketsManager.PORT;


public class PSPChallenge {
    public static ArrayList<User> usersList = new ArrayList<>();
    public static User actualUser;

    public static JFrame frame;

    public static boolean adminLogout = false;

    public static UserConnected userConnected;

    public PSPChallenge() {
    }


    public static void main(String[] args) {

        try {
            SocketsManager.server = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        frame = new JFrame("Gestión de usuarios y procesos");


        // añadimos un windowListener de apertura y cierre de ventana
        frame.addWindowListener(new FrameWindowListener(frame));


        frame.setContentPane(new PanelMain());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        //vuelve visible el frame
        frame.setVisible(true);

        //Coloca la ventana en el centro de la pantalla
        frame.setLocationRelativeTo(null);

        //cambia el icono de la aplicación
        Toolkit pantalla = Toolkit.getDefaultToolkit();
        Image icon = pantalla.getImage("src/resources/img/server.png");
        frame.setIconImage(icon);
    }


}