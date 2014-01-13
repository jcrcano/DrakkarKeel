/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval;

import drakkar.oar.DocumentMetaData;
import drakkar.oar.Response;
import drakkar.oar.facade.event.FacadeDesktopEvent;
import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.oar.util.KeyField;
import static drakkar.oar.util.KeyMessage.*;
import static drakkar.oar.util.KeyTransaction.*;
import drakkar.oar.util.NotifyAction;
import drakkar.oar.util.OutputMonitor;
import drakkar.mast.IndexException;
import drakkar.mast.SearchException;
import drakkar.mast.recommender.LSIManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Esta constituye la super clase de todos los contextos de los motores de
 * búsquedas de DrakkarKeel. Contiene los métodos comunes de búsqueda de cualquier
 * buscador.
 *
 * 
 */
public abstract class EngineContext implements Contextable, FileIndexable {

    /////////////////ATRIBUTOS COMUNES PARA TODOS LOS MOTORES
    /**
     *
     */
    public long indexedDocsCount = 0;         //cantidad de documentos indexados
    /**
     *
     */
    public long retrievedDocsCount = 0;       //cantidad de documentos encontrados
    /**
     *
     */
    public File indexPath;                   //carpeta donde se guardara el indice creado
    /**
     *
     */
    public File indexLSIPath;                   //carpeta donde se guardara el indice para LSI
    /**
     *
     */
    public File collectionPath;              //direcci'on de la colecci'on indexada
    /**
     *
     */
    public String defaultIndexPath;          //dirección por defecto para guardar el indice
    /**
     *
     */
    public String defaultIndexLSIPath;          //dirección por defecto para guardar el indice LSI
    /**
     *
     */
    public String defaultCollectionPath;     //dirección por defecto de las collectiones
    /**
     *
     */
    public ArrayList<DocumentMetaData> finalMetaResult;
    /**
     *
     */
    public ArrayList<String> documentalSource = new ArrayList<String>();
    /**
     *
     */
    protected Date startTimeOfIndexation;
    /**
     *
     */
    protected Date endTimeOfIndexation;
    /**
     *
     */
    protected Date startTimeOfSearch;
    /**
     *
     */
    protected Date endTimeOfSearch;
    /**
     *
     */
    protected FacadeDesktopListener listener;

//    protected CollectionInfo collectionInfo;
    /**
     *
     */
    protected LSIManager lsiManager;

    /**
     *
     */
    protected boolean applyLSI = false;

    /**
     *
     */
    protected int singularValue = 2;




    /**
     * Constructor por defecto de la clase
     */
    public EngineContext() {

        documentalSource.add("Java");
        documentalSource.add("Documents");

        defaultCollectionPath = "./collection";
        lsiManager = new LSIManager();

        existCollPath(defaultCollectionPath);


    }

    /**
     * Constructor de la clase
     *
     * @param listener oyente empleado para la notificación de las operaciónes
     *                 a la aplicación del servidor.
     */
    public EngineContext(FacadeDesktopListener listener) {
        this.listener = listener;
        documentalSource.add("Java");
        documentalSource.add("Documents");

        defaultCollectionPath = "./collection";
        lsiManager = new LSIManager();

        existCollPath(defaultCollectionPath);
    }


     /**
     * Ejecuta una búsqueda a partir de los parámetros de entrada
     *
     * @param query             consulta
     * @param caseSensitive     tener en cuenta mayúsculas y minúsculas
     * @return                  resultados de búsqueda
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public abstract ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException;

    /**
     *
     * @param query             consulta
     * @param docType           tipo de documento(Extensión: .java,.pdf,.doc,.....)
     * @param caseSensitive     tener en cuenta mayúsculas y minúsculas
     * @return                  resultados de búsqueda
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public abstract ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException;

    /**
     * Ejecutar una búsqueda a partir de los parámetros de entrada
     *
     * @param query             consulta
     * @param docTypes          tipos de documento(Extensión: .java,.pdf,.doc,.....)
     * @param caseSensitive     tener en cuenta mayúsculas y minúsculas
     * @return                  resultados de búsqueda
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public abstract  ArrayList<DocumentMetaData> search(String query, String[] docTypes, boolean caseSensitive) throws SearchException;

    /**
     * Devuelve el tiempo del proceso de indexación
     *
     * @return tiempo de indexación
     */
    public String getIndexationTime() {
        String time = String.valueOf(endTimeOfIndexation.getTime() - startTimeOfIndexation.getTime());
        return time;
    }

    /**
     * Devuelve el tiempo del proceso de búsqueda
     *
     * @return tiempo de búsqueda
     */
    public String getSearchTime() {
        String time = String.valueOf(endTimeOfSearch.getTime() - startTimeOfSearch.getTime());
        return time;
    }

    /**
     * Devuelve el total de documentos indexados
     *
     * @return total de documentos
     */
    public long getIndexedDocumentsCount() {
        return this.indexedDocsCount;
    }

    /**
     * Devuelve el total de documentos recuperados durante el proceso de búsqueda
     *
     * @return total de documentos
     */
    public long geRetrievedDocumentsCount() {
        return this.retrievedDocsCount;
    }

    /**
     * Realiza una selección de los documentos para la extensión(tipo) especificada
     *
     * @param docType        tipo de documento
     * @param metaDocuments  listado de documentos
     *
     * @return documentos filtrados
     */
    public ArrayList<DocumentMetaData> filterMetaDocuments(String docType, ArrayList<DocumentMetaData> metaDocuments) {
        ArrayList<DocumentMetaData> finalResult = new ArrayList<DocumentMetaData>();

        for (DocumentMetaData metaDocument : metaDocuments) {
            if (docType.equals(getDocumentSource(KeyField.SOURCE_DOCUMENTS))) {

                if (metaDocument.getType().equalsIgnoreCase("pdf") || metaDocument.getType().equalsIgnoreCase("txt")) {
                    finalResult.add(metaDocument);
                }

            } else if (docType.equals(getDocumentSource(KeyField.SOURCE_JAVA_CODE))) {
                if (metaDocument.getType().equalsIgnoreCase("java")) {
                    finalResult.add(metaDocument);
                }
            }
        }


        //eliminar repetidos
        if (finalResult.size() > 1) {
            deleteRepeated(finalResult);
        }

        return finalResult;

    }

    /**
     * Verifica el path del índice para crear o añadir a un índice
     * 
     * @param indexPath     -- dirección del índice
     * @param operation     -- MAKE o ADD index
     * @return
     * @throws IndexException
     */
    public abstract boolean safeToBuildIndex(File indexPath, int operation) throws IndexException;

    /**
     *
     * @return
     */
    public String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        String dateTime = null;
        dateTime = "[" + calendar.get(Calendar.YEAR) + "/"
                + calendar.get(Calendar.MONTH) + "/"
                + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.HOUR)
                + ":" + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND) + ":"
                + calendar.get(Calendar.MILLISECOND) + "]";
        return dateTime;
    }

    /**
     * Este método notifica al servidor el progreso de las actividades invocadas
     * para actualizar el tablón de Log y Monitor.
     *
     * @param messageType   tipo de mensage:<tt>INFORMATION_MESSAGE,ERROR_MESSAGE</tt>
     * @param message       contenido del mensaje
     */
    public void notifyTaskProgress(int messageType, String message) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NotifyAction.NOTIFY_TEXT_MESSAGE);
            rs.put(MESSAGE_TYPE, messageType);
            rs.put(MESSAGE, message);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
       

    }

    /**
     * Este método notifica al servidor el progreso de la indexación de documentos
     */
    public void notifyIndexedDocument() {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NotifyAction.NOTIFY_INDEXED_DOCUMENT);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
    }
    /**
     * Este método notifica al servidor el progreso de la indexación de documentos
     * 
     * @param count valor de la cantidad de documentos indexados
     */
     public void notifyIndexedDocumentTerrier(int count) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NotifyAction.NOTIFY_INDEXED_DOCUMENT_COUNT);
            rs.put(VALUE, count);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
    }

    /**
     * Este método notifica al servidor el progreso de la indexación de documentos
     */
    public void notifyAddedDocument() {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NotifyAction.NOTIFY_ADDED_DOCUMENT);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
    }

    /**
     * Este método notifica al servidor el progreso de la indexación de documentos
     * @param count
     */
    public void notifyLoadedDocument(int count) {
//        System.out.println("loaded documents---------- "+count);
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NotifyAction.NOTIFY_LOADED_DOCUMENT);
            rs.put(VALUE, count);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
    }

    /**
     * Devuelve el valor del tiempo al finalizar el proceso de indexación
     *
     * @return tiempo
     */
    public Date getEndTimeOfIndexation() {
        return endTimeOfIndexation;
    }

    /**
     * Modifica el valor del tiempo al finalizar el proceso de indexación
     *
     * @param date  nuevo timpo
     */
    public void setEndTimeOfIndexation(Date date) {
        this.endTimeOfIndexation = date;
    }

    /**
     * Devuelve el valor tiempo al finalizar el proceso de búsqueda
     *
     * @return tiempo
     */
    public Date getEndTimeOfSearch() {
        return endTimeOfSearch;
    }

    /**
     * Modifica el el valor tiempo al finalizar el proceso de búsqueda
     * @param date
     */
    public void setEndTimeOfSearch(Date date) {
        this.endTimeOfSearch = date;
    }

    /**
     * Devuelve el valor del tiempo al iniciar el proceso de indexación
     *
     * @return timpo
     */
    public Date getStartTimeOfIndexation() {
        return startTimeOfIndexation;
    }

    /**
     * Devuelve el valor del tiempo al iniciar el proceso de indexación
     *
     * @param date nuevo tiempo
     */
    public void setStartTimeOfIndexation(Date date) {
        this.startTimeOfIndexation = date;
    }

    /**
     * Devuelve el valor del tiempo al iniciar el proceso de búsqueda
     *
     * @return tiempo
     */
    public Date getStartTimeOfSearch() {
        return startTimeOfSearch;
    }

    /**
     * modifica el valor del tiempo al iniciar el proceso de búsqueda
     *
     * @param date nuevo tiempo
     */
    public void setStartTimeOfSearch(Date date) {
        this.startTimeOfSearch = date;
    }

    /**
     * Devuelve el oyente de la aplicación del servidor
     *
     * @return oyente
     */
    public FacadeDesktopListener getListenerTaskProgress() {
        return listener;
    }

    /**
     * Modifica el objeto oyente de la aplicación del servidor
     *
     * @param listener  nuevo oyente
     */
    public void setListenerTaskProgress(FacadeDesktopListener listener) {
        this.listener = listener;
    }

    /**
     * Elimina los documentos repetidos en una lista de resultados
     *
     * @param list  lista de resultados
     */
    protected void deleteRepeated(ArrayList<DocumentMetaData> list) {

        try {

            for (int i = 0; i < list.size() - 1; i++) {
                for (int k = 0; k < list.size(); k++) {
                    for (int j = i + 1; j < list.size(); j++) {
                        DocumentMetaData docData1 = list.get(j);
                        DocumentMetaData docData2 = list.get(i);
                        if (docData1.getPath().equals(docData2.getPath())) {
                            list.remove(j);
                        }

                    }
                }
            }

        } catch (NullPointerException ex) {
            OutputMonitor.printStream("",ex);
        }

    }

    /**
     * Devuelve la extension de un fichero
     *
     * @param path  dirección del documento
     * @return extensión
     */
    public String getFileExtension(String path) {

        int position = path.lastIndexOf('.');
        String result = path.substring(position + 1);
        return result;

    }

    /**
     * Devuelve el tipo de documento apartir el identificador
     *
     * @param id  identificador del documento
     * 
     * @return tipo de documento
     */
    public String getDocumentSource(int id) {

        switch (id) {
            case KeyField.SOURCE_DOCUMENTS:
                return "documents";
            case KeyField.SOURCE_JAVA_CODE:
                return "java";

            default:
                return null;
        }
    }

    /**
     * Elimina los archivos de un directorio
     *
     * @param dir  directorio a procesar
     */
    
    public void deleteFiles(java.io.File dir) {
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            java.io.File f = new java.io.File(dir, files[i]);
            if (f.exists()) {
                if (f.isFile()) {
                   OutputMonitor.printLine("Deleting: " + f + ": " + f.delete(),OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, "Deleting: " + f + ": " + f.delete());
                 
                } else {
                    deleteFiles(f);
                }
            }
        }
    }

    /**
     * Método para  verificar que el directorio de colecciones existe
     * @param path  dirección del documento
     */
    public void existCollPath(String path) {
        File file = new File(path);

        if (!file.exists()) {
            if (!file.mkdir()) {
                this.notifyTaskProgress(ERROR_MESSAGE, "error creating collectionDefaultPath");
            }
        }
    }

    /**
     *
     * @return
     */
    public File getIndexPath() {
        return indexPath;
    }

    /**
     * 
     * @param indexPath
     * @throws FileNotFoundException
     */
    public void setIndexPath(File indexPath) throws FileNotFoundException {
        if(indexPath.exists()){
             this.indexPath = indexPath;
        }else{
            throw new FileNotFoundException("The index path doesn't exist.");
        }




    }

    /**
     *
     * @return
     */
    public File getCollectionPath() {
        return collectionPath;
    }

    /**
     *
     * @param collectionPath
     */
    public void setCollectionPath(File collectionPath) {
        this.collectionPath = collectionPath;
    }

    /**
     *
     * @return
     */
    public File getIndexLSIPath() {
        return indexLSIPath;
    }

    /**
     *
     * @param indexLSIPath
     */
    public void setIndexLSIPath(File indexLSIPath) {
        this.indexLSIPath = indexLSIPath;
    }


//    public CollectionInfo getCollectionInfo(){
//        return collectionInfo;
//    }

    /**
     *
     * @return
     */
    public LSIManager getLSIManager(){
        return lsiManager;
    }

    /**
     *
     * @return
     */
    public int getSingularValue() {
        return singularValue;
    }

    /**
     *
     * @param singularValue
     */
    public void setSingularValue(int singularValue) {
        this.singularValue = singularValue;
    }

    public boolean isApplyLSI() {
        return applyLSI;
    }

    public void setApplyLSI(boolean applyLSI) {
        this.applyLSI = applyLSI;
    }



    

}
