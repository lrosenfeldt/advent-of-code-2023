CC = docker run --rm -v $(PWD):/app
FLAGS = --memory 2g --cpus 4
CONTAINER_TAG = kotlin-aoc

.PHONY: run clean docker

run: build/Day11.jar
	java -jar ./build/Day11.jar $(FILE)

build/Day11.jar: day11/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day11/Main.kt $(CONTAINER_TAG)

build/Day10.jar: day10/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day10/Main.kt $(CONTAINER_TAG)

build/Day09.jar: day09/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day09/Main.kt $(CONTAINER_TAG)

build/Day08.jar: day08/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day08/Main.kt $(CONTAINER_TAG)

build/Day07.jar: day07/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day07/Main.kt $(CONTAINER_TAG)

build/Day06.jar: day06/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day06/Main.kt $(CONTAINER_TAG)

build/Day05.jar: day05/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day05/Main.kt $(CONTAINER_TAG)

build/Day04.jar: day04/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day04/Main.kt $(CONTAINER_TAG)

build/Day03.jar: day03/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day03/Main.kt $(CONTAINER_TAG)

build/Day02.jar: day02/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day02/Main.kt $(CONTAINER_TAG)

build/Day01.jar: day01/Main.kt
	$(CC) $(FLAGS) --env OUTPUT=$@ --env TARGET=day01/Main.kt $(CONTAINER_TAG)

docker: Dockerfile
	docker build -t kotlin-aoc .

clean:
	rm -rf ./build
	mkdir ./build

