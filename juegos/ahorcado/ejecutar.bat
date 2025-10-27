@echo off
echo Compilando Ahorcado Pokemon...
javac AhorcadoPokemon.java
if %errorlevel% equ 0 (
    echo Compilacion exitosa!
    echo Ejecutando el juego...
    java AhorcadoPokemon
) else (
    echo Error en la compilacion.
    pause
)
