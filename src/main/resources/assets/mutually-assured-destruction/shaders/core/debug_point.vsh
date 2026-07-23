#version 330

#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec4 Color;
in float LineWidth;

out vec4 vertexColor;

void main() {
	float r = (Position.x * Position.x) + (Position.z * Position.z);
	float shiftX = sin(r / 48.0) * 2.0;
	float shiftY = r * r / -524288.0;
	float shiftZ = cos(r / 48.0) * -2.0;

    gl_Position = ProjMat * ModelViewMat * (vec4(Position, 1.0) + vec4(shiftX, shiftY, shiftZ + 2, 0.0));

    vertexColor = Color;
    gl_PointSize = LineWidth;
}
