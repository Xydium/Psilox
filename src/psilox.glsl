#version 400 core

in vec3 v_position;
in vec2 v_tcoord;

out vec2 f_tcoord;

uniform mat4 u_projection;
uniform mat4 u_transform;

void main(void) {
    gl_Position = u_projection * u_transform * vec4(v_position, 1);
    f_tcoord = v_tcoord;
}

#version 400 core

in vec2 f_tcoord;

out vec4 f_fragcolor;

uniform vec4 u_modulate;

void main(void) {
    f_fragcolor = vec4(f_tcoord * u_modulate.xy, 1.0, 1.0);
}