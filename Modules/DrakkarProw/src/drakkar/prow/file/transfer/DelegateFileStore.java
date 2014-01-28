/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.file.transfer;

import drakkar.prow.ApplicationContext;
import drakkar.oar.Communication;
import drakkar.oar.Delegate;
import drakkar.oar.callback.NextAMICallback;
import drakkar.oar.exception.ProxyNotExistException;
import drakkar.oar.slice.transfer.FileAccessException;
import drakkar.oar.slice.transfer.FilePrx;
import drakkar.oar.slice.transfer.FileStorePrx;
import drakkar.oar.slice.transfer.FileStorePrxHelper;
import drakkar.oar.util.OutputMonitor;
import java.io.*;

/**
 * Esta clase tiene el objetivo de obtener un objeto proxy(FileStorePrx) para
 * que actúe como embajador del correspondiente objeto registrado en el tiempo
 * de ejecución de ICE, pudiendose así ejecutar las operaciones soportadas por
 * este, para la transferencia de fichero entre el cliente y el servidor.
 */
public class DelegateFileStore extends Delegate implements Serializable {

    private static final long serialVersionUID = 80000000000024L;

    private FileStorePrx fileStorePrx;
    private int numberBytesToRead = 1000 * 1024;

    /**
     * Constructor de la clase
     *
     * @param communication objeto cumnicación
     * @param name nombre del objeto proxy
     * @param ip número del host, donde se encuantra desplegado el servidor
     * @param porNumber número del puerto de escucha del servidor
     */
    public DelegateFileStore(Communication communication, String name, String ip, int porNumber) {
        super(communication, name, ip, porNumber);

    }

    /**
     * Este método obtiene un proxy del objeto remoto(FileStorePrx) para que
     * actúe este actué como embajador del correspondiente objeto registrado en
     * el tiempo de ejecución de Ice, solamente escuchará las peticiones
     * procedentes de la interfaz del servidor especificada
     *
     * @throws ProxyNotExistException si no se encuentra el objeto proxy
     */
    public void create() throws ProxyNotExistException {

        // stringToProxy: obtiene un proxy del objeto remoto
        this.proxy = this.communication.getCommunicator().stringToProxy(this.name
                + ":tcp -h " + this.ip + " -p " + this.portNumber);
        if (this.proxy == null) {
            throw new ProxyNotExistException("Invalid Proxy");
        }
        this.fileStorePrx = FileStorePrxHelper.checkedCast(this.proxy);

    }

    /**
     * Este método obtiene un proxy del objeto remoto(FileStorePrx) para que
     * actúe este actué como embajador del correspondiente objeto registrado en
     * el tiempo de ejecución de Ice, escuchando cualquier petición o invocación
     * efectuada por cualquier interfaz que implemte una instancia de este
     * objeto proxy.
     *
     * @throws ProxyNotExistException si no se encuentrael objeto proxy
     */
    public void createMultiListener() throws ProxyNotExistException {

        // stringToProxy: obtiene un proxy del objeto remoto
        this.proxy = this.communication.getCommunicator().stringToProxy(this.name
                + ":tcp -p " + this.portNumber);
        if (this.proxy == null) {
            throw new ProxyNotExistException("Invalid Proxy");
        }
        this.fileStorePrx = FileStorePrxHelper.checkedCast(this.proxy);

    }

    /**
     * Transfiere un fichero determinado a la aplicación servidora
     *
     * @param filePathName dirección del fichero
     *
     * @throws FileNotFoundException si no se puede localizar el fichero
     * especificado
     * @throws IOException si ocurre algún error durante el proceso de lectura
     * del fichero especificado
     *
     */
    public void upload(String filePathName) throws FileNotFoundException, IOException {

        File file = new File(filePathName);
        try (FileInputStream in = new FileInputStream(file)) {
            long filesize = file.length();
            byte[] b = new byte[(int) filesize];
            int readbytes = in.read(b);

            if (readbytes == -1) {
                throw new FileNotFoundException("No se pudo encontrar el fichero");
            }
        }
    }

    /**
     * Descarga el fichero especificado de la aplicación servidora
     *
     * @param filePathName dirección del fichero
     *
     * @return ubicación del fichero desacargado
     *
     * @throws IOException si ocurre algún error durante el proceso de
     * transferencia del fichero especificado
     * @throws FileAccessException si ocurre algún error mientras se trataba de
     * acceder al fichero seleccionado
     */
    public String download(String filePathName) throws IOException, FileAccessException {
        File temp = new File(filePathName);
        OutputMonitor.printLine("Download : " + temp.getName(), OutputMonitor.INFORMATION_MESSAGE);
        File file = new File(ApplicationContext.cache);

        if (!file.exists()) {
            file.mkdir();
        }
        String filePath = ApplicationContext.cache + File.separator + temp.getName();
        try (FileOutputStream out = new FileOutputStream(filePath, true)) {
            FilePrx filePrx = fileStorePrx.read(filePathName, numberBytesToRead);

            NextAMICallback curr = null, next = null;
            byte[] bytes;

            curr = new NextAMICallback();
            next = new NextAMICallback();
            filePrx.next_async(curr);

            while (true) {
                filePrx.next_async(next);
                bytes = curr.getData();
                if (bytes.length == 0.) {
                    break;
                }
                out.write(bytes);

                bytes = next.getData();
                if (bytes.length == 0.) {
                    break;
                }

                out.write(bytes);

                filePrx.next_async(curr);
            }
        }
        OutputMonitor.printLine("Completed download : " + temp.getName(), OutputMonitor.INFORMATION_MESSAGE);
        return filePath;
    }

    /**
     * Descarga el fichero especificado de la aplicación servidora
     *
     * @param filePathName dirección del fichero
     *
     * @param svnSearch indicador de si el resultado de búsqueda proviene de una
     * búsqueda en repositorios SVN
     *
     * @return fichero descargado
     *
     * @throws IOException si ocurre algún error durante el proceso de
     * transferencia del fichero especificado
     * @throws FileAccessException si ocurre algún error mientras se trataba de
     * acceder al fichero seleccionado
     */
    public File getFile(String filePathName) throws IOException, FileAccessException {
        String filePath = download(filePathName);

        return new File(filePath);
    }

    /**
     * Devuelve el total de bytes a leer durante las transferencias de ficheros
     *
     * @return total de bytes
     */
    public int getNumberBytesToRead() {
        return numberBytesToRead;
    }

    /**
     * Modifica el valor del número de bytes a leer durante las transferencias
     * de ficheros
     *
     * @param numberBytesToRead número de bytes
     */
    public void setNumberBytesToRead(int numberBytesToRead) {
        this.numberBytesToRead = numberBytesToRead;
    }

}
