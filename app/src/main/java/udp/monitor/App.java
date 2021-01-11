package udp.monitor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import org.apache.commons.cli.*;
import org.apache.http.conn.util.InetAddressUtils;

public class App {
    public static void main(String[] args) {
        Options options = new Options();

        Option type = new Option("t", "type", true, "type requested - client/server");
        type.setRequired(false);
        options.addOption(type);

        Option address = new Option("a", "address", true, "ip address");
        address.setRequired(false);
        options.addOption(address);

        Option port = new Option("p", "port", true, "port number");
        port.setRequired(false);
        options.addOption(port);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String appType = cmd.getOptionValue("type");
            String targetAddress = cmd.getOptionValue("address");
            String targetPort = cmd.getOptionValue("port");

            if (appType.equals(null))
            {
                appType = "server";
            }

            if (!appType.equals("client") && !appType.equals("server"))
            {
                throw new ParseException("Application type must be either client or server");
            }

            if (targetAddress == null) {
                targetAddress = "localhost";
            }
            else {
                boolean isValid = InetAddressUtils.isIPv4Address(targetAddress);
                if (!isValid)
                {
                    System.out.println("Requested address is invalid.");
                    System.out.println("Using localhost");
                    targetAddress = "localhost";
                }
            }
            
            
            if (targetPort == null) {
                targetPort = "4444";
            }
            else {
                Pattern pattern = Pattern.compile("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
                Matcher matcher = pattern.matcher(targetPort);
                boolean matchFound = matcher.find();
                if (!matchFound)
                {
                    System.out.println("Port number outside of valid range");
                    System.out.println("Using port 4444");
                    targetPort = "4444";
                }
            }
            

            System.out.println();
            System.out.println(String.format("Starting new UDP %s using %s:%s", appType, targetAddress, targetPort));

            // TODO: ensure that the multi-project configuration works properly
            // TODO: Start application
            int intPort = Integer.parseInt(targetPort);
            Console console = System.console();

            if (appType.equals("server"))
            {
                EchoServer server = new EchoServer(intPort);
                server.run();
            }
            else if (appType.equals("client"))
            {
                boolean running = true;
                String input = "";
                String response = "";
                EchoClient client = new EchoClient(targetAddress, intPort);
                System.out.println("What's the ip address of the server?");
                String serverAddress = console.readLine();
                System.out.println("What's the port of the server?");
                String serverPort = console.readLine();
                System.out.println(String.format("Configured to send echos to %s:%s\n", serverAddress, serverPort));

                while (running) {
                    System.out.println("What message would you like to echo?");
                    input = console.readLine();
                    
                    response = client.sendEcho(input, serverAddress, serverPort);
                    System.out.println(String.format("[Client] Server responded: %s", response));
                    running = !input.equals("end");
                }

                System.out.println("[Client] Shutting down");
            }

            //System.out.println(inputFilePath);
            //System.out.println(outputFilePath);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }
}