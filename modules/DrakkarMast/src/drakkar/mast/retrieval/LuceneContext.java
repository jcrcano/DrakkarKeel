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
import drakkar.oar.facade.event.FacadeDesktopListener;
import static drakkar.oar.util.KeyField.*;
import static drakkar.oar.util.KeyMessage.*;
import drakkar.oar.util.KeySearchable;
import drakkar.oar.util.OutputMonitor;
import drakkar.oar.util.Utilities;
import drakkar.mast.IndexException;
import drakkar.mast.SearchException;
import drakkar.mast.recommender.CollectionInfo;
import drakkar.mast.recommender.DocInfo;
import drakkar.mast.recommender.DocTermInfo;
import drakkar.mast.recommender.TermInfo;
import drakkar.mast.retrieval.analysis.NGramAnalyzer;
import drakkar.mast.retrieval.analysis.NGramAnalyzerCaseSensitive;
import drakkar.mast.retrieval.analysis.NGramQuery;
import drakkar.mast.retrieval.analysis.StopStemAnalyzer;
import drakkar.mast.retrieval.analysis.StopStemAnalyzerCaseSensitive;
import drakkar.mast.retrieval.analysis.WikiAnalyzer;
import drakkar.mast.retrieval.analysis.WikiCaseSensitiveAnalyzer;
import drakkar.mast.retrieval.parser.JavaParser;
import drakkar.mast.retrieval.parser.PdfParser;
import com.thoughtworks.qdox.parser.ParseException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryTermScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * context del motor de búsqueda Apache Lucene
 */
public class LuceneContext extends AdvEngineContext {

    private IndexSearcher searcher;
    private boolean appendIndex;
    private IndexWriter writer;
    private IndexWriter writerLSI;
    private PerFieldAnalyzerWrapper fieldAnalyzer;
    private PerFieldAnalyzerWrapper fieldAnalyzerCS;
    private ScoreDoc[] scoreDoc;
    private ScoreDoc scoreDocObj;
    private Query queryq;
    private Query[] queryall;
    private Directory directory;
    private IndexReader reader;
    private TopDocs topDocs;
    private Highlighter hg;
    private Document docum = null;
    private TokenStream tokens;

    /**
     * Default Constructor
     */
    public LuceneContext() {
        defaultIndexPath = "./index/lucene/";
        defaultIndexLSIPath = "./index/lsi/lucene/";
        this.applyLSI = false;

    }

    /**
     * constructor
     *
     * @param listener oyente de los procesos realizados por este motor
     */
    public LuceneContext(FacadeDesktopListener listener) {
        super(listener);
        defaultIndexPath = "./index/lucene/";
        defaultIndexLSIPath = "./index/lsi/lucene/";
        this.applyLSI = false;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> finalResultsList = null;
        this.finalMetaResult = new ArrayList<DocumentMetaData>();
        this.scoreDoc = null;
        this.queryq = null;
        String[] codeAndBooks;
        this.queryall = new Query[5];

        try {
            setStartTimeOfSearch(new Date());

            if (IndexReader.indexExists(FSDirectory.open(this.indexPath))) {

                this.directory = FSDirectory.open(this.indexPath);
                this.reader = IndexReader.open(this.directory);
                this.searcher = new IndexSearcher(this.reader);
                //////////////////////////////////////////////
                try {

                    if (caseSensitive == false) {
                        codeAndBooks = new String[5];
                        codeAndBooks[0] = getDocumentField(FIELD_CODE_ALL_COMMENTS);
                        codeAndBooks[1] = getDocumentField(FIELD_CODE_ALL_SOURCE);
                        codeAndBooks[2] = getDocumentField(FIELD_DOC_TEXT);
                        codeAndBooks[3] = getDocumentField(FIELD_NAME);
                        codeAndBooks[4] = getDocumentField(FIELD_DOC_BOOK);

                        this.setFieldAnalyzer(new PerFieldAnalyzerWrapper(new StopStemAnalyzer()));
                        this.getFieldAnalyzer().addAnalyzer(getDocumentField(FIELD_CODE_ALL_SOURCE), new NGramAnalyzer());
                        this.getFieldAnalyzer().addAnalyzer(getDocumentField(FIELD_NAME), new NGramAnalyzer());

                        String fieldToProcess;
                        for (int i = 0; i < codeAndBooks.length; i++) {
                            fieldToProcess = codeAndBooks[i];

                            this.queryq = new NGramQuery(this.getFieldAnalyzer(), query, fieldToProcess);
                            this.queryall[i] = this.queryq;
                            this.queryq = this.queryq.combine(this.queryall);

                        }

                    } else if (caseSensitive == true) {
                        codeAndBooks = new String[5];
                        codeAndBooks[0] = getDocumentFieldCS(FIELD_CODE_ALL_COMMENTS);
                        codeAndBooks[1] = getDocumentFieldCS(FIELD_CODE_ALL_SOURCE);
                        codeAndBooks[2] = getDocumentFieldCS(FIELD_DOC_TEXT);
                        codeAndBooks[3] = getDocumentFieldCS(FIELD_NAME);
                        codeAndBooks[4] = getDocumentFieldCS(FIELD_DOC_BOOK);

                        this.setFieldAnalyzerCS(new PerFieldAnalyzerWrapper(new StopStemAnalyzerCaseSensitive()));
                        this.getFieldAnalyzerCS().addAnalyzer(getDocumentFieldCS(FIELD_CODE_ALL_SOURCE), new NGramAnalyzerCaseSensitive());
                        this.getFieldAnalyzerCS().addAnalyzer(getDocumentFieldCS(FIELD_NAME), new NGramAnalyzerCaseSensitive());

                        String fieldToProcess;
                        for (int i = 0; i < codeAndBooks.length; i++) {
                            fieldToProcess = codeAndBooks[i];

                            this.queryq = new NGramQuery(this.getFieldAnalyzerCS(), query, fieldToProcess);
                            this.queryall[i] = this.queryq;
                            this.queryq = this.queryq.combine(this.queryall);
                        }
                    }

                    //Finds the top n  hits for query, applying filter if non-null.
                    this.topDocs = this.searcher.search(this.queryq, null, 1000);
                    int totalHits = this.topDocs.totalHits;
                    this.retrievedDocsCount = totalHits;
                    this.scoreDoc = this.topDocs.scoreDocs;
                    setEndTimeOfSearch(new Date());
                    String message = "Lucene retrieved " + totalHits + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'.";
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                    //save results
                    this.finalMetaResult = saveResults(this.scoreDoc, caseSensitive, this.queryq);
                    //delete repeated
                    if (this.finalMetaResult.size() > 1) {
                        deleteRepeated(this.finalMetaResult);
                    }
                    finalResultsList = this.finalMetaResult;

                } catch (IOException ex) {
                    String message = "Class: SearchEngineLucene." + " Method: searchFiles(String query)." + "  Error: " + ex.getMessage();
                    this.notifyTaskProgress(ERROR_MESSAGE, message);
                    throw new SearchException(ex.getMessage());
                }

            } else {
                OutputMonitor.printLine("Index path incorrect", OutputMonitor.ERROR_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, "Index path incorrect");
            }

        } catch (CorruptIndexException ex) {
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new SearchException(ex.getMessage());
        } catch (IOException ex) {
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new SearchException(ex.getMessage());
        }

        this.retrievedDocsCount += finalResultsList.size();
        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> finalResultsList = null;

        try {
            setStartTimeOfSearch(new Date());

            if (IndexReader.indexExists(FSDirectory.open(this.indexPath))) {

                this.directory = FSDirectory.open(this.indexPath);
                this.reader = IndexReader.open(this.directory);
                this.searcher = new IndexSearcher(this.reader);
                ////////////////////////////////////////////

                ArrayList<DocumentMetaData> tempList = search(query, caseSensitive); //busca en toda la colección de documentos
                finalResultsList = this.filterMetaDocuments(docType, tempList);

                this.finalMetaResult = finalResultsList;
                setEndTimeOfSearch(new Date());
                String message = "Lucene retrieved " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'. for doctype " + docType;
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);

            } else {
                OutputMonitor.printLine("Index path incorrect", OutputMonitor.ERROR_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, "Index path incorrect");
            }

        } catch (CorruptIndexException ex) {
            OutputMonitor.printStream("", ex);
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new SearchException(ex.getMessage());
        } catch (IOException ex) {
            OutputMonitor.printStream("", ex);
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new SearchException(ex.getMessage());
        }

        this.retrievedDocsCount += finalResultsList.size();
        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> finalResultsList = null;
        ArrayList<DocumentMetaData> tempList = null;

        setStartTimeOfSearch(new Date());

        for (int i = 0; i < docTypes.length; i++) {
            String doc = docTypes[i];
            tempList = search(query, doc, caseSensitive);
            finalResultsList.addAll(tempList);
        }

        if (finalResultsList.size() > 1) {
            deleteRepeated(finalMetaResult);
        }

        String message = "Lucene retrieved " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'. for doctypes ";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        setEndTimeOfSearch(new Date());

        this.retrievedDocsCount += finalResultsList.size();
        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, int field, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> finalResultsList = null;

        try {
            if (IndexReader.indexExists(FSDirectory.open(this.indexPath))) {

                this.directory = FSDirectory.open(this.indexPath);
                this.reader = IndexReader.open(this.directory);
                this.searcher = new IndexSearcher(this.reader);
                ////////////////////////////////////////////
                this.finalMetaResult = new ArrayList<DocumentMetaData>();
                this.queryq = null;
                setStartTimeOfSearch(new Date());
                try {
                    if (caseSensitive == false) {
                        this.setFieldAnalyzer(new PerFieldAnalyzerWrapper(new StopStemAnalyzer()));
                        this.getFieldAnalyzer().addAnalyzer(getDocumentField(FIELD_CODE_ALL_SOURCE), new NGramAnalyzer());
                        this.getFieldAnalyzer().addAnalyzer(getDocumentField(FIELD_NAME), new NGramAnalyzer());

//                        this.fieldAnalyzer = new PerFieldAnalyzerWrapper(new NGramAnalyzer());
//                        this.fieldAnalyzer.addAnalyzer(getDocumentField(FIELD_CODE_ALL_COMMENTS), new StopStemAnalyzer());
                        String fieldToProcess = getDocumentField(field);
                        this.queryq = new NGramQuery(this.getFieldAnalyzer(), query, fieldToProcess);

                    } else if (caseSensitive == true) {
                        this.setFieldAnalyzerCS(new PerFieldAnalyzerWrapper(new StopStemAnalyzerCaseSensitive()));
                        this.getFieldAnalyzerCS().addAnalyzer(getDocumentFieldCS(FIELD_CODE_ALL_SOURCE), new NGramAnalyzerCaseSensitive());
                        this.getFieldAnalyzerCS().addAnalyzer(getDocumentFieldCS(FIELD_NAME), new NGramAnalyzerCaseSensitive());

//                        this.fieldAnalyzerCS = new PerFieldAnalyzerWrapper(new NGramAnalyzerCaseSensitive());
//                        this.fieldAnalyzerCS.addAnalyzer(getDocumentFieldCS(FIELD_CODE_ALL_COMMENTS), new StopStemAnalyzerCaseSensitive());
                        String fieldToProcess = getDocumentFieldCS(field);
                        this.queryq = new NGramQuery(this.getFieldAnalyzerCS(), query, fieldToProcess);
                    }

                    this.topDocs = this.searcher.search(this.queryq, null, 1000); //Finds the top n  hits for query, applying filter if non-null.
                    int totalHits = this.topDocs.totalHits;
                    this.retrievedDocsCount = totalHits;
                    this.scoreDoc = this.topDocs.scoreDocs;
                    setEndTimeOfSearch(new Date());
                    String message = "Lucene retrieved " + totalHits + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'.";
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                    this.finalMetaResult = saveResults(this.scoreDoc, caseSensitive, this.queryq);
                    if (this.finalMetaResult.size() > 1) {
                        deleteRepeated(this.finalMetaResult);
                    }
                    finalResultsList = this.finalMetaResult;

                } catch (IOException ex) {
                    String message = "Class: SearchEngineLucene." + " Method: searchFiles(String query)." + "  Error: " + ex.getMessage();
                    OutputMonitor.printStream(message, ex);
                    this.notifyTaskProgress(ERROR_MESSAGE, message);
                    throw new SearchException(ex.getMessage());
                }
            } else {
                OutputMonitor.printLine("Index path incorrect", OutputMonitor.ERROR_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, "Index path incorrect");
            }

        } catch (CorruptIndexException ex) {
            OutputMonitor.printStream("", ex);
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new SearchException(ex.getMessage());
        } catch (IOException ex) {
            OutputMonitor.printStream("", ex);
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new SearchException(ex.getMessage());
        }

        this.retrievedDocsCount += finalResultsList.size();
        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, int[] fields, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> tempList = null;
        ArrayList<DocumentMetaData> documents = new ArrayList<DocumentMetaData>();
        int field;
        setStartTimeOfSearch(new Date());

        for (int i = 0; i < fields.length; i++) {
            field = fields[i];
            tempList = this.search(query, field, caseSensitive);
            documents.addAll(tempList);
        }

        if (documents.size() > 1) {
            deleteRepeated(documents);
        }
        setEndTimeOfSearch(new Date());

        String message = "Lucene retrieved " + documents.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'. for fields ";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        this.retrievedDocsCount += documents.size();
        return documents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String docType, int field, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> docsResult = new ArrayList<DocumentMetaData>();
        ArrayList<DocumentMetaData> finalResultsList = new ArrayList<DocumentMetaData>();
        setStartTimeOfSearch(new Date());
        try {
            if (IndexReader.indexExists(FSDirectory.open(this.indexPath))) {
                this.directory = FSDirectory.open(this.indexPath);
                this.reader = IndexReader.open(this.directory);
                this.searcher = new IndexSearcher(this.reader);
                ////////////////////////////////////////////
                docsResult = search(query, field, caseSensitive);

                finalResultsList = this.filterMetaDocuments(docType, docsResult);
                this.finalMetaResult = finalResultsList;
                setEndTimeOfSearch(new Date());
                String message = "Lucene retrieved " + finalResultsList.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'for field and docType";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                this.retrievedDocsCount += finalResultsList.size();
            } else {
                OutputMonitor.printLine("Index path incorrect", OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, "Index path incorrect");
            }
        } catch (IOException ex) {
            OutputMonitor.printStream("", ex);
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
        }

        return finalResultsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String docType, int[] fields, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> tempList = null;
        ArrayList<DocumentMetaData> documents = new ArrayList<DocumentMetaData>();

        try {

            setStartTimeOfSearch(new Date());
            if (IndexReader.indexExists(FSDirectory.open(this.indexPath))) {
                this.directory = FSDirectory.open(this.indexPath);
                this.reader = IndexReader.open(this.directory);
                this.searcher = new IndexSearcher(this.reader);
                /////////////////////////////////////////////
                String docSource;
                for (int i = 0; i < this.documentalSource.size(); i++) {
                    docSource = this.documentalSource.get(i);
                    if (docSource.equalsIgnoreCase(docType)) {
                        if (fields != null && fields.length > 0) {
                            for (Integer field : fields) {
                                if (field != 0) {
                                    tempList = search(query, docType, field, caseSensitive);

                                    if (tempList != null) {
                                        documents.addAll(tempList);
                                    }
                                }
                            }
                            // esto es para eliminar los documentos repetidos.
                            this.deleteRepeated(documents);
                        } else {
                            tempList = search(query, docType, caseSensitive);
                            this.retrievedDocsCount += tempList.size();
                            return tempList;
                        }
                    } else if (docType == null) {
                        tempList = search(query, caseSensitive);
                        this.retrievedDocsCount += tempList.size();
                        return tempList;
                    }
                }

            } else {
                OutputMonitor.printLine("Index path incorrect", OutputMonitor.ERROR_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, "Index path incorrect");
            }

        } catch (CorruptIndexException ex) {
            OutputMonitor.printStream("", ex);
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new SearchException(ex.getMessage());

        } catch (IOException ex) {
            OutputMonitor.printStream("", ex);
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            throw new SearchException(ex.getMessage());
        }

        setEndTimeOfSearch(new Date());
        String message = "Lucene retrieved " + documents.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'for fields and doctype";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        this.retrievedDocsCount += documents.size();
        return documents;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, int field, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> tempList = null;
        ArrayList<DocumentMetaData> documents = new ArrayList<DocumentMetaData>();
        String doc;
        setStartTimeOfSearch(new Date());

        for (int i = 0; i < docTypes.length; i++) {
            doc = docTypes[i];
            if (doc.equals("documents")) {
                tempList = search(query, doc, caseSensitive);
            } else {
                tempList = this.search(query, doc, field, caseSensitive);
            }

            documents.addAll(tempList);
        }

        if (documents.size() > 1) {
            deleteRepeated(documents);
        }

        String message = "Lucene retrieved " + documents.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'. for doctypes and field ";

        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        setEndTimeOfSearch(new Date());

        this.retrievedDocsCount += documents.size();
        return documents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> tempList = null;
        ArrayList<DocumentMetaData> documents = new ArrayList<DocumentMetaData>();
        String doc;
        setStartTimeOfSearch(new Date());

        for (int i = 0; i < docTypes.length; i++) {
            doc = docTypes[i];
            if (doc.equals("documents")) {
                tempList = search(query, doc, caseSensitive);
            } else {
                tempList = this.search(query, doc, fields, caseSensitive);
            }

            documents.addAll(tempList);
        }

        if (documents.size() > 1) {
            deleteRepeated(documents);
        }

        String message = "Lucene retrieved " + documents.size() + " document(s) (in " + getSearchTime() + " milliseconds) that matched query '" + query + "'. for doctypes and fields ";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        setEndTimeOfSearch(new Date());

        this.retrievedDocsCount += documents.size();
        return documents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex() throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        if (applyLSI) {
            this.indexLSIPath = new File(this.defaultIndexLSIPath);
        }

        this.collectionPath = new File(this.defaultCollectionPath);
        long indexedFiles = 0;

        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (this.indexPath != null) {
            indexedFiles = this.build(MAKE_INDEX);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex(File collectionPath) throws IndexException {

        this.indexPath = new File(this.defaultIndexPath);
        if (applyLSI) {
            this.indexLSIPath = new File(this.defaultIndexLSIPath);
        }
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (this.indexPath != null) {
            indexedFiles = this.build(MAKE_INDEX);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex(List<File> collectionPath) throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        this.indexLSIPath = new File(this.defaultIndexLSIPath);
        long indexedFiles = 0;

        if (collectionPath.isEmpty()) {
            OutputMonitor.printLine("The collection does not have files", OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, MAKE_INDEX);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex(File collectionPath, File indexPath) throws IndexException {

        this.indexPath = indexPath;
        if (applyLSI) {
            this.indexLSIPath = new File(this.defaultIndexLSIPath);
        }
        this.collectionPath = collectionPath;
        long indexedFiles = 0;

        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (indexPath != null) {
            indexedFiles = this.build(MAKE_INDEX);
        } else {
            String message = "indexPath is null";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long makeIndex(List<File> collectionPath, File indexPath) throws IndexException {

        this.indexPath = indexPath;
        if (applyLSI) {
            this.indexLSIPath = new File(this.defaultIndexLSIPath);
        }
        long indexedFiles = 0;
        if (collectionPath.isEmpty()) {
            OutputMonitor.printLine("The collection does not have files", OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, MAKE_INDEX);
        } else {
            String message = "indexPath is null";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long updateIndex(File collectionPath) throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (this.indexPath != null) {
            indexedFiles = this.build(ADD_INDEX);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long updateIndex(List<File> collectionPath) throws IndexException {
        this.indexPath = new File(this.defaultIndexPath);
        long indexedFiles = 0;

        if (collectionPath.isEmpty()) {
            OutputMonitor.printLine("The collection does not have files", OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, "The collection does not have files");
            throw new IndexException("The collection does not have files");
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, ADD_INDEX);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long updateIndex(File collectionPath, File indexPath) throws IndexException {
        this.indexPath = indexPath;
        this.collectionPath = collectionPath;
        long indexedFiles = 0;
        if (!this.collectionPath.exists() || this.collectionPath.listFiles().length == 0) {
            String message = collectionPath + "does not exist or is empty";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (indexPath != null) {
            indexedFiles = this.build(ADD_INDEX);
        } else {
            String message = "IndexPath is null";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long updateIndex(List<File> collectionPath, File indexPath) throws IndexException {
        this.indexPath = indexPath;
        long indexedFiles = 0;
        String message;
        if (collectionPath.isEmpty()) {
            message = "The collection does not have files";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } else if (this.indexPath != null) {
            indexedFiles = this.build(collectionPath, ADD_INDEX);
        } else {
            message = "indexPath is null";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        this.indexedDocsCount += indexedFiles;

        return indexedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean loadIndex() throws IndexException {
        this.reader = null;
        boolean flag = false;
        File defaultFile = new File(this.defaultIndexPath);
        if (applyLSI) {
            this.indexLSIPath = new File(this.defaultIndexLSIPath);
        }
        String message;
        try {
            if (!defaultFile.isDirectory() || !defaultFile.exists() || defaultFile == null || IndexReader.indexExists(FSDirectory.open(defaultFile)) == false) {
                message = "Not found index in default index path";
                OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                throw new IndexException(message);

            } else {

                this.reader = IndexReader.open(FSDirectory.open(defaultFile));
                int cant = this.reader.numDocs();
                this.reader.close();

                message = "Loading Lucene...";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    OutputMonitor.printStream("", ex);
                }
                message = "Total of documents of the index: " + cant;
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                flag = true;
                this.notifyLoadedDocument(cant);

                //set path for search
                this.indexPath = defaultFile;

                initLSIManager();
            }

        } catch (CorruptIndexException ex) {
            message = "Class: SearchEngineLucene" + " Method: LoadIndex" + "  Error: " + ex.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } catch (IOException ex) {
            message = "Class: SearchEngineLucene" + " Method: LoadIndex" + "  Error: " + ex.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }
        return flag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean loadIndex(File indexPath) throws IndexException {
        String message;
        try {
            if (applyLSI) {
                this.indexLSIPath = new File(this.defaultIndexLSIPath);
            }
            this.reader = null;

            if (!indexPath.isDirectory() || !indexPath.exists() || indexPath == null || IndexReader.indexExists(FSDirectory.open(indexPath)) == false) {
                message = "Not found index in this directory: " + indexPath.getAbsolutePath();
                throw new IndexException(message);

            } else {

                this.reader = IndexReader.open(FSDirectory.open(indexPath));
                int cant = this.reader.numDocs();
                this.reader.close();

                message = "Loading Lucene...";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    OutputMonitor.printStream("", ex);
                }

                message = "Total of documents of the index: " + cant;
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                this.notifyLoadedDocument(cant);

                //set path for search
                this.indexPath = indexPath;

                return true;
            }

        } catch (CorruptIndexException ex) {
            message = "Class: SearchEngineLucene" + " Method: LoadIndex" + "  Error: " + ex.getMessage();
            OutputMonitor.printStream(message, ex);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        } catch (IOException ex) {
            message = "Class: SearchEngineLucene" + " Method: LoadIndex" + "  Error: " + ex.getMessage();
            OutputMonitor.printStream(message, ex);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean safeToBuildIndex(File indexPath, int operation) throws IndexException {

        boolean flag = true;
        try {
            this.appendIndex = false;
            //File idx = indexPath;
            String idxpath = indexPath.getPath();
            File dir = indexPath.getParentFile();
            String message = null;
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    //ensure that the index folder exists
                    flag = false;
                    message = "Could not create the index folders at: " + dir.getPath() + ".\n" + "Aborting indexing process.";
                    OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                    this.notifyTaskProgress(ERROR_MESSAGE, message);
                    throw new IndexException(message);

                }
            } else if (IndexReader.indexExists(FSDirectory.open(indexPath))) {

                switch (operation) {
                    case MAKE_INDEX:
                        message = "Overwriting index " + idxpath + "\n";
                        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                        deleteFiles(indexPath);
                        flag = true;
                        break;

                    case ADD_INDEX:

                        message = "Appending new files to index " + idxpath + "\n";
                        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                        this.appendIndex = true;
                        flag = true;
                        break;

                    default:
                        message = "Not building index " + idxpath + "\n";
                        OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                        this.notifyTaskProgress(ERROR_MESSAGE, message);
                        flag = false;
                        throw new IndexException(message);

                }
            } else if (operation == ADD_INDEX) {
                flag = false;
                message = "Not exist Lucene index  in this address" + indexPath;
                this.notifyTaskProgress(ERROR_MESSAGE, message);
                throw new IndexException(message);
            }
        } catch (IOException ex) {
            OutputMonitor.printStream("", ex);
        }
        return flag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentField(int field) {

        switch (field) {
            case FIELD_FILEPATH:
                return "filepath";
            case FIELD_NAME:
                return "name";
            case FIELD_CODE_PACKAGE:
                return "package";
            case FIELD_CODE_CLASSES_NAMES:
                return "classesnames";
            case FIELD_CODE_METHODS_NAMES:
                return "methodsnames";
            case FIELD_CODE_ALL_COMMENTS:
                return "allcomments";
            case FIELD_CODE_ALL_SOURCE:
                return "allsource";//todo el contenido del codigo
            case FIELD_CODE_VARIABLES_NAMES:
                return "classesvariables";
            case FIELD_CODE_JAVADOCS:
                return "javadocs";
            case FIELD_DOC_TEXT:
                return "content"; //todo el contenido del doc txt
            case FIELD_DOC_BOOK:
                return "book";
            case AUTHOR_DOCUMENTS:
                return "author";
            case LAST_MODIFIED_DOCUMENTS:
                return "lastModified";
            default:
                return null;
        }

    }

    /**
     * Devuelve los campos correspondientes cuando casesensitive es true
     *
     * @param field
     * @return
     */
    public String getDocumentFieldCS(int field) {

        switch (field) {
            case FIELD_FILEPATH:
                return "filepathcs";
            case FIELD_NAME:
                return "namecs";
            case FIELD_CODE_PACKAGE:
                return "packagecs";
            case FIELD_CODE_CLASSES_NAMES:
                return "classesnamescs";
            case FIELD_CODE_METHODS_NAMES:
                return "methodsnamescs";
            case FIELD_CODE_ALL_COMMENTS:
                return "allcommentscs";
            case FIELD_CODE_ALL_SOURCE:
                return "allsourcecs";
            case FIELD_CODE_VARIABLES_NAMES:
                return "classesvariablescs";
            case FIELD_CODE_JAVADOCS:
                return "javadocscs";
            case FIELD_DOC_TEXT:
                return "contentcs";
            case FIELD_DOC_BOOK:
                return "bookcs";

            default:
                return null;
        }

    }

    /**
     * Método para construir el índice con la colección por defecto
     *
     * @param operación a realizar: MAKE o ADD
     */
    private long build(int operation) throws IndexException {
        long indexedFiles = 0;
        String message = "Lucene index will be created at [" + this.indexPath + "]";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        //inicia la indexacion
        try {
            if (safeToBuildIndex(this.indexPath, operation)) {
                setStartTimeOfIndexation(new Date());
                // this.analyzer = new NGramAnalyzer();

                //TODO I changed theses lines 2012-11-12
//                this.setFieldAnalyzer(new PerFieldAnalyzerWrapper(new NGramAnalyzer()));
//                this.getFieldAnalyzer().addAnalyzer(getDocumentField(FIELD_CODE_ALL_COMMENTS), new StopStemAnalyzer());
                if (this.appendIndex) {
                    //Adding: new docs
                    this.writer = new IndexWriter(FSDirectory.open(this.indexPath), this.getFieldAnalyzer(), false, IndexWriter.MaxFieldLength.UNLIMITED);
                    if (applyLSI) {
                        this.writerLSI = new IndexWriter(FSDirectory.open(this.indexLSIPath), new PerFieldAnalyzerWrapper(new StopStemAnalyzer()), false, IndexWriter.MaxFieldLength.UNLIMITED);
                    }

                    // ("number "+writer.getReader().maxDoc());
                } else {
                    //create or overwrite index
                    this.writer = new IndexWriter(FSDirectory.open(this.indexPath), this.getFieldAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
                    if (applyLSI) {
                        this.writerLSI = new IndexWriter(FSDirectory.open(this.indexLSIPath), new PerFieldAnalyzerWrapper(new StopStemAnalyzer()), true, IndexWriter.MaxFieldLength.UNLIMITED);
                    }

                }

                indexedFiles = indexDocs(this.writer, this.writerLSI, this.collectionPath, operation);

                message = "Optimizing...";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                this.writer.optimize();
                this.writer.close();
                if (applyLSI) {
                    this.writerLSI.optimize();
                    this.writerLSI.close();
                }

                setEndTimeOfIndexation(new Date());
                message = "Indexation Time " + this.getIndexationTime() + " milliseconds.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            }
        } catch (IOException e) {
            message = " caught a " + e.getClass() + "\n with message: " + e.getMessage() + ".";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        initLSIManager(); // inicializar la matriz de LSI

        return indexedFiles;
    }

    private void initLSIManager() {

        if (applyLSI) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        CollectionInfo collectionInfo = getCollectionInfo();
                        lsiManager.setInitValues(collectionInfo);
                    } catch (IndexException ex) {
                        OutputMonitor.printStream("Reading LSI index.", ex);
                    }
                }
            });
            t.start();

        }
    }

    /**
     * Método para construir el índice a partir de una colección de files
     *
     * @param operation ----- operación a realizar: MAKE o ADD
     * @param collectionPath ----- lista de ficheros que representan la
     * colección
     */
    private long build(List<File> collectionPath, int operation) throws IndexException {
        long indexedFiles = 0;

        String message = "Indexing to directory '" + this.indexPath + "'...";
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
        //inicia la indexacion
        try {
            setStartTimeOfIndexation(new Date());
            if (safeToBuildIndex(this.indexPath, operation)) {
                // this.analyzer = new NGramAnalyzer();
                this.setFieldAnalyzer(new PerFieldAnalyzerWrapper(new NGramAnalyzer()));
                this.getFieldAnalyzer().addAnalyzer(getDocumentField(FIELD_CODE_ALL_COMMENTS), new StopStemAnalyzer());

                if (this.appendIndex) {
                    //añadir docs a un indice existente
                    this.writer = new IndexWriter(FSDirectory.open(this.indexPath), this.getFieldAnalyzer(), false, IndexWriter.MaxFieldLength.UNLIMITED);
                    if (applyLSI) {
                        this.writerLSI = new IndexWriter(FSDirectory.open(this.indexLSIPath), new PerFieldAnalyzerWrapper(new StopStemAnalyzer()), false, IndexWriter.MaxFieldLength.UNLIMITED);
                    }

                } else {
                    //crear o sobreescribir
                    this.writer = new IndexWriter(FSDirectory.open(this.indexPath), this.getFieldAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
                    if (applyLSI) {
                        this.writerLSI = new IndexWriter(FSDirectory.open(this.indexLSIPath), new PerFieldAnalyzerWrapper(new StopStemAnalyzer()), true, IndexWriter.MaxFieldLength.UNLIMITED);
                    }
                }
                indexedFiles = indexDocs(this.writer, this.writerLSI, collectionPath, operation);
                message = "Optimizing...";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                this.writer.optimize();
                this.writer.close();
                if (applyLSI) {
                    this.writerLSI.optimize();
                    this.writerLSI.close();
                }

                setEndTimeOfIndexation(new Date());
                message = "Indexation Time " + this.getIndexationTime() + " milliseconds.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            }
        } catch (IOException e) {
            message = " caught a " + e.getClass() + "\n with message: " + e.getMessage() + ".";
            this.notifyTaskProgress(ERROR_MESSAGE, message);
            throw new IndexException(message);
        }

        initLSIManager();

        return indexedFiles;
    }

    /**
     * Indexa los documentos que estan en un vector
     *
     * @param writer
     * @param list
     * @throws IOException
     */
    private int indexDocs(IndexWriter writer, IndexWriter writerLSI, List<File> list, int operation) throws IndexException {
        int docCount = 0;
        File file;
        String message;
        for (int i = 0; i < list.size(); i++) {
            file = list.get(i);
            if (file.getName().endsWith(".java") || file.getName().endsWith(".pdf") || file.getName().endsWith(".txt") || file.getName().endsWith(".xml")) {
                {
                    indexFile(writer, writerLSI, file, operation);
                    message = "Adding: " + file;
                    docCount++;
                    OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                    this.notifyTaskProgress(INFORMATION_MESSAGE, message);
                }
            } else {
                message = "There are files in the collection that are not: .java, pdf, txt o xml documents" + "\n" + "so, they could not be indexed.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);

            }
        }
        return docCount;
    }

    /**
     * Indexa los documentos que estan en un file
     */
    private int indexDocs(IndexWriter writer, IndexWriter writerLSI, File file, int operation) throws IndexException {
        int docCount = 0;
        String message;
        if (file.canRead()) {
            if (file.isDirectory()) {
                String[] files = file.list();
                this.indexedDocsCount = files.length;
                if (files != null) {

                    for (int i = 0; i < files.length; i++) {
                        indexDocs(writer, writerLSI, new File(file, files[i]), operation);
                    }
                }
            } else if (file.getName().endsWith(".java") || file.getName().endsWith(".pdf") || file.getName().endsWith(".txt") || file.getName().endsWith(".xml")) {
                indexFile(writer, writerLSI, file, operation);
                message = "Adding: " + file;
                docCount++;
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);

            } else {
                message = "There are files in the collection that are not: .java, pdf, txt or xml documents" + "\n" + "so, they could not be indexed.";
                OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                this.notifyTaskProgress(INFORMATION_MESSAGE, message);

            }
        }
        return docCount;
    }

    /**
     * Indexacion por campo de cada documento del repositorio
     *
     * @param writer
     * @param f
     */
    private void indexFile(IndexWriter writer, IndexWriter writerLSI, File f, int operation) throws IndexException {

        boolean javaFile = false;
        if (f.isHidden() || !f.exists() || !f.canRead()) {
            return;
        }

        DocumentLucene doc = null; //para case insensitive
        DocumentLucene doccs = null; //para case sensitive
        DocumentLucene contentDocLSI = null; //para case contenido lsi
        DocumentLucene pathDocLSI = null; //para case path lsi
//        String extFile = f.getPath().endsWith(".pdf");
        try {

            setFieldAnalyzer(new PerFieldAnalyzerWrapper(new NGramAnalyzer()));
            setFieldAnalyzerCS(new PerFieldAnalyzerWrapper(new NGramAnalyzerCaseSensitive()));

            if (f.getPath().endsWith(".pdf")) {

                doc = new DocumentLucene();
                doccs = new DocumentLucene();
                if (applyLSI) {
                    contentDocLSI = new DocumentLucene();
                    pathDocLSI = new DocumentLucene();
                }
                PdfParser pdfp = new PdfParser();
                try {
                    pdfp.divideTextforLucene(f, doccs, doc, contentDocLSI);
                } catch (Exception e) {
                }
                pdfp.analyzePdfDocument(f);
                doc.addField(getDocumentField(AUTHOR_DOCUMENTS), pdfp.getAuthor());
                doccs.addField(getDocumentField(AUTHOR_DOCUMENTS), pdfp.getAuthor());
                try {
                    String date = DateFormat.getDateInstance().format(pdfp.getCalModification().getTime());
                    doc.addField(getDocumentField(LAST_MODIFIED_DOCUMENTS), date);
                    doccs.addField(getDocumentField(LAST_MODIFIED_DOCUMENTS), date);
                } catch (Exception ex) {
                    doc.addField(getDocumentField(LAST_MODIFIED_DOCUMENTS), "");
                    doccs.addField(getDocumentField(LAST_MODIFIED_DOCUMENTS), "");
                }
            } else if (f.getPath().endsWith(".java")) {
                getFieldAnalyzer().addAnalyzer(getDocumentField(FIELD_CODE_ALL_COMMENTS), new StopStemAnalyzer());
                getFieldAnalyzerCS().addAnalyzer(getDocumentFieldCS(FIELD_CODE_ALL_COMMENTS), new StopStemAnalyzerCaseSensitive());
                ArrayList<String> comment = new ArrayList<String>();
                ArrayList<String> javadocs = new ArrayList<String>();
                JavaParser jp = new JavaParser();
                jp.AnalyzeDocument(f);
                doc = new DocumentLucene();
                doccs = new DocumentLucene();
                doc.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                doccs.addField(getDocumentFieldCS(FIELD_FILEPATH), f.getCanonicalPath());

                doc.addField(getDocumentField(FIELD_NAME), f.getName());
                doccs.addField(getDocumentFieldCS(FIELD_NAME), f.getName());

                doc.addField(getDocumentField(FIELD_CODE_ALL_SOURCE), jp.getAllSource());
                doccs.addField(getDocumentFieldCS(FIELD_CODE_ALL_SOURCE), jp.getAllSource());

                if (jp.getClassPackage() != null) {
                    doc.addField(getDocumentField(FIELD_CODE_PACKAGE), jp.getClassPackage());
                    doccs.addField(getDocumentFieldCS(FIELD_CODE_PACKAGE), jp.getClassPackage());
                }

                for (int i = 0; i < jp.getClassNumber(); i++) {
                    if (jp.getClassesComments(i) != null) {
                        comment.add(jp.getClassesComments(i));
                    }
                    if (jp.getClassesJDocs(i) != null) {
                        javadocs.add(jp.getClassesJDocs(i));
                    }

                    doc.addField(getDocumentField(FIELD_CODE_CLASSES_NAMES), jp.getClassesNames(i));
                    doccs.addField(getDocumentFieldCS(FIELD_CODE_CLASSES_NAMES), jp.getClassesNames(i));

                    for (int l = 0; l < jp.getClassVariableNumber(); l++) {
                        doc.addField(getDocumentField(FIELD_CODE_VARIABLES_NAMES), jp.getClassesVarName(i, l));
                        doccs.addField(getDocumentFieldCS(FIELD_CODE_VARIABLES_NAMES), jp.getClassesVarName(i, l));
                        if (jp.getClassesCommentVariables(i, l) != null) {
                            comment.add(jp.getClassesCommentVariables(i, l));
                        }
                        if (jp.getVariablesJDocs(i, l) != null) {
                            javadocs.add(jp.getVariablesJDocs(i, l));
                        }

                    }

                    for (int j = 0; j < jp.getClassesMethods(i); j++) {
                        if (jp.getClassesMethodComment(i, j) != null) {
                            comment.add(jp.getClassesMethodComment(i, j));

                        }
                        if (jp.getClassesMethodJDocs(i, j) != null) {
                            javadocs.add(jp.getClassesMethodJDocs(i, j));
                        }

                        doc.addField(getDocumentField(FIELD_CODE_METHODS_NAMES), jp.getClassesMethodsName(i, j));
                        doccs.addField(getDocumentFieldCS(FIELD_CODE_METHODS_NAMES), jp.getClassesMethodsName(i, j));

                    }
                }
                //unir los comentarios
                doc.addField(getDocumentField(FIELD_CODE_ALL_COMMENTS), joinData(comment));
                doccs.addField(getDocumentFieldCS(FIELD_CODE_ALL_COMMENTS), joinData(comment));

                //unir los javadocs
                doc.addField(getDocumentField(FIELD_CODE_JAVADOCS), joinData(javadocs));
                doccs.addField(getDocumentFieldCS(FIELD_CODE_JAVADOCS), joinData(javadocs));

                ///////////////////
                if (applyLSI) {
                    contentDocLSI = new DocumentLucene();
                    contentDocLSI.addField(getDocumentField(FIELD_CODE_ALL_SOURCE), jp.getAllSource());

                    pathDocLSI = new DocumentLucene();
                    pathDocLSI.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                    pathDocLSI.addField(getDocumentField(FIELD_NAME), f.getName());
                }

            } else if (f.getPath().endsWith(".txt")) {
                doc = new DocumentLucene();
                doccs = new DocumentLucene();
                doc.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                doccs.addField(getDocumentFieldCS(FIELD_FILEPATH), f.getCanonicalPath());
                doc.addField(getDocumentField(FIELD_NAME), f.getName());
                doccs.addField(getDocumentFieldCS(FIELD_NAME), f.getName());

                String textFile = Utilities.readFile(f);
                doc.addField(getDocumentField(FIELD_DOC_TEXT), textFile);
                doccs.addField(getDocumentFieldCS(FIELD_DOC_TEXT), textFile);
                ////////////////////
                if (applyLSI) {
                    contentDocLSI = new DocumentLucene();
//                    doclsi.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                    contentDocLSI.addField(getDocumentField(FIELD_DOC_TEXT), textFile);
                    pathDocLSI = new DocumentLucene();
                    pathDocLSI.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                    pathDocLSI.addField(getDocumentField(FIELD_NAME), f.getName());
                }

            } else if (f.getPath().endsWith(".xml")) {

                getFieldAnalyzer().addAnalyzer(getDocumentField(FIELD_DOC_TEXT), new WikiAnalyzer());
                getFieldAnalyzerCS().addAnalyzer(getDocumentFieldCS(FIELD_DOC_TEXT), new WikiCaseSensitiveAnalyzer());

                doc = new DocumentLucene();
                doccs = new DocumentLucene();
                doc.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                doccs.addField(getDocumentFieldCS(FIELD_FILEPATH), f.getCanonicalPath());
                doc.addField(getDocumentField(FIELD_NAME), f.getName());
                doccs.addField(getDocumentFieldCS(FIELD_NAME), f.getName());

                String textFile = Utilities.readFile(f);
                doc.addField(getDocumentField(FIELD_DOC_TEXT), textFile);
                doccs.addField(getDocumentFieldCS(FIELD_DOC_TEXT), textFile);
                ////////////////////
                if (applyLSI) {
                    contentDocLSI = new DocumentLucene();
//                    doclsi.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                    contentDocLSI.addField(getDocumentField(FIELD_DOC_TEXT), textFile);
                    pathDocLSI = new DocumentLucene();
                    pathDocLSI.addField(getDocumentField(FIELD_FILEPATH), f.getCanonicalPath());
                    pathDocLSI.addField(getDocumentField(FIELD_NAME), f.getName());
                }

            }

            writer.addDocument(doc.getDoc(), getFieldAnalyzer());
            writer.addDocument(doccs.getDoc(), getFieldAnalyzerCS());
            ////////
            if (applyLSI) {
                setFieldAnalyzer(new PerFieldAnalyzerWrapper(new StopStemAnalyzer()));
                writerLSI.addDocument(pathDocLSI.getDoc(), getFieldAnalyzer());
                writerLSI.addDocument(contentDocLSI.getDoc(), getFieldAnalyzer());
            }

            if (operation == ADD_INDEX) {
                this.notifyAddedDocument();
            } else if (operation == MAKE_INDEX) {
                this.notifyIndexedDocument();
            }

        } catch (IOException ex) {
            OutputMonitor.printStream("", ex);
            this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
        }
    }

    /**
     * Metodo para unir los comentarios de las variables, los metodos y de la
     * clase de codigo fuente y para unir javadocs también
     *
     * @param aa
     * @return
     */
    private String joinData(ArrayList<String> aa) {
        String result = " ";

        if (!aa.isEmpty()) {
            for (int i = 0; i < aa.size(); i++) {
                if (aa.get(i) != null) {
                    result = result.concat(" " + aa.get(i));
                }
            }
        } else {
            result = " ";
        }

        return result;

    }

    /**
     * Devuelve una lista de DocumentMetaData construida a partir de los
     * resultados de búsqueda
     *
     * @param sd
     * @param queryT
     * @param caseS
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private ArrayList<DocumentMetaData> saveResults(ScoreDoc[] sd, boolean caseS, Query q) {
        DocumentMetaData metaDoc;
        long size = 0;
        String summary = null, filepath = null, name = null, author = null, lastModified = null;
        ArrayList<DocumentMetaData> docsfound = new ArrayList<DocumentMetaData>();
        this.setFieldAnalyzer(null);
        String textfield = null, field = null, filetype = null, scoreString = null;
        float score;
        double ss;

        for (int k = 0; k < sd.length; k++) {
            metaDoc = new DocumentMetaData();

            this.scoreDocObj = sd[k];
            int iddoc = this.scoreDocObj.doc;
            //scoreDocObj.doc es el numero que representa ese doc en los resultados de busqueda

            score = this.scoreDocObj.score;
            scoreString = String.valueOf(score);
            ss = Double.valueOf(scoreString);

            this.docum = null;
            try {
                this.docum = this.searcher.doc(iddoc);
            } catch (CorruptIndexException ex) {
                OutputMonitor.printStream("", ex);
                this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            } catch (IOException ex) {
                OutputMonitor.printStream("", ex);
                this.notifyTaskProgress(ERROR_MESSAGE, ex.getMessage());
            }

            if (caseS == false) {
                filepath = this.docum.get(getDocumentField(FIELD_FILEPATH));
                name = this.docum.get(getDocumentField(FIELD_NAME));
                author = this.docum.get(getDocumentField(AUTHOR_DOCUMENTS));
                lastModified = this.docum.get(getDocumentField(LAST_MODIFIED_DOCUMENTS));
                //this.analyzer = new NGramAnalyzer();
                this.setFieldAnalyzer(new PerFieldAnalyzerWrapper(new NGramAnalyzer()));
                this.getFieldAnalyzer().addAnalyzer(getDocumentField(FIELD_CODE_ALL_COMMENTS), new StopStemAnalyzer());

            } else {
                filepath = this.docum.get(getDocumentFieldCS(FIELD_FILEPATH));
                name = this.docum.get(getDocumentFieldCS(FIELD_NAME));
                author = this.docum.get(getDocumentField(AUTHOR_DOCUMENTS));
                lastModified = this.docum.get(getDocumentField(LAST_MODIFIED_DOCUMENTS));
                //this.analyzer = new NGramAnalyzerCaseSensitive();
                this.setFieldAnalyzer(new PerFieldAnalyzerWrapper(new NGramAnalyzerCaseSensitive()));
                this.getFieldAnalyzer().addAnalyzer(getDocumentFieldCS(FIELD_CODE_ALL_COMMENTS), new StopStemAnalyzerCaseSensitive());
            }

            filetype = getFileExtension(filepath);
            File f = new File(filepath);
            size = f.length();

            if (filetype.equalsIgnoreCase("java")) {
                //  field = getDocumentField(SearchAssignable.FIELD_CODE_ALL_COMMENTS);
                //textfield = this.docum.get(getDocumentField(SearchAssignable.FIELD_CODE_ALL_COMMENTS));
                field = getDocumentField(FIELD_CODE_ALL_SOURCE);
                textfield = docum.get(getDocumentField(FIELD_CODE_ALL_SOURCE));

            } else if (filetype.equalsIgnoreCase("pdf")) {
                field = getDocumentField(FIELD_DOC_BOOK);
                textfield = this.docum.get(getDocumentField(FIELD_DOC_BOOK));

            } else if (filetype.equalsIgnoreCase("txt")) {
                field = getDocumentField(FIELD_DOC_TEXT);
                textfield = this.docum.get(getDocumentField(FIELD_DOC_TEXT));
            }
            if (textfield != null) {
                String temp = getHighlighter(q, this.getFieldAnalyzer(), textfield, field);
                summary = filterTags(filterTags(temp, "<B>"), "</B>");
            } else {
                summary = " ";
            }
            //////////////////////////////
            metaDoc.setName(name);
            metaDoc.setPath(filepath);
            metaDoc.setIndex(iddoc);
            metaDoc.setAuthor(author);
            metaDoc.setLastModified(lastModified);
            //   System.err.println("lucene "+iddoc);
            metaDoc.setSynthesis(summary);
            metaDoc.setSize(size);
            metaDoc.setType(filetype);
            metaDoc.setScore(ss);
            metaDoc.setSearcher(KeySearchable.LUCENE_SEARCH_ENGINE);
            docsfound.add(metaDoc);

        } //end for
        return docsfound;
    }

    /**
     * Para la sumarización
     *
     * @return
     */
    private String getHighlighter(Query q, Analyzer a, String text, String field) {

        String summary = null;

        this.hg = new Highlighter(new QueryTermScorer(q));
        this.hg.setTextFragmenter(new SimpleFragmenter(20));
        this.hg.setMaxDocCharsToAnalyze(600);

        try {
            try {
                this.tokens = TokenSources.getTokenStream(field, text, a);
                summary = this.hg.getBestFragments(this.tokens, text, 20, "...");
                // summary = this.hg.getBestFragments(this.tokens, text, 10).toString();
            } catch (IOException ex) {
                OutputMonitor.printStream("IO", ex);
            }
        } catch (InvalidTokenOffsetsException ex) {
            OutputMonitor.printStream("", ex);
        }

        if (summary == null) {
            summary = " ";
        }
        return summary;
    }

    /**
     * Método para eliminar etiquetas producidas en el summary por el analyzer
     *
     * @param text
     * @param mark
     * @return
     */
    public String filterTags(String text, String mark) {
        String result = "";

        if (text.contains(mark)) {
            String[] array = text.split(mark);
            for (int i = 0; i < array.length; i++) {
                result += array[i];
            }
        }
        return result;
    }

    /**
     *
     * @param indexDirectory
     * @return
     * @throws IndexException
     */
    public CollectionInfo getCollectionInfo(String indexDirectory) throws IndexException {
        this.defaultIndexLSIPath = indexDirectory;
        return getCollectionInfo();

    }

    /**
     * Este método obtiene la relación de ocurrencia de los términos en el
     * índice de la colección especificada.
     *
     * @return relación documentos por término
     *
     * @throws IndexException si ocurre una error el el proceso de obtención de
     * los términos de la colección.
     */
    public CollectionInfo getCollectionInfo() throws IndexException {
        try {
            this.indexLSIPath = new File(defaultIndexLSIPath);
            this.directory = FSDirectory.open(this.indexLSIPath);
            if (IndexReader.indexExists(this.directory)) {
                // se verifica que exista un índice en el directorio especificado
                this.reader = IndexReader.open(this.directory);
                TermEnum terms = this.reader.terms(); // se obtienen todos los términos del índice de la colección

                Map<TermInfo, List<DocTermInfo>> termsMap = new HashMap<TermInfo, List<DocTermInfo>>();
                List<DocTermInfo> list;
                Term termItem;
                TermDocs docs;
                int docsCount = 0, termsCount = 0;
                docs = this.reader.termDocs();
                Document doc;
                List<String> termsList = new ArrayList<String>();
                Set<Integer> docsIds = new HashSet<Integer>();
                docsCount = this.reader.numDocs();
                Map<Integer, Integer> docsMap = new HashMap<Integer, Integer>();
                List<DocInfo> docInfoList = new ArrayList<DocInfo>(docsCount);
                String name, filePath;
                int index = 0;
                for (int i = 0; i < docsCount; i += 2) {
                    doc = this.reader.document(i);
                    name = doc.get("name");
                    filePath = doc.get("filepath");
                    docInfoList.add(new DocInfo(name, filePath));
                    docsMap.put(i + 1, index);
                    index++;
                }

                docsMap.remove(docsCount + 1);

                while (terms.next()) {
                    termItem = terms.term();
                    list = new ArrayList<DocTermInfo>();
                    docs = this.reader.termDocs(termItem);
                    while (docs.next()) {
                        int docNum = docs.doc();
                        if (!(docNum % 2 == 0)) {
                            doc = this.reader.document(docNum);
                            int termFreq = docs.freq();
                            list.add(new DocTermInfo(docsMap.get(docNum), termFreq));
                            docsIds.add(docNum);
                        }
                    }

                    if (!list.isEmpty()) {
                        termsMap.put(new TermInfo(termsCount, termItem.text(), reader.docFreq(termItem)), list);
                        termsList.add(termItem.text());
                        termsCount++;
                    }
                }
                return new CollectionInfo(termsMap, "Apache Lucene", termsList, docInfoList, singularValue);
            } else {
                throw new IndexException("Index invalid. Not exist index in the directory: " + defaultIndexLSIPath);
            }

        } catch (IOException ex) {
            throw new IndexException(ex.getMessage());
        }

    }

    /**
     * @return the fieldAnalyzer
     */
    public PerFieldAnalyzerWrapper getFieldAnalyzer() {
        return fieldAnalyzer;
    }

    /**
     * @param fieldAnalyzer the fieldAnalyzer to set
     */
    public void setFieldAnalyzer(PerFieldAnalyzerWrapper fieldAnalyzer) {
        this.fieldAnalyzer = fieldAnalyzer;
    }

    /**
     * @return the fieldAnalyzerCS
     */
    public PerFieldAnalyzerWrapper getFieldAnalyzerCS() {
        return fieldAnalyzerCS;
    }

    /**
     * @param fieldAnalyzerCS the fieldAnalyzerCS to set
     */
    public void setFieldAnalyzerCS(PerFieldAnalyzerWrapper fieldAnalyzerCS) {
        this.fieldAnalyzerCS = fieldAnalyzerCS;
    }
}
