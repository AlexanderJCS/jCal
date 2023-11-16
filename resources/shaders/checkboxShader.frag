// A shader that finds-and-replaces one color with the desired color.
// Used in the calendar program to make checkmark in the checkbox the calendar color.
// The vertex shader should be the default vertex texture shader.

#version 410

uniform sampler2D texSampler;

uniform vec3 findColor;
uniform vec3 replaceColor;

in vec2 texCoords;
out vec4 fragColor;


bool dist(float f1, float f2) {
    return abs(f1 - f2) < 0.000001;
}


void main() {
    fragColor = texture(texSampler, texCoords);

    // How close is "close enough" for the find/replace color
    float closeEnough = 0.4f;

    // Find-and-replace the color
    if (abs(fragColor.x - findColor.x) < closeEnough &&
        abs(fragColor.y - findColor.y) < closeEnough &&
        abs(fragColor.z - findColor.z)  < closeEnough)
    {
        fragColor = vec4(replaceColor, fragColor.w);
    }
}
