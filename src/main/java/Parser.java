import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mat on 11.07.16.
 */
@RequiredArgsConstructor
public class Parser {

    private static final int OWNER_USER_ID = 3;
    private static final int FILE_NAME = 2;
    private static final int FILE_ID = 0;
    private static final int SIZE = 1;
    private static final int USER_ID = 0;
    private static final int USER_NAME = 1;
    private static final int USERS_ARGUMENT = 0;
    private static final int FILES_ARGUMENT = 1;

    final Runner runner;

    public void loadData(String... args) throws IOException {
        runner.setUsersById(parseUsersToMap(args[USERS_ARGUMENT]));
        runner.setFilesById(parseFilesToMap(args[FILES_ARGUMENT]));
        for (File file : runner.getFilesById().values()) {
            file.setUser(runner.getUsersById().get(file.getUserId()));
        }

    }

    public Map<Long, User> parseUsersToMap(String arg) throws IOException {
        return parseFile(arg)
                .map(a -> new User(Long.parseLong(a.get(USER_ID)), a.get(USER_NAME)))
                .collect(Collectors.toMap(User::getId, user -> user));
    }

    public Map<String, File> parseFilesToMap(String arg) throws IOException {
        return parseFile(arg)
                .map(a -> new File(Long.parseLong(a.get(OWNER_USER_ID)), a.get(FILE_NAME), a.get(FILE_ID), Long.parseLong(a.get(SIZE))))
                .collect(Collectors.toMap(File::getFileId, file -> file));
    }
    public Stream<List<String>> parseFile(String arg) throws IOException {
        return Files.readAllLines(Paths.get(arg)).stream().skip(1)
                .map(s -> Arrays.asList(s.split(",")));
    }

}
