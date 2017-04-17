package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	private int position2Dpacmans;
	private int position2Dghosts;
	private int position2Ddots;

	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman) {
		//instancer les attributs
		for(int i=0; i< _stategamepacman.getNumberOfPacmans() ; i++){
			position2Dpacmans += _stategamepacman.getPacmanState(i).getX() + _stategamepacman.getPacmanState(i).getY();
		}

		for(int i=0; i< _stategamepacman.getNumberOfGhosts() ; i++){
			position2Dghosts += _stategamepacman.getGhostState(i).getX() + _stategamepacman.getGhostState(i).getY();
		}

		for(int i=0; i<_stategamepacman.getMaze().getSizeX(); i++) {
			for (int j = 0; j < _stategamepacman.getMaze().getSizeY(); j++) {
				if (_stategamepacman.getMaze().isFood(i,j)){
					position2Ddots += i + j;
				}
			}
		}

	}
	
	@Override
	public String toString() {
		
		return "";
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		


		// on renvoie le clone
		return clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + position2Dpacmans;
		result = prime * result + position2Ddots;
		result = prime * result + position2Dghosts;
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

		if (position2Dpacmans != that.position2Dpacmans) return false;
		if (position2Dghosts != that.position2Dghosts) return false;
		return position2Ddots == that.position2Ddots;
	}


}
