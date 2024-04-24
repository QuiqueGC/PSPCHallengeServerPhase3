package data_classes;

import p_s_p_challenge.PSPChallenge;

import java.util.ArrayList;

public class UserConnected {

    private String name;
    private String ip;
    private ArrayList<String> installedPrograms;
    private ArrayList<WindowsProcess> executingProcesses;
    private String loadingPrograms;
    private String loadingProcess;
    private String orderToClient;
    private String processPID;


    public UserConnected(String name, String ip) {
        this.name = name;
        this.ip = ip;
        loadingProcess = "Cargando...";
        loadingPrograms = "Cargando...";
        orderToClient = "";
        processPID = "";
    }

    public String getProcessPID() {
        return processPID;
    }

    public void setProcessPID(String processPID) {
        this.processPID = processPID;
    }

    public String getOrderToClient() {
        return orderToClient;
    }

    public void setOrderToClient(String orderToClient) {
        this.orderToClient = orderToClient;
    }

    public void setLoadingPrograms(String loadingPrograms) {
        this.loadingPrograms = loadingPrograms;
    }

    public void setLoadingProcess(String loadingProcess) {
        this.loadingProcess = loadingProcess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public ArrayList<String> getInstalledPrograms() {
        return installedPrograms;
    }

    public void setInstalledPrograms(ArrayList<String> installedPrograms) {
        this.installedPrograms = installedPrograms;
    }

    public ArrayList<WindowsProcess> getExecutingProcesses() {
        return executingProcesses;
    }

    public void setExecutingProcesses(ArrayList<WindowsProcess> executingProcesses) {
        this.executingProcesses = executingProcesses;
    }

    public void clearDataAfterOrder(){
        PSPChallenge.userConnected.setProcessPID("");
        processPID = "";
        orderToClient = "";
        executingProcesses = new ArrayList<>();
        loadingProcess = "cargando...";
    }


    public String showData() {
        return "<html>Conexión establecida!<br><br>" +
                " IP del cliente: " + ip + "<br><br>" +
                "Nombre del usuario: " + name + "<br><br>" +
                "Procesos en ejecución de " + name + " -> " + loadingProcess + "<br>" +
                "Programas instalados  en el dispositivo de " + name + " -> " + loadingPrograms + "<html>";
    }
}
