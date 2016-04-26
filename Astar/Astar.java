package controllers.Astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import controllers.Astar.Agent;
import core.game.Observation;
import core.game.StateObservation;
import ontology.Types.ACTIONS;
import ontology.Types.WINNER;
import tools.Vector2d;

public class Astar{
	//private StateObservation currentState;					//current state
	private StateObservation startState; 
	private int movingSign;
	private int num;
	private int length;
	public ArrayList<ACTIONS>actionList = new ArrayList<ACTIONS>();
	private HashSet<StateObservation> openSet = new HashSet<StateObservation>();
	private HashSet<StateObservation> closeSet = new HashSet<StateObservation>();
	private HashMap<StateObservation,Double> gscore = new HashMap<StateObservation,Double>();
	private HashMap<StateObservation,Double> fscore = new HashMap<StateObservation,Double>();
	private HashMap<StateObservation,StateObservation> camefrom = new HashMap<StateObservation,StateObservation>();
	
	public Astar(StateObservation state){
		startState = state;
		openSet.add(state);
		movingSign = 0;
		num = 0;
		gscore.put(state, 0.0);
		fscore.put(state,heuristic(state));
		
	}
	
	public boolean bestAction(){
		while (!openSet.isEmpty()) {
			StateObservation currentState = minState();
			if (currentState.getGameWinner() == WINNER.PLAYER_WINS) {
				getActionList(currentState);
				return true;
			}

			System.out.println(currentState.getAvatarPosition());
			openSet.remove(currentState);
			closeSet.add(currentState);
			for (int i = 0; i < Agent.actions.length; i++) {
				StateObservation newState = currentState.copy();
				newState.advance(Agent.actions[i]);
				inCloseSet(newState);
				if(inCloseSet(newState))continue;
				double tentative_gScore = gscore.get(currentState) + 50;
				if (!inOpenSet(newState)) {
					openSet.add(newState);
				}
				//else if(gscore.containsKey(newState) && tentative_gScore >= gscore.get(newState))continue;
				camefrom.put(newState, currentState);
				gscore.put(newState, tentative_gScore);
				fscore.put(newState, heuristic(newState));
			}
		}
		return false;
	}
	
	public boolean inOpenSet(StateObservation currentState){
		ArrayList<Observation>[] movingPositions1 = currentState.getMovablePositions();
		//length = movingPositions1.length;
		for(StateObservation state:openSet){
		//	ArrayList<Observation>[] movingPositions1 = currentState.getMovablePositions();
	    	ArrayList<Observation>[] movingPositions2 = state.getMovablePositions();
	    	if(movingPositions1 != null){
	    		if(movingPositions1.length == movingPositions2.length && movingPositions1.length > 1){
	    			if(state.equalPosition(currentState)&&(/*movingSign==1 ||*/ movingPositions1[1].size()==movingPositions2[1].size()))
	    				return true;
	    		}
	    		else{
	    			if(state.equalPosition(currentState))
	    				return true;
	    		}
	    	}
	    	else{
	    		if(state.equalPosition(currentState))
    				return true;
	    	}
		}
		return false;
	}
	
	public boolean inCloseSet(StateObservation currentState){
		ArrayList<Observation>[] movingPositions1 = currentState.getMovablePositions();
		//System.out.println(movingPositions1.length);
		//length = movingPositions1.length;
		for(StateObservation state:closeSet){
			//ArrayList<Observation>[] movingPositions1 = currentState.getMovablePositions();
	    	ArrayList<Observation>[] movingPositions2 = state.getMovablePositions();
	    	//System.out.println(movingPositions1[1].size() + "," + movingPositions2[1].size());
	    	if(movingPositions1 != null){
	    		if(movingPositions1.length == movingPositions2.length && movingPositions1.length > 1){
	    		//System.out.println(movingPositions1.length+","+movingPositions2.length);
	    			if(state.equalPosition(currentState) &&(/*movingSign==1 ||*/ movingPositions1[1].size()==movingPositions2[1].size()))
	    				return true;
	    		}
	    		else{
	    			if(state.equalPosition(currentState)){
	    				return true;
	    			}
	    		}
	    	}
	    	else{
	    		if(state.equalPosition(currentState)){
    				return true;
    			}
	    	}
		}
		return false;
	}
	
	
	public double heuristic(StateObservation currentState){
		double value = 0;
		num++;
		System.out.println(num);
		ArrayList<Observation>[] fixedPositions = currentState.getImmovablePositions();
		ArrayList<Observation>[] movingPositions = currentState.getMovablePositions();
		Vector2d keypos;
		Vector2d goalpos;
		keypos = currentState.getAvatarPosition();
		if(fixedPositions[1].size() != 0){
			goalpos = fixedPositions[1].get(0).position; 
			//System.out.println(fixedPositions[1].get(0).position);
		}
		else{
			goalpos = currentState.getAvatarPosition();
		}
		
		System.out.println(movingSign);
		if(movingSign == 0){
			if(movingPositions[0].size() != 0){
				//System.out.println(movingPositions[0].get(0).position);
				keypos = movingPositions[0].get(0).position; 
			}
			else{
				//System.out.println(currentState.getAvatarPosition());
				keypos = currentState.getAvatarPosition();
				movingSign = 1;
			}
		}
		else{
			keypos = currentState.getAvatarPosition();
		}
		Vector2d avatorpos = currentState.getAvatarPosition();
		
		double key_ava = Math.abs(avatorpos.x-keypos.x) + Math.abs(avatorpos.y-keypos.y);
		double goal_key = Math.abs(keypos.x-goalpos.x) + Math.abs(keypos.y-goalpos.y);
		
		//if(currentState.getAvatarPosition().x > 0)
		value = goal_key + key_ava;
		
		//System.out.println(currentState.getAvatarPosition());
		//System.out.println(goal_key);
		return value;
	}
	
	public void getActionList(StateObservation currentState){
		if(currentState.equalPosition(startState)){
			Collections.reverse(actionList);
			return;
		}
		StateObservation lastState = camefrom.get(currentState);
		for (int i = 0; i < Agent.actions.length; i++) {
			StateObservation newState = lastState.copy();
			newState.advance(Agent.actions[i]);
			if(newState.equalPosition(currentState)){
				actionList.add(Agent.actions[i]);
				break;
			}
		}
		getActionList(lastState);
	}
	
	public StateObservation minState(){
		double min = Double.MAX_VALUE;
		StateObservation minState = null;
		for(StateObservation state:openSet){
			if(fscore.containsKey(state)){
				if(fscore.get(state) < min){
					min = fscore.get(state);
					minState = state;
				}
			}
		}
		return minState;
	}
}