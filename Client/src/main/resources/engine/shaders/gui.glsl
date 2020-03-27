#ifdef VERTEX_SHADER
    #define engine:utils-> VERSION
    #define engine:utils-> BINDS
    #define engine:utils-> UNIFORMS
    out vec2 pass_textureCoords;
    void main(){
        gl_Position = modelMatrix * vec4(vertex, 1.0);
        pass_textureCoords = textureCoords;
    }
#endif

#ifdef FRAGMENT_SHADER
    #define engine:utils-> VERSION
    in vec2 pass_textureCoords;
    out vec4 out_Color;

    void main(){
//        out_Color = vec4(pass_textureCoords.x - 0.2, pass_textureCoords.y, 0.0, 1.0);
        out_Color = texture(diffuse, pass_textureCoords);
    }
#endif