# Webpage Update Worker

NOTE: This branch also adds basic signals with `MantraSignaler`. Use `make signal` and you don't have to set a schedule.

## Overview

This project is designed to update a Webpage every three minutes, providing fresh content. It uses the Temporal server to schedule Workflows that execute updates, ensuring that the content remains dynamic and engaging.

## Prerequisites

Before proceeding with the setup and execution of the project, ensure you have the following installed:

- Java JDK (version 8 or newer)
- Gradle (for building the Java project)
- Temporal Server (for scheduling and managing tasks)

## Getting Started

To get the project up and running, follow these steps:

### Build the Project

Compile the Java source to prepare the worker and scheduler for execution.

```bash
make

--or--

make build
```

## Start the Server

Initiate the Temporal Development Server so it can manage the scheduled workflow, and the workflow's activities.

```
make server
```

## Run the Worker

Start the Worker so it's available to kick off Workflows and Activities as they appear in its polled Task Queues.

```
make worker
```

## Establish the Schedule

Schedule the workflow that triggers the webpage update.

```
make schedule
```

## Check the Schedule

Verify that the schedule has been correctly established.

```
make check
```

View the schedule from the command line. It tells you when the next update will occur.

```
`which temporal` schedule list
```

View the schedule on the Web page

```
make open_web
```

# Make Commands

This project includes a Makefile that simplifies the process of building, running, and managing the project components. The available commands are as follows:

* `make build`: Compiles the Java source files.
* `make server`: Starts the Temporal Development Server.
* `make worker`: Initiates the worker process.
* `make schedule`: Triggers the scheduling of the webpage update * workflow.
* `make check`: Checks whether the update schedule is in place.
* `make open_web`: Opens the Temporal Server Web UI Schedules page for monitoring.
* `make stop_worker`: Stops the worker process.
* `make stop_schedule`: Stops and deletes the update schedule.
* `make stop`: Stops the worker process, and stops and deletes the update schedule.
* `make clean`: Removes build artifacts to clean up the project directory.

There are a few extra items in the Makefile if you look carefully.

# Stopping the Project

To stop and clean up all components of the project, use the following commands:

**Stop the worker process**

```
make stop_worker
```

**Stop and delete the schedule**

```
make stop_schedule
```

**Perform both stop\_worker and stop\_schedule**

```
make stop
```

**Clean the build artifacts**

```
make clean
```

# Project Structure

* **TutorialWorkerApp.java** - Entry point for the Worker app
* **TutorialWorker.java** - A basic Worker. It polls the 'TutorialWorkflow-queue' Task Queue.
* **TutorialWorkflow.java** Hosts the Workflow that calls the update activity.
* **TutorialActivities.java** - Hosts the update activity that sends new content to the [Mantra Webpage](http://146.190.45.234:8888).
* **MantraScheduler.java** - Standalone scheduling app. 
    * Sets the 'mantra-update' schedule. The schedule places the TutorialWorkflow onto the TutorialWorkflow-queue every three minutes. 
    * The Workflow ID is always 'tutorial-mantra-workflow'. 		
* **Mantra.java** - Defines the Mantra class and its one public method: `getRandomMantra()`, which returns a String.

## License

MIT License

Copyright (c) 2020 temporal.io

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
