#!/bin/bash
# Script para compilar y ejecutar MazeHunter con patrón MVC

echo "🏗️  Compilando MazeHunter..."
javac -cp "lib/gson-2.10.1.jar" -d out -sourcepath src $(find src -name "*.java")

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa!"
    echo ""
    echo "🎮 Ejecutando MazeHunter..."
    echo "================================"
    java -cp "out:lib/gson-2.10.1.jar" Main.Main
else
    echo "❌ Error en la compilación"
    exit 1
fi
