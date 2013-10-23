package org.bundolo.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface IconResource extends ClientBundle {

	IconResource INSTANCE = GWT.create(IconResource.class);

	@Source("images/comment.png")
    ImageResource comment();
	
    @Source("images/comment_plus.png")
    ImageResource commentPlus();
    
    @Source("images/comment_minus.png")
    ImageResource commentMinus();
    
    @Source("images/comment_edit.png")
    ImageResource commentEdit();
    
    @Source("images/comment_accept.png")
    ImageResource commentSave();
    
    @Source("images/comment_cancel.png")
    ImageResource commentCancel();
    
    @Source("images/folder_text.png")
    ImageResource itemList();
    
    @Source("images/folder_text_plus.png")
    ImageResource itemListPlus();
    
    @Source("images/folder_text_minus.png")
    ImageResource itemListMinus();
    
    @Source("images/folder_text_edit.png")
    ImageResource itemListEdit();
    
    @Source("images/folder_text_accept.png")
    ImageResource itemListSave();
    
    @Source("images/folder_text_cancel.png")
    ImageResource itemListCancel();
    
    @Source("images/user_profile.png")
    ImageResource userProfile();
	
    @Source("images/user_profile_plus.png")
    ImageResource userProfilePlus();
    
    @Source("images/user_profile_minus.png")
    ImageResource userProfileMinus();
    
    @Source("images/user_profile_edit.png")
    ImageResource userProfileEdit();
    
    @Source("images/user_profile_accept.png")
    ImageResource userProfileSave();
    
    @Source("images/user_profile_cancel.png")
    ImageResource userProfileCancel();
    
    @Source("images/user_profile_information.png")
    ImageResource userProfileInformation();
    
    @Source("images/website.png")
    ImageResource page();
    
    @Source("images/website_plus.png")
    ImageResource pagePlus();
    
    @Source("images/website_minus.png")
    ImageResource pageMinus();
    
    @Source("images/website_edit.png")
    ImageResource pageEdit();
    
    @Source("images/website_accept.png")
    ImageResource pageSave();
    
    @Source("images/website_cancel.png")
    ImageResource pageCancel();
    
    @Source("images/document_text.png")
    ImageResource text();
    
    @Source("images/document_text_plus.png")
    ImageResource textPlus();
    
    @Source("images/document_text_minus.png")
    ImageResource textMinus();
    
    @Source("images/document_text_edit.png")
    ImageResource textEdit();
    
    @Source("images/document_text_accept.png")
    ImageResource textSave();
    
    @Source("images/document_text_cancel.png")
    ImageResource textCancel();
    
    @Source("images/mail_plus.png")
    ImageResource messagePlus();
    
    @Source("images/mail_accept.png")
    ImageResource messageSave();
    
    @Source("images/mail_cancel.png")
    ImageResource messageCancel();
    
    @Source("images/news.png")
    ImageResource news();
    
    @Source("images/news_plus.png")
    ImageResource newsPlus();
    
    @Source("images/news_minus.png")
    ImageResource newsMinus();
    
    @Source("images/news_edit.png")
    ImageResource newsEdit();
    
    @Source("images/news_accept.png")
    ImageResource newsSave();
    
    @Source("images/news_cancel.png")
    ImageResource newsCancel();
    
    @Source("images/home.png")
    ImageResource home();
    
    @Source("images/vuvuzela.png")
    ImageResource forum();
    
    @Source("images/vuvuzela_plus.png")
    ImageResource forumPlus();
    
    @Source("images/vuvuzela_minus.png")
    ImageResource forumMinus();
    
    @Source("images/vuvuzela_edit.png")
    ImageResource forumEdit();
    
    @Source("images/vuvuzela_accept.png")
    ImageResource forumSave();
    
    @Source("images/vuvuzela_cancel.png")
    ImageResource forumCancel();
    
    @Source("images/viewer_text.png")
    ImageResource contest();
	
    @Source("images/viewer_text_plus.png")
    ImageResource contestPlus();
    
    @Source("images/viewer_text_minus.png")
    ImageResource contestMinus();
    
    @Source("images/viewer_text_edit.png")
    ImageResource contestEdit();
    
    @Source("images/viewer_text_accept.png")
    ImageResource contestSave();
    
    @Source("images/viewer_text_cancel.png")
    ImageResource contestCancel();
    
    @Source("images/calendar.png")
    ImageResource event();
    
    @Source("images/imprint.png")
    ImageResource user();
    
    @Source("images/internet.png")
    ImageResource link();
    
    @Source("images/internet_plus.png")
    ImageResource linkPlus();
    
    @Source("images/internet_minus.png")
    ImageResource linkMinus();
    
    @Source("images/internet_edit.png")
    ImageResource linkEdit();
    
    @Source("images/internet_accept.png")
    ImageResource linkSave();
    
    @Source("images/internet_cancel.png")
    ImageResource linkCancel();
    
    @Source("images/book_sans.png")
    ImageResource serial();
    
    @Source("images/book_sans_plus.png")
    ImageResource serialPlus();
    
    @Source("images/book_sans_minus.png")
    ImageResource serialMinus();
    
    @Source("images/book_sans_edit.png")
    ImageResource serialEdit();
    
    @Source("images/book_sans_accept.png")
    ImageResource serialSave();
    
    @Source("images/book_sans_cancel.png")
    ImageResource serialCancel();
    
    @Source("images/questionmark.png")
    ImageResource custom();
    
    @Source("images/book_text.png")
    ImageResource episode();
    
    @Source("images/book_text_plus.png")
    ImageResource episodePlus();
    
    @Source("images/book_text_minus.png")
    ImageResource episodeMinus();
    
    @Source("images/book_text_edit.png")
    ImageResource episodeEdit();
    
    @Source("images/book_text_accept.png")
    ImageResource episodeSave();
    
    @Source("images/book_text_cancel.png")
    ImageResource episodeCancel();
    
    @Source("images/information.png")
    ImageResource about();
    
    @Source("images/login.png")
    ImageResource login();
    
    @Source("images/logout.png")
    ImageResource logout();
    
    @Source("images/facebook.png")
    ImageResource facebook();

}