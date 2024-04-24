package utils;

import data_classes.User;
import data_classes.UserConnected;
import j_panels.PanelMain;
import p_s_p_challenge.PSPChallenge;
import javax.swing.*;
import java.net.Socket;


public class ConnectionThread extends Thread {

    private final JLabel lblConnectionTxt;
    boolean isClientConnected;
    boolean isLoggedIn;

    boolean isLogin = true;
    Socket socketClient;
    UserConnected userOfThisThread;

    public ConnectionThread(JLabel lblConnectionTxt){
        super();
        this.lblConnectionTxt = lblConnectionTxt;
        this.userOfThisThread = new UserConnected();
    }

    @Override
    public void run() {
        super.run();
        PSPChallenge.adminLogout = false;
        isClientConnected = false;

        do{
            if(socketClient != null){
                isClientConnected = !socketClient.isClosed();
            }

            System.out.println("EMPIEZA EL BUCLE");
            System.out.println("VALOR DEL BOOLEAN IS_LOGGED_IN -> " + isLoggedIn);
            System.out.println("VALOR DEL BOOLEAN IS_CLIENT_CONNECTED -> " + isClientConnected);
            if(!isClientConnected){

                System.out.println("NO ESTÁ CONECTADO, ASÍ QUE SE CONECTA");
                establishConnection();

                isClientConnected = true;
            }
            if(!socketClient.isClosed()){
            System.out.println("CHEQUEA EL LOGIN");
                awaitForClientLogin();
            }

            if(!socketClient.isClosed()){
            System.out.println("COGE PROGRAMAS");
            SocketsManager.getPrograms(socketClient);
            if(userOfThisThread.equals(PSPChallenge.userConnected)){
                lblConnectionTxt.setText(PSPChallenge.userConnected.showData());
            }

            }

            if(!socketClient.isClosed()) {
            System.out.println("COGE PROCESOS");
                SocketsManager.getProcesses(socketClient);
                if(userOfThisThread.equals(PSPChallenge.userConnected)) {
                    lblConnectionTxt.setText(PSPChallenge.userConnected.showData());
                }
            }

            if(!socketClient.isClosed()) {
            System.out.println("ENVÍA PETICIÓN A CLIENTE");
                sendOrderToClient();
            }

            if(!socketClient.isClosed()) {
            System.out.println("RECIBE PETICIÓN DE CLIENTE");
                receiveOrderFromClient();
            }

            if(!socketClient.isClosed()) {
            System.out.println("CHEQUEA SI ESTÁ LOGEADO");
                checkIfClientStillConnected();
            }

            if(socketClient.isClosed()){
                isLoggedIn = false;
                PSPChallenge.usersConnected.remove(userOfThisThread);
            }

            System.out.println("ENVÍA SI EL ADMIN SIGUE LOGEADO");
            SocketsManager.sendAdminConnection(socketClient);

        }while (!PSPChallenge.adminLogout);

        System.out.println("EL ADMIN HA HECHO LOGOUT Y SE HA SALIDO DEL BUCLE PRINCIPAL DEL THREAD");
        SocketsManager.closeClient(socketClient);
        PSPChallenge.frame.setContentPane(new PanelMain());
        PSPChallenge.actualUser = null;
    }

    /**
     * Chequea si el usuario ha cerrado sesión y actualiza la interfaz
     */
    private void checkIfClientStillConnected() {
        isLoggedIn = SocketsManager.checkUserConnection(socketClient);
        if(!isLoggedIn){
            JOptionPane.showMessageDialog(null, userOfThisThread.getName() + " ha cerrado sesión", "Información", JOptionPane.INFORMATION_MESSAGE);
            PSPChallenge.usersConnected.remove(userOfThisThread);
            userOfThisThread.clearDataAfterOrder();
            if(userOfThisThread.equals(PSPChallenge.userConnected)) {
                lblConnectionTxt.setText(
                        "<html>Conexión establecida!<br><br>" +
                                " IP del cliente: " + socketClient.getInetAddress().getHostAddress() + "<html>");
            }
            isLogin = true;
        }
    }

    /**
     * Recibe la petición de modificación de usuario del cliente
     * y lo actualiza
     */
    private void receiveOrderFromClient() {
        String clientOrder = SocketsManager.getString(socketClient);
        int indexToDelete;
        if(clientOrder.equals("changeUser")){

            User userToChange = SocketsManager.getUserFromClient(socketClient);

            indexToDelete = lookingForUser();

            updateUser(userToChange, indexToDelete);

            showAndSendInfo();

        }
    }

    /**
     * Busca al usuario en la lista de usuarios y obtiene su index
     * @return int con el index del usuario
     */
    private int lookingForUser() {
        int indexToDelete = -1;
        for (User user :
                PSPChallenge.usersList) {
            if(user.getName().equals(userOfThisThread.getName())){
                indexToDelete = PSPChallenge.usersList.indexOf(user);
            }
        }
        return indexToDelete;
    }

    /**
     * Actualiza la lista de usuarios y sobreescribe el fichero
     * @param userToChange User que será modificado en la lista
     * @param indexToDelete int con el índice del usuario que hay que modificar
     */
    private void updateUser(User userToChange, int indexToDelete) {
        PSPChallenge.usersList.remove(indexToDelete);
        PSPChallenge.usersList.add(userToChange);
        FilesRW.overwritingFile();
        userOfThisThread.setName(userToChange.getName());
    }

    /**
     * Muestra el diálogo de información de actualización de usuario y le envía la respuesta al cliente
     */
    private void showAndSendInfo() {
        JOptionPane.showMessageDialog(null, userOfThisThread.getName() + " ha cambiado de nombre o contraseña", "Información", JOptionPane.INFORMATION_MESSAGE);
        SocketsManager.sendString("Usuario actualizado con éxito", socketClient);
        if(userOfThisThread.equals(PSPChallenge.userConnected)) {
            lblConnectionTxt.setText(userOfThisThread.showData());
        }
    }



    /**
     * Envía la orden (o nada) al cliente
     */
    private void sendOrderToClient() {

        SocketsManager.sendString(userOfThisThread.getOrderToClient(), socketClient);

        if(userOfThisThread.getOrderToClient().equals("stopProcess")) {

            String response;
            SocketsManager.sendString(userOfThisThread.getProcessPID(), socketClient);
            response = SocketsManager.getString(socketClient);
            JOptionPane.showMessageDialog(null, response, "Información", JOptionPane.INFORMATION_MESSAGE);
            userOfThisThread.clearDataAfterOrder();

            if(userOfThisThread.equals(PSPChallenge.userConnected)) {
                lblConnectionTxt.setText(PSPChallenge.userConnected.showData());
            }
        }
    }

    /**
     * Espera a que se conecte un cliente
     */
    private void awaitForClientLogin() {
        while(!isLoggedIn && !socketClient.isClosed()){
            System.out.println("NO ESTÁ LOGEADO");
            isLoggedIn = SocketsManager.getRegisterOrLoginPetition(socketClient.getInetAddress().getHostAddress(), userOfThisThread, socketClient);
        }

        System.out.println("SALIÓ DEL BUCLE DE LOGIN");
        if(PSPChallenge.userConnected != null && PSPChallenge.userConnected.equals(userOfThisThread)){
            lblConnectionTxt.setText(PSPChallenge.userConnected.showData());
        }
        if(isLogin){
            JOptionPane.showMessageDialog(null, userOfThisThread.getName() + " ha iniciado sesión", "Información", JOptionPane.INFORMATION_MESSAGE);
            isLogin = false;
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
            System.out.println(e);
        }
    }

}
