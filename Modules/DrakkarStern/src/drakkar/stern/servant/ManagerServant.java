/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant;

import drakkar.stern.controller.ContainerController;
import java.io.*;
import java.security.*;
import java.util.*;

/**
 * Esta clase constitye el sirviente  del objeto ice Manager, por lo cual
 * implementa los métodos definidos en esta interfaz, los cuales tienen objetivo
 * facilitar la administración del sistema.
 */
public class ManagerServant {

    private ArrayList<ContainerController> containersList = null;
    private String name = null;
    private String password = null;

    /**
     * Constructor de la clase
     *
     * @param containersList lista de contenedore en existencia en el server.
     */
    public ManagerServant(ArrayList<ContainerController> containersList) {
        this.containersList = containersList;
    }

    /**
     * Este método es empleado para la autentificación del administrador del sistema.
     *
     * @param name
     * @param password
     * @return un objeto Response con el resultado de la operación
     */
    public boolean login(String name, String password) {

        this.name = name;
        this.password = password;

        boolean flag = false;
        File file = new File("adminConf.txt");
        String nam = null;
        String passw = null;

        BufferedReader fileRead = null;
        if (file.exists()) {
            try {
                fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } catch (FileNotFoundException err) {
                err.printStackTrace();
            }
            try {
                nam = fileRead.readLine();
                passw = fileRead.readLine();
                fileRead.close();
            } catch (IOException err) {
                err.printStackTrace();
            }

            if (nam.equals(name)) {
                if (passw.equals("sisAdminColab")) {
                    if (passw.equals(password)) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                } else {
                    String passwEncrypting = passwordEncrypting(password);
                    if (passwEncrypting.equals(passw)) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
            } else {
                flag = false;
            }

        }
       
        return flag;
    }

    private String passwordEncrypting(String passWord) {
        byte[] key = new byte[2];
        key[0] = 5;
        key[1] = 9;
        byte[] buffer = passWord.getBytes();
        return new String(getKeyedDigest(buffer, key));
    }

    private byte[] getKeyedDigest(byte[] buffer, byte[] key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            return md5.digest(key);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    /**
     * Este método devuelve el nombre del administrador del sistema.
     *
     * @return nombre del administrador
     */
    public String getName() {
        return this.name;
    }

    /**
     * Este método devuelve la contraseña del administrador del sistema.
     *
     * @return contraseña del administrador.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Este método reemplaza el nombre actual de administrador del sistema.
     *
     * @param name - nuevo nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Este método reemplaza la contraseña del administrador del sistema.
     *
     * @param password - nueva contraseña
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Este método de vuelve una lista con los contenedores existentes en el servidor.
     *
     * @return lista de contenedores
     */
    public ArrayList<ContainerController> getContainersList() {
        return containersList;
    }

    /**
     * Este método reemplaza la lista de contenedores
     *
     * @param containersList - nueva lista de contenedores.
     */
    public void setContainersList(ArrayList<ContainerController> containersList) {
        this.containersList = containersList;
    }

   
}

