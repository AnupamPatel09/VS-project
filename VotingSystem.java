import java.util.*;

/**
 * Voting System Backend (CLI + UI support)
 */
public class VotingSystem {

    // --- Backend Data ---
    protected final Map<String, Integer> votes = new HashMap<>();  // candidate -> voteCount
    protected final Set<String> voterRegistry = new HashSet<>();   // voterIds that already voted

    private final Scanner sc = new Scanner(System.in);

    // --- CLI Entry Point ---
    public static void main(String[] args) {
        new VotingSystem().runCLI();
    }

    // --- CLI Menu ---
    private void runCLI() {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addCandidateCLI();
                case 2 -> listCandidates();
                case 3 -> castVoteCLI();
                case 4 -> showResultsCLI();
                case 5 -> showWinners();
                case 6 -> resetElection();
                case 7 -> removeCandidateCLI();
                case 0 -> System.out.println("Exiting. Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
            System.out.println();
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("==============================");
        System.out.println("         VOTING SYSTEM        ");
        System.out.println("==============================");
        System.out.println("1. Add candidate");
        System.out.println("2. List candidates");
        System.out.println("3. Cast vote");
        System.out.println("4. Show results");
        System.out.println("5. Show winner(s)");
        System.out.println("6. Reset election");
        System.out.println("7. Remove candidate");
        System.out.println("0. Exit");
    }

    private void addCandidateCLI() {
        String name = readNonEmpty("Enter candidate name: ");
        if (votes.containsKey(name)) {
            System.out.println("Candidate already exists.");
            return;
        }
        votes.put(name, 0);
        System.out.println("Added candidate: " + name);
    }

    private void listCandidates() {
        if (votes.isEmpty()) {
            System.out.println("No candidates added yet.");
            return;
        }
        System.out.println("Candidates:");
        int i = 1;
        for (String c : new TreeSet<>(votes.keySet())) { // alphabetical order
            System.out.printf("%d) %s (votes: %d)%n", i++, c, votes.get(c));
        }
    }

    private void castVoteCLI() {
        if (votes.isEmpty()) {
            System.out.println("No candidates available. Add candidates first.");
            return;
        }
        String voterId = readNonEmpty("Enter your Voter ID: ");
        if (voterRegistry.contains(voterId)) {
            System.out.println("This Voter ID has already voted!");
            return;
        }
        listCandidates();
        String candidate = readNonEmpty("Enter candidate name to vote for: ");
        if (!votes.containsKey(candidate)) {
            System.out.println("Candidate not found.");
            return;
        }
        votes.put(candidate, votes.get(candidate) + 1);
        voterRegistry.add(voterId);
        System.out.println("Vote cast successfully for " + candidate + "!");
    }

    private void showResultsCLI() {
        if (votes.isEmpty()) {
            System.out.println("No candidates to show.");
            return;
        }
        System.out.println("Results:");
        votes.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));
        System.out.println("Total votes: " + voterRegistry.size());
    }

    private void showWinners() {
        if (votes.isEmpty()) {
            System.out.println("No candidates available.");
            return;
        }
        int max = votes.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        if (max == 0) {
            System.out.println("No votes cast yet.");
            return;
        }
        System.out.println("Winner(s):");
        votes.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .forEach(e -> System.out.println(" - " + e.getKey() + " (" + max + " votes)"));
    }

    private void resetElection() {
        votes.replaceAll((k, v) -> 0);
        voterRegistry.clear();
        System.out.println("Election reset.");
    }

    private void removeCandidateCLI() {
        if (votes.isEmpty()) {
            System.out.println("No candidates to remove.");
            return;
        }
        listCandidates();
        String name = readNonEmpty("Enter candidate name to remove: ");
        if (!votes.containsKey(name)) {
            System.out.println("Candidate not found.");
            return;
        }
        votes.remove(name);
        System.out.println("Removed candidate: " + name);
    }

    // --- CLI Helpers ---
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Input cannot be empty.");
        }
    }

    // --- Methods for UI integration ---
    public void addCandidateFromUI(String name) {
        votes.putIfAbsent(name, 0);
    }

    public String castVoteFromUI(String voterId, String candidate) {
        if (voterRegistry.contains(voterId)) {
            return "This Voter ID has already voted!";
        }
        if (!votes.containsKey(candidate)) {
            return "Candidate not found!";
        }
        votes.put(candidate, votes.get(candidate) + 1);
        voterRegistry.add(voterId);
        return "Vote cast successfully for " + candidate + "!";
    }

    public void resetElectionFromUI() {
        votes.replaceAll((k, v) -> 0);
        voterRegistry.clear();
    }

    public void removeCandidateFromUI(String name) {
        votes.remove(name);
    }

    public Map<String, Integer> getResults() {
        return new HashMap<>(votes);
    }
}
