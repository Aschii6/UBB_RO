#ifndef EXPRESSION_UTILS_H
#define EXPRESSION_UTILS_H

#include <stack>
#include <string>
#include <cctype>
#include <vector>
#include <sstream>
#include <map>

using namespace std;

int precedence(char op)
{
    if (op == '+' || op == '-')
        return 1;
    if (op == '*')
        return 2;
    return 0;
}

bool isOperator(char c)
{
    return c == '+' || c == '-' || c == '*';
}

bool isOperator(const string &s)
{
    return s == "+" || s == "-" || s == "*";
}

bool isInteger(const string &s)
{
    if (s.empty() || (!isdigit(s[0]) && s[0] != '-' && s[0] != '+'))
        return false;

    char *p;
    strtol(s.c_str(), &p, 10);

    return (*p == 0);
}

vector<string> infixToPostfix(const string &infix)
{
    stack<char> operators;
    vector<string> postfix;

    for (size_t i = 0; i < infix.length(); ++i)
    {
        char c = infix[i];

        if (isspace(c))
            continue;

        if (isalnum(c))
        {
            string operand(1, c);
            while (i + 1 < infix.length() && isalnum(infix[i + 1]))
            {
                operand += infix[++i];
            }
            postfix.push_back(operand);
        }
        else if (c == '-' && (postfix.empty() || !isalnum(postfix.back().back())))
        {
            string operand(1, c);
            while (i + 1 < infix.length() && isdigit(infix[i + 1]))
            {
                operand += infix[++i];
            }
            postfix.push_back(operand);
        }
        else if (isOperator(c))
        {
            while (!operators.empty() && precedence(operators.top()) >= precedence(c))
            {
                postfix.push_back(string(1, operators.top()));
                operators.pop();
            }
            operators.push(c);
        }
        else if (c == '(')
        {
            operators.push(c);
        }
        else if (c == ')')
        {
            while (!operators.empty() && operators.top() != '(')
            {
                postfix.push_back(string(1, operators.top()));
                operators.pop();
            }
            if (!operators.empty())
                operators.pop();
        }
        else
        {
            throw invalid_argument("Invalid character in expression");
        }
    }

    while (!operators.empty())
    {
        postfix.push_back(string(1, operators.top()));
        operators.pop();
    }

    return postfix;
}

vector<string> generateAsmCode(vector<string> polishPostfix, string id)
{
    vector<string> asmCode;

    stack<string> operands;

    if (polishPostfix.size() == 1)
    {
        string operand = polishPostfix[0];

        if (!isInteger(operand))
        {
            asmCode.push_back("mov eax, [" + operand + "]\n");

            asmCode.push_back("mov [" + id + "], eax\n");
        }
        else
        {
            asmCode.push_back("mov dword [" + id + "], " + operand + "\n");
        }

        return asmCode;
    }

    bool ebxUsed = false;
    bool ecxUsed = false;

    bool justAddedOperand = false;

    string lastRegisterUsed;

    for (string token : polishPostfix)
    {
        if (isOperator(token))
        {
            if (operands.size() >= 2)
            {
                string operand2 = operands.top();
                operands.pop();

                string operand1 = operands.top();
                operands.pop();

                if (!isInteger(operand1))
                    operand1 = '[' + operand1 + ']';

                if (!isInteger(operand2))
                    operand2 = '[' + operand2 + ']';

                asmCode.push_back("mov eax, " + operand1);

                if (token == "+")
                {
                    asmCode.push_back("add eax, " + operand2);
                }
                else if (token == "-")
                {
                    asmCode.push_back("sub eax, " + operand2);
                }
                else if (token == "*")
                {
                    asmCode.push_back("imul eax, " + operand2);
                }

                if (!ebxUsed)
                {
                    asmCode.push_back("mov ebx, eax\n");
                    ebxUsed = true;
                    lastRegisterUsed = "ebx";
                }
                else if (!ecxUsed)
                {
                    asmCode.push_back("mov ecx, eax\n");
                    ecxUsed = true;
                    lastRegisterUsed = "ecx";
                }
            }
            else if (operands.size() == 1)
            {
                string operand1 = operands.top();
                operands.pop();

                if (!isInteger(operand1))
                    operand1 = '[' + operand1 + ']';

                string operand2 = lastRegisterUsed;

                if (justAddedOperand) {
                    string aux = operand1;
                    operand1 = operand2;
                    operand2 = aux;
                }

                asmCode.push_back("mov eax, " + operand1);

                if (token == "+")
                {
                    asmCode.push_back("add eax, " + operand2);
                }
                else if (token == "-")
                {
                    asmCode.push_back("sub eax, " + operand2);
                }
                else if (token == "*")
                {
                    asmCode.push_back("imul eax, " + operand2);
                }

                asmCode.push_back("mov " + lastRegisterUsed + ", eax\n");
            }
            else
            {
                if (!ebxUsed && !ecxUsed)
                    throw runtime_error("This shouldn't happen :)");

                string operand2 = lastRegisterUsed;
                string operand1 = lastRegisterUsed == "ebx" ? "ecx" : "ebx";

                asmCode.push_back("mov eax, " + operand1);

                if (token == "+")
                {
                    asmCode.push_back("add eax, " + operand2);
                }
                else if (token == "-")
                {
                    asmCode.push_back("sub eax, " + operand2);
                }
                else if (token == "*")
                {
                    asmCode.push_back("imul eax, " + operand2);
                }

                asmCode.push_back("mov ecx, eax\n");
                lastRegisterUsed = "ecx";

                ebxUsed = false;
                ecxUsed = true;
            }

            justAddedOperand = false;
        }
        else
        {
            operands.push(token);
            justAddedOperand = true;
        }
    }

    asmCode.push_back("mov [" + id + "], eax\n");

    return asmCode;
}

#endif // EXPRESSION_UTILS_H