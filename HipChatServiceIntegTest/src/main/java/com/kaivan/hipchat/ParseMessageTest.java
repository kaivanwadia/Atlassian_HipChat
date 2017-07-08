package com.kaivan.hipchat;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kaivanwadia
 */
public class ParseMessageTest {

    private Logger logger = Logger.getLogger(HipChatService.class.getName());
    private List<String> emoticons;
    private List<String> mentions;
    private List<Link> links;

    @Before
    public void setup() {
        emoticons = new ArrayList<String>();
        emoticons.add("emoticon1");
        emoticons.add("emoticon2");
        mentions = new ArrayList<String>();
        mentions.add("kaivan");
        mentions.add("wadia");
        links = new ArrayList<Link>();
        links.add(new Link("https://www.hipchat.com/emoticons","Emoticons & Custom Emoticons | HipChat"));
        links.add(new Link("https://www.google.com", "Google"));
    }

    private ParsedMessage getParsedMessage(String message) {
        try {
            HttpURLConnection conn;
            String urlString = "http://localhost:8080/newjersey/hipchat/parse/message/?message=" + URLEncoder.encode(message, "UTF-8");
            conn = (HttpURLConnection) (new URL(urlString)).openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Gson gson = new Gson();
            return gson.fromJson(response.toString(), ParsedMessage.class);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Exception while running test.", e);
            return null;
        }
    }

    @Test
    public void testParseMessage() {
        String message = "@kaivan @wadia Check out this (emoticon1) and (emoticon2) .. You can create similar ones according to https://www.hipchat.com/emoticons " +
                "Check out another link here https://www.google.com";
        ParsedMessage parsedMessage = getParsedMessage(message);
        assertEquals(emoticons, parsedMessage.getEmoticons());
        assertEquals(mentions, parsedMessage.getMentions());
        assertEquals(links, parsedMessage.getLinks());
    }

    @Test
    public void testParseMessage_NoEmoticons() {
        String message = "@kaivan @wadia Check out this non-emoticon .. You can create similar ones according to https://www.hipchat.com/emoticons " +
                "Check out another link here https://www.google.com";
        ParsedMessage parsedMessage = getParsedMessage(message);
        assertTrue(parsedMessage.getEmoticons().isEmpty());
        assertEquals(mentions, parsedMessage.getMentions());
        assertEquals(links, parsedMessage.getLinks());
    }

    @Test
    public void testParseMessage_NoMentions() {
        String message = "Check out this (emoticon1) and (emoticon2) .. You can create similar ones according to https://www.hipchat.com/emoticons " +
                "Check out another link here https://www.google.com";
        ParsedMessage parsedMessage = getParsedMessage(message);
        assertEquals(emoticons, parsedMessage.getEmoticons());
        assertTrue(parsedMessage.getMentions().isEmpty());
        assertEquals(links, parsedMessage.getLinks());
    }

    @Test
    public void testParseMessage_NoLinks() {
        String message = "@kaivan @wadia Check out this (emoticon1) and (emoticon2) .. You can create similar ones according to";
        ParsedMessage parsedMessage = getParsedMessage(message);
        assertEquals(emoticons, parsedMessage.getEmoticons());
        assertEquals(mentions, parsedMessage.getMentions());
        assertTrue(parsedMessage.getLinks().isEmpty());
    }
}
