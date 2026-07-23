#version 150

uniform sampler2D In;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 uv = texCoord;
    vec2 centered = uv - 0.5;

    // sample normal view
    vec4 main_view = texture(In, uv);

    // sample a zoomed centered copy - this is the "second eye"
    vec2 inner_uv = centered * 2.0 + 0.5; // zoom in 2x from center
    vec4 inner_view = texture(In, inner_uv);

    // circular mask for the inner eye - radius 0.25 of screen
    float dist = length(centered);
    float mask = smoothstep(0.26, 0.24, dist); // 1.0 inside circle, 0.0 outside

    // vignette on the inner circle edge
    vec4 color = mix(main_view, inner_view, mask);

    // purple tint
    color.r *= 0.8;
    color.g *= 0.4;
    color.b *= 1.0;

    fragColor = color;
}