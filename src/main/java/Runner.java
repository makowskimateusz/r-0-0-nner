import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by mat on 09.07.16.
 */
public class Runner {

    public Printer printer;
    public Parser parser;

    @Getter
    @Setter
    private Map<Long, User> usersById;
    @Getter
    @Setter
    private Map<String, File> filesById;

    public Runner(Printer printer){
        this.printer = printer;
        parser = new Parser(this);
    }

    public static void main(String[] args) throws IOException {

        Runner runner = new Runner(new Printer());
        runner.run(args);

    }

    public void run(String[] args){

        CommandLineParser commandLineParser = new DefaultParser();
        Options options = new Options();

        options.addOption("c", false, "CSV MODE");
        options.addOption("t", "top", true, "TOP N");

        try {
            CommandLine cmd = commandLineParser.parse(options, args);
            if (cmd.hasOption("c")) {
                parser.loadData(args[1], args[2]);
                this.runCSV();
            } else if (cmd.hasOption("t")) {
                parser.loadData(args[2], args[3]);
                this.runTopN(Integer.parseInt(cmd.getOptionValue("t")));
            } else {
                parser.loadData(args[0], args[1]);
                this.runAudit();
            }

        } catch (NumberFormatException e) {
            System.out.println("Error while parsing files. Reason: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Error while parsing arguments. Reason: " + e.getMessage());
        } catch (NoSuchFileException e) {
            System.out.println("Error, there is no file " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error, you didnt pass the files");
        } catch (IOException e) {
            System.out.println("Error while loading file: Reason " + e.getMessage());
        }

    }

    public void runAudit() {
        printer.printHeader();
        usersById.entrySet().stream()
                .forEach(user -> {
                    printer.printUserHeader(user.getValue().getUserName());
                        filesById.entrySet().stream().forEach(file -> {
                            if(file.getValue().getUserId() == user.getValue().getId()){
                                printer.printFile(file.getValue().getFileName(), file.getValue().getSize());
                            }
                        }
                     );
                });
    }

    public void runCSV() {
        filesById.entrySet().stream()
                .forEach(e -> printer.printCSV(e.getValue().getUser().getUserName(), e.getValue().getFileName(), e.getValue().getSize()));
    }

    public void runTopN(int range) {
        printer.printTopNHeader(range);
        sortFiles(filesById, range)
                .forEachOrdered(e -> printer.printTopN(e.getValue().getFileName(), e.getValue().getSize(), e.getValue().getUser().getUserName()));

        sortFiles(filesById, range)
                .forEachOrdered(e -> printer.printCSV(e.getValue().getUser().getUserName(), e.getValue().getFileName(), e.getValue().getSize()));
    }

    public Stream<Map.Entry<String, File>> sortFiles(Map<String, File> files, int range){
        return files.entrySet().stream()
                .sorted(Collections.reverseOrder((firstFile, secondFile) -> firstFile.getValue().getSize().compareTo(secondFile.getValue().getSize())))
                .limit(range);
    }

}
