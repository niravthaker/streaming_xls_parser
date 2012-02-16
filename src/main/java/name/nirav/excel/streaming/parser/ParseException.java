package name.nirav.excel.streaming.parser;

public class ParseException extends Exception {
    public ParseException(Exception e) {
        super(e);
    }

    public ParseException(String msg, Exception e) {
        super(msg, e);
    }

    private static final long serialVersionUID = -9051737755039130402L;

}
