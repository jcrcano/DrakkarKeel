/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.file.transfer;

import drakkar.oar.slice.transfer.AMD_File_next;
import drakkar.oar.slice.transfer._FileDisp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Clase que maneja la transferencia de ficheros con ICE
 */
public class FileServant extends _FileDisp {

    private FileInputStream input;
    private int num;
    private byte[] data, dataTemp;
    private long fileLength;
    private boolean flag;
    private int qoute;

    /**
     *
     * @param file
     * @param num
     * @throws FileNotFoundException
     */
    public FileServant(File file, int num) throws FileNotFoundException {
        this.input = new FileInputStream(file);
        this.num = num;

        this.fileLength = file.length();

        if (num > fileLength) {
            this.num = (int) fileLength;
            this.data = new byte[this.num];
        } else {

            this.qoute = (int) (fileLength % num);
            this.flag = true;
            this.data = new byte[qoute];


        }
        this.dataTemp = new byte[this.num];
    }
  

    /**
     *
     * @param cb
     * @param current
     */
    public synchronized void next_async(AMD_File_next cb, Ice.Current current) {

        try {
            int readbyte;
            if (flag) {
                flag = false;
                readbyte = input.read(data, 0, qoute);

            } else {
                data = dataTemp;
                readbyte = input.read(data, 0, num);
            }


            if (readbyte == -1) {
                input.close();
                data = new byte[0];
                cb.ice_response(data);
                current.adapter.remove(current.id);

                return;
            }

            cb.ice_response(data);

        } catch (IOException ex) {
            ex.printStackTrace();
            cb.ice_exception(ex);
        }
    }
}
