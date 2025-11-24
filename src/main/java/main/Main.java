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

public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * Creeaza un JSON ce contine
     * un mesaj de eroare.
     */
    private static ObjectNode createErrorNode(final String message) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("message", message);
        return node;
    }

    /**
     * Creeaza un JSON ce contine
     * un mesaj normal.
     */
    private static ObjectNode createMessageNode(final String message) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("message", message);
        return node;
    }

    /**
     * Implementarea functiei Main, entrypoint-ul temei.
     */
    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();

        // luam simularile din inputloader, comenzile la fel, dupa luam fiecare
        // simulare si comenzile
        // corespunzatoare simularii respective
        ArrayList<SimulationInput> simulations = inputLoader.getSimulations();
        ArrayList<CommandInput> commands = inputLoader.getCommands();

        // implementare main
        Simulation currentSimulation = null;
        int simulationIndex = 0;
        int lastTimestamp = 0;

        for (final CommandInput command : commands) {
            final int currentTimestamp = command.getTimestamp();
            if (currentSimulation != null) {
                for (int t = lastTimestamp + 1; t <= currentTimestamp; t++) {
                    if (t == 1 && command.getCommand().equals("startSimulation")) {
                        continue;
                    }
                    currentSimulation.evolveEnvironment(t);
                }
            }
            lastTimestamp = currentTimestamp;
            final ObjectNode commandOutput = MAPPER.createObjectNode();
            commandOutput.put("command", command.getCommand());

            ObjectNode resultData = null;

            switch (command.getCommand()) {
                case "startSimulation":
                    if (currentSimulation != null) {
                        resultData = createErrorNode(
                                "ERROR: Simulation already started. Cannot perform action");
                    } else if (simulationIndex >= simulations.size()) {
                        resultData = createErrorNode(
                                "ERROR: No simulation parameters left to start.");
                    } else {
                        SimulationInput simData = simulations.get(simulationIndex);
                        currentSimulation = new Simulation(simData);
                        simulationIndex++;
                        resultData = createMessageNode("Simulation has started.");
                    }
                    break;

                case "endSimulation":
                    if (currentSimulation == null) {
                        resultData = createErrorNode(
                                "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        currentSimulation = null;
                        resultData = createMessageNode("Simulation has ended.");
                    }
                    break;
                default:
                    if (currentSimulation == null) {
                        resultData = createErrorNode(
                                "ERROR: Simulation not started. Cannot perform action");
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

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
