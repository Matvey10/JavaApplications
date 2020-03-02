import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FileSystemWalker {
    private Path rootDirectory;
    private PatternFilter patternFilter;
    private int directoryNameLength;
    private static final int indent = 5;
    public FileSystemWalker (String rootDirectory){
        this.rootDirectory = Paths.get(rootDirectory);
        directoryNameLength = rootDirectory.length();
        byPassFileSystem(this.rootDirectory);
    }
    public FileSystemWalker(String rootDirectory, String regex){
        this.rootDirectory = Paths.get(rootDirectory);
        try{
            patternFilter = new PatternFilter(regex);
        }
        catch (PatternSyntaxException e){
            System.out.println(e.getMessage());
        }
        directoryNameLength = rootDirectory.length();
        byPassFileSystem(this.rootDirectory);
    }
    public void byPassFileSystem(Path path){
        formatOutput(rootDirectory.getParent(), rootDirectory, 0);
        byPassFileSystem(rootDirectory, 0);
    }
    private void byPassFileSystem(Path path, int level){
        level++;
        if (Files.isDirectory(path)){
            try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)){
                for (Path child : dirStream){
                    if (patternFilter!=null && patternFilter.match(path.relativize(child).toString())){
                       continue;
                    }
                    else {
                        formatOutput(path, child, level);
                        byPassFileSystem(child, level);
                    }
                }
            }
            catch (AccessDeniedException e1){
                formatOutputAndAccess(path.getParent(), path, level, false);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private void formatOutput(Path parentPath, Path currentPath, int level){
        formatOutputAndAccess(parentPath, currentPath, level, true);
    }
    private void formatOutputAndAccess(Path parentPath, Path currentPath, int level, boolean access){
        if (level!=0){
            for (int i = 0; i < directoryNameLength/2; i++){
                System.out.print(" ");
            }
        }
        for (int i = 0; i < level*indent; i++){
            System.out.print(" ");
        }
        for (int i = 0; i < level; i++){
            System.out.print("| ");
        }
        System.out.print("_");
        if (level==0 && access==true){
            System.out.println(currentPath);
        }
        else if (access==true)
            System.out.println(parentPath.relativize(currentPath));
        else
            System.out.println(parentPath.relativize(currentPath)+ " (access denied)");
    }
    public static class PatternFilter{
        private Pattern pattern;
        public PatternFilter(String regex) throws PatternSyntaxException {
            pattern = Pattern.compile(regex);
        }
        public boolean match(String text){
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches())
                return true;
            else
                return false;
        }
    }
}

