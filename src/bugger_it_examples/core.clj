(ns bugger-it-examples.core
  (:gen-class))


(defn inspect-me
  "A do-nothing function that you can put a breakpoint in to see what comes out
  of the debugger when you call it with various arguments."
  [my-arg & my-args]
  (let [my-map {:arg my-arg :args my-args}]
    (str my-map)))

(defn load-fib
  []
  (load "fibonacci")
  (use 'bugger-it-examples.fibonacci))
