package idk.tools.androidrenderengine;

import static android.opengl.GLES31.*;

import android.util.Log;

import java.nio.FloatBuffer;
import java.util.HashMap;

public class ShaderProgram {

    private String vertCode;
    private String fragCode;
    private int programID;

    public ShaderProgram(){

    }
    public void setVertexShaderCode(String str){
        vertCode = str;

    }
    public void setFragmentShaderCode(String str){
        fragCode = str;
    }

    public boolean compile(){
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertID, vertCode);
        glCompileShader(vertID);

        int[] compileStatus1 = new int[1];
        glGetShaderiv(vertID,GL_COMPILE_STATUS, compileStatus1, 0);
        if (compileStatus1[0] != GL_TRUE) {
            error = glGetShaderInfoLog(vertID);
            Log.d("Error",error);
            return false;
        }

        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragID, fragCode);
        glCompileShader(fragID);

        compileStatus1 = new int[1];
        glGetShaderiv(fragID,GL_COMPILE_STATUS, compileStatus1, 0);
        if (compileStatus1[0] != GL_TRUE) {
            error = glGetShaderInfoLog(fragID);
            Log.d("Error",error);
            return false;
        }

        programID = glCreateProgram();
        glAttachShader(programID,vertID);
        glAttachShader(programID,fragID);
        glLinkProgram(programID);
       return true;
    }
    private String error;
    public String getError(){
        return error;
    }

    public int getID(){
return programID;
    }

    public void bind(){
        glUseProgram(programID);
    }
    public void unbind(){
        glUseProgram(0);
    }
    public void setAttribute(String string,VertexBufferObject obj,int size){
        obj.bind();
        int posID = getAttribute(string);
        glVertexAttribPointer(posID, size, GL_FLOAT, false, size*4, 0);
        glEnableVertexAttribArray(posID);

    }

    public void setAttribute(String string, FloatBuffer obj, int size){
        int posID = getAttribute(string);
        obj.position(0);
        glVertexAttribPointer(posID, size, GL_FLOAT, false, size*4, obj);
        glEnableVertexAttribArray(posID);
    }

    public int getAttribute(String string) {
        if(attribs.containsKey(string)){
            return attribs.get(string);
        }else {
            int pos =glGetAttribLocation(programID,string);
            attribs.put(string,pos);
            return pos;
        }

    }

    public int getUniform(String string) {
        if(unis.containsKey(string)){
            return unis.get(string);
        }else {
            int pos =glGetUniformLocation(programID,string);
            unis.put(string,pos);
            return pos;
        }
    }

    public void setMatrix4Uniform(String name,float[] val){
        glProgramUniformMatrix4fv(programID,getUniform(name), 1, false, val, 0);
    }

    private HashMap<String,Integer> attribs = new HashMap<>();
    private HashMap<String,Integer> unis = new HashMap<>();
}
