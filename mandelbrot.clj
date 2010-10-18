(import
 '(java.awt Color Graphics Dimension)
 '(javax.swing JPanel JFrame))

(use 'clojure.contrib.complex-numbers
     'clojure.contrib.generic.arithmetic)

(defn make-panel [w h render]
  (doto (proxy [JPanel] []
	  (paint [#^Graphics g] (render g)))
    (.setPreferredSize (Dimension. w h))))

(defn make-frame [& panel-args]
  (doto (JFrame.)
    (.add (apply make-panel panel-args))
    .pack
    .show))
