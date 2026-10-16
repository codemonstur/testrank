package testrank;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import static org.apache.maven.plugins.annotations.LifecyclePhase.VALIDATE;
import static testrank.core.CheckTests.checkTestsIn;

@Mojo(defaultPhase = VALIDATE, name = "check", threadSafe = true)
public final class MavenCheckTests extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    public MavenProject project;

    @Parameter(defaultValue = "true")
    public boolean enabled;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!enabled) return;

        final var log = getLog();
        try {
            for (final var testSourceRoot : project.getCompileSourceRoots()) {
                final var results = checkTestsIn(testSourceRoot);
                log.info(results.toString());
            }
        } catch (final Exception e) {
            throw new MojoExecutionException("Error checking tests", e);
        }

    }

}
