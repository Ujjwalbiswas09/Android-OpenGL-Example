package idk.tools.androidrenderengine;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static android.opengl.GLES31.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;

public class MainActivity extends Activity {


    float positionX;
    float positionZ;

    float projectMat[] = new float[16];
    float viewMat[] = new float[16];
    float modelMat[] = new float[16];
    float aspect=0.5f;

    boolean w_pressd;
    boolean s_pressd;
    boolean a_pressd;
    boolean d_pressd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CustomView(this));

    }
    class CustomView extends GLSurfaceView implements GLSurfaceView.Renderer{

        public CustomView(Context context) {
            super(context);
            setEGLContextClientVersion(3);
            setRenderer(this);
            setPreserveEGLContextOnPause(true);

        }

        private int programID;




        ShaderProgram shaderProgram = new ShaderProgram();
        VertexBufferObject bufferObject;
        IndexBufferObject indexBufferObject;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            try {

                Obj base = ObjReader.read(getAssets().open("Rock1.obj"));
                Obj obj = ObjUtils.convertToRenderable(base);
                FloatBuffer positionBuffer = ObjData.getVertices(obj);
                IntBuffer indiceBuffer = ObjData.getFaceVertexIndices(obj);

                indexBufferObject = IndexBufferObject.create();
                indexBufferObject.setData(indiceBuffer);

                bufferObject = VertexBufferObject.create();
                bufferObject.setData(positionBuffer);


                String vertCode = readString(getAssets().open("VertexShader.txt"));
                String fragCode = readString(getAssets().open("FragmentShader.txt"));
                shaderProgram.setFragmentShaderCode(fragCode);
                shaderProgram.setVertexShaderCode(vertCode);
                shaderProgram.compile();


                Matrix.setIdentityM(projectMat,0);
                Matrix.perspectiveM(projectMat,0,60f,aspect,0.5f,1000f);

                Matrix.setIdentityM(viewMat,0);

                Matrix.setIdentityM(modelMat,0);
                Matrix.translateM(modelMat,0,positionX,0,positionZ);

            }catch (Exception e){
                Log.e(e.getMessage(),e.toString(),e);
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0,0,width,height);
            aspect = ((float) width )/(float) height;
        }



        @Override
        public void onDrawFrame(GL10 gl) {
            try {

                glClear(GL_COLOR_BUFFER_BIT);
                glClear(GL_DEPTH_BUFFER_BIT);
                glClearColor(0.1f, 0.1f, 0.1f, 1f);
                glEnable(GL_DEPTH_FUNC);
                glEnable(GL_DEPTH_TEST);
                glEnable(GL_CULL_FACE);


                shaderProgram.bind();
                shaderProgram.setMatrix4Uniform("projectMat",projectMat);
                shaderProgram.setMatrix4Uniform("viewMat",viewMat);
                shaderProgram.setMatrix4Uniform("modelMat",modelMat);

                shaderProgram.setAttribute("position",bufferObject,3);

                indexBufferObject.drawTriangle();
                shaderProgram.unbind();

                if(w_pressd){
                    positionZ += 0.1f;
                }
                if(s_pressd){
                    positionZ -= 0.1f;
                }
                if(a_pressd){
                    positionX -= 0.1f;
                }
                if(d_pressd){
                    positionX += 0.1f;
                }

                Matrix.setIdentityM(viewMat,0);
                Matrix.translateM(viewMat,0,positionX,0,positionZ);

            }catch (Exception e){
                Log.e(e.getMessage(),e.toString(),e);
            }
        }

    }

    public static String readString(InputStream inputStream){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int i;
            byte[] data = new byte[512];
            while ((i=bis.read(data))!= -1){
                bos.write(data,0,i);
            }
            String str = new String(bos.toByteArray());
            bos.close();
            bis.close();
            return str;
        }catch (Exception e){

        }
        return "";
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_W){
            w_pressd = true;
        }
        if (keyCode==KeyEvent.KEYCODE_S){
            s_pressd = true;
        }
        if(keyCode==KeyEvent.KEYCODE_A){
            a_pressd = true;
        }
        if (keyCode==KeyEvent.KEYCODE_D){
            d_pressd = true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_W){
            w_pressd = false;
        }
        if (keyCode==KeyEvent.KEYCODE_S){
            s_pressd = false;
        }
        if(keyCode==KeyEvent.KEYCODE_A){
            a_pressd = false;
        }
        if (keyCode==KeyEvent.KEYCODE_D){
            d_pressd = false;
        }
        return super.onKeyUp(keyCode, event);
    }
}