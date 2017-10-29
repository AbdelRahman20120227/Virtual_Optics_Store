package com.example.ahmed.opengl;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmed on 3/9/2017.
 */
public class ModelLoader {

    public static Map<String, Triangle> load(BufferedReader reader) throws IOException {
        ArrayList<ArrayList<Float>> objects = new ArrayList<>();
        ArrayList<ArrayList<Integer>> objectsIndices = new ArrayList<>();
        ArrayList<String> names = new ArrayList<String>();
        String line = "";
        Map<Integer, Integer> vertexToNormal = new HashMap<Integer, Integer>();
        ArrayList<Float> vertices = new ArrayList<Float>();
        ArrayList<Float> normals = new ArrayList<Float>();

        int objectIndex = 0;
        objects.add(new ArrayList<Float>());
        objectsIndices.add(new ArrayList<Integer>());
        boolean face = false;
        int vertexStart = 0;
        while((line = reader.readLine()) != null){

            String[] tokens = line.split(" ");
            if(tokens[0].equals("o")){
                names.add(tokens[1]);
            }
            if(tokens[0].equals("v")){
                if(face){

                    for(int i = vertexStart; i < vertices.size() / 3; i++){
                        int index = i * 3;
                        objects.get(objectIndex).add(vertices.get(index));
                        objects.get(objectIndex).add(vertices.get(index + 1));
                        objects.get(objectIndex).add(vertices.get(index + 2));
                        if(vertexToNormal.get(i) != null){
                            objects.get(objectIndex).add(normals.get(vertexToNormal.get(i) * 3));
                            objects.get(objectIndex).add(normals.get((vertexToNormal.get(i) * 3) + 1));
                            objects.get(objectIndex).add(normals.get((vertexToNormal.get(i) * 3) + 2));
                        }
                        else{
                            objects.get(objectIndex).add(0.0f);
                            objects.get(objectIndex).add(0.0f);
                            objects.get(objectIndex).add(0.0f);
                        }
                    }
                    vertexStart = vertices.size() / 3;
                    face = false;
                    objectIndex++;
                    objects.add(new ArrayList<Float>());
                    objectsIndices.add(new ArrayList<Integer>());
                }
                float x = Float.valueOf(tokens[1]);
                float y = Float.valueOf(tokens[2]);
                float z = Float.valueOf(tokens[3]);
                vertices.add(x);
                vertices.add(y);
                vertices.add(z);
            }
            else if(tokens[0].equals("f")){
                face = true;
                String splitValue = tokens[1].contains("//") ? "//" : "/";
                for(int i = 1; i <= 3; i++){
                    String[] innerTokens = tokens[i].split(splitValue);
                    int vertexIndex = Integer.valueOf(innerTokens[0]) - 1;
                    objectsIndices.get(objectIndex).add(vertexIndex - vertexStart);
                    int normalIndex = Integer.valueOf(innerTokens[innerTokens.length - 1]) - 1;
                    vertexToNormal.put(vertexIndex, normalIndex);
                }
            }
            else if(tokens[0].equals("vn")){
                float x = Float.valueOf(tokens[1]);
                float y = Float.valueOf(tokens[2]);
                float z = Float.valueOf(tokens[3]);
                normals.add(x);
                normals.add(y);
                normals.add(z);
            }
        }

        for(int i = vertexStart; i < vertices.size() / 3; i++){
            int index = i * 3;
            objects.get(objectIndex).add(vertices.get(index));
            objects.get(objectIndex).add(vertices.get(index + 1));
            objects.get(objectIndex).add(vertices.get(index + 2));
            if(vertexToNormal.get(i) != null){
                objects.get(objectIndex).add(normals.get(vertexToNormal.get(i) * 3));
                objects.get(objectIndex).add(normals.get((vertexToNormal.get(i) * 3) + 1));
                objects.get(objectIndex).add(normals.get((vertexToNormal.get(i) * 3) + 2));

            }
            else{
                objects.get(objectIndex).add(0.0f);
                objects.get(objectIndex).add(0.0f);
                objects.get(objectIndex).add(0.0f);
            }
        }

        Map<String, Triangle> result = new HashMap<>();
        for(int i = 0; i < objects.size(); i++){
            result.put(names.get(i), new Triangle(objects.get(i), objectsIndices.get(i)));
        }
        return result;
    }
}
