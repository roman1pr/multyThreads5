import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.*;

/**
 * Parse an Apache log file with Regular Expressions
 */

public class LogAccessData{

    private static String logEntryPattern = "^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+) (\\S+)\\s?(.*?)\" (\\d{3}) (\\S+)";

    /** The sample log entry to be parsed. */
    private static String logEntryLine = "127.0.0.1 - - [21/Jul/2014:9:55:27 -0800] \"GET /home.html HTTP/1.1\" 200 2048";

    /** The number of fields that must be found. */
    private static int NUM_FIELDS = 9;

    /** in what format read DataTime */
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.UK);

    private String IP;
    private String DateTimeString;
    private String Method;
    private String Request;
    private String ResponseCode;
    private String Bytes;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getDateTimeString() {
        return DateTimeString;
    }

    public void setDateTimeString(String dateTimeString) {
        DateTimeString = dateTimeString;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        Request = request;
    }

    public int getResponseCodeInt(){
        return Integer.parseInt(ResponseCode);
    }

    public String getResponseCode(){
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getBytes() {
        return Bytes;
    }

    public void setBytes(String bytes) {
        Bytes = bytes;
    }

    public LogAccessData(String IP, String DateTimeString, String Method,
                         String Request, String ResponseCode, String Bytes){

        init(IP, DateTimeString, Method, Request, ResponseCode, Bytes);
    }

    public void init(String IP, String DateTimeString, String Method,
                         String Request, String ResponseCode, String Bytes){
        this.IP = IP;
        this.DateTimeString = DateTimeString;
        this.Method = Method;
        this.Request = Request;
        this.ResponseCode = ResponseCode;
        this.Bytes = Bytes;
    }

    public static LogAccessData parseString(String str) {

        Pattern patter = Pattern.compile(logEntryPattern);
        Matcher matcher = patter.matcher(str);

        if (!matcher.matches() ||
                NUM_FIELDS != matcher.groupCount()) {
            System.err.println("Bad log entry (or problem with RE?): " + str);
            return  new LogAccessData("",
                    "",
                    "",
                    "",
                    "-1",
                    "");
        }

        String date = matcher.group(4);
        LocalDateTime localDateTime = LocalDateTime.parse(date, DATE_FORMATTER);

        String stringISODate = localDateTime
                .toLocalDate()
                .atStartOfDay()
                .format(DateTimeFormatter.ISO_DATE);

        return new LogAccessData(matcher.group(1),
            stringISODate,
            matcher.group(5),
            matcher.group(6),
            matcher.group(8),
            matcher.group(9));

    }

    @Override
    public String toString() {
        return "IP: " + IP +
                " Timestamp: " + DateTimeString +
                " Method: " + Method +
                " Request: " + Request +
                " ResponseCode: " + ResponseCode +
                " Bytes : " + Bytes +
                " \n";
    }

}


