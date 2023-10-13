package tetris;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Tetris extends JFrame {

	/**
	 * Variable que controla los eventos temporales del juego
	 */
	
	private Timer Temporizador;

	/**
	 * Donde discurre toda la logica del juego
	 */
	private Tablero Tablero;
	

    public static void main(String[] args) 
    {
        Tetris Juego = new Tetris();
        Juego.setLocationRelativeTo(null);
        Juego.setVisible(true);
    }
	
	/**
	 * Constructor
	 */
    public Tetris() 
    {
    	setSize(400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Creamos el tablero del juego de tipo JPanel y lo a�adimos al JFrame
    	Tablero = new Tablero();
    	Tablero.setFocusable(true);
        add(Tablero);
        
        
        // Creamos el temporizador
        Temporizador = new Timer(1000, new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		
        		// Si la pieza actual en el tablero no ha llegado al final, entonces continuamos su caida
        		if (!Tablero.caida_ha_finalizado){
        			Tablero.bajarUnaLinea();
        		}
        		else{
        			// Si ha llegado al final del trayecto creamos una nueva pieza en el tablero
        			boolean se_pudo_anyadir = Tablero.anyadirNuevaPieza();
        			
        			// En caso de no poder a�adir una pieza nueva entonces fin del juego
        			if (!se_pudo_anyadir) {
        				Temporizador.stop();
        			}
                }
            }
        });
        
        // Iniciamos el juego
        Temporizador.start();
        
    }
    
}