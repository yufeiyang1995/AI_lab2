package controllers.Astar;

import java.util.ArrayList;
import java.util.Random;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer{


    /**
     * Random generator for the agent.
     */
    protected Random randomGenerator;

    /**
     * Observation grid.
     */
    protected ArrayList<Observation> grid[][];

    /**
     * block size
     */
    protected int block_size;
    public static Types.ACTIONS[] actions;
    public static ArrayList<ACTIONS>bestActions = new ArrayList<ACTIONS>();
    public static ArrayList<StateObservation> StateList = new ArrayList<StateObservation>();
    public static int NUM_ACTIONS;
    public static int MAX_DEPTH;
    public static int actTime;
    public static int keySign;       
    
	/**
	 * initialize all variables for the agent
	 * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
	 */
	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
		randomGenerator = new Random();
	//	bestActions = stateObs.getAvailableActions().toArray(new ACTIONS[0]);
		actions = stateObs.getAvailableActions().toArray(new ACTIONS[0]);
		actTime = 0;
		keySign = 0;

		Astar root =  new Astar(stateObs);
		if(root.bestAction()){
			bestActions = root.actionList;
		}
		else{
			bestActions = null;
		}
	}
	
	/**
	 * return ACTION_NIL on every call to simulate doNothing player
	 * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
	 * @return 	ACTION
	 */
	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		Types.ACTIONS action = null;
		grid = stateObs.getObservationGrid();
		StateObservation stCopy = stateObs.copy();
		
		//depthFirst root = new depthFirst(stateObs); 
		//root.BestAction(stateObs);
		//System.out.println(bestActions);
		
		
		double worstCase = 9000;
		double avgTime = 10;
		double totalTime = 0;
		double iteration = 0;
		int bestAction = -1;
		
		
		
		while(elapsedTimer.remainingTimeMillis() > 2 * avgTime && elapsedTimer.remainingTimeMillis() > worstCase){
			ElapsedCpuTimer temp = new ElapsedCpuTimer();
			
			if(stateObs.isGameOver() == true){
				System.out.println("GameOver");
			}
			//treeSelect
			//TreeNode node = root.SelectNode();
			
			//Simulate
			//double value = node.ExploreNode();
			
			//RollBack
			//node.UpdateNode(value);
			
			//Get the best action
			//bestAction = root.GetBestChild();
			
			totalTime += temp.elapsedMillis();
			iteration += 1;
			avgTime = totalTime / iteration;
			
			action = actions[0];
			//action = bestActions.get(actTime);
		}

		if(bestActions != null){
			action = bestActions.get(actTime);
			actTime++;
		}
		else{
			System.out.println("The game has not a successful answer!");
		}
		return action;
	}
}