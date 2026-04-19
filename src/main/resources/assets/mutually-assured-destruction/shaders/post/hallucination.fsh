#version 150

uniform sampler2D InSampler;
uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 uv = texCoord;

    float t = GameTime * 1200.0;
    float intensity = clamp(t / 600.0, 0.0, 1.0);

    float wobbleX = sin(uv.y * 8.0 + t * 3.0) * 0.008 * intensity;
    float wobbleY = cos(uv.x * 8.0 + t * 2.5) * 0.008 * intensity;
    uv.x += wobbleX;
    uv.y += wobbleY;

    uv = clamp(uv, 0.0, 1.0);
    vec4 color = texture(InSampler, uv);

    // fluctuating saturation
    float satPulse = 0.5 + 0.5 * sin(t * 1.5);
    float saturation = mix(1.0, 2.0, satPulse * intensity);
    float grey = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    vec3 saturated = mix(vec3(grey), color.rgb, saturation);

    // purple tint pulse
    float pulse = 0.5 + 0.5 * sin(t * 4.0);
    saturated.r += intensity * 0.15 * pulse;
    saturated.g *= (1.0 - intensity * 0.2);
    saturated.b += intensity * 0.25 * pulse;

    fragColor = vec4(clamp(saturated, 0.0, 1.0), color.a);
}