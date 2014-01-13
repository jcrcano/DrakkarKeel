/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.util;

import java.io.*;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Esta clase es la encargada de crea y leer los parámetros del fichero XML creado
 * para guardar determinadas propiedades de configuración
 */
public class XMLConfig {


    /**
     *  Constructor por defecto de la clase
     */
    public XMLConfig() {
    }

    /**
     * Constructor de la clase
     *
     * @param configurationFile directorio del fichero de configuración
     *
     * @throws Exception si ocurre algún error durante el proceso de construcción
     *                   del fichero de configuración
     */
    @SuppressWarnings({"unchecked", "unchecked"})
    public XMLConfig(File configurationFile) throws Exception {

        this.configurationfile = configurationFile;
        SAXBuilder builder = new SAXBuilder(false);
        xmldoc = builder.build(configurationFile);
        raiz = xmldoc.getRootElement();
        parameters = (List<Element>) raiz.getChildren("parameter");
    }

    /**
     * Constructor de la clase
     *
     * @param configFilePath dirección del fichero de configración
     *
     * @throws Exception si ocurre algún error durante el proceso de construcción
     *                   del fichero de configuración
     */
    public XMLConfig(String configFilePath) throws Exception {
        this(new File(configFilePath));
    }

    /**
     * Devuelve el valor asociado al nombre del parámetro
     *
     * @param parameterName nombre del parámetro
     *
     * @return valor
     */
    public String getValue(String parameterName) {
        for (Element parameter : parameters) {
            if (parameter.getAttributeValue("name").equals(parameterName)) {
                return parameter.getAttributeValue("value");
            }
        }
        return null;
    }

    private Element getElement(String parameterName) {

        for (Element parameter : parameters) {
            if (parameter.getAttributeValue("name").equals(parameterName)) {
                return parameter;
            }
        }

        return null;
    }

    /**
     * Modifica el valor asociado al parámetro especificado
     *
     * @param parametername nombre del parámetro
     * @param newvalue      nuevo valor
     *
     * @return true si se pudo actualizar el valor, false en caso contrario
     */
    public boolean setValue(String parametername, String newvalue) {
        org.jdom.Element element = getElement(parametername);
        if (element != null) {
            element.setAttribute("value", newvalue);
            try {
                org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
                try (java.io.FileOutputStream file = new java.io.FileOutputStream(configurationfile)) {
                    out.output(xmldoc, file);
                    file.flush();
                }
                //out.output(xmldoc, System.out);
                return true;
            } catch (Exception ex) {
                OutputMonitor.printStream("", ex);
            }
        }
        return false;
    }

    /**
     * Crea un fichero de configuración el la dirección especificada, a partir de
     * los parámetros proporcionados
     *
     * @param configFilePath dirección del fichero de configuración
     * @param params         lista de parámetros
     *
     * @return  true si se pudo crear el fichero, false en caso contrario
     */
    public static boolean createXMLConfig(String configFilePath, List<Parameter> params) {
        PrintWriter write = null;
        try {
            // File file = new File(uriConfigFile + ".xml");
            File file = new File(configFilePath);
            write = new PrintWriter(new FileOutputStream(file));
            write.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            write.println("<!-- DrakkarKeel Framework Configuration-->");
            write.println("<configuration>");
            for (Parameter parameter : params) {
                write.print("<parameter name=\"" + parameter.getKey() + "\" value=\"" + parameter.getValue() + "\"/>\n");
            }
            write.println("</configuration>");
            write.close();

        } catch (FileNotFoundException ex) {
            if (write != null) {
                write.close();
            }
        }
        return true;
    }

    /**
     * Devuelve una instancia de la clase
     *
     * @param name nombre del fichero de configuración
     *
     * @return instancia
     *
     * @throws Exception si ocurre algún error durante el proceso de construcción
     *                   del fichero de configuración
     */
    public static XMLConfig getInstance(String name) throws Exception {
        return new XMLConfig(name + "Config.xml");
    }
    private Document xmldoc;
    private List<Element> parameters;
    private Element raiz;
    private File configurationfile;
}

