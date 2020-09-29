/*
 * Author: {Ruben Veiga}
 * Contributor: {Liscuate}
 */

package com.misterveiga.rdss.listeners;

import java.time.Instant;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.misterveiga.rdss.utils.Properties;
import com.misterveiga.rdss.utils.RoleUtils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * The listener interface for receiving reaction events. The class that is
 * interested in processing a reaction event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addReactionListener<code> method. When the reaction event
 * occurs, that object's appropriate method is invoked.
 *
 * @see ReactionEvent
 */
@Component
@PropertySource("classpath:application.properties")
public class ReactionListener extends ListenerAdapter {

	@Value("${pastebin.apikey}")
	String pastebinApiKey;

	@Autowired
	PastebinFactory pastebinFactory;

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(ReactionListener.class);

	/** The Constant ID_REACTION_MUTE. */
	private static final String ID_REACTION_MUTE = "760204798984454175";
	private static final String ID_REACTION_MUTE2 = "452813334429827072";

	/** The Constant ID_REACTION_MUTE_WITH_HISTORY. */
	private static final String ID_REACTION_MUTE_WITH_HISTORY = "";

	/** The Constant ID_REACTION_BAN. */
	private static final String ID_REACTION_BAN = "";

	/** The Constant ID_REACTION_CLEAR_MESSAGES. */
	private static final String ID_REACTION_CLEAR_MESSAGES = "756816315624325151";

	/** The Constant COMMAND_MUTE_USER_DEFAULT. */
	private static final String COMMAND_MUTE_USER_DEFAULT = ";mute %s %s %s";

	/** The Constant COMMAND_CLEAN_MESSAGES_USER. */
	private static final String COMMAND_CLEAN_MESSAGES_USER = ";clean user %s";

	/** The Constant COMMAND_REASON. */
	private static final String COMMAND_REASON = "(By %s) Message Evidence: %s";

	/**
	 * On message reaction add.
	 *
	 * @param event the event
	 */
	@Override
	public void onMessageReactionAdd(final MessageReactionAddEvent event) {
		final TextChannel commandChannel = event.getGuild().getTextChannelById(Properties.CHANNEL_COMMANDS_ID);

		final MessageReaction reaction = event.getReaction();
		final Member reactee = event.getMember();
		final Long messageId = event.getMessageIdLong();
		final MessageChannel channel = event.getTextChannel();

		final ReactionEmote emote = reaction.getReactionEmote();
		final Message message = event.getChannel().retrieveMessageById(messageId).complete(); // (reaction.getMessageId()).complete();
		final Member messageAuthor = message.getMember();

		if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_SERVER_MANAGER) != null
				|| RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_COMMUNITY_SUPERVISOR) != null) {

			switch (emote.getId()) {
			case ID_REACTION_MUTE: muteUser(reactee, messageAuthor, "30m", message, commandChannel);
			case ID_REACTION_MUTE2: muteUser(reactee, messageAuthor, "1h", message, commandChannel);

				log.info("[Reaction Command] Quick-Mute executed by {} on {} for Message \"{}\"", reactee,
					messageAuthor, message);
					clearMessages(messageAuthor, channel);
				break;


			case ID_REACTION_CLEAR_MESSAGES:
				// clearMessages(messageAuthor, channel);
				break;
			}
		}

	}

	/**
	 * Mute user.
	 *
	 * @param reactee        the reactee
	 * @param messageAuthor  the message author
	 * @param message        the message
	 * @param commandChannel the command channel
	 */
	private void muteUser(final Member reactee, final Member messageAuthor, final Message message,
			final TextChannel commandChannel) {
		System.out.println(messageAuthor.getId() == null ? "getId null"
				: "" + reactee.getEffectiveName() == null ? "getEffectiveName null"
						: "" + message.getContentStripped() == null ? "getContentStripped null" : "");

		final String messageContent = message.getContentStripped();

		if (messageContent.replace("\n", " ").length() < 120) {
			commandChannel
				.sendMessage(String.format(COMMAND_MUTE_USER_DEFAULT, messageAuthor.getId(),
						String.format(COMMAND_REASON, reactee.getEffectiveName(),
								messageContent.replace("\n", " "))))
				.allowedMentions(new ArrayList<MentionType>()).queue();
			}

		} else {
			final Pastebin pastebin = pastebinFactory.createPastebin(pastebinApiKey);
			final String pasteTitle = new StringBuilder().append("Evidence against ")
					.append(messageAuthor.getEffectiveName()).append(" (").append(messageAuthor.getId()).append(")")
					.append(" on ").append(Instant.now()).toString();
			final Paste paste = pastebinFactory.createPaste().setTitle(pasteTitle).setRaw(messageContent)
					.setMachineFriendlyLanguage("text").setExpire(PasteExpire.Never).setVisiblity(PasteVisiblity.Public)
					.build();
			final String pasteKey = pastebin.post(paste).get();

			log.info(String.format("Pastebin \"%s\" posted to %s", pasteTitle, pasteKey));

			commandChannel
					.sendMessage(String.format(COMMAND_MUTE_USER_DEFAULT, messageAuthor.getId(),
							String.format(COMMAND_REASON, reactee.getEffectiveName(),
									messageContent.replace("\n", " ").substring(0, 17) + "... Pastebin: " + pasteKey)))
					.allowedMentions(new ArrayList<MentionType>())
//					.addFile(message.getContentStripped().getBytes(),
//							String.format("Message Evidence from %s against %s.txt", reactee.getEffectiveName(),
//									messageAuthor.getId()))
					.queue();
		}

		switch (emote.getId()) {
			case ID_REACTION_MUTE:
			case ID_REACTION_MUTE2:

			log.info(String.format(COMMAND_MUTE_USER_DEFAULT, messageAuthor.getId(),
						String.format(COMMAND_REASON, reactee.getEffectiveName(), message.getContentStripped())));
				}
		}

	/**
	 * Clear messages.
	 *
	 * @param messageAuthor the message author
	 * @param channel       the channel
	 */
	private void clearMessages(final Member messageAuthor, final MessageChannel channel) {
		channel.sendMessage(String.format(COMMAND_CLEAN_MESSAGES_USER, messageAuthor.getId()))
				.queue(message -> message.delete().queue());
		log.info(String.format(COMMAND_CLEAN_MESSAGES_USER, messageAuthor.getId()));
	}
}