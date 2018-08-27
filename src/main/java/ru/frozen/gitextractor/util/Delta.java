package ru.frozen.gitextractor.util;

import ru.frozen.gitextractor.model.Snapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Delta {



    private Map<String, String> deltaFilesList;

    private Util util = new Util();

    public Delta(){
        this.deltaFilesList = new HashMap<>();
    }

    public Map<String, String> getDelta(List<String> filesListOld, List<String> filesListNew, String pathToRepos, Snapshot snapshot){
        for (String fileName : filesListOld){
            if (!filesListNew.contains(fileName) && fileName != "meta.properties"){
                deltaFilesList.put(fileName, "Deleted");

            }
        }
        for (String fileName : filesListNew){
            if (!filesListOld.contains(fileName)){
                //TODO adding check
                deltaFilesList.put(fileName, "Added");
            }
        }
        for (int i = 0; i < filesListNew.size(); i++){
            for (int j = 0; j < filesListOld.size(); j++){
                if (filesListNew.get(i).equals(filesListOld.get(j))){
                    String prevSnapshot = snapshot.getPrevSnapshotName() != null ? snapshot.getPrevSnapshotName() : "backup";
                    String pathOld = pathToRepos + snapshot.getRepoName() + "/" + prevSnapshot + "/" + filesListOld.get(j) ;
                    String pathNew = pathToRepos + snapshot.getRepoName() + "/" + snapshot.getNextSnapshotName() + "/" + filesListNew.get(i);
                    try {
                        if (!util.compareFiles(pathOld, pathNew)){
                            deltaFilesList.put(filesListNew.get(i), "Edited");
                        }
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

        }
        return (deltaFilesList);
    }

}
