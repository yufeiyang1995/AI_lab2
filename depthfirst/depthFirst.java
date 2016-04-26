package controllers.depthfirst;

import java.util.ArrayList;
import java.util.Iterator;

import controllers.depthfirst.Agent;
import core.game.Observation;
import core.game.StateObservation;
import ontology.Types.ACTIONS;
import ontology.Types.WINNER;

public class depthFirst{
	private StateObservation state;
	private StateObservation currentState;
	private ArrayList<StateObservation> StateList = new ArrayList<StateObservation>();
	private ArrayList<ACTIONS>actionList = new ArrayList<ACTIONS>();
	private int actionNum;
	private double value;
	private int depth;
	private boolean isWin;
	
	public depthFirst(StateObservation state) {
		this.state = state;
		currentState = state;
		actionNum = 0;
		isWin = false;
		StateList.add(state);
		StateObservation newState = currentState.copy();
		newState.advance(Agent.actions[0]);
	//	System.out.println(currentState.getAvatarPosition());
	}
	
	public void BestAction(StateObservation currentState){
		System.out.println(actionList);
		ArrayList<Observation>[] movingPositions = currentState.getMovablePositions();
		//System.out.println(movingPositions[1].size());
		//System.out.println(currentState.getAvatarPosition());
		if(actionList.isEmpty()){
			for(int i = 0;i < Agent.actions.length;i++){
				if(isWin == true)break;
				StateObservation newState = currentState.copy();
				actionList.add(Agent.actions[i]);
				newState.advance(Agent.actions[i]);
				BestAction(newState);
			}
		}
		else if(currentState.getGameWinner()  != WINNER.PLAYER_WINS && isWin == false){
			if(currentState.getGameWinner() == WINNER.PLAYER_LOSES){
				int temp = actionList.size();
				actionList.remove(temp-1);
				return;
			}
			int sign = 0;
			for(int i = 0;i < StateList.size(); i ++){
				if(StateList.get(i).equalPosition(currentState) == true){
					sign=1;break;
				}
		    }
		//	num++;
			if(sign == 1){
				int temp = actionList.size();
				actionList.remove(temp-1);
				return;
			}
			else{
				StateList.add(currentState);
			}
			for(int i = 0;i < Agent.actions.length;i++){
				if(isWin == true)break;
				StateObservation newState = currentState.copy();
				actionList.add(Agent.actions[i]);
				newState.advance(Agent.actions[i]);
				BestAction(newState);
			}
			if(isWin == false){
				int temp = actionList.size();
				actionList.remove(temp-1);
			}
			return;
		}
		else{
			System.out.println(StateList.size());
			System.out.println(actionList);
			Agent.bestActions = actionList;
			isWin = true;
			return;
		}
	}
	
}