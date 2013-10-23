package org.bundolo.client.view.list;

import org.bundolo.shared.model.ConnectionDTO;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ContestDTO;
import org.bundolo.shared.model.UserDTO;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

  public class ContentCell extends AbstractCell {

    public ContentCell() {
    }

	@Override
	public void render(Context context, Object value, SafeHtmlBuilder sb) {
		if (value != null) {
			if (value instanceof ContentDTO) {
				ContentDTO contentDTO = (ContentDTO) value;
				switch(contentDTO.getKind()) {
				case forum_group:
					sb.appendHtmlConstant(contentDTO.getText());
					break;
				case forum_post:
					//TODO this needs to be done properly
					String postTextTrimmed = contentDTO.getText();
					postTextTrimmed = postTextTrimmed.replaceAll("\\<[^>]*>","");
					postTextTrimmed = postTextTrimmed.substring(0, Math.min(postTextTrimmed.length() - 1, 30));
					if (postTextTrimmed.length() == 30) {
						postTextTrimmed += "...";
					}
					sb.appendHtmlConstant(contentDTO.getAuthorDisplayName() + " " + postTextTrimmed);
					break;
				case text:
					sb.appendHtmlConstant(contentDTO.getAuthorDisplayName() + " " + contentDTO.getName());
					break;
				case news:
					sb.appendHtmlConstant(contentDTO.getName());
					break;
				default:
					sb.appendHtmlConstant(contentDTO.getName());
					break;
				}
				
			} else if (value instanceof UserDTO) {
				UserDTO userDTO = (UserDTO) value;
				sb.appendHtmlConstant(userDTO.getUsername());
			} else if (value instanceof ContestDTO) {
				ContestDTO contestDTO = (ContestDTO) value;
				sb.appendHtmlConstant(contestDTO.getDescriptionContent().getName());
			} else if (value instanceof ConnectionDTO) {
				ConnectionDTO connectionDTO = (ConnectionDTO) value;
				sb.appendHtmlConstant(connectionDTO.getDescriptionContent().getName());
			}
	      }
	}
  }
