package idk.tools.androidrenderengine;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_DYNAMIC_DRAW;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_INT;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glGenBuffers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class IndexBufferObject {

    private IndexBufferObject(){

    }

    private int type;
    public static IndexBufferObject create(){
        int[] pos = new int[1];
        glGenBuffers(1,pos,0);
        IndexBufferObject vbo = new IndexBufferObject();
        vbo.id = pos[0];
        vbo.type = GL_STATIC_DRAW;
        return vbo;
    }
    public static IndexBufferObject createDynamic(){
        int[] pos = new int[1];
        glGenBuffers(1,pos,0);
        IndexBufferObject vbo = new IndexBufferObject();
        vbo.id = pos[0];
        vbo.type = GL_DYNAMIC_DRAW;
        return vbo;
    }
    public void setData(IntBuffer buffer){
        bind();
        buffer.position(0);
        dataType = GL_UNSIGNED_INT;
        count = buffer.remaining();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,buffer.remaining()*4,buffer,type);
        unbind();
    }

    public void setData(int[] buffer){
        ByteBuffer bf = ByteBuffer.allocateDirect(buffer.length*4);
        bf.order(ByteOrder.nativeOrder());
        bf.position(0);
        setData(bf.asIntBuffer().put(buffer));
    }

    public void setData(short[] buffer){
        ByteBuffer bf = ByteBuffer.allocateDirect(buffer.length*2);
        bf.order(ByteOrder.nativeOrder());
        bf.position(0);
        setData(bf.asShortBuffer().put(buffer));
    }

    public void setData(ShortBuffer buffer){
        bind();
        buffer.position(0);
        dataType = GL_UNSIGNED_SHORT;
        count = buffer.remaining();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,buffer.remaining()*2,buffer,type);
        unbind();
    }

    private int dataType;
    private int count;

    public void drawTriangle(){
        bind();
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
        unbind();
    }

    public void bind(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,id);
    }

    public void unbind(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
    }

    private int id;
}
