# args2
### Argument string parser to convenient data accessor

It makes sense to use the Args class in the **main** method, where we
get an array of arguments as strings.

    public static void main(String[] args) {
        Args arguments = new Args(args);
        // ...
    }

Also, of course, you can pass an entire string to the constructor:

    new Args("--host localhost --port 8080 --debugMode")

Now we have access to data through special methods of the **Args** class:

    String host = arguments.get("--host"); // localhost
    int port = arguments.get(Integer.class, "--port"); // 8080

As seen above, we can explicitly specify the type of the expected value. Supported types:

    String, Boolean, Integer, Long, Float, Double, Short, Byte

The String.class type is optional and can be omitted using the **get(String key)** method overload

You can use the default value by passing it as an argument:

    arguments.get(Integer.class, "--port", 8080);

Check if an argument has been passed:

    boolean debugMode = arguments.get(Boolean.class, "--debugMode");

If you pass not a string, but an array of strings as a key, each element of this array is checked.
This is how key aliases are supported, for example:

    int port = arguments.get(Integer.class, new String[] {"--port", "-p"}, 8080);

For more about the API, see the public methods of the **Args** class

