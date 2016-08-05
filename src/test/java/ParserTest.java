import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Created by mat on 12.07.16.
 */
public class ParserTest {

    String filesPath;
    String usersPath;
    Parser parser;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        filesPath = "src/test/files/correctFiles/files.csv";
        usersPath = "src/test/files/correctFiles/users.csv";
        parser = new Parser(new Runner(new Printer()));
    }

    @Test
    public void shouldParseFileToStream() throws Exception {
        Stream<List<String>> files = parser.parseFile(usersPath);
        Stream<List<String>> users = parser.parseFile(filesPath);

        assertEquals(files.findFirst().get(), Arrays.asList("1", "jpublic"));
        assertEquals(users.findFirst().get(), Arrays.asList("5974448e-9afd-4c9a-ac5a-9b1e84227820", "5372274", "pic.jpg", "2"));
    }

    @Test
    public void shouldParseToFiles() throws Exception {
        Map<String, File> map = parser.parseFilesToMap(filesPath);
        assertEquals(map.size(), 5);
    }

    @Test
    public void shouldParseToUsers() throws Exception {
        Map<Long, User> map = parser.parseUsersToMap(usersPath);
        assertEquals(map.size(), 2);

    }

    @Test
    public void shouldThrowExceptionWhenFileNotFound() throws Exception{
        exception.expect(NoSuchFileException.class);
        parser.parseFile(" ");
    }

    @Test
    public void shouldThrowExceptionWhenFileUsersContainsIncorrectData() throws Exception {
        exception.expect(NumberFormatException.class);
        parser.parseUsersToMap("src/test/files/incorrectFiles/users.csv");
    }

    @Test
    public void shouldThrowExceptionWhenFileFilesContainsIncorrectData() throws Exception {
        exception.expect(NumberFormatException.class);
        parser.parseUsersToMap("src/test/files/incorrectFiles/files.csv");
    }

    @Test
    public void shouldThrowExceptionWhenUserDidntPassFiles() throws Exception {
        exception.expect(ArrayIndexOutOfBoundsException.class);
        parser.loadData();
    }

}