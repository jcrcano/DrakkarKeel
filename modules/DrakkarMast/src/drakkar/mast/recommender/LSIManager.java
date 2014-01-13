/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.recommender;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import drakkar.oar.DocSuggest;
import drakkar.oar.TermSuggest;
import drakkar.oar.util.OutputMonitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.tartarus.snowball.SnowballStemmer;

public class LSIManager {

    private double[][] valsMatrix, termVectors, docVectors;
    private double[] scales;
    private int singularValue;
    private String[] terms, termsStemmer;
    private DocInfo[] docs;
    private Class stemClass;
    private org.tartarus.snowball.SnowballStemmer stemmer;
    private String language;

    /**
     *
     */
    public LSIManager() {
    }

    /**
     *
     * @param c
     */
    public LSIManager(CollectionInfo c) {

        valsMatrix = AWEntropy.assignmentOfWeights(c);

        this.language = "english";
//        Matrix a = new Matrix(valsMatrix);
//        a.print(5, 2);
        terms = c.getTerms().toArray(new String[0]);
        singularValue = c.getSingularValue();
        docs = c.getDocs().toArray(new DocInfo[0]);


    }

    /**
     *
     * @param valsMatrix
     * @param singularValue
     * @param terms
     * @param docs
     */
    public LSIManager(double[][] valsMatrix, int singularValue, String[] terms, DocInfo[] docs) {
        this.valsMatrix = valsMatrix;
        this.singularValue = singularValue;
        this.terms = terms;
        this.docs = docs;
        this.language = "english";


    }

    /**
     * Inicializa la matriz de descomposición de valores singulares
     */
    public void initSVDMatrix() {
        try {
            Matrix A = new Matrix(valsMatrix);
            SingularValueDecomposition s = A.svd();
            Matrix U = s.getU(); // matriz de términos
            termVectors = U.getArrayCopy();
            double[] singularValues = s.getSingularValues(); // valores sigulares
            scales = new double[singularValue];
            System.arraycopy(singularValues, 0, scales, 0, singularValue);
            Matrix V = s.getV(); // matriz de documentos
            docVectors = V.getArrayCopy();

            stemClass = Class.forName("org.tartarus.snowball.ext." + language + "Stemmer");
//             Class stemClass = Class.forName("org.tartarus.snowball.ext.englishStemmer");
            stemmer = (SnowballStemmer) stemClass.newInstance();

            this.termsStemmer = stemmerTerms(terms);

        } catch (InstantiationException ex) {
            OutputMonitor.printStream("", ex);
        } catch (IllegalAccessException ex) {
            OutputMonitor.printStream("", ex);
        } catch (ClassNotFoundException ex) {
            OutputMonitor.printStream("", ex);
        }

        OutputMonitor.printLine("[Class] LSIManager [Method] initSVDMatrix.", OutputMonitor.TRACE_MESSAGE);

    }

    /**
     *
     * @param m
     * @param r
     * @param c
     * @return
     */
    public double[][] trunk(double[][] m, int r, int c) {
        double[][] subm = new double[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(m[i], 0, subm[i], 0, c);
        }
        return subm;
    }

    /**
     * 
     * @param m
     * @param maxFactors
     * @return
     */
    public double[][] trunk(double[][] m, int maxFactors) {
        double[][] subm = new double[m.length][maxFactors];
        for (int i = 0; i < m.length; i++) {
            System.arraycopy(m[i], 0, subm[i], 0, maxFactors);
        }
        return subm;
    }

    /**
     * Devuelve una lista de sugerencias de términos de consulta, apartir
     * de la consulta especificada.
     *
     * @param query términos de la consulta de búsqueda
     *
     * @return  términos sugeridos
     */
    public List<TermSuggest> getTermsSuggest(String query) {
        List<TermSuggest> termsSuggest = new ArrayList<TermSuggest>();

        List<String> querys = Arrays.asList(query.split(" |,"));
        double[] queryVector = new double[singularValue];
        Arrays.fill(queryVector, 0.0);

        for (String term : querys) {
            addTermVector(term, termVectors, queryVector);
        }

        OutputMonitor.printLine("Query=" + querys, OutputMonitor.INFORMATION_MESSAGE);
        OutputMonitor.printLine("", OutputMonitor.INFORMATION_MESSAGE);
        System.out.print("Query Vector=(");
        for (int k = 0; k < queryVector.length; ++k) {
            if (k > 0) {
                System.out.print(", ");
            }
            System.out.printf("% 5.2f", queryVector[k]);
        }
        System.out.println(" )");

//        System.out.println("\nTERM SCORES VS. QUERY");
        for (int i = 0; i < termVectors.length; ++i) {
//            double score = dotProduct(queryVector, termVectors[i], scales);
            double score = cosine(queryVector, termVectors[i], scales);
//            System.out.printf("  %d: % 5.2f  %s\n", i, score, terms[i]);
            if (score > 0 && !querys.contains(terms[i])) {
                termsSuggest.add(new TermSuggest(terms[i], score));
            }
        }

        List<TermSuggest> list = getNewTermsSuggestList(termsSuggest);

        return list;

    }

    /**
     * Devuelve una lista de sugerencias de documentos, apartir de la consulta especificada.
     *
     * @param query términos de la consulta de búsqueda
     *
     * @return  documentos sugeridos
     */
    public List<DocSuggest> getDocsSuggest(String query) {
        List<DocSuggest> docsSuggest = new ArrayList<DocSuggest>();
        String[] queryTerms = query.split(" |,"); // space or comma separated

        double[] queryVector = new double[singularValue];
        Arrays.fill(queryVector, 0.0);

        for (String term : queryTerms) {
            addTermVector(term, termVectors, queryVector);
        }


        System.out.println("\nQuery=" + Arrays.asList(queryTerms));
        System.out.print("Query Vector=(");
        for (int k = 0; k < queryVector.length; ++k) {
            if (k > 0) {
                System.out.print(", ");
            }
            System.out.printf("% 5.2f", queryVector[k]);
        }
        System.out.println(" )");

//        System.out.println("\nDOCUMENT SCORES VS. QUERY");
        for (int j = 0; j < docVectors.length; ++j) {
//            double score = dotProduct(queryVector, docVectors[j], scales);
            double score = cosine(queryVector, docVectors[j], scales);
//            System.out.printf("  %d: % 5.2f  %s\n", j, score, docs[j]);
            if (score > 0) {
                docsSuggest.add(new DocSuggest(docs[j].getName(), docs[j].getFilePath(), score));
            }
        }

        List<DocSuggest> list = getNewDocsSuggestList(docsSuggest);

        OutputMonitor.printLine("[Class] LSIManager [Method] getDocsSuggest.", OutputMonitor.TRACE_MESSAGE);

        return list;
    }

    /**
     * Devuelve una lista de sugerencias de términos de consulta y documentos, apartir
     * de la consulta especificada.
     * 
     * @param query términos de la consulta de búsqueda
     *
     * @return términos y documentos sugeridos
     */
    public TermDocSuggest getTermsDocsSuggest(String query) {

        List<DocSuggest> docsSuggest = new ArrayList<DocSuggest>();
        List<TermSuggest> termsSuggest = new ArrayList<TermSuggest>();
//        String[] queryTerms = query.split(" |,"); // space or comma separated
        List<String> querys = Arrays.asList(query.split(" |,"));
        double[] queryVector = new double[singularValue];
        Arrays.fill(queryVector, 0.0);

        for (String term : querys) {
            addTermVector(term, termVectors, queryVector);
        }


        System.out.println("\nQuery=" + querys);
        System.out.print("Query Vector=(");
        for (int k = 0; k < queryVector.length; ++k) {
            if (k > 0) {
                System.out.print(", ");
            }
            System.out.printf("% 5.2f", queryVector[k]);
        }
        System.out.println(" )");

//        System.out.println("\nDOCUMENT SCORES VS. QUERY");
        for (int j = 0; j < docVectors.length; ++j) {
//            double score = dotProduct(queryVector, docVectors[j], scales);
            double score = cosine(queryVector, docVectors[j], scales);
//            System.out.printf("  %d: % 5.2f  %s\n", j, score, docs[j]);
            if (score > 0) {
                docsSuggest.add(new DocSuggest(docs[j].getName(), docs[j].getFilePath(), score));
            }

        }

//        System.out.println("\nTERM SCORES VS. QUERY");
        for (int i = 0; i < termVectors.length; ++i) {
//            double score = dotProduct(queryVector, termVectors[i], scales);
            double score = cosine(queryVector, termVectors[i], scales);
//            System.out.printf("  %d: % 5.2f  %s\n", i, score, terms[i]);
            if (score > 0 && !querys.contains(terms[i])) {
                termsSuggest.add(new TermSuggest(terms[i], score));
            }
        }

        List<DocSuggest> docsList = getNewDocsSuggestList(docsSuggest);
        List<TermSuggest> termsList = getNewTermsSuggestList(termsSuggest);
        TermDocSuggest termsDocsSuggest = new TermDocSuggest(termsList, docsList);

        OutputMonitor.printLine("[Class] LSIManager [Method] getTermsDocsSuggest.", OutputMonitor.TRACE_MESSAGE);

        return termsDocsSuggest;
    }

    /**
     *
     * @param term
     * @param termVectors
     * @param queryVector
     */
    public void addTermVector(String term, double[][] termVectors, double[] queryVector) {
        for (int i = 0; i < termsStemmer.length; ++i) {
            if (termsStemmer[i].equalsIgnoreCase(stemmerTerm(term))) {
//            if (terms[i].equals(term)) {
                for (int j = 0; j < singularValue; ++j) {
                    queryVector[j] += termVectors[i][j];
                }
                return;

            }
        }
    }

    /**
     *
     * @param xs
     * @param ys
     * @param scales
     * @return
     */
    public double dotProduct(double[] xs, double[] ys, double[] scales) {
        double sum = 0.0;
        for (int k = 0; k < xs.length; ++k) {
            sum += xs[k] * ys[k] * scales[k];
        }
        return sum;
    }

    /**
     *
     * @param xs
     * @param ys
     * @param scales
     * @return
     */
    public double cosine(double[] xs, double[] ys, double[] scales) {
        double product = 0.0;
        double xsLengthSquared = 0.0;
        double ysLengthSquared = 0.0;
        for (int k = 0; k < xs.length; ++k) {
            double sqrtScale = Math.sqrt(scales[k]);
            double scaledXs = sqrtScale * xs[k];
            double scaledYs = sqrtScale * ys[k];
            xsLengthSquared += scaledXs * scaledXs;
            ysLengthSquared += scaledYs * scaledYs;
            product += scaledXs * scaledYs;
        }
        return product / Math.sqrt(xsLengthSquared * ysLengthSquared);
    }

    // este método devuelve la lista final de términos a sugerir al cliente
    private List<TermSuggest> getNewTermsSuggestList(List<TermSuggest> termsSuggest) {
        List<TermSuggest> list;
        int size = termsSuggest.size();

        if (size != 0) {
            Collections.sort(termsSuggest);
            if (size > singularValue) {
                int sizeSuggest = size / (singularValue * 3);

                if (sizeSuggest > 0) {
                    list = new ArrayList<TermSuggest>(termsSuggest.subList(0, sizeSuggest));
                } else {
                    list = new ArrayList<TermSuggest>(termsSuggest.subList(0, singularValue));
                }
            } else {
                list = termsSuggest;
            }
        } else {
            list = new ArrayList<TermSuggest>(0);
        }

        return list;
    }

    //TODO arreglar el valor de selection de la lista
    // este método devuelve la lista final de términos a sugerir al cliente
    private List<DocSuggest> getNewDocsSuggestList(List<DocSuggest> docsSuggest) {
        List<DocSuggest> list;
        int size = docsSuggest.size();

        if (size != 0) {
            Collections.sort(docsSuggest);
            if (size > singularValue) {
                int sizeSuggest = size / singularValue;
                list = docsSuggest.subList(0, sizeSuggest);
            } else {
                list = docsSuggest;
            }
        } else {
            list = new ArrayList<DocSuggest>(0);
        }

        return list;
    }

    private String[] stemmerTerms(String[] terms) {
        String[] finalTerms = new String[terms.length];
        for (int i = 0; i < terms.length; i++) {
            finalTerms[i] = stemmerTerm(terms[i]);

        }
        OutputMonitor.printLine("[Class] LSIManager [Method] stemmerTerms.", OutputMonitor.TRACE_MESSAGE);
        return finalTerms;
    }

    private String stemmerTerm(String term) {

        stemmer.setCurrent(term);
        stemmer.stem();
        return stemmer.getCurrent();
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

    /**
     *
     * @return
     */
    public double[][] getValsMatrix() {
        return valsMatrix;
    }

    /**
     *
     * @param valsMatrix
     */
    public void setValsMatrix(double[][] valsMatrix) {
        this.valsMatrix = valsMatrix;
    }

    /**
     *
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     *
     * @return
     */
    public DocInfo[] getDocs() {
        return docs;
    }

    /**
     *
     * @param docs
     */
    public void setDocs(DocInfo[] docs) {
        this.docs = docs;
    }

    /**
     *
     * @return
     */
    public String[] getTerms() {
        return terms;
    }

    /**
     *
     * @param terms
     */
    public void setTerms(String[] terms) {
        this.terms = terms;
    }

    /**
     *
     * @param c
     */
    public void setInitValues(CollectionInfo c) {
        valsMatrix = AWEntropy.assignmentOfWeights(c);
        language = "english";
        terms = c.getTerms().toArray(new String[0]);
        singularValue = c.getSingularValue();
        docs = c.getDocs().toArray(new DocInfo[0]);
        initSVDMatrix();
    }
}
