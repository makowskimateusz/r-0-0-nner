import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Created by mat on 11.07.16.
 */
public class RunnerTest {

    public static final int FIRST_FILE = 0;
    public static final int SECOND_FILE = 1;
    public static final int THIRD_fILE = 2;
    Runner runner;
    String expectedOutput;
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    List<File> sortedFiles;


    @Before
    public void setUp() throws Exception {

        String[] paths = new String[2];

        paths[0] = "src/test/files/correctFiles/users.csv";
        paths[1] = "src/test/files/correctFiles/files.csv";

        runner = new Runner(new Printer());
        Parser parser = new Parser(runner);
        parser.loadData(paths);

        sortedFiles = new LinkedList<>();

        System.setOut(new PrintStream(outContent));

    }

    @After
    public void tearDown(){
        outContent = null;
    }

    @Test
    public void shouldPrintAuditMode() throws Exception {

        expectedOutput = "Audit Report\n" +
                "============\n" +
                "## User: jpublic\n" +
                "* movie.avi ==> 734003200 bytes\n" +
                "* audit.xlsx ==> 1638232 bytes\n" +
                "* marketing.txt ==> 150680 bytes\n" +
                "## User: atester\n" +
                "* holiday.docx ==> 570110 bytes\n" +
                "* pic.jpg ==> 5372274 bytes\n";

        runner.runAudit();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void shouldPrintCSV() throws Exception {

        expectedOutput = "atester,holiday.docx,570110\n" +
        "jpublic,movie.avi,734003200\n" +
        "jpublic,audit.xlsx,1638232\n" +
        "jpublic,marketing.txt,150680\n" +
        "atester,pic.jpg,5372274\n";

        runner.runCSV();
        assertEquals(expectedOutput, outContent.toString());

    }

    @Test
    public void shouldPrintTopNFiles() throws Exception {

        expectedOutput = "Top #3 Report\n" +
                "============\n" +
                "* movie.avi ==> user 734003200, jpublic bytes\n" +
                "* pic.jpg ==> user 5372274, atester bytes\n" +
                "* audit.xlsx ==> user 1638232, jpublic bytes\n";

        runner.runTopN(3);
        assertEquals(expectedOutput, outContent.toString());

    }

    @Test
    public void shouldSortFiles() throws Exception {

        Stream<Map.Entry<String, File>> stream = runner.sortFiles(runner.getFilesById(), 3);
        stream.forEachOrdered(file -> sortedFiles.add(file.getValue()));

        assertEquals(sortedFiles.get(FIRST_FILE).getSize(), new Long(734003200));
        assertEquals(sortedFiles.get(SECOND_FILE).getSize(), new Long(5372274));
        assertEquals(sortedFiles.get(THIRD_fILE).getSize(), new Long(1638232));


    }

}