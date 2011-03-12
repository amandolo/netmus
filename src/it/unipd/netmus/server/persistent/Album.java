package it.unipd.netmus.server.persistent;

import it.unipd.netmus.server.utils.Utils;

import com.google.code.twig.annotation.Id;

/**
 * Nome: MusicLibrary.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 10 Marzo 2011
 * 
 */

public class Album {

    private static final String SEPARATOR = "-vtg-";
    
    static String generateAlbumId(String name, String artist) {
        String song_id = (Utils.cleanString(name) + Album.SEPARATOR
        		+ Utils.cleanString(artist));
        /*if (song_id != Album.SEPARATOR) {
            song_id = song_id.replace('.', ' ');
            song_id = song_id.replace('\"', ' ');
            song_id = song_id.replace('\'', ' ');
            song_id = song_id.replace(':', ' ');
            song_id = song_id.replace('/', ' ');
            song_id = song_id.replace('\\', ' ');
            song_id = song_id.replaceAll(" ", "");
        }*/
        return song_id;
    }
    
    public static String getAlbumCover(String name, String artist) {
        Album tmp = ODF.get().load().type(Album.class).id(generateAlbumId(name, artist)).now();
        if (tmp != null) {
            return tmp.getCover();
        }
        else { 
            return "";
        }
    }
    
    public static boolean storeNewAlbum(String name, String artist) {
        try {
            new Album(name, artist);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static String getAlbumCoverLastFm(String name, String artist) {
        Album tmp = ODF.get().load().type(Album.class).id(generateAlbumId(name, artist)).now();
        if (tmp != null) {
            if (!tmp.getCover().equals("")) {
                return tmp.getCover();
            }
            else {
                tmp.setCover(Utils.getCoverImage(artist, name));
                ODF.get().update(tmp);
                return tmp.getCover();
            }
        }
        else { 
            return "";
        }
    }
    
    @SuppressWarnings("unused")
    @Id
    private String id;
    
    private String cover;
    
    public Album() {}
    
    public Album(String name, String artist) throws Exception {
        setId(generateAlbumId(name, artist));
        setCover("");
        ODF.get().store().instance(this).ensureUniqueKey().now();
    }

    private void setCover(String cover) {
        this.cover = cover;
    }

    private String getCover() {
        return cover;
    }

    private void setId(String id) {
        this.id = id;
    }

}