
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Agent extends Observable implements Runnable {

    private String nom;
    private Coordonnees position;
    private Grille grille;
    private final int pas;
    private final int tailleMemoire;
    private final float kPLUS;
    private final float kMOINS;
    private int nbMouvements;
    private Objet objetPris;
    
    public static int REFRESH_TIME = 10;
    
    
    
    public Objet getObjetPris() {
		return objetPris;
	}

	private LinkedList<Character> memoire;

    public Agent(String nom, Grille grille, Coordonnees coordonnees, int pas, int tailleMemoire, float kPLUS, float kMOINS){
        this.nom = nom;
        this.position = coordonnees;
        this.grille = grille;
        this.pas = pas;
        this.tailleMemoire = tailleMemoire;
        this.kPLUS = kPLUS;
        this.kMOINS = kMOINS;
        this.memoire = new LinkedList<>();
        this.objetPris = null;
        this.nbMouvements = 0;
    }

    public void run(){
        while (true) {
            try {
                this.perception();
                this.action();
                this.setChanged();
                this.notifyObservers();

                Thread.sleep(REFRESH_TIME);
                
            } catch (Exception ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void perception() {
        ArrayList<Coordonnees> coords = getVoisinageCoordinates();
        for (Coordonnees c : coords) {
            Object o = grille.getCase(c);
            if (o instanceof Objet) {
                memoire.addFirst(((Objet) o).getName());
            } else {
                memoire.addFirst(null);
            }
        }
        while (memoire.size() > tailleMemoire) {
            memoire.removeLast();
        }
    }

    public void action() throws Exception {
        if (objetPris == null) {
            if (nbMouvements < pas) {
                deplacerAleatoirement();
                nbMouvements++;
            } else {
                ramasserObjet();
                if (objetPris == null) {
                    deplacerAleatoirement();
                }
            }
        } else {
            deposerObjet();
            if (objetPris != null) {
                deplacerAleatoirement();
            }
        }

    }

    public void ramasserObjet() throws Exception {
        if (objetPris != null) throw new Exception("L'agent a déjà pris un Objet");
        HashMap<Character, Integer> countersObjet = new HashMap<>();
        for (Character type : memoire) {
            if (type != null) {
                if (countersObjet.containsKey(type)) {
                    countersObjet.put(type, countersObjet.get(type) + 1);
                } else {
                    countersObjet.put(type, 1);
                }
            }
        }
        ArrayList<Coordonnees> coordonnee = getVoisinageCoordinates();
        synchronized(grille) {
            for (Coordonnees coordonnees : coordonnee) {
                Object o = grille.getCase(coordonnees);
                if (o instanceof Objet) {
                    Objet obj = (Objet) o;
                    if (countersObjet.containsKey(obj.getName())) {
                        double PPrise = Math.pow(kPLUS / (kPLUS +(countersObjet.get(obj.getName()) / (float) tailleMemoire)), 2);
                        if (Math.random() <= PPrise) {
                            grille.supprimerObjet(coordonnees);
                            objetPris = obj;
                            return;
                        }
                    }
                }
            }
        }
    }

    public void deposerObjet() throws Exception {
        int counterObjet = 0;
        ArrayList<Coordonnees> voisinage = getVoisinageCoordinates();
        ArrayList<Coordonnees> casesPossible = new ArrayList<>();
        synchronized(grille) {
            for (Coordonnees coordonnee : voisinage) {
                Object o = grille.getCase(coordonnee);
                if (o == null) {
                    casesPossible.add(coordonnee);
                } else if (o instanceof Objet &&
                        objetPris.equals((Objet) o)) {
                    counterObjet++;
                }
            }
            if (counterObjet > 0) {
                for (Coordonnees c : casesPossible) {
                    double PPose = Math.pow(kMOINS /(kMOINS +((float) counterObjet)/ tailleMemoire), 2);
                    if (Math.random() <= PPose) {
                        grille.addObjet(c, objetPris);
                        objetPris = null;
                        return;
                    }
                }
            }
        }
    }

    public ArrayList<Coordonnees> getVoisinageCoordinates() {
        ArrayList<Coordonnees> coords = new ArrayList<>();
        coords.add(new Coordonnees(position.getX()-1,position.getY()));
        coords.add(new Coordonnees(position.getX()+1,position.getY()));
        coords.add(new Coordonnees(position.getX(),position.getY()-1));
        coords.add(new Coordonnees(position.getX(),position.getY()+1));
        Iterator<Coordonnees> itC = coords.iterator();
        while (itC.hasNext()) {
            if (! grille.estDansLaGrille(itC.next())) {
                itC.remove();
            }
        }
        return coords;
    }

    public void deplacerAleatoirement(){
        Coordonnees newPosition;
        ArrayList<Coordonnees> casesPossible = new ArrayList<>();
        Coordonnees haut = new Coordonnees(position.getX()-1,position.getY());
        Coordonnees bas = new Coordonnees(position.getX()+1,position.getY());
        Coordonnees gauche = new Coordonnees(position.getX(),position.getY()-1);
        Coordonnees droite = new Coordonnees(position.getX(),position.getY()+1);
        if(position.getX() > 0){
            if(grille.caseLibre(haut)){
                casesPossible.add(haut);
            }
        }
        if(position.getX() < grille.getTaille()-1){
            if(grille.caseLibre(bas)){
                casesPossible.add(bas);
            }
        }
        if(position.getY() > 0){
            if(grille.caseLibre(gauche)){
                casesPossible.add(gauche);
            }
        }
        if(position.getY() < grille.getTaille()-1){
            if(grille.caseLibre(droite)){
                casesPossible.add(droite);
            }
        }
        if (!casesPossible.isEmpty()) {
            if(casesPossible.size() == 1) {
                newPosition = casesPossible.get(0);
            } else {
                newPosition = casesPossible.get((int)(Math.random()*casesPossible.size()));
            }
            grille.deplacerAgent(this, newPosition);
        }
    }

    public Coordonnees getPosition() {
        return position;
    }

    public Grille getGrille() {
        return grille;
    }

    public String getNom(){
        return nom;
    }

    public void setPosition(Coordonnees position) {
        this.position = position;
    }

    public void setGrille(Grille grille) {
        this.grille = grille;
    }

    public void setNom(String n){
        this.nom = n;
    }
}