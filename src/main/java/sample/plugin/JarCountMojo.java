package sample.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "jar")
public class JarCountMojo extends AbstractMojo {
	
	@Parameter(property="dependenciesPath",required=true)
	public String dependencisePath ;
	
	public int count = 0;

	public void execute() throws MojoExecutionException, MojoFailureException {
		Path path = Paths.get(dependencisePath);

		if (Files.exists(path)) {
			try {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(Path file,
							BasicFileAttributes attrs) throws IOException {
						PathMatcher matcher = 
								FileSystems.getDefault().getPathMatcher("glob:*.jar");
						Path name = file.getFileName();
						if(null != name && matcher.matches(name)){
							count++;
						}
					
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.getLog().info("contain jars :" + count);
		}else{
			this.getLog().info("Please confirm if dependency jars are in "+dependencisePath);
		}
	}

}
