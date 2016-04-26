package controllers.limitdepthfirst;

import java.util.ArrayList;

import controllers.limitdepthfirst.Agent;
import core.game.Observation;
import core.game.StateObservation;
import ontology.Types.ACTIONS;
import ontology.Types.WINNER;
import tools.Vector2d;

public class limitDepthfirst{
	private int depth;				//search depth
	private StateObservation currentState;					//current state
	private ArrayList<ACTIONS>actionList = new ArrayList<ACTIONS>();		//action list
	private ArrayList<ACTIONS>bestActionList = new ArrayList<ACTIONS>();	//best action list
	private double Min;
	
	public limitDepthfirst(StateObservation state){
		depth = 5;
		currentState = state;
		Min = 10000;
	}
	
	public void BestAction(StateObservation currentState,int depth){
		if(depth == 0){
			System.out.println(actionList);
			if(bestActionList.isEmpty()){
				Min = evaluate(currentState);
				//bestActionList = (ArrayList<ACTIONS>) actionList.clone();
				bestActionList = (ArrayList<ACTIONS>) actionList.clone();
			}
			else{
				if(evaluate(currentState) < Min){
					Min = evaluate(currentState);
					System.out.println(Min);
					bestActionList = (ArrayList<ACTIONS>) actionList.clone();
				}
			//	System.out.println(actionList);
			//	System.out.println(Min);
			}
			int temp = actionList.size();
			actionList.remove(temp-1);
			return;
		}
		if(actionList.isEmpty() ==true){
			for(int i = 0;i < Agent.actions.length;i++){
				StateObservation newState = currentState.copy();
				actionList.add(Agent.actions[i]);
				newState.advance(Agent.actions[i]);
				BestAction(newState,depth-1);
			}
		}
		else if(currentState.getGameWinner()  != WINNER.PLAYER_WINS){
			if(currentState.getGameWinner() == WINNER.PLAYER_LOSES){
				int temp = actionList.size();
				actionList.remove(temp-1);
				return;
			}
			int sign = 0;
			for(int i = 0;i < Agent.StateList.size(); i ++){
				if(Agent.StateList.get(i).equalPosition(currentState) == true){
					sign=1;break;
				}
		    }
			if(sign == 1){
				int temp = actionList.size();
				actionList.remove(temp-1);
				return;
			}
			for(int i = 0;i < Agent.actions.length;i++){
				StateObservation newState = currentState.copy();
				actionList.add(Agent.actions[i]);
				newState.advance(Agent.actions[i]);
				BestAction(newState,depth-1);
			}
			System.out.println(actionList);
			int temp = actionList.size();
			actionList.remove(temp-1);
			return;
		}
		else{
			if(bestActionList.isEmpty()){
				Min = evaluate(currentState);
				bestActionList = (ArrayList<ACTIONS>) actionList.clone();
			}
			else{
				System.out.println(actionList);
			//	System.out.println(Min);
				if(evaluate(currentState) < Min){
					Min = evaluate(currentState);
					bestActionList = (ArrayList<ACTIONS>) actionList.clone();
				}
				if(evaluate(currentState) == Min && actionList.size()<bestActionList.size()){
					Min = evaluate(currentState);
					bestActionList = (ArrayList<ACTIONS>) actionList.clone();	
				}
			//	System.out.println(actionList);
				System.out.println(Min);
			}
			int temp = actionList.size();
			actionList.remove(temp-1);
			return;
		}
	}

	
	public ACTIONS returnAction(StateObservation currentState){
		BestAction(currentState,depth);
		System.out.println(bestActionList);
		System.out.println(Min);
		return bestActionList.get(0);
	}
	
	public double evaluate(StateObservation currentState){
		ArrayList<Observation>[] fixedPositions = currentState.getImmovablePositions();
		ArrayList<Observation>[] movingPositions = currentState.getMovablePositions();
		Vector2d keypos;
		Vector2d goalpos;
		
		if(fixedPositions[1].size() != 0){
			goalpos = fixedPositions[1].get(0).position; //Ä¿±êµÄ×ø±ê
		}
		else{
			goalpos = currentState.getAvatarPosition();
		}
		if(movingPositions[0].size() != 0){
			keypos = movingPositions[0].get(0).position; //Ô¿³×µÄ×ø±ê
		}
		else{
			System.out.println(currentState.getAvatarPosition());
			keypos = currentState.getAvatarPosition();
		}
		
		
		Vector2d avatorpos = currentState.getAvatarPosition();
		
		double goal_ava = Math.abs(avatorpos.x-goalpos.x) + Math.abs(avatorpos.y-goalpos.y);
		double key_ava = Math.abs(avatorpos.x-keypos.x) + Math.abs(avatorpos.y-keypos.y);
		double goal_key = Math.abs(keypos.x-goalpos.x) + Math.abs(keypos.y-goalpos.y);
		
		double value = 0;
		if(Agent.keySign == 0){
			value = goal_key+key_ava;
		}
		else{
			value = 10*goal_ava;
		}
		
		return value;
	}
}