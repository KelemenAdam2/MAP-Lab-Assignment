package view;

import controller.Controller;
import exceptions.MyException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyList;
import model.adt.MyStack;
import model.stmt.IStmt;
import repository.IRepository;
import repository.Repository;

import java.util.Scanner;

public class TextMenu {
    private IStmt selectedProgram = null;
    private final Scanner scanner = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("\n=== Toy Language Menu ===");
            System.out.println("1. Select a program");
            System.out.println("2. Run the selected program completely");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextLine()) break;
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> selectProgram();
                case "2" -> runSelectedProgram();
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Please choose 1, 2, or 0.");
            }
        }
    }

    private void selectProgram() {
        System.out.println("\n--- Available Programs ---");
        System.out.println("1. int v; v = 2; print(v)");
        System.out.println("2. int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)");
        System.out.println("3. bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)");
        System.out.print("Select program (1-3): ");

        if (!scanner.hasNextLine()) return;
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                selectedProgram = Examples.EX1;
                System.out.println("Program 1 selected.");
            }
            case "2" -> {
                selectedProgram = Examples.EX2;
                System.out.println("Program 2 selected.");
            }
            case "3" -> {
                selectedProgram = Examples.EX3;
                System.out.println("Program 3 selected.");
            }
            default -> System.out.println("Invalid program selection.");
        }
    }

    private void runSelectedProgram() {
        if (selectedProgram == null) {
            System.out.println("Error: No program selected. Please select a program first (Option 1).");
            return;
        }

        System.out.print("Display intermediate states? (y/n): ");
        boolean dflag = scanner.hasNextLine() && scanner.nextLine().trim().equalsIgnoreCase("y");

        try {
            PrgState state = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    selectedProgram
            );
            IRepository repo = new Repository(state);
            Controller controller = new Controller(repo, dflag);

            controller.allStep();
            System.out.println("Final Output: " + state.getOut());
        } catch (MyException | RuntimeException e) {
            System.out.println("Execution error: " + e.getMessage());
        }
    }
}