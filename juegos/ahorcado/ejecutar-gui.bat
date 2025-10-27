@echo off
echo Compilando Ahorcado Pokemon GUI...
javac AhorcadoPokemonGUI.java
if %errorlevel% equ 0 (
    echo Compilacion exitosa!
    echo Ejecutando el juego con interfaz grafica...
    java AhorcadoPokemonGUI
) else (
    echo Error en la compilacion.
    pause
)
