package org.bundolo.client.view.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.presenter.EditPagePresenter;
import org.bundolo.client.validation.LabelTextActionExtended;
import org.bundolo.client.validation.NotEmptyValidatorExtended;
import org.bundolo.client.widget.EnumListBox;
import org.bundolo.client.widget.ExtendedHTMLPanel;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.model.PageDTO;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.PageKindType;
import org.bundolo.shared.model.enumeration.PageStatusType;

import com.axeiya.gwtckeditor.client.CKConfig;
import com.axeiya.gwtckeditor.client.CKConfig.TOOLBAR_OPTIONS;
import com.axeiya.gwtckeditor.client.CKEditor;
import com.axeiya.gwtckeditor.client.Toolbar;
import com.axeiya.gwtckeditor.client.ToolbarLine;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.LabelTextAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;

public class EditPageViewImpl extends Composite implements EditPageView {
	
	private static final Logger logger = Logger.getLogger(EditPageViewImpl.class.getName());

	@UiTemplate("EditPageView.ui.xml")
	interface EditPageViewUiBinder extends UiBinder<Widget, EditPageViewImpl> {
	}

	private static EditPageViewUiBinder uiBinder = GWT.create(EditPageViewUiBinder.class);

	@UiField
	TextBox name;

	@UiField(provided = true)
	EnumListBox<PageKindType> kind = new EnumListBox<PageKindType>(PageKindType.class);

	//@UiField(provided = true)
	EnumListBox<PageStatusType> pageStatus = new EnumListBox<PageStatusType>(PageStatusType.class);
	
	@UiField(provided = true)
	CKEditor text;

	@UiField
	ExtendedHTMLPanel childrenContainer;
	
//	@UiField
//	FocusPanel childrenTarget;
//	
//	@UiField
//	HTMLPanel childrenContainerTarget;

	@UiField
	RaphaelButtonWidget savePageButton;

	@UiField
	RaphaelButtonWidget cancelPageButton;
	
	@UiField Label kindLabel;
	@UiField Label nameLabel;
	@UiField Label textLabel;
	@UiField Label childrenLabel;

	private Presenter presenter;
	
	Label draggedLabel;

	public EditPageViewImpl() {
		CKConfig CONFIG_MODIFICATION = new CKConfig();
        // Creating personalized toolbar
        ToolbarLine line = new ToolbarLine();
        // Define the first line
        TOOLBAR_OPTIONS[] t1 = { TOOLBAR_OPTIONS.Preview, TOOLBAR_OPTIONS.Source, TOOLBAR_OPTIONS._,
                        TOOLBAR_OPTIONS.Cut, TOOLBAR_OPTIONS.Copy,TOOLBAR_OPTIONS.Paste, TOOLBAR_OPTIONS.PasteText,TOOLBAR_OPTIONS.PasteFromWord, TOOLBAR_OPTIONS._,
                        TOOLBAR_OPTIONS.Templates, TOOLBAR_OPTIONS._, TOOLBAR_OPTIONS.Print, TOOLBAR_OPTIONS.SpellChecker, TOOLBAR_OPTIONS.Scayt,TOOLBAR_OPTIONS._, 
                        TOOLBAR_OPTIONS.Undo, TOOLBAR_OPTIONS.Redo,TOOLBAR_OPTIONS._, 
                        TOOLBAR_OPTIONS.Find,TOOLBAR_OPTIONS.Replace, TOOLBAR_OPTIONS._, TOOLBAR_OPTIONS.SelectAll, TOOLBAR_OPTIONS.RemoveFormat };
        line.addAll(t1);

        // Define the second line
        ToolbarLine line2 = new ToolbarLine();
        TOOLBAR_OPTIONS[] t2 = { 
                        TOOLBAR_OPTIONS.Bold, TOOLBAR_OPTIONS.Italic,TOOLBAR_OPTIONS.Underline, TOOLBAR_OPTIONS.Strike,
                        TOOLBAR_OPTIONS.Subscript, TOOLBAR_OPTIONS.Superscript, TOOLBAR_OPTIONS._, 
                        TOOLBAR_OPTIONS.NumberedList,TOOLBAR_OPTIONS.BulletedList, TOOLBAR_OPTIONS._,
                        TOOLBAR_OPTIONS.Outdent, TOOLBAR_OPTIONS.Indent,TOOLBAR_OPTIONS.Blockquote, TOOLBAR_OPTIONS._,
                        TOOLBAR_OPTIONS.JustifyLeft, TOOLBAR_OPTIONS.JustifyCenter,     TOOLBAR_OPTIONS.JustifyRight, TOOLBAR_OPTIONS.JustifyBlock, TOOLBAR_OPTIONS._,
                        TOOLBAR_OPTIONS.Link, TOOLBAR_OPTIONS.Unlink, TOOLBAR_OPTIONS.Anchor, TOOLBAR_OPTIONS._,
                        TOOLBAR_OPTIONS.Image,TOOLBAR_OPTIONS.Table, TOOLBAR_OPTIONS.HorizontalRule, TOOLBAR_OPTIONS.Smiley, TOOLBAR_OPTIONS.SpecialChar, TOOLBAR_OPTIONS.PageBreak };
        line2.addAll(t2);

        //define the third line
        ToolbarLine line3 = new ToolbarLine();
        TOOLBAR_OPTIONS[] t3 = { 
                        TOOLBAR_OPTIONS.Styles, TOOLBAR_OPTIONS.Format, TOOLBAR_OPTIONS.Font, TOOLBAR_OPTIONS.FontSize, TOOLBAR_OPTIONS._,
                        TOOLBAR_OPTIONS.TextColor, TOOLBAR_OPTIONS.BGColor,     TOOLBAR_OPTIONS._,
                        TOOLBAR_OPTIONS.Maximize, TOOLBAR_OPTIONS.ShowBlocks };
        line3.addAll(t3);

        // Creates the toolbar
        Toolbar t = new Toolbar();
        t.add(line);
        t.addSeparator();
        t.add(line2);
        t.addSeparator();
        t.add(line3);

        // sets some params
        CONFIG_MODIFICATION.setResizeMinWidth(520);
        CONFIG_MODIFICATION.setResizeMinHeight(250);
        CONFIG_MODIFICATION.setResizeMaxWidth(1600);
        CONFIG_MODIFICATION.setResizeMaxHeight(1600);
        CONFIG_MODIFICATION.setEntities_latin(false);
        CONFIG_MODIFICATION.setEntities(false);
        //CONFIG_MODIFICATION.setWidth(Window.getClientWidth() / 2 + "");
        // Set the toolbar to the config (replace the FULL preset toolbar)
        CONFIG_MODIFICATION.setToolbar(t);
        //TODO prevent editor from alowing to be resized probably. it interferes with sidebar
        
        text = new CKEditor(CONFIG_MODIFICATION);
		initWidget(uiBinder.createAndBindUi(this));
		kindLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_kind));
		nameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_name));
		textLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_text));
		childrenLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_children));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.page_name.name(), new NotEmptyValidatorExtended(name).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(nameLabel, LabelType.page_name)));
		presenter.getValidator().addValidators(LabelType.page_text.name(), new NotEmptyValidatorExtended(text).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(textLabel, LabelType.page_text)));
		//TODO more validation
	}

	@UiHandler("savePageButton")
	void onSavePageButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSavePageButtonClicked();
		}
	}

	@UiHandler("cancelPageButton")
	void onCancelPageButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelPageButtonClicked();
		}
	}

	public Widget asWidget() {
		if (presenter != null) {
			name.setValue(presenter.getCurrentPage().getDescriptionContent().getName());
			kind.setValue(presenter.getCurrentPage().getKind());			
			pageStatus.setValue(presenter.getCurrentPage().getPageStatus());
			text.setHTML(presenter.getCurrentPage().getDescriptionContent().getText());
		}
		childrenContainer.setPixelSize(200, 200);

		return this;
	}

	@Override
	public String getName() {
		return name.getValue();
	}

	@Override
	public PageKindType getKind() {
		return kind.getValue();
	}

	@Override
	public PageStatusType getPageStatus() {
		return pageStatus.getValue();
	}

	@Override
	public List<String> getChildren() {
		ArrayList<String> result = new ArrayList<String>();
		if ((childrenContainer != null) && (childrenContainer.getWidgetCount() > 0)) {
			Iterator<Widget> children = childrenContainer.iterator();
			while (children.hasNext()) {
				Label label = (Label) children.next();
				result.add(label.getText());
			}
		}
		logger.log(Level.FINE, "getChildren: " + result);
		return result;
	}

	@Override
	public void displayChildren() {
		childrenContainer.clear();
		if ((presenter != null) && (presenter.getPageChildren() != null) && (presenter.getPageChildren().size() > 0)) {
			for (PageDTO pageDTO : presenter.getPageChildren()) {
				if (pageDTO != null) {
					if (pageDTO.getDescriptionContent() != null) {
						Label childPageLabel = new Label(pageDTO.getDescriptionContent().getName());
						childrenContainer.add(childPageLabel);
						addHandlers(childPageLabel);
					} else {
						logger.log(Level.FINE, "pageDTO.getDescriptionContent() null: " + pageDTO.getPageId());
					}
				} else {
					logger.log(Level.FINE, "pageDTO null");
				}
//				final Label childPageLabel = new Label(pageDTO.getDescriptionContent().getName());
//				childrenContainer.add(childPageLabel);
//				addHandlers(childPageLabel);
			}
		}
	}
	
	private void addHandlers(final Label childPageLabel) {
		childPageLabel.getElement().setDraggable(Element.DRAGGABLE_TRUE);
		 
		childPageLabel.addDragStartHandler(new DragStartHandler() {
		    @Override
		    public void onDragStart(DragStartEvent event) {
		    	logger.log(Level.INFO, "onDragStart: " + childPageLabel.getText());
		        // required
		        event.setData("dragged", childPageLabel.getText());
		        draggedLabel = childPageLabel;
		        childPageLabel.setVisible(false);
		        //childrenContainer.remove(childPageLabel);
		        // optional: show copy of the image
		        event.getDataTransfer().setDragImage(childPageLabel.getElement(), 10, 10);
		    }
		});
		
		childPageLabel.addDragOverHandler(new DragOverHandler() {
		    @Override
		    public void onDragOver(DragOverEvent event) {
		    	logger.log(Level.INFO, "onDragOver: " + childPageLabel.getText());
		    	childPageLabel.getElement().getStyle().setBackgroundColor("#ffa");
		    }
		});
		
		childPageLabel.addDragLeaveHandler(new DragLeaveHandler () {
		    @Override
		    public void onDragLeave(DragLeaveEvent event) {
		    	logger.log(Level.INFO, "onDragLeave: " + childPageLabel.getText());
		    	childPageLabel.getElement().getStyle().setBackgroundColor("blue");
		    }
		});
		
		childPageLabel.addDropHandler(new DropHandler() {
		    @Override
		    public void onDrop(DropEvent event) {
		    	logger.log(Level.INFO, "onDrop: " + event.getData("dragged"));
		        // prevent the native text drop
		        event.preventDefault();
		 
		        // get the data out of the event
		        //Label droppedLabel = (Label)childrenContainer.getWidget(Integer.parseInt(event.getData("index")));
		        Label droppedLabel = new Label(event.getData("dragged"));
		        //String droppedLabelText = droppedLabel.getText();
		        childrenContainer.remove(draggedLabel);
		        childrenContainer.insert(droppedLabel, childrenContainer.getElement(), childrenContainer.getWidgetIndex(childPageLabel), true);
		        addHandlers(droppedLabel);
		        childPageLabel.getElement().getStyle().setBackgroundColor("blue");
		        //target.setText(data);
		    }
		});
	}

	@Override
	public String getText() {
		return text.getHTML();
	}
}