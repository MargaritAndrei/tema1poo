package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;
import simulation.Simulation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public class Main {

    private Main(){
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    private static ObjectNode createErrorNode(String message) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("message", message);
        return node;
    }
    private static ObjectNode createMessageNode(String message) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("message", message);
        return node;
    }

    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();

        // luam simularile din inputloader, comenzile la fel, dupa luam fiecare simulare si comenzile
        // corespunzatoare simularii respective
        ArrayList<SimulationInput> simulations = inputLoader.getSimulations();
        ArrayList<CommandInput> commands = inputLoader.getCommands();

        // implementare main

        Simulation currentSimulation = null;
        int simulationIndex = 0;
        for (CommandInput command : commands) {
            int currentTimestamp = command.timestamp;
            if (currentSimulation != null && !command.command.equals("startSimulation")) {
                currentSimulation.evolveEnvironment(currentTimestamp);
            }
            ObjectNode commandOutput = MAPPER.createObjectNode();
            commandOutput.put("command", command.command);

            ObjectNode resultData = null;

            switch (command.command) {
                case "startSimulation":
                    if (currentSimulation != null) {
                        resultData = createErrorNode("ERROR: Simulation already started. Cannot perform action");
                    } else if (simulationIndex >= simulations.size()) {
                        resultData = createErrorNode("ERROR: No simulation parameters left to start.");
                    } else {
                        SimulationInput simData = simulations.get(simulationIndex);
                        currentSimulation = new Simulation(simData);
                        simulationIndex++;
                        resultData = createMessageNode("Simulation has started.");
                    }
                    break;

                case "endSimulation":
                    if (currentSimulation == null) {
                        resultData = createErrorNode("ERROR: Simulation not started. Cannot perform action");
                    } else {
                        currentSimulation = null;
                        resultData = createMessageNode("Simulation has ended.");
                    }
                    break;
                default:
                    if (currentSimulation == null) {
                        resultData = createErrorNode("ERROR: Simulation not started. Cannot perform action");
                    } else {
                        resultData = currentSimulation.dispatchCommand(command, currentTimestamp);
                    }
                    break;
            }

            if (resultData != null) {
                if (resultData.has("message")) {
                    commandOutput.put("message", resultData.get("message").asText());
                }
                if (resultData.has("output")) {
                    commandOutput.set("output", resultData.get("output"));
                }
            }
            commandOutput.put("timestamp", currentTimestamp);
            output.add(commandOutput);
        }

        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         *
         * ObjectNode objectNode = MAPPER.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = MAPPER.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}