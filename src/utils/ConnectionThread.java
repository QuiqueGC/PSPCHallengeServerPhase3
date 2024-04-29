package utils;

import data_classes.User;
import data_classes.UserConnected;
import j_panels.PanelMain;
import p_s_p_challenge.PSPChallenge;

import javax.swing.*;
import java.net.Socket;


public class ConnectionThread extends Thread {

    private final JLabel lblConnectionTxt;
    boolean isClientConnected, isLoggedIn;
    boolean hasToShowLoginMsg = true;
    Socket socketClient;
    UserConnected userOfThisThread;

    public ConnectionThread(JLabel lblConnectionTxt) {
        super();
        this.lblConnectionTxt = lblConnectionTxt;
        this.userOfThisThread = new UserConnected();
    }

    @Override
    public void run() {
        super.run();
        PSPChallenge.adminLogout = false;
        isClientConnected = false;

        do {
            if (socketClient != null) {
                isClientConnected = !socketClient.isClosed();
            }

            if (!isClientConnected) {
                establishConnection();
                isClientConnected = true;
            }

            if (!socketClient.isClosed()) {
                awaitForClientLogin();
            }

            if (!socketClient.isClosed()) {
                SocketsManager.getPrograms(socketClient);
                if (userOfThisThread.equals(PSPChallenge.userConnected)) {
                    lblConnectionTxt.setText(PSPChallenge.userConnected.showData());
                }

            }

            if (!socketClient.isClosed()) {
                SocketsManager.getProcesses(socketClient);
                if (userOfThisThread.equals(PSPChallenge.userConnected)) {
                    lblConnectionTxt.setText(PSPChallenge.userConnected.showData());
                }
            }

            if (!socketClient.isClosed()) {
                sendOrderToClient();
            }

            if (!socketClient.isClosed()) {
                receiveOrderFromClient();
            }

            if (!socketClient.isClosed()) {
                checkIfClientStillConnected();
            }

            if (socketClient.isClosed()) {
                isLoggedIn = false;
                PSPChallenge.usersConnected.remove(userOfThisThread);
            }

            SocketsManager.sendAdminConnection(socketClient);

        } while (!PSPChallenge.adminLogout);

        SocketsManager.closeClient(socketClient);
        PSPChallenge.frame.setContentPane(new PanelMain());
        PSPChallenge.actualUser = null;
    }

    /**
     * Chequea si el usuario ha cerrado sesión y actualiza la interfaz
     */
    private void checkIfClientStillConnected() {
        isLoggedIn = SocketsManager.checkUserConnection(socketClient);
        if (!isLoggedIn) {
            JOptionPane.showMessageDialog(
                    null,
                    userOfThisThread.getName() + " ha cerrado sesión", "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
            PSPChallenge.usersConnected.remove(userOfThisThread);
            userOfThisThread.clearDataAfterOrder();

            if (userOfThisThread.equals(PSPChallenge.userConnected)) {
                lblConnectionTxt.setText(
                        "<html>Conexión establecida!<br><br>" +
                                " IP del cliente: " + socketClient.getInetAddress().getHostAddress() + "<html>");
            }
            hasToShowLoginMsg = true;
        }
    }

    /**
     * Recibe la petición de modificación de usuario del cliente
     * y lo actualiza
     */
    private void receiveOrderFromClient() {
        String clientOrder = SocketsManager.getString(socketClient);
        int indexToDelete;
        if (clientOrder.equals("changeUser")) {

            User userToChange = SocketsManager.getUserFromClient(socketClient);


            synchronized (PSPChallenge.usersList){
                indexToDelete = lookingForUser();
                updateUser(userToChange, indexToDelete);
            }

            showAndSendInfo();
        }
    }

    /**
     * Busca al usuario en la lista de usuarios y obtiene su index
     *
     * @return int con el index del usuario
     */
    private int lookingForUser() {
        int indexToDelete = -1;
        for (User user :
                PSPChallenge.usersList) {
            if (user.getName().equals(userOfThisThread.getName())) {
                indexToDelete = PSPChallenge.usersList.indexOf(user);
            }
        }
        return indexToDelete;
    }

    /**
     * Actualiza la lista de usuarios y sobreescribe el fichero
     *
     * @param userToChange  User que será modificado en la lista
     * @param indexToDelete int con el índice del usuario que hay que modificar
     */
    private void updateUser(User userToChange, int indexToDelete) {
        if(indexToDelete != -1){
            PSPChallenge.usersList.remove(indexToDelete);
            PSPChallenge.usersList.add(userToChange);
            FilesRW.overwritingFile();
            userOfThisThread.setName(userToChange.getName());
        }else{
            JOptionPane.showMessageDialog(null, "No se ha podido sobreescribir el usuario", "Información", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    /**
     * Muestra el diálogo de información de actualización de usuario y le envía la respuesta al cliente
     */
    private void showAndSendInfo() {
        JOptionPane.showMessageDialog(null, userOfThisThread.getName() + " ha cambiado de nombre o contraseña", "Información", JOptionPane.INFORMATION_MESSAGE);
        SocketsManager.sendString("Usuario actualizado con éxito", socketClient);
        if (userOfThisThread.equals(PSPChallenge.userConnected)) {
            lblConnectionTxt.setText(userOfThisThread.showData());
        }
    }


    /**
     * Envía la orden (o nada) al cliente
     */
    private void sendOrderToClient() {

        SocketsManager.sendString(userOfThisThread.getOrderToClient(), socketClient);

        if (userOfThisThread.getOrderToClient().equals("stopProcess")) {

            String response;
            SocketsManager.sendString(userOfThisThread.getProcessPID(), socketClient);
            response = SocketsManager.getString(socketClient);
            JOptionPane.showMessageDialog(null, response, "Información", JOptionPane.INFORMATION_MESSAGE);
            userOfThisThread.clearDataAfterOrder();

            if (userOfThisThread.equals(PSPChallenge.userConnected)) {
                lblConnectionTxt.setText(PSPChallenge.userConnected.showData());
            }
        }
    }

    /**
     * Espera a que se conecte un cliente
     */
    private void awaitForClientLogin() {
        while (!isLoggedIn && !socketClient.isClosed()) {
            isLoggedIn = SocketsManager.getRegisterOrLoginPetition(socketClient.getInetAddress().getHostAddress(), userOfThisThread, socketClient);
        }

        if (PSPChallenge.userConnected != null && PSPChallenge.userConnected.equals(userOfThisThread)) {
            lblConnectionTxt.setText(PSPChallenge.userConnected.showData());
        }
        if (hasToShowLoginMsg) {
            JOptionPane.showMessageDialog(null, userOfThisThread.getName() + " ha iniciado sesión", "Información", JOptionPane.INFORMATION_MESSAGE);
            hasToShowLoginMsg = false;
        }
    }


    /**
     * establece la conexión con el cliente
     */
    private void establishConnection() {
        try {
            lblConnectionTxt.setText("Socket servidor abierto esperando conexiones...");

            socketClient = SocketsManager.server.accept();
            lblConnectionTxt.setText(
                    "<html>Conexión establecida!<br><br>" +
                            " IP del cliente: " + socketClient.getInetAddress().getHostAddress() + "<html>");

        } catch (Exception e) {
            System.out.println("Error estableciendo conexión");
        }
    }
}
