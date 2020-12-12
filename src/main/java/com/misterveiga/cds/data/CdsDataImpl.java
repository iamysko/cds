package com.misterveiga.cds.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.entities.Action;
import com.misterveiga.cds.entities.BannedUser;
import com.misterveiga.cds.entities.MutedUser;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;

@Component
public class CdsDataImpl implements CdsData {

	private static Logger log = LoggerFactory.getLogger(CdsDataImpl.class);

	@Autowired
	public MongoTemplate mongoTemplate;

	public CdsDataImpl() {
	}

	@Override
	public void insertAction(final Action commandAction) {
		this.mongoTemplate.insert(commandAction);
		log.debug("Perstisted action {}", commandAction);
	}

	/**
	 * Insert banned user.
	 *
	 * @param bannedUser the banned user
	 */
	@Override
	public void insertBannedUser(final BannedUser bannedUser) {
		this.mongoTemplate.insert(bannedUser);
		log.debug("Perstisted banned user {}", bannedUser);
	}

	/**
	 * Removes the banned user.
	 *
	 * @param userId the user id
	 */
	@Override
	public void removeBannedUser(final Long userId) {
		final MongoCollection<Document> banCollection = this.mongoTemplate.getCollection("bans");
		final BasicDBObject query = new BasicDBObject();
		query.append("bannedUserId", userId);
		banCollection.findOneAndDelete(query);
	}

	/**
	 * Insert muted user.
	 *
	 * @param mutedUser the muted user
	 */
	@Override
	public void insertMutedUser(final MutedUser mutedUser) {
		this.mongoTemplate.insert(mutedUser);
	}

	/**
	 * Removes the muted user.
	 *
	 * @param userId the user id
	 */
	@Override
	public void removeMutedUser(final Long userId) {
		final MongoCollection<Document> muteCollection = this.mongoTemplate.getCollection("mutes");
		final BasicDBObject query = new BasicDBObject();
		query.append("mutedUserId", userId);
		muteCollection.findOneAndDelete(query);
	}

	/**
	 * Gets the muted user.
	 *
	 * @param userId the user id
	 * @return the muted user
	 */
	@Override
	public MutedUser getMutedUser(final Long userId) {
		final MongoCollection<Document> muteCollection = this.mongoTemplate.getCollection("mutes");
		final BasicDBObject query = new BasicDBObject();
		query.append("mutedUserId", userId);
		final MutedUser mutedUser = muteCollection.find(query, MutedUser.class).first();
		return mutedUser;
	}

	/**
	 * Gets the banned user.
	 *
	 * @param userId the user id
	 * @return the banned user
	 */
	@Override
	public BannedUser getBannedUser(final Long userId) {
		final MongoCollection<Document> banCollection = this.mongoTemplate.getCollection("bans");
		final BasicDBObject query = new BasicDBObject();
		query.append("bannedUserId", userId);
		final BannedUser bannedUser = banCollection.find(query, BannedUser.class).first();
		return bannedUser;
	}

	/**
	 * Gets the muted users.
	 *
	 * @return the muted users
	 */
	@Override
	public List<MutedUser> getMutedUsers() {
		final List<MutedUser> mutedUsers = new ArrayList<>();

		final MongoCollection<Document> muteCollection = this.mongoTemplate.getCollection("bans");

		final CodecRegistry registry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		muteCollection.withCodecRegistry(registry).find(new BasicDBObject(), MutedUser.class).forEach((mutedUser) -> {
			mutedUsers.add(mutedUser);
		});

		return mutedUsers;
	}

	/**
	 * Gets the banned users.
	 *
	 * @return the banned users
	 */
	@Override
	public List<BannedUser> getBannedUsers() {
		final List<BannedUser> bannedUsers = new ArrayList<>();

		final MongoCollection<Document> banCollection = this.mongoTemplate.getCollection("bans");

		final CodecRegistry registry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		banCollection.withCodecRegistry(registry).find(new BasicDBObject(), BannedUser.class).forEach((bannedUser) -> {
			bannedUsers.add(bannedUser);
		});

		return bannedUsers;
	}

	/**
	 * Checks if is muted.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	@Override
	public Boolean isMuted(final Long userId) {
		final MutedUser mutedUser = getMutedUser(userId);
		if (mutedUser != null) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if is banned.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	@Override
	public Boolean isBanned(final Long userId) {
		final BannedUser bannedUser = getBannedUser(userId);
		if (bannedUser != null) {
			return true;
		}
		return false;
	}

}
