package com.misterveiga.cds.listeners;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class UpdateRoleListener extends ListenerAdapter {
	
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
	{
		List<Role> roles = event.getRoles();
		for(Role item : roles) {
			if(Long.parseLong(item.getId()) == 864250017962131467L) {
		
				EmbedBuilder embed = new EmbedBuilder();
				
				embed.setTitle("You have been muted. This means your chat permissions have been temporarily restricted.");
				embed.setThumbnail(event.getGuild().getIconUrl());
				embed.setDescription("Please use this time to read <#801557724503736335>. Once your mute expires, you will regain access to the chat and voice channels.\r\n"
						+ "\r\n"
						+ "This mute does not affect your account on Roblox.com.\r\n\n");
			
				embed.addField( "<:ejheknh:864251674206011453> **Mute Reason**\r\n",
						 "If you are unsure why you have been muted, or you want to know how long you are muted for then please DM a @Moderator.\r\n", false);
				embed.addField( "<:z_qm60:452813334429827072> **Mute Bypassing**\r\n",
						 "Attempting to bypass a mute will result in you being banned from the server. :ban_hammer:", false);
				
				LocalDate currentdate = LocalDate.now();
				embed.setFooter("Roblox Unofficial Discord • " + currentdate.getMonthValue() + "/" + currentdate.getDayOfMonth() +"/" + currentdate.getYear() , event.getGuild().getIconUrl());
				
				event.getUser().openPrivateChannel().flatMap(channel -> 
				channel.sendMessage(embed.build()))
		        .queue();
			} 
		}
	}
}
