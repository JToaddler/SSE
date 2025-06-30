package com.madlabs.live.dao;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.madlabs.live.model.SseSession;
import com.madlabs.live.repo.SseSessionRepository;

@Component
public class SseSessionDao {

	Logger logger = LoggerFactory.getLogger(SseSessionDao.class);

	private String hostName;

	@Autowired
	SseSessionRepository sseSessionRepository;

	public void saveSession(String uuid) {

		SseSession sseSession = new SseSession();
		sseSession.setUuid(uuid);
		sseSession.setHost(hostName);
		sseSessionRepository.save(sseSession);
	}

	public void saveSession(SseSession sseSession) {
		sseSessionRepository.save(sseSession);
	}

	public void deleteSession(String uuid) {
		sseSessionRepository.deleteByUuid(uuid);
	}

	public SseSessionDao() {
		try {
			this.hostName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			logger.error("Error getting host name: {} ", e.getMessage());
		}
	}

}
