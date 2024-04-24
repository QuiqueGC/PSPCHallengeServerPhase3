package utils;

import data_classes.User;
import data_classes.UserConnected;
import data_classes.WindowsProcess;
import p_s_p_challenge.PSPChallenge;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public abstract class SocketsManager {
    public static final int PORT = 5002;
    public static ServerSocket server;


    /**
     * Recoge la petición de registro o login del usuario
     */
    public static boolean getRegisterOrLoginPetition(String ipClient, UserConnected userOfThread, Socket socketClient) {
        String petition;
        boolean isLoggedIn = false;
        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            petition = (String) ois.readObject();

            switch (petition) {
                case "register" -> tryToRegister(socketClient);
                case "login" -> isLoggedIn = tryToLogin(ipClient, userOfThread, socketClient);
            }

        } catch (Exception e) {
            System.out.println("Error recibiendo login/registro");
            closeClient(socketClient);
        }

        return isLoggedIn;
    }

    /**
     * Chequea si existe el usuario y si coincide el passwd y hace login
     *
     * @param ipClient String con la ip del cliente, para mostrarla en pantalla
     * @return Boolean indicando si se ha podido hacer el login
     */
    private static boolean tryToLogin(String ipClient, UserConnected userOfThread, Socket socketClient) {
        boolean loginSuccessful = false;
        User userToLogin = getUserFromClient(socketClient);
        User foundUser;
        String msg;

        if (userToLogin != null) {
            foundUser = SpellBook.lookingForUser(userToLogin.getName());

            if (foundUser != null) {
                msg = SpellBook.loginClient(userToLogin.getPasswd(), foundUser);
                sendString(msg, socketClient);

                if (msg.equals("Login realizado con éxito")) {
                    loginSuccessful = true;
                    sendUser(foundUser, socketClient);
                    userOfThread.setName(foundUser.getName());
                    userOfThread.setIp(ipClient);
                    PSPChallenge.usersConnected.add(userOfThread);

                    if (PSPChallenge.userConnected == null) {
                        PSPChallenge.userConnected = userOfThread;
                    }
                }
            } else {
                sendString("El nombre de usuario no está registrado", socketClient);
            }
        } else {
            sendString("Ha habido un problema de conexión", socketClient);
        }
        return loginSuccessful;
    }

    /**
     * Chequea si el usuario ya existe y, en caso negativo, lo registra
     */
    private static void tryToRegister(Socket socketClient) {
        User userToRegister = getUserFromClient(socketClient);
        boolean alreadyExist;

        if (userToRegister != null) {
            alreadyExist = SpellBook.checkingIfUserExist(userToRegister.getName());

            if (!alreadyExist) {
                SpellBook.creatingNewUser(userToRegister.getName(), userToRegister.getPasswd(), userToRegister.getUserType());
                sendString("Usuario registrado con éxito", socketClient);
            } else {
                sendString("Ya existe un usuario registrado con ese nombre", socketClient);
            }

        } else {
            sendString("Ha habido un problema de conexión", socketClient);
        }
    }

    /**
     * Envía una respuesta al cliente
     *
     * @param response String con la respuesta
     */
    public static void sendString(String response, Socket socketClient) {
        try {
            new ObjectOutputStream(socketClient.getOutputStream()).writeObject(response);

        } catch (IOException ex) {
            closeClient(socketClient);
            System.out.println("excepción IOE");
        }
    }

    /**
     * Recibe una respuesta del server para poder mostrarla en un diálogo
     *
     * @return String con la respuesta del server
     */
    public static String getString(Socket socketClient) {
        String response = "";
        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            response = (String) ois.readObject();

        } catch (Exception e) {
            closeClient(socketClient);
            System.out.println("Error recibiendo String");
        }
        return response;
    }

    /**
     * Extrae el usuario que está intentando hacer login
     *
     * @return User con nombre y psswd del usuario
     */
    public static User getUserFromClient(Socket socketClient) {
        User user = null;

        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            user = (User) ois.readObject();

        } catch (Exception e) {
            closeClient(socketClient);
            System.out.println("Error recibiendo User");
        }
        return user;
    }

    /**
     * Envía un objeto de tipo User al server
     *
     * @param user User que enviará al server
     */
    public static void sendUser(User user, Socket socketClient) {
        try {
            new ObjectOutputStream(socketClient.getOutputStream()).writeObject(user);

        } catch (IOException ex) {
            closeClient(socketClient);
            System.out.println("excepción IOE");
        }
    }

    /**
     * Obtiene la lista de programas del cliente
     */
    public static void getPrograms(Socket socketClient) {
        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            PSPChallenge.userConnected.setInstalledPrograms((ArrayList<String>) ois.readObject());
            PSPChallenge.userConnected.setLoadingPrograms("Cargados con éxito");

        } catch (Exception e) {
            closeClient(socketClient);
            System.out.println("Error cogiendo el arrayList de programas");
        }
    }

    /**
     * Obtiene la lista de procesos del cliente
     */
    public static void getProcesses(Socket socketClient) {
        try {
            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            PSPChallenge.userConnected.setExecutingProcesses((ArrayList<WindowsProcess>) ois.readObject());
            PSPChallenge.userConnected.setLoadingProcess("Cargados con éxito");

        } catch (Exception e) {
            closeClient(socketClient);
            System.out.println("Error cogiendo el arrayList de procesos");
        }
    }

    /**
     * Chequea si el cliente sigue logeado
     *
     * @return boolean con el estado del login/logout
     */
    public static boolean checkUserConnection(Socket socketClient) {
        boolean userLoggedIn = true;

        try {
            InputStream is = socketClient.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            userLoggedIn = dis.readBoolean();

        } catch (Exception e) {
            closeClient(socketClient);
            System.out.println("Error cogiendo el boolean de login");
        }

        return userLoggedIn;
    }

    public static void sendAdminConnection(Socket socketClient) {
        try {
            new DataOutputStream(socketClient.getOutputStream()).writeBoolean(PSPChallenge.adminLogout);

        } catch (IOException ex) {
            System.out.println("excepción IOE");
            System.out.println("FALLO ENVIANDO BOOLEAN ADMIN_LOGOUT");
        }
    }

    public static void closeClient(Socket socketClient) {
        try {
            socketClient.close();
            System.out.println("Conexión con cliente cerrada");

        } catch (Exception e) {
            System.out.println("Error cerrando conexión con cliente");
        }
    }

    public static void closeServer() {
        try {
            server.close();
            System.out.println("Conexión con cliente cerrada");
            System.out.println("Servidor cerrado");

        } catch (Exception e) {
            System.out.println("Error cerrando conexión de servidor");
        }
    }
}
