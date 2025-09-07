import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * Swing UI for Voting System
 */
public class VotingSystemUI extends JFrame {
    private final VotingSystem votingSystem;
    private final JTextField voterIdField = new JTextField(10);
    private final JTextField candidateField = new JTextField(10);
    private final JTextArea resultArea = new JTextArea(10, 30);
    private final JComboBox<String> candidateList = new JComboBox<>();

    public VotingSystemUI() {
        votingSystem = new VotingSystem(); // backend

        setTitle("Voting System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Add Candidate Panel ---
        JPanel addPanel = new JPanel();
        addPanel.add(new JLabel("Candidate:"));
        addPanel.add(candidateField);
        JButton addBtn = new JButton("Add Candidate");
        addBtn.addActionListener(this::addCandidate);
        JButton removeBtn = new JButton("Remove Candidate");
        removeBtn.addActionListener(this::removeCandidate);
        addPanel.add(addBtn);
        addPanel.add(removeBtn);

        // --- Voting Panel ---
        JPanel votePanel = new JPanel();
        votePanel.add(new JLabel("Voter ID:"));
        votePanel.add(voterIdField);
        votePanel.add(new JLabel("Candidate:"));
        votePanel.add(candidateList);
        JButton voteBtn = new JButton("Cast Vote");
        voteBtn.addActionListener(this::castVote);
        votePanel.add(voteBtn);

        // --- Results Panel ---
        JPanel resultPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton resultBtn = new JButton("Show Results");
        resultBtn.addActionListener(this::showResults);
        JButton resetBtn = new JButton("Reset Election");
        resetBtn.addActionListener(this::resetElection);
        buttonPanel.add(resultBtn);
        buttonPanel.add(resetBtn);

        resultPanel.add(buttonPanel, BorderLayout.NORTH);
        resultArea.setEditable(false);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // --- Frame Layout ---
        add(addPanel, BorderLayout.NORTH);
        add(votePanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- Actions ---
    private void addCandidate(ActionEvent e) {
        String name = candidateField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Candidate name cannot be empty.");
            return;
        }
        votingSystem.addCandidateFromUI(name);
        candidateList.addItem(name);
        candidateField.setText("");
    }

    private void removeCandidate(ActionEvent e) {
        String candidate = (String) candidateList.getSelectedItem();
        if (candidate == null) {
            JOptionPane.showMessageDialog(this, "No candidate selected to remove.");
            return;
        }
        votingSystem.removeCandidateFromUI(candidate);
        candidateList.removeItem(candidate);
        JOptionPane.showMessageDialog(this, "Removed candidate: " + candidate);
    }

    private void castVote(ActionEvent e) {
        String voterId = voterIdField.getText().trim();
        String candidate = (String) candidateList.getSelectedItem();
        if (voterId.isEmpty() || candidate == null) {
            JOptionPane.showMessageDialog(this, "Enter Voter ID and select a candidate.");
            return;
        }
        String result = votingSystem.castVoteFromUI(voterId, candidate);
        JOptionPane.showMessageDialog(this, result);
        voterIdField.setText("");
    }

    private void showResults(ActionEvent e) {
        Map<String, Integer> results = votingSystem.getResults();
        StringBuilder sb = new StringBuilder("Election Results:\n");
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            sb.append(entry.getKey()).append(": ")
              .append(entry.getValue()).append(" votes\n");
        }
        resultArea.setText(sb.toString());
    }

    private void resetElection(ActionEvent e) {
        votingSystem.resetElectionFromUI();
        resultArea.setText("");
        voterIdField.setText("");
        JOptionPane.showMessageDialog(this, "Election has been reset!");
    }

    // --- Entry Point ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VotingSystemUI::new);
    }
}
