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
    public static void main(String[] args) throws IOException, InterruptedException {
//        Team6 team6 = new Team6();
        // run this somehow
        //Team6.main();
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("DONE FSM");
        ArrayList<String[]> allEdges = new ArrayList<String[]>();
        for(Node node: Team6.fsm){

            //allEdges.addAll(node.edges);

        }
//        List<ArrayList<String[]>> result = ga_search(allEdges, Team6.fsm);
//        System.out.println(result);
    }

    public static ArrayList<ArrayList<String>> createRandomChromosome(){
        // length of chromosome == max length of path from all possible paths
        int totalSize = GA_Search.maxLengthPath;
        ArrayList<ArrayList<String>> allEdgesList = (ArrayList<ArrayList<String>>) GA_Search.allEdges.clone();
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> edgesid = new ArrayList<>();
        result.add(titles);
        result.add(edgesid);
        int i;
        for (i=0;i<totalSize;i++){
            int randIndex = new Random().nextInt(allEdgesList.size());

            result.get(0).add(allEdgesList.get(randIndex).get(0));
            result.get(1).add(allEdgesList.get(randIndex).get(1));
            allEdgesList.remove(randIndex);
        }

        return result;
        //return new String[]{currentPopulation.get(randIndex)[0], currentPopulation.get(randIndex)[1]};

    }

    public static int findFitness(ArrayList<ArrayList<String>> chromosome) throws IOException {

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
        int countPathTouch = 0;
        int fitness = 0;
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
//        int countNodeTouch = 1, countEdgeTouch = 0;
//        WebClient webClient = new WebClient((BrowserVersion.CHROME));
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.getOptions().setJavaScriptEnabled(true);
////        java.net.URL url = file.toURI().toURL();
//        HtmlPage page = webClient.getPage("https://melodize.github.io/");
//        HashSet<String> storeEdgeId = new HashSet<>();
//        HashSet<String> storeNodeTitle = new HashSet<>();
//        for (String[] edge: Population){
////            .click.
//            if(page.getElementById(edge[0]) != null) {
//                HtmlPage pageResult = page.getElementById(edge[0]).click();
//                System.out.println("CLICK SUCCESS");
//                System.out.println(pageResult);
//                String title = pageResult.getTitleText();
//                if (!title.equals(page.getTitleText())) {
//                    if(!storeEdgeId.contains(edge[0])){
//                        countEdgeTouch++;
//                        storeEdgeId.add(edge[0]);
//                    }
//                    if(!storeNodeTitle.contains(title)){
//                        countNodeTouch++;
//                        storeNodeTitle.add(title);
//                    }
//
//                }
//                page = pageResult;
//            }
//        }

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
                ArrayList<ArrayList<String>> tempEdge = new ArrayList<>();
                tempEdge = edges;
                tempEdge.get(0).add(n.getTitle());
                tempEdge.get(1).add(edgesout.get(0)[0]);
                checkNoRecur = false;
                ArrayList<String> oneEdge = new ArrayList<>();
                //TODO: using Hashset might not distinguish different edge
                oneEdge.add(n.getTitle());
                oneEdge.add(edgesout.get(0)[0]);
                GA_Search.allEdges.add(oneEdge);
                recur(n,tempEdge);
            }

            i++;
        }

        if (checkNoRecur){
            if (GA_Search.maxLengthPath < edges.get(0).size()){
                GA_Search.maxLengthPath = edges.get(0).size();
            }
            GA_Search.allPaths.add(edges);
        }

    }
    public static ArrayList<ArrayList<ArrayList<String>>> ga_search(Node home) throws IOException {

        /*
            I use Node.title as each node id
         */

        int max = -1;
        //ArrayList<String> newedges = new ArrayList<String>();
        ArrayList<ArrayList<String>> edges = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> edgesid = new ArrayList<>();
        edges.add(titles);
        edges.add(edgesid);
        recur(home, edges);
        // NOW all paths will be in GA_Search.allPaths
        //TODO: change numPop to adjust for GA_Search.allPaths.size() (the more size the more numPop)
        // I use #of all possible paths * 10 ??
        int numPopulation = GA_Search.allPaths.size() * 10;
        int i, iter;
        Random randomGenerator = new Random();
        ArrayList<ArrayList<ArrayList<String>>> currentPopulation = new ArrayList<>();
//        currentPopulation.add(listAllEdges);
        for(iter = 0; iter < 100; iter++) {
            System.out.println("ITERATION" + iter);
            System.out.println("-------------------------------------------------------------------------");

            // Create Population, each pop is list of edges (path)
            // edges = [['title1','title2',...],['edge1','edge2',...]] means 'edge1' from 'title1' is one id of one edge
            while (currentPopulation.size() < numPopulation) {
                currentPopulation.add(GA_Search.createRandomChromosome());
            }
            // Elitism find best fitness
            max = -100000000;
            int fitness = -1, index = 0;
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
            //TODO: tournament selection subtract by num all possible path ?
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
            ArrayList<ArrayList<String>> allEdgesList = new ArrayList <> (GA_Search.allEdges);
            double MutationChance = 0.02;
            for(i=0; i+1<tournamentSelection.size(); i++){
                // Mutation rate
                double randDouble = Math.random();
                if (randDouble > MutationChance) continue;
                ArrayList<ArrayList<String>> parent = tournamentSelection.get(i);

                int randomNum1 = randomGenerator.nextInt(allEdgesList.size());
                int chkTitle = parent.get(0).indexOf(allEdgesList.get(randomNum1).get(0));
                int chkEdgeId = parent.get(1).indexOf(allEdgesList.get(randomNum1).get(1));
                // Parent already have that edge, we won't mutate into it in our parent
                while(chkTitle==chkEdgeId){
                    randomNum1 = randomGenerator.nextInt(allEdgesList.size());
                    chkTitle = parent.get(0).indexOf(allEdgesList.get(randomNum1).get(0));
                    chkEdgeId = parent.get(1).indexOf(allEdgesList.get(randomNum1).get(1));
                }
                int randomNum2 = randomGenerator.nextInt(parent.get(0).size());
                ArrayList<ArrayList<String>> offspring = (ArrayList<ArrayList<String>>) parent.clone();
                offspring.get(0).set(randomNum2, allEdgesList.get(randomNum1).get(0));
                offspring.get(1).set(randomNum2, allEdgesList.get(randomNum1).get(1));
//                offspring.set(randomNum1, parent2.get(randomNum2));
                newGen.add(offspring);
            }
            currentPopulation = newGen;
        }
        return currentPopulation;
    }
}