#version 150

uniform sampler2D InSampler;
uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 color = texture(InSampler, texCoord);

    float t = GameTime * 3.0;

    float r = 0.5 + 0.5 * sin(t);
    float g = 0.5 + 0.5 * sin(t + 2.094); // 120 degrees offset
    float b = 0.5 + 0.5 * sin(t + 4.189); // 240 degrees offset

    // blue-green-pink cycle
    color.r *= 0.7 + r * 0.6;
    color.g *= 0.7 + g * 0.6;
    color.b *= 0.7 + b * 0.6;

    fragColor = vec4(clamp(color.rgb, 0.0, 1.0), color.a);
}