/**
 * Created by Gavin on 04/02/2017.
 */

import twitter4j.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TwitterInterface {


    Twitter twitter = TwitterFactory.getSingleton();

    TwitterParser tweet = new TwitterParser();
    ScrabbleSolver sSolver = new ScrabbleSolver();

    public List<String> dicList = new ArrayList<>();

    public List<String> answersArray = new ArrayList<>();


    //if something goes wrong, we might see a TwitterException
    //TO DO :: Ensure that the classes throw the twitter exception and make sure to catch them in the main block or something
    public static void main(String... args) {

        TwitterInterface twitterInterface = new TwitterInterface();
        //This may need to be moved to dedicated class
        System.out.println("Importing Word list");
        twitterInterface.parseDictionary();
        System.out.println("Word list complete");

       // twitterInterface.testSolver();

        twitterInterface.StartStream();
        twitterInterface.StartListeningMentions();


    }

    public void parseDictionary(){

        java.util.Scanner dicFile = null;

        String newWord;

        try {
            dicFile = new Scanner(new File("sowpods.txt"));

            while (dicFile.hasNext()) {
                newWord = dicFile.nextLine().toLowerCase();
                dicList.add(newWord);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File was not found. Make sure the file exist.");
            System.err.println("Message: " + e.getMessage());
            System.exit(-1);

        } finally {
            if (dicFile != null) {
                dicFile.close();
            }else{
                System.exit(-1);
            }
        }
    }

    //Test function in place to bypass twitter setup for now
    public void testSolver(){
        answersArray = sSolver.solveTweet("test", dicList );
        for(int i = 0; i < answersArray.size(); i++){
            System.out.println("Result " + i + " : " + answersArray.get(i));
        }
        TweetResults(answersArray);

        answersArray.clear();

        answersArray = sSolver.solveTweet("thisa", dicList );
        for(int i = 0; i < answersArray.size(); i++){
            System.out.println("Result " + i + " : " + answersArray.get(i));
        }
        TweetResults(answersArray);

    }

    public void StartStream() {
        //access the twitter API using your twitter4j.properties file
        UpdateStatus("I am now online to solve all your scrabble problems");
    }

    public void StartListeningMentions(){
        StatusListener listener = new StatusListener(){
            public void onStatus(Status status) {
                System.out.println(status.getUser().getName() + " : " + status.getText());

               if(!tweet.parseTweet(status.getUser().getName(),status.getText())){
                   System.out.println("Hit an error when parsing > 5 characters");
                   ReplyToTweet(status , "Error when parsing scrabble characters. Sorry :(");
               }else{
                   System.out.println("Tweet Passed up stack");
                   answersArray = sSolver.solveTweet(tweet.tweetContent, dicList );
                   List<String> tweetReply = new ArrayList<>();
                   tweetReply = TweetResults(answersArray);
                   for (String tweets: tweetReply) {
                       ReplyToTweet(status , tweets);
                   }

                   answersArray.clear();
               }
                System.out.println("We have stopped searching");

            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

            @Override
            public void onScrubGeo(long l, long l1) {

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        twitterStream.user();

        FilterQuery query = new FilterQuery();
        query.track("@UnscrambleBot");
        twitterStream.filter(query);
    }

    public void ExitStream(){
        System.out.println(" We are sending the offline tweet");
        UpdateStatus("I am now offline, have a nice day");
    }

    public void UpdateStatus(String update){
        try {
            System.out.println(" We are sending the online tweet");
            Status sendStatus = twitter.updateStatus(" " + update);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
        }
    }

    public void ReplyToTweet(Status status, String reply ){
        StatusUpdate statusUpdate = new StatusUpdate(".@" + status.getUser().getScreenName() + " " + reply);
        statusUpdate.inReplyToStatusId(status.getId());
        try {
            Status sendStatus = twitter.updateStatus(statusUpdate);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public List<String> TweetResults(List<String> array){
        String tweet = "";
        int tweetLength = 0;
        List<String> tweets = new ArrayList<>();

        for(int i = 0; i < array.size(); i++){
            if(array.get(i).length() + tweetLength >= 100 || i == array.size() - 1 ){
                tweet += (array.get(i));
                System.out.println("Test Output: " + tweet);
                tweets.add(tweet);
                tweet = "";
                tweetLength = 0;
            }else{
                tweetLength += (array.get(i).length() + 1) ;
                tweet += (array.get(i) + ",");
            }
        }

        return tweets;
    }


}
