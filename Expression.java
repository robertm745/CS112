package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	StringTokenizer st = new StringTokenizer(expr, delims, true);
    	String str = "";
    	String strA = "";
    	while (st.hasMoreTokens()) {
    		str = st.nextToken();
    		char c = str.charAt(0);
    		if (Character.isLetter(c)) {
    			if (st.hasMoreTokens()) {
    				strA = st.nextToken();
					if (strA.contentEquals("[")) {
						Array temp = new Array(str);
						if (!arrays.contains(temp)) 
							arrays.add(temp);
					}
					else {
						Variable temp = new Variable(str);
						if (!vars.contains(temp))
							vars.add(temp);
					}
    			}
    			else {
    				Variable temp = new Variable(str);
					if (!vars.contains(temp))
						vars.add(temp);
    			}
    		}
    	}
    	/*
    	for (int j = 0; j < arrays.size(); j++)
    		System.out.println(arrays.get(j));
    	System.out.println("Size A = " + arrays.size());
		*/
    	
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
     	expr = expr.replaceAll("\\s+", "").trim();
    	if (expr.equals(""))
    		return 0;
    	StringTokenizer st = new StringTokenizer(expr, delims, true);
    	Stack<Float> valStk = new Stack<Float>();
    	Stack<Character> opStk = new Stack<Character>();
    	float num1 = 0;
    	float num2 = 0;
    	float temp = 0;
    	String str1 = "";
    	String str2 = "";
    	String str3 = "";
    	String tempStr = "";
    	String tempStr2 = "";
    	boolean neg = false;
    	char op = '0';
    	char ch = '0';
    	int temp1 = -1;
    	int temp2 = -1;
    	/*
    	str1 = st.nextToken();
    	temp1 = varContains(str1, vars);
    	temp2 = arrContains(str1, arrays);
    	if (temp1 != -1)
    		num1 = vars.get(temp1).value;
    	else if (temp2 != -1)
    		num1 = 0;
    	else {
    		num1 = Float.parseFloat(str1);
    	}
    	valStk.push(num1);
    	*/
 // BEGIN PROCESSING REST OF TOKENS
        
        while (st.hasMoreTokens()) {
            tempStr = "";
            tempStr2 = "";
            str1 = st.nextToken();  // see next token
            if (str1.equals("(")) {     // is it open paren?
                //System.out.println("test2");
                str2 = st.nextToken();
                tempStr = tempStr.concat(str2);
                while (str2.charAt(0) != ')') {
                    str2 = st.nextToken();
                    tempStr = tempStr.concat(str2);
                }
                System.out.println(tempStr + " is tempStr A");
                temp = evaluate(tempStr, vars, arrays);
                
                if (neg) {
                    temp = -temp;
                    neg = false;
                }
                valStk.push(temp);
                continue;
            }
            op = str1.charAt(0);
            if (op == '*' || op == '/') {       // is next token a * or / operator?
                str2 = st.nextToken();
                if (str2.charAt(0) == '(') {    // is the next token a paren?
                    //System.out.println("test");
                    str2 = st.nextToken();
                    tempStr = tempStr.concat(str2);
                    while (str2.charAt(0) != ')') {
                        str2 = st.nextToken();
                        tempStr = tempStr.concat(str2);
                    }
//                  System.out.println(tempStr + " is tempStr B");
                    temp = evaluate(tempStr, vars, arrays);
//                  System.out.println(temp + " is temp after rercursion");
                    if (false) {
                        str3 = st.nextToken();
                        if (str3.charAt(0) == '(') {
                          
                        }
                        else {
                            temp1 = varContains(str3, vars);
                            if (temp1 != -1)
                                num1 = vars.get(temp1).value;
                            else
                                num1 = Float.parseFloat(str3);
                        }
                    }
                    num2 = valStk.pop();

                    if (op == '*')
                        temp = num2 * temp;
                    else
                        temp = num2 / temp;
                    if (neg) {
                        temp = -temp;
                        neg = false;
                    }
                    System.out.println(temp + " is temp after * or / and " + num2 + " is num2");
                    valStk.push(temp);
                    continue;
                }
                else {                                      // if not a paren, a variable?
                    temp1 = varContains(str2, vars);
                    if (temp1 != -1)
                        num1 = vars.get(temp1).value;
                    else
                        num1 = Float.parseFloat(str2);
                    num2 = valStk.pop();
                    if (op == '*')
                        temp = num2 * num1;
                    else
                        temp = num2 / num1;
                    if (neg) {
                        temp = -temp;
                        neg = false;
                    }
                    System.out.println(temp + " here");
                    valStk.push(temp);
                    continue;
                }
            }
            
            if (op == '+' || op == '-') {
                if (op == '-') {
                    neg = true;
                    op = '+';
                }
                opStk.push(op);
                continue;
            }
            
            temp1 = varContains(str1, vars);
            if (temp1 != -1)
                num1 = vars.get(temp1).value;
            else if (str1.charAt(0) == ')') {
                break;
            }
            else
                num1 = Float.parseFloat(str1);
            if (neg) {
                num1 = -num1;
                neg = false;
            }
            System.out.println(num1 + " is num1");
            valStk.push(num1);
        }
        /*
        if (st.hasMoreTokens()) {
            System.out.println("is this working");
            while(st.hasMoreTokens()) {
                tempStr = tempStr.concat(st.nextToken());
            }
            System.out.println("prblem" + tempStr);
            temp = evaluate(tempStr, vars, arrays);

            num2 = valStk.pop();
            valStk.push(temp);
        }   */
        System.out.println(valStk.size() + " is valstk size");
        if (opStk.isEmpty())
            return valStk.pop(); 
        
        while (!opStk.isEmpty()) {
            num2 = valStk.pop();
            num1 = valStk.pop();
            op = opStk.pop();
            System.out.println(num1 + " with " + op + " on " + num2);
            if (op == '-') 
                valStk.push(num1-num2);
            else
                valStk.push(num1+num2);
        }
        System.out.println("ended a recur!");
        return valStk.pop();
	}

    
    
    // PRIVATE METHODS: 
    
    private static float eval(String str, ArrayList<Variable> vars, ArrayList<Array> arrays, Stack<Character> parenStk) {
    	while(!parenStk.isEmpty()) {
    		parenStk.pop();
    	}
    	return 0;
    }
    
	private static float eval(String str, ArrayList<Variable> vars, ArrayList<Array> arrays) {
	    	
	    	return 0;
	    }
    
    private static int varContains(String str, ArrayList<Variable> vars) {
    	String temp = "";
    	for (int i = 0; i < vars.size(); i++) {
    		temp = vars.get(i).name;
    		if (temp.equals(str))
    			return i;
    	}
    	return -1; 
    }
    
    private static int arrContains(String str, ArrayList<Array> arrays) {
    	String temp = "";
    	for (int i = 0; i < arrays.size(); i++) {
    		temp = arrays.get(i).name;
    		if (temp.equals(str))
    			return i;
    	}
    	return -1;
    }
}