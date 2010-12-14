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
; or nil if it never does.
(defn num-mandelbrot-iterations [#^complex c max-iter]
  (loop [z (complex 0.0 0.0) num-iter 0]
    (if (> num-iter max-iter)
      nil
      (if (> (abs z) 2.0)
	num-iter
	(recur (+ (* z z) c) (inc num-iter))))))

(defn mandelbrot []
  (make-frame 768 512
	      (fn [#^Graphics g]
		(let [dot (fn [x y c]
			    (.setColor g c)
			    (.fillRect g x y 1 1))]
		  (doseq [x (range 768) y (range 512)]
		    (let [max-iter 30
			  inv-max-iter (int (/ 255.0 max-iter))
			  num-iter (num-mandelbrot-iterations (complex (+ (* (/ x 768.0) 3.0) -2.0)
								       (+ (* (/ y 512.0) 2.0) -1.0))
							      max-iter)]
		      (dot x y (if (nil? num-iter)
				 (Color. 0 0 0)
				 (Color. (* num-iter inv-max-iter) 0 0)))))))))
