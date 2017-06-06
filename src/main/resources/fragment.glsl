#version 330

in vec2 outTexCoords;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main() {
    fragColor = texture(texture_sampler, outTexCoords);
//    fragColor = vec4(1.0,1.0,1.0,1.0);
}
