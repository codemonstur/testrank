package testrank;

import testrank.core.CheckTests;

import java.io.IOException;

public enum Main {;

    public static void main(final String... args) throws IOException {
        System.out.println(CheckTests.checkTestsIn("src/test/java"));
    }

}
