SRC_DIR := src
OUT_DIR := build/classes
PACKAGE := unreliable
MAIN_CLASS := CoffeeServer

JAVAC := javac
JAVA := java
JAVAC_FLAGS := -d $(OUT_DIR) -cp $(OUT_DIR)
JAVA_FLAGS := -cp $(OUT_DIR)

.PHONY: all build run clean

all: build

build:
	@mkdir -p $(OUT_DIR)
	$(JAVAC) $(JAVAC_FLAGS) $(MAIN_CLASS).java

run:
	@$(JAVA) $(JAVA_FLAGS) $(PACKAGE).$(MAIN_CLASS) $(filter-out $@,$(MAKECMDGOALS))
	@false

clean:
	@rm -rf build

