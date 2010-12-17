(import
 '(java.awt Color Graphics Dimension)
 '(javax.swing JPanel JFrame))

(use 'clojure.contrib.complex-numbers
     'clojure.contrib.generic.arithmetic
     'clojure.contrib.generic.math-functions)

(defn make-panel [w h render]
  (doto (proxy [JPanel] []
	  (paint [#^Graphics g] (render g)))
    (.setPreferredSize (Dimension. w h))))

(defn make-frame [& panel-args]
  (doto (JFrame.)
    (.add (apply make-panel panel-args))
    .pack
    .show))

; Returns the number of iterations for |z| to exceed 2
; or max-iter+1 if it doesn't within max-iter iterations.
(defn num-mandelbrot-iterations [#^complex c max-iter]
  (count (take-while #(<= (abs %) 2.0)
		     (take (inc max-iter)
			   (iterate #(+ (* % %) c) (complex 0.0 0.0))))))

(defn mandelbrot []
  (make-frame 768 512
	      (fn [#^Graphics g]
		(doseq [x (range 768) y (range 512)]
		  (let [num-iter (num-mandelbrot-iterations (complex (+ (/ x 256.0) -2.0)
								     (+ (/ y 256.0) -1.0))
							    30)]
		    (.setColor g (Color. (if (> num-iter 30) 0 (* num-iter 8)) 0 0))
		    (.fillRect g x y 1 1))))))
