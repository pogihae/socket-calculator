# Socket Calculator
Socket Calculator is a Server - Client program with TCP connection. Server allows multi-client with each thread. Using my own protocol for sending, receiving message format

## How to run

    run Demo files with up to JAVA 8

## Architecture
- ###
  ![flow_chart](https://user-images.githubusercontent.com/76048647/135282514-92a48e00-5baf-4aaa-9f6e-2c6d7796029e.png)


- ### Protocol
  - #### Request MSG
    > **[operator_num operators operands]**
        
        [2 ADD DIV 2 6 3] <- [2 + 6 / 3]
        
        [1 DIV 5 0] <- [5 / 0]
  - #### Response MSG
    > **[status content]**
    
        [ANS 4]
    
        [DIV0 5/0]
  - #### Used Word
    * *operator*
          
          ADD(+) MIN(-) DIV(/) MUL(*)
    
    * *status*
          
          ANS(success) DIV0(divide by 0) ARG(more or less operand) WRG(wrong expression)

- ### Class Implements
  
  ![class drawio (2)](https://user-images.githubusercontent.com/76048647/135292009-5a2f1857-e620-41b1-bc97-39eab841593d.png)
  
- ### Screenshot
  
  <img src="https://user-images.githubusercontent.com/76048647/135550284-d5f99b7b-c05e-48e1-af57-116ba98f4f3f.PNG" width="600">
  <img src="https://user-images.githubusercontent.com/76048647/135550468-ddb86e5b-50ff-4f9d-9f21-123b72b6c57e.PNG" width="600">

