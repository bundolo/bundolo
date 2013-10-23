package org.bundolo.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bundolo.server.SessionUtils;
import org.bundolo.server.dao.ContentDAO;
import org.bundolo.server.dao.ContestDAO;
import org.bundolo.server.model.Content;
import org.bundolo.server.model.Contest;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ContestDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.services.ContentService;
import org.bundolo.shared.services.ContestService;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("contestService")
public class ContestServiceImpl implements ContestService {
	
	private static final Logger logger = Logger.getLogger(ContestServiceImpl.class.getName());

	@Autowired
	private ContestDAO contestDAO;
	
	@Autowired
	private ContentDAO contentDAO;
	
	@Autowired
	private ContentService contentService;

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() {
	}
	
	@Override
	public ContestDTO findContest(Long contestId) {
		ContestDTO result = null;
		Contest contest = contestDAO.findById(contestId);
		if(contest != null) {
			result = DozerBeanMapperSingletonWrapper.getInstance().map(contest, ContestDTO.class);
		}
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Long saveContest(ContestDTO contestDTO) throws Exception {
		Long result = null;
		Contest contest = null;
		if (contestDTO.getContestId() != null) {
			contest = contestDAO.findById(contestDTO.getContestId());
		}
		if(contest == null) {
			if (contestDTO.getDescriptionContent() != null) {
				Long contentId = contentService.saveContent(contestDTO.getDescriptionContent());
				contest = new Contest(contestDTO.getContestId(), SessionUtils.getUsername(), contentId, contestDTO.getKind(), new Date(), contestDTO.getExpirationDate(), contestDTO.getContestStatus()); 
				try {
					contestDAO.persist(contest);
				} catch (Exception ex) {
					contentService.deleteContent(contentId);
					throw new Exception("db exception");
				}
				result = contest.getContestId();
			}
		}
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateContest(ContestDTO contestDTO) throws Exception {
		Contest contest = contestDAO.findById(contestDTO.getContestId());

		if(contest != null) {
			ContentDTO descriptionContent = contestDTO.getDescriptionContent();
			if (descriptionContent != null) {
				if (descriptionContent.getContentId() == null) {
					contentService.saveContent(contestDTO.getDescriptionContent());
				} else {
					contentService.updateContent(contestDTO.getDescriptionContent());
				}
			}
			contest.setContestStatus(contestDTO.getContestStatus());
			contest.setCreationDate(contestDTO.getCreationDate());
			contest.setExpirationDate(contestDTO.getExpirationDate());
			contest.setKind(contestDTO.getKind());
			try {
				contestDAO.merge(contest);
			} catch (Exception ex) {
				throw new Exception("db exception");
			}
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteContest(Long contestId) throws Exception {
		Contest contest = contestDAO.findById(contestId);
		if(contest != null) {
			contestDAO.remove(contest);
		}
	}

	@Override
	public List<ContestDTO> findItemListContests(String query, Integer start, Integer end) throws Exception {
		List<Contest> contests = contestDAO.findItemListContests(query, start, end);
		List<ContestDTO> contestDTOs = new ArrayList<ContestDTO>();
		if (contests != null) {
			for (Contest contest : contests) {
				ContestDTO contestDTO = DozerBeanMapperSingletonWrapper.getInstance().map(contest, ContestDTO.class);
				Content descriptionContent = contentDAO.findContentForLocale(contestDTO.getDescriptionContentId(), ContentKindType.contest_description, SessionUtils.getUserLocale());
				if (descriptionContent != null) {
					contestDTO.setDescriptionContent(DozerBeanMapperSingletonWrapper.getInstance().map(descriptionContent, ContentDTO.class));
				}
				contestDTOs.add(contestDTO);				
			}
		}
		return contestDTOs;
	}

	@Override
	public Integer findItemListContestsCount(String query) throws Exception {
		return contestDAO.findItemListContestsCount(query);
	}

}
