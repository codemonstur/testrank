.PHONY: help clean build test validate release-notes coverage

NAME=`xmllint --xpath "//project/artifactId/text()" pom.xml`
VERSION=`xmllint --xpath "//project/version/text()" pom.xml`
PREVIOUS_TAG=`git tag | sort -r | head -n 1`

# Blue [INFO] prefix before every message, NO_COLOR=1 prints it plain
ifneq ($(NO_COLOR),)
INFO = printf '[INFO] %s\n'
else
INFO = printf '[\033[1;34mINFO\033[m] %s\n'
endif

help:
	@echo "Available targets for $(NAME):"
	@echo "  help   -        this output"
	@echo "  clean  -        Delete everything in ./target"
	@echo "  build  -        Compiles and packages the code"
	@echo "  test   -        Runs the tests"
	@echo "  coverage   -        Run tests and generate a jacoco coverage report"
	@echo "  validate -        Check dependency versions, cves, and licenses"
	@echo "  release-notes -        Create release notes for the latest version"

clean:
	@$(INFO) "Cleaning"
	@mvn clean

build:
	@mvn -DskipTests clean package

test:
	@mvn test

# Runs the tests and writes a coverage report to target/site/jacoco
# Noisy plugin banner lines are silenced through .mvn/maven.config
coverage:
	@$(INFO) "Running tests and generating coverage report"
	@mvn -Dstyle.color=always org.jacoco:jacoco-maven-plugin:0.8.15:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.15:report
	@$(INFO) "Coverage report written to target/site/jacoco/index.html"
	@output=$$(java src/build/CoverageSummary.java target/site/jacoco/jacoco.csv) || exit $$?; \
	printf '%s\n' "$$output" | while IFS= read -r line; do $(INFO) "$$line"; done

validate:
	@mvn versions:display-dependency-updates
	@mvn versions:display-plugin-updates
	@mvn validate -P vulnerabilities
	@mvn validate -P licenses

release-notes:
	@$(INFO) "Writing release notes to src/docs/releases/release-$(VERSION).txt"
	@mkdir -p src/docs/releases
	@echo "$(VERSION)" >> src/docs/releases/release-$(VERSION).txt
	@echo "" >> src/docs/releases/release-$(VERSION).txt
	@git log --pretty="%s" $(PREVIOUS_TAG)... master >> src/docs/releases/release-$(VERSION).txt
