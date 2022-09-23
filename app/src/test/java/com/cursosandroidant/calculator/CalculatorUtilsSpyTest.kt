package com.cursosandroidant.calculator

import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CalculatorUtilsSpyTest {
    //Con Spy nos evitamos de indicar los valores que deben retornar los metodos getOperator y divideOperation como lo que hicimos con CalculatorUtilsMockTest
    //de la lineas 133 y 135 los cuales son:
    //`when`(operations.getOperator(operation)).thenReturn("x")
    //`when`(operations.divideOperation(operator, operation)).thenReturn(arrayOf("3.5","2"))
    @Spy
    lateinit var operations: Operations
    @Mock
    lateinit var listener: OnResolveListener
    lateinit var calculatorUtils: CalculatorUtils

    @Before
    fun setup(){
        calculatorUtils = CalculatorUtils(operations, listener)
    }

    @Test
    fun calc_callAddPoint_validSecondPoint_noReturns(){
        val operation = "3.5x2"
        val operator = "x"
        var isCorrect = false

        calculatorUtils.addPoint(operation) {
            isCorrect = true
        }
        assertTrue(isCorrect)

        verify(operations).getOperator(operation)
        verify(operations).divideOperation(operator,operation)
    }

    @Test
    fun calc_callAddPoint_invalidSecondPoint_noReturns() {
        val operation = "3.5x2."
        val operator = "x"
        var isCorrect = false

        calculatorUtils.addPoint(operation) {
            isCorrect = true
        }
        assertFalse(isCorrect)

        //Con esto nos ayuda a conocer si se ha ejecutado un metodo o variables que ha tenido interacción dentro de un proceso en especifico
        verify(operations).getOperator(operation)
        verify(operations).divideOperation(operator,operation)
    }
}