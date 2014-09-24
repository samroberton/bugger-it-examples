# bugger-it-examples

**Very much a work in progress --- please be patient!**

See [`bugger-it`](https://githug.com/samroberton/bugger-it) for details. This
project is a collection of introductory examples for `bugger-it`, to get you
going and hopefully convince you that `bugger-it` is worth trying out. The idea
is to run some code in the `bugger-it-examples` project while debugging it from
a separate JVM process running `bugger-it`.

## Installation

Download from https://github.com/samroberton/bugger-it-examples.

You'll also need [bugger-it](https://githug.com/samroberton/bugger-it) for this
to be of any use to you at all.

## Usage

I'm going to explain this on the assumption that you are running `bugger-it` and
the examples in plain `lein repl`s. If you're running them through CIDER or
Eclipse or whatever else, there's no reason that shouldn't work.

First, start up the debuggee (this project). You'll want to enable the JVM debug
interface and disable Clojure locals clearing, so there's a (very simple!)
[`repl.sh`](https://github.com/samroberton/bugger-it-examples/blob/master/repl.sh)
which will do that for you.

Pay attention to the first line of output:
```
[sam@ubuntu ~/git/bugger-it-examples]$ ./repl.sh
Listening for transport dt_socket at address: 40950
```

Now, in the `bugger-it` project:
```
[sam@ubuntu ~/git/bugger-it]$ lein repl
...
bugger-it.core=> (load "repl")
nil
bugger-it.core=> (def d (connect-to-vm "localhost" 40950))
```

Congratulations. Debugger attached. `d` is a `Debuggee` object that we'll hold
the debuggee VM handle in for our REPL session.

## Examples

### Fibonacci

First off, let's start with something simple: Fibonacci numbers. In the examples
project, in `fibonacci.clj`, there's a particularly brain-dead implementation of
a function to calculate the `nth` Fibonacci number. We're going to trace its
execution (logging function entry and exit).

In the REPL session of the debugger (the `bugger-it` project):
```
bugger-it.core=> (bugger-it.repl/trace-fn d 'bugger-it-examples.fibonacci/stupid-fib)
[#<MethodEntryRequestImpl method entry request  (enabled)> #<MethodExitRequestImpl method exit request (enabled)>]
```

Now, in the REPL session of the debuggee (the examples project):
```
bugger-it-examples.core=> (load "fibonacci")
nil
bugger-it-examples.core=> (bugger-it-examples.fibonacci/stupid-fib 6)
8
```

So, let's run Rich Hickey's "ants" demo. In the REPL session of the debuggee
(the examples project):
```
bugger-it-examples.core=> (load "ants")
nil
```

You should see a `JFrame` pop up now, and the ants starting to run around
collecting food.

Great. But I don't understand what's going on. So let's debug the code.
Specifically, let's see what this `behave` method does. On line 178.

In the debugger process (the `bugger-it` REPL):
```
bugger-it.core=> (def t (bugger-it.repl/suspend-and-save-at-point d (as-java-class-name 'bugger-it-examples.core/behave) 178))
#'bugger-it.core/t
```
This `t` that you get back is an atom. You've just installed a breakpoint, and
when threads in the debuggee hit it, they'll suspend. (You should have seen a
heap of output in your REPL indicating breakpoints being hit as soon as you
eval'd that last form.) Have a look at `@t` and you'll see there's a vector of
suspended threads.

Let's look at one of them:
```
bugger-it.core=> (bugger-it.repl/arguments (first @t))
([23 45])
```

Ok, interesting, I guess. The arguments to `behave` for this thread were `[23 45]`.

More interesting is all the visible variables in this thread.

```
bugger-it.core=> (bugger-it.repl/visible-variables (first @t))
```

You can resume an individual thread with, eg, `(.resume (first @t))`. (Although
if you do, you'll notice it'll almost immediately hit the breakpoint again.)
Note that when you do this, your thread reference for that thread in `@t` is no
longer valid -- but it's still there. So you might want to remove it, or just
always use `(last @t)` as your chosen thread of interest.


## License

Copyright © 2014 Sam Roberton (with Ants demo copyright Rich Hickey)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

The Ants demo is distributed under the Common Public License, as described in
the comment at the top of its source file.
