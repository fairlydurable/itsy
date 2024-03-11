# Itsy Bitsy Teeny Weeny Super Tiny Minimal Worker and Workflow Project

A teeny tiny working solution to make Temporal easier to learn. 

It's Gradle. This setup may break your mind and your heart if you love Java.  But...it reduces the complexity immensely so you can focus on learning Temporal concepts. These projects assume you know and love Java and want to learn (and love) Temporal.

Each project is in a different branch. If I've added notes to branches, use git log to read them.

See `tserve`, `tflow`, and `clflow` from [Useful Commands](https://github.com/fairlydurable/useful-cmds) to make your life easier.

## Folder Contents

Each project has the same top level folder, with varying contents in each of the main files. The contents include:

* **build.gradle** A minimal Gradle build file with no tests.
* **CoinFlipperWorkerApp** launches a new Worker.
* **CoinFlipperWorker** runs a Worker that polls either the local server or Temporal Cloud.
* **CoinFlipperWorkflow** A basic Workflow, which changes for each sample.
* **CoinFlipperActivities** Activities, which change for each sample.
* **/src** (optional) When this appears, additional source needed for more complex samples.

## Building and Running

**Pull your dependencies**: `gradle dependencies`. Only need to do this once.

**Build the Worker app**: `gradle build`

**Run the Worker**: `gradle run` Note: It will block and stay there, which is what you want, especially when you start learning. Any text output will show right there. Later there will be other ways through Activities to communicate and produce results.

**Clean up**: `gradle clean`. Depending on your [".gitignore"](https://gitignore.io) setup, your Gradle artifacts may not be tracked. For tidy enthusiasts, choose from the following:

* `rm -rf .gradle`.
* `git stash; git stash drop`
* If you feel _really_ courageous, `git reset --hard HEAD`. 

## Naming

* **Task Queue** CoinFlipperWorkflow-queue (`--queue`)
* **Workflow Definition** CoinFlipperWorkflow (`--type`)
* **Workflow Execution ID** CoinFlipperWorkflow-\<numberstring> derived from `date +%s`, the number of seconds since the Unix epoch. (`--workflow-id`, if used. If not, one is generated). Adjust this as you like.
* **Namespace** Set this up for yourself. (`--namespace`)
* **Address** Set this up for yourself, default port is 7233. (`--address`)
* **Certificate paths**: ~/.ssh/tcloud.pem and .key. Otherwise, adjust as needed. (`--tls-cert-path` and `--tls-key-path`)

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
