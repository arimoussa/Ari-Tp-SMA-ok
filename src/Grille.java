
public class Grille {

    private Object[][] grille;

	public Grille(int taille) {
        grille = new Object[taille][taille];
        for(int i = 0; i < taille; i++){
            for(int j = 0; j < taille; j++){
                grille[i][j] = null;
            }
        }
    }

    public boolean caseLibre(Coordonnees coordonnees){
        synchronized(this) {
            return(grille[coordonnees.getX()][coordonnees.getY()] == null);
        }
    }

    public Agent deplacerAgent(Agent agent, Coordonnees cible) {
        if (caseLibre(cible)) {
            synchronized(this) {
                grille[agent.getPosition().getX()][agent.getPosition().getY()] = null;
                grille[cible.getX()][cible.getY()] = agent;
            }
            agent.setPosition(cible);
        }

        return agent;
    }

    public void addObjet(Coordonnees coordonnees, Objet objet) throws Exception {
        synchronized(this) {
            if (grille[coordonnees.getX()][coordonnees.getY()] != null)
                throw new Exception("Objet déjà présent");
            grille[coordonnees.getX()][coordonnees.getY()] = objet;
        }
    }

    public void addAgent(Agent agent) throws Exception {
        synchronized(this) {
            if (grille[agent.getPosition().getX()][agent.getPosition().getY()] != null)
                throw new Exception("Agent déjà présent");
            grille[agent.getPosition().getX()][agent.getPosition().getY()] = agent;
        }
    }

    public Object getCase(Coordonnees coordonnees){
        synchronized(this) {
            if (coordonnees.getX() < 0 || coordonnees.getX() >= grille.length ||
                    coordonnees.getY() < 0 || coordonnees.getY() >= grille[0].length) {
                return null;
            }
            return grille[coordonnees.getX()][coordonnees.getY()];
        }
    }

    public void supprimerObjet(Coordonnees coordonnees) throws Exception {
        synchronized(this) {
            if(!(grille[coordonnees.getX()][coordonnees.getY()] instanceof Objet))
                throw new Exception("Aucun objet");
            grille[coordonnees.getX()][coordonnees.getY()] = null;
        }
    }

    public synchronized void affichageConsole(){
        String ligne="";
        for(int i = 0; i < grille.length; i++){
            for(int j = 0; j < grille[i].length; j++){
                synchronized(this) {
                    ligne += "[";
                    if(grille[i][j] != null){
                        if (grille[i][j] instanceof Agent) {
                            ligne += ((Agent) grille[i][j]).getNom();
                        } else if (grille[i][j] instanceof Objet) {
                            ligne += ((Objet) grille[i][j]).getName();
                        }
                    } else {
                        ligne += " ";
                    }
                    ligne += "]";
                }
            }
            System.out.println(ligne);
            ligne ="";
        }
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    }

    public boolean estDansLaGrille(Coordonnees coordonnees) {
        synchronized(this) {
            return (coordonnees.getX() >= 0 && coordonnees.getX() < grille.length &&
                    coordonnees.getY() >= 0 && coordonnees.getY() < grille[0].length);
        }
    }

    public int getTaille(){
        return grille.length;
    }
}