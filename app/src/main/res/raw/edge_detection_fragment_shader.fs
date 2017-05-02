#extension GL_OES_EGL_image_external : require

precision mediump float;

uniform samplerExternalOES samplerTexture;
varying vec2 texCoord;


vec4 getEdge(float offset) {

  vec2 tc0 = texCoord.st + vec2(-offset, -offset);
  vec2 tc1 = texCoord.st + vec2(    0.0, -offset);
  vec2 tc2 = texCoord.st + vec2(+offset, -offset);
  vec2 tc3 = texCoord.st + vec2(-offset,     0.0);
  vec2 tc4 = texCoord.st + vec2(    0.0,     0.0);
  vec2 tc5 = texCoord.st + vec2(+offset,     0.0);
  vec2 tc6 = texCoord.st + vec2(-offset, +offset);
  vec2 tc7 = texCoord.st + vec2(    0.0,  offset);
  vec2 tc8 = texCoord.st + vec2(+offset, +offset);

  vec4 col0 = texture2D(samplerTexture, tc0);
  vec4 col1 = texture2D(samplerTexture, tc1);
  vec4 col2 = texture2D(samplerTexture, tc2);
  vec4 col3 = texture2D(samplerTexture, tc3);
  vec4 col4 = texture2D(samplerTexture, tc4);
  vec4 col5 = texture2D(samplerTexture, tc5);
  vec4 col6 = texture2D(samplerTexture, tc6);
  vec4 col7 = texture2D(samplerTexture, tc7);
  vec4 col8 = texture2D(samplerTexture, tc8);

  vec4 sum = (-1.0 * col0 + -1.0 * col1 + -1.0 * col2 +
              -1.0 * col3 +  8.0 * col4 + -1.0 * col5 +
              -1.0 * col6 + -1.0 * col7 + -1.0 * col8);

  float meanValue = (sum.r + sum.g + sum.b) / 3.0;
  float thresh = 0.5;
  vec4 on  = vec4(1.0, 1.0, 1.0, 1.0);
  vec4 off = vec4(0.0, 0.0, 0.0, 1.0);
  bool isOn = (sum.r > thresh || sum.g > thresh || sum.b > thresh);

  return isOn ? on : off;
}

void main() {
  gl_FragColor = getEdge(0.005);
}

