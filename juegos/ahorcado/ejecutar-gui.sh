#!/bin/bash
echo "Compilando Ahorcado Pokemon GUI..."
javac AhorcadoPokemonGUI.java
if [ $? -eq 0 ]; then
    echo "Compilación exitosa!"
    echo "Ejecutando el juego con interfaz gráfica..."
    java AhorcadoPokemonGUI
else
    echo "Error en la compilación."
    read -p "Presiona Enter para continuar..."
fi
