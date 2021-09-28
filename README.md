# socket-calculator

calculator using TCP socket, thread

## How to run

run cal[Server|Client]Demo 

## Architecture
- ### Stream

- ### Protocol
  - #### Request MSG
    (operator_num operators operands)
    
    eg) (2 ADD DIV 2 6 3) <- (2 + 6 / 3 )
    
    eg) (1 DIV 5 0) <- (5 / 0)
  - #### Response MSG
    (status content)
    
    eg) ANS 4
    
    eg) DIV0! 5/0
  - #### Used Word
    operator: ADD(+) MIN(-) DIV(/) MUL(*)
    
    status: ANS(success) DIV0!(divide by 0) ARG!(more or less operand)

- ### Class
