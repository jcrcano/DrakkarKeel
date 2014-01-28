/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval.improves;

import drakkar.oar.DocSuggest;

public class DocResource implements IDocResource {

    protected static final short I_HIT_PROPERTY_SCORE = 0;
    protected static final short I_HIT_PROPERTY_RANK = 1;
    protected static final short I_HIT_PROPERTY_NORMALIZED_VALUE = 2;
    private String id;
    private double score;
    private long rank;
    private double normalizedValue;
    private double combinedValue;
    private short hitProperty;
    private DocSuggest doc;

    /**
     *
     * @param doc
     */
    public DocResource(DocSuggest doc) {
        super();
        this.id = doc.getPath();
        this.doc = doc;
        this.score = doc.getScore();
        this.setHitPropertyScore();
    }

    /**
     *
     * @param doc
     * @param rank
     */
    public DocResource(DocSuggest doc, long rank) {
        super();
        this.id = doc.getPath();
        this.doc = doc;
        this.score = doc.getScore();
        this.rank = rank;
        this.setHitPropertyScore();

    }

    /**
     *
     * @param doc
     * @param rank
     * @param normalizedValue
     * @param combinedValue
     */
    public DocResource(DocSuggest doc, long rank, double normalizedValue, double combinedValue) {
        super();
        this.id = doc.getPath();
        this.doc = doc;
        this.score = doc.getScore();
        this.rank = rank;
        this.normalizedValue = normalizedValue;
        this.combinedValue = combinedValue;
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

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public DocResource clone() {
        try {
            return (DocResource) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setHitPropertyScore() {
        this.hitProperty = I_HIT_PROPERTY_SCORE;
    }

    /**
     * {@inheritDoc}
     */
    public void setHitPropertyRank() {
        this.hitProperty = I_HIT_PROPERTY_RANK;
    }

    /**
     * {@inheritDoc}
     */
    public void setHitPropertyNormalizedValue() {
        this.hitProperty = I_HIT_PROPERTY_NORMALIZED_VALUE;
    }

    /**
     *
     * @return
     */
    public double getValue() {
        if (this.hitProperty == I_HIT_PROPERTY_SCORE) {
            return this.getScore();
        } else if (this.hitProperty == I_HIT_PROPERTY_RANK) {
            return (double) this.getRank();
        } else if (this.hitProperty == I_HIT_PROPERTY_NORMALIZED_VALUE) {
            return this.getNormalizedValue();
        } else {
            throw new RuntimeException("Unrecognized hit property");
        }
    }

    public void dump() {
        System.out.print("Id: " + this.id + ", Score: " + this.score + ", Rank: " + this.rank + ", NormalizedValue: " + this.normalizedValue + ", CombinedValue: " + this.combinedValue);
    }

    public DocSuggest getDocSuggest() {
        return  doc;
    }
}
