/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author ValterTexasGroup
 *
 */
@RemoteServiceRelativePath("songsService")
public interface SongsService extends RemoteService {
    
    MusicLibraryDTO rateSong(String user, SongSummaryDTO song, int rating);
}
