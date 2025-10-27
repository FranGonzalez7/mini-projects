# Calculadora Java con Interfaz Gráfica

Una calculadora completa desarrollada en Java con interfaz gráfica usando Swing.

## Características

- ✅ Interfaz gráfica moderna y fácil de usar
- ✅ Operaciones básicas: suma (+), resta (-), multiplicación (×), división (÷)
- ✅ Botones especiales:
  - **C**: Limpiar todo
  - **CE**: Limpiar entrada actual
  - **⌫**: Borrar último carácter
  - **±**: Cambiar signo
  - **.**: Punto decimal
  - **=**: Calcular resultado
- ✅ Manejo de errores (división por cero)
- ✅ Formato automático de números enteros y decimales
- ✅ Diseño responsive con colores distintivos

## Requisitos

- Java 8 o superior
- No se requieren librerías externas

## Cómo ejecutar

1. Compila el archivo Java:
```bash
javac Calculadora.java
```

2. Ejecuta la aplicación:
```bash
java Calculadora
```

## Funcionalidades

### Operaciones Básicas
- Suma: `5 + 3 = 8`
- Resta: `10 - 4 = 6`
- Multiplicación: `7 × 6 = 42`
- División: `15 ÷ 3 = 5`

### Botones Especiales
- **C (Clear)**: Borra todo y reinicia la calculadora
- **CE (Clear Entry)**: Borra solo la entrada actual
- **⌫ (Backspace)**: Elimina el último dígito ingresado
- **± (Sign Change)**: Cambia el signo del número actual
- **.**: Agrega punto decimal
- **=**: Calcula el resultado de la operación

### Características Técnicas
- Interfaz desarrollada con Java Swing
- Manejo de eventos con ActionListener
- Diseño con GridLayout para organización de botones
- Colores distintivos para diferentes tipos de botones
- Manejo de errores para operaciones inválidas
- Formato automático de números (enteros sin decimales innecesarios)

## Estructura del Código

- `Calculadora.java`: Clase principal que contiene toda la lógica de la aplicación
- Implementa `ActionListener` para manejar eventos de botones
- Métodos organizados por funcionalidad:
  - Configuración de ventana
  - Creación de componentes
  - Manejo de operaciones matemáticas
  - Funciones auxiliares

## Personalización

Puedes modificar fácilmente:
- Colores de los botones cambiando los valores RGB
- Tamaño de la ventana ajustando `setSize()`
- Fuente y tamaño del texto modificando `Font`
- Distribución de botones alterando el `GridLayout`

¡Disfruta usando tu calculadora Java! 🧮
