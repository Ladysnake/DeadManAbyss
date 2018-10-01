#version 120

uniform sampler2D DiffuseSampler;

uniform float Time;

varying vec2 texCoord;

void main() {
    vec4 tex = texture2D(DiffuseSampler, texCoord);
    float greyscale = tex.r + tex.g + tex.b / 3.;
    tex *= 0.9 + (log2(greyscale) / log2(10)); // log10(greyscale)
    gl_FragColor = tex;
}
