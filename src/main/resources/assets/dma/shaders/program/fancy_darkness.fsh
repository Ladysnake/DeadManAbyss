#version 120

struct LightStruct
{
  // The position of the light's center relative to the camera, in world coordinates
  vec3 position;
  // The radius of the light. 1 is slightly more than half a block
  float radius;
};

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

// 0 when the event has just started, 1 when it has fully kicked in
uniform float Progress;

// The magic matrix to get world coordinates from pixel ones
uniform mat4 InverseTransformMatrix;
// The size of the viewport (typically, [0,0,1080,720])
uniform ivec4 ViewPort;
uniform LightStruct Lights[100];
uniform int LightCount;

varying vec2 texCoord;
varying vec4 vPosition;

vec4 CalcEyeFromWindow(in float depth)
{
  // derived from https://www.khronos.org/opengl/wiki/Compute_eye_space_from_window_space
  // ndc = Normalized Device Coordinates
  vec3 ndcPos;
  ndcPos.xy = ((2.0 * gl_FragCoord.xy) - (2.0 * ViewPort.xy)) / (ViewPort.zw) - 1;
  ndcPos.z = (2.0 * depth - gl_DepthRange.near - gl_DepthRange.far) / (gl_DepthRange.far - gl_DepthRange.near);
  vec4 clipPos = vec4(ndcPos, 1.);
  vec4 homogeneous = InverseTransformMatrix * clipPos;
  vec4 eyePos = vec4(homogeneous.xyz / homogeneous.w, homogeneous.w);
  return eyePos;
}

void main()
{
    vec4 tex = texture2D(DiffuseSampler, texCoord);

    vec3 ndc = vPosition.xyz / vPosition.w; //perspective divide/normalize
    vec2 viewportCoord = ndc.xy * 0.5 + 0.5; //ndc is -1 to 1 in GL. scale for 0 to 1

    // Depth fading
    float sceneDepth = texture2D(DepthSampler, viewportCoord).x;
    vec3 pixelPosition = CalcEyeFromWindow(sceneDepth).xyz;

    // 0 = dead center of a light, 1 = too far
    float distanceFromLight = 1.;
    for(int i = 0; i < LightCount; i++)
    {
      // Interpolate the distance between the light and the current pixel from [0, radius] to [0, 1]
      float relativeDepth = smoothstep(0, Lights[i].radius, distance(Lights[i].position, pixelPosition));
      distanceFromLight = min(distanceFromLight, relativeDepth);
    }

    float greyscale = tex.r + tex.g + tex.b / 3.0;
    // the darker it is, ***the darker it becomes***
    vec4 darkened = tex * 0.9 + (log2(greyscale) / log2(10.0)); // log10(greyscale)
    // desaturate brighter areas
    vec4 desaturated = vec4(mix(vec3(dot(darkened.rgb, vec3(0.2125, 0.7154, 0.0721))), darkened.rgb, max(0.3, 1. - greyscale)), 1);
    gl_FragColor = mix(tex, desaturated, distanceFromLight);
}
