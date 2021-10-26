/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.misterveiga.cds.utils.Properties;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;

import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.misterveiga.cds.listeners.DiscordDownListener;
import com.misterveiga.cds.listeners.DiscordUpListener;
import com.misterveiga.cds.listeners.MessageListener;
import com.misterveiga.cds.listeners.ReactionListener;

import net.dv8tion.jda.api.JDA;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.*;
import javax.servlet.http.Cookie;
import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class Controller.
 */
@RestController
@RequestMapping("rdss")
public class Controller {

	/** The jda. */
	@Autowired
	JDA jda;

	/** The discord up listener. */
	@Autowired
	DiscordUpListener discordUpListener;

	/** The discord down listener. */
	@Autowired
	DiscordDownListener discordDownListener;

	/** The message listener. */
	@Autowired
	MessageListener messageListener;

	/** The reaction listener. */
	@Autowired
	ReactionListener reactionListener;

	/**
	 * Ok.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody String ok() {
		return "OK";
	}

	/**
	 * Disable discord alerts.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/disable/discord-uptime-alerts", method = RequestMethod.GET)
	public @ResponseBody String disableDiscordAlerts() {
		if (jda.getRegisteredListeners().contains(discordUpListener)
				&& jda.getRegisteredListeners().contains(discordDownListener)) {
			jda.removeEventListener(discordUpListener, discordDownListener);
			return "Discord uptime/downtime alerts have been disabled.";
		}
		return "Discord uptime/downtime alerts are already disabled.";
	}

	/**
	 * Enable discord alerts.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/enable/discord-uptime-alerts", method = RequestMethod.GET)
	public @ResponseBody String enableDiscordAlerts() {
		if (!jda.getRegisteredListeners().contains(discordUpListener)
				&& !jda.getRegisteredListeners().contains(discordDownListener)) {
			jda.addEventListener(discordUpListener, discordDownListener);
			return "Discord uptime/downtime alerts have been enabled.";
		}
		return "Discord uptime/downtime alerts are already enabled.";
	}

	/**
	 * Enable message listener.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/enable/message-listener", method = RequestMethod.GET)
	public @ResponseBody String enableMessageListener() {
		if (!jda.getRegisteredListeners().contains(messageListener)) {
			jda.addEventListener(messageListener);
			return "Message Listener has been enabled.";
		}
		return "Message Listener is already enabled.";
	}

	/**
	 * Disable message listener.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/disable/message-listener", method = RequestMethod.GET)
	public @ResponseBody String disableMessageListener() {
		if (jda.getRegisteredListeners().contains(messageListener)) {
			jda.removeEventListener(messageListener);
			return "Message Listener has been disabled.";
		}
		return "Message Listener is already disabled.";
	}

	/**
	 * Enable reaction listener.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/enable/reaction-listener", method = RequestMethod.GET)
	public @ResponseBody String enableReactionListener() {
		if (!jda.getRegisteredListeners().contains(reactionListener)) {
			jda.addEventListener(reactionListener);
			return "Reaction Listener has been enabled.";
		}
		return "Reaction Listener is already enabled.";
	}

	/**
	 * Disable reaction listener.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/disable/reaction-listener", method = RequestMethod.GET)
	public @ResponseBody String disableReactionListener() {
		if (jda.getRegisteredListeners().contains(reactionListener)) {
			jda.removeEventListener(reactionListener);
			return "Reaction Listener has been disabled.";
		}
		return "Reaction Listener is already disabled.";
	}

	/**
	 * Disable reaction listener.
	 *
	 * @return
	 */
	@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
	@RequestMapping(value = "/checkAuthorization", method = RequestMethod.GET)
	public AuthorizedDiscordUserData checkIfAuthorized(@CookieValue(value = "Discord-Token", defaultValue = "false") String AuthorizedToken) {
		AuthorizedDiscordUserData test = FetchAuthorizedUserWithToken(AuthorizedToken);
		return test;
	}

	public static class DiscordUser{
		@JsonProperty("banner_color")
		private String banner_color;

		@JsonProperty("accent_color")
		private String accent_color;

		@JsonProperty("banner")
		private String banner;

		@JsonProperty("id")
		private String id;

		@JsonProperty("avatar")
		private String avatar;

		@JsonProperty("public_flags")
		private String public_flags;

		@JsonProperty("username")
		private String username;

		@JsonProperty("discriminator")
		private String discriminator;

		public DiscordUser(){}

		public String getBanner_color()
		{
			return banner_color;
		}

		public void setBanner_color(String banner_color)
		{
			this.banner_color = banner_color;
		}

		public String getAccent_color()
		{
			return accent_color;
		}

		public void setAccent_color(String accent_color)
		{
			this.accent_color = accent_color;
		}

		public String getBanner()
		{
			return banner;
		}

		public void setBanner(String banner)
		{
			this.banner = banner;
		}

		public String getId()
		{
			return id;
		}

		public void setId(String id)
		{
			this.id = id;
		}


		public String getAvatar()
		{
			return avatar;
		}

		public void setAvatar(String avatar)
		{
			this.avatar = avatar;
		}

		public String getPublic_flags()
		{
			return public_flags;
		}

		public void setPublic_flags(String public_flags)
		{
			this.public_flags = public_flags;
		}


		public String getUsername()
		{
			return username;
		}

		public void setUsername(String username)
		{
			this.username = username;
		}


		public String getDiscriminator()
		{
			return discriminator;
		}

		public void setDiscriminator(String discriminator)
		{
			this.discriminator = discriminator;
		}
	}


	class MyResponse {
		public MyResponse(){}
		private boolean banned;
		private String joined_at;
		private int mutes;
		private String userId;
		private String nickName;
		private int warnings;
		private DiscordUser userApiData;
		private List<MemberRoles> MemberRoles;

		public boolean getBanned()
		{
			return banned;
		}

		public void setBanned(boolean banned)
		{
			this.banned = banned;
		}

		public String getJoined_at()
		{
			return joined_at;
		}

		public void setJoined_at(String joined_at)
		{
			this.joined_at = joined_at;
		}

		public int getMutes()
		{
			return mutes;
		}

		public void setMutes(int mutes)
		{
			this.mutes = mutes;
		}

		public String getUserId()
		{
			return userId;
		}

		public void setUserId(String userId)
		{
			this.userId = userId;
		}


		public int getWarnings()
		{
			return warnings;
		}

		public void setWarnings(int warnings)
		{
			this.warnings = warnings;
		}

		public DiscordUser getUserApiData()
		{
			return userApiData;
		}

		public void setUserApiData(DiscordUser userApiData)
		{
			this.userApiData = userApiData;
		}

		public List<MemberRoles> getUserRoles()
		{
			return MemberRoles;
		}

		public void setUserRoles(List<Role> userRoles)
		{
			this.MemberRoles = MemberRoles;
		}

		public String getNickName(){
			return nickName;
		}

		public void setNickName(String nickName){
			this.nickName = nickName;
		}
	}

	class MemberRoles{
		String name;
		int red;
		int green;
		int blue;

		public String getName(){
			return name;
		}

		public void setName(){
			this.name = name;
		}

		public int getRed(){
			return red;
		}

		public void setRed(){
			this.red = red;
		}
		public int getGreen(){
			return green;
		}

		public void setGreen(){
			this.green = green;
		}
		public int getBlue(){
			return blue;
		}

		public void setBlue(){
			this.blue = blue;
		}
	}


	@RequestMapping(value = "/get-member-data/{userId}", method = RequestMethod.GET)
	@CrossOrigin
	@ResponseBody
	public MyResponse getUserData(@PathVariable String userId, HttpSession session) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		DiscordUser discordUserFromDiscordAPI = null;

		try {
			URL url = new URL("https://discord.com/api/v9/users/" + userId);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Authorization", "Bot ODY0MTkxMzIzNzcwMTkxOTIz.YOx24w.K5O0QPsSNg6Ispn78XYEe5y7emw");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("GET");

			String line = "";

			int status = con.getResponseCode();

			InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder response = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
			}
			bufferedReader.close();

			discordUserFromDiscordAPI = mapper.readValue(response.toString(),DiscordUser.class);

		} catch (Exception e) {
			e.printStackTrace();

			System.out.println("someting went wrong");
		}

		final Guild guild = jda.getGuildById(Properties.GUILD_ROBLOX_DISCORD_ID);

		RestAction<Member> memberData = guild.retrieveMemberById(userId);
		Member member = memberData.complete();

		List<Role> roles = member.getRoles();
		List<MemberRoles> memberRoles = new ArrayList<>();

		for(Role item : roles) {
        MemberRoles memberRole = new MemberRoles();
        memberRole.name = item.getName();
        try {
			memberRole.red = item.getColor().getRed();
			memberRole.green = item.getColor().getGreen();
			memberRole.blue = item.getColor().getBlue();
		} catch (Exception e) {
			memberRole.red = 0;
			memberRole.green = 0;
			memberRole.blue = 0;
		}
        memberRoles.add(memberRole);
		}

		MyResponse response = new MyResponse();
		response.banned = true;
		response.warnings = 7;
		response.joined_at = member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-yy hh:mm a")).toString();
		response.mutes = 5;
		response.userId = userId;
		response.nickName = member.getNickname();
		response.userApiData = discordUserFromDiscordAPI;
		response.MemberRoles = memberRoles;

		return response;
	}


	@ResponseBody
	@GetMapping("/error")
	public String error(HttpSession session) {
		return "There was an error.";
	}

	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		return Collections.singletonMap("name", principal.getAttribute("name"));
	}



	static class AuthResponse{
		@JsonProperty("access_token")
		private String access_token;

		@JsonProperty("expires_in")
		private long expires_in;

		@JsonProperty("refresh_token")
		private String refresh_token;

		@JsonProperty("scope")
		private String scope;

		@JsonProperty("token_type")
		private String token_type;

		public AuthResponse(){}


		public String getAccess_token()
		{
			return access_token;
		}

		public void setAccess_token(String access_token)
		{
			this.access_token = access_token;
		}

		public long getExpires_in()
		{
			return expires_in;
		}

		public void setExpires_in(long expires_in)
		{
			this.expires_in = expires_in;
		}

		public String getRefresh_token()
		{
			return refresh_token;
		}

		public void setRefresh_token(String banner)
		{
			this.refresh_token = refresh_token;
		}

		public String getScope()
		{
			return scope;
		}

		public void setScope(String scope)
		{
			this.scope = scope;
		}


		public String getToken_type()
		{
			return token_type;
		}

		public void setToken_type(String token_type)
		{
			this.token_type = token_type;
		}

	}


	@RequestMapping(value = "/login/oauth2/code/discord", method = RequestMethod.GET)
	@CrossOrigin
	@ResponseBody
	public ModelAndView getString(@RequestParam("code") String code, HttpServletResponse res, HttpSession session) throws Exception {

		OkHttpClient client = new OkHttpClient();
		RequestBody formBody = new FormBody.Builder()
				.add("client_id","862352717527646228")
				.add("client_secret","mPywUCkzazVE16E9IeYCQ8ffNjFraUKl")
				.add("grant_type","authorization_code")
				.add("code", code)
				.add("redirect_uri","http://localhost:8080/cds/rdss/login/oauth2/code/discord")
				.build();

		Request request = new Request.Builder()
				.url("https://discord.com/api/oauth2/token")
				.addHeader("Content-Type", "application/json")
				.post(formBody)
        .build();

		ObjectMapper mapper = new ObjectMapper();
		AuthResponse authResponse = null;

		try (Response response = client.newCall(request).execute()) {
			authResponse = mapper.readValue(response.body().string(),AuthResponse.class);
		} catch (Exception e){
			System.out.println("Something went wrong: "+ e);
		}

		AuthorizedDiscordUserData AuthUser = FetchAuthorizedUserWithToken(authResponse.access_token);
		if(AuthUser.id != null){
			Cookie cookie = new Cookie("Discord-Token", authResponse.access_token);

			int cookieAgeInSeconds = 86400;
			cookie.setMaxAge(cookieAgeInSeconds);
			cookie.setSecure(true);
			cookie.setHttpOnly(true);
			cookie.setPath("/cds");
			res.addCookie(cookie);

			session.setAttribute("userId", AuthUser.id);
		}

		return new ModelAndView("redirect:" + "http://localhost:3000/");
	}


	static class AuthorizedDiscordUserData{
		@JsonProperty("id")
		private String id;
		@JsonProperty("username")
		private String username;
		@JsonProperty("avatar")
		private String avatar;
		@JsonProperty("discriminator")
		private String discriminator;
		@JsonProperty("public_flags")
		private String public_flags;

		public AuthorizedDiscordUserData(){}

		public String getId()
		{
			return id;
		}
		public void setId(String id)
		{
			this.id = id;
		}
		public String getUsername()
		{
			return username;
		}
		public void setUsername(String username)
		{
			this.username = username;
		}
		public String getAvatar()
		{
			return avatar;
		}
		public void setAvatar(String avatar)
		{
			this.avatar = avatar;
		}
		public String getDiscriminator()
		{
			return discriminator;
		}
		public void setDiscriminator(String discriminator)
		{
			this.discriminator = discriminator;
		}
		public String getPublic_flags()
		{
			return public_flags;
		}
		public void setPublic_flags(String public_flags)
		{
			this.public_flags = public_flags;
		}

	}

	@CrossOrigin
	@GetMapping("/getServerConfigurations")
	public PanelServerConfigurations getServerConfigurations() {



		final Guild guild = jda.getGuildById(Properties.GUILD_ROBLOX_DISCORD_ID);

		List<net.dv8tion.jda.api.entities.GuildChannel> guildChannels = guild.getChannels();
		List<GuildChannel> channels = new ArrayList<>();
		for(net.dv8tion.jda.api.entities.GuildChannel channel : guildChannels) {
			if (channel.getType().toString().equals("TEXT")){
				GuildChannel currentChannel = new GuildChannel();
				currentChannel.Name = channel.getName();
				currentChannel.Id = channel.getId();
				channels.add(currentChannel);
			}
		}

		PanelServerConfigurations response = new PanelServerConfigurations();
		response.guildChannels = channels;

		return response;
	}

	static class GuildChannel{
		private String Name;
		private String Id;

		public GuildChannel(){}

		public String getName(){
			return Name;
		}

		public void setName(String Name){
			this.Name = Name;
		}

		public String getId(){
			return Id;
		}

		public void setId(String Id){
			this.Id = Id;
		}
	}

	static class PanelServerConfigurations{
		private List<GuildChannel> guildChannels;
		private Properties properties = new Properties();


		public PanelServerConfigurations(){}

		public List<GuildChannel> getGuildChannels(){ return guildChannels; }
		public Properties getProperties(){ return properties; }
	}

	private AuthorizedDiscordUserData FetchAuthorizedUserWithToken(@CookieValue(value = "Discord-Token", defaultValue = "null") String token) {
		OkHttpClient client = new OkHttpClient();
		AuthorizedDiscordUserData AuthorizedDiscordUserData = null;
		Request request = new Request.Builder()
				.url("https://discord.com/api/oauth2/@me")
				.addHeader("Authorization", "Bearer " + token)
				.build();

		try (Response response = client.newCall(request).execute()) {
			ObjectNode node = new ObjectMapper().readValue(response.body().string(), ObjectNode.class);
			if (node.has("user")) {
				AuthorizedDiscordUserData = new ObjectMapper().readValue(node.get("user").toPrettyString(), AuthorizedDiscordUserData.class);
			}
		}catch (Exception e){
			AuthorizedDiscordUserData = new AuthorizedDiscordUserData();
			AuthorizedDiscordUserData.id = null;
		}


		return AuthorizedDiscordUserData;
	}

	private void CheckPermission(String userId, String[] strings){

	}
}
