import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InputGenerator {
    private final List<String> countries;
    private final int numberOfProblems;
    private final int minParticipants;
    private final int maxParticipants;

    private final float unsolvedProbability;
    private final float fraudProbability;

    private final List<Integer> possibleParticipants;

    public InputGenerator(List<String> countries, int numberOfProblems, int minParticipants, int maxParticipants,
                          float unsolvedProbability, float fraudProbability) {
        this.countries = countries;
        this.numberOfProblems = numberOfProblems;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.unsolvedProbability = unsolvedProbability;
        this.fraudProbability = fraudProbability;

        possibleParticipants = new ArrayList<>();
        for (int i = minParticipants; i <= maxParticipants; i++) {
            possibleParticipants.add(i);
        }
    }

    public void generate() {
        Random randomGenerator = new Random();

        for (String country : countries) {
            int random = randomGenerator.nextInt(possibleParticipants.size());
            int participants = possibleParticipants.get(random);
            possibleParticipants.remove(random);

            for (int i = 0; i < numberOfProblems; i++) {
                String filename = "C:/Users/Daniel/IdeaProjects/PPD_T4/src/inputs/Results" + country + "_" + "P" + (i + 1) + ".txt";

                try {
                    FileWriter writer = new FileWriter(filename);

                    for (int j = 0; j < participants; j++) {
                        String participantId = country + "_" + j;
                        double probability = Math.random();

                        if (probability < fraudProbability) {
                            writer.write("(" + participantId + ",-1)\n");
                        } else if (probability >= unsolvedProbability + fraudProbability) {
                            int points = randomGenerator.nextInt(1, 11);
                            writer.write("(" + participantId + "," + points + ")\n");
                        } else {
                            // Unsolved
                        }
                    }
                    writer.close();
                } catch (Exception e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                }
            }
        }
    }
}
