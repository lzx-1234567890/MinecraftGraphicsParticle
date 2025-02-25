package com.lzx.minecraftparticle.logic;
import com.lzx.minecraftparticle.logic.dao.SaveDao;
import com.lzx.minecraftparticle.logic.model.Save;

public class Repository {
    private static volatile Repository instance;
    
    private Repository() {}
    
    public static Repository getInstance() {
        if(instance == null) {
            synchronized(Repository.class) {
                if(instance == null) {
                    instance = new Repository();
                }
            }
        }
        return instance;
    }
    
    public void save(Save save, Integer[] size) {
        SaveDao.getInstance().save(save, size);
    }
    
    public Save getSave(int id) {
        return SaveDao.getInstance().getSave(id);
    }
    
    public void removeSave(int id) {
        SaveDao.getInstance().removeSave(id);
    }
    
    public void sortSave(int from, int to) {
        SaveDao.getInstance().sortSave(from, to);
    }
    
    public void modifySave(Save save) {
        SaveDao.getInstance().modifySave(save);
    }
}
