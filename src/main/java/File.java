import lombok.Data;

/**
 * Created by mat on 11.07.16.
 */
@Data
public class File {

    private final long userId;
    private final String fileName;
    private final String fileId;
    private final Long size;
    private User user;

}
