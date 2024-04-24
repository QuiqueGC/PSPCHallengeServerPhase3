package listeners;

import p_s_p_challenge.PSPChallenge;
import utils.FilesRW;
import utils.SocketsManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * listener que extiende WindowAdapter para el botón de cierre de ventana
 */
public class FrameWindowListener extends WindowAdapter {
    JFrame frame;

    public FrameWindowListener(JFrame frame) {
        this.frame = frame;
    }


    /**
     * evento con la apertura de ventana
     *
     * @param e the event to be processed
     */
    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);

        FilesRW.takingUserDataFromFile(PSPChallenge.usersList);

    }


    /**
     * evento para el cierre de la ventana
     *
     * @param e the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);

        int confirmado = JOptionPane.showConfirmDialog(null,
                "Seguro del cierre?",
                "mensaje",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmado == JOptionPane.YES_OPTION) {

            SocketsManager.closeServer();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        } else {

            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        }
    }
}
