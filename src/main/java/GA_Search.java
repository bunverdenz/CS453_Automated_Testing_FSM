import java.io.IOException;
import java.util.*;

/*
    Population: {{'workBtn','e'},{'learnBtn','dud'},{'id','b'}}
    CreateRandomPop -> randomly swap the currentPop
    create numPopulation size of random pop
 */
public class GA_Search {
    public static ArrayList<ArrayList<ArrayList<String>>> allPaths = new ArrayList<ArrayList<ArrayList<String>>>();
    public static ArrayList<ArrayList<String>> allEdges = new ArrayList<>();

    public static int maxLengthPath = -1;

    public static ArrayList<ArrayList<String>> createRandomChromosome(){
        // length of chromosome == max length of path from all possible paths
        int totalSize = GA_Search.maxLengthPath;
        //deep copy
        ArrayList<ArrayList<String>> allEdgesList = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> edgesid = new ArrayList<>();
        allEdgesList.add(titles);
        allEdgesList.add(edgesid);
        int i;
        for(i=0; i<GA_Search.allEdges.get(0).size();i++){
            allEdgesList.get(0).add(GA_Search.allEdges.get(0).get(i));
            allEdgesList.get(1).add(GA_Search.allEdges.get(1).get(i));
        }
        //finish deep copy
        //ArrayList<ArrayList<String>> allEdgesList = (ArrayList<ArrayList<String>>) GA_Search.allEdges.clone();
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        ArrayList<String> titles1 = new ArrayList<>();
        ArrayList<String> edgesid1 = new ArrayList<>();
        result.add(titles1);
        result.add(edgesid1);

        for (i=0;i<totalSize;i++){
            int randIndex = new Random().nextInt(allEdgesList.get(0).size());

            result.get(0).add(allEdgesList.get(0).get(randIndex));
            result.get(1).add(allEdgesList.get(1).get(randIndex));
            allEdgesList.get(0).remove(randIndex);
            allEdgesList.get(1).remove(randIndex);
        }

        return result;
        //return new String[]{currentPopulation.get(randIndex)[0], currentPopulation.get(randIndex)[1]};

    }

    public static double findFitness(ArrayList<ArrayList<String>> chromosome) throws IOException {

        /* find fault, test coverage
            hierarchy :
            we should be able to follow the events (hierarchy), we can't click page 2 first be4 clicking page 1 which
            goes to page 2
            find fault by: search for bad list of events, i.e
            test coverage is : node 1 -> node 2 by edge 1. Does edge 1 actually bring us from node 1 -> node 2?
            we extract fsm by ourselves, we say this fsm is correct for sure
        ----------------------------------------------------------------------
            FITNESS = #of paths cover in this sequence of edges / # total paths
        */
        double countPathTouch = 0.0;
        double fitness = 0.0;
        int allPathSize = GA_Search.allPaths.size();
        int i;
        for (i=0; i<allPathSize;i++){
            int indexPop = 0, indexPath = 0;
            while (indexPop < chromosome.get(0).size() && indexPath < GA_Search.allPaths.get(i).get(0).size()){
                // check same edge or not, then check next edge (2 pointers)
                boolean chkTitle = chromosome.get(0).get(indexPop).equals(GA_Search.allPaths.get(i).get(0).get(indexPath));
                boolean chkEdge = chromosome.get(1).get(indexPop).equals(GA_Search.allPaths.get(i).get(1).get(indexPath));
                if (chkTitle && chkEdge){
                    // if same, move both
                    indexPop++;
                    indexPath++;
                }
                else {
                    // if not same, move just Population (to check this path exist or not)
                    indexPop++;
                }
            }
            boolean pathExist = indexPath == GA_Search.allPaths.get(i).get(0).size();
            if (pathExist){
                countPathTouch++;
            }
        }

        fitness = countPathTouch / allPathSize;
        return fitness;


    }
    public static void recur(Node node, ArrayList<ArrayList<String>> edges){
        /*
            I discard the case 1,8,5,3,8,... (loop, no same node in any path cuz too hard to handle all cases)
            I use edge id instead of node id
            edges = [['title1','title2',...],['edge1','edge2',...]] means 'edge1' from 'title1' is one id of one edge
            NO SAME EDGE IN ONE PATH
         */
        int i = 0;
        boolean checkNoRecur = true;
        //deep copy
        ArrayList<ArrayList<String>> outEdge = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> edgesid = new ArrayList<>();
        outEdge.add(titles);
        outEdge.add(edgesid);
        for(i=0; i<edges.get(0).size();i++){
            outEdge.get(0).add(edges.get(0).get(i));
            outEdge.get(1).add(edges.get(1).get(i));
        }
        //finish deep copy
        i = 0;
        for (Node n: node.out){
            ArrayList<ArrayList> edgesouts = node.edges;
            ArrayList<String[]> edgesout = edgesouts.get(i);
            boolean chkSameEdge = false;
            int j = 0;
            for (String title: edges.get(0)){
                chkSameEdge = title.equals(n.getTitle()) && edges.get(1).get(j).equals(edgesout.get(0)[0]);
                if(chkSameEdge){
                    break;
                }
                j++;
            }

            if(!chkSameEdge){
                //deep copy
                ArrayList<ArrayList<String>> tempEdge = new ArrayList<>();
                ArrayList<String> titles1 = new ArrayList<>();
                ArrayList<String> edgesid1 = new ArrayList<>();
                tempEdge.add(titles1);
                tempEdge.add(edgesid1);
                int k = 0;
                for(k=0; k<outEdge.get(0).size();k++){
                    tempEdge.get(0).add(edges.get(0).get(k));
                    tempEdge.get(1).add(edges.get(1).get(k));
                }
                //finish deep copy
                if (!edgesout.get(0)[1].equals("dud")) {
                    tempEdge.get(0).add(n.getTitle());
                    tempEdge.get(1).add(edgesout.get(0)[0]);
                    ArrayList<String> oneEdge = new ArrayList<>();
//                    oneEdge.add(n.getTitle());
//                    oneEdge.add(edgesout.get(0)[0]);
                    boolean chkSameEdgeInAllEdges = false;
                    int m = 0;
                    for (String title: GA_Search.allEdges.get(0)){
                        chkSameEdgeInAllEdges = title.equals(n.getTitle()) && GA_Search.allEdges.get(1).get(m).equals(edgesout.get(0)[0]);
                        if(chkSameEdgeInAllEdges){
                            break;
                        }
                        m++;
                    }
                    if(!chkSameEdgeInAllEdges){
                        GA_Search.allEdges.get(0).add(n.getTitle());
                        GA_Search.allEdges.get(1).add(edgesout.get(0)[0]);
                    }
                    checkNoRecur = false;
                    recur(n,tempEdge);
                }

            }

            i++;
        }

        if (checkNoRecur){
            if (GA_Search.maxLengthPath < edges.get(0).size()){
                GA_Search.maxLengthPath = edges.get(0).size();
            }
            GA_Search.allPaths.add(outEdge);
        }

    }
    public static ArrayList<ArrayList<ArrayList<String>>> ga_search(Node home) throws IOException {

        /*
            I use Node.title as each node id
         */

        double max = -1;
        //ArrayList<String> newedges = new ArrayList<String>();
        ArrayList<ArrayList<String>> Edges = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> edgesid = new ArrayList<>();
        Edges.add(titles);
        Edges.add(edgesid);
        ArrayList<ArrayList<String>> Edges1 = new ArrayList<>();
        ArrayList<String> titles0 = new ArrayList<>();
        ArrayList<String> edgesid0 = new ArrayList<>();
        GA_Search.allEdges.add(titles0);
        GA_Search.allEdges.add(edgesid0);
        recur(home, Edges);
        // NOW all paths will be in GA_Search.allPaths
        int numPopulation = GA_Search.allPaths.size() + 200;
        int i, iter;
        Random randomGenerator = new Random();
        ArrayList<ArrayList<ArrayList<String>>> currentPopulation = new ArrayList<>();
//        currentPopulation.add(listAllEdges);
        for(iter = 0; iter < 2; iter++) {
            System.out.println("ITERATION" + iter);
            System.out.println("-------------------------------------------------------------------------");

            // Create Population, each pop is list of edges (path)
            // edges = [['title1','title2',...],['edge1','edge2',...]] means 'edge1' from 'title1' is one id of one edge
            while (currentPopulation.size() < numPopulation) {
                currentPopulation.add(GA_Search.createRandomChromosome());
            }
            System.out.println("BEST_FITNESS" + iter);
            System.out.println("-------------------------------------------------------------------------");
            // Elitism find best fitness
            max = -100000000;
            double fitness = -1, index = 0;
            ArrayList<ArrayList<String>> bestChromosome = new ArrayList<>();
            for(ArrayList<ArrayList<String>> chromosome: currentPopulation){
                fitness = GA_Search.findFitness(chromosome);
                if(max < fitness){
                    max = fitness;
                    bestChromosome = chromosome;
                }
                index++;
            }
            ArrayList<ArrayList<ArrayList<String>>> tournamentSelection = new ArrayList<>();
            // Add 1 Elite
            tournamentSelection.add(bestChromosome);
            System.out.println("TOURNAMENT SELECTION" + iter);
            System.out.println("-------------------------------------------------------------------------");
            // tournament selection with best rank from each 5 population -> 19 population we selected + 1 elite
            int j;
            Collections.shuffle(currentPopulation);
            
            for(i=0; i<numPopulation; i+=5){
                max = -100000000;

                for(j=0;j<5 && i+j < numPopulation;j++){

                    fitness = GA_Search.findFitness(currentPopulation.get(i+j));
                    if(max < fitness){
                        max = fitness;
                        bestChromosome = currentPopulation.get(i+j);
                    }
                }
                tournamentSelection.add(bestChromosome);
            }
            ArrayList<ArrayList<ArrayList<String>>> newGen = tournamentSelection;

            // Cross Over

            double CrossOverChance = 0.7;
            System.out.println("CROSSOVER" + iter);
            System.out.println("-------------------------------------------------------------------------");
            for(i=0; i+1<tournamentSelection.size(); i+=2){
                // Crossover Chance
                double randDouble = Math.random();
                if (randDouble > CrossOverChance) continue;
                ArrayList<ArrayList<String>> parent1 = tournamentSelection.get(i);
                ArrayList<ArrayList<String>> parent2 = tournamentSelection.get(i+1);
                // all parents have same length
                int Size = parent1.get(0).size();

                int randomNum = randomGenerator.nextInt(Size);
                ArrayList<ArrayList<String>> offspring = new ArrayList<>();
                ArrayList<String> OffspringTitles = new ArrayList<>();
                ArrayList<String> OffspringEdgesid = new ArrayList<>();
                for(j=0; j< randomNum; j++){
                    OffspringTitles.add(parent1.get(0).get(j));
                    OffspringEdgesid.add(parent1.get(1).get(j));
                }
                for(j=randomNum; j< Size; j++){
                    OffspringTitles.add(parent2.get(0).get(j));
                    OffspringEdgesid.add(parent2.get(1).get(j));
                }
                offspring.add(OffspringTitles);
                offspring.add(OffspringEdgesid);
                newGen.add(offspring);
            }
            //Mutation
            System.out.println("MUTATION" + iter);
            System.out.println("-------------------------------------------------------------------------");
            //deep copy
            ArrayList<ArrayList<String>> allEdgesList = new ArrayList<>();
            ArrayList<String> titles2 = new ArrayList<>();
            ArrayList<String> edgesid2 = new ArrayList<>();
            allEdgesList.add(titles2);
            allEdgesList.add(edgesid2);

            for(i=0; i<GA_Search.allEdges.get(0).size();i++){
                allEdgesList.get(0).add(GA_Search.allEdges.get(0).get(i));
                allEdgesList.get(1).add(GA_Search.allEdges.get(1).get(i));
            }
            //finish deep copy
            double MutationChance = 0.02;
            for(i=0; i+1<tournamentSelection.size(); i++){
                // Mutation rate
                double randDouble = Math.random();
                if (randDouble > MutationChance) continue;
                ArrayList<ArrayList<String>> parent = tournamentSelection.get(i);

                int randomNum1 = randomGenerator.nextInt(allEdgesList.get(0).size());
                int chkTitle = parent.get(0).indexOf(allEdgesList.get(0).get(randomNum1));
                int chkEdgeId = parent.get(1).indexOf(allEdgesList.get(1).get(randomNum1));
                // Parent already have that edge, we won't mutate into it in our parent
                while(chkTitle==chkEdgeId){
                    randomNum1 = randomGenerator.nextInt(allEdgesList.get(0).size());
                    chkTitle = parent.get(0).indexOf(allEdgesList.get(0).get(randomNum1));
                    chkEdgeId = parent.get(1).indexOf(allEdgesList.get(1).get(randomNum1));
                }
                int randomNum2 = randomGenerator.nextInt(parent.get(0).size());
                ArrayList<ArrayList<String>> offspring = (ArrayList<ArrayList<String>>) parent.clone();
                offspring.get(0).set(randomNum2, allEdgesList.get(0).get(randomNum1));
                offspring.get(1).set(randomNum2, allEdgesList.get(1).get(randomNum1));
//                offspring.set(randomNum1, parent2.get(randomNum2));
                newGen.add(offspring);
            }
            currentPopulation = newGen;
        }
        return currentPopulation;
    }
    public static double coverage_percentage(ArrayList<ArrayList<ArrayList<String>>> currentPopulation){

        double out = 0.0;
        double countPathTouch = 0.0;
        ArrayList<ArrayList<ArrayList<String>>> storePathFound = new ArrayList<>();
        for(ArrayList<ArrayList<String>> chromosome: currentPopulation){

            int allPathSize = GA_Search.allPaths.size();
            int i;
            for (i=0; i<allPathSize;i++){
                int indexPop = 0, indexPath = 0;
                while (indexPop < chromosome.get(0).size() && indexPath < GA_Search.allPaths.get(i).get(0).size()){
                    // check same edge or not, then check next edge (2 pointers)

                    boolean chkTitle = chromosome.get(0).get(indexPop).equals(GA_Search.allPaths.get(i).get(0).get(indexPath));
                    boolean chkEdge = chromosome.get(1).get(indexPop).equals(GA_Search.allPaths.get(i).get(1).get(indexPath));
                    if (chkTitle && chkEdge){
                        // if same, move both
                        indexPop++;
                        indexPath++;
                    }
                    else {
                        // if not same, move just Population (to check this path exist or not)
                        indexPop++;
                    }
                }
                boolean pathExist = indexPath == GA_Search.allPaths.get(i).get(0).size();
                if (pathExist){

                    //CHECK if GA_Search.allPaths.get(i) is already stored TO GET coverage percentage
                    boolean chkNotFound = true;
                    int countNotFound = 0;
                    for(ArrayList<ArrayList<String>> path: storePathFound){
                        int length = path.get(0).size();
                        int i1;

                        for(i1=0;i1<length;i1++){
                            boolean chkTitle = path.get(0).get(i1).equals(GA_Search.allPaths.get(i).get(0).get(i1));
                            boolean chkEdge = path.get(1).get(i1).equals(GA_Search.allPaths.get(i).get(1).get(i1));
                            if(!(chkTitle && chkEdge)){
                                countNotFound++;
                                break;
                            }
                        }

                    }
                    if (countNotFound != storePathFound.size()){
                        chkNotFound = false;
                    }
                    if(chkNotFound){
                        countPathTouch++;
                        //store the path we found
                        storePathFound.add(GA_Search.allPaths.get(i));
                    }
                }
            }
        }
        out = countPathTouch/GA_Search.allPaths.size();
        return out;
    }
}