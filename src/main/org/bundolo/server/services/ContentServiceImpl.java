package org.bundolo.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bundolo.server.GlobalStorage;
import org.bundolo.server.SessionUtils;
import org.bundolo.server.dao.ContentDAO;
import org.bundolo.server.model.Content;
import org.bundolo.server.model.Rating;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ContentStatusType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.RatingKindType;
import org.bundolo.shared.model.enumeration.RatingStatusType;
import org.bundolo.shared.services.ContentService;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.maydu.gwt.validation.client.server.ServerValidation;

@Service("contentService")
public class ContentServiceImpl implements ContentService, ApplicationContextAware {
	
	private static final Logger logger = Logger.getLogger(ContentServiceImpl.class.getName());
	
	private ApplicationContext applicationContext;

	@Autowired
	private ContentDAO contentDAO;

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() {
	}

	public ContentDTO findContent(Long contentId) {
		ContentDTO result = null;
		Content content = contentDAO.findById(contentId);
		if(content != null) {
			result = DozerBeanMapperSingletonWrapper.getInstance().map(content, ContentDTO.class);
			result.setDescriptionContent(getDescriptionContent(content.getContentId(), content.getKind()));
		}
		return result;
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteContent(Long contentId) throws Exception {

		Content content = contentDAO.findById(contentId);

		if(content != null)
			contentDAO.remove(content);

	}

	/* (non-Javadoc)
	 * @see org.bundolo.shared.services.ContentService#findContents(java.lang.Long, org.bundolo.shared.model.enumeration.ContentKindType)
	 */
	@Override
	public List<ContentDTO> findContents(Long parentContentId, ContentKindType kind) throws Exception {
		List<ContentDTO> contentDTOs = new ArrayList<ContentDTO>();
//		switch (kind) {
//		case text_comment:
//			contentDTOs.add(getDescriptionContent(parentContentId, ContentKindType.text_description));
//			break;
//		case episode_group_comment:
//			//TODO see about this, retrieving description here instead from list, to improve performance
////			contentDTOs.add(getDescriptionContent(parentContentId)); 
//			break;
//		default:
//			break;
//		}
		List<Content> contents = contentDAO.findContents(parentContentId, kind, SessionUtils.getUserLocale());
		if (Utils.hasElements(contents)) {
			for (Content content : contents) {
				ContentDTO contentDTO = DozerBeanMapperSingletonWrapper.getInstance().map(content, ContentDTO.class);				
				List<ContentDTO> childContents = findContents(contentDTO.getContentId(), kind);
				if (Utils.hasElements(childContents)) {
					contentDTO.setContentChildren(childContents);
				}
//				contentDTO.setDescriptionContent(getDescriptionContent(content.getContentId(), content.getKind()));
				contentDTOs.add(contentDTO);
			}
		}
		return contentDTOs;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Long saveContent(ContentDTO contentDTO) throws Exception {
		//Map<String, String> labels = getLabelsForLocale(SessionUtils.getUserLocale());
		
		//ServerValidation.exception(LabelType.text_adding_failed.name(), LabelType.text_name.name());
		//new ServerValidation(true).notEqual(contentDTO.getName(), "kiloster", LabelType.text_name.name());
		
		Long result = null;
		Content content = null;
		if(contentDTO.getContentId() != null) {
			content = contentDAO.findById(contentDTO.getContentId());
		}
		if(content == null) {
			Rating rating;
			if (contentDTO.getRating() != null) {
				rating = DozerBeanMapperSingletonWrapper.getInstance().map(contentDTO.getRating(), Rating.class);
			} else {
				rating = new Rating(null, SessionUtils.getUsername(), RatingKindType.general, new Date(), RatingStatusType.active, 0L);
			}
			content = new Content(contentDTO.getContentId(), SessionUtils.getUsername(), contentDTO.getParentContentId(), contentDTO.getKind(), contentDTO.getName(), contentDTO.getText(), SessionUtils.getUserLocale(), new Date(), contentDTO.getContentStatus(), rating);
			rating.setParentContent(content);
			try {
				contentDAO.persist(content);
			} catch (Exception ex) {
				//TODO once db constraints fully implemented, check type of exception here and throw appropriate validation exception
				throw new Exception("db exception");
			}
			
			ContentKindType descriptionContentKind = Utils.getDescriptionContentKind(content.getKind());
			if (descriptionContentKind != null) {
				Content descriptionContent = new Content(null, SessionUtils.getUsername(), content.getContentId(), descriptionContentKind, null, null, SessionUtils.getUserLocale(), new Date(), ContentStatusType.active, null);
				if (contentDTO.getDescriptionContent() != null) {
					descriptionContent.setText(contentDTO.getDescriptionContent().getText());
				}
				contentDAO.persist(descriptionContent);
			}
			result = content.getContentId();
		}
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateContent(ContentDTO contentDTO) throws Exception {
		logger.log(Constants.SERVER_WARN_LOG_LEVEL, "updateContent: " + contentDTO.getContentId());
		Content content = contentDAO.findById(contentDTO.getContentId());

		if(content != null) {
			//content.setAuthorUsername(getUsername()); //we want to keep the original author
			content.setParentContentId(contentDTO.getParentContentId());
			content.setKind(contentDTO.getKind());
			content.setName(contentDTO.getName());
			content.setText(contentDTO.getText());
			//content.setLocale(getUserLocale());
			//content.setCreationDate(contentDTO.getCreationDate());
			content.setContentStatus(contentDTO.getContentStatus());
			if (content.getRating() == null) {
				Rating rating = new Rating(null, SessionUtils.getUsername(), RatingKindType.general, new Date(), RatingStatusType.active, 0L);
				content.setRating(rating);
				rating.setParentContent(content);
			}
			if (contentDTO.getRating() != null) {
				content.getRating().setValue(contentDTO.getRating().getValue());
			}
			try {
				contentDAO.merge(content);
			} catch (Exception ex) {
				//TODO once db constraints fully implemented, check type of exception here and throw appropriate validation exception
				throw new Exception("db exception");
			}
			
			ContentKindType descriptionContentKind = Utils.getDescriptionContentKind(content.getKind());
			if (contentDTO.getDescriptionContent() != null && descriptionContentKind != null) {
				ContentDTO descriptionContent = null;
				if (contentDTO.getContentId() != null) {
					descriptionContent = getDescriptionContent(contentDTO.getContentId(), contentDTO.getKind());
				}
				if (descriptionContent != null) {
					descriptionContent.setText(contentDTO.getDescriptionContent().getText());
					updateContent(descriptionContent);
				} else { //just for db inconsistencies
					Content newDescriptionContent = new Content(null, SessionUtils.getUsername(), content.getContentId(), descriptionContentKind, null, contentDTO.getDescriptionContent().getText(), SessionUtils.getUserLocale(), new Date(), ContentStatusType.active, null);
					contentDAO.persist(newDescriptionContent);
				}
			}
		}
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Map<String, String> getLabelsForLocale(String locale) {
		logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "getLabelsForLocale: " + locale);
		SessionUtils.setAttribute("locale", locale);
		GlobalStorage globalStorage = (GlobalStorage)applicationContext.getBean("globalStorage");
		return globalStorage.getLabelsForLocale(locale);
	}

	@Override
	public List<ContentDTO> findItemListContents(String query, Integer start, Integer end) throws Exception {
		List<Content> contents = contentDAO.findItemListContents(query, start, end);
		List<ContentDTO> contentDTOs = new ArrayList<ContentDTO>();
		if (contents != null) {
			for (Content content : contents) {
				ContentDTO contentDTO = DozerBeanMapperSingletonWrapper.getInstance().map(content, ContentDTO.class);
//				contentDTO.setDescriptionContent(getDescriptionContent(contentDTO));
				contentDTOs.add(contentDTO);				
			}
		}
		return contentDTOs;
	}

	@Override
	public Integer findItemListContentsCount(String query) throws Exception {
		return contentDAO.findItemListContentsCount(query);
	}
	
	@Override
	public ContentDTO getDescriptionContent(Long parentContentId, ContentKindType parentKind) {
		logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "getDescriptionContent: " + parentContentId + ", " + parentKind);
		ContentDTO result = null;
		ContentKindType descriptionContentKind = Utils.getDescriptionContentKind(parentKind);
		if (descriptionContentKind != null) {
			List<Content> descriptionContents = contentDAO.findContents(parentContentId, descriptionContentKind, SessionUtils.getUserLocale());
			if (Utils.hasElements(descriptionContents)) {
				result = DozerBeanMapperSingletonWrapper.getInstance().map(descriptionContents.get(0), ContentDTO.class);
			}
		}
		return result;
	}

	@Override
	public void saveLabels(Map<String, String> localeLabels) throws Exception {
		//TODO
		
	}

}
