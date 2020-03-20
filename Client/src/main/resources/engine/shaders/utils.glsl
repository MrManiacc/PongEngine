/**
 * A uniform will be manuall mapped to the open shader in the background.
 * They will also be automatically mapped in to the specified shader, [vert, frag, or both]
 */
#define UNIFORMS-> projectionMatrix(vert): mat4, viewMatrix(vert): mat4, modelMatrix(vert): mat4, diffuse(frag): sampler2D
/**
 * Binds are manually mapped into the vertex shader,
 * using their index, and a second optional paramter,
 * Which specifies whether or not to pass the value to the frag shader.
 * If it's specified to pass to the frag shader, the fragment shader will have
 * An input value with the specified type and the name with a prefix of pass_
 */
#define BINDS-> vertex(0, false): vec3, textCoords(1, true): vec2

/**
 * We can also define and use indvidual variables
 */
#define VERSION-> "#version 400 core"