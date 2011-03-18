package it.unipd.netmus.client.ui;

import com.google.gwt.i18n.client.Constants;

/**
 * Nome: MyConstants.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 19 Febbraio 2011
*/
public interface MyConstants extends Constants {
	  
	String loginLabel();
	String registerLabel();
	String registerSwitchLabel();
	String loginSwitchLabel();
	String accountGoogle();  
	String accountNetmus();  
	String passwordCheck();
	String errorPassword();
	String errorCPassword();
	String errorEmail();
	String infoUserInsertDb();
	String infoUserUsato();
	String infoUserAlreadyDb();
	String infoCorrectLogin();
	String infoLoginIncorrect();
	String databaseErrorGeneric();
    String accountGoogleLogin();
	String leavingProfilePage();
	String confirmDelete();
	String confirmDeleteProfile();
	String yes();
	String no();
	String downloadPDF();
	
	String addPlaylistError();
	String addSongToPlaylistError();
	String removePlaylistError();
	String deleteSongError();
	String editProfileError();
	String findRelatedUsersError();
	String getPlaylistError();
	String getProfileViewError();
	String getSongDTOError();
	String rateSongError();
	String storeStatisticsError();
	String removeSongFromPlaylistError();
	String loadProfileError();
	String generatePDFError();
	
	}