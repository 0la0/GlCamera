attribute vec4 vertexPosition;
attribute vec2 vertexTexCoord;
uniform float inputVariable1;
uniform float inputVariable2;
uniform float inputVariable3;


varying vec2 texCoord;
varying float param1;
varying float param2;
varying float param3;

void main() {
  texCoord = vertexTexCoord;
  param1 = inputVariable1;
  param2 = inputVariable2;
  param3 = inputVariable3;
  gl_Position = vec4(vertexPosition.x, vertexPosition.y, 0.0, 1.0);
}
