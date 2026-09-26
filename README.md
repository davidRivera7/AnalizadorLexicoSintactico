
```markdown
# 🔍 Analizador Léxico y Sintáctico (Compilador LL(1))

Proyecto desarrollado para la materia de **Autómatas I**, enfocado en la construcción de un compilador modular en **Java** capaz de validar si un programa fuente cumple estrictamente con las reglas léxicas y sintácticas de una gramática formal[cite: 3].

---

## 🎯 Acerca del Proyecto

Este software procesa un archivo de entrada (`programa.txt`)[cite: 3] y evalúa su conformidad mediante un **Análisis Sintáctico Predictivo Tabular (LL(1))** impulsado por el algoritmo **LIDriver**[cite: 2]. A diferencia de los analizadores tradicionales, el componente léxico opera **bajo demanda**: entrega únicamente un token a la vez conforme el parser lo solicita, manteniendo una **Tabla de Símbolos** optimizada con identificadores únicos y atributos específicos[cite: 2, 3].

---

## 📐 Gramática del Lenguaje (LC)

El compilador opera basándose en una gramática formal de 28 producciones que soporta la declaración de clases, tipos de datos primitivos (`int`, `float`), asignaciones aritméticas complejas, y sentencias de entrada/salida (`read` y `write`)[cite: 3]:

```text
1. programa      → class id { lista_sent }
2. lista_sent    → sentencia sent_final
3. sent_final    → sentencia sent_final | ε
5. sentencia     → tipo lista_id; | id = expresion ; | read ( lista_id ) ; | write ( lista_expr ) ;
9. lista_id      → id id_final
12. lista_expr   → expresion lista_exprfinal
15. expresion    → expr_arit expr_final
18. expr_arit    → ( expresion ) | id | enteros | reales
22. tipo         → int | float
24. operador     → + | - | * | /
28. inicio       → programa $

```

---

## ⚙️ Arquitectura y Funcionamiento Interno

El motor de análisis combina tres elementos clave coordinados en tiempo de ejecución:

1. **Estructura de Pila Dinámica ($X$):** Rastrea las acciones pendientes y el orden jerárquico de derivación impuesto por la gramática.


2. **Token Actual ($A$):** Representa el componente léxico extraído del código fuente en curso.


3. **Matriz Predictiva:** Tabla de doble entrada que relaciona los símbolos no terminales con los tokens de entrada para determinar exactamente qué regla gramatical aplicar en cada paso.



Durante cada ciclo de ejecución, la consola muestra en tiempo real:

* El símbolo actual en la Pila ($X$) y el token analizado ($A$).


* La producción de derivación activa.


* El estado actualizado de la **Tabla de Símbolos** (registrando lexemas, atributos y categorías léxicas).



---

## 🧪 Pruebas y Casos de Uso

El compilador maneja de manera robusta tanto código válido como la detección precisa de errores léxicos y sintácticos:

### 1. Caso Válido (Estructura Compleja)

* **Entrada (`programa.txt`):**

```java
class ejemplo {
    int var1;
    var1 = (10 + 1) / 2;
    float decimal;
    read (var1); write (var1);
    decimal = ((10.1 - 2) * 5) / 100.4;
}

```


* **Salida en consola:**

```text
LA PILA ESTÁ VACÍA
EL PROGRAMA ES LÉXICA Y SINTÁCTICAMENTE CORRECTO

```



### 2. Detección de Errores (Léxicos y Sintácticos)

* **Error Léxico:** Detecta identificadores inválidos que comienzan con números (ej. `1ejemplo`).


* **Error Sintáctico:** Reporta fallas de estructura ante palabras reservadas mal empleadas (ej. `float int;`) o sintaxis no contempladas por la gramática (ej. inicializaciones directas en la declaración como `int var1 = 10;`).



---

## 🚀 Guía de Instalación y Ejecución

Sigue estos pasos para clonar el repositorio y ejecutar el proyecto:

1. **Clonar el repositorio:**
```bash
git clone [https://github.com/tu-usuario/AnalizadorLexicoSintactico.git](https://github.com/tu-usuario/AnalizadorLexicoSintactico.git)
cd AnalizadorLexicoSintactico

```


2. **Ejecutar el proyecto:**
* Mediante **Apache Ant**:
```bash
ant run

```


* O de forma manual mediante terminal de Java:
```bash
javac -encoding UTF-8 -d build/classes -sourcepath src src/main/Main.java
java -Dfile.encoding=UTF-8 -cp build/classes main.Main

```





---

## 💡 Competencias Demostradas

* Diseño e implementación de compiladores e intérpretes basados en teoría de autómatas.
* Manejo de tablas de símbolos dinámicas y análisis léxico bajo demanda.
* Aplicación práctica de gramáticas libres de contexto y tablas de parsing LL(1).

```