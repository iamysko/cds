package com.misterveiga.cds.listeners;

import org.springframework.stereotype.Component;

import com.misterveiga.cds.utils.EmbedBuilds;
import com.misterveiga.cds.utils.RoleUtils;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class UpdateRoleListener extends ListenerAdapter {
	
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
	{
		for(Role item : event.getRoles()) {
			if(Long.parseLong(item.getId()) == RoleUtils.ROLE_MUTED) {
				event.getUser().openPrivateChannel().flatMap(channel ->
					channel.sendMessageEmbeds(EmbedBuilds.getMutedDMEmbed(event).build())).queue();
			} 
		}
	}
}
