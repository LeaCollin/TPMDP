package agent.planningagent;

import java.util.*;

import util.HashMapUtil;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;
	
	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		V = new HashMap<Etat,Double>();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
		
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon 
		this.delta=0.0; //difference max entre 2 mises a jour de V(s) (utile dans run pour convergence)
		HashMap<Etat,Double> vClone = (HashMap<Etat,Double>) this.V.clone();
		double somme = 0.0;
		double max = 0.0;
		double deltaIntermediaire = 0.0;
		for (Etat s:this.mdp.getEtatsAccessibles()) {
			max = 0.0; // en negatif ?
			List<Action> actions = this.mdp.getActionsPossibles(s);
			try {
				for (Action a: actions) {
					somme = 0.0;
					Map<Etat,Double> transitions = this.mdp.getEtatTransitionProba(s,a);

					//On somme sur les s'
					for (Etat sprime:transitions.keySet()) {
						double r = this.mdp.getRecompense(s,a,sprime); //R(s,a,sprime)
						double t = transitions.get(sprime); //T(s,a,sprime)

						somme += t * (r + gamma * vClone.get(sprime));
						System.out.println("V "+s+" vaut "+somme);
					}
					if (max < somme){
						max = somme;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			deltaIntermediaire = Math.abs(max-vClone.get(s));
			delta = Math.max(delta,deltaIntermediaire);
			this.V.put(s,max);
			if (getValeur(s)>vmax){
				vmax = getValeur(s);
			}
			if (getValeur(s)<vmin){
				vmin = getValeur(s);
			}
		}

		//******************* laisser la notification a la fin de la methode	

		this.notifyObs();
	}
	
	
	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		List<Action> actions = this.getPolitique(e);
		if (actions.size() == 0)
			return Action2D.NONE;
		else{//choix aleatoire
			return actions.get(rand.nextInt(actions.size()));
		}

		
	}
	@Override
	public double getValeur(Etat _e) {
		return  V.get(_e);
	}
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {

		// retourne action de meilleure valeur dans _e selon V, 
		// retourne liste vide si aucune action legale (etat absorbant)
		List<Action> returnactions = new ArrayList<Action>();
		HashMap<Etat,Double> vClone = (HashMap<Etat,Double>) this.V.clone();
		double max = 0.0;
		double somme = 0.0;
		List<Action> actions = this.mdp.getActionsPossibles(_e);
		try {
			for (Action a: actions) {
				somme = 0.0;
				Map<Etat,Double> transitions = this.mdp.getEtatTransitionProba(_e,a);
				//On somme sur les s'
				for (Etat sprime:transitions.keySet()) {
				double r = this.mdp.getRecompense(_e,a,sprime); //R(s,a,sprime)
				double t = transitions.get(sprime); //T(s,a,sprime)

				somme += t * (r + gamma * vClone.get(sprime));
				}
				if (max < somme){
					max = somme;
					returnactions.clear();
					returnactions.add(a);
				}

			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnactions;
		
	}
	
	@Override
	public void reset() {
		super.reset();

		
		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}
