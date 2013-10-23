package org.bundolo.client.view.page;

import java.util.List;

import org.bundolo.shared.model.PageDTO;
import org.bundolo.shared.model.enumeration.PageKindType;
import org.bundolo.shared.model.enumeration.PageStatusType;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;

public interface EditPageView {

	public interface Presenter {
		void onSavePageButtonClicked();
		void onCancelPageButtonClicked();
		PageDTO getCurrentPage();
		void setCurrentPage(PageDTO currentPage);
		List<PageDTO> getPageChildren();
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getName();
	String getText();
	PageKindType getKind();
	PageStatusType getPageStatus();
	List<String> getChildren();
	void displayChildren();
}