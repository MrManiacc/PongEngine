#define engine:utils-> BINDS, UNIFORMS

#ifdef VERTEX_SHADER
    #define engine:utils-> VERSION
    void main(){
        //Automatically passed
        vec4 mvp = projectionMatrix * viewMatrix * modelMatrix;
        gl_Position = mvp * vertex;
        //Here the textureCoords will be passed to the frag shader
        //With the following pseduo code: pass_textCoord = textCoord;
    }
#endif

#ifdef FRAGMENT_SHADER
    #define engine:utils-> VERSION
    void main(){
        //Out color is always automatically generated, unless specified otherwise from java pre-processor
        out_Color = texture(diffuse, pass_textCoord);
    }
#endif