package tetris;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import tetris.Pieza.Figuras;

public class Tablero extends JPanel  {

	// Ancho del tablero
    final int ancho = 12;

    // Alto del tablero
    final int alto = 25;

    // Indica si la pieza ha llegado a su fin en el tablero y necesitamos crear otra pieza nueva
    boolean caida_ha_finalizado = false;

    // Una vez se inicia el juego empezaremos a escuchar eventos del teclado
    boolean escuchar_eventos = false;

    // Posición de la pieza en el tablero para el eje de las X
    int x_actual = 0;

    // Posición de la pieza en el tablero para el eje de las Y
    int y_actual = 0;

    // Pieza actual con la que nos moveremos por el tablero
    Pieza Pieza;

    // Aqui almacenaremos el resultado de las piezas colocadas
    Figuras[] tablero_final;

    /**
     * Formación del tablero
     */
    public Tablero() 
    {
       // Inicializamos el tablero final
       inicializarTableroFinal();

       escuchar_eventos = true;

       // Añadimos al tablero la primera pieza
       anyadirNuevaPieza();

       // Iniciamos los capturadores de eventos para el teclado
       addKeyListener(new KeyAdapter() {
           public void keyPressed(KeyEvent e) {

               if (escuchar_eventos) {  
                   switch (e.getKeyCode()) {
	  	             case KeyEvent.VK_LEFT:
	  	                 puedeCaer(Pieza, x_actual - 1, y_actual);
	  	                 break;

	  	             case KeyEvent.VK_RIGHT:
	  	                 puedeCaer(Pieza, x_actual + 1, y_actual);
	  	                 break;

	  	             case KeyEvent.VK_UP:
	  	                 puedeCaer(Pieza.rotar(), x_actual, y_actual);
	  	                 break;
	  	             case KeyEvent.VK_DOWN:
	  	            	 puedeCaer(Pieza, x_actual, y_actual - 2);
	  	            	 break;
	               }
               }
           }
       });  
    }

    /**
     * Metodo publico que añade una pieza nueva al tablero y reinicializa otras variables
     */
    public boolean anyadirNuevaPieza()
    {
    	caida_ha_finalizado = false;
    	return nuevaPieza();
    }

    /**
     * Añade una nueva pieza al tablero
     */
    private boolean nuevaPieza()
    {
    	Pieza = new Pieza();
        Pieza.escogerPiezaAleatoriamente();

        x_actual = ancho / 2 + 1;
        y_actual = alto - 1 + Pieza.obtenerYMinima();

        // Si la pieza no puede ser emplazada es que hemos llegado al fin del juego
        if (!puedeCaer(Pieza, x_actual, y_actual)) {

        	// Dejamos de escuchar eventos del teclado
            escuchar_eventos = false;

            // Con esto finalizaremos el juego
        	return false;
        }

        return true;
    }
    
    /**
     * Comprueba si una Pieza puede caer a la siguiente fila sin chocar con otra o sin llegar al algún limite del tablero
     */
    private boolean puedeCaer(Pieza nuevaPieza, int x_siguiente, int y_siguiente)
    {
        for (int i = 0; i < Pieza.obtenerNumCoordRel(); ++i) {
            int x = x_siguiente + nuevaPieza.obtenerX(i);
            int y = y_siguiente - nuevaPieza.obtenerY(i);

            if (x < 0 || x >= ancho || y < 0 || y > alto){
                return false;
            }

            if (obtenerFiguraAlmacenadaEn(x, y) != Figuras.Sin_Forma)
                return false;
        }

        Pieza = nuevaPieza;
        x_actual = x_siguiente;
        y_actual = y_siguiente;

        repaint();

        return true;
    }

    /**
     * Inicializa el tablero final
     */
    private void inicializarTableroFinal()
    {
    	tablero_final = new Figuras[ancho * alto];

        for (int i = 0; i < (alto * ancho); ++i){
            tablero_final[i] = Figuras.Sin_Forma;
        }
    }

    /**
     * Obtiene la figura almacenada en las coordenadas indicadas
     */
    private Figuras obtenerFiguraAlmacenadaEn(int x, int y) 
    {
    	return tablero_final[(y * ancho) + x];
	}

    /**
     * Baja la pieza una fila
     */
    public void bajarUnaLinea()
    {
        if (!puedeCaer(Pieza, x_actual, y_actual-1)){
            piezaHaLlegadoAlFinal();
        }
    }
    
    /**
     * Cuando una pieza ya no puede ser emplazada entonces es que ha llegado al final
     * y debe almacenarse en el tablero final
     */
    private void piezaHaLlegadoAlFinal()
    {
        for (int i = 0; i < 4; ++i) {

        	// Obtenemos las casillas ocupadas a partir de sus coordenadas relativas
            int x = x_actual + Pieza.obtenerX(i);
            int y = y_actual - Pieza.obtenerY(i);

            // Almacenamos la pieza en el tablero final de forma lineal
            tablero_final[(y * ancho) + x] = Pieza.obtenerForma();
        }

        if (!caida_ha_finalizado){
            nuevaPieza();
        }
    }

	/**
	 * Obtiene el ancho del cuadrado a pintar de forma relativa al tamaño del Panel
	 */
    private int obtenerAnchoDelCuadrado()
    {
    	return (int) (getSize().getWidth()/ancho);
	}

    /**
	 * Obtiene el alto del cuadrado a pintar de forma relativa al tamaño del Panel
	 */
    private int obtenerAltoDelCuadrado()
    {
    	return (int) (getSize().getHeight()/alto);
	}

    /**
     * Pintamos el tablero del juego
     */
    public void paint(Graphics g)
    { 
        super.paint(g);

        Dimension size = getSize();

        // Calculamos el tope del tablero
        int tope_del_tablero = (int) size.getHeight() - alto * obtenerAltoDelCuadrado();

        // Pintamos las piezas que ya están en el tablero
        // Iteramos hasta formar la cuadrícula definida por alto x ancho
        for (int i = 0; i < alto; ++i) {
            for (int j = 0; j < ancho; ++j) {

            	// Recuperamos la forma almacenada en la coordenada
            	Figuras Forma = obtenerFiguraAlmacenadaEn(j, alto - i - 1);

            	// Pintamos la forma almacenada
                if (Forma != Figuras.Sin_Forma){
                    pintarCuadrado(
                		g, 
                		j * obtenerAnchoDelCuadrado(), 
                		tope_del_tablero + i * obtenerAltoDelCuadrado(), 
                		Forma
            		);
                }
            }
        }

        // Pintamos la pieza actual
        if (Pieza.obtenerForma() != Figuras.Sin_Forma) {
            for (int i = 0; i < 4; ++i) {

                int x = x_actual + Pieza.obtenerX(i);
                int y = y_actual - Pieza.obtenerY(i);

                pintarCuadrado(
            		g,
            		x * obtenerAnchoDelCuadrado(), 
            		tope_del_tablero + (alto - y - 1) * obtenerAltoDelCuadrado(),
            		Pieza.obtenerForma()
        		);
            }
        }
    }

    /**
     * Pintar los cuadrados
     */
    private void pintarCuadrado(Graphics g, int x, int y, Figuras Forma)
    {
        Color colors[] = { 
    		new Color(0, 0, 0), new Color(204, 102, 102), 
            new Color(102, 204, 102), new Color(102, 102, 204), 
            new Color(204, 204, 102), new Color(204, 102, 204), 
            new Color(102, 204, 204), new Color(218, 170, 0)
        };

        Color color = colors[Forma.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, obtenerAnchoDelCuadrado() - 2, obtenerAltoDelCuadrado() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + obtenerAltoDelCuadrado() - 1, x, y);
        g.drawLine(x, y, x + obtenerAnchoDelCuadrado() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + obtenerAltoDelCuadrado() - 1, x + obtenerAnchoDelCuadrado() - 1, y + obtenerAltoDelCuadrado() - 1);
        g.drawLine(x + obtenerAnchoDelCuadrado() - 1, y + obtenerAltoDelCuadrado() - 1, x + obtenerAnchoDelCuadrado() - 1, y + 1);
    }

}