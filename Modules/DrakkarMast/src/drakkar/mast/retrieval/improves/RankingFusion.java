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
import drakkar.oar.DocumentMetaData;
import drakkar.oar.ResultSetDocument;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.ResultSetTerm;
import drakkar.oar.TermSuggest;
import es.uam.eps.nets.rankfusion.GenericRankAggregator;
import es.uam.eps.nets.rankfusion.GenericSearchResults;
import es.uam.eps.nets.rankfusion.combination.MNZCombiner;
import es.uam.eps.nets.rankfusion.combination.MinCombiner;
import es.uam.eps.nets.rankfusion.combination.SumCombiner;
import es.uam.eps.nets.rankfusion.interfaces.IFCombiner;
import es.uam.eps.nets.rankfusion.interfaces.IFNormalizer;
import es.uam.eps.nets.rankfusion.interfaces.IFRankAggregator;
import es.uam.eps.nets.rankfusion.interfaces.IFResource;
import es.uam.eps.nets.rankfusion.interfaces.IFSearchResults;
import es.uam.eps.nets.rankfusion.normalization.RankSimNormalizer;
import es.uam.eps.nets.rankfusion.normalization.StdNormalizer;
import es.uam.eps.nets.rankfusion.normalization.ZMUVNormalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Esta clase implementa lo diferentes métodos de fusión de resultados
 *
 * 
 *
 */
public class RankingFusion {

    /**
     *
     */
    public static final int STANDAR_NORMALIZE = 0;
    /**
     *
     */
    public static final int ZMUV_NORMALIZE = 1;
    /**
     *
     */
    public static final int RANKSIM_NORMALIZE = 2;
    /**
     *
     */
    public static final int DEFAULT_NORMALIZE = 3;
    /**
     *
     */
    public static final int SUM_COMBINER = 4;
    /**
     *
     */
    public static final int MIN_COMBINER = 5;
    /**
     *
     */
    public static final int MNZ_COMBINER = 6;
    /**
     *
     */
    public static final int DEFAULT_COMBINER = 7;
    static final Comparator<DocumentMetaData> DESCENDING_ORDER = new Comparator<DocumentMetaData>() {

        public int compare(DocumentMetaData doc1, DocumentMetaData doc2) {
            return Double.compare(doc2.getScore(), doc1.getScore());
        }
    };

    /**
     * Ejecuta la normalización de una lista de documentos, para el normalizador
     * seleccionado
     *
     * @param list       lista de documentos
     * @param normalizer normalizador
     *
     * @return documentos normalizados
     */
    public static List<DocumentMetaData> normalize(List<DocumentMetaData> list, int normalizer) {

        long index = 1;
        Collections.sort(list, DESCENDING_ORDER);
        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
        for (DocumentMetaData metaDocument : list) {
            IFResource resource = new MetaResource(metaDocument, index);
            index++;
            //TODO why Long
            resourcesList.put(new Long(metaDocument.getPath().hashCode()), resource);
        }

        IFSearchResults sr = new GenericSearchResults(resourcesList);
        IFNormalizer norm = null;

        switch (normalizer) {

            case STANDAR_NORMALIZE:
                norm = new StdNormalizer();
                break;
            case ZMUV_NORMALIZE:
                norm = new ZMUVNormalizer();
                break;
            case RANKSIM_NORMALIZE:
                norm = new RankSimNormalizer();
                break;
            case DEFAULT_NORMALIZE:
                norm = new StdNormalizer();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized normalizer");
        }

        norm.normalize(sr);
        List<DocumentMetaData> normalizedList = new ArrayList<DocumentMetaData>();
        MetaResource temp = null;
        for (IFResource aggResult : sr.getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE)) {
            temp = (MetaResource) aggResult;
            normalizedList.add(temp.getMetaDocument());
        }

        return normalizedList;
    }

    /**
     * Ejecuta la fusión de las dos listas de documentos
     *
     * @param listA      lista de documentos A
     * @param listB      lista de documentos A
     * @param normalizer normalizador
     * @param combiner   combinador
     *
     * @return lista de documentos fusionados
     */
    public static List<DocumentMetaData> fusion(List<DocumentMetaData> listA, List<DocumentMetaData> listB, int normalizer, int combiner) {
        long index = 1;
        // sort list A based on the values
        Collections.sort(listA, DESCENDING_ORDER);
        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
        for (DocumentMetaData metaDocument : listA) {
            IFResource resource = new MetaResource(metaDocument, index);
            index++;
            resourcesList.put(new Long(metaDocument.getPath().hashCode()), resource);
        }
        IFSearchResults srA = new GenericSearchResults(resourcesList);


        // sort list B based on the values
        index = 1;
        Collections.sort(listB, DESCENDING_ORDER);
        resourcesList = new HashMap<Long, IFResource>();
        for (DocumentMetaData metaDocument : listB) {
            IFResource resource = new MetaResource(metaDocument, index);
            index++;
            resourcesList.put(new Long(metaDocument.getPath().hashCode()), resource);
        }
        IFSearchResults srB = new GenericSearchResults(resourcesList);

        // Aggregated lists
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();
        srList.add(srA);
        srList.add(srB);
        IFNormalizer norm = null;

        switch (normalizer) {

            case STANDAR_NORMALIZE:
                norm = new StdNormalizer();
                break;
            case ZMUV_NORMALIZE:
                norm = new ZMUVNormalizer();
                break;
            case RANKSIM_NORMALIZE:
                norm = new RankSimNormalizer();
                break;
            case DEFAULT_NORMALIZE:
                norm = new StdNormalizer();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized normalizer");
        }


        IFCombiner comb = null;

        switch (combiner) {
            case SUM_COMBINER:
                comb = new SumCombiner();
                break;
            case MIN_COMBINER:
                comb = new MinCombiner();
                break;
            case MNZ_COMBINER:
                comb = new MNZCombiner();
                break;
            case DEFAULT_COMBINER:
                comb = new SumCombiner();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized combiner");
        }


        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        List<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        List<DocumentMetaData> aggregatedResults = new ArrayList<DocumentMetaData>();
        MetaResource temp = null;

        for (IFResource aggResult : aggResults) {
            temp = (MetaResource) aggResult;
            aggregatedResults.add(temp.getMetaDocument());
        }

        return aggregatedResults;

    }

    /**
     * Ejecuta la fusión de un conjunto de listas de documentos
     *
     * @param resultSet
     * @param list       listas de documentos
     * @param normalizer normalizador
     * @param combiner   combinador
     *
     * @return lista de documentos fusionados
     */
    public static List<DocumentMetaData> fusion(ResultSetMetaData resultSet, int normalizer, int combiner) {

        List<List<DocumentMetaData>> list = resultSet.getResultList();

        long index = 1;
        // sort list based on the values

        HashMap<Long, IFResource> resourcesList;
        IFResource resource;
        IFSearchResults sr;
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();

        for (List<DocumentMetaData> arrayList : list) {
            Collections.sort(arrayList, DESCENDING_ORDER);
            resourcesList = new HashMap<Long, IFResource>();

            for (DocumentMetaData metaDocument : arrayList) {
                resource = new MetaResource(metaDocument, index);
                index++;
                //TODO why Long
                resourcesList.put(new Long(metaDocument.getPath().hashCode()), resource);
            }
            sr = new GenericSearchResults(resourcesList);
            srList.add(sr);
        }


        IFNormalizer norm = null;

        switch (normalizer) {

            case STANDAR_NORMALIZE:
                norm = new StdNormalizer();
                break;
            case ZMUV_NORMALIZE:
                norm = new ZMUVNormalizer();
                break;
            case RANKSIM_NORMALIZE:
                norm = new RankSimNormalizer();
                break;
            case DEFAULT_NORMALIZE:
                norm = new StdNormalizer();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized normalizer");
        }



        IFCombiner comb = null;

        switch (combiner) {
            case SUM_COMBINER:
                comb = new SumCombiner();
                break;
            case MIN_COMBINER:
                comb = new MinCombiner();
                break;
            case MNZ_COMBINER:
                comb = new MNZCombiner();
                break;
            case DEFAULT_COMBINER:
                comb = new SumCombiner();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized combiner");
        }

        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        List<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        List<DocumentMetaData> aggregatedResults = new ArrayList<DocumentMetaData>();
        MetaResource temp = null;

        for (IFResource aggResult : aggResults) {
            temp = (MetaResource) aggResult;
            aggregatedResults.add(temp.getMetaDocument());
        }

        return aggregatedResults;

    }



    /**
     * Ejecuta la fusión de un conjunto de listas de documentos
     *
     * @param resultSet
     * @param list       listas de documentos
     * @param normalizer normalizador
     * @param combiner   combinador
     *
     * @return lista de documentos fusionados
     */
    public static List<TermSuggest> termsFusion(ResultSetTerm resultSet, int normalizer, int combiner) {

        List<List<TermSuggest>> list = resultSet.getResults();

        long index = 1;
        // sort list based on the values

        HashMap<Long, IFResource> resourcesList;
        IFResource resource;
        IFSearchResults sr;
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();

        for (List<TermSuggest> arrayList : list) {
           
            resourcesList = new HashMap<Long, IFResource>();

            for (TermSuggest metaDocument : arrayList) {
                resource = new TermResource(metaDocument, index);
                index++;
                //TODO why Long
                resourcesList.put(new Long(metaDocument.getTerm().hashCode()), resource);
            }
            sr = new GenericSearchResults(resourcesList);
            srList.add(sr);
        }


        IFNormalizer norm = null;

        switch (normalizer) {

            case STANDAR_NORMALIZE:
                norm = new StdNormalizer();
                break;
            case ZMUV_NORMALIZE:
                norm = new ZMUVNormalizer();
                break;
            case RANKSIM_NORMALIZE:
                norm = new RankSimNormalizer();
                break;
            case DEFAULT_NORMALIZE:
                norm = new StdNormalizer();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized normalizer");
        }



        IFCombiner comb = null;

        switch (combiner) {
            case SUM_COMBINER:
                comb = new SumCombiner();
                break;
            case MIN_COMBINER:
                comb = new MinCombiner();
                break;
            case MNZ_COMBINER:
                comb = new MNZCombiner();
                break;
            case DEFAULT_COMBINER:
                comb = new SumCombiner();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized combiner");
        }

        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        List<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        List<TermSuggest> aggregatedResults = new ArrayList<TermSuggest>();
        TermResource temp = null;

        for (IFResource aggResult : aggResults) {
            temp = (TermResource) aggResult;
            aggregatedResults.add(temp.getTermSuggest());
        }

        return aggregatedResults;

    }

    /**
     * Ejecuta la fusión de un conjunto de listas de documentos
     *
     * @param resultSet
     * @param list       listas de documentos
     * @param normalizer normalizador
     * @param combiner   combinador
     *
     * @return lista de documentos fusionados
     */
    public static List<DocSuggest> docsFusion(ResultSetDocument resultSet, int normalizer, int combiner) {

        List<List<DocSuggest>> list = resultSet.getResults();

        long index = 1;
        // sort list based on the values

        HashMap<Long, IFResource> resourcesList;
        IFResource resource;
        IFSearchResults sr;
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();

        for (List<DocSuggest> arrayList : list) {

            resourcesList = new HashMap<Long, IFResource>();

            for (DocSuggest metaDocument : arrayList) {
                resource = new DocResource(metaDocument, index);
                index++;
                //TODO why Long
                resourcesList.put(new Long(metaDocument.getPath().hashCode()), resource);
            }
            sr = new GenericSearchResults(resourcesList);
            srList.add(sr);
        }


        IFNormalizer norm = null;

        switch (normalizer) {

            case STANDAR_NORMALIZE:
                norm = new StdNormalizer();
                break;
            case ZMUV_NORMALIZE:
                norm = new ZMUVNormalizer();
                break;
            case RANKSIM_NORMALIZE:
                norm = new RankSimNormalizer();
                break;
            case DEFAULT_NORMALIZE:
                norm = new StdNormalizer();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized normalizer");
        }



        IFCombiner comb = null;

        switch (combiner) {
            case SUM_COMBINER:
                comb = new SumCombiner();
                break;
            case MIN_COMBINER:
                comb = new MinCombiner();
                break;
            case MNZ_COMBINER:
                comb = new MNZCombiner();
                break;
            case DEFAULT_COMBINER:
                comb = new SumCombiner();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized combiner");
        }

        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        List<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        List<DocSuggest> aggregatedResults = new ArrayList<DocSuggest>();
        DocResource temp = null;

        for (IFResource aggResult : aggResults) {
            temp = (DocResource) aggResult;
            aggregatedResults.add(temp.getDocSuggest());
        }

        return aggregatedResults;

    }



    /**
     * Ejecuta la fusión de las dos listas de documentos
     *
     * @param listA      lista de documentos A
     * @param listB      lista de documentos A
     * @param normalizer normalizador
     * @param combiner   combinador
     *
     * @return lista de documentos fusionados
     */
    public static List<TermSuggest> termsFusion(List<TermSuggest> listA, List<TermSuggest> listB, int normalizer, int combiner) {
        long index = 1;
        // sort list A based on the values
//        Collections.sort(listA, DESCENDING_ORDER);
        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
        for (TermSuggest item : listA) {
            IFResource resource = new TermResource(item, index);
            index++;
            resourcesList.put(new Long(item.getTerm().hashCode()), resource);
        }
        IFSearchResults srA = new GenericSearchResults(resourcesList);

        // sort list B based on the values
        index = 1;
        resourcesList = new HashMap<Long, IFResource>();
        for (TermSuggest item : listB) {
            IFResource resource = new TermResource(item, index);
            index++;
            resourcesList.put(new Long(item.getTerm().hashCode()), resource);
        }
        IFSearchResults srB = new GenericSearchResults(resourcesList);

        // Aggregated lists
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();
        srList.add(srA);
        srList.add(srB);
        IFNormalizer norm = null;

        switch (normalizer) {

            case STANDAR_NORMALIZE:
                norm = new StdNormalizer();
                break;
            case ZMUV_NORMALIZE:
                norm = new ZMUVNormalizer();
                break;
            case RANKSIM_NORMALIZE:
                norm = new RankSimNormalizer();
                break;
            case DEFAULT_NORMALIZE:
                norm = new StdNormalizer();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized normalizer");
        }

        IFCombiner comb = null;

        switch (combiner) {
            case SUM_COMBINER:
                comb = new SumCombiner();
                break;
            case MIN_COMBINER:
                comb = new MinCombiner();
                break;
            case MNZ_COMBINER:
                comb = new MNZCombiner();
                break;
            case DEFAULT_COMBINER:
                comb = new SumCombiner();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized combiner");
        }


        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        List<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        List<TermSuggest> aggregatedResults = new ArrayList<TermSuggest>();
        TermResource temp = null;

        for (IFResource aggResult : aggResults) {
            temp = (TermResource) aggResult;
            aggregatedResults.add(temp.getTermSuggest());
        }

        return aggregatedResults;


    }

    /**
     * Ejecuta la fusión de las dos listas de documentos
     *
     * @param listA      lista de documentos A
     * @param listB      lista de documentos A
     * @param normalizer normalizador
     * @param combiner   combinador
     *
     * @return lista de documentos fusionados
     */
    public static List<DocSuggest> docsFusion(List<DocSuggest> listA, List<DocSuggest> listB, int normalizer, int combiner) {
        long index = 1;
        // sort list A based on the values
//        Collections.sort(listA, DESCENDING_ORDER);
        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
        for (DocSuggest item : listA) {
            IFResource resource = new DocResource(item, index);
            index++;
            resourcesList.put(new Long(item.getPath().hashCode()), resource);
        }
        IFSearchResults srA = new GenericSearchResults(resourcesList);


        // sort list B based on the values
        index = 1;

        resourcesList = new HashMap<Long, IFResource>();
        for (DocSuggest item : listB) {
            IFResource resource = new DocResource(item, index);
            index++;
            resourcesList.put(new Long(item.getPath().hashCode()), resource);
        }
        IFSearchResults srB = new GenericSearchResults(resourcesList);

        // Aggregated lists
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();
        srList.add(srA);
        srList.add(srB);
        IFNormalizer norm = null;

        switch (normalizer) {

            case STANDAR_NORMALIZE:
                norm = new StdNormalizer();
                break;
            case ZMUV_NORMALIZE:
                norm = new ZMUVNormalizer();
                break;
            case RANKSIM_NORMALIZE:
                norm = new RankSimNormalizer();
                break;
            case DEFAULT_NORMALIZE:
                norm = new StdNormalizer();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized normalizer");
        }

        IFCombiner comb = null;

        switch (combiner) {
            case SUM_COMBINER:
                comb = new SumCombiner();
                break;
            case MIN_COMBINER:
                comb = new MinCombiner();
                break;
            case MNZ_COMBINER:
                comb = new MNZCombiner();
                break;
            case DEFAULT_COMBINER:
                comb = new SumCombiner();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized combiner");
        }


        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        List<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        List<DocSuggest> aggregatedResults = new ArrayList<DocSuggest>();
        DocResource temp = null;

        for (IFResource aggResult : aggResults) {
            temp = (DocResource) aggResult;
            aggregatedResults.add(temp.getDocSuggest());
        }
        return aggregatedResults;
    }

}
