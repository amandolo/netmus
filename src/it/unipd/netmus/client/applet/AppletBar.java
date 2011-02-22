/**
 * 
 */
package it.unipd.netmus.client.applet;

import java.util.List;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.client.service.LibraryServiceAsync;
import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author ValterTexasGroup
 *
 */
public class AppletBar {
	
	private static AppletBar APPLET_BAR = null;
    private boolean visible = false;
    private AppletConstants constants = GWT.create(AppletConstants.class);
    private Label title = new Label(constants.title());
    private Anchor onOff = new Anchor();
    private Anchor rescan = new Anchor(); // VISIBILE FINITA LA SCANSIONE
    private Anchor chooser = new Anchor();
    private TextBox status = new TextBox();
    private HTML applet = new HTML();
    private String user;
    private String original_user;
    private boolean state;
    private TranslateDTOXML translator = new TranslateDTOXML();
    
    private LibraryServiceAsync libraryService = GWT.create(LibraryService.class);
    
    public static AppletBar get(String user, boolean state) {
        if (APPLET_BAR == null)
            APPLET_BAR = new AppletBar(user, state);
        APPLET_BAR.setUser(user);
        return APPLET_BAR;
    }
    
    private void setUser(String user) {
        this.user = user;
    }
    
    private AppletBar(String user, boolean state) {
    	
        this.original_user = user;
    	this.user=user.replaceAll("@\\S*", "");
    	this.state=state;
        
        makeNativeFunction(this);

        title.setSize("150px", "14px");
        
        onOff.setSize("50px","14px");
        if (state) onOff.setText(constants.appletDisable());
        else onOff.setText(constants.appletEnable());
        
        rescan.setSize("50px", "10px");
        rescan.setText(constants.rescan());
        rescan.setVisible(false); // diventera' visibile a fine scansione di un dispositivo
        
        status.setSize("180px", "10px");
        
        chooser.setSize("70px", "10px");
        chooser.setText(constants.chooser());

        RootPanel.get("applet-bar").add(title,5,2);
        RootPanel.get("applet-bar").add(onOff,145,2);    
        RootPanel.get("applet-bar").add(status,195,2);
        RootPanel.get("applet-bar").add(rescan,400,2);
        RootPanel.get("applet-bar").add(chooser,460,2);
        RootPanel.get("applet-bar").add(applet);
        
        onOff.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send changeStatus signal
                changeState();
            }
        });
        
        rescan.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send rescan signal
                reScanAll();
            }
        });
        
        chooser.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send rescan signal
                showChooser();
            }
        });
    }
    
    public void appletBarON() {
        if (!visible) {
            visible = true;
            RootPanel.get("applet-bar").setVisible(true);
            applet.setHTML("<applet " +
            		"style=\"position:relative; left:-99999px;\"" +
            		"id='netmus_applet' name='netmus_applet' " +
                    "code=\"applet/NetmusApplet.class\" " +
                    "archive=\"applet/netmusApplet.jar, applet/jid3lib-0.5.4.jar\" " +
                    "width=1 height=1></applet>");
            
            System.out.println("Applet caricata");
        }
    }
    
    public void appletBarOFF() {
        if (visible) {
            visible = false;
            RootPanel.get("applet-bar").setVisible(false);
            applet.setHTML("");
        }
    }
    
    // metodi per comunicare con l applet
    
    // per dire all'applet di cambiare stato
    // dopo che ONOFF e' stato premuto
    private void changeState() {
        state = !state;
        if (state)
            onOff.setText(constants.appletDisable());
        else
            onOff.setText(constants.appletEnable());
        
        setState(state);
    }
    
    // per dire all'applet di rifare interamente
    // la scansione dopo che RESCAN e' stato premuto
    private void reScanAll() {
        showStatus("RESCAN");
        
        sendRescan();
    }
    
    /**
     * Metodo che deve chiamare il metodo dell'applet 
     * letsGo(String username, boolean state)
     * per mandare user e stato iniziale e avviare il thread
     */
    private void sendStarts() {
    	System.out.println("invio i dati: "+user);
    	sendStartsJSNI(user,state);
    	System.out.println("Inviati!");
    }
    
    private native void sendStartsJSNI( String user, boolean state )/*-{
    	var t = $doc.getElementById('netmus_applet');
    	t.letsGO(user,state);
    }-*/;
    
    private native void setState( boolean state )/*-{
        var t = $doc.getElementById('netmus_applet');
        t.setState(state);
    }-*/;
    
    private native void sendRescan()/*-{
        var t = $doc.getElementById('netmus_applet');
        t.rescanAll();
    }-*/;
    
    private native void showChooser()/*-{
        var t = $doc.getElementById('netmus_applet');
        t.showChooser();
    }-*/;
    
    private void scanningStatus(int actual, int total){
    	//aggiornare la grafica con le nuove info
        showStatus(actual+"/"+total);
    }
    
    private void showStatus(String status){
    	//modifica le informazioni di stato sulla grafica
    	System.out.println("Stato: "+status);
    	this.status.setText("Stato: "+status);
    }
    
    private void translateXML(String result) {
        
        System.out.println("Dati XML arrivati: \n"+result);
        List<SongDTO> new_songs = translator.XMLToDTO(result);
        if (new_songs == null){showStatus("Errore nel parsing XML");}
        
        this.status.setText("Dati XML arrivati");
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                showStatus("Sending Error");
                System.out.println("KO");
                System.err.println(caught.toString());
            }
            @Override
            public void onSuccess(Void result) {
                showStatus("Sent to Server");
                System.out.println("OK");
            }
        };
        
        libraryService.sendUserNewMusic(original_user, new_songs, callback);
        System.out.println("Dati XML spediti");
    }
    
    private void rescanNotVisible() {
        rescan.setVisible(false);
    }
    private void rescanVisible() {
        rescan.setVisible(true);
    }
    
    // metodo per pubblicare le funzioni native di linking
    private native void makeNativeFunction(AppletBar x)/*-{
    $wnd.getStarts = function () {
    x.@it.unipd.netmus.client.applet.AppletBar::sendStarts()();
    };
    $wnd.scanResult = function (result) {
    x.@it.unipd.netmus.client.applet.AppletBar::translateXML(Ljava/lang/String;)(result);
    };
    $wnd.scanStatus = function (actual, total) {
    x.@it.unipd.netmus.client.applet.AppletBar::scanningStatus(II)(actual, total);
    };
    $wnd.showStatus = function (s) {
    x.@it.unipd.netmus.client.applet.AppletBar::showStatus(Ljava/lang/String;)(s);
    };
    $wnd.rescanVisible = function () {
    x.@it.unipd.netmus.client.applet.AppletBar::rescanVisible()();
    };
    $wnd.rescanNotVisible = function () {
    x.@it.unipd.netmus.client.applet.AppletBar::rescanNotVisible()();
    };
    }-*/;
    // mancano parametri sopra in ingresso Stringa XML da applet
}
