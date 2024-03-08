# "Coffee Server" Learning Endpoint: Unreliable-by-Design 

If everything in the world worked predictably, there\'d be no need for Temporal. Instead, handling unreliable services is central to real-world software development. Network issues, service outages, and other circumstances regularly interrupt or delay business processes.

This microserve provides a simulated coffee server. It offers controlled end-point reliability with a failure rate that you choose. Use it to explore and experiment as you explore building resilient systems.

## Building and Running

This project uses "make" commands to build and execute the Coffee server, and to clean up build artifacts from the working directory.

### Project Contents

The project consists of a single Java source, a Makefile, and this README.md.

```
$ ls
CoffeeServer.java	Makefile		README.md
```

### Build

To build the server, issue:

```bash
$ make
```
Alternately, `make build` starts compilation.

### Run the Server

Start up the server by executing:

```bash
$ make run
```

To keep your server running, use `nohup` execution along with your `make run` command:

```bash
$ nohup make run &
```

Using `nohup` makes the server immune to hangups. It sets the execution to ignore the SIGHUP signal (terminate process and/or terminal line hangup). Hangup immunity provides continuous execution even after ending your shell or SSH session or closing your terminal window. The server continues to run when you log out.

### Nohup and Output

During a nohup run, standard output and standard error are normally redirected to the working directory's "nohup.out" file. This server logs its address, port, and all connections to standard out. View the most recent connections by issuing:

```
$ tail nohup.out
```

### Resolving SSH hang-ups

Occasionally, you may encounter issues with some SSH clients when using nohup. If the session hangs, try this approach:

```
$ nohup make run & > nohup.out 2> nohup.err < /dev/null &
```

The source of the issue is when an SSH session refuses to log off because it's protecting the data going to or from the background job. This resolution approach redirects all three streams (stdin, stdout, stderr).

**Note:** The server operates on port 8418, selected with a nod towards the Teapot HTTP status. 

### Cleaning Up

To remove any generated files or artifacts, use the following command:

```bash
$ make clean
```

This deletes the `build` folder in your working directory.

## Tutorial Endpoint: 

Gain practical experience with this learning-focused endpoint. Use this endpoint to:

-   Test and validate against controlled levels of service reliability.
-   Implement retry and error handling strategies.
-   Mitigate service failures impact.
-   Design with reliable fault-tolerant durable execution.
-   Explore concepts of resilience like circuit breakers, timeouts, and exponential backoff.

## Use Cases: 

-   Incorporate this endpoint into your Temporal learning process.
-   Use an endpoint like this during your prototyping phase.
-   Experiment with controlled resilience patterns when validating designs.

## Examples: 

Request a cup of coffee with default reliability (50% success).

```
GET /coffee
```

Request a cup of coffee with increased reliability (80% success).

```
GET /coffee?reliability=0.8
```

Request a cup of coffee with low reliability (20% success).

```
GET /coffee?reliability=0.2
```

```
    static String asciiTeaArt = ""
    + "           .------.____\n"
    + "         .-'       \\ ___)\n"
    + "      .-'         \\\\\\\n"
    + "   .-'        ___  \\\\)\n"
    + ".-'          /  (\\  |)\n"
    + "         __  \\  ( | |\n"
    + "        /  \\  \\__'| |\n"
    + "       /    \\____).-'\n"
    + "     .'       /   |\n"
    + "    /     .  /    |\n"
    + "  .'     / \\/     |\n"
    + " /      /   \\     |\n"
    + "       /    /    _|_\n"
    + "       \\   /    /\\ /\\\n"
    + "        \\ /    /__v__\\\n"
    + "         '    |       |\n"
    + "              |     .#|\n"
    + "              |#.  .##|\n"
    + "              |#######|\n"
    + "              |#######|\n" 
```