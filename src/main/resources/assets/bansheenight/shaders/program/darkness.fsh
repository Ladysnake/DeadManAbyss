#version 120

uniform sampler2D DiffuseSampler;

uniform float Time;

varying vec2 texCoord;

void main() {
    vec4 tex = texture2D(DiffuseSampler, texCoord);
    float greyscale = tex.r + tex.g + tex.b / 3.0;
    // the darker it is, ***the darker it becomes***
    tex *= 0.9 + (log2(greyscale) / log2(10.0)); // log10(greyscale)
    // desaturate brighter areas
    gl_FragColor = vec4(mix(vec3(dot(tex.rgb, vec3(0.2125, 0.7154, 0.0721))), tex.rgb, 1. - greyscale), 1);
}
