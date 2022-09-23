package com.cursosandroidant.calculator

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

//Mockito nos ayuda hacer testing que deriben de dependencias de las clases a testear
@RunWith(MockitoJUnitRunner::class)
class CalculatorUtilsMockTest{
    //Se declaran las variables que dependen de CalculatorUtils para funcionar los cuales son las siguientes clases
    //Operations y OnResolveListener
    //Estas son las clases complementarias de la clase calculatorUtils que seran mockeadas, es decir que las clases van simular o imitar el contexto
    //de si implementación
    @Mock
    lateinit var operations: Operations
    @Mock
    lateinit var listener: OnResolveListener

    //Esta es la clase que queremos testear pero no necesita la anotación de mockito ya que no queremos simular su contexto si no
    //que esta es la clase principal que estaremos testeando como lo haciamos con jUnit
    lateinit var calculatorUtils: CalculatorUtils

    @Before
    fun setup(){
        //Calculator utils es necesario instanciarlo y le pondremos las variables de operations y listener,
        //recordemos que esas variables simulan las clases para que pueda funcionar y al final estamos simulando esta linea de codigo
        // que se encuentra en CalculatorUtils en la linea 13
        //class CalculatorUtils(private val operations: Operations, private val listener: OnResolveListener) {
        calculatorUtils = CalculatorUtils(operations, listener)
    }

    //Si esta trabajando con clases de tipo objeto puede tirar error por que se considera una clase final o estatica
    //por lo que es necesario importar una libreria de mockito llamada testImplementation("org.mockito:mockito-inline:4.8.0")
    @Test
    fun cacl_callCheckOrResolve_noReturn(){
        val operation = "-5x2.5"
        val isFromResolve = true
        calculatorUtils.checkOrResolve(operation, isFromResolve)
        verify(operations).tryResolve(operation, isFromResolve, listener)
    }

    @Test
    fun calc_callAddOperator_validSub_noReturn(){
        //Indicamos las variables que se utilizarán en el metodo addOperator
        //la variable operator es la indica el operador que se utilizará en la expresion matematica
        val operator = "-"
        //la variable operation es en si la operación actual
        val operation = "4+"
        //La idea es comprobar si la operación sub es valido, es decir si tenemos esta expresion matematica
        //4+- , esto quiere decir que es correcto la expresión
        //pero no nos debe de permitir poner otro signo de menos (-) por que ya no es correcto
        //y para verificar eso creamos una variable en el cual le asignaremos un false
        var isCorrect = false
        //esto es por que si el metodo addOperator se ejecuta correctamente la variable isCorrect nos devolvera un true
        calculatorUtils.addOperator(operator,operation){
            isCorrect = true
        }
        //entonces aqui validamos si la afirmación es correcta o verdadera y si es asi se cumple el test
        assertTrue(isCorrect)
    }

    //Pero en este test forzamos el agregar otro signo de mas para invalidar la operación sub
    //por lo que quedaria asi la expresion matematica 4+-- y esto no es correcto, es invalido
    @Test
    fun calc_callAddOperator_invalidSub_noReturn(){
        //Given, dado estos valores
        val operator = "-"
        val operation = "4+-"
        var isCorrect = false
        //por lo que la función addOperator no se cumple por lo que hace que no entre adentro del metodo
        //y si no entra no se reasigna la variable isCorrect con el valor de true por lo que se queda con el false
        //que se le habia asignado al principio

        //When, cuando es llamado esta operación
        calculatorUtils.addOperator(operator,operation){
            isCorrect = true
        }
        //entonces la afirmación debe ser falsa por que se invalido la operación y si es false se cumple el test
        //Then, entonces se espera que ocurra esto
        assertFalse(isCorrect)
    }

    //Lo que queremos validar con este test es de que la expresion matematica no tenga un punto y lo que queremos es verificar
    //si se puede añadir un punto siendo este el primer punto de un numero osea que el numero debe de ser asi 2 y no 2.0 ya que
    //el 2.0 ya tiene un punto y no es posible ponerle otro punto, no podemos hacer 2.0. es invalido por lo que el test
    //solo debe de validar de que se pueda agregar el primer punto y si se puede nos debe de regresar un true
    @Test
    fun calc_callAddPoint_firstPoint_noReturns(){
        val operation = "3x2"
        var isCorrect =  false
        calculatorUtils.addPoint(operation){
            isCorrect = true
        }
        assertTrue(isCorrect)
        //Aqui lo que hacemos es que no haya interacciones con operaciones, es decir, en la clase CalculatorUtils
        //tenemos el metodo addPoint lo cual es el metodo que estamos analizando con mockito, entonces, en el metodo
        //valida primero si la operación no contiene un punto y si esto es correcto se ejecuta un bloque de codigo que vendria
        //siendo un callback o una respuesta proveniente de este bloque, y dentro de ese bloque no debe existir alguna interacción
        //con la clase instanciada de operations y es por eso que se hace esta comprobación adicional para ser mas certeros en la prueba
        //en caso de que se haya pasado a otro bloque de codigo en donde valida de que ya tiene un punto, aqui si hay interacciones
        //con la clase instanciada como lo podemos observar en el metodo addPoint de la clase CalculatorUtils en la linea 37 y 38

        verifyNoInteractions(operations)
    }

    //pero en este test ya empezamos a validar si hay otro punto, pero ojo esto valida el primer numero y que despues del operador valida el segundo numero
    //es decir 5.5 + 3 -> aqui deberia de poner otro punto en el 3 ya que aun que haya puesto un punto no significa que no puedo poner otro punto
    //ya que no es que este evaluando toda la expresion completa si no fragmentos de ella, por lo que se valida de que la expresion no pueda hacer esto
    // 1.2.3 + 2.1 -> el 1.2.3 no es valido ya que tiene dos puntos, a esto nos referimos, estamos evaluando si ya hay un punto en la primera expresion y si
    //despues tiene un operador deberiamos de poder poner el segundo punto, ya esto no ayudará a determinar si es posible o no agregar un segundo punto
    @Test
    fun calc_callAddPoint_secondPoint_noReturns() {
        val operation = "3.5x2"
        val operator = "x"
        var isCorrect = false

        //Necesitamos asignarles los valores de forma directa a los metodos que dependen del metodo principal que es addPoint con la palabra
        //reservada `when` nos permite hacer eso, ya que recordemos que al entrar a este bloque de validación existen relaciones con la clase operations
        //dadas en las en las lineas 37 y 38 de la clase CalculatorUtils
        /*
        SON REQUERIDAS ESTAS LLAMADAS PARA PODER HACER FUNCIONAR NUESTRO TEST
        * val operator = operations.getOperator(operation)
          val values = operations.divideOperation(operator, operation)
        *
        * por lo tanto para simular su respuesta con mokito necesitamos esta palabra reservada `when` para utilizar las llamadas de los metodos getOperator y divideOperation*/

        //interprentando esta linea de codigo:
        //cuando obtengamos el operador dentro de la operación entonces regresanos signo de x
        `when`(operations.getOperator(operation)).thenReturn("x")
        //cuando obtengamos el operador definido y la operación entonces regresanos un array de los dos numeros que tiene esa expresión matematica
        `when`(operations.divideOperation(operator, operation)).thenReturn(arrayOf("3.5","2"))

        //Con esto cumplimos los requerimientos de los metodos dependientes para poder continuar con nuestro test
        //que viene siendo a continuación, de que valida si se puede agregar otro punto o no
        calculatorUtils.addPoint(operation) {
            isCorrect = true
        }
        assertTrue(isCorrect)
        //aqui verificamos si estan mandando a llamar el metodo de getOperator o divideOperation dentro de las operaciones
        verify(operations).getOperator(operation)
        verify(operations).divideOperation(operator,operation)
    }
}