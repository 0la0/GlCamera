#extension GL_OES_EGL_image_external : require

precision mediump float;

uniform samplerExternalOES samplerTexture;
varying vec2 texCoord;
varying float param1;
varying float param2;
varying float param3;

float weightList[9] = float[9](
    1.0, 2.0, 1.0,
    2.0, 4.0, 2.0,
    1.0, 2.0, 1.0
);

vec3 saturate(vec3 rgb, float adjustment) {
    vec3 colorWeights = vec3(0.2125, 0.7154, 0.0721);
    vec3 intensity = vec3(dot(rgb, colorWeights));
    return mix(intensity, rgb, adjustment);
}

vec4 getBlur(float offsetX, float offsetY) {

  vec2 tc0 = texCoord.st + vec2(-offsetX, -offsetY);
  vec2 tc1 = texCoord.st + vec2(    0.0,  -offsetY);
  vec2 tc2 = texCoord.st + vec2(+offsetX, -offsetY);
  vec2 tc3 = texCoord.st + vec2(-offsetX,      0.0);
  vec2 tc4 = texCoord.st + vec2(    0.0,       0.0);
  vec2 tc5 = texCoord.st + vec2(+offsetX,      0.0);
  vec2 tc6 = texCoord.st + vec2(-offsetX, +offsetY);
  vec2 tc7 = texCoord.st + vec2(    0.0,   offsetY);
  vec2 tc8 = texCoord.st + vec2(+offsetX, +offsetY);

  vec3 pixel0 = texture2D(samplerTexture, tc0).rgb;
  vec3 pixel1 = texture2D(samplerTexture, tc1).rgb;
  vec3 pixel2 = texture2D(samplerTexture, tc2).rgb;
  vec3 pixel3 = texture2D(samplerTexture, tc3).rgb;
  vec3 pixel4 = texture2D(samplerTexture, tc4).rgb;
  vec3 pixel5 = texture2D(samplerTexture, tc5).rgb;
  vec3 pixel6 = texture2D(samplerTexture, tc6).rgb;
  vec3 pixel7 = texture2D(samplerTexture, tc7).rgb;
  vec3 pixel8 = texture2D(samplerTexture, tc8).rgb;

  vec3 col0 = saturate(pixel0, 2.0 - (param1 * 2.0));
  vec3 col1 = saturate(pixel1, param1 * 2.0);
  vec3 col2 = saturate(pixel2, param2 * 2.0);
  vec3 col3 = saturate(pixel3, param3 * 2.0);
  vec3 col4 = saturate(pixel4, param2 * 2.0);
  vec3 col5 = saturate(pixel5, param2 * 2.0);
  vec3 col6 = saturate(pixel6, param3 * 2.0);
  vec3 col7 = saturate(pixel7, param3 * 2.0);
  vec3 col8 = saturate(pixel8, 2.0 - (param1 * 2.0));

  float mult = param1;
  float denominator = 4.0 + (12.0 * mult);

  vec3 sum = (mult * 1.0 * col0 + mult * 2.0 * col1 + mult * 1.0 * col2 +
              mult * 2.0 * col3 +        4.0 * col4 + mult * 2.0 * col5 +
              mult * 1.0 * col6 + mult * 2.0 * col7 + mult * 1.0 * col8) / denominator;

  //vec3 saturated = saturate(sum.rgb, 3.0);
  return vec4(sum, 1.0);
}


void main() {
  gl_FragColor = getBlur(param2 / 40.0, param3 / 30.0);
  //gl_FragColor = getBlur(0.05, 0.05);
}

