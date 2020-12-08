package com.misterveiga.cds.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.entities.Action;
import com.misterveiga.cds.entities.BannedUser;
import com.misterveiga.cds.entities.MutedUser;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

@Component
public class CdsDataImpl {

	private static Logger log = LoggerFactory.getLogger(CdsDataImpl.class);

	@Autowired
	public MongoTemplate mongoTemplate;

	public CdsDataImpl() {
		// Empty
	}

	public void insertAction(final Action commandAction) {
		this.mongoTemplate.insert(commandAction);
		log.debug("Perstisted action {}", commandAction);
	}

	public void insertBannedUser(final BannedUser bannedUser) {
		this.mongoTemplate.insert(bannedUser);
	}

	public void removeBannedUser(final Long userId) {
		final MongoCollection<Document> banCollection = this.mongoTemplate.getCollection("bans");
		final BasicDBObject query = new BasicDBObject();
		query.append("bannedUserId", userId);
		banCollection.findOneAndDelete(query);
	}

	public void insertMutedUser(final MutedUser mutedUser) {
		this.mongoTemplate.insert(mutedUser);
	}

	public void removeMutedUser(final Long userId) {
		final MongoCollection<Document> muteCollection = this.mongoTemplate.getCollection("mutes");
		final BasicDBObject query = new BasicDBObject();
		query.append("mutedUserId", userId);
		muteCollection.findOneAndDelete(query);
	}

	public List<MutedUser> getMutedUsers() {
		final List<MutedUser> mutedUsers = new ArrayList<>();
		// TODO
		return mutedUsers;
	}

	public List<BannedUser> getBannedUsers() {
		final List<BannedUser> bannedUsers = new ArrayList<>();
		// TODO
		return bannedUsers;
	}

	private MutedUser getMutedUser(final Long userId) {
		final MongoCollection<Document> muteCollection = this.mongoTemplate.getCollection("mutes");
		final BasicDBObject query = new BasicDBObject();
		query.append("mutedUserId", userId);
		final MutedUser mutedUser = muteCollection.find(query, MutedUser.class).first();
		return mutedUser;
	}

	private BannedUser getBannedUser(final Long userId) {
		final MongoCollection<Document> banCollection = this.mongoTemplate.getCollection("bans");
		final BasicDBObject query = new BasicDBObject();
		query.append("bannedUserId", userId);
		final BannedUser bannedUser = banCollection.find(query, BannedUser.class).first();
		return bannedUser;
	}

	public Boolean isMuted(final Long userId) {
		final MutedUser mutedUser = getMutedUser(userId);
		if (mutedUser != null) {
			return true;
		}
		return false;
	}

	public Boolean isBanned(final Long userId) {
		final BannedUser bannedUser = getBannedUser(userId);
		if (bannedUser != null) {
			return true;
		}
		return false;
	}

}
