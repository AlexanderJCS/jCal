// A shader that finds-and-replaces one color with the desired color.
// Used in the calendar program to make checkmark in the checkbox the calendar color.
// The vertex shader should be the default vertex texture shader.

#version 410

uniform sampler2D texSampler;

uniform vec3 findColor;
uniform vec3 replaceColor;

in vec2 texCoords;
out vec4 fragColor;


bool equalf(float f1, float f2) {
    return abs(f1 - f2) < 0.000001;
}


void main() {
    fragColor = texture(texSampler, texCoords);

    // Find-and-replace the color
    if (equalf(fragColor.x, findColor.x) && equalf(fragColor.y, findColor.y) && equalf(fragColor.z, findColor.z)) {
        fragColor = vec4(replaceColor, fragColor.w);
    }
}
