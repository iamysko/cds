package com.misterveiga.cds.listeners;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.misterveiga.cds.utils.RoleUtils;

import net.dv8tion.jda.api.EmbedBuilder;
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
					channel.sendMessage(new EmbedBuilder()
					.setTitle("You have been muted. This means your chat permissions have been temporarily restricted.")
					.setThumbnail(event.getGuild().getIconUrl())
					.setDescription("Please use this time to read <#801557724503736335>. Once your mute expires, you will regain access to the chat and voice channels.\r\n"
							+ "\r\n"
							+ "This mute does not affect your account on Roblox.com.\r\n\n")
					.addField( "<:info:452813376788234250> **Mute Reason**\r\n",
							 "If you are unsure why you have been muted, or you want to know how long you are muted for then please DM a Moderator.\r\n", false)
					.addField( "<:z_qm60:452813334429827072> **Mute Bypassing**\r\n",
							 "Attempting to bypass a mute will result in you being banned from the server. <:ban_hammer:234839744092176384>", false)
					.setFooter("Roblox Unofficial Discord • " + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getDayOfMonth() +"/" + LocalDate.now().getYear() , event.getGuild().getIconUrl())
					.build()).queue();
				);
			} 
		}
	}
}
