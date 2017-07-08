package com.kaivan.hipchat;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kaivanwadia
 */
public class HipChatServiceTest  {

    private HipChatService service;

    @Before
    public void setup() {
        service = new HipChatService();
    }

    @Test
    public void testParseMessage() {
        String message = "@kaivan @wadia Check out this (emoticon1) and (emoticon2) .. You can create simlar ones according to https://www.hipchat.com/emoticons \n" +
                "Check out another link here https://www.google.com";
        List<String> emoticons = new ArrayList<String>();
        emoticons.add("emoticon1");
        emoticons.add("emoticon2");
        List<String> mentions = new ArrayList<String>();
        mentions.add("kaivan");
        mentions.add("wadia");
        List<Link> links = new ArrayList<Link>();
        links.add(new Link("https://www.hipchat.com/emoticons","Emoticons & Custom Emoticons | HipChat"));
        links.add(new Link("https://www.google.com", "Google"));
        ParsedMessage parsedMessage = service.parseMessage(message);
        assertEquals(emoticons, parsedMessage.getEmoticons());
        assertEquals(mentions, parsedMessage.getMentions());
        assertEquals(links, parsedMessage.getLinks());
    }

    @Test
    public void testParseEmoticons() {
        String test = "(dasd)(sadsadS) (sadsadsad) asdasd(asdasDSAD)sadsad";
        List<String> emoticons = new ArrayList<String>();
        emoticons.add("dasd");
        emoticons.add("sadsadS");
        emoticons.add("sadsadsad");
        emoticons.add("asdasDSAD");
        List<String> actualEmoticons = service.parseEmoticons(test);
        assertEquals(emoticons, actualEmoticons);
    }

    @Test
    public void testParseEmoticons_NotEmoticons() {
        String test = "(dasd(sadsadS sadsadsad asdasd(a&@#@sdasDSAD)sadsad";
        List<String> actualEmoticons = service.parseEmoticons(test);
        assertTrue(actualEmoticons.isEmpty());
    }

    @Test
    public void testParseMentions() {
        String test = "@kaivan @wadia @temp23##";
        List<String> mentions = new ArrayList<String>();
        mentions.add("kaivan");
        mentions.add("wadia");
        mentions.add("temp23");
        List<String> actualMentions = service.parseMentions(test);
        assertEquals(mentions, actualMentions);
    }

    @Test
    public void testParseMentions_NotMentions() {
        String test = "incorrect@mention @*&*&(mention";
        List<String> actualMentions = service.parseMentions(test);
        assertTrue(actualMentions.isEmpty());
    }

    @Test
    public void testParseUrls() throws Exception {
        String urlString = "https://www.google.com/ https://www.google.de https://www.google.co.uk http://foo-asdasd.sadsadsa.com ";
        List<String> urls = Arrays.asList(urlString.split("\\s+"));
        List<Link> links = service.parseUrls(urlString);
        for (Link l : links) {
            System.out.println(l.getUrl());
        }
        for (int i = 0; i < urls.size(); i++) {
            assertEquals(links.get(i).getUrl(), urls.get(i));
        }
    }

    @Test
    public void testParseUrls_NotUrls() throws Exception {
        String urlString = "htdsatps://www.google.com/ fsadsa  ";
        List<String> urls = Arrays.asList(urlString.split("\\s+"));
        List<Link> links = service.parseUrls(urlString);
        assertTrue(links.isEmpty());
    }

}