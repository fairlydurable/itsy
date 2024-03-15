JAVAC = javac
JAVAC_FLAGS = -d $(OUT_DIR)
OUT_DIR = build/classes
PACKAGE = token
PACKAGE_DIR = .
SERVER_SOURCE = $(PACKAGE_DIR)/TokenServer.java
STORE_SOURCE = $(PACKAGE_DIR)/TokenStore.java

MAIN_CLASS = TokenServer

.PHONY: all run clean

all: $(OUT_DIR) $(OUT_DIR)/$(MAIN_CLASS).class

$(OUT_DIR):
	mkdir -p $(OUT_DIR)

$(OUT_DIR)/$(MAIN_CLASS).class: $(SERVER_SOURCE) $(STORE_SOURCE)
	$(JAVAC) $(JAVAC_FLAGS) $(SERVER_SOURCE) $(STORE_SOURCE)

run:
	java -classpath $(OUT_DIR) $(PACKAGE)/$(MAIN_CLASS)

clean:
	rm -rf $(OUT_DIR)
