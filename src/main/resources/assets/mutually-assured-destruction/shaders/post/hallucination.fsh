#version 150

uniform sampler2D InSampler;
uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    float t = GameTime * 1200.0;
    float intensity = clamp(t / 600.0, 0.0, 1.0);

    vec4 color = texture(InSampler, texCoord);

    // fill genuinely black pixels (the bugged southeast area)
    float brightness = dot(color.rgb, vec3(1.0));
    float blackMask = 1.0 - smoothstep(0.0, 0.05, brightness);
    color.rgb = mix(color.rgb, vec3(0.05, 0.0, 0.1), blackMask);

    // pulsing purple additive tint
    float pulse = 0.5 + 0.5 * sin(t * 1.5);
    vec3 result = color.rgb + vec3(0.2, 0.0, 0.3) * intensity * pulse;

    fragColor = vec4(clamp(result, 0.0, 1.0), color.a);
}