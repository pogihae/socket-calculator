## How to run

run " "Demo files

## Architecture
- ### Flow
  ![flow_chart](https://user-images.githubusercontent.com/76048647/135282514-92a48e00-5baf-4aaa-9f6e-2c6d7796029e.png)


- ### Protocol
  - #### Request MSG
    [operator_num operators operands]
    
    eg) (2 ADD DIV 2 6 3) <- (2 + 6 / 3 )
    
    eg) (1 DIV 5 0) <- (5 / 0)
  - #### Response MSG
    (status content)
    
    eg) ANS 4
    
    eg) DIV0 5/0
  - #### Used Word
    operator: ADD(+) MIN(-) DIV(/) MUL(*)
    
    status: ANS(success) DIV0(divide by 0) ARG(more or less operand)

- ### Class
  todo: picture
