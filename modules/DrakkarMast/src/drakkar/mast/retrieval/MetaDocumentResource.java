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
import es.uam.eps.nets.rankfusion.interfaces.IFResource;

public class MetaDocumentResource implements IFResource {

    protected static final short I_HIT_PROPERTY_SCORE = 0;
    protected static final short I_HIT_PROPERTY_RANK = 1;
    protected static final short I_HIT_PROPERTY_NORMALIZED_VALUE = 2;
    private String id;
    private double score;
    private long rank;
    private double normalizedValue;
    private double combinedValue;
    private short hitProperty;

    private DocumentMetaData metaDocument;


  
    /**
     *
     * @param metaDoc
     * @param rank
     * @param normalizedValue
     * @param combinedValue
     */
    public MetaDocumentResource(DocumentMetaData metaDoc, long rank, double normalizedValue, double combinedValue) {
        super();
        this.metaDocument = metaDoc;
        this.id = metaDoc.getName();
        this.score = metaDoc.getScore();
        this.rank = rank;
        this.normalizedValue = normalizedValue;
        this.combinedValue = combinedValue;
        this.setHitPropertyScore();
    }


    /**
     *
     * @param metaDoc
     * @param rank
     */
    public MetaDocumentResource(DocumentMetaData metaDoc, long rank) {
        super();
        this.metaDocument = metaDoc;
        this.id = metaDoc.getName();
        this.score = metaDoc.getScore();
        this.rank = rank;
        this.setHitPropertyScore();

    }

    /**
     *
     * @param metaDoc
     * @param score
     * @param rank
     */
    public MetaDocumentResource(DocumentMetaData metaDoc,double score, long rank) {
        super();
        this.metaDocument = metaDoc;
        this.id = metaDoc.getName();
        this.score = score;
        this.rank = rank;
        this.setHitPropertyScore();

      
    }

    /**
     * @return the combinedValue
     */
    public double getCombinedValue() {
        return this.combinedValue;
    }

    /**
     * @param combinedValue the combinedValue to set
     */
    public void setCombinedValue(double combinedValue) {
        this.combinedValue = combinedValue;
    }

    /**
     * @return the normalizedValue
     */
    public double getNormalizedValue() {
        return this.normalizedValue;
    }

    /**
     * @param normalizedValue the normalizedValue to set
     */
    public void setNormalizedValue(double normalizedValue) {
        this.normalizedValue = normalizedValue;
    }

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the rank
     */
    public long getRank() {
        return this.rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(long rank) {
        this.rank = rank;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return this.score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     *
     * @return
     */
    public DocumentMetaData getMetaDocument() {
        return metaDocument;
    }

    /**
     *
     * @param metaDocument
     */
    public void setMetaDocument(DocumentMetaData metaDocument) {
        this.metaDocument = metaDocument;
    }




    @Override
    public MetaDocumentResource clone() {
        try {
            return (MetaDocumentResource) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setHitPropertyScore() {
        this.hitProperty = MetaDocumentResource.I_HIT_PROPERTY_SCORE;
    }

    /**
     *
     */
    public void setHitPropertyRank() {
        this.hitProperty = MetaDocumentResource.I_HIT_PROPERTY_RANK;
    }

    /**
     *
     */
    public void setHitPropertyNormalizedValue() {
        this.hitProperty = MetaDocumentResource.I_HIT_PROPERTY_NORMALIZED_VALUE;
    }

    /**
     *
     * @return
     */
    public double getValue() {
        if (this.hitProperty == MetaDocumentResource.I_HIT_PROPERTY_SCORE) {
            return this.getScore();
        } else if (this.hitProperty == MetaDocumentResource.I_HIT_PROPERTY_RANK) {
            return (double) this.getRank();
        } else if (this.hitProperty == MetaDocumentResource.I_HIT_PROPERTY_NORMALIZED_VALUE) {
            return this.getNormalizedValue();
        } else {
            throw new RuntimeException("Unrecognized hit property");
        }
    }


    /**
     * Muestra las propiedades del MetaDocumentResource
     */
    public void dump() {
        System.out.print("Id: " + this.id + ", Score: " + this.score + ", Rank: " + this.rank + ", NormalizedValue: " + this.normalizedValue + ", CombinedValue: " + this.combinedValue);
    }
}
