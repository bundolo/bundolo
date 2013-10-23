package org.bundolo.shared.services;

import java.util.List;

import org.bundolo.shared.model.ContestDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ContestServiceAsync {

	void findContest(Long contestId, AsyncCallback<ContestDTO> callback);

	void saveContest(ContestDTO contestDTO, AsyncCallback<Long> callback);

	void updateContest(ContestDTO contestDTO, AsyncCallback<Void> callback);

	void deleteContest(Long contestId, AsyncCallback<Void> callback);

	void findItemListContests(String query, Integer start, Integer end, AsyncCallback<List<ContestDTO>> callback);

	void findItemListContestsCount(String query, AsyncCallback<Integer> callback);

}
