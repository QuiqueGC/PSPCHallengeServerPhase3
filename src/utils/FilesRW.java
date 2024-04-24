package utils;

import data_classes.User;
import p_s_p_challenge.PSPChallenge;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Almacena todos los métodos referentes a la lectura y/o escritura de ficheros
 */
public class FilesRW {

    /**
     * recupera los datos del fichero en un array
     *
     * @param users el ArrayList donde guardaremos la información del fichero
     */
    public static void takingUserDataFromFile(ArrayList<User> users) {

        String fileURL = "src/resources/files/usersList.txt";
        String line;
        String[] data;

        try (BufferedReader br = new BufferedReader(new FileReader(fileURL))) {

            while ((line = br.readLine()) != null) {

                data = line.split(";");


                if (data[0].equals("0")) {

                    users.add(new User(data[1], data[2], Integer.parseInt(data[0])));

                } else if (data[0].equals("1")) {

                    users.add(new User(data[1], data[2], Integer.parseInt(data[0])));

                } else {

                    users.add(new User(data[1], data[2], Integer.parseInt(data[0])));
                }

            }

        } catch (FileNotFoundException e) {

            System.out.println("NO SE HA ENCONTRADO EL FICHERO");

        } catch (IOException e) {

            System.out.println("ERROR DURANTE LA LECTURA DEL FICHERO");

        } catch (ArrayIndexOutOfBoundsException e) {

            System.out.println("INFORMACIÓN MAL GUARDADA EN EL FICHERO");
        }

    }


    /**
     * Añade al fichero un nuevo usuario
     */
    public static void addingNewUser(User user) {

        Path path = Paths.get("src/resources/files/usersList.txt");

        if (!Files.exists(path)) {

            creatingFile(path);
        }

        addingNewUserToFile(path, user);
    }


    /**
     * Añade al usuario que se da de alta al fichero de usuarios
     *
     * @param path la ruta en la que está el fichero
     * @param user el usuario que hay que introducir en el fichero
     */
    private static void addingNewUserToFile(Path path, User user) {
        try {
            Files.writeString(path, user.toFile() + System.lineSeparator(), StandardOpenOption.APPEND);

        } catch (IOException e) {

            System.out.println("ERROR EN LA ESCRITURA DEL FICHERO");
        }
    }

    /**
     * Sobreescribe el archivo donde están guardados los usuarios
     */
    public static void overwritingFile() {
        Path path = Paths.get("src/resources/files/usersList.txt");

        deleteFile(path);

        creatingFile(path);

        addingAllUsersToFile(path);
    }

    private static void addingAllUsersToFile(Path path) {
        for (User user :
                PSPChallenge.usersList) {

            try {
                Files.writeString(path, user.toFile() + System.lineSeparator(), StandardOpenOption.APPEND);

            } catch (IOException ee) {

                System.out.println("ERROR EN LA ESCRITURA DEL FICHERO");
            }
        }
    }


    /**
     * Crea un nuevo fichero en la ruta establecida
     *
     * @param path la ruta en la que crear
     */
    public static void creatingFile(Path path) {
        try {
            Files.createFile(path);

        } catch (IOException ex) {

            System.out.println(ex);
        }
    }

    public static void deleteFile(Path path) {
        try {

            Files.delete(path);

        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }
    }
}
