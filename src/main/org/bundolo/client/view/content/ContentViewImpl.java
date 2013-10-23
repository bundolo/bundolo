package org.bundolo.client.view.content;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentViewImpl extends Composite implements ContentView {

	private static final Logger logger = Logger.getLogger(ContentViewImpl.class.getName());

	@UiTemplate("ContentView.ui.xml")
	interface ContentViewUiBinder extends UiBinder<Widget, ContentViewImpl> {
	}

	private static ContentViewUiBinder uiBinder = GWT.create(ContentViewUiBinder.class);

	//HTMLPanel contentText;
	@UiField
	DisclosurePanel contentPanel;
	@UiField
	RaphaelButtonWidget addCommentButton;
//	PushButton deleteContentButton;
//	PushButton editContentButton;
//	@UiField
//	HTMLPanel contentControls;
	@UiField
	VerticalPanel commentContainer;
	
	@UiField
	HTMLPanel contentContainer;

	private Presenter presenter;

	public ContentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		//TODO i18n
		contentPanel.setHeader(new Label("Comments"));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		refreshControlsVisibility();
	}

	public Widget asWidget() {
		return this;
	}

	@Override
	public void displayContent() {
		
		contentContainer.clear();
		
		
		//TODO display author display name in some cases, maybe something else in others
		if (ContentKindType.text.equals(presenter.getContentDTO().getKind())) {
			contentContainer.add(new HTML(presenter.getContentDTO().getAuthorDisplayName()));
		} else {
			
		}
		
		contentContainer.add(new HTML(presenter.getContentDTO().getName()));
		if (presenter.getContentDTO().getDescriptionContent() != null && Utils.getDescriptionContentKind(presenter.getContentDTO().getKind()) != null) {
			contentContainer.add(new HTML(presenter.getContentDTO().getDescriptionContent().getText()));
		}
		
		contentContainer.add(new HTML(presenter.getContentDTO().getText()));
		String ratingValue = "0";
		if (presenter.getContentDTO().getRating() != null) {
			ratingValue = presenter.getContentDTO().getRating().getValue().toString();
		}
		contentContainer.add(new Label("TODO Rating: " + ratingValue));
		commentContainer.clear();
		
		//TODO comments are sometimes in the wrong order; verify this, shouldn't be the case anymore
		addComments(presenter.getContentDTO().getContentChildren(), commentContainer);
		refreshControlsVisibility();
	}
	
	private void addComments(List<ContentDTO> comments, HasWidgets container) {
		if (Utils.hasElements(comments)) {
			for(ContentDTO contentDTO: comments) {
				if (contentDTO != null) {
					addComment(contentDTO, container);					
				}
			}
		}
	}
	
	private void addComment(final ContentDTO contentDTO, HasWidgets container) {
		HorizontalPanel commentEntryPanel = new HorizontalPanel();
		commentEntryPanel.setStyleName("contentEntry");
		VerticalPanel childCommentContainer = new VerticalPanel();
		childCommentContainer.setStyleName("contentContent");
		HTMLPanel commentControls = new HTMLPanel("");
		commentControls.setStyleName("contentControls");
		RaphaelButtonWidget addContentButton = new RaphaelButtonWidget("comment", "add_comment", "add");
		addContentButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presenter != null) {
					presenter.onAddCommentButtonClicked(contentDTO);
				}
			}

		});
		RaphaelButtonWidget deleteContentButton =  new RaphaelButtonWidget("comment", "delete_comment", "remove");
		deleteContentButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//TODO
				if (Window.confirm("Are you sure you want to delete?")) {
					if (presenter != null) {
						presenter.onDeleteCommentButtonClicked(contentDTO);
					}
				}				
			}

		});
		RaphaelButtonWidget editContentButton = new RaphaelButtonWidget("comment", "edit_comment", "edit");
		editContentButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presenter != null) {
					presenter.onEditCommentButtonClicked(contentDTO);
				}
			}

		});
		commentControls.add(addContentButton);
		commentControls.add(deleteContentButton);
		commentControls.add(editContentButton);
		
		HTMLPanel contentText = new HTMLPanel(contentDTO.getText());
		contentText.setStyleName("contentText");
		commentEntryPanel.add(contentText);
		commentEntryPanel.add(commentControls);
		childCommentContainer.add(commentEntryPanel);
		addComments(contentDTO.getContentChildren(), childCommentContainer);
		container.add(childCommentContainer);
	}
	
	 @UiHandler("addCommentButton")
	 void onAddCommentButtonClicked(ClickEvent event) {
		 logger.log(Level.INFO, "onAddCommentButtonClicked");
		 if (presenter != null) {
			 presenter.onAddCommentButtonClicked(presenter.getContentDTO());
		 }
	 }

	@Override
	public void refreshControlsVisibility() {
		// TODO Auto-generated method stub
	}

}