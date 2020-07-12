package com.example.calci;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Stack;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonOne;
    private Button mButtonTwo;
    private Button mButtonThree;
    private Button mButtonFour;
    private Button mButtonFive;
    private Button mButtonSix;
    private Button mButtonSeven;
    private Button mButtonEight;
    private Button mButtonNine;
    private Button mButtonDot;
    private Button mButtonZero;
    private Button mButtonDel;
    private Button mButtonDiv;
    private Button mButtonMul;
    private Button mButtonSub;
    private Button mButtonAdd;
    private Button mButtonEqual;

    private TextView mTextView;
    private String mNumbers = "";
    private String str2 = "";

    private static final String expression = "expression";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonOne = findViewById(R.id.btn_one);
        mButtonTwo = findViewById(R.id.btn_two);
        mButtonThree = findViewById(R.id.btn_three);
        mButtonFour = findViewById(R.id.btn_four);
        mButtonFive = findViewById(R.id.btn_five);
        mButtonSix = findViewById(R.id.btn_six);
        mButtonSeven = findViewById(R.id.btn_seven);
        mButtonEight = findViewById(R.id.btn_eight);
        mButtonNine = findViewById(R.id.btn_nine);
        mButtonDot = findViewById(R.id.btn_dot);
        mButtonZero = findViewById(R.id.btn_zero);
        mButtonDel = findViewById(R.id.btn_del);
        mButtonDiv = findViewById(R.id.btn_div);
        mButtonMul = findViewById(R.id.btn_mul);
        mButtonSub = findViewById(R.id.btn_sub);
        mButtonAdd = findViewById(R.id.btn_add);
        mButtonEqual = findViewById(R.id.btn_equal);

        mTextView = findViewById(R.id.tv_num);

        if(savedInstanceState != null) {
            String expr = savedInstanceState.get(expression).toString();
            mTextView.setText(expr);
        }

        mButtonOne.setOnClickListener(this);
        mButtonTwo.setOnClickListener(this);
        mButtonThree.setOnClickListener(this);
        mButtonFour.setOnClickListener(this);
        mButtonFive.setOnClickListener(this);
        mButtonSix.setOnClickListener(this);
        mButtonSeven.setOnClickListener(this);
        mButtonEight.setOnClickListener(this);
        mButtonNine.setOnClickListener(this);
        mButtonDot.setOnClickListener(this);
        mButtonZero.setOnClickListener(this);
        mButtonDel.setOnClickListener(this);
        mButtonDiv.setOnClickListener(this);
        mButtonMul.setOnClickListener(this);
        mButtonSub.setOnClickListener(this);
        mButtonAdd.setOnClickListener(this);
        mButtonEqual.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_one:
                setExpression('1');
                break;
            case R.id.btn_two:
                setExpression('2');
                break;
            case R.id.btn_three:
                setExpression('3');
                break;
            case R.id.btn_four:
                setExpression('4');
                break;
            case R.id.btn_five:
                setExpression('5');
                break;
            case R.id.btn_six:
                setExpression('6');
                break;
            case R.id.btn_seven:
                setExpression('7');
                break;
            case R.id.btn_eight:
                setExpression('8');
                break;
            case R.id.btn_nine:
                setExpression('9');
                break;
            case R.id.btn_dot:
                setExpression('.');
                break;
            case R.id.btn_zero:
                setExpression('0');
                break;
            case R.id.btn_del:
                mNumbers = mTextView.getText().toString();
                if (!mNumbers.isEmpty()) {
                    mNumbers = mNumbers.substring(0, mNumbers.length() - 1);
                    mTextView.setText(mNumbers);
                }
                break;
            case R.id.btn_div:
                setExpression('/');
                break;
            case R.id.btn_mul:
                setExpression('*');
                break;
            case R.id.btn_sub:
                setExpression('-');
                break;
            case R.id.btn_add:
                setExpression('+');
                break;
            case R.id.btn_equal:
                mNumbers = mTextView.getText().toString() + '$';
                /*mTextView.setText(infixToPostfix(mNumbers));
                return;*/
                if(isInfixExpValid(mNumbers)) {
                    str2 = infixToPostfix(mNumbers);
                    mTextView.setText(evaluatePostfix(str2) + "");
                } else {
                    Toast.makeText(this,"Math error", Toast.LENGTH_SHORT).show();
                }
                //mTextView.setText(eval);
                break;
        }
    }

    private String getExpression() {
        return mTextView.getText().toString();
    }

    private void setExpression(char ch) {
        String exp = getExpression();
        if(ch == '.') {
            String num = getCurrentNumber(exp, exp.length());
            if(num.contains(".")) {
                return;
            }
        }
        if(exp.length() == 0 && isOperator(ch)) {
            if(ch == '-') {
                mTextView.setText(ch + "");
            }
            return;
        }
        if (isOperator(ch)) {
            int len = exp.length();
            if (len > 0) {
                char lastCh = exp.charAt(len - 1);
                if(len == 1 && lastCh == '-') {
                    return;
                }
                if (isOperator(lastCh)) {
                    exp = exp.substring(0, len - 1);
                }

            }
        }
        mTextView.setText(exp + ch);
    }


    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return -1;
    }

    public static String infixToPostfix(String infixExpression) {
        final Stack<Character> operatorStack = new Stack<Character>();
        String result = "";
        String num = "";
        for (int i = 0; i < infixExpression.length(); i++) {
            char ch = infixExpression.charAt(i);
            if(ch == '-' && i == 0) {
                num = num + ch;
            } else {
                if (isDigit(ch)) {
                    num = num + ch;
                } else if (ch != '$') {

                    if (isOperator(ch)) {
                        result = result + "(" + num + ")";
                        num = "";
                        while (!operatorStack.empty() && precedence(operatorStack.peek()) >= precedence(ch)) {
                            result = result + operatorStack.peek();
                            operatorStack.pop();
                        }
                        operatorStack.push(ch);
                    }
                }
            }

        }
        result = result + "(" + num + ")";
        while (!operatorStack.empty()) {
            result = result + operatorStack.peek();
            operatorStack.pop();

        }
        return result;
    }


    public static double compute(char symbol, double op1, double op2) {
        switch (symbol) {
            case '+':
                return op1 + op2;
            case '-':
                return op1 - op2;
            case '*':
                return op1 * op2;
            case '/':
                return op1 / op2;
            default:
                return 0;
        }
    }

    private static double evaluatePostfix(String postfixExp) {
        final Stack<Double> resultStk = new Stack<>();
        String num = "";
        for (int i = 0; i < postfixExp.length(); i++) {
            char ch = postfixExp.charAt(i);
            if (isOperator(ch)) {
                if( ch == '-' && i == 1) {
                    num += ch;
                } else {
                    double operatorRight = resultStk.pop();
                    double operatorLeft = resultStk.pop();
                    double res = compute(ch, operatorLeft, operatorRight);
                    resultStk.push(res);
                }
            } else {
                if (isDigit(ch)) {
                    num += ch;
                }
                if (ch == ')') {
                    resultStk.push(Double.parseDouble(num));
                    num = "";
                }
            }
        }

        return resultStk.peek();
    }

    private static boolean isDigit(char ch) {
        switch (ch) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '.':
                return true;
        }
        return false;

    }

    public static boolean isOperator(char ch) {
        switch (ch) {
            case '+':
            case '-':
            case '*':
            case '/':
                return true;
        }
        return false;

    }

    private static boolean isInfixExpValid(String exp) {
        for (int i = 0; i < exp.length(); i++) {
            char ch = exp.charAt(i);
            if(isOperator(ch) && i == 0 && ch == '-') {
                return true;
            }
            if (isOperator(ch)) {
                int len = exp.length();
                if(exp.indexOf(ch) > 0 && exp.indexOf(ch) < len - 1) {
                    if (!isDigit(exp.charAt(i - 1)) || !isDigit(exp.charAt(i + 1))) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(expression, mTextView.getText().toString());
    }

    private static String getCurrentNumber(String expression, int index) {
        String number = "";
        //after current position
        for(int i = index-1; i >= 0; i--) {
            char ch = expression.charAt(i);
            if(isOperator(ch)) {
                break;
            }
            number = ch + number;
        }

        return number;
    }
}

