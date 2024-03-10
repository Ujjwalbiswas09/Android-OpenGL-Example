package idk.tools.androidrenderengine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import static android.opengl.GLES31.*;

public class VertexBufferObject {

    private VertexBufferObject(){

    }

    private int type;
    public static VertexBufferObject create(){
        int[] pos = new int[1];
        glGenBuffers(1,pos,0);
        VertexBufferObject vbo = new VertexBufferObject();
        vbo.id = pos[0];
        vbo.type = GL_STATIC_DRAW;
        return vbo;
    }
    public static VertexBufferObject createDynamic(){
        int[] pos = new int[1];
        glGenBuffers(1,pos,0);
        VertexBufferObject vbo = new VertexBufferObject();
        vbo.id = pos[0];
        vbo.type = GL_DYNAMIC_DRAW;
        return vbo;
    }
    public void setData(FloatBuffer buffer){
        bind();
        buffer.position(0);
        glBufferData(GL_ARRAY_BUFFER,buffer.remaining()*4,buffer,type);
        unbind();
    }

    public void setData(float[] buffer){
        ByteBuffer bf = ByteBuffer.allocateDirect(buffer.length*4);
        bf.order(ByteOrder.nativeOrder());
        bf.position(0);
        setData(bf.asFloatBuffer().put(buffer));
    }

    public void bind(){
        glBindBuffer(GL_ARRAY_BUFFER,id);
    }

    public void unbind(){
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }

    private int id;
}
