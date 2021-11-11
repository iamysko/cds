/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds;

import java.net.UnknownHostException;
import java.time.Instant;

import javax.annotation.PreDestroy;
import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.misterveiga.cds.listeners.DiscordDownListener;
import com.misterveiga.cds.listeners.DiscordUpListener;
import com.misterveiga.cds.listeners.MessageListener;
import com.misterveiga.cds.listeners.ReactionListener;
import com.misterveiga.cds.telegram.TelegramService;
import com.mongodb.client.MongoClients;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * The Class AppConfig.
 */
@Configuration
@EnableConfigurationProperties
@EnableScheduling
@PropertySource("classpath:application.properties")
public class AppConfig {

	/** The mongo database. */
	@Value("${spring.data.mongodb.database}")
	public String mongoDatabase;

	/**
	 * Jda.
	 *
	 * @param discordUpListener   the discord up listener
	 * @param discordDownListener the discord down listener
	 * @param reactionListener    the reaction listener
	 * @param messageListener     the message listener
	 * @param jdaToken            the jda token
	 * @return the jda
	 */
	@Bean
	JDA jda(@Qualifier("discordUpListener") final DiscordUpListener discordUpListener,
			@Qualifier("discordDownListener") final DiscordDownListener discordDownListener,
			@Qualifier("reactionListener") final ReactionListener reactionListener,
			@Qualifier("messageListener") final MessageListener messageListener,
			@Value("${jda.token}") final String jdaToken) {
		final JDABuilder builder = JDABuilder.createDefault(jdaToken);
		builder.addEventListeners(discordUpListener, discordDownListener, reactionListener, messageListener);
		builder.setActivity(Activity.watching("the Roblox Discord"));
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.VOICE_STATE,
				CacheFlag.MEMBER_OVERRIDES);
		builder.setChunkingFilter(ChunkingFilter.NONE);
		builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
		builder.setLargeThreshold(50);
		try {
			final JDA jda = builder.build();
			jda.awaitReady();
			return jda;
		} catch (final LoginException e) {
			TelegramService.sendToTelegram(Instant.now(), TelegramService.ERROR_UNKNOWN);
		} catch (final InterruptedException e) {
			TelegramService.sendToTelegram(Instant.now(), TelegramService.ERROR_WAIT_JDA);
		}
		return null;
	}

	@Bean
	TelegramService telegramService(@Value("${telegram.token}") final String telegramToken,
			@Value("${developer.update-notes}") final String devNote) {
		return new TelegramService(telegramToken, devNote);
	}

	/**
	 * Mongo template.
	 *
	 * @return the mongo template
	 * @throws UnknownHostException the unknown host exception
	 */
	@Bean
	MongoTemplate mongoTemplate() throws UnknownHostException {
		return new MongoTemplate(MongoClients.create("mongodb://localhost:27017"), "rdss");
	}

	@PreDestroy
	public void onExit() {
		// TelegramService.sendToTelegram(Instant.now(), TelegramService.CDS_END);
	}

}