/**
 * Created by mat on 11.07.16.
 */
public class Printer {

    public void printHeader() {
        System.out.println("Audit Report");
        System.out.println("============");
    }

    public void printTopNHeader(int range) {
        System.out.println("Top #" + range + " Report");
        System.out.println("============");
    }

    public void printUserHeader(String userName) {
        System.out.println("## User: " + userName);
    }


    public void printFile(String fileName, long fileSize) {
        System.out.println("* " + fileName + " ==> " + fileSize + " bytes");
    }


    public void printCSV(String user, String fileName, long fileSize){
        System.out.println(user + "," + fileName + "," + fileSize);
    }

    public void printTopN(String fileName, Long user, String fileSize){
        System.out.println("* " + fileName + " ==> user " + user + ", " + fileSize + " bytes");
    }

}
