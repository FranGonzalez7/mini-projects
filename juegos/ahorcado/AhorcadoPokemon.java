import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

//Ahorcado Pokemon
public class AhorcadoPokemon {
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final int PRIMERA_GENERACION_MAX = 151;
    private static final int MAX_INTENTOS = 6;
    
    private String palabraSecreta;
    private char[] palabraOculta;
    private List<Character> letrasUsadas;
    private int intentosRestantes;
    private Scanner scanner;
    
    public AhorcadoPokemon() {
        this.letrasUsadas = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }
    
    public static void main(String[] args) {
        AhorcadoPokemon juego = new AhorcadoPokemon();
        juego.iniciarJuego();
    }
    
    public void iniciarJuego() {
        System.out.println("🎮 ¡Bienvenido al Ahorcado Pokémon! 🎮");
        System.out.println("Adivina el nombre del Pokémon de la primera generación");
        System.out.println("Tienes " + MAX_INTENTOS + " intentos para adivinar la palabra.");
        System.out.println("=====================================");
        
        try {
            // Obtener un Pokémon aleatorio de la primera generación
            palabraSecreta = obtenerPokemonAleatorio();
            if (palabraSecreta == null) {
                System.out.println("❌ Error al obtener el Pokémon. Usando lista local...");
                palabraSecreta = obtenerPokemonLocal();
            }
            
            inicializarJuego();
            jugar();
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            System.out.println("Usando lista local de Pokémon...");
            palabraSecreta = obtenerPokemonLocal();
            inicializarJuego();
            jugar();
        }
    }
    
    private String obtenerPokemonAleatorio() {
        try {
            Random random = new Random();
            int pokemonId = random.nextInt(PRIMERA_GENERACION_MAX) + 1;
            
            URL url = new URL(POKEAPI_BASE_URL + pokemonId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "AhorcadoPokemon/1.0");
            
            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
                );
                
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                // Extraer el nombre del JSON (método simple)
                String json = response.toString();
                int nameIndex = json.indexOf("\"name\":\"") + 8;
                int nameEndIndex = json.indexOf("\"", nameIndex);
                String nombre = json.substring(nameIndex, nameEndIndex);
                
                return nombre.toUpperCase();
            }
            
        } catch (IOException e) {
            System.out.println("⚠️ No se pudo conectar con la API: " + e.getMessage());
        }
        
        return null;
    }
    
    private String obtenerPokemonLocal() {
        String[] pokemonPrimeraGen = {
            "BULBASAUR", "IVYSAUR", "VENUSAUR", "CHARMANDER", "CHARMELEON", "CHARIZARD",
            "SQUIRTLE", "WARTORTLE", "BLASTOISE", "CATERPIE", "METAPOD", "BUTTERFREE",
            "WEEDLE", "KAKUNA", "BEEDRILL", "PIDGEY", "PIDGEOTTO", "PIDGEOT",
            "RATTATA", "RATICATE", "SPEAROW", "FEAROW", "EKANS", "ARBOK",
            "PIKACHU", "RAICHU", "SANDSHREW", "SANDLASH", "NIDORAN", "NIDORINA",
            "NIDOQUEEN", "NIDORINO", "NIDOKING", "CLEFAIRY", "CLEFABLE", "VULPIX",
            "NINETALES", "JIGGLYPUFF", "WIGGLYTUFF", "ZUBAT", "GOLBAT", "ODDISH",
            "GLOOM", "VILEPLUME", "PARAS", "PARASECT", "VENONAT", "VENOMOTH",
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
    
    private void inicializarJuego() {
        palabraOculta = new char[palabraSecreta.length()];
        for (int i = 0; i < palabraOculta.length; i++) {
            palabraOculta[i] = '_';
        }
        intentosRestantes = MAX_INTENTOS;
        letrasUsadas.clear();
    }
    
    private void jugar() {
        while (intentosRestantes > 0 && !palabraCompleta()) {
            mostrarEstado();
            char letra = pedirLetra();
            
            if (letrasUsadas.contains(letra)) {
                System.out.println("⚠️ Ya usaste esa letra. Intenta con otra.");
                continue;
            }
            
            letrasUsadas.add(letra);
            
            if (palabraSecreta.contains(String.valueOf(letra))) {
                System.out.println("✅ ¡Correcto! La letra '" + letra + "' está en la palabra.");
                revelarLetra(letra);
            } else {
                System.out.println("❌ La letra '" + letra + "' no está en la palabra.");
                intentosRestantes--;
                dibujarAhorcado();
            }
        }
        
        mostrarResultado();
    }
    
    private void mostrarEstado() {
        System.out.println("\n" + repetirCaracter('=', 50));
        System.out.println("Palabra: " + String.valueOf(palabraOculta));
        System.out.println("Intentos restantes: " + intentosRestantes);
        System.out.println("Letras usadas: " + letrasUsadas);
        System.out.println(repetirCaracter('=', 50));
    }
    
    private char pedirLetra() {
        System.out.print("Ingresa una letra: ");
        String entrada = scanner.nextLine().toUpperCase().trim();
        
        while (entrada.length() != 1 || !Character.isLetter(entrada.charAt(0))) {
            System.out.print("Por favor, ingresa una sola letra válida: ");
            entrada = scanner.nextLine().toUpperCase().trim();
        }
        
        return entrada.charAt(0);
    }
    
    private void revelarLetra(char letra) {
        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letra) {
                palabraOculta[i] = letra;
            }
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
    
    private void dibujarAhorcado() {
        System.out.println("\n🎯 Dibujo del ahorcado:");
        String[] dibujo = {
            "  +---+",
            "  |   |",
            "  O   |",
            " /|\\  |",
            " / \\  |",
            "      |",
            "========="
        };
        
        int lineasAMostrar = MAX_INTENTOS - intentosRestantes;
        for (int i = 0; i < Math.min(lineasAMostrar, dibujo.length); i++) {
            System.out.println(dibujo[i]);
        }
        
        if (intentosRestantes == 0) {
            System.out.println("💀 ¡Ahorcado!");
        }
    }
    
    private void mostrarResultado() {
        System.out.println("\n" + repetirCaracter('=', 50));
        if (palabraCompleta()) {
            System.out.println("🎉 ¡FELICIDADES! ¡Has adivinado el Pokémon!");
            System.out.println("El Pokémon era: " + palabraSecreta);
            System.out.println("¡Eres un verdadero maestro Pokémon!");
        } else {
            System.out.println("💀 ¡Game Over!");
            System.out.println("El Pokémon era: " + palabraSecreta);
            System.out.println("¡Mejor suerte la próxima vez!");
        }
        System.out.println(repetirCaracter('=', 50));
        
        System.out.print("\n¿Quieres jugar de nuevo? (s/n): ");
        String respuesta = scanner.nextLine().toLowerCase().trim();
        if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
            System.out.println("\n");
            iniciarJuego();
        } else {
            System.out.println("¡Gracias por jugar! ¡Hasta la próxima!");
        }
    }
    
    private String repetirCaracter(char caracter, int veces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            sb.append(caracter);
        }
        return sb.toString();
    }
}
