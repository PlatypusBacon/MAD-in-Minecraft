#version 150

uniform sampler2D InSampler;
uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

void main() {

    vec4 color = texture(InSampler, texCoord);

    float brightness = dot(color.rgb, vec3(1.0));
    float blackMask = 1.0 - smoothstep(0.0, 0.05, brightness);
    color.rgb = mix(color.rgb, vec3(0.05, 0.0, 0.1), blackMask);

    vec3 result = color.rgb + vec3(0.2, 0.0, 0.3);

    fragColor = vec4(clamp(result, result, result), color.a);
}