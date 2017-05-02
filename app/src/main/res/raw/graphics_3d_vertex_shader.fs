uniform mat4 mvpMatrix;
attribute vec4 position;
attribute vec4 color;
varying vec4 _color;
void main() {
  _color = color;
  gl_Position = mvpMatrix * position;
}