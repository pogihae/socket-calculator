import java.util.Queue;

/** MyProtocol
 *
 * Enum for each operator and status
 * Making message using the rule
 * */
public class MyProtocol {

    /** Operator in protocol*/
    public enum Operator {
        ADD('+'), MIN('-'), DIV('/'), MUL('*');

        Operator(final char opr) {
            this.opr = opr;
        }

        private final char opr;

        public static String convertToType(char opr) {
            for(Operator op : Operator.values()) {
                if(op.opr == opr) return op.name();
            }
            return "?";
        }

        public char getOpr() {
            return opr;
        }
    }
    /** Status in protocol*/
    public enum Status {
        ANS(0), DIV0(1), ARG(2), WRG(3);

        Status(final int code) {
            this.code = code;
        }

        private final int code;

        public static String convertToType(int code) {
            for(Status s : Status.values()) {
                if(code == s.code) return s.name();
            }
            return "?";
        }
    }

    /** makeRequest
     * make request message using rule in protocol
     *
     * @return formed request message
     * */
    public static String makeRequest(Queue<Character> operators, Queue<Double> operands) {
        StringBuffer msg = new StringBuffer();
        msg.append(operators.size()+" ");

        for(char c : operators) {
            msg.append(Operator.convertToType(c)+" ");
        }

        for(double d : operands) {
            msg.append(d+" ");
        }

        msg.setLength(msg.length() - 1);
        return msg + "\n";
    }

    /** makeResponse
     * make response message using rule in protocol
     *
     * @return formed response message
     * */
    public static String makeResponse(int code, String content) {
        StringBuffer msg = new StringBuffer();
        msg.append(Status.convertToType(code)+": ");
        msg.append(content);
        return msg + "\n";
    }
}
