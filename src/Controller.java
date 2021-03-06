import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Controller implements ActionListener {
	
    private final MainWindow view;

    private int nbAgents;
    private int sizeMem;
    private float kPrise;
    private float kDepot;
    private int nbCaisse;
    
    
    public Controller(MainWindow view, int nbAgent, float kPrise, float kDepot, int nbCaisse, int sizeMem) {
        this.view = view;
        this.nbAgents = nbAgent;
        this.sizeMem = sizeMem;
        this.nbCaisse = nbCaisse;
        this.kPrise = kPrise;
        this.kDepot = kDepot;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        view.getbSolve().setEnabled(false);
        Grille g = view.getModGrille();

        //creation des agents
        Factory fact = new Factory(view.getModGrille(), this.nbAgents, this.nbCaisse, this.sizeMem, this.kPrise, this.kDepot);
        List<Agent> agents = null;
		try {
			agents = fact.creationAgents();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        List<Thread> tAgents = new ArrayList<>();

        //Demarrer tous les agents
        for (Agent a : agents) {
            a.addObserver(view);
            Thread t = new Thread(a);
            tAgents.add(t);
            t.start();
        }

        view.update();

        //Attendre la fin des agents
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Thread t : tAgents)
                    try {
                        t.join();
                    } catch (InterruptedException e1) {
                    }
            }
        }).start();
    }
    

}
