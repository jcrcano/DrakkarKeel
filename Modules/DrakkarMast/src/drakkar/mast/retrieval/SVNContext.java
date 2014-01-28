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
import drakkar.oar.security.DrakkarSecurity;
import static drakkar.oar.util.KeyMessage.*;
import drakkar.oar.util.KeySearchable;
import drakkar.oar.util.OutputMonitor;
import drakkar.oar.util.Utilities;
import drakkar.mast.IndexException;
import drakkar.mast.SearchException;
import static drakkar.mast.retrieval.FileIndexable.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.store.FSDirectory;
import pl.infovide.SVNIndexUpdater.Index;
import pl.infovide.SVNIndexUpdater.IndexRepositoryList;
import pl.infovide.SVNIndexer.ConfigReaderException;
import pl.infovide.SVNInfo.ConfProviderException;
import pl.infovide.SVNInfo.ConfigurationProvider;
import pl.infovide.SVNInfo.ConfigurationProviderSearch;
import pl.infovide.SVNSearcher.IndexSearcherSVN;
import pl.infovide.SVNSearcher.SearchResultBean;
import pl.infovide.SVNSearcher.SearchResultEntry;

/**
 * Contexto para indexar y buscar en repositorios SVN
 */
public class SVNContext extends CVSContextable {

    private Index index;
    private ConfigurationProvider provider;
    private IndexRepositoryList indexList;
    long indexedDocs = 0;
    String message = null;
    int loadedDocs = 0;
    Properties properties;
    private IndexSearcherSVN searcher;
    private SearchResultBean searchResultBean;

    /**
     *
     * @param listener
     */
    public SVNContext(FacadeDesktopListener listener) {
        super(listener);
        defaultIndexPath = "./index/svn/";

    }

    /**
     *
     */
    public SVNContext() {
        defaultIndexPath = "./index/svn/";

    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex() throws IndexException {

        if (safeToBuildIndex(new File(this.properties.getProperty("indexPath")), MAKE_INDEX)) {
            ConfigurationProvider.setProp(getProperties());
            try {
                this.indexedDocs = build();

            } catch (ConfigReaderException ex) {
                OutputMonitor.printStream("", ex);
            }
            message = "SVNIndexer finished";
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
            this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        } else {
            message = "Is not safe to build index in the path selected";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);
        }

        return indexedDocs;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(File indexPath) throws IOException, IndexException {
        IndexReader reader = null;
        boolean flag = false;

        if (!indexPath.isDirectory() || !indexPath.exists() || indexPath == null || IndexReader.indexExists(FSDirectory.open(indexPath)) == false) {
            message = "Not found index in default index path";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            throw new IndexException(message);

        } else {

            reader = IndexReader.open(FSDirectory.open(indexPath));
            loadedDocs = reader.numDocs();
            reader.close();

            message = "Loading SVN index...";
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
            this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                message = "Error loading index: " + ex.toString();
                OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, message);
            }
            message = "Total of documents of the index: " + loadedDocs;
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
            this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            flag = true;
            this.notifyLoadedDocument(loadedDocs);

        }

        return flag;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex() throws IndexException, IOException {
        IndexReader reader = null;
        File defaultFile = new File(this.defaultIndexPath);

        boolean flag = false;

        if (!defaultFile.isDirectory() || !defaultFile.exists() || defaultFile == null || IndexReader.indexExists(FSDirectory.open(defaultFile)) == false) {
            message = "Not found index in default index path";
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            throw new IndexException(message);

        } else {

            reader = IndexReader.open(FSDirectory.open(defaultFile));
            loadedDocs = reader.numDocs();
            reader.close();

            message = "Loading SVN index...";
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
            this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                message = "Error loading index: " + ex.toString();
                OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
                this.notifyTaskProgress(ERROR_MESSAGE, message);
            }
            message = "Total of documents of the index: " + loadedDocs;
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
            this.notifyTaskProgress(INFORMATION_MESSAGE, message);
            flag = true;
            this.notifyLoadedDocument(loadedDocs);

        }

        return flag;
    }

    private boolean safeToBuildIndex(File indexPath, int operation) throws IndexException {

        boolean flag = true;
        try {

            String idxpath = indexPath.getPath();
            File dir = indexPath.getParentFile();
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
                        Utilities.deleteFiles(indexPath);
                        flag = true;
                        break;

                    case ADD_INDEX:
                        message = "Appending new files to index " + idxpath + "\n";
                        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
                        this.notifyTaskProgress(INFORMATION_MESSAGE, message);
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

    private long build() throws ConfigReaderException {

        provider = new ConfigurationProvider();
        try {

            provider.getConfiguration();

        } catch (ConfProviderException e) {
            message = "SVNIndexer failed:" + e.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        } catch (FileNotFoundException e) {
            message = "SVNIndexer failed:" + e.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        } catch (IOException e) {
            message = "SVNIndexer failed:" + e.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        }

        index = new Index();

        //finding existed index or create new in given directory
        try {
            index.prepareIndex(provider.getIndexPath());
        } catch (IOException ex) {
            message = "SVNIndexer failed. Index is not prepared:" + ex.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        }

        message = "Indexing to: " + provider.getIndexPath();
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        indexList = new IndexRepositoryList(provider.getIndexPath());
        try {
            indexList.setIndexRepository(provider.getUrl(), provider.getUser(), DrakkarSecurity.decryptPassword(provider.getPassword()), provider.getDirectPath());
            indexList.updateIndexedRepositories(provider.getUser(), DrakkarSecurity.decryptPassword(provider.getPassword()));

            int countIndexed = 0;
            try {
                boolean load = loadIndex(new File(provider.getIndexPath()));
                if (load) {
                    countIndexed = loadedDocs;
                }
            } catch (IndexException ex) {
                OutputMonitor.printStream("", ex);
            }

            this.notifyIndexedDocument(countIndexed);
            return countIndexed;

        } catch (IOException ex) {
            message = "SVNIndexer failed:" + ex.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        } catch (ConfigReaderException e) {
            message = "SVNIndexer failed:" + e.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        } catch (ClassNotFoundException e) {
            message = "SVNIndexer failed:" + e.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        }
        return 0;
    }

    /**
     *
     * @return
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     *
     * @param properties
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public ArrayList<DocumentMetaData> search(String query, String sort, String fileType, String date, String user, boolean onfileBody) throws SearchException {

        ArrayList<DocumentMetaData> listResults = new ArrayList<DocumentMetaData>();

        ConfigurationProviderSearch.setProperties(properties);
        ConfigurationProviderSearch prov = new ConfigurationProviderSearch();
        try {
            prov.getConfiguration(properties);
        } catch (ConfProviderException ex) {
            ex.printStackTrace();
        }

        String indexDirectory = prov.getIndexPath();
        String urlRegexp = prov.getUrlRegexp();
        String urlReplacement = prov.getUrlReplacement();
        searcher = new IndexSearcherSVN(indexDirectory, properties);
        searchResultBean = new SearchResultBean();
        long before = System.currentTimeMillis();
        this.searchResultBean.setSort(sort);

        StringBuffer finallQuery = null;
        if (query != null) {
            finallQuery = constructQueryString(query, fileType, date, onfileBody, searchResultBean);
        }
        if (finallQuery.toString() == null || finallQuery.toString().equals("") || finallQuery.toString().trim().equals("")) {
            message = "Query empty";
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
            this.notifyTaskProgress(INFORMATION_MESSAGE, message);

        }
        /*Getting phrase from all query */
        String simpleQuery = makePhraseFromQuery(query);
        Object[] res = null;

        try {

            res = searcher.search(finallQuery.toString(), user, sort, true);

        } catch (OutOfMemoryError e) {
            message = "Server isn't prepared for that amount of results.\n Try query which gives less results. More info in logs." + e.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        } catch (BooleanQuery.TooManyClauses e) {
            message = "SVN Searcher configuration isn't prepared for this amount of clauses.\n "
                    + "Increas \'maxClouseCount\' property in your properties file\n" + e.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        } catch (RuntimeException e) {
            message = "Please use other criterion of sort, this one is not served in this index.\n " + e.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

            // all other exceptions are logged only for application administrator. SVNSearcher user see only one type of message.
        } catch (Exception e) {
            message = "SVNSearcher isn't configured correctly. Please contact with administrator - more details in SVNSearcher logs.  " + e.getMessage();
            OutputMonitor.printLine(message, OutputMonitor.ERROR_MESSAGE);
            this.notifyTaskProgress(ERROR_MESSAGE, message);

        }
        @SuppressWarnings("unchecked")
        ArrayList<SearchResultEntry> result = (ArrayList<SearchResultEntry>) res[0];
        Integer numberOfDocuments = (Integer) res[1];
        Float searchTime = (Float) res[2];
        Integer numberOfFoundDocuments = (Integer) res[3];

        analyzeQuery(simpleQuery, result, urlRegexp, urlReplacement);

        /// @li Creating Bean with all results
        if (res[3] != null) {
            this.searchResultBean.setSearchTime(searchTime);
        }
        long after = System.currentTimeMillis();
        this.searchResultBean.setAllTime(new Float((float) (after - before) / 1000f));
        this.searchResultBean.setSimplePhrase(simpleQuery);
        this.searchResultBean.setResult(result);
        this.searchResultBean.setNumberOfAllDocuments(numberOfDocuments.intValue());
        this.searchResultBean.setNumberOfFoundDocuments(numberOfFoundDocuments.intValue());

        listResults = saveDocumentMetadata(searchResultBean.getResult());
        return listResults;
    }

    /**
     * This method checks if 'query' comes from Advanced Searching site. If yes
     * and advanced options was used, then add to query special characters
     * ("+Name" or "+Type" and put 'query phrase' into '(' ')' ). Result of this
     * parsing is written as 'QueryString' in result bean.
     *
     * @param request Request for the servlet
     * @param searchResultBean Bean which stores result and information about
     * one searching process
     * @return Final query which is parsed query if request was from
     * AdvancedSearching site, or returns 'search' field if there was no
     * advanced option used.
     */
    private StringBuffer constructQueryString(String query, String fileType, String date, boolean fileBody, SearchResultBean searchResultBean) {
        StringBuffer finallQuery = new StringBuffer();
        boolean advancedSearch = fileBody;

        /*
         * Parsing Date to format : +Date[yyyymmdd TO yyymmdd]
         */
        if (date != null && !date.equals("") && !date.equals("ALL")) {
            Date today = new Date(System.currentTimeMillis());
            java.text.SimpleDateFormat todaySimple = new java.text.SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            String todayS = todaySimple.format(today);
            String[] tableToday = todayS.split(" ");

            int y = Integer.parseInt(tableToday[0].substring(0, 4));
            int m = Integer.parseInt(tableToday[0].substring(5, 7));
            int d = Integer.parseInt(tableToday[0].substring(8, 10));

            int yFrom = y;
            int mFrom = m;

            if (date.equals("1MON")) {
                mFrom = m - 1;
                if (mFrom <= 0) {
                    mFrom = 12 + mFrom;
                    yFrom -= 1;
                }
            } else if (date.equals("3MON")) {
                mFrom = m - 3;
                if (mFrom <= 0) {
                    mFrom = 12 + mFrom;
                    yFrom -= 1;
                }
            } else if (date.equals("6MON")) {
                mFrom = m - 6;
                if (mFrom <= 0) {
                    mFrom = 12 + mFrom;
                    yFrom -= 1;
                }
            } else if (date.equals("12MON")) {
                mFrom = m - 12;
                if (mFrom <= 0) {
                    mFrom = 12 + mFrom;
                    yFrom -= 1;
                }
            }
            String dToday = ("" + d).length() < 2 ? ("0" + d) : ("" + d);
            String mToday = ("" + m).length() < 2 ? ("0" + m) : ("" + m);
            String mFromS = ("" + mFrom).length() < 2 ? ("0" + mFrom) : ("" + mFrom);

            String fromString = ("" + yFrom) + mFromS + dToday;
            String todayString = ("" + y) + mToday + dToday;
            finallQuery.append(" +Date:[" + fromString + " TO " + todayString + "] ");
            advancedSearch = true;
        }

        if ((fileType != null) && (!fileType.equals(""))) {
            if (!fileType.equals("ALL")) {
                finallQuery.append(" +Type:" + fileType);
                advancedSearch = true;
                searchResultBean.setFileType(fileType);
            }
        } else {
            /* Cleanup empty string from parameter */
            searchResultBean.setFileType(null);
        }

        if (advancedSearch && query != null && !(query.trim().equals(""))) {
            //if (fileBody != null && fileBody.equals("true")) {
            if (fileBody) {
                finallQuery.append(" +FileBody:(" + query + ")");
            } else {
                finallQuery.append(" +(" + query + ")");
            }
        } else {
            finallQuery.append(query);
        }

        searchResultBean.setQueryString(finallQuery.toString().trim());

        return finallQuery;
    }

    /**
     * This method parse 'phrase' (equals to text from 'search' field in web
     * site form) from given query. This 'phrase' is needed for field 'search'
     * in AdvancedSearching site and for find (and bold) key words in found
     * files.
     *
     * @param search Text from 'search' field in web site form.
     * @return If there was no advanced options used, then returns given
     * 'search'. In other was takes 'phrase' between '(' and ')'.
     */
    private String makePhraseFromQuery(String search) {
        int lstart = search.indexOf("(");
        int lend = search.lastIndexOf(")");
        String simplePhrase = "";
        if (lstart != -1 && lend != -1 && lstart <= lend) {
            simplePhrase = search.substring(lstart + 1, lend);
        } else {
            simplePhrase = search;
        }
        return simplePhrase;
    }

    private void analyzeQuery(String simpleQuery, ArrayList<SearchResultEntry> result, String urlRegexp, String urlReplacement) {
        //cleaning special characters
        String[] tmpQuery = simpleQuery.split(" ");
        ArrayList<String> withNoSpecial = new ArrayList<String>();
        for (String string : tmpQuery) {
            string = string.replaceAll(" ", "");
            string = string.replaceAll("\\{", "");
            string = string.replaceAll("\\}", "");
            string = string.replaceAll("\\[", "");
            string = string.replaceAll("\\]", "");

            if (string.length() != 0) {
                withNoSpecial.add(string);
            }
        }

        // end cleaning
        String[] splitedQuery = new String[withNoSpecial.size()];
        splitedQuery = withNoSpecial.toArray(splitedQuery);
        ArrayList<String> boldStrings = new ArrayList<String>();
        boolean inQuote = false;
        String boldString = "";
        boolean ifMinusChar = false;

        /// @li Analyze user query
        // eg: +Name:/clients/lafarge +Type:XLS +(lafarge)
        for (int i = 0; i < splitedQuery.length; ++i) {

            if (splitedQuery[i] == null || splitedQuery[i].length() == 0 || splitedQuery[i].equals("")) {
                continue;
            }
            if (splitedQuery[i].equals("\t") || splitedQuery[i].equals("\n") || splitedQuery[i].equals(" ") || splitedQuery[i].equals("\r") || splitedQuery[i].equals("\b") || splitedQuery[i].equals("\f")) {
                continue;
            }

            splitedQuery[i] = splitedQuery[i].trim();

            if (inQuote == false && (splitedQuery[i].equals("AND") == true
                    || splitedQuery[i].equals("OR") == true
                    || splitedQuery[i].equals("NOT") == true
                    || splitedQuery[i].equals("TO") == true)) {
                continue;
            }

            if (splitedQuery[i].length() >= 3) {
                if (splitedQuery[i].charAt(0) == '\"' && (splitedQuery[i].charAt(splitedQuery[i].length() - 1) == '\"')) {
                    boldStrings.add(splitedQuery[i].substring(1, splitedQuery[i].length() - 1));
                    ifMinusChar = false;
                    continue;
                }
            }

            if (inQuote == false) {
                if (splitedQuery[i].charAt(0) == '-') {
                    ifMinusChar = true;
                    splitedQuery[i] = splitedQuery[i].replace('-', ' ').trim();
                    continue;
                } else if (splitedQuery[i].charAt(0) == '+') {
                    ifMinusChar = false;
                    splitedQuery[i] = splitedQuery[i].replace('+', ' ').trim();
                    continue;
                }
            }

            if (splitedQuery[i].charAt(0) == '\"') {
                inQuote = true;
                boldString = splitedQuery[i].replace('\"', ' ').trim();
                continue;
            }
            if (splitedQuery[i].indexOf(':') >= 0) {
                if (!(splitedQuery[i].indexOf("\\:") >= 0)) {
                    inQuote = true;
                    boldString = splitedQuery[i].substring(splitedQuery[i].indexOf(':'), splitedQuery[i].length());
                    continue;
                }
            }
            if (splitedQuery[i].charAt(splitedQuery[i].length() - 1) == '\"') {
                inQuote = false;
                if (boldString.length() > 0) {
                    if (boldString.charAt(boldString.length() - 1) != ' ') {
                        boldString += " ";
                    }
                }

                boldString += splitedQuery[i].substring(0, splitedQuery[i].length() - 1);

                if (ifMinusChar == false) {
                    boldStrings.add(boldString);
                }
                ifMinusChar = false;
                continue;
            }

            if (inQuote == true) {
                boldString += " " + splitedQuery[i] + " ";
                continue;
            }

            if (splitedQuery[i].length() != 0 && splitedQuery[i].charAt(0) == '(') {
                splitedQuery[i] = splitedQuery[i].substring(1, splitedQuery[i].length());
            }

            if (splitedQuery[i].length() != 0 && splitedQuery[i].charAt(splitedQuery[i].length() - 1) == ')') {
                splitedQuery[i] = splitedQuery[i].substring(0, splitedQuery[i].length() - 1);
            }

            if (splitedQuery[i].length() != 0 && splitedQuery[i].charAt(0) == '[') {
                splitedQuery[i] = splitedQuery[i].substring(1, splitedQuery[i].length());
            }

            if (splitedQuery[i].length() != 0 && splitedQuery[i].charAt(splitedQuery[i].length() - 1) == ']') {
                splitedQuery[i] = splitedQuery[i].substring(0, splitedQuery[i].length() - 1);
            }

            if (splitedQuery[i].length() != 0 && splitedQuery[i].charAt(0) == '{') {
                splitedQuery[i] = splitedQuery[i].substring(1, splitedQuery[i].length());
            }

            if (splitedQuery[i].length() != 0 && splitedQuery[i].charAt(splitedQuery[i].length() - 1) == '}') {
                splitedQuery[i] = splitedQuery[i].substring(0, splitedQuery[i].length() - 1);
            }

            if (ifMinusChar == false) {
                boldStrings.add(splitedQuery[i]);
            }

            ifMinusChar = false;
        }

        shrinkingContent(boldStrings, result, urlRegexp, urlReplacement);
    }

    private void shrinkingContent(ArrayList<String> boldStrings, ArrayList<SearchResultEntry> result, String urlRegexp, String urlReplacement) {
        /// this variable decide to put '...' on the end of each file body, or not.
        boolean isEndOfBody = false;
        /// @li Documents content shrinking
        for (int i = 0; i < result.size(); ++i) {
            isEndOfBody = false;
            SearchResultEntry entry = result.get(i);
            String fileBody = entry.getFileBody();
            String name = entry.getFileName().replaceAll(urlRegexp, urlReplacement);
            entry.setFileName(name);

            int from = 0, to = 0;
            if (fileBody != null) {
                Pattern pattern = null;
                Matcher matcher = null;
                if (boldStrings.size() > 0) {
                    try {
                        pattern = Pattern.compile("(?i)\\b" + boldStrings.get(0) + "\\b");
                        matcher = pattern.matcher(fileBody);
                    } catch (PatternSyntaxException e) {
                        this.searchResultBean.setError("Incorrect query syntax: " + e.getMessage());
                    }
                    if (pattern != null && matcher != null && matcher.find() == true) {
                        if (matcher.start() + 300 / 2 >= fileBody.length()) {
                            to = fileBody.length();
                            isEndOfBody = true;
                        } else {
                            to = matcher.start() + 300 / 2;
                        }
                        if (matcher.start() - 300 / 2 <= 0) {
                            from = 0;
                            if ((to + 300 / 2 - matcher.start()) >= fileBody.length()) {
                                to = fileBody.length();
                                isEndOfBody = true;
                            } else {
                                to += 300 / 2 - matcher.start();
                            }
                        } else {
                            from = matcher.start() - 300 / 2;
                            if (matcher.start() + 300 / 2 >= fileBody.length()) {
                                from -= 300 / 2 - (fileBody.length() - matcher.start());
                                if (from < 0) {
                                    from = 0;
                                }
                            }
                        }
                    } else {
                        from = 0;
                        if (fileBody.length() <= 600 / 2) {
                            to = fileBody.length();
                            isEndOfBody = true;
                        } else {
                            to = 600 / 2;
                        }
                    }
                } else {
                    if (300 <= fileBody.length()) {
                        to = 300;
                    } else {
                        to = fileBody.length();
                        isEndOfBody = true;
                    }
                }

                fileBody = fileBody.substring(from, to);
                fileBody = fileBody.replaceAll("&", "&amp;");
                fileBody = fileBody.replaceAll("<", "&lt;");
                fileBody = fileBody.replaceAll(">", "&gt;");
                fileBody = fileBody.replaceAll("\"", "&quot;");
                fileBody = fileBody.replaceAll("'", "&apos;");

                // @INFO: this part of code, can be not optimize.
                /// @li Bolder for key word.
                for (int ii = 0; ii < boldStrings.size(); ++ii) {
                    StringBuffer temp = new StringBuffer(fileBody.length());

                    try {
                        pattern = Pattern.compile("(?i)\\b" + boldStrings.get(ii) + "\\b");
                    } catch (PatternSyntaxException e) {
                        continue;
                    }
                    matcher = pattern.matcher(fileBody);

                    int start2 = 0;
                    while (true) {
                        if (matcher.find() == false) {
                            if (start2 != fileBody.length()) {
                                temp.append(fileBody.substring(start2, fileBody.length()));
                            }
                            start2 = fileBody.length();
                            break;
                        }

                        temp.append(fileBody.substring(start2, matcher.start()));
                        temp.append("<b>" + fileBody.substring(matcher.start(), matcher.end()) + "</b>");
                        start2 = matcher.end();
                    }

                    fileBody = temp.toString();
                }

                /*
                 * Start - Making endLines in equal intervals ('lineLenght')
                 */
                fileBody = fileBody.replaceAll("\n", " ");
                fileBody = fileBody.replaceAll("([ \t\n\r\f]*<BR>)+", "");

                int lineLenght = 70;
                for (; lineLenght < fileBody.length(); lineLenght += 70) {

                    /// how long can be word for find space after them at the end of each line.
                    int maxWordLength = 20;
                    int li2 = 0;
                    /// looking for space - line should be ended when space or ';' or ','
                    /// but if 'maxWordLength' is shorter than iterator on last word in line, then the word is ended
                    /// with no space on the end.
                    for (; li2 < maxWordLength; li2++) {
                        if (fileBody.length() > (li2 + lineLenght + 1)) {
                            char checkedChar = fileBody.charAt(li2 + lineLenght);
                            if (checkedChar == ' ' || checkedChar == ';' || checkedChar == ',') {
                                lineLenght = lineLenght + li2;
                                break;
                            }
                        }
                    }
                    String beforeString = fileBody.substring(0, lineLenght);
                    String afterString = fileBody.substring(lineLenght);
                    fileBody = beforeString + "<BR>" + afterString;
                }
                /*
                 * End
                 */

                if (!isEndOfBody) {
                    fileBody = fileBody + "...";
                }
                entry.setFileBody(fileBody);
            } else {
                entry.setFileBody("");
            }
            String pom = name;
            pom = pom.toLowerCase();
            String auth = entry.getAuthor();
            if (auth != null) {
                auth = auth.toLowerCase();
            }
            entry.setBoldName(name);
            for (int ii = 0; ii < boldStrings.size(); ++ii) {
                String pom2 = boldStrings.get(ii).toLowerCase();
                if (pom.lastIndexOf(pom2) > 0) {
                    entry.setBoldName(name.substring(0, pom.lastIndexOf(pom2)) + "<b>" + name.substring(pom.lastIndexOf(pom2), pom.lastIndexOf(pom2) + pom2.length()) + "</b>" + name.substring((pom.lastIndexOf(pom2) + pom2.length())));
                }
                pom2 = boldStrings.get(ii).toLowerCase();
                if (auth != null && auth.equals(pom2)) {
                    entry.setAuthor("<b>" + entry.getAuthor() + "</b>");
                }

            }
        }
    }

    private ArrayList<DocumentMetaData> saveDocumentMetadata(ArrayList<SearchResultEntry> result) {

        DocumentMetaData metaData;
        ArrayList<DocumentMetaData> list = new ArrayList<DocumentMetaData>();
        String name, path, synthesis, author, type = null;
        String lastModified= null;
        float size = 0;
        int numberIndex;
        int idsearcher = KeySearchable.SVN_SEARCHER;
        double score;

        for (int i = 0; i < result.size(); i++) {
            SearchResultEntry searchResultEntry = result.get(i);
            name = searchResultEntry.getShortFileName();
            author = searchResultEntry.getAuthor();
            lastModified = DateFormat.getDateInstance().format(searchResultEntry.getLastModified());
            path = searchResultEntry.getFileName();
            synthesis = "Last commit by: " + searchResultEntry.getAuthor() + "\n" + "\n" + "Last commit date: " + searchResultEntry.getLastModified() + "\n" + "\n" + "Comment to commit: " + searchResultEntry.getComment() + "\n" + "\n" + "Body: " + searchResultEntry.getFileBody();
            score = searchResultEntry.getHitScore();
            numberIndex = searchResultEntry.getNumber();//not good....
            type = searchResultEntry.getType();

            //todo el numberindex y size estan en cero
            metaData = new DocumentMetaData(author, lastModified, name, path, size, synthesis, type, numberIndex, score, idsearcher);
            list.add(metaData);

        }
        return list;
    }
}
