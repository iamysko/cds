package com.misterveiga.cds.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.entities.Action;

@Component
public class CdsDataImpl {

	private static Logger log = LoggerFactory.getLogger(CdsDataImpl.class);

	@Autowired
	public MongoTemplate mongoTemplate;

	public CdsDataImpl() {
	}

	public void insertAction(final Action commandAction) {
		this.mongoTemplate.insert(commandAction);
		log.info("Perstisted action {}", commandAction);
	}

}
