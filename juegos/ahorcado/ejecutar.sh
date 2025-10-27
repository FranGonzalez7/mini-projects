#!/bin/bash
echo "Compilando Ahorcado Pokemon..."
javac AhorcadoPokemon.java
if [ $? -eq 0 ]; then
    echo "Compilación exitosa!"
    echo "Ejecutando el juego..."
    java AhorcadoPokemon
else
    echo "Error en la compilación."
    read -p "Presiona Enter para continuar..."
fi
