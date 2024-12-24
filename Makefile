DOCKER_COMPOSE_PATH=.docker
DOCKER_COMPOSE_FILE=${DOCKER_COMPOSE_PATH}/docker-compose.yml

ifneq (,$(wildcard ${DOCKER_COMPOSE_PATH}/.env))
    include ${DOCKER_COMPOSE_PATH}/.env
endif

APP_NETWORK = ${APP_NAME}_network

# Use Windows-friendly command for UID (username in this case)
UID = $(shell echo %USERNAME%)

help: ## Show this help message
	@echo 'Welcome to ${APP_NAME} make'
	@echo 'usage: make [target]'
	@echo
	@echo 'targets:'
	@egrep '^(.+)\:\ ##\ (.+)' ${MAKEFILE_LIST} | column -t -c 2 -s ':#'

run: ## Start the containers
	set U_ID=${UID} && docker compose --project-directory=${DOCKER_COMPOSE_PATH} --file ${DOCKER_COMPOSE_FILE} up ${params}

stop: ## Stop the containers
	set U_ID=${UID} && docker compose --project-directory=${DOCKER_COMPOSE_PATH} --file ${DOCKER_COMPOSE_FILE} stop

down: ## Stop the containers
	set U_ID=${UID} && docker compose --project-directory=${DOCKER_COMPOSE_PATH} --file ${DOCKER_COMPOSE_FILE} down -v

restart: ## Restart the containers
	$(MAKE) stop && $(MAKE) run

rebuild: ## Rebuilds all the containers
	echo ${DOCKER_COMPOSE_PATH}
	set U_ID=${UID} && docker compose --project-directory=${DOCKER_COMPOSE_PATH} --file ${DOCKER_COMPOSE_FILE} build
