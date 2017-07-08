package com.kaivan.hipchat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kaivanwadia
 */
@Path("/parsemessage")
public class HipChatService {

    private Logger logger = Logger.getLogger(HipChatService.class.getName());

    /**
     * REST GET API to return all the mentions, emoticons, and URLs in a given message.
     * @param message
     * @return
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public ParsedMessage parseMessage(@DefaultValue("") @QueryParam("message") String message) {
        List<String> emoticons = parseEmoticons(message);
        List<String> mentions = parseMentions(message);
        List<Link> links = parseUrls(message);
        ParsedMessage parsedMessage = new ParsedMessage(mentions, emoticons, links);
        return parsedMessage;
    }

    /**
     * Method to parse URLs from a given string.
     * Definition - A URL has to have a space before it or start on a new line.
     *              The URL can start with http:// or https://. The URL can also start
     *              with letters directly and omit the http|https.
     * @param message
     * @return - List of Links (URL and Title)
     */
    public List<Link> parseUrls(String message) {
        String[] words = message.split("\\s+");
        List<Link> links = new ArrayList<Link>();
        for (String urlString : words) {
            String title = "";
            try {
                URL url = new URL(urlString);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));

                String html = "";
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    html = html.concat(inputLine);
                }
                in.close();
                Document doc = Jsoup.parse(html);
                title = doc.title();
            } catch (MalformedURLException e) {
                continue;
            } catch (Exception e) {
                logger.log(Level.WARNING, String.format("Could not find title for URL [%s]", urlString));
            }
            Link link = new Link(urlString, title);
            links.add(link);
        }
        return links;
    }

    /**
     * Method to parse mentions from a given string.
     * Definition - Always starts with an '@' and ends when hitting a non-word character.
     *            Character before '@' should be a whitespace or start of a new line. Mention ends with a non-word character.
     * @param message
     * @return - List of mentions
     */
    public List<String> parseMentions(String message) {
        Matcher mentionsMatcher = Pattern.compile(Patterns.MENTION_PATTERN).matcher(message);
        List<String> mentions = new ArrayList<String>();
        while (mentionsMatcher.find()) {
            String mention = mentionsMatcher.group().replaceAll("\\W+","");
            mentions.add(mention);
        }
        return mentions;
    }

    /**
     * Method to parse emoticons from a given string
     * Definition - Alphanumeric strings of length 1 to 15 surrounded by parenthesis (). Emoticons do not need to have white space around them.
     *
     * @param message
     * @return - List of emoticons
     */
    public List<String> parseEmoticons(String message) {
        Matcher emoticonMatcher = Pattern.compile(Patterns.EMOTICON_PATTERN).matcher(message);
        List<String> emoticons = new ArrayList<String>();
        while (emoticonMatcher.find()) {
            String emoticon = emoticonMatcher.group().replaceAll("\\W+","");
            emoticons.add(emoticon);
        }
        return emoticons;
    }

    // TODO : Write up Readme with what all you would do. Integration tests etc
    // TODO : Write up Readme with how to run the project
}

