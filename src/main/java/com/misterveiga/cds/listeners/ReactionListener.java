/*
 * Author: {Ruben Veiga}
 * Contributor: {Liscuate}
 */

package com.misterveiga.cds.listeners;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.thread.ReactionEventThread;

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

	/**
	 * On message reaction add.
	 *
	 * @param event the event
	 */
	@Override
	public void onMessageReactionAdd(final MessageReactionAddEvent event) {
		new Thread(new ReactionEventThread(event)).run();
	}

}