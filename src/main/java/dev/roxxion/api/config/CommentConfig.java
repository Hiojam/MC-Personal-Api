package dev.roxxion.api.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class CommentConfig extends YamlConfiguration {

    private final Map<String, List<String>> comments;

    public CommentConfig( ){
        comments = new HashMap<>();
    }

    @Override
    public String saveToString(){

        String contents = super.saveToString();

        List < String > list = new ArrayList<>();
        Collections.addAll(list, contents.split("\n"));

        int currentLayer = 0;
        StringBuilder currentPath = new StringBuilder();

        StringBuilder sb = new StringBuilder();

        int lineNumber = 0;
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); lineNumber++){
            String line = iterator.next();
            sb.append(line);
            sb.append("\n");

            if (!line.isEmpty()){
                if (line.contains( ":" )){

                    int layerFromLine = getLayerFromLine(line);

                    if (layerFromLine < currentLayer){
                        new StringBuilder(regressPathBy(currentLayer - layerFromLine, currentPath.toString()));
                    }

                    String key = getKeyFromLine(line);

                    assert key != null;
                    currentPath = new StringBuilder(key);

                    String path = currentPath.toString();
                    if ( comments.containsKey(path)){
                        comments.get( path ).forEach(string -> {
                            sb.append(string);
                            sb.append("\n");
                        });
                    }
                }
            }
        }

        return sb.toString();
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException{
        super.loadFromString(contents);

        List <String> list = new ArrayList<>();
        Collections.addAll(list, contents.split("\n"));

        String currentPath = "";

        int lineNumber = 0;
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); lineNumber++){
            String line = iterator.next();

            String trimmed = line.trim();
            if (trimmed.startsWith("##") || trimmed.isEmpty()){
                addCommentLine(currentPath, line);
                continue;
            }

            if (!line.isEmpty()){
                if (line.contains(":")){

                    int layerFromLine = getLayerFromLine(line);

                    currentPath = getKeyFromLine(line);
                }
            }
        }
    }

    private void addCommentLine(String currentPath, String line){

        List <String> list = comments.get(currentPath);
        if (list == null){
            list = new ArrayList <>();
        }
        list.add(line);

        comments.put(currentPath, list);
    }

    private String getKeyFromLine(String line){

        String key = null;

        for (int i = 0; i < line.length(); i++){
            if (line.charAt( i ) == ':'){
                key = line.substring(0, i);
                break;
            }
        }

        return key == null ? null : key.trim();
    }

    private String regressPathBy(int i, String currentPath){

        if ( i <= 0 ) {
            return currentPath;
        }
        String[] split = currentPath.split("\\.");

        StringBuilder rebuild = new StringBuilder();
        for (int j = 0; j < split.length - i; j++){
            rebuild.append(split[j]);
            if (j <= (split.length - j)){
                rebuild.append(".");
            }
        }

        return rebuild.toString();
    }

    private int getLayerFromLine(String line){

        double d = 0;
        for ( int i = 0; i < line.length( ); i++ ) {
            if ( line.charAt( i ) == ' ' ) {
                d += 0.5;
            } else {
                break;
            }
        }

        return ( int ) d;
    }
}
