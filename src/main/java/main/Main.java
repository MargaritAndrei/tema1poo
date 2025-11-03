package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;

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


    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();

        // luam simularile din inputloader, comenzile la fel, dupa luam fiecare simulare si comenzile
        // corespunzatoare simularii respective
        ArrayList<SimulationInput> simulations = inputLoader.getSimulations();
        ArrayList<CommandInput> commands = inputLoader.getCommands();

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