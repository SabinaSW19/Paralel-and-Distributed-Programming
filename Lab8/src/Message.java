import java.io.Serializable;

import java.io.Serializable;

public class Message implements Serializable {
    private String operation; // subscribe, unsubscribe, value changed
    private String variable;
    private Integer value;

    public Message(String operation, String variable, Integer value) {
        this.operation = operation;
        this.variable = variable;
        this.value = value;
    }

    public String getOperation() {
        return operation;
    }

    public String getVariable() {
        return variable;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        String str = "Message: operation = ";
        if(operation.equals("subscribe"))
            str += "new subscriber, ";
        else if(operation.equals("unsubscribe"))
            str += "unsubscribe, ";
        else
            str += "changed value, ";
        str += "variable = " + variable + ", value = " + value;
        return str;
    }
}

