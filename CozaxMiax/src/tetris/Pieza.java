package tetris;
import java.util.Random;

public class Pieza {

	// Figuras
    enum Figuras {
    	Sin_Forma,
    	L, 
    	L_Inverso,
    	S,
    	Cuadrado,
    	Linea,
    	Z,
    	T    	
	};

	// Cada formas tiene aqui definidos sus puntos de coordenadas iniciales 
	private int[][][] plantilla_de_coordenadas;

    // Contiene las coordenadas de la pieza
    private int coordenadas[][];

	// Aqui se almacenara la forma actual de la pieza
    private Figuras forma_actual;

    // Numero de coordenadas relativas de la Pieza
    private int num_coord_rel = 4;

    // Numero de atributos que tiene cada coordenada relativa (X e Y)
    private int num_ejes = 2;

	/**
	 * Construcción de lsa piezas
	 */
    public Pieza()
    {
    	// Cada pieza tiene 4 coordenedas relativas con 2 posiciones, son relativas a la posicion actual del tablero
        coordenadas = new int[num_coord_rel][num_ejes];

        // Inicialmente la pieza no tiene forma asignada
        asignarForma(Figuras.Sin_Forma);
    }

    /**
     * Dada una forma le asignamos sus coordenadas iniciales
     */
    public void asignarForma(Figuras forma) 
    {
    	// Plantilla de coordenadas relativas de cada una de las formas enumeradas en Figuras
    	// Una de ellas siempre coincide con la posicion actual en el tablero y las demás son relativas
        plantilla_de_coordenadas = new int[][][] {
    		// Sin Forma
    		{ { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },

    		// L
    		{ {-1,-1 }, { 0,-1 }, { 0, 0 }, { 0, 1 } },

    		// L inverso
    		{ { 1,-1 }, { 0,-1 }, { 0, 0 }, { 0, 1 } },

    		// S
    		{ { 0,-1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },

    		// Cuadrado
    		{ { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },

    		// Linea
    		{ { 0,-1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },

    		// Z
    		{ {-1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } },

    		// T
    		{ { 0,-1 }, { 0, 0 }, {-1, 0 }, {-1, 1 } }
		};

        // Asignamos las coordenadas relativas de la forma elegida
        for (int i = 0; i < num_coord_rel ; i++) {
        	for (int j = 0; j < num_ejes; ++j) {

        		coordenadas[i][j] = plantilla_de_coordenadas[forma.ordinal()][i][j];
        	}
        }

        forma_actual = forma;
    }

    /**
     * Asignamos la posicion X a una de las 4 coordenadas relativas de la pieza 
     */
    private void asignarX(int posicion_relativa, int x) 
    { 
    	coordenadas[posicion_relativa][0] = x; 
	}

    /**
     * Asignamos la posicion Y a una de las 4 coordenadas relativas de la pieza 
     */
    private void asignarY(int posicion_relativa, int y) 
    {
    	coordenadas[posicion_relativa][1] = y;
	}

    /**
     * Obtenemos la posicion X de una coordenada relativa
     */
    public int obtenerX(int posicion_relativa) 
    {
    	return coordenadas[posicion_relativa][0]; 
	}

    /**
     * Obtenemos la posicion Y de una coordenada relativa
     */
    public int obtenerY(int posicion_relativa) 
    { 
    	return coordenadas[posicion_relativa][1]; 
	}

    public int obtenerNumCoordRel()
    {
    	return num_coord_rel;
    }

    /**
     * Obtenemos la forma actual de la pieza
     */
    public Figuras obtenerForma()
    {
    	return forma_actual; 
	}

	/**
	 * Escogemos aleatoriamete una pieza y le asignamos forma a la Pieza
	 */
    public void escogerPiezaAleatoriamente()
    {
    	Random r = new Random();

    	// Obtenemos las formas disponivles
    	Figuras[] figuras_disponibles = Figuras.values();

    	// Escogemos aleatoriamente una forma
        Figuras forma_elegida = figuras_disponibles[(Math.abs(r.nextInt()) % 7 + 1)];

        // Se la asignamos a la Pieza
        asignarForma(forma_elegida);
    }

    /**
     * Obtenemos la menor posición Y (posicion mas baja en el tablero) de las coordenadas relativas
     */
    public int obtenerYMinima() 
    {
    	// Empezamos asumiendo que es la primera la menor
    	int minima = coordenadas[0][1];

    	// Comparamos con el resto de coordenadas relativas
    	for (int i=0; i < num_coord_rel; i++) {
    		minima = Math.min(minima, coordenadas[i][1]);
    	}

    	return minima;
    }

    /**
     * Realiza la rotación en una copia de la pieza.
     * 
     * La rotacion se realiza en el sentido de las agujas del reloj.
     */
    public Pieza rotar()
    {
    	// El cuadrado no gira
        if (forma_actual == Figuras.Cuadrado){
            return this;
        }

        Pieza PiezaRotada = new Pieza();
        PiezaRotada.forma_actual = forma_actual;

        // Realizamos el giro a la derecha
        for (int i = 0; i < num_coord_rel; ++i) {
        	PiezaRotada.asignarX(i, -obtenerY(i));
            PiezaRotada.asignarY(i, obtenerX(i));
        }

        return PiezaRotada;
    }

}