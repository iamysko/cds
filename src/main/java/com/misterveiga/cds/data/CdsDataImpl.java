package com.misterveiga.cds.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.entities.Action;
import com.misterveiga.cds.entities.BannedUser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

@Component
public class CdsDataImpl {

	private static Logger log = LoggerFactory.getLogger(CdsDataImpl.class);

	@Autowired
	public MongoTemplate mongoTemplate;

	public CdsDataImpl() {
	}

	public void insertAction(final Action commandAction) {
		this.mongoTemplate.insert(commandAction);
		log.debug("Perstisted action {}", commandAction);
	}

	public void insertBannedUser(final BannedUser bannedUser) {
		this.mongoTemplate.insert(bannedUser);
	}

	public void removeBannedUser(final Long userId) {
		final DBCollection banCollection = this.mongoTemplate.getCollection("bans");
		final BasicDBObject query = new BasicDBObject();
		query.append("bannedUserId", userId);
		banCollection.remove(query);
	}

}
