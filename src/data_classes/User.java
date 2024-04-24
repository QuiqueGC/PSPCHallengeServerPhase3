package data_classes;

import java.io.Serializable;

public class User implements Serializable {

    protected String name;
    protected String passwd;
    protected int userType;


    public User(String name, String passwd, int userType) {
        this.name = name;
        this.passwd = passwd;
        this.userType = userType;
    }


    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    /**
     * texto para introducir en el fichero al usuario
     *
     * @return String con la línea de texto
     */
    public String toFile() {

        return this.userType + ";" + this.name + ";" + this.passwd;
    }
}
