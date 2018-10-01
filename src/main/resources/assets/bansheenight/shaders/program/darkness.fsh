#version 120

uniform sampler2D DiffuseSampler;

uniform float Time;

varying vec2 texCoord;

void main() {
    vec4 tex = texture2D(DiffuseSampler, texCoord);
    tex = 1 + log2(tex) / log2(10); // log10(tex)
    gl_FragColor = tex;
}
