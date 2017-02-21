/**
 * Created by Gavin on 12/02/2017.
 */
public class TwitterParser {

    // ScrabbleSolver scrabbleSolver = new ScrabbleSolver();

    public String tweetContent;

    public boolean parseTweet(String userName , String tweetContent){

        String tweetWords[] = tweetContent.split(" ");
        System.out.println("Initial Tweet: " + tweetContent);
        String parsedTweet = "";

        for(int i = 0; i < tweetWords.length; i++){
            if(tweetWords[i].charAt(0) != '@'){
                parsedTweet += tweetWords[i];
            }
        }

        System.out.println("Letters: " + parsedTweet);
        if(parsedTweet.length() > 5){
            //Return an error and send a tweet to try again
            System.out.println("Amount: " + parsedTweet.length());
            return false;
        }else{
            //solve tweet scrabble
            //somehow send results back

            this.tweetContent = parsedTweet;
            return true;
        }

    }
}
