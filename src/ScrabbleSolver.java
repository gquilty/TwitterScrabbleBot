import com.sun.xml.internal.fastinfoset.util.CharArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin on 12/02/2017.
 */

public class ScrabbleSolver {

    public List<String> answers = new ArrayList<>();

    public List<String> solveTweet(String tweet , List<String> dicList){

        String currentWord;
        List<Character> tweetArrayList= new ArrayList<>();


        System.out.println("characters initialised... Starting search....");

        for(int i =0 ; i < dicList.size(); i++){
            currentWord = "";
            //Reset tweet array after removal
            tweetArrayList.clear();
            for(int charPos = 0; charPos < tweet.length(); charPos++){
                tweetArrayList.add(tweet.charAt(charPos));
            }
            char [] cArray = dicList.get(i).toCharArray();
            for(int j = 0; j < cArray.length; j++){
                for(int k = tweetArrayList.size()-1; k >= 0; k--){
                    if(cArray[j] == tweetArrayList.get(k)){
                        currentWord += tweetArrayList.get(k);
                        tweetArrayList.remove(k);
                        break;
                    }
                }
            }
            if(currentWord.equals(dicList.get(i))){
                answers.add(currentWord);
            }
        }
        return answers;
    }

}
