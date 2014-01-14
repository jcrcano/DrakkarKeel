package test;



import drakkar.oar.DocumentMetaData;
import drakkar.mast.retrieval.improves.MetaResource;
import es.uam.eps.nets.rankfusion.GenericRankAggregator;
import es.uam.eps.nets.rankfusion.GenericResource;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class Test {

//    public static TreeMap<String, Double> normalize(Map<String, Double> list, String normalizer) {
//        long index = 1;
//        // sort the list based on the values
//        HashMap<Double, ArrayList<String>> mapValues = new HashMap<Double, ArrayList<String>>();
//        for (Entry<String, Double> e : list.entrySet()) {
//            ArrayList<String> currentList = mapValues.get(e.getValue());
//            if (currentList == null) {
//                currentList = new ArrayList<String>();
//            }
//            currentList.add(e.getKey());
//            mapValues.put(e.getValue(), currentList);
//        }
//        SortedMap<Double, ArrayList<String>> sortedMap = new TreeMap<Double, ArrayList<String>>(mapValues).descendingMap();
//        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
//        for (Entry<Double, ArrayList<String>> e : sortedMap.entrySet()) {
//            Double score = e.getKey();
//            for (String user : e.getValue()) {
//                IFResource resource = new GenericResource(user, score, index);
//                index++;
//                resourcesList.put(new Long(user.hashCode()), resource);
//            }
//        }
//        IFSearchResults sr = new GenericSearchResults(resourcesList);
//        IFNormalizer norm = null;
//        if (normalizer.compareTo("stdnorm") == 0) {
//            norm = new StdNormalizer();
//        } else if (normalizer.compareTo("zmuvnorm") == 0) {
//            norm = new ZMUVNormalizer();
//        } else if (normalizer.compareTo("ranksimnorm") == 0) {
//            norm = new RankSimNormalizer();
//        } else if (normalizer.compareTo("defaultnorm") == 0) {
//            // Default normalizer
//            norm = new StdNormalizer();
//        } else {
//            // Unrecognized normalizer
//            norm = null;
//        }
//        norm.normalize(sr);
//        TreeMap<String, Double> normalizedList = new TreeMap<String, Double>();
//        for (IFResource aggResult : sr.getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE)) {
//            normalizedList.put(aggResult.getId(), aggResult.getNormalizedValue());
//        }
//        return normalizedList;
//    }

    public static TreeMap<String, Double> aggregate(Map<String, Double> listA, Map<String, Double> listB, String normalizer, String combiner) {
        long index = 1;
        // sort list A based on the values
        HashMap<Double, ArrayList<String>> mapValues = new HashMap<Double, ArrayList<String>>();
        for (Entry<String, Double> e : listA.entrySet()) {
            ArrayList<String> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<String>();
            }
            currentList.add(e.getKey());
            mapValues.put(e.getValue(), currentList);
        }

        System.out.println(" list A --1 "+mapValues);
        SortedMap<Double, ArrayList<String>> sortedMap = new TreeMap<Double, ArrayList<String>>(mapValues).descendingMap();
        System.out.println(" list A --2 "+sortedMap);
        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<String>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            System.out.println("score "+e.getKey());
            for (String user : e.getValue()) {
                 System.out.println("value "+e.getValue());
                IFResource resource = new GenericResource(user, score, index);
                index++;
                resourcesList.put(new Long(user.hashCode()), resource);
            }
        }

        System.out.println("IFResource "+ resourcesList.keySet());
        System.out.println("IFResource "+ resourcesList.values());
        IFSearchResults srA = new GenericSearchResults(resourcesList);


        // sort list B based on the values
        index = 1;
        mapValues = new HashMap<Double, ArrayList<String>>();
        for (Entry<String, Double> e : listB.entrySet()) {
            ArrayList<String> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<String>();
            }
            currentList.add(e.getKey());
             System.out.println("e.getValue() "+e.getValue()+ " / " +currentList);
            mapValues.put(e.getValue(), currentList);
        }
        sortedMap = new TreeMap<Double, ArrayList<String>>(mapValues).descendingMap();
        System.out.println("sortedMap "+sortedMap);
        resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<String>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            for (String user : e.getValue()) {
                System.out.println("user "+user+" score "+score+" index "+index);
                IFResource resource = new GenericResource(user, score, index);
                index++;
                resourcesList.put(new Long(user.hashCode()), resource);
            }
        }
        IFSearchResults srB = new GenericSearchResults(resourcesList);
        // Aggregated lists
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();
        srList.add(srA);
        srList.add(srB);
        IFNormalizer norm = null;
        if (normalizer.compareTo("stdnorm") == 0) {
            norm = new StdNormalizer();
        } else if (normalizer.compareTo("zmuvnorm") == 0) {
            norm = new ZMUVNormalizer();
        } else if (normalizer.compareTo("ranksimnorm") == 0) {
            norm = new RankSimNormalizer();
        } else if (normalizer.compareTo("defaultnorm") == 0) {
            // Default normalizer
            norm = new StdNormalizer();
        } else {
            // Unrecognized normalizer
            norm = null;
        }
        IFCombiner comb = null;
        if (combiner.compareTo("sumcomb") == 0) {
            comb = new SumCombiner();
        } else if (combiner.compareTo("mincomb") == 0) {
            comb = new MinCombiner();
        } else if (combiner.compareTo("mnzcomb") == 0) {
            comb = new MNZCombiner();
        } else if (combiner.compareTo("defaultcomb") == 0) {
            // Default combiner
            comb = new SumCombiner();
        } else {
            // Unrecognized combiner
            comb = null;
        }
        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        ArrayList<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        TreeMap<String, Double> aggregatedResults = new TreeMap<String, Double>();
        for (IFResource aggResult : aggResults ) {
        	System.out.println("---- "+aggResult.getId()+" ----" +aggResult.getCombinedValue());
        	aggregatedResults.put(aggResult.getId(), aggResult.getCombinedValue());
//                System.out.println("list-- "+aggregatedResults);
        }
        return aggregatedResults;
    }

     public static TreeMap<DocumentMetaData, Double> aggregateMD(Map<DocumentMetaData, Double> listA, Map<DocumentMetaData, Double> listB, String normalizer, String combiner) {
        long index = 1;
        // sort list A based on the values
        HashMap<Double, ArrayList<DocumentMetaData>> mapValues = new HashMap<Double, ArrayList<DocumentMetaData>>();
        for (Entry<DocumentMetaData, Double> e : listA.entrySet()) {
            ArrayList<DocumentMetaData> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<DocumentMetaData>();
            }
            currentList.add(e.getKey());
            mapValues.put(e.getValue(), currentList);
        }

        System.out.println(" list A --1 "+mapValues);
        SortedMap<Double, ArrayList<DocumentMetaData>> sortedMap = new TreeMap<Double, ArrayList<DocumentMetaData>>(mapValues).descendingMap();
        System.out.println(" list A --2 "+sortedMap);
        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<DocumentMetaData>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            System.out.println("score "+e.getKey());
            for (DocumentMetaData user : e.getValue()) {

                 System.out.println("value "+e.getValue());
                IFResource resource = new MetaResource(user,index);
                index++;
                resourcesList.put(new Long(user.getPath().hashCode()), resource);
            }
        }

        System.out.println("IFResource "+ resourcesList.keySet());
        System.out.println("IFResource "+ resourcesList.values());
        IFSearchResults srA = new GenericSearchResults(resourcesList);


        // sort list B based on the values
        index = 1;
        mapValues = new HashMap<Double, ArrayList<DocumentMetaData>>();
        for (Entry<DocumentMetaData, Double> e : listB.entrySet()) {
            ArrayList<DocumentMetaData> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<DocumentMetaData>();
            }
            currentList.add(e.getKey());
             System.out.println("e.getValue() "+e.getValue()+ " / " +currentList);
            mapValues.put(e.getValue(), currentList);
        }
        sortedMap = new TreeMap<Double, ArrayList<DocumentMetaData>>(mapValues).descendingMap();
        System.out.println("sortedMap "+sortedMap);
        resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<DocumentMetaData>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            for (DocumentMetaData user : e.getValue()) {
                System.out.println("user-p "+user.getPath()+" score "+score+" index "+index);
                IFResource resource = new MetaResource(user,index);
                index++;
                resourcesList.put(new Long(user.getPath().hashCode()), resource);
            }
        }
        IFSearchResults srB = new GenericSearchResults(resourcesList);
        // Aggregated lists
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();
        srList.add(srA);
        srList.add(srB);
        IFNormalizer norm = null;
        if (normalizer.compareTo("stdnorm") == 0) {
            norm = new StdNormalizer();
        } else if (normalizer.compareTo("zmuvnorm") == 0) {
            norm = new ZMUVNormalizer();
        } else if (normalizer.compareTo("ranksimnorm") == 0) {
            norm = new RankSimNormalizer();
        } else if (normalizer.compareTo("defaultnorm") == 0) {
            // Default normalizer
            norm = new StdNormalizer();
        } else {
            // Unrecognized normalizer
            norm = null;
        }
        IFCombiner comb = null;
        if (combiner.compareTo("sumcomb") == 0) {
            comb = new SumCombiner();
        } else if (combiner.compareTo("mincomb") == 0) {
            comb = new MinCombiner();
        } else if (combiner.compareTo("mnzcomb") == 0) {
            comb = new MNZCombiner();
        } else if (combiner.compareTo("defaultcomb") == 0) {
            // Default combiner
            comb = new SumCombiner();
        } else {
            // Unrecognized combiner
            comb = null;
        }
        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        ArrayList<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        TreeMap<DocumentMetaData, Double> aggregatedResults = new TreeMap<DocumentMetaData, Double>();
        MetaResource meta ;
        for (IFResource aggResult : aggResults ) {
                meta = (MetaResource) aggResult;
        	System.out.println("---- "+meta.getMetaDocument().getName()+" ----" +meta.getCombinedValue());
        	DocumentMetaData e =meta.getMetaDocument();
                double score = aggResult.getCombinedValue();
                e.setScore(score);
                aggregatedResults.put(e,score );
//                System.out.println("list-- "+aggregatedResults);
        }
        return aggregatedResults;
    }

      public static List<DocumentMetaData> aggregateMD1(Map<DocumentMetaData, Double> listA, Map<DocumentMetaData, Double> listB, String normalizer, String combiner) {
        long index = 1;
        // sort list A based on the values
        HashMap<Double, ArrayList<DocumentMetaData>> mapValues = new HashMap<Double, ArrayList<DocumentMetaData>>();
        for (Entry<DocumentMetaData, Double> e : listA.entrySet()) {
            ArrayList<DocumentMetaData> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<DocumentMetaData>();
            }
            currentList.add(e.getKey());
            mapValues.put(e.getValue(), currentList);
        }

        System.out.println(" list A --1 "+mapValues);
        SortedMap<Double, ArrayList<DocumentMetaData>> sortedMap = new TreeMap<Double, ArrayList<DocumentMetaData>>(mapValues).descendingMap();
        System.out.println(" list A --2 "+sortedMap);
        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<DocumentMetaData>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            System.out.println("score "+e.getKey());
            for (DocumentMetaData user : e.getValue()) {
                 System.out.println("value "+e.getValue());
                IFResource resource = new MetaResource(user, index);
                index++;
                resourcesList.put(new Long(user.getPath().hashCode()), resource);
            }
        }

        System.out.println("IFResource "+ resourcesList.keySet());
        System.out.println("IFResource "+ resourcesList.values());
        IFSearchResults srA = new GenericSearchResults(resourcesList);


        // sort list B based on the values
        index = 1;
        mapValues = new HashMap<Double, ArrayList<DocumentMetaData>>();
        for (Entry<DocumentMetaData, Double> e : listB.entrySet()) {
            ArrayList<DocumentMetaData> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<DocumentMetaData>();
            }
            currentList.add(e.getKey());
             System.out.println("e.getValue() "+e.getValue()+ " / " +currentList);
            mapValues.put(e.getValue(), currentList);
        }
        sortedMap = new TreeMap<Double, ArrayList<DocumentMetaData>>(mapValues).descendingMap();
        System.out.println("sortedMap "+sortedMap);
        resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<DocumentMetaData>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            for (DocumentMetaData user : e.getValue()) {
                System.out.println("user-p "+user.getPath()+" score "+score+" index "+index);
                IFResource resource = new MetaResource(user, index);
                index++;
                resourcesList.put(new Long(user.getPath().hashCode()), resource);
            }
        }
        IFSearchResults srB = new GenericSearchResults(resourcesList);
        // Aggregated lists
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();
        srList.add(srA);
        srList.add(srB);
        IFNormalizer norm = null;
        if (normalizer.compareTo("stdnorm") == 0) {
            norm = new StdNormalizer();
        } else if (normalizer.compareTo("zmuvnorm") == 0) {
            norm = new ZMUVNormalizer();
        } else if (normalizer.compareTo("ranksimnorm") == 0) {
            norm = new RankSimNormalizer();
        } else if (normalizer.compareTo("defaultnorm") == 0) {
            // Default normalizer
            norm = new StdNormalizer();
        } else {
            // Unrecognized normalizer
            norm = null;
        }
        IFCombiner comb = null;
        if (combiner.compareTo("sumcomb") == 0) {
            comb = new SumCombiner();
        } else if (combiner.compareTo("mincomb") == 0) {
            comb = new MinCombiner();
        } else if (combiner.compareTo("mnzcomb") == 0) {
            comb = new MNZCombiner();
        } else if (combiner.compareTo("defaultcomb") == 0) {
            // Default combiner
            comb = new SumCombiner();
        } else {
            // Unrecognized combiner
            comb = null;
        }
        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        ArrayList<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        List<DocumentMetaData> aggregatedResults = new ArrayList<DocumentMetaData>();
        MetaResource meta ;
        for (IFResource aggResult : aggResults ) {
                meta = (MetaResource) aggResult;
        	System.out.println("---- "+meta.getMetaDocument().getName()+" ----" +meta.getCombinedValue());
        	DocumentMetaData e =meta.getMetaDocument();
                double score = aggResult.getCombinedValue();
                e.setScore(score);
                aggregatedResults.add(e);
//                System.out.println("list-- "+aggregatedResults);
        }
        return aggregatedResults;
    }


       public static List<DocumentMetaData> aggregateMD2(Map<DocumentMetaData, Double> listA, Map<DocumentMetaData, Double> listB, String normalizer, String combiner) {
        long index = 1;
        // sort list A based on the values
        HashMap<Double, ArrayList<DocumentMetaData>> mapValues = new HashMap<Double, ArrayList<DocumentMetaData>>();
        for (Entry<DocumentMetaData, Double> e : listA.entrySet()) {
            ArrayList<DocumentMetaData> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<DocumentMetaData>();
            }
            currentList.add(e.getKey());
            mapValues.put(e.getValue(), currentList);
        }

        System.out.println(" list A --1 "+mapValues);
        SortedMap<Double, ArrayList<DocumentMetaData>> sortedMap = new TreeMap<Double, ArrayList<DocumentMetaData>>(mapValues).descendingMap();
        System.out.println(" list A --2 "+sortedMap);
        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<DocumentMetaData>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            System.out.println("score "+e.getKey());
            for (DocumentMetaData user : e.getValue()) {
                 System.out.println("value "+e.getValue());
                IFResource resource = new MetaResource(user, index);
                index++;
                resourcesList.put(new Long(user.getPath().hashCode()), resource);
            }
        }

        System.out.println("IFResource "+ resourcesList.keySet());
        System.out.println("IFResource "+ resourcesList.values());
        IFSearchResults srA = new GenericSearchResults(resourcesList);


        // sort list B based on the values
        index = 1;
        mapValues = new HashMap<Double, ArrayList<DocumentMetaData>>();
        for (Entry<DocumentMetaData, Double> e : listB.entrySet()) {
            ArrayList<DocumentMetaData> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<DocumentMetaData>();
            }
            currentList.add(e.getKey());
             System.out.println("e.getValue() "+e.getValue()+ " / " +currentList);
            mapValues.put(e.getValue(), currentList);
        }
        sortedMap = new TreeMap<Double, ArrayList<DocumentMetaData>>(mapValues).descendingMap();
        System.out.println("sortedMap "+sortedMap);
        resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<DocumentMetaData>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            for (DocumentMetaData user : e.getValue()) {
                System.out.println("user-p "+user.getPath()+" score "+score+" index "+index);
                IFResource resource = new MetaResource(user, index);
                index++;
                resourcesList.put(new Long(user.getPath().hashCode()), resource);
            }
        }
        IFSearchResults srB = new GenericSearchResults(resourcesList);
        // Aggregated lists
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();
        srList.add(srA);
        srList.add(srB);
        IFNormalizer norm = null;
        if (normalizer.compareTo("stdnorm") == 0) {
            norm = new StdNormalizer();
        } else if (normalizer.compareTo("zmuvnorm") == 0) {
            norm = new ZMUVNormalizer();
        } else if (normalizer.compareTo("ranksimnorm") == 0) {
            norm = new RankSimNormalizer();
        } else if (normalizer.compareTo("defaultnorm") == 0) {
            // Default normalizer
            norm = new StdNormalizer();
        } else {
            // Unrecognized normalizer
            norm = null;
        }
        IFCombiner comb = null;
        if (combiner.compareTo("sumcomb") == 0) {
            comb = new SumCombiner();
        } else if (combiner.compareTo("mincomb") == 0) {
            comb = new MinCombiner();
        } else if (combiner.compareTo("mnzcomb") == 0) {
            comb = new MNZCombiner();
        } else if (combiner.compareTo("defaultcomb") == 0) {
            // Default combiner
            comb = new SumCombiner();
        } else {
            // Unrecognized combiner
            comb = null;
        }
        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        ArrayList<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        List<DocumentMetaData> aggregatedResults = new ArrayList<DocumentMetaData>();
        MetaResource meta ;
        for (IFResource aggResult : aggResults ) {
                meta = (MetaResource) aggResult;
        	System.out.println("---- "+meta.getMetaDocument().getName()+" ----" +meta.getCombinedValue());
        	DocumentMetaData e =meta.getMetaDocument();
                double score = aggResult.getCombinedValue();
                e.setScore(score);
                aggregatedResults.add(e );
//                System.out.println("list-- "+aggregatedResults);
        }
        return aggregatedResults;
    }

    public static TreeMap<DocumentMetaData, Double> aggregate1(Map<DocumentMetaData, Double> listA, Map<DocumentMetaData, Double> listB, String normalizer, String combiner) {
        long index = 1;
        // sort list A based on the values
        HashMap<Double, ArrayList<DocumentMetaData>> mapValues = new HashMap<Double, ArrayList<DocumentMetaData>>();
        for (Entry<DocumentMetaData, Double> e : listA.entrySet()) {
            ArrayList<DocumentMetaData> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<DocumentMetaData>();
            }
            currentList.add(e.getKey());
            mapValues.put(e.getValue(), currentList);
        }

        System.out.println(" list A --1 " + mapValues);
        SortedMap<Double, ArrayList<DocumentMetaData>> sortedMap = new TreeMap<Double, ArrayList<DocumentMetaData>>(mapValues).descendingMap();
        System.out.println(" list A --2 " + sortedMap);
        HashMap<Long, IFResource> resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<DocumentMetaData>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            System.out.println("score " + e.getKey());
            for (DocumentMetaData user : e.getValue()) {
                System.out.println("value " + e.getValue());
                IFResource resource = new MetaResource(user,  index);
                index++;
                resourcesList.put(new Long(user.hashCode()), resource);
            }
        }

        System.out.println("IFResource " + resourcesList.keySet());
        IFSearchResults srA = new GenericSearchResults(resourcesList);


        // sort list B based on the values
        index = 1;
        mapValues = new HashMap<Double, ArrayList<DocumentMetaData>>();
        for (Entry<DocumentMetaData, Double> e : listB.entrySet()) {
            ArrayList<DocumentMetaData> currentList = mapValues.get(e.getValue());
            if (currentList == null) {
                currentList = new ArrayList<DocumentMetaData>();
            }
            currentList.add(e.getKey());
            System.out.println("e.getValue() " + e.getValue() + " / " + currentList);
            mapValues.put(e.getValue(), currentList);
        }
        sortedMap = new TreeMap<Double, ArrayList<DocumentMetaData>>(mapValues).descendingMap();
        System.out.println("sortedMap " + sortedMap);
        resourcesList = new HashMap<Long, IFResource>();
        for (Entry<Double, ArrayList<DocumentMetaData>> e : sortedMap.entrySet()) {
            Double score = e.getKey();
            for (DocumentMetaData user : e.getValue()) {
                System.out.println("user " + user + " score " + score + " index " + index);
                IFResource resource = new MetaResource(user,  index);
                index++;
                resourcesList.put(new Long(user.hashCode()), resource);
            }
        }
        IFSearchResults srB = new GenericSearchResults(resourcesList);
        // Aggregated lists
        ArrayList<IFSearchResults> srList = new ArrayList<IFSearchResults>();
        srList.add(srA);
        srList.add(srB);
        IFNormalizer norm = null;
        if (normalizer.compareTo("stdnorm") == 0) {
            norm = new StdNormalizer();
        } else if (normalizer.compareTo("zmuvnorm") == 0) {
            norm = new ZMUVNormalizer();
        } else if (normalizer.compareTo("ranksimnorm") == 0) {
            norm = new RankSimNormalizer();
        } else if (normalizer.compareTo("defaultnorm") == 0) {
            // Default normalizer
            norm = new StdNormalizer();
        } else {
            // Unrecognized normalizer
            norm = null;
        }
        IFCombiner comb = null;
        if (combiner.compareTo("sumcomb") == 0) {
            comb = new SumCombiner();
        } else if (combiner.compareTo("mincomb") == 0) {
            comb = new MinCombiner();
        } else if (combiner.compareTo("mnzcomb") == 0) {
            comb = new MNZCombiner();
        } else if (combiner.compareTo("defaultcomb") == 0) {
            // Default combiner
            comb = new SumCombiner();
        } else {
            // Unrecognized combiner
            comb = null;
        }
        IFRankAggregator ra = new GenericRankAggregator(norm, comb);
        // Get sorted aggregated list
        ArrayList<IFResource> aggResults = ra.aggregate(srList).getSortedResourceList(GenericSearchResults.I_RESOURCE_ORDER_COMBINED_VALUE);
        TreeMap<DocumentMetaData, Double> aggregatedResults = new TreeMap<DocumentMetaData, Double>();
        MetaResource meta ;
        for (IFResource aggResult : aggResults ) {
                meta = (MetaResource) aggResult;
        	System.out.println("---- "+meta.getMetaDocument().getName()+" ----" +meta.getCombinedValue());
        	DocumentMetaData e =meta.getMetaDocument();
                double score = aggResult.getCombinedValue();
                e.setScore(score);
                aggregatedResults.put(e,score );
//                System.out.println("list-- "+aggregatedResults);
        }
        return aggregatedResults;
    }

    public static void main(String[] args) {


     
        Map<String, Double> firstList = new HashMap<String, Double>();
        firstList.put("a", 1.00);
        firstList.put("b", 0.90);
        firstList.put("c", 0.80);
        firstList.put("d", 0.70);

//        firstList.put("a", 1.00);
//        firstList.put("b", 8.00);
//		firstList.put("c", 0.80);
//		firstList.put("d", 0.50);

        Map<String, Double> secondList = new HashMap<String, Double>();
        secondList.put("a", 0.25);
        secondList.put("b", 0.75);
        secondList.put("c", 0.50);
        secondList.put("d", 1.00);

//        secondList.put("a", 7.00);
//		secondList.put("b", 0.20);
//        secondList.put("c", 6.50);
//		secondList.put("d", 0.50);
//                secondList.put("e", 4.50);

                System.out.println("First list: " + firstList);
		System.out.println("Second list: " + secondList);
//		System.out.println("First normalized list (standard): " + normalize(firstList, "defaultnorm"));
//		System.out.println("First normalized list (zmuv): " + normalize(firstList, "zmuvnorm"));
//		System.out.println("First normalized list (ranksim): " + normalize(firstList, "ranksimnorm"));
//		System.out.println("Second normalized list (standard): " + normalize(secondList, "defaultnorm"));

                TreeMap<String, Double> d = aggregate(firstList, secondList, "defaultnorm", "defaultcomb");
//                Set<String>  set = d.keySet();
		System.out.println("Aggregated list0: " + d);

//                System.out.println("Set list0: " + set);
//

        DocumentMetaData s = new DocumentMetaData();
        s.setName("a");
        s.setPath("a");
        s.setScore(1.00);
        DocumentMetaData s1 = new DocumentMetaData();
        s1.setName("b");
        s1.setPath("b");
        s1.setScore(8.00);

        Map<DocumentMetaData, Double> firstList1 = new HashMap<DocumentMetaData, Double>();
        firstList1.put(s, 1.00);
        firstList1.put(s1, 8.00);

        Map<DocumentMetaData, Double> secondList1 = new HashMap<DocumentMetaData, Double>();

        DocumentMetaData ss = new DocumentMetaData();
        ss.setName("a");
        ss.setPath("a");
        ss.setScore(7.00);

        DocumentMetaData ss2 = new DocumentMetaData();
        ss2.setName("c");
        ss2.setPath("c");
        ss2.setScore(6.50);

       

        secondList1.put(ss,7.00);
        secondList1.put(ss2,6.50);
         

        List<DocumentMetaData> d1 = aggregateMD1(firstList1, secondList1, "defaultnorm", "defaultcomb");
//                Collection<DocumentMetaData>  set1 = d1.values());
		System.out.println("Aggregated list1: " + d1);

//                System.out.println("Set list1: " + set1);
    }
}
