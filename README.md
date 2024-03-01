# Itsy Bitsy Teeny Weeny Super Tiny Minimal Worker and Workflow Project

A teeny tiny working solution to make Temporal easier to learn. 

It's Gradle and it breaks your mind and your heart if you love Java but it reduces the complexity so immensely that you can just focus on learning Temporal concepts.

## Limits

Only for use with the local server. (Someday, Cloud too!).

See `tserve` and `tflow` from [Useful Commands](https://github.com/fairlydurable/useful-cmds) to make your life easier to go along with this.

## Things you need to know

**Pull your dependencies**: `gradle dependencies`. Only need to do this once.

**Build the Worker app**: `gradle build`

**Run the Worker**: `gradle runApp` Note: It will block and stay there, which is what you want, especially when you start learning. Any text output will show right there. Later there will be other ways through Activities to communicate and produce results.

**Clean up**: `gradle clean`. Most people won't need this, but it's there if you like a tidy folder.

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
