package seedu.tripbuddy.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;

public class InputHandlerTest {

    private InputHandler inputHandler;
    private Logger testLogger;
    private ExpenseManager expenseManager;

    @BeforeEach
    void setUp() {
        // Create a test logger.
        testLogger = Logger.getLogger("TestLogger");
        // Instantiate a test ExpenseManager with a sample budget.
        expenseManager = new ExpenseManager(1000);
        // Instantiate InputHandler with the injected logger and expenseManager.
        inputHandler = new InputHandler(testLogger, expenseManager);
    }

    @Test
    void handleUserInputTutorialTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            String output = inputHandler.handleUserInput("tutorial");
            assertTrue(output.contains("set-budget"));
            assertTrue(output.contains("add-expense"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void isQuitCommandTest_setBudget_expectFalse() {
        String userInput = "set-budget 135";
        boolean result = inputHandler.isQuitCommand(userInput);
        assertFalse(result);
    }

    @Test
    public void isQuitCommandTest_quit_expectTrue() {
        String userInput = "quit";
        boolean result = inputHandler.isQuitCommand(userInput);
        assertTrue(result);
    }

    @Test
    public void isQuitCommandTest_quitWithExtra_expectTrue() {
        String userInput = "quit 123";
        boolean result = inputHandler.isQuitCommand(userInput);
        assertTrue(result);
    }

    @Test
    public void handleUserInputTest_validInputs() {
        assertAll(
                () -> inputHandler.handleUserInput("tutorial"),
                () -> inputHandler.handleUserInput("set-budget 381"),
                () -> inputHandler.handleUserInput("adjust-budget 432"),
                () -> inputHandler.handleUserInput("view-budget"),
                () -> inputHandler.handleUserInput("create-category accommodation"),
                () -> inputHandler.handleUserInput("add-expense greek-meal -a 10"),
                () -> inputHandler.handleUserInput("set-category greek-meal -c food"),
                () -> inputHandler.handleUserInput("delete-expense greek-meal")
        );
    }

    @Test
    public void handleUserInputTest_invalidInputs() {
        assertAll(
                () -> inputHandler.handleUserInput("tuutorial"),
                () -> inputHandler.handleUserInput("set-budget"),
                () -> inputHandler.handleUserInput("create-category"),
                () -> inputHandler.handleUserInput("add-expense greek-meal -a twenty"),
                () -> inputHandler.handleUserInput("set-category greek-meal"),
                () -> inputHandler.handleUserInput("delete-expense greek-meal"),
                () -> inputHandler.handleUserInput("set-budget 100000000"),
                () -> inputHandler.handleUserInput("set-budget -1234")
        );
    }

    @Test
    public void handleUserInputTest_edgeCases() {
        assertAll(
                () -> inputHandler.handleUserInput("set-budget 0"),
                () -> inputHandler.handleUserInput("set-budget -100"),
                () -> inputHandler.handleUserInput("add-expense item -a 0"),
                () -> inputHandler.handleUserInput("add-expense item -a -5"),
                () -> inputHandler.handleUserInput("create-category Meals"),
                () -> inputHandler.handleUserInput("create-category "),
                () -> inputHandler.handleUserInput("add-expense Lunch -a 15 with extra text"),
                () -> inputHandler.handleUserInput("delete-expense "),
                () -> inputHandler.handleUserInput("view-expenses"),
                () -> inputHandler.handleUserInput("      view-expenses")
        );
    }
}
