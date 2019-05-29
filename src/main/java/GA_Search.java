import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
    Population: {{'workBtn','e'},{'learnBtn','dud'},{'id','b'}}
    CreateRandomPop -> randomly swap the currentPop
    create numPopulation size of random pop

 */
public class GA_Search {

    public static ArrayList<String[]> createRandomPop(ArrayList<String[]> currentPopulation){
        //int randIndex = new Random().nextInt(currentPopulation.size());
        Collections.shuffle(currentPopulation);
        return currentPopulation;
        //return new String[]{currentPopulation.get(randIndex)[0], currentPopulation.get(randIndex)[1]};

    }

    public static int findFitness(ArrayList<String[]> Population){
        int fitness = 0;
        
        return fitness;
    }
    public static void ga_search(ArrayList<String[]> listAllEdges){

        int numPopulation = 100;
        int i, iter;
        ArrayList<ArrayList<String[]>> currentPopulation = new ArrayList<>();
        currentPopulation.add(listAllEdges);
        for(iter = 0; iter < 10000; iter++) {
            int numPopToCreate = numPopulation - currentPopulation.size();
            // Create Population
            for (i = 0; i < numPopToCreate; i++) {
                currentPopulation.add(GA_Search.createRandomPop(currentPopulation.get(0)));
            }

            // Cross Over


        }
    }
}
