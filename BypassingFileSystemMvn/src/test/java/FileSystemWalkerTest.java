import org.junit.Test;

import java.util.regex.PatternSyntaxException;

public class FileSystemWalkerTest {
    @Test
    public void testWithRegex(){
        FileSystemWalker fileSystemWalker = new FileSystemWalker("C:\\Users\\user\\Desktop\\A", "[A-Z]1");
    }
    @Test()
    public void testWithInvalidRegex(){
        FileSystemWalker fileSystemWalker = new FileSystemWalker("C:\\Users\\user\\Desktop\\A", "$'~[Al-Z]1");
    }
    @Test
    public void testWithoutRegex(){
        FileSystemWalker fileSystemWalker = new FileSystemWalker("C:\\Users\\user\\Desktop\\A");
    }
}
