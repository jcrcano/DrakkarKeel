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

import drakkar.oar.util.OutputMonitor;
import drakkar.mast.retrieval.DocumentLucene;
import drakkar.mast.retrieval.DocumentMinion;
import com.sun.labs.minion.SimpleIndexer;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * Para extraer de una documento PDF: contenido, título, autor, sumario, número
 * de páginas, fecha de creación y de modificación --version de PDFBox 1.0.0
 * --nota: Terrier utiliza PDFBox 0.6.7a
 *
 *
 */
public class PdfParser {

    PDDocument pdoc = null;
    PDFTextStripper pdfText = null;
    StringWriter swriter = null;
    PDDocumentInformation pinf = null;
    private String title = null;
    private String author = null;
    private int numberPages = 0;
    private Calendar calcreation = null;
    private Calendar calmodification = null;
    private String allContent;

    /**
     * Default constructor
     */
    public PdfParser() {
    }

    /**
     * Para extraer contenido del pdf
     *
     * @param f
     * @return
     */
    public boolean analyzePdfDocument(File f) {

        try {

            pdoc = PDDocument.load(f);

            if (!pdoc.isEncrypted() && pdoc.getCurrentAccessPermission().canExtractContent() && pdoc.getNumberOfPages() != 0) {

                this.numberPages = pdoc.getNumberOfPages();
                pdfText = new PDFTextStripper();

                swriter = new StringWriter();

                ////////////////////datos
                pinf = pdoc.getDocumentInformation();
                if (pinf == null) {
                    OutputMonitor.printLine("The document does not have available information.", OutputMonitor.INFORMATION_MESSAGE);
                } else {
                    setTitle(pinf.getTitle());
                    setAuthor(pinf.getAuthor());
                    setNumberpages(pdoc.getNumberOfPages());
                    setCalCreation(pinf.getCreationDate());
                    setCalModification(pinf.getModificationDate());

                    pdfText.writeText(pdoc, swriter);
                    allContent = swriter.getBuffer().toString();
                }

                pdoc.close();
                swriter.close();

                return true;

            } else {
                OutputMonitor.printLine("Encrypted document.", OutputMonitor.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            OutputMonitor.printStream("", ex);
        } finally {
            if (pdoc != null) {
                try {
                    pdoc.close();
                } catch (IOException ex) {
                    OutputMonitor.printStream("IO", ex);
                }
            }
        }

        return false;
    }

    /**
     * Divide el contenido del pdf de 100 en 100 páginas de acuerdo al número
     * total para el motor de búsqueda Minion
     *
     * @param f
     * @param indexer indexador de Minion
     * @throws IOException
     */
    public void divideTextforMinion(File f, SimpleIndexer indexer) throws IOException {
        pdoc = PDDocument.load(f);
        this.numberPages = pdoc.getNumberOfPages();

        if (!pdoc.isEncrypted() && pdoc.getCurrentAccessPermission().canExtractContent() && pdoc.getNumberOfPages() != 0) {

            String fragment = null;
            int start = 0, end = 0;
            int count = 0;

            if (this.numberPages > 100) {

                for (int i = 0; i < numberPages; i = i + 100) {
                    count++; //para el key del document
                    swriter = new StringWriter();
                    pdfText = new PDFTextStripper();

                    start = i;
                    end = 99 + i;

                    if (end > numberPages) {
                        end = numberPages;
                    }

                    pdfText.setStartPage(start);
                    pdfText.setEndPage(end);
                    pdfText.writeText(pdoc, swriter);

                    fragment = swriter.getBuffer().toString();

                    DocumentMinion docm = new DocumentMinion(indexer, f.getPath() + count);
                    docm.addField("filepath", f.getAbsolutePath());
                    docm.addField("name", f.getName());
                    docm.addField("book", fragment);
                    docm.closeDocument();

                    swriter.close();
                }

                pdoc.close();

            } else { //si tiene menos de 100 páginas toma todo el texto como está

                swriter = new StringWriter();
                pdfText = new PDFTextStripper();

                pdfText.writeText(pdoc, swriter);
                fragment = swriter.getBuffer().toString();
                DocumentMinion docm = new DocumentMinion(indexer, f.getPath());
                docm.addField("filepath", f.getAbsolutePath());
                docm.addField("name", f.getName());
                docm.addField("book", fragment);
                docm.closeDocument();

                swriter.close();
                pdoc.close();
            }
        } else {
            OutputMonitor.printLine("Encrypted book.", OutputMonitor.INFORMATION_MESSAGE);
        }

        if (pdoc != null) {
            try {
                pdoc.close();
            } catch (IOException ex) {
                OutputMonitor.printStream("", ex);
            }
        }

    }

    /**
     * Divide el contenido del pdf de 100 en 100 páginas de acuerdo al número
     * total para el motor de búsqueda Lucene
     *
     * @param f
     * @param doccs
     * @param doc
     * @param doclsi
     * @throws IOException
     */
    public void divideTextforLucene(File f, DocumentLucene doccs, DocumentLucene doc, DocumentLucene doclsi) throws IOException {
        pdoc = PDDocument.load(f);
        this.numberPages = pdoc.getNumberOfPages();

        if (!pdoc.isEncrypted() && pdoc.getCurrentAccessPermission().canExtractContent() && pdoc.getNumberOfPages() != 0) {

            String fragment = null;
            int start = 0, end = 0;
            int count = 0;

            if (this.numberPages > 100) {

                for (int i = 0; i < numberPages; i = i + 100) {
                    count++; //para el key del document
                    swriter = new StringWriter();
                    pdfText = new PDFTextStripper();

                    start = i;
                    end = 99 + i;

                    if (end > numberPages) {
                        end = numberPages;
                    }

                    pdfText.setStartPage(start);
                    pdfText.setEndPage(end);
                    pdfText.writeText(pdoc, swriter);

                    fragment = swriter.getBuffer().toString();

                    doc.addField("filepath", f.getCanonicalPath());
                    doccs.addField("filepathcs", f.getCanonicalPath());
                    doc.addField("name", f.getName());
                    doccs.addField("namecs", f.getName());
                    doc.addField("book", fragment);
                    doccs.addField("bookcs", fragment);
                    ///////
                    if (doclsi != null) {
                        doclsi.addField("book", fragment);

                    }

                    swriter.close();
                }

                pdoc.close();

            } else { //si tiene menos de 100 páginas toma todo el texto como está

                swriter = new StringWriter();
                pdfText = new PDFTextStripper();

                //index
                pdfText.writeText(pdoc, swriter);
                fragment = swriter.getBuffer().toString();
                doc.addField("filepath", f.getCanonicalPath());
                doccs.addField("filepathcs", f.getCanonicalPath());
                doc.addField("name", f.getName());
                doccs.addField("namecs", f.getName());
                doc.addField("book", fragment);
                doccs.addField("bookcs", fragment);

                if (doclsi != null) {
                    doclsi.addField("book", fragment);

                }
                swriter.close();
                pdoc.close();
            }
        } else {
            OutputMonitor.printLine("Encrypted book.", OutputMonitor.INFORMATION_MESSAGE);            
        }

        if (pdoc != null) {
            try {
                pdoc.close();
            } catch (IOException ex) {
                OutputMonitor.printStream("", ex);
            }
        }

    }

///////////////////////////SET y GET
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the numberpages
     */
    public int getNumberpages() {
        return numberPages;
    }

    /**
     * @param numberpages the numberpages to set
     */
    public void setNumberpages(int numberpages) {
        this.numberPages = numberpages;
    }

    /**
     * @return the cal
     */
    public Calendar getCalCreation() {
        return calcreation;
    }

    /**
     * @param cal the cal to set
     */
    public void setCalCreation(Calendar cal) {
        this.calcreation = cal;
    }

    /**
     * @return the calmod
     */
    public Calendar getCalModification() {
        return calmodification;
    }

    /**
     * @param calmod the calmod to set
     */
    public void setCalModification(Calendar calmod) {
        this.calmodification = calmod;
    }

    /**
     * @return the allContent
     */
    public String getAllContent() {
        return allContent;
    }

    /**
     * @param allContent the allContent to set
     */
    public void setAllContent(String allContent) {
        this.allContent = allContent;
    }
}
