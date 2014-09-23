(ns bugger-it-examples.fibonacci
  "Everybody's favourite my-first-example function.")

(defn stupid-fib
  [n]
  (condp = n
    0 0
    1 1
    (+ (stupid-fib (- n 1)) (stupid-fib (- n 2)))))
