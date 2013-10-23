package org.bundolo.client.view.page;

import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import org.bundolo.shared.model.PageDTO;

public interface PageView {
	  //TODO update this class
	
  public interface Presenter {
    void onAddPageButtonClicked();
    void onDeletePageButtonClicked();
    void onEditPageButtonClicked();
    //void onAddContentButtonClicked();
    void onNavigatePageButtonClicked(PageDTO pageDTO);
    PageDTO getCurrentPage();
    org.bundolo.client.presenter.Presenter getPageSpecificPresenter(List<Object> navigationPages);
    void displayMainPanel();
    void displayContextPanel(String presenterName);
    void displayStatusMessage(String statusMessage);
    String getPageDisplayName(PageDTO pageDTO);
    void displayPageSpecificList();
  }
  
  void setPresenter(Presenter presenter);
  void displayNavigationData(List<Object> navigationPages);
  void displayCurrentPageData();
  void displayMainPanel();
  void displayPageSpecificPanel(List<Object> navigationPages);
  void displayContextPanel(org.bundolo.client.presenter.Presenter presenter);
  void displayPageSpecificList(org.bundolo.client.presenter.Presenter presenter);
  void refreshControlsVisibility();
  Widget asWidget();
} 