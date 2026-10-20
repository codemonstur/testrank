# testrank

A Maven plugin for quality-checking your tests.

Testrank parses your sources with [JavaParser](https://javaparser.org), finds every
JUnit Jupiter `@Test` method, and reports how many assertions each test contains
relative to its body size — so tests that execute code but never verify anything
stand out immediately.

Each test produces a `Finding`:

```java
public record Finding(String file, String method, int assertCount, int bodyLength) {}
```

## Usage

Bind the `check` goal (it defaults to the `validate` phase, so it runs early in the build):

```xml
<plugin>
    <groupId>com.github.codemonstur</groupId>
    <artifactId>testrank</artifactId>
    <version>0</version>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Or run it directly on a project that has it on the plugin path:

```sh
mvn com.github.codemonstur:testrank:check
```

The findings are written to the build log, one line per test source root.

### Configuration

| Parameter  | Default | Description                          |
|------------|---------|--------------------------------------|
| `enabled`  | `true`  | Set to `false` to skip the checks    |

## Standalone

The checker also works without Maven, from the CLI:

```java
testrank.Main  // runs CheckTests.checkTestsIn("src/test/java")
```

## Building

Built with Maven (Java 17) and wrapped by the usual Makefile:

```sh
make help       # list targets
make build      # mvn -DskipTests clean package
make test       # mvn test
make validate   # dependency updates + CVE scan + license check
```

Or, with [bob](https://github.com/codemonstur/bobthebuildtool), use the tasks in the `bobfile`:

```sh
bob build
bob update
```

## License

[MIT](LICENSE)
