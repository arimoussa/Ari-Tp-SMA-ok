import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Factory {
    private List<Agent> agents;
    private Grille grille;
    private int sizeMem, nbAgents, nbCaisse;
    private float kPrise, kDepot;
    


    public Factory(Grille grille, int nbAgents, int nbCaisse, int sizeMem, float kPrise, float kDepot) {
        this.grille = grille;
        this.sizeMem = sizeMem;
        this.kPrise = kPrise;
        this.kDepot = kDepot;
        this.nbAgents = nbAgents;
        this.nbCaisse = nbCaisse;
        this.agents = new ArrayList<>();
    }
    
    protected Agent createAgent()
    {
        Agent a = new Agent(String.valueOf(agents.size()), this.grille,genererCoordonnees(this.grille),1,this.sizeMem,this.kPrise,this.kDepot);

        agents.add(a);

        return a;
    }
    
    
    
    public List<Agent> creationAgents() throws Exception
    {

        Random r = new Random();

        for (int i = 0; i < nbAgents; ++i)
        {
            // Selection points aleatoire pour creer agent
            Agent a = this.createAgent();

            grille.addAgent(a);
        }

        for (int i = 0; i < nbCaisse; ++i)
        {
            // Selection points aleatoire pour creer un objet

            int randLabel = r.nextInt(2);
            Character label = (randLabel == 0) ? 'A' : 'B'; // type aleatoire

            grille.addObjet( this.genererCoordonnees(grille), new Objet(label) );
        }

        return agents;
    }
    
    
    public Coordonnees genererCoordonnees(Grille grille) {
        Coordonnees coordonnee;
        do {
            coordonnee = new Coordonnees(
                    (int) (Math.random() * grille.getTaille()),
                    (int) (Math.random() * grille.getTaille())
            );
        } while (!grille.caseLibre(coordonnee));
        return coordonnee;
    }
    public List<Agent> getAgents() {
        return agents;
    }

    

}
