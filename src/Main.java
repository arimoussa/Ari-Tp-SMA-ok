/*
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        lancerTri(10, 4, 20, 1, 8, 0.1f, 0.3f);
    }

    public static void lancerTri(int taille, int nbAgents, int nbObjets, int i, int t, float kp, float km) {
        Grille grille = new Grille(taille);
        ArrayList<Agent> la = new ArrayList();
        for (int x = 0; x < nbAgents; x++) {
            Agent a = new Agent(String.valueOf(x), grille, genererCoordonnees(grille), i, t, kp, km);
            la.add(a);
            try {
                grille.addAgent(a);
                
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (int x = 0; x < nbObjets; x++) {
            try {
                grille.addObjet(genererCoordonnees(grille), new Objet('A'));
                grille.addObjet(genererCoordonnees(grille), new Objet('B'));
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        for (Agent a : la) {
            a.start();
        }
        while (true) {
            grille.affichageConsole();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Coordonnees genererCoordonnees(Grille grille) {
        Coordonnees coordonnee;
        do {
            coordonnee = new Coordonnees(
                    (int) (Math.random() * grille.getTaille()),
                    (int) (Math.random() * grille.getTaille())
            );
        } while (!grille.caseLibre(coordonnee));
        return coordonnee;
    }

}
*/


import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args)
    {
        float kprise = (float) 0.1;
        float kdepot = (float) 0.3;
        int nbAgents = 15, nbObjects = 50, mem = 10;

        Grille g = new Grille(50);


        MainWindow mw = new MainWindow(g);
        
        mw.addController(new Controller(mw, nbAgents, kprise, kdepot, nbObjects, mem));

        mw.setVisible(true);
    }
}

