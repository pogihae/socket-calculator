import java.util.List;

/** MyProtocol
 * Enum for each operators and status
 * Making message using the rule
 * */
public class MyProtocol {

    /** Operator in protocol*/
    public static enum Operator {
        ADD('+'), MIN('-'), DIV('/'), MUL('*');

        Operator(final char opr) {
            this.opr = opr;
        }

        private char opr;

        public static String convertToType(char opr) {
            for(Operator op : Operator.values()) {
                if(op.opr == opr) return op.name();
            }
            return null;
        }
    }
    /** Status in protocol*/
    public static enum Status {
        ANS(0), DIV0(1), ARG(2);

        Status(final int code) {
            this.code = code;
        }

        private int code;

        public static String convertToType(int code) {
            for(Status s : Status.values()) {
                if(code == s.code) return s.name();
            }
            return null;
        }
    }

    /** makeRequest
     * make request message using rule in protocol
     * @return formed request message
     * */
    public static String makeRequest(List<Character> operators, List<Double> operands) {
        StringBuffer msg = new StringBuffer();
        msg.append(operators.size()+" ");

        for(char c : operators) {
            msg.append(Operator.convertToType(c)+" ");
        }

        for(double d : operands) {
            msg.append(d+" ");
        }

        msg.setLength(msg.length() - 1);
        return msg.toString();
    }

    /** makeResponse
     * make response message using rule in protocol
     * @return formed response message
     * */
    public static String makeResponse(int errCode, String content) {
        StringBuffer msg = new StringBuffer();
        msg.append(Status.convertToType(errCode)+" ");
        msg.append(content);
        return msg.toString();
    }
}
