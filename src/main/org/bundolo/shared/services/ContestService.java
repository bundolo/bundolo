package org.bundolo.shared.services;

import java.util.List;

import org.bundolo.shared.model.ContestDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bundoloServices/contestService")
public interface ContestService extends RemoteService {
	
	public ContestDTO findContest(Long contestId);
	public Long saveContest(ContestDTO contestDTO) throws Exception;
	public void updateContest(ContestDTO contestDTO) throws Exception;
	public void deleteContest(Long contestId) throws Exception;
	
	public List<ContestDTO> findItemListContests(String query, Integer start, Integer end) throws Exception;
	public Integer findItemListContestsCount(String query) throws Exception;
}
