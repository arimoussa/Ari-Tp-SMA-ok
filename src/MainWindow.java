import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;


public class MainWindow extends JFrame implements Observer {
	
    private final Grille modGrille;
    private JPanel p;
    private JPanel jpcases;
    private JButton bStart;
    private GridLayout g;
    private Map<Coordonnees,JPanel> cases;
    
    public MainWindow(Grille modGrille) throws HeadlessException {
        this.modGrille = modGrille;
        cases=new HashMap<>();
        setup();
    }

    public Grille getModGrille() {
        return modGrille;
    }
    
    private void setup(){
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        JPanel pNorth = new JPanel();
        pNorth.setLayout(new BoxLayout(pNorth, BoxLayout.PAGE_AXIS));

        JLabel titre = new JLabel("Tri collectif Multi-Agents");
        titre.setHorizontalAlignment(JLabel.CENTER);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setFont(new Font("serif", Font.PLAIN, 28));

        pNorth.add(titre);

        this.getContentPane().add(pNorth, BorderLayout.NORTH);

        p = new JPanel();
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setLayout(new BorderLayout());
        jpcases = new JPanel();
        jpcases.setBackground(new Color(51, 102, 102));
        g = new GridLayout(modGrille.getTaille(), modGrille.getTaille());
        for (int x = 0; x < modGrille.getTaille(); ++x)
        {
            for (int y = 0; y < modGrille.getTaille(); ++y) {

                JPanel uneCase=new JPanel();
                uneCase.setBackground(Color.BLACK);
                uneCase.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                uneCase.setPreferredSize(new Dimension(15, 15));
                jpcases.add(uneCase);
                cases.put(new Coordonnees(x,y),uneCase);
            }
        }
        this.update();
        jpcases.setLayout(g);

        this.getContentPane().add(p);
        p.add(jpcases,BorderLayout.CENTER);

        bStart = new JButton("Start");
        this.getContentPane().add(bStart, BorderLayout.SOUTH);

        this.pack();
    }   
    
    public void update()
    {
        for (int x = 0; x < modGrille.getTaille(); ++x)
            for (int y = 0; y < modGrille.getTaille(); ++y) {

                //Point p = new Point(x, y);
                Coordonnees p = new Coordonnees(x,y);
                //Case ca = modGrille.getCaseAt(p);
                Object ca =  modGrille.getCase(p);

                Color c = Color.BLACK;
                if (ca != null) {
                    if (ca instanceof Objet && ((Objet) ca).getName().equals('A'))
                        c =  new Color(0, 0, 255);
                    else if ( ca instanceof Objet &&  ((Objet) ca).getName().equals('B'))
                        c = new Color(255, 0, 0);
                    else if (ca instanceof Agent)
                    {
                        //Caisse caisse = ((Agent) ca).getMaCaisse();
                    	Objet caisse = ((Agent) ca).getObjetPris();
                        
                        if (caisse != null)
                            if (caisse.getName().equals('A'))
                                c = new Color(0, 0, 100); // Un agent portant une caisse A 
                            else
                                c = new Color(100, 0, 0); // Un agent portant une caisse B
                        else
                            c = new Color(0, 120, 0); // Un agent seul 
                    }
                }

    	        for (Entry<Coordonnees, JPanel> current : cases.entrySet() ) {

    	        	if(current.getKey().equals(p) ) {
        	        	//System.out.println("Key:"+ current.getKey().toString() );
        	        	//System.out.println("Value:" );
        	        	current.getValue().setBackground(c);
    	        	}

    	        	
    	        }
               /* 
                if( cases.get(p) != null ) {
                    cases.get(p).setBackground(c);
                }
                */
            }

        p.validate();
        p.repaint();
    }

	@Override
	public void update(Observable o, Object arg) {
		this.update();		
	}
	
   public JButton getbSolve() {
        return bStart;
    }
   public void addController(Controller s)
   {
       bStart.addActionListener(s);
   }

	
	
}
