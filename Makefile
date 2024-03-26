# Check whether the 'mantra-update' schedule is already running
check:
	@sh -c '`which temporal` schedule list | grep -q mantra-update && echo "Schedule '\''mantra-update'\'' is running" || echo "Schedule '\''mantra-update'\'' not found"'

# Show the schedule details (Undocumented)
checkin:
	`which temporal` schedule list

# Start running the Temporal Development Server
start_server:
	@-`which tserve` start || true
	
# Open the Temporal Development Server Web UI
open_web:
	@-`which tserve` schedules || true

# Stop the worker process
stop_worker:
	@-pkill -f 'org.gradle.launcher.daemon.bootstrap.GradleDaemon' || true

# Stop and delete the 'mantra-update' Workflow schedule
stop_schedule:
	@-`which temporal` schedule delete --schedule-id "mantra-update" || true

# Build with Gradle
build:
	gradle build

clean:
	rm -rf build

# Run all Temporal Server startup commands
server: start_server open_web

# Run all stop commands
stop: stop_worker stop_schedule

# Run 'TutorialWorkerApp' in the background
worker:
	nohup gradle worker > /dev/null 2>&1 &
	
# Schedule the 'mantra-update' Workflow to begin repeating
schedule:
	gradle schedule > /dev/null 2>&1
	
# Send a signal to run the Workflow
signal:
	gradle signal > /dev/null 2>&1
	
# List the commands
list:
	@-echo "build * clean * server * worker * schedule * stop * check * checkin * start_server * open_web * stop_worker * stop_schedule * list"

# These commands are side-effects
.PHONY: build clean server worker schedule stop check checkin start_server open_web stop_worker stop_schedule list
