package com.madlabs.live.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.madlabs.live.model.SseSession;

@Repository
public interface SseSessionRepository extends CrudRepository<SseSession, String> {

	public SseSession findByUuid(String uuid);

	public SseSession deleteByUuid(String uuid);

}
