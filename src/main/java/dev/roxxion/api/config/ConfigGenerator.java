package dev.roxxion.api.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Clase para generar archivos de configuración
 */
public class ConfigGenerator extends CommentConfig {

    private final String fileName;
    private final String resourcePath;
    private final JavaPlugin plugin;
    private final File filePath;
    private File file;

    /**
     * @param plugin Instancia del plugin
     * @param name   Nombre del archivo
     */
    public ConfigGenerator(JavaPlugin plugin, String name){
        this.plugin = plugin;

        this.fileName = name.endsWith(".yml") ? name : name + ".yml";
        this.resourcePath = fileName;

        this.filePath = plugin.getDataFolder();

        loadFile();
        createData();

        try {
            loadConfig();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param plugin   Instancia del plugin
     * @param name     Nombre del archivo
     * @param filePath Path del directorio
     */
    public ConfigGenerator(JavaPlugin plugin, String name, String resourcePath, String filePath){
        this.plugin = plugin;

        this.fileName = name.endsWith(".yml") ? name : name + ".yml";
        this.resourcePath = resourcePath;

        this.filePath = new File(filePath);

        loadFile( );
        createData( );

        try {
            loadConfig( );

        } catch ( Exception e ) {
            e.printStackTrace( );
        }
    }

    public void loadConfig() throws Exception{
        this.load( file );
    }

    public void loadFile(){
        this.file = new File(filePath , this.fileName);
    }

    public void saveData( ){
        this.file = new File(filePath , this.fileName);
        try {
            this.save(this.file);
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Attempting to fix the error...");
            createData();
            saveData();
        }
    }

    @Override
    public void save(File file) throws IOException{
        super.save(file);
    }

    public void createData(){
        if (!file.exists()){
            if (!this.filePath.exists()){
                this.filePath.mkdirs();
            }

            //If file isn't a resource, create from scratch
            try {
                this.file.createNewFile();
                writeToFile(this.plugin.getResource(this.resourcePath), this.file);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void delete(){
        if (this.file.exists()){
            this.file.delete();
        }
    }

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException{
        if (file == null)
            return;
        FileInputStream stream = new FileInputStream(file);
        this.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    //WRITE TO FILE FROM INPUT STREAM
    private void writeToFile(final InputStream input , final File target) throws IOException{
        final OutputStream output = Files.newOutputStream(target.toPath());
        final byte[] buffer = new byte[8 * 1024];
        int length = input.read(buffer);
        while (length > 0){
            output.write(buffer, 0 , length);
            length = input.read(buffer);
        }
        input.close();
        output.close();
    }
}

