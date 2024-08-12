import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Question {
    private String question;
    private String[] options;
    private int correctOption;
    private int maxAttempts;
    private int attempts;

    public Question(String question, String[] options, int correctOption, int maxAttempts) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
        this.maxAttempts = maxAttempts;
        this.attempts = 0;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean isCorrect(int userChoice) {
        attempts++;
        return userChoice == correctOption;
    }
}

class Quiz {
    private List<Question> questions;
    private int score;

    public Quiz(List<Question> questions) {
        this.questions = questions;
        this.score = 0;
        this.start();
    }


    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Quiz Application! Press any key to start or 'q' to quit.");
        String startOrQuit = scanner.nextLine();
        if (startOrQuit.equalsIgnoreCase("q")) {
            System.out.println("Quiz quit!");
            return;
        }
        System.out.print("Enter the number of Questions you want : ");
        int questionSize = scanner.nextInt();
        System.out.print("Enter the number of Options you want : ");
        int optionSize = scanner.nextInt();
        System.out.print("Enter the number of Attempts you want : ");
        int AttemptSize = scanner.nextInt();
        

        Collections.shuffle(questions);
        List<Question> quizQuestions = questions.subList(0, Math.min(questionSize, questions.size()));
        for (Question question : quizQuestions) {
                System.out.println(question.getQuestion());
                String[] options = question.getOptions();
                String[] modifiedOptions = new String[optionSize];
                for(int s = 0 ; s < optionSize ; s++){
                    modifiedOptions[s] = options[s];
                }  
                for (int i = 0; i < modifiedOptions.length; i++) {
                    System.out.println((i + 1) + ". " + modifiedOptions[i]);
                }
                int remainingAttempts = AttemptSize;
                while (remainingAttempts > 0) {
                    System.out.print("Enter your choice: ");
                    scanner.nextLine();
                    String userInput = scanner.nextLine();
                    System.out.println(userInput);
                    if (userInput.equalsIgnoreCase("q")) {
                        System.out.println("oops ! you left the Quiz your Score is = "+ score +"/" + questionSize);
                        return;
                    }
                    int userChoice;
                    try {
                        userChoice = Integer.parseInt(userInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a valid option or 'q' to quit.");
                        continue;
                    }
                    if (userChoice < 1 || userChoice > options.length) {
                        System.out.println("Invalid choice! Please enter a valid option or 'q' to quit.");
                        continue;
                    }
                    if (question.isCorrect(userChoice)) {
                        System.out.println("Correct!");
                        score++;
                        break;
                    } else {
                        System.out.println("Incorrect! Remaining attempts: " + (--remainingAttempts));
                        if (remainingAttempts == 0) {
                            System.out.println("Out of attempts for this question!");
                        }
                    }
                }
                System.out.println();
            }
        System.out.println("Quiz completed! Your score: " + score + "/" + questionSize);
    }
}

public class project {
    public static void main(String[] args) {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("questions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                String questionText = parts[0];
                String[] options = parts[1].split(",");
                int correctOption = Integer.parseInt(parts[2]);
                int maxAttempts = Integer.parseInt(parts[3]);
                questions.add(new Question(questionText, options, correctOption, maxAttempts));
            }
        } catch (IOException e) {
            System.out.println("Error reading questions file: " + e.getMessage());
            return;
        }

        new Quiz(questions);
    }
}