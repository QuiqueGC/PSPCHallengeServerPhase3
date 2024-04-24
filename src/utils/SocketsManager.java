package utils;

import data_classes.User;
import data_classes.UserConnected;
import data_classes.WindowsProcess;
import p_s_p_challenge.PSPChallenge;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public abstract class SocketsManager {

    public static final int PORT = 5002;
    public static Socket socketClient;
    public static ServerSocket server;


    /**
     * Recoge la petición de registro o login del usuario
     */
    public static boolean getRegisterOrLoginPetition(String ipClient) {
        String petition;
        boolean isLoggedIn = false;
        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            petition = (String) ois.readObject();
            switch (petition){
                case "register":
                    tryToRegister();
                    break;
                case "login":
                    isLoggedIn = tryToLogin(ipClient);
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
            closeClient();
        }

        return isLoggedIn;
    }

    /**
     * Chequea si existe el usuario y si coincide el passwd y hace login
     * @param ipClient String con la ip del cliente, para mostrarla en pantalla
     * @return Boolean indicando si se ha podido hacer el login
     */
    private static boolean tryToLogin(String ipClient) {
        boolean loginSuccessful = false;
        User userToLogin = getUserFromClient();
        User foundUser;
        String msg;
        if(userToLogin != null){
           foundUser = SpellBook.lookingForUser(userToLogin.getName());
           if(foundUser != null){
               msg = SpellBook.loginClient(userToLogin.getPasswd(), foundUser);
               sendString(msg);
               if(msg.equals("Login realizado con éxito")){
                   loginSuccessful = true;
                   sendUser(foundUser);
                   PSPChallenge.userConnected = new UserConnected(foundUser.getName(), ipClient);
               }
           }else{
               sendString("El nombre de usuario no está registrado");
           }
        }else {
            sendString("Ha habido un problema de conexión");
        }
        return loginSuccessful;
    }

    /**
     * Chequea si el usuario ya existe y, en caso negativo, lo registra
     */
    private static void tryToRegister() {
        User userToRegister = getUserFromClient();
        boolean alreadyExist;
        if (userToRegister != null){
            alreadyExist = SpellBook.checkingIfUserExist(userToRegister.getName());
            if(!alreadyExist){
                SpellBook.creatingNewUser(userToRegister.getName(), userToRegister.getPasswd(), userToRegister.getUserType());
                sendString("Usuario registrado con éxito");
            }else{
                sendString("Ya existe un usuario registrado con ese nombre");
            }
        }else {
            sendString("Ha habido un problema de conexión");
        }
    }


    /**
     * Envía una respuesta al cliente
     * @param response String con la respuesta
     */
    public static void sendString(String response) {
        try {
            new ObjectOutputStream(socketClient.getOutputStream()).writeObject(response);
        } catch (IOException ex) {
            closeClient();
            System.out.println("excepción IOE");
        }
    }

    /**
     * Recibe una respuesta del server para poder mostrarla en un diálogo
     *
     * @return String con la respuesta del server
     */
    public static String getString() {
        String response = "";
        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            response = (String) ois.readObject();

        } catch (Exception e) {

            closeClient();
            System.out.println(e);
        }

        return response;
    }

    /**
     * Extrae el usuario que está intentando hacer login
     * @return User con nombre y psswd del usuario
     */
    public static User getUserFromClient() {
        User user = null;
        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            user = (User) ois.readObject();

        } catch (Exception e) {

            closeClient();
            System.out.println(e);
        }
        return user;
    }


    /**
     * Envía un objeto de tipo User al server
     *
     * @param user User que enviará al server
     */
    public static void sendUser(User user) {
        try {

            new ObjectOutputStream(socketClient.getOutputStream()).writeObject(user);

        } catch (IOException ex) {

            closeClient();
            System.out.println("excepción IOE");
        }
    }

    /**
     * Obtiene la lista de programas del cliente
     */
    public static void getPrograms() {

        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            PSPChallenge.userConnected.setInstalledPrograms((ArrayList<String>) ois.readObject());
            PSPChallenge.userConnected.setLoadingPrograms("Cargados con éxito");

        } catch (Exception e) {
            closeClient();
            System.out.println("Error cogiendo el arrayList de programas");
            System.out.println(e);
        }
    }


    /**
     * Obtiene la lista de procesos del cliente
     */
    public static void getProcesses() {

        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            PSPChallenge.userConnected.setExecutingProcesses((ArrayList<WindowsProcess>) ois.readObject());
            PSPChallenge.userConnected.setLoadingProcess("Cargados con éxito");

        } catch (Exception e) {
            closeClient();
            System.out.println("Error cogiendo el arrayList de procesos");
            System.out.println(e);
        }
    }

    /**
     * Chequea si el cliente sigue logeado
     * @return boolean con el estado del login/logout
     */
    public static boolean checkUserConnection(){
        boolean userLoggedIn = true;
        try {
            InputStream is = socketClient.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            userLoggedIn = dis.readBoolean();

        } catch (Exception e) {
            closeClient();
            System.out.println("Error cogiendo el boolean de login");
            System.out.println(e);
        }

        return  userLoggedIn;
    }

    public static void sendAdminConnection() {
        System.out.println("VALOR DEL BOOLEAN ADMIN_LOGOUT -> " + PSPChallenge.adminLogout);
        try {
            new DataOutputStream(socketClient.getOutputStream()).writeBoolean(PSPChallenge.adminLogout);

        } catch (IOException ex) {
            System.out.println("excepción IOE");
            System.out.println("FALLO ENVIANDO BOOLEAN ADMIN_LOGOUT");
        }
    }



    public static void closeClient() {
        try{
            socketClient.close();
            System.out.println("Conexión con cliente cerrada");

        }catch (Exception e){

            System.out.println(e);
        }
    }



    public static void closeServer() {
        try{
            socketClient.close();
            server.close();
            System.out.println("Conexión con cliente cerrada");
            System.out.println("Servidor cerrado");

        }catch (Exception e){

            System.out.println(e);
        }
    }

}
