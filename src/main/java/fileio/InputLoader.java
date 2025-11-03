package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic skeleton for loading input JSON file as a Map.
 * Students should implement deeper parsing themselves.
 */
public class InputLoader {
    private ArrayList<SimulationInput> simulations;
    private ArrayList<CommandInput> commands;

    public InputLoader(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputRoot root = mapper.readValue(new File(filePath), InputRoot.class);
        this.simulations = new ArrayList<>(root.simulationParams);
        this.commands = new ArrayList<>(root.commands);
    }

    public ArrayList<SimulationInput> getSimulations() {
        return simulations;
    }

    public ArrayList<CommandInput> getCommands() {
        return commands;
    }

    // Helper class for root deserialization
    public static class InputRoot {
        public List<SimulationInput> simulationParams;
        public List<CommandInput> commands;
    }
}
