import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

public class AhorcadoPokemonGUI extends JFrame {
    private static final String POKEAPI_SPRITES_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
    private static final String POKEBALL_ICON_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png";
    private static final int MAX_INTENTOS = 6;
    
    private String palabraSecreta;
    private char[] palabraOculta;
    private List<Character> letrasUsadas;
    private int intentosRestantes;
    private int pokemonId;
    
    // Componentes de la interfaz
    private JLabel lblPalabra;
    private JLabel lblIntentos;
    private JLabel lblLetrasUsadas;
    private JLabel lblImagenPokemon;
    private JPanel panelLetras;
    private JPanel panelAhorcado;
    private JButton btnNuevoJuego;
    private JLabel lblMensaje;
    
    // Colores personalizados
    private Color colorFondo = new Color(240, 248, 255);
    private Color colorTexto = new Color(25, 25, 112);
    private Color colorExito = new Color(34, 139, 34);
    private Color colorError = new Color(220, 20, 60);
    
    public AhorcadoPokemonGUI() {
        inicializarInterfaz();
        iniciarNuevoJuego();
    }
    
    private void inicializarInterfaz() {
        setTitle("🎮 Ahorcado Pokémon - Primera Generación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(colorFondo);
        
        // Panel superior con información del juego
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con el juego
        JPanel panelCentral = crearPanelCentral();
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botones de letras
        panelLetras = crearPanelLetras();
        add(panelLetras, BorderLayout.SOUTH);
        
        // Configurar la ventana
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorFondo);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Título con iconos de Pokéball desde URL
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        header.setBackground(colorFondo);
        JLabel leftIcon = new JLabel(cargarIcono(POKEBALL_ICON_URL, 24, 24));
        JLabel rightIcon = new JLabel(cargarIcono(POKEBALL_ICON_URL, 24, 24));
        JLabel titulo = new JLabel("Ahorcado Pokémon - Primera Generación");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(220, 20, 60)); // Rojo Pokémon
        header.add(leftIcon);
        header.add(titulo);
        header.add(rightIcon);
        panel.add(header, BorderLayout.NORTH);
        
        // Información del juego
        JPanel panelInfo = new JPanel(new FlowLayout());
        panelInfo.setBackground(colorFondo);
        
        lblIntentos = new JLabel("Intentos restantes: " + MAX_INTENTOS);
        lblIntentos.setFont(new Font("Arial", Font.BOLD, 14));
        lblIntentos.setForeground(colorTexto);
        
        lblLetrasUsadas = new JLabel("Letras usadas: ");
        lblLetrasUsadas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblLetrasUsadas.setForeground(colorTexto);
        
        panelInfo.add(lblIntentos);
        panelInfo.add(Box.createHorizontalStrut(20));
        panelInfo.add(lblLetrasUsadas);
        
        panel.add(panelInfo, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorFondo);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel izquierdo - Ahorcado con diseño moderno
        panelAhorcado = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarAhorcado(g);
            }
        };
        panelAhorcado.setPreferredSize(new Dimension(350, 450));
        panelAhorcado.setBackground(new Color(248, 248, 255));
        panelAhorcado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 3),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Panel derecho - Palabra e imagen
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(colorFondo);
        
        // Palabra oculta
        lblPalabra = new JLabel();
        lblPalabra.setFont(new Font("Arial", Font.BOLD, 24));
        lblPalabra.setForeground(colorTexto);
        lblPalabra.setHorizontalAlignment(SwingConstants.CENTER);
        lblPalabra.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Imagen del Pokémon (inicialmente oculta)
        lblImagenPokemon = new JLabel();
        lblImagenPokemon.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenPokemon.setVerticalAlignment(SwingConstants.CENTER);
        lblImagenPokemon.setPreferredSize(new Dimension(300, 300));
        lblImagenPokemon.setVisible(false);
        
        // Mensaje de estado
        lblMensaje = new JLabel("¡Adivina el Pokémon!");
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 16));
        lblMensaje.setForeground(colorTexto);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelDerecho.add(lblPalabra, BorderLayout.NORTH);
        panelDerecho.add(lblImagenPokemon, BorderLayout.CENTER);
        panelDerecho.add(lblMensaje, BorderLayout.SOUTH);
        
        panel.add(panelAhorcado, BorderLayout.WEST);
        panel.add(panelDerecho, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelLetras() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorFondo);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Botón de nuevo juego más estilizado
        btnNuevoJuego = new JButton("🔄 Nuevo Juego");
        btnNuevoJuego.setFont(new Font("Arial", Font.BOLD, 16));
        btnNuevoJuego.setBackground(new Color(255, 99, 99)); // Rojo Pokémon
        btnNuevoJuego.setForeground(Color.WHITE);
        btnNuevoJuego.setFocusPainted(false);
        btnNuevoJuego.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        btnNuevoJuego.setBorderPainted(false);
        btnNuevoJuego.setOpaque(true);
        btnNuevoJuego.addActionListener(e -> iniciarNuevoJuego());
        
        // Panel de botones de letras
        JPanel panelBotones = new JPanel(new GridLayout(3, 9, 5, 5));
        panelBotones.setBackground(colorFondo);
        // margen superior para separar del botón "Nuevo Juego"
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Crear botones modernos para cada letra del alfabeto
        for (char letra = 'A'; letra <= 'Z'; letra++) {
            JButton btnLetra = new JButton(String.valueOf(letra));
            btnLetra.setFont(new Font("Arial", Font.BOLD, 14));
            btnLetra.setBackground(new Color(255, 255, 255));
            btnLetra.setForeground(new Color(25, 25, 112));
            btnLetra.setFocusPainted(false);
            btnLetra.setBorderPainted(false);
            btnLetra.setOpaque(true);
            
            // Crear borde redondeado personalizado
            btnLetra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));
            
            // Efecto hover
            btnLetra.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnLetra.setBackground(new Color(240, 248, 255));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnLetra.setBackground(new Color(255, 255, 255));
                }
            });
            
            final char letraFinal = letra;
            btnLetra.addActionListener(e -> procesarLetra(letraFinal));
            
            panelBotones.add(btnLetra);
        }
        
        panel.add(btnNuevoJuego, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void dibujarAhorcado(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(new Color(70, 130, 180));
        
        int errores = MAX_INTENTOS - intentosRestantes;
        
        // Base del ahorcado con diseño mejorado
        if (errores >= 1) {
            g2d.drawLine(60, 380, 220, 380); // Base horizontal más larga
            g2d.drawLine(140, 380, 140, 60);  // Poste vertical más alto
            g2d.drawLine(140, 60, 220, 60);  // Travesaño superior más largo
            g2d.drawLine(220, 60, 220, 90);  // Cuerda más larga
        }
        
        // Cabeza más grande
        if (errores >= 2) {
            g2d.drawOval(195, 90, 50, 50);
        }
        
        // Cuerpo más largo
        if (errores >= 3) {
            g2d.drawLine(220, 140, 220, 280);
        }
        
        // Brazos más largos
        if (errores >= 4) {
            g2d.drawLine(220, 160, 185, 220);
        }
        
        if (errores >= 5) {
            g2d.drawLine(220, 160, 255, 220);
        }
        
        // Piernas más largas
        if (errores >= 6) {
            g2d.drawLine(220, 280, 185, 340);
        }
        
        if (errores >= 7) {
            g2d.drawLine(220, 280, 255, 340);
        }
        
        // Mostrar mensaje si se perdió con mejor diseño
        if (intentosRestantes == 0) {
            g2d.setColor(new Color(220, 20, 60));
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("💀 ¡Ahorcado!", 160, 360);
        }
        
        // Agregar título del panel
        g2d.setColor(new Color(25, 25, 112));
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("🎯 Dibujo del Ahorcado", 20, 30);
    }
    
    private void iniciarNuevoJuego() {
        try {
            // Usar directamente la lista local para evitar problemas con la API
            palabraSecreta = obtenerPokemonLocal();
            pokemonId = obtenerIdPokemon(palabraSecreta);
            
            inicializarJuego();
            actualizarInterfaz();
            
            // Habilitar todos los botones de letras
            habilitarBotonesLetras();
            
            // Ocultar imagen del Pokémon anterior
            lblImagenPokemon.setVisible(false);
            lblImagenPokemon.setIcon(null);
            
            // Resetear mensaje
            lblMensaje.setText("¡Adivina el Pokémon!");
            lblMensaje.setForeground(colorTexto);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al iniciar nuevo juego: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private String obtenerPokemonLocal() {
        String[] pokemonPrimeraGen = {
            "BULBASAUR", "IVYSAUR", "VENUSAUR", "CHARMANDER", "CHARMELEON", "CHARIZARD",
            "SQUIRTLE", "WARTORTLE", "BLASTOISE", "CATERPIE", "METAPOD", "BUTTERFREE",
            "WEEDLE", "KAKUNA", "BEEDRILL", "PIDGEY", "PIDGEOTTO", "PIDGEOT",
            "RATTATA", "RATICATE", "SPEAROW", "FEAROW", "EKANS", "ARBOK",
            "PIKACHU", "RAICHU", "SANDSHREW", "SANDLASH", "NIDORANF", "NIDORINA",
            "NIDOQUEEN", "NIDORANM", "NIDORINO", "NIDOKING", "CLEFAIRY", "CLEFABLE", 
            "VULPIX", "NINETALES", "JIGGLYPUFF", "WIGGLYTUFF", "ZUBAT", "GOLBAT",
            "ODDISH", "GLOOM", "VILEPLUME", "PARAS", "PARASECT", "VENONAT", "VENOMOTH",
            "DIGLETT", "DUGTRIO", "MEOWTH", "PERSIAN", "PSYDUCK", "GOLDUCK",
            "MANKEY", "PRIMEAPE", "GROWLITHE", "ARCANINE", "POLIWAG", "POLIWHIRL",
            "POLIWRATH", "ABRA", "KADABRA", "ALAKAZAM", "MACHOP", "MACHOKE",
            "MACHAMP", "BELLSPROUT", "WEEPINBELL", "VICTREEBEL", "TENTACOOL", "TENTACRUEL",
            "GEODUDE", "GRAVELER", "GOLEM", "PONYTA", "RAPIDASH", "SLOWPOKE",
            "SLOWBRO", "MAGNEMITE", "MAGNETON", "FARFETCHD", "DODUO", "DODRIO",
            "SEEL", "DEWGONG", "GRIMER", "MUK", "SHELLDER", "CLOYSTER",
            "GASTLY", "HAUNTER", "GENGAR", "ONIX", "DROWZEE", "HYPNO",
            "KRABBY", "KINGLER", "VOLTORB", "ELECTRODE", "EXEGGCUTE", "EXEGGUTOR",
            "CUBONE", "MAROWAK", "HITMONLEE", "HITMONCHAN", "LICKITUNG", "KOFFING",
            "WEEZING", "RHYHORN", "RHYDON", "CHANSEY", "TANGELA", "KANGASKHAN",
            "HORSEA", "SEADRA", "GOLDEEN", "SEAKING", "STARYU", "STARMIE",
            "MRMIME", "SCYTHER", "JYNX", "ELECTABUZZ", "MAGMAR", "PINSIR",
            "TAUROS", "MAGIKARP", "GYARADOS", "LAPRAS", "DITTO", "EEVEE",
            "VAPOREON", "JOLTEON", "FLAREON", "PORYGON", "OMANYTE", "OMASTAR",
            "KABUTO", "KABUTOPS", "AERODACTYL", "SNORLAX", "ARTICUNO", "ZAPDOS",
            "MOLTRES", "DRATINI", "DRAGONAIR", "DRAGONITE", "MEWTWO", "MEW"
        };
        
        Random random = new Random();
        return pokemonPrimeraGen[random.nextInt(pokemonPrimeraGen.length)];
    }
    
    private int obtenerIdPokemon(String nombrePokemon) {
        // Mapeo específico de nombres a IDs para manejar casos especiales
        switch (nombrePokemon) {
            case "BULBASAUR": return 1;
            case "IVYSAUR": return 2;
            case "VENUSAUR": return 3;
            case "CHARMANDER": return 4;
            case "CHARMELEON": return 5;
            case "CHARIZARD": return 6;
            case "SQUIRTLE": return 7;
            case "WARTORTLE": return 8;
            case "BLASTOISE": return 9;
            case "CATERPIE": return 10;
            case "METAPOD": return 11;
            case "BUTTERFREE": return 12;
            case "WEEDLE": return 13;
            case "KAKUNA": return 14;
            case "BEEDRILL": return 15;
            case "PIDGEY": return 16;
            case "PIDGEOTTO": return 17;
            case "PIDGEOT": return 18;
            case "RATTATA": return 19;
            case "RATICATE": return 20;
            case "SPEAROW": return 21;
            case "FEAROW": return 22;
            case "EKANS": return 23;
            case "ARBOK": return 24;
            case "PIKACHU": return 25;
            case "RAICHU": return 26;
            case "SANDSHREW": return 27;
            case "SANDLASH": return 28;
            case "NIDORANF": return 29; // Nidoran♀ (hembra)
            case "NIDORINA": return 30;
            case "NIDOQUEEN": return 31;
            case "NIDORANM": return 32; // Nidoran♂ (macho)
            case "NIDORINO": return 33;
            case "NIDOKING": return 34;
            case "CLEFAIRY": return 35;
            case "CLEFABLE": return 36;
            case "VULPIX": return 37;
            case "NINETALES": return 38;
            case "JIGGLYPUFF": return 39;
            case "WIGGLYTUFF": return 40;
            case "ZUBAT": return 41;
            case "GOLBAT": return 42;
            case "ODDISH": return 43;
            case "GLOOM": return 44;
            case "VILEPLUME": return 45;
            case "PARAS": return 46;
            case "PARASECT": return 47;
            case "VENONAT": return 48;
            case "VENOMOTH": return 49;
            case "DIGLETT": return 50;
            case "DUGTRIO": return 51;
            case "MEOWTH": return 52;
            case "PERSIAN": return 53;
            case "PSYDUCK": return 54;
            case "GOLDUCK": return 55;
            case "MANKEY": return 56;
            case "PRIMEAPE": return 57;
            case "GROWLITHE": return 58;
            case "ARCANINE": return 59;
            case "POLIWAG": return 60;
            case "POLIWHIRL": return 61;
            case "POLIWRATH": return 62;
            case "ABRA": return 63;
            case "KADABRA": return 64;
            case "ALAKAZAM": return 65;
            case "MACHOP": return 66;
            case "MACHOKE": return 67;
            case "MACHAMP": return 68;
            case "BELLSPROUT": return 69;
            case "WEEPINBELL": return 70;
            case "VICTREEBEL": return 71;
            case "TENTACOOL": return 72;
            case "TENTACRUEL": return 73;
            case "GEODUDE": return 74;
            case "GRAVELER": return 75;
            case "GOLEM": return 76;
            case "PONYTA": return 77;
            case "RAPIDASH": return 78;
            case "SLOWPOKE": return 79;
            case "SLOWBRO": return 80;
            case "MAGNEMITE": return 81;
            case "MAGNETON": return 82;
            case "FARFETCHD": return 83;
            case "DODUO": return 84;
            case "DODRIO": return 85;
            case "SEEL": return 86;
            case "DEWGONG": return 87;
            case "GRIMER": return 88;
            case "MUK": return 89;
            case "SHELLDER": return 90;
            case "CLOYSTER": return 91;
            case "GASTLY": return 92;
            case "HAUNTER": return 93;
            case "GENGAR": return 94;
            case "ONIX": return 95;
            case "DROWZEE": return 96;
            case "HYPNO": return 97;
            case "KRABBY": return 98;
            case "KINGLER": return 99;
            case "VOLTORB": return 100;
            case "ELECTRODE": return 101;
            case "EXEGGCUTE": return 102;
            case "EXEGGUTOR": return 103;
            case "CUBONE": return 104;
            case "MAROWAK": return 105;
            case "HITMONLEE": return 106;
            case "HITMONCHAN": return 107;
            case "LICKITUNG": return 108;
            case "KOFFING": return 109;
            case "WEEZING": return 110;
            case "RHYHORN": return 111;
            case "RHYDON": return 112;
            case "CHANSEY": return 113;
            case "TANGELA": return 114;
            case "KANGASKHAN": return 115;
            case "HORSEA": return 116;
            case "SEADRA": return 117;
            case "GOLDEEN": return 118;
            case "SEAKING": return 119;
            case "STARYU": return 120;
            case "STARMIE": return 121;
            case "MRMIME": return 122;
            case "SCYTHER": return 123;
            case "JYNX": return 124;
            case "ELECTABUZZ": return 125;
            case "MAGMAR": return 126;
            case "PINSIR": return 127;
            case "TAUROS": return 128;
            case "MAGIKARP": return 129;
            case "GYARADOS": return 130;
            case "LAPRAS": return 131;
            case "DITTO": return 132;
            case "EEVEE": return 133;
            case "VAPOREON": return 134;
            case "JOLTEON": return 135;
            case "FLAREON": return 136;
            case "PORYGON": return 137;
            case "OMANYTE": return 138;
            case "OMASTAR": return 139;
            case "KABUTO": return 140;
            case "KABUTOPS": return 141;
            case "AERODACTYL": return 142;
            case "SNORLAX": return 143;
            case "ARTICUNO": return 144;
            case "ZAPDOS": return 145;
            case "MOLTRES": return 146;
            case "DRATINI": return 147;
            case "DRAGONAIR": return 148;
            case "DRAGONITE": return 149;
            case "MEWTWO": return 150;
            case "MEW": return 151;
            default: return 1; // Fallback a Bulbasaur
        }
    }
    
    private void inicializarJuego() {
        palabraOculta = new char[palabraSecreta.length()];
        for (int i = 0; i < palabraOculta.length; i++) {
            palabraOculta[i] = '_';
        }
        intentosRestantes = MAX_INTENTOS;
        letrasUsadas = new ArrayList<>();
    }
    
    private void procesarLetra(char letra) {
        if (letrasUsadas.contains(letra)) {
            lblMensaje.setText("⚠️ Ya usaste esa letra");
            lblMensaje.setForeground(Color.ORANGE);
            return;
        }
        
        letrasUsadas.add(letra);
        
        // Deshabilitar el botón de la letra
        Component[] botones = panelLetras.getComponents();
        for (Component comp : botones) {
            if (comp instanceof JPanel) {
                Component[] subBotones = ((JPanel) comp).getComponents();
                for (Component subComp : subBotones) {
                    if (subComp instanceof JButton) {
                        JButton btn = (JButton) subComp;
                        if (btn.getText().equals(String.valueOf(letra))) {
                            btn.setEnabled(false);
                            break;
                        }
                    }
                }
            }
        }
        
        boolean letraEncontrada = false;
        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letra) {
                palabraOculta[i] = letra;
                letraEncontrada = true;
            }
        }
        
        if (letraEncontrada) {
            lblMensaje.setText("✅ ¡Correcto! La letra '" + letra + "' está en la palabra");
            lblMensaje.setForeground(colorExito);
        } else {
            lblMensaje.setText("❌ La letra '" + letra + "' no está en la palabra");
            lblMensaje.setForeground(colorError);
            intentosRestantes--;
        }
        
        actualizarInterfaz();
        
        // Verificar si el juego terminó
        if (palabraCompleta()) {
            mostrarVictoria();
        } else if (intentosRestantes == 0) {
            mostrarDerrota();
        }
    }
    
    private boolean palabraCompleta() {
        for (char c : palabraOculta) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }
    
    private void mostrarVictoria() {
        lblMensaje.setText("🎉 ¡FELICIDADES! ¡Has adivinado el Pokémon!");
        lblMensaje.setForeground(colorExito);
        
        // Mostrar imagen del Pokémon
        cargarImagenPokemon();
        
        // Deshabilitar todos los botones de letras
        deshabilitarBotonesLetras();
        
        JOptionPane.showMessageDialog(this, 
            "🎉 ¡FELICIDADES! 🎉\n\n" +
            "Has adivinado el Pokémon: " + palabraSecreta + "\n" +
            "¡Eres un verdadero maestro Pokémon!",
            "¡Victoria!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarDerrota() {
        lblMensaje.setText("💀 ¡Game Over! El Pokémon era: " + palabraSecreta);
        lblMensaje.setForeground(colorError);
        
        // Deshabilitar todos los botones de letras
        deshabilitarBotonesLetras();
        
        JOptionPane.showMessageDialog(this, 
            "💀 ¡Game Over! 💀\n\n" +
            "El Pokémon era: " + palabraSecreta + "\n" +
            "¡Mejor suerte la próxima vez!",
            "Derrota", JOptionPane.ERROR_MESSAGE);
    }
    
    private void cargarImagenPokemon() {
        try {
            String imageUrl = POKEAPI_SPRITES_URL + pokemonId + ".png";
            URL url = new URL(imageUrl);
            BufferedImage imagen = ImageIO.read(url);
            
            if (imagen != null) {
                // Redimensionar la imagen a tamaño más grande
                Image imagenRedimensionada = imagen.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                ImageIcon icono = new ImageIcon(imagenRedimensionada);
                lblImagenPokemon.setIcon(icono);
                lblImagenPokemon.setVisible(true);
            }
            
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen del Pokémon: " + e.getMessage());
            // Mostrar un icono por defecto
            lblImagenPokemon.setIcon(new ImageIcon());
            lblImagenPokemon.setText("🖼️ Imagen no disponible");
            lblImagenPokemon.setVisible(true);
        }
    }

    private ImageIcon cargarIcono(String urlStr, int width, int height) {
        try {
            URL url = new URL(urlStr);
            BufferedImage img = ImageIO.read(url);
            if (img != null) {
                Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (IOException e) {
            // Ignorar, devolver icono vacío
        }
        return new ImageIcon();
    }
    
    private void deshabilitarBotonesLetras() {
        Component[] botones = panelLetras.getComponents();
        for (Component comp : botones) {
            if (comp instanceof JPanel) {
                Component[] subBotones = ((JPanel) comp).getComponents();
                for (Component subComp : subBotones) {
                    if (subComp instanceof JButton) {
                        ((JButton) subComp).setEnabled(false);
                    }
                }
            }
        }
    }
    
    private void habilitarBotonesLetras() {
        Component[] botones = panelLetras.getComponents();
        for (Component comp : botones) {
            if (comp instanceof JPanel) {
                Component[] subBotones = ((JPanel) comp).getComponents();
                for (Component subComp : subBotones) {
                    if (subComp instanceof JButton) {
                        ((JButton) subComp).setEnabled(true);
                    }
                }
            }
        }
    }
    
    private void actualizarInterfaz() {
        // Actualizar palabra oculta
        StringBuilder palabraMostrar = new StringBuilder();
        for (int i = 0; i < palabraOculta.length; i++) {
            palabraMostrar.append(palabraOculta[i]);
            if (i < palabraOculta.length - 1) {
                palabraMostrar.append(" ");
            }
        }
        lblPalabra.setText(palabraMostrar.toString());
        
        // Actualizar intentos
        lblIntentos.setText("Intentos restantes: " + intentosRestantes);
        
        // Actualizar letras usadas
        StringBuilder letrasMostrar = new StringBuilder("Letras usadas: ");
        for (char letra : letrasUsadas) {
            letrasMostrar.append(letra).append(" ");
        }
        lblLetrasUsadas.setText(letrasMostrar.toString());
        
        // Repintar el ahorcado
        panelAhorcado.repaint();
    }
    
    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos
        SwingUtilities.invokeLater(() -> {
            new AhorcadoPokemonGUI().setVisible(true);
        });
    }
}
