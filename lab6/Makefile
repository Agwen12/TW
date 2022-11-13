SHELL=/bin/bash
# define standard colors
ifneq (,$(findstring xterm,${TERM}))
	BLACK        := $(shell tput -Txterm setaf 0)
	RED          := $(shell tput -Txterm setaf 1)
	GREEN        := $(shell tput -Txterm setaf 2)
	YELLOW       := $(shell tput -Txterm setaf 3)
	LIGHTPURPLE  := $(shell tput -Txterm setaf 4)
	PURPLE       := $(shell tput -Txterm setaf 5)
	BLUE         := $(shell tput -Txterm setaf 6)
	WHITE        := $(shell tput -Txterm setaf 7)
	RESET := $(shell tput -Txterm sgr0)
else
	BLACK        := ""
	RED          := ""
	GREEN        := ""
	YELLOW       := ""
	LIGHTPURPLE  := ""
	PURPLE       := ""
	BLUE         := ""
	WHITE        := ""
	RESET        := ""
endif


# set target color
TARGET_COLOR := $(BLUE)

POUND = \#

PRODUCERS=5
CONSUMERS=5
SEED=2137
BUFF=10
LOGGER_CONF=/home/agwen/sem5/TW/lab6/log4j.properties
PID_FILE=/home/agwen/sem5/TW/lab6/run.pid
TASK_TIMEOUT=1
PROPS=

build: ## clean build
	mvn clean
	mvn install

run: build ## run with predefined config and clean build
	java -Dproducer.count=${PRODUCERS} \
		-Dconsumer.count=${CONSUMERS} \
		-Dbuffer.size=${BUFF} \
		-Dseed=${SEED} \
		-Dlogger.configuration=${LOGGER_CONF} \
		-Dpid.file=${PID_FILE} \
		-Dtask.timeout=${TASK_TIMEOUT} \
		-jar ./target/lab6-1.0-jar-with-dependencies.jar

run_fast: ## run with predefined config
	java -Dproducer.count=${PRODUCERS} \
     	-Dconsumer.count=${CONSUMERS} \
     	-Dbuffer.size=${BUFF} \
     	-Dseed=${SEED} \
     	-Dlogger.configuration=${LOGGER_CONF} \
     	-Dpid.file=${PID_FILE} \
     	-Dtask.timeout=${TASK_TIMEOUT} \
    	-jar ./target/lab6-1.0-jar-with-dependencies.jar


run_prop: ## run with configuration from file
	java -Dproperties.file=${PROPS} -jar ./target/lab6-1.0-jar-with-dependencies.jar

stop: ## stop main process
	kill $(shell cat run.pid)

colors: ## show all the colors
	@echo "${BLACK}BLACK${RESET}"
	@echo "${RED}RED${RESET}"
	@echo "${GREEN}GREEN${RESET}"
	@echo "${YELLOW}YELLOW${RESET}"
	@echo "${LIGHTPURPLE}LIGHTPURPLE${RESET}"
	@echo "${PURPLE}PURPLE${RESET}"
	@echo "${BLUE}BLUE${RESET}"
	@echo "${WHITE}WHITE${RESET}"

help:
	@echo "${BLACK}-----------------------------------------------------------------${RESET}"
	@grep -E '^[a-zA-Z_0-9%-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "${TARGET_COLOR}%-30s${RESET} %s\n", $$1, $$2}'

.DEFAULT_GOAL := help
.PHONY: build run run_fast run_prop



