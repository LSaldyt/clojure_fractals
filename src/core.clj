(ns hello-quil.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color 0
   :angle 0})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)})

; (defn draw-state [state]
;   ; Clear the sketch by filling it with light-grey color.
;   (q/background 240)
;   ; Set circle color.
;   (q/fill (:color state) 255 255)
;   ; Calculate x and y coordinates of the circle.
;   (let [angle (:angle state)
;         x (* 1N (q/cos angle))
;         y (* 1N (q/sin angle))]
;     ; Move origin point to the center of the sketch.
;     (q/with-translation [(/ (q/width) 2)
;                          (/ (q/height) 2)]
;       ; Draw the circle.
;       (q/ellipse x y N0 N0))))

(def max_iterations 5000)
; def mandelbrot(i, j, N):
;     x0 = (i/N)*(bx - ax) + ax # Scale
;     y0 = (j/N)*(by - ay) + ay # Scale
; ax, bx = -2.5, 1.0
; ay, by = -1.0, 1.0

(def ax -2.5)
(def bx 1.0)
(def ay -1.0)
(def by 1.0)

(def N 100)

(defn mandelbrot [i j]
  (let [x0 (+ (* (/ i N) (- bx ax)) ax)
        y0 (+ (* (/ j N) (- by ay)) ay)]
    (loop [x 0 y 0 iteration 0]
      (if (and (< (+ (* x x) (* y y)) 4) (< iteration max_iterations))
        (recur (+ (- (* x x) (* y y)) x0)
               (+ (* 2 x y) y0)
               (+ 1 iteration))
        iteration))))

(defn draw-state [state]
  (q/background 255)
  ; create image and draw gradient on it
  (let [im (q/create-image N N :rgb)]
    (dotimes [x N]
      (dotimes [y N] 
        (let [i (mandelbrot x y)]
          (q/set-pixel im x y (q/color i i i))))
      )
    (q/image im 0 0)))

(q/defsketch hello-quil
  :title "Fractal"
  :size [N N]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
