package testrank.core;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.utils.SourceRoot;
import org.junit.jupiter.api.Test;
import testrank.model.Finding;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.github.javaparser.utils.SourceRoot.Callback.Result.DONT_SAVE;

public enum CheckTests {;

    public static List<Finding> checkTestsIn(final String directory) throws IOException {
        final var results = new ArrayList<Finding>();

        new SourceRoot(Paths.get(directory)).parse("",
            (localPath, absolutePath, result) -> {

                final var cu = result.getResult();
                if (cu.isEmpty()) return DONT_SAVE;

                for (final var type : cu.get().getTypes()) {
                    for (final var method : type.getMethods()) {
                        if (isTestMethod(method)) {

                            method.getBody().ifPresent(block -> {
                                NodeList<Statement> statements = block.getStatements();

                                int assertCount = 0;
                                for (Statement statement : statements) {
                                    if (isAssert(statement))
                                        assertCount++;
                                }

                                results.add(new Finding(type, method, assertCount, statements.size()));
                            });

                        }
                    }
                }

                return DONT_SAVE;
            });

        return results;
    }

    private static boolean isTestMethod(final MethodDeclaration method) {
        return method.getAnnotationByClass(Test.class).isPresent();
    }

    private static boolean isAssert(final Statement statement) {
        return statement != null && statement.toString().startsWith("assert");
    }


}
