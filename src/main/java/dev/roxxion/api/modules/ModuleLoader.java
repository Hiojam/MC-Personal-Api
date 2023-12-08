package dev.roxxion.api.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ModuleLoader {

    private final JavaPlugin plugin;

    private final List<Module> modules;
    private final List<URLClassLoader> classLoaders = new ArrayList<>();

    public ModuleLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        this.modules = new ArrayList<>();
    }

    public void loadModules(){
        List<File> files = getJarFilesInFolder();
        if(files == null) return;

        for(File file : files) {

            File resourceFile = null;
            try {
                JarFile jarFile = new JarFile(file);
                ZipEntry entry = jarFile.getEntry("module.json");
                resourceFile = File.createTempFile(entry.getName(), "");

                InputStream in =
                        new BufferedInputStream(jarFile.getInputStream(entry));
                OutputStream out =
                        new BufferedOutputStream(new FileOutputStream(resourceFile));
                byte[] buffer = new byte[2048];
                for (; ; ) {
                    int nBytes = in.read(buffer);
                    if (nBytes <= 0) break;
                    out.write(buffer, 0, nBytes);
                }
                out.flush();
                out.close();
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (resourceFile == null) continue;

            ModuleInfo moduleInfoClass = null;

            Gson gson = new GsonBuilder().serializeNulls().create();

            try {
                FileReader reader = new FileReader(resourceFile);
                moduleInfoClass = gson.fromJson(reader, ModuleInfo.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (moduleInfoClass == null) continue;
            String mainClass = moduleInfoClass.getMain();

            URL myJarFileURL = null;
            try {
                myJarFileURL = new URL("jar", "", "file:" + file.getAbsolutePath() + "!/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URL[] classes = {myJarFileURL};

            URLClassLoader child = new URLClassLoader(classes, this.getClass().getClassLoader());
            this.classLoaders.add(child);

            Object instance = null;
            try {
                Class<?> classToLoad = Class.forName(mainClass, true, child);
                instance = classToLoad.getConstructor(new Class[]{ plugin.getClass() }).newInstance(plugin);

                Method method = classToLoad.getDeclaredMethod("onLoad");
                method.invoke(instance);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(instance == null) continue;

            moduleInfoClass.setAbsolutePath(file.getAbsolutePath());

            modules.add((Module) instance);
        }
    }

    public void unloadModules(){
        for(Module moduleInfoClass : modules){
            moduleInfoClass.onUnload();
        }
        modules.clear();

        for (URLClassLoader classLoader : classLoaders) {
            try {
                classLoader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        classLoaders.clear();
        System.gc();
    }

    public boolean isLoaded(String moduleName){
        for (Module module : modules){
            if (Objects.equals(module.getModuleName(), moduleName)) return true;
        }
        return false;
    }

    private List<File> getJarFilesInFolder(){
        File addonsFile = new File(plugin.getDataFolder(), "modules");

        if(!addonsFile.exists()){
            addonsFile.mkdir();
        }

        File[] listOfFiles = addonsFile.listFiles();
        if(listOfFiles == null) return null;

        List<File> jarFiles = new ArrayList<>();

        for(File file : listOfFiles){
            if(!file.getName().endsWith(".jar")) continue;
            jarFiles.add(file);
        }

        if(jarFiles.isEmpty()) return null;
        return jarFiles;
    }
}
