public class Spark
{

    public static void main(String[] args) {
        try {
            if (args.length < 2)
            {
                throw new IllegalArgumentException("Invalid arguments");
            }
            countsWords(args[0], args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void countsWords(final String input, final String output) {
        Worker.start(input, output);
    }
}

