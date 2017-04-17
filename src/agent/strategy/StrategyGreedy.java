package agent.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
/**
 * Strategie qui renvoit un choix aleatoire avec proba epsilon, un choix glouton (suit la politique de l'agent) sinon
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	/**
	 * parametre pour probabilite d'exploration
	 */
	protected double epsilon;
	private Random rand=new Random();
	
	
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		this.epsilon = epsilon;
	}

	//epsilon = probabilité d'exploration
	//Avec probabilité epsilon, choisit une action possible au hasard
	//Avec probabilité 1-epsilon, choisit une des meilleures actions (cf getPolitique)
	@Override
	public Action getAction(Etat _e) {//renvoi null si _e absorbant
		double d = rand.nextDouble();
		List<Action> actions;
		if (this.agent.getActionsLegales(_e).isEmpty()){
			return null;
		}
		if (d<epsilon){
			actions = agent.getActionsLegales(_e);
			return actions.get(rand.nextInt(actions.size()));
		} else {
			actions = agent.getPolitique(_e);
			return actions.get(rand.nextInt(actions.size()));
		}
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		System.out.println("epsilon:"+epsilon);
	}

/*	@Override
	public void setAction(Action _a) {
		// TODO Auto-generated method stub
		
	}*/

}
