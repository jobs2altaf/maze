package uk.gov.dwp.maze.explorer;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.maze.Maze;
import uk.gov.dwp.maze.explorer.direction.ExplorerDirectionService;
import uk.gov.dwp.maze.explorer.moves.ExplorerNextCellFinder;
import uk.gov.dwp.maze.explorer.moves.strategy.impl.ExplorerNextCellFinderStrategyForForwardMove;
import uk.gov.dwp.maze.explorer.moves.strategy.impl.ExplorerNextCellFinderStrategyForLeftMove;
import uk.gov.dwp.maze.explorer.moves.strategy.impl.ExplorerNextCellFinderStrategyForRightMove;
import uk.gov.dwp.maze.model.Cell;
import uk.gov.dwp.maze.model.CellState;
import uk.gov.dwp.maze.explorer.model.ExplorerMoveType;
import uk.gov.dwp.maze.util.MazeUtilException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
//**NOTE** NO mocks in this test for time constraint
public class ExplorerTest {

    private Explorer explorer;

    @Before
    public void setup() throws MazeUtilException {
        String fileName = "src//test//resources//Maze-test.txt";
        Maze maze = new Maze(fileName);
        explorer = new Explorer(maze);
        explorer.setExplorerDirectionService(new ExplorerDirectionService());
        explorer.setExplorerNextCellFinder(new ExplorerNextCellFinder(
                new ExplorerNextCellFinderStrategyForForwardMove(),
                new ExplorerNextCellFinderStrategyForLeftMove(),
                new ExplorerNextCellFinderStrategyForRightMove()
        ));
    }

    @Test
    public void testMoveForward() {

        explorer.move(ExplorerMoveType.FORWARD);
        assertEquals(new Cell(1,1, ' '), explorer.getCurrentCell());
        int expectedTimesOfUserMovedForwarded = explorer.getNoOfTimesMovedForward();
        assertEquals(expectedTimesOfUserMovedForwarded, 1);
    }

    @Test
    public void testMoveForwardFailed() throws MazeUtilException, IOException {

        Cell expectedCell = new Cell(1,1, ' ');

        explorer.move(ExplorerMoveType.FORWARD);
        assertEquals(expectedCell, explorer.getCurrentCell());
        explorer.move(ExplorerMoveType.FORWARD);
        assertEquals(expectedCell, explorer.getCurrentCell());
    }

    @Test
    public void testTurnRight() throws MazeUtilException, IOException {

        explorer.move(ExplorerMoveType.RIGHT);
        assertEquals(new Cell(2,2, ' '), explorer.getCurrentCell());
    }

    @Test
    public void testTurnRightFailed() throws MazeUtilException, IOException {

        Cell expectedCell = new Cell(1,1, ' ');

        explorer.move(ExplorerMoveType.FORWARD);
        explorer.move(ExplorerMoveType.RIGHT);
        assertEquals(expectedCell, explorer.getCurrentCell());
        explorer.move(ExplorerMoveType.RIGHT);
        assertEquals(expectedCell, explorer.getCurrentCell());
    }

    @Test
    public void testTurnLeft() throws MazeUtilException, IOException {

        explorer.move(ExplorerMoveType.LEFT);
        assertEquals(new Cell(2,1, 'S'), explorer.getCurrentCell());
    }

    @Test
    public void testTurnLeftFailed() throws MazeUtilException, IOException {

        Cell expectedCell = new Cell(2,1, 'S');

        explorer.move(ExplorerMoveType.LEFT);
        assertEquals(expectedCell, explorer.getCurrentCell());
        explorer.move(ExplorerMoveType.LEFT);
        assertEquals(expectedCell, explorer.getCurrentCell());
    }


    @Test
    public void testFindWhatIsInFront() throws MazeUtilException, IOException {

        CellState cellState = explorer.findWhatIsInFront();
        assertEquals(CellState.OPEN, cellState);
    }

    @Test
    public void testGetAvailableMovementsFromMyCurrentCell() throws MazeUtilException, IOException {

        List<ExplorerMoveType> availableMovements = explorer.getAvailableMovementsFromCurrentCell();

        List<ExplorerMoveType> lstExplorerMoveType = new ArrayList<>();
        lstExplorerMoveType.add(ExplorerMoveType.RIGHT);
        lstExplorerMoveType.add(ExplorerMoveType.FORWARD);
        assertEquals(lstExplorerMoveType, availableMovements);
    }

    @Test
    public void testToStringReadableFormat() throws MazeUtilException, IOException {

        explorer.move(ExplorerMoveType.FORWARD);
        String stringReturned = explorer.toStringReadableFormat();
        assertEquals(Files.lines(Paths.get("src//test//resources//Maze-test-expected-output-after-usermove.txt"),
                StandardCharsets.US_ASCII).collect(Collectors.joining("\n")), stringReturned.trim());
    }


}
