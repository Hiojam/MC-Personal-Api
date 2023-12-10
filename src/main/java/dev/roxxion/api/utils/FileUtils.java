package dev.roxxion.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FileUtils {

    private final String dataFolder;
    private final Gson gson = new GsonBuilder().serializeNulls().create();

    public FileUtils(String dataFolder) {
        this.dataFolder = dataFolder;
    }

    public @NotNull <T> List<T> getAllFiles(String prevPath, Class<T> clazz){
        String path = dataFolder+prevPath;
        File filePath = new File(path);

        if(!filePath.exists()) new ArrayList<>();

        File[] fileList = filePath.listFiles();
        if(fileList == null || fileList.length == 0) return new ArrayList<>();

        List<T> objects = new ArrayList<>();

        for(File file : fileList) {

            try {
                FileReader reader = new FileReader(file);
                T region = gson.fromJson(reader, clazz);
                objects.add(region);
            } catch (FileNotFoundException ignored) {
            }
        }

        return objects;
    }

    public <T> void createFile(String prevPath, String fileName, Object object){
        String path = dataFolder+prevPath;
        File file = new File(path, fileName);

        File filePath = new File(path);

        if (!file.exists()){

            if (!filePath.exists()){
                filePath.mkdirs();
            }

            try {
                file.createNewFile();
            } catch (Exception e){
                return;
            }
        }

        setDefaultOptionsFromClass(object, file);
    }

    public <T> Object getObject(File file, Class<T> clazz){
        try {
            FileReader reader = new FileReader(file);
            return gson.fromJson(reader, clazz);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private void setDefaultOptionsFromClass(Object object, File file){
        try {
            FileWriter f = new FileWriter(file);
            String json = gson.toJson(object);

            f.write(json);
            f.flush();
            f.close();
        } catch (Exception ignored){
        }
    }
}
