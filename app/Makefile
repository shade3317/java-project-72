setup:
	./gradlew wrapper --gradle-version 8.7

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew clean install

run-dist:
	@./build/install/app/bin/app

run:
	./gradlew run

lint:
	./gradlew checkstyleMain

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

.PHONY: build