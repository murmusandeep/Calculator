package com.example.calci

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class KotlinMainActivity : AppCompatActivity(), View.OnClickListener{

    private val mTextView: TextView? = null

    private var mNumbers: String  = ""
    private var str2: String  = ""

    private var expression: String = "expression"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            val expr = savedInstanceState.getString(expression).toString()
            if (mTextView != null) {
                mTextView.setText(expr)
            }
        }

        btn_one.setOnClickListener(this)
        btn_two.setOnClickListener(this)
        btn_three.setOnClickListener(this)
        btn_four.setOnClickListener(this)
        btn_five.setOnClickListener(this)
        btn_six.setOnClickListener(this)
        btn_seven.setOnClickListener(this)
        btn_eight.setOnClickListener(this)
        btn_nine.setOnClickListener(this)
        btn_zero.setOnClickListener(this)
        btn_dot.setOnClickListener(this)
        btn_add.setOnClickListener(this)
        btn_sub.setOnClickListener(this)
        btn_mul.setOnClickListener(this)
        btn_div.setOnClickListener(this)
        btn_del.setOnClickListener(this)
        btn_equal.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        var id = v?.id
        when(id){
            R.id.btn_one -> setExpression('1')
            R.id.btn_two -> setExpression('2')
            R.id.btn_three -> setExpression('3')
            R.id.btn_four-> setExpression('4')
            R.id.btn_five -> setExpression('5')
            R.id.btn_six -> setExpression('6')
            R.id.btn_seven -> setExpression('7')
            R.id.btn_eight -> setExpression('8')
            R.id.btn_nine -> setExpression('9')
            R.id.btn_zero -> setExpression('0')
            R.id.btn_del -> {
                if (mTextView != null) {
                    mNumbers = mTextView?.text.toString()
                }
                if(!mNumbers.isEmpty()){
                    mNumbers = mNumbers.substring(0, mNumbers.length - 1)
                    if (mTextView != null) {
                        mTextView?.text = mNumbers
                    }
                }
            }
            R.id.btn_div -> setExpression('/')
            R.id.btn_mul -> setExpression('*')
            R.id.btn_add -> setExpression('+')
            R.id.btn_sub -> setExpression('-')
            R.id.btn_equal -> {
                mNumbers = mTextView?.text.toString() + '$'
                /*mTextView.setText(infixToPostfix(mNumbers));
                return;*/
                /*mTextView.setText(infixToPostfix(mNumbers));
                return;*/
                if (isInfixExpValid(mNumbers)) {
                    str2 = infixToPostfix(mNumbers)
                    mTextView?.text = evaluatePostfix(str2).toString() + ""
                } else {
                    Toast.makeText(this, "Math error", Toast.LENGTH_SHORT).show()
                }
                //mTextView.setText(eval);
                //mTextView.setText(eval);
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mTextView != null) {
            outState.putString(expression,mTextView.text.toString())
        }
    }

    private fun setExpression(ch: Char) {
        var exp = getExpression()
        if (ch == '.') {
            val num = exp?.length?.let { getCurrentNumber(exp!!, it) }
            if (num != null) {
                if (num.contains(".")) {
                    return
                }
            }
        }
        if (exp!!.length == 0 && isOperator(ch)) {
            if (ch == '-') {
                mTextView!!.text = ch.toString() + ""
            }
            return
        }
        if (isOperator(ch)) {
            val len = exp.length
            if (len > 0) {
                val lastCh = exp[len - 1]
                if (len == 1 && lastCh == '-') {
                    return
                }
                if (isOperator(lastCh)) {
                    exp = exp.substring(0, len - 1)
                }
            }
        }
        mTextView?.text = exp + ch
    }

    private fun getExpression(): String? {
        return mTextView?.text.toString()
    }

     private fun getCurrentNumber(expression: String, index: Int): String? {
        var number = ""
        //after current position
        for (i in index - 1 downTo 0) {
            val ch = expression[i]
            if (isOperator(ch)) {
                break
            }
            number = ch.toString() + number
        }
        return number
    }

    private fun isOperator(ch: Char): Boolean {
        when (ch) {
            '+', '-', '*', '/' -> return true
        }
        return false
    }

    private fun infixToPostfix(infixExpression: String): String {
        val operatorStack = Stack<Char>()
        var result = ""
        var num = ""
        for (i in 0 until infixExpression.length) {
            val ch = infixExpression[i]
            if (ch == '-' && i == 0) {
                num += ch
            } else {
                if (isDigit(ch)) {
                    num += ch
                } else if (ch != '$') {
                    if (MainActivity.isOperator(ch)) {
                        result = "$result($num)"
                        num = ""
                        while (!operatorStack.empty() && precedence(operatorStack.peek()) >= precedence(ch)) {
                            result += operatorStack.peek()
                            operatorStack.pop()
                        }
                        operatorStack.push(ch)
                    }
                }
            }
        }
        result = "$result($num)"
        while (!operatorStack.empty()) {
            result += operatorStack.peek()
            operatorStack.pop()
        }
        return result
    }

    private fun isInfixExpValid(exp: String): Boolean {
        for (i in 0 until exp.length) {
            val ch = exp[i]
            if (isOperator(ch) && i == 0 && ch == '-') {
                return true
            }
            if (isOperator(ch)) {
                val len = exp.length
                if (exp.indexOf(ch) > 0 && exp.indexOf(ch) < len - 1) {
                    if (isDigit(exp[i - 1]) || isDigit(exp[i + 1])) {
                        return false
                    }
                } else {
                    return false
                }
            }
        }
        return true
    }

    private fun evaluatePostfix(postfixExp: String): Double {
        val resultStk = Stack<Double>()
        var num = ""
        for (i in 0 until postfixExp.length) {
            val ch = postfixExp[i]
            if (MainActivity.isOperator(ch)) {
                if (ch == '-' && i == 1) {
                    num += ch
                } else {
                    val operatorRight = resultStk.pop()
                    val operatorLeft = resultStk.pop()
                    val res = compute(ch, operatorLeft, operatorRight)
                    resultStk.push(res)
                }
            } else {
                if (isDigit(ch)) {
                    num += ch
                }
                if (ch == ')') {
                    resultStk.push(num.toDouble())
                    num = ""
                }
            }
        }
        return resultStk.peek()
    }

    private fun isDigit(ch: Char): Boolean {
        when (ch) {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' -> return true
        }
        return false
    }

    private fun compute(symbol: Char, op1: Double, op2: Double): Double {
        return when (symbol) {
            '+' -> op1 + op2
            '-' -> op1 - op2
            '*' -> op1 * op2
            '/' -> op1 / op2
            else -> 0.0
        }
    }

    private fun precedence(operator: Char): Int {
        when (operator) {
            '+', '-' -> return 1
            '*', '/' -> return 2
        }
        return -1
    }
}