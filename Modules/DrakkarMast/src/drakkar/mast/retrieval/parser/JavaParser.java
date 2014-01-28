/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval.parser;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaPackage;
import com.thoughtworks.qdox.model.JavaSource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Para extraer de una clase Java: nombre de la clase, paquete,
 * nombre de los metodos, comentarios, nombre de los atributos, javadocs
 *
 */
public class JavaParser {

    private JavaDocBuilder builder;
    private JavaSource[] src;
    private JavaPackage pkg;
    private JavaClass[] classesDefinition; //en un file puede haber mas de una clase....
    private JavaField[] vars;
    //////////////////////////////
    private String classPackage, allSource;
    private String[] classNames;
    private String[] classComments;
    private String[] classJavaDocs;
    private String[][] classMethodsComments;
    private String[][] classMethodJavaDocs;
    private String[][] classMethodNames;
    private String[][] classVarNames;
    private String[][] classVarComments;
    private String[][] classVarJavaDocs;
    private int classNumber;
    private int classVarNumber;
    private int classMethodNumber;

    public JavaParser() {
        builder = new JavaDocBuilder();
    }

    /**
     * Analiza el fichero que puede contener mas de una clase java.
     * Extrae el nombre del paquete, y por cada clase su nombre, atributos,
     * metodos y comentarios.
     * @param f
     * @throws IOException
     */
    public void AnalyzeDocument(File f) throws IOException {

        getBuilder().addSource(new FileReader(f.getCanonicalPath()));
        setSrc(getBuilder().getSources());

        //Getting all code of the file
        allSource = getSrc()[0].getCodeBlock();

        //Getting package
        if (getSrc()[0].getPackage() != null) {
            setPkg(getSrc()[0].getPackage());
            setClassPackage(getPkg().getName());
        }

        //Getting classesnames
        if (getSrc()[0].getClasses() != null) {
            setClassdef(getSrc()[0].getClasses());
            setClassNumber(getClassdef().length);

            classNames = new String[classesDefinition.length];
            classVarNames = new String[classesDefinition.length][];
            setClassVarComments(new String[classesDefinition.length][]);
            classMethodNames = new String[classesDefinition.length][];
            classMethodsComments = new String[classesDefinition.length][];
            classComments = new String[classesDefinition.length];
            setClassJavaDocs(new String[classesDefinition.length]);

            for (int i = 0; i < classesDefinition.length; i++) {
                JavaClass jclass = classesDefinition[i];
                classNames[i] = jclass.getName();

                //Getting variables
                if (jclass.getFields() != null) {
                    vars = jclass.getFields();
                    setClassVariableNumber(vars.length);

                    classVarNames = new String[classesDefinition.length][vars.length];
                    setClassVarComments(new String[classesDefinition.length][vars.length]);

                    for (int j = 0; j < vars.length; j++) {
                        JavaField jfield = vars[j];
                        classVarNames[i][j] = jfield.getName();
                        classVarJavaDocs = new String[classesDefinition.length][vars.length];

                        if (jfield.getComment() != null) {
                            getClassVarComments()[i][j] = jfield.getComment();
                        }

                        if (jfield.getTags().length != 0) {
                            String value = "", name = "", line = "";
                            DocletTag[] doclet = jfield.getTags();
                            if (doclet.length != 0) {
                                for (DocletTag docletTag : doclet) {
                                    name = docletTag.getName();
                                    value = docletTag.getValue();
                                    line += name + " " + value + " ";
                                }

                                classVarJavaDocs[i][j] = line;
                            }
                        }
                    }
                }
                //Getting methods
                if (jclass.getMethods() != null) {
                    JavaMethod[] jmethod = jclass.getMethods();
                    setClassMethodNumber(jmethod.length);
                    classMethodNames = new String[classesDefinition.length][jmethod.length];

                    for (int k = 0; k < jmethod.length; k++) {
                        JavaMethod javaMethod = jmethod[k];
                        classMethodNames[i][k] = javaMethod.getName();
                        classMethodJavaDocs = new String[classesDefinition.length][jmethod.length];
                        classMethodsComments = new String[classesDefinition.length][jmethod.length];//duda

                        if (javaMethod.getComment() != null) {
                            classMethodsComments[i][k] = javaMethod.getComment();
                        }

                        if (javaMethod.getTags().length != 0) {
                            String value = "", name = "", line = "";
                            DocletTag[] doclet = javaMethod.getTags();
                            for (DocletTag docletTag : doclet) {
                                name = docletTag.getName();
                                value = docletTag.getValue();
                                line += name + " " + value + " ";
                            }
                            classMethodJavaDocs[i][k] = line;
                        }
                    }
                }

                //Getting comments
                if (jclass.getComment() != null) {
                    classComments[i] = jclass.getComment();
                }

                //Getting class javadocs
                if (jclass.getTags().length != 0) {
                    String value = "", name = "", line = "";
                    DocletTag[] doclet = jclass.getTags();
                    for (DocletTag docletTag : doclet) {
                        name = docletTag.getName();
                        value = docletTag.getValue();
                        line += name + " " + value + " ";
                    }
                    getClassJavaDocs()[i] = line;
                }
            }
        }
    }

    /**
     * Obtener el nombre de la clase
     * @param i
     * @return
     */
    public String getClassesNames(int i) {
        return classNames[i];

    }

    /**
     * Obtener el comentario de la clase
     * @param i
     * @return
     */
    public String getClassesComments(int i) {
        return classComments[i];

    }

    /**
     * Obtener el javadocs de la clase
     * @param i
     * @return
     */
    public String getClassesJDocs(int i) {
        return classJavaDocs[i];

    }

    /**
     * Obtener la cantidad de variables que existen por clase
     * @param i
     * @return
     */
    public int getClassVariableNumber(int i) {
        return classVarNames[i].length;
    }

    /**
     * Obtener los nombres de las variables
     * @param i
     * @param j
     * @return
     */
    public String getClassesVarName(int i, int j) {
        return classVarNames[i][j];

    }

    /**
     * Obtener la cantidad de métodos por clase
     * @param i
     * @return
     */
    public int getClassesMethods(int i) {
        return classMethodNames[i].length;
    }

    /**
     * Obtener los nombres de cada metodo por clase
     * @param i
     * @param j
     * @return
     */
    public String getClassesMethodsName(int i, int j) {
        return classMethodNames[i][j];

    }

    /**
     * Obtener el comentario de un metodo de la clase
     * @param i
     * @param j
     * @return
     */
    public String getClassesMethodComment(int i, int j) {
        return classMethodsComments[i][j];

    }

    /**
     * Obtener el javadocs de un metodo de la clase
     * @param i
     * @param j
     * @return
     */
    public String getClassesMethodJDocs(int i, int j) {
        return classMethodJavaDocs[i][j];
    }

    /**
     * Obtener el comentario de una variable de la clase
     * @param i
     * @param j
     * @return
     */
    public String getClassesCommentVariables(int i, int j) {
        return this.classVarComments[i][j];

    }

    /**
     * Obtener el javadocs de una variable de la clase
     * @param i
     * @param j
     * @return
     */
    public String getVariablesJDocs(int i, int j) {
        return this.classVarJavaDocs[i][j];

    }

//////////////////////////////Methods SET Y GET
    /**
     * @return the builder
     */
    public JavaDocBuilder getBuilder() {
        return builder;
    }

    /**
     * @param builder the builder to set
     */
    public void setBuilder(JavaDocBuilder builder) {
        this.builder = builder;
    }

    /**
     * @return the src
     */
    public JavaSource[] getSrc() {
        return src;
    }

    /**
     * @param src the src to set
     */
    public void setSrc(JavaSource[] src) {
        this.src = src;
    }

    /**
     * @return the pkg
     */
    public JavaPackage getPkg() {
        return pkg;
    }

    /**
     * @param pkg the pkg to set
     */
    public void setPkg(JavaPackage pkg) {
        this.pkg = pkg;
    }

    /**
     * @return the classPackage
     */
    public String getClassPackage() {
        return classPackage;
    }

    /**
     * @param classPackage the classPackage to set
     */
    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    /**
     * @return the classNames
     */
    public String[] getClassNames() {
        return classNames;
    }

    /**
     * @param classNames the classNames to set
     */
    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    /**
     * @return the classComments
     */
    public String[] getClassComments() {
        return classComments;
    }

    /**
     * @param classComments the classComments to set
     */
    public void setClassComments(String[] classComments) {
        this.classComments = classComments;
    }

    /**
     * @return the methodNames
     */
    public String[][] getMethodNames() {
        return classMethodNames;
    }

    /**
     * @param methodNames the methodNames to set
     */
    public void setMethodNames(String[][] methodNames) {
        this.classMethodNames = methodNames;
    }

    /**
     * @return the classVarNames
     */
    public String[][] getClassVarNames() {
        return classVarNames;
    }

    /**
     * @param classVarNames the classVarNames to set
     */
    public void setClassVarNames(String[][] classVarNames) {
        this.classVarNames = classVarNames;
    }

    /**
     * @return the classdef
     */
    public JavaClass[] getClassdef() {
        return classesDefinition;
    }

    /**
     * @param classdef the classdef to set
     */
    public void setClassdef(JavaClass[] classdef) {
        this.classesDefinition = classdef;
    }

    /**
     * @return the classNumber
     */
    public int getClassNumber() {
        return classNumber;
    }

    /**
     * @param classNumber the classNumber to set
     */
    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    /**
     * @return the classVariableNumber
     */
    public int getClassVariableNumber() {
        return classVarNumber;
    }

    /**
     * @param classVariableNumber the classVariableNumber to set
     */
    public void setClassVariableNumber(int classVariableNumber) {
        this.classVarNumber = classVariableNumber;
    }

    /**
     * @return the classMethodNumber
     */
    public int getClassMethodNumber() {
        return classMethodNumber;
    }

    /**
     * @param classMethodNumber the classMethodNumber to set
     */
    public void setClassMethodNumber(int classMethodNumber) {
        this.classMethodNumber = classMethodNumber;
    }

    /**
     * @return the classMethodsComments
     */
    public String[][] getClassMethodsComments() {
        return classMethodsComments;
    }

    /**
     * @param classMethodsComments the classMethodsComments to set
     */
    public void setClassMethodsComments(String[][] classMethodsComments) {
        this.classMethodsComments = classMethodsComments;
    }

    /**
     * @return the allSource
     */
    public String getAllSource() {
        return allSource;
    }

    /**
     * @param allSource the allSource to set
     */
    public void setAllSource(String allSource) {
        this.allSource = allSource;
    }

    /**
     * @return the classVarComments
     */
    public String[][] getClassVarComments() {
        return classVarComments;
    }

    /**
     * @param classVarComments the classVarComments to set
     */
    public void setClassVarComments(String[][] classVarComments) {
        this.classVarComments = classVarComments;
    }

    /**
     * @return the classJavaDocs
     */
    public String[] getClassJavaDocs() {
        return classJavaDocs;
    }

    /**
     * @param classJavaDocs the classJavaDocs to set
     */
    public void setClassJavaDocs(String[] classJavaDocs) {
        this.classJavaDocs = classJavaDocs;
    }

    /**
     * @return the classMethodsJavaDocs
     */
    public String[][] getClassMethodsJavaDocs() {
        return classMethodJavaDocs;
    }

    /**
     * @param classMethodsJavaDocs the classMethodsJavaDocs to set
     */
    public void setClassMethodsJavaDocs(String[][] classMethodsJavaDocs) {
        this.classMethodJavaDocs = classMethodsJavaDocs;
    }

    /**
     * @return the classVarJavaDocs
     */
    public String[][] getClassVarJavaDocs() {
        return classVarJavaDocs;
    }

    /**
     * @param classVarJavaDocs the classVarJavaDocs to set
     */
    public void setClassVarJavaDocs(String[][] classVarJavaDocs) {
        this.classVarJavaDocs = classVarJavaDocs;
    }
}
