import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculadora extends JFrame implements ActionListener {
    private JTextField pantalla;
    private String operacion = "";
    private double primerNumero = 0;
    private boolean nuevaOperacion = true;
    
    public Calculadora() {
        configurarVentana();
        crearComponentes();
        agregarComponentes();
    }
    
    private void configurarVentana() {
        setTitle("🧮 Calculadora Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250)); // Fondo suave de la ventana
    }
    
    private void crearComponentes() {
        // Crear la pantalla
        pantalla = new JTextField();
        pantalla.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setEditable(false);
        pantalla.setBackground(new Color(248, 249, 250)); // Gris muy claro
        pantalla.setForeground(new Color(33, 37, 41)); // Gris oscuro para mejor contraste
        pantalla.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Crear el panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5, 4, 8, 8));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelBotones.setBackground(new Color(240, 242, 245)); // Fondo suave del panel
        
        // Crear los botones
        String[] textoBotones = {
            "C", "CE", "←", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "±", "0", ".", "="
        };
        
        for (String texto : textoBotones) {
            JButton boton = new JButton(texto);
            boton.addActionListener(this);
            boton.setFocusPainted(false); // Quitar el borde de enfoque
            
            // Todos los textos en negro para máxima legibilidad
            boton.setForeground(Color.BLACK);
            
            if (texto.equals("C") || texto.equals("CE")) {
                // Botones de limpieza - fondo claro para contraste con texto negro
                boton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Fuente más grande para C y CE
                boton.setBackground(new Color(255, 200, 200)); // Rosa claro
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 100, 100), 3), // Marco rojo más intenso
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
                ));
            } else if (texto.equals("=")) {
                // Botón igual - fondo claro para contraste con texto negro
                boton.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Fuente más grande para =
                boton.setBackground(new Color(200, 220, 255)); // Azul claro
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 150, 200), 3), // Marco azul más intenso
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
                ));
            } else if (texto.matches("[÷×+-]")) {
                // Operadores - fondo amarillo claro
                boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
                boton.setBackground(new Color(255, 255, 200)); // Amarillo muy claro
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 180, 50), 3), // Marco amarillo más intenso
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
                ));
            } else {
                // Números y otros - fondo blanco
                boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
                boton.setBackground(Color.WHITE);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
            
            // Efecto hover suave
            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    Color colorOriginal = boton.getBackground();
                    boton.setBackground(colorOriginal.darker());
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    // Restaurar color original según el tipo de botón
                    if (texto.equals("C") || texto.equals("CE")) {
                        boton.setBackground(new Color(255, 200, 200)); // Rosa claro
                    } else if (texto.equals("=")) {
                        boton.setBackground(new Color(200, 220, 255)); // Azul claro
                    } else if (texto.matches("[÷×+-]")) {
                        boton.setBackground(new Color(255, 255, 200)); // Amarillo muy claro
                    } else {
                        boton.setBackground(Color.WHITE);
                    }
                }
            });
            
            panelBotones.add(boton);
        }
        
        // Agregar componentes al frame
        add(pantalla, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
    }
    
    private void agregarComponentes() {
        // Los componentes ya se agregaron en crearComponentes()
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        
        switch (comando) {
            case "C":
                limpiarTodo();
                break;
            case "CE":
                limpiarEntrada();
                break;
            case "←":
                borrarUltimo();
                break;
            case "÷":
            case "×":
            case "-":
            case "+":
                manejarOperacion(comando);
                break;
            case "=":
                calcularResultado();
                break;
            case "±":
                cambiarSigno();
                break;
            case ".":
                agregarDecimal();
                break;
            default:
                agregarNumero(comando);
                break;
        }
    }
    
    private void limpiarTodo() {
        pantalla.setText("0");
        operacion = "";
        primerNumero = 0;
        nuevaOperacion = true;
    }
    
    private void limpiarEntrada() {
        pantalla.setText("0");
        nuevaOperacion = true;
    }
    
    private void borrarUltimo() {
        String texto = pantalla.getText();
        if (texto.length() > 1) {
            pantalla.setText(texto.substring(0, texto.length() - 1));
        } else if (texto.equals("0")) {
            // Si solo hay "0", no hacer nada (no cambiar a vacío)
            return;
        } else {
            // Si está vacío o tiene solo un carácter que no es "0", poner "0"
            pantalla.setText("0");
        }
    }
    
    private void manejarOperacion(String op) {
        if (!nuevaOperacion) {
            calcularResultado();
        }
        operacion = op;
        primerNumero = Double.parseDouble(pantalla.getText());
        nuevaOperacion = true;
    }
    
    private void calcularResultado() {
        if (!operacion.isEmpty() && !nuevaOperacion) {
            double segundoNumero = Double.parseDouble(pantalla.getText());
            double resultado = 0;
            
            switch (operacion) {
                case "+":
                    resultado = primerNumero + segundoNumero;
                    break;
                case "-":
                    resultado = primerNumero - segundoNumero;
                    break;
                case "×":
                    resultado = primerNumero * segundoNumero;
                    break;
                case "÷":
                    if (segundoNumero != 0) {
                        resultado = primerNumero / segundoNumero;
                    } else {
                        pantalla.setText("Error");
                        return;
                    }
                    break;
            }
            
            // Formatear el resultado
            if (resultado == (long) resultado) {
                pantalla.setText(String.valueOf((long) resultado));
            } else {
                pantalla.setText(String.valueOf(resultado));
            }
            
            operacion = "";
            nuevaOperacion = true;
        }
    }
    
    private void cambiarSigno() {
        String texto = pantalla.getText();
        if (!texto.equals("0")) {
            if (texto.startsWith("-")) {
                pantalla.setText(texto.substring(1));
            } else {
                pantalla.setText("-" + texto);
            }
        }
    }
    
    private void agregarDecimal() {
        String texto = pantalla.getText();
        if (nuevaOperacion) {
            pantalla.setText("0.");
            nuevaOperacion = false;
        } else if (!texto.contains(".")) {
            pantalla.setText(texto + ".");
        }
    }
    
    private void agregarNumero(String numero) {
        String texto = pantalla.getText();
        
        if (nuevaOperacion || texto.equals("0")) {
            pantalla.setText(numero);
            nuevaOperacion = false;
        } else {
            pantalla.setText(texto + numero);
        }
    }
    
    public static void main(String[] args) {
        // Configurar el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Crear y mostrar la calculadora
        SwingUtilities.invokeLater(() -> {
            new Calculadora().setVisible(true);
        });
    }
}
