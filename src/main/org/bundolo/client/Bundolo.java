package org.bundolo.client;

import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.connection.ConnectionUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.presenter.LoginPresenter;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.services.ContentService;
import org.bundolo.shared.services.ContentServiceAsync;
import org.bundolo.shared.services.UserProfileService;
import org.bundolo.shared.services.UserProfileServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.hydro4ge.raphaelgwt.client.IconPaths;
import com.hydro4ge.raphaelgwt.client.Raphael;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Bundolo implements EntryPoint {
	
	private Logger logger = Logger.getLogger(Bundolo.class.getName());	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		logger.log(Level.FINE, "onModuleLoad start");
		
		//TODO design a way to store locale (cookie probably)
		
		ContentServiceAsync contentService = GWT.create(ContentService.class);
		contentService.getLabelsForLocale(Constants.DEFAULT_LOCALE, new AsyncCallback<Map<String, String>>() {
    		@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error getting locale labels: " + caught);
			}
			@Override
			public void onSuccess(Map<String, String> result) {
				if (result != null) {
					LocalStorage.getInstance().getMessageResource().setLocaleLabels(result);
					LocalStorage.getInstance().refreshNavigationPages();
					loadPage();
				}
			}
		});

		//TODO display some animated image instead
		RootPanel.get().add(new HTMLPanel("Loading..."));

//		HTMLPanel panel = new HTMLPanel("");
//		panel.add(new ExtendedButton(IconPaths.home));
//		panel.add(new ExtendedButton(IconPaths.chat));
//		panel.add(new ExtendedButton(IconPaths.connect));
//		panel.add(new ExtendedButton(IconPaths.download));
//		panel.add(new ExtendedButton(IconPaths.mail));
//		RootPanel.get().add(new Navigation(Window.getClientWidth(), Window.getClientHeight()));
	}
	
	private void loadPage() {
		AppController appViewer = new AppController();
	    
	    String sessionId = Cookies.getCookie(Constants.SESSION_ID_COOKIE_NAME);
	    String rememberMe = Cookies.getCookie(Constants.REMEMBER_ME_COOKIE_NAME);
	    logger.log(Level.WARNING, "sessionId: " + sessionId + ", rememberMe: " + rememberMe);	
	    if (Utils.hasText(sessionId) || Utils.hasText(rememberMe)) {
	    	UserProfileServiceAsync userProfileService = GWT.create(UserProfileService.class);
	    	userProfileService.validateSession(new AsyncCallback<UserDTO>() {
	    		@Override
				public void onFailure(Throwable caught) {
	    			logger.log(Level.SEVERE, "Error validating session user: ", caught);
				}
				@Override
				public void onSuccess(UserDTO result) {
					if (result != null) {
						logger.log(Level.WARNING, "validateSession onSuccess: " + result.getUsername() + ", " + result.getSessionId());						
						Cookies.setCookie(Constants.SESSION_ID_COOKIE_NAME, result.getSessionId(), Utils.addMinutes(new Date(), 15));
						LoginPresenter loginPresenter = (LoginPresenter)LocalStorage.getInstance().getPresenter(PresenterName.login.name());
						if (loginPresenter != null) {
							logger.log(Level.WARNING, "setting loginPresenter userDTO: " +result.getUsername());
							loginPresenter.setUserDTO(result);
							LocalStorage.getInstance().getEventBus().fireEvent(new UserAccessRightsUpdatedEvent());
						}
					}
				}
			});
	    }
	    
	    appViewer.go(RootPanel.get());
	}
}
