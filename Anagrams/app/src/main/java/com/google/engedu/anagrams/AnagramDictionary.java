/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private ArrayList<String> wordList;

    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer,ArrayList<String>> sizeToWord;

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private int wordLength=DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        wordSet = new HashSet<>();
        wordList = new ArrayList<>();
        lettersToWord = new HashMap<>();
        sizeToWord = new HashMap<>();

        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordList.add(word);
            wordSet.add(word);

            String key = sortString(word);
            ArrayList<String> anagrams = new ArrayList<>();

            if ( !(lettersToWord.containsKey(key)) ) {
                anagrams.add(word);
            } else {
                anagrams = lettersToWord.get(key);
                anagrams.add(word);
            }
            lettersToWord.put(key, anagrams);

            ArrayList<String> sameSized = sizeToWord.get(word.length());
            if (sameSized == null) {
                sameSized = new ArrayList<>();
                sizeToWord.put(word.length(), sameSized);
            }
            sameSized.add(word);
        }
    }

    public String sortString(String s){
        char[] letters = s.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    public boolean isGoodWord(String word, String base) {
        return (wordSet.contains(word) && !word.contains(base));
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();

        for (char i = 'a'; i <= 'z'; i++) {
            String wordWithExtraLetter = word + i;
            String key = sortString(wordWithExtraLetter);
            if(lettersToWord.containsKey(key)) {
                ArrayList<String> anagrams = lettersToWord.get(key);
                //add the obtained set of anagrams for this key to the result set
                result.addAll(anagrams);

            }
        }

        Iterator<String> iter = result.iterator();

        while (iter.hasNext()) {
            String anagramword = iter.next();

            if (!isGoodWord(anagramword,word))
                iter.remove();
        }
        return result;
    }

    public List<String> getAnagramsWithTwoMoreLetters(String word) {
        ArrayList<String> result = new ArrayList<>();

        for (char i = 'a'; i <= 'z'; i++) {
            for(char j='a';j<='z';j++) {
                String wordWithExtraLetters = word + i + j;
                String key = sortString(wordWithExtraLetters);
                if (lettersToWord.containsKey(key)) {
                    ArrayList<String> anagrams = lettersToWord.get(key);
                    //add the obtained set of anagrams for this key to the result set
                    result.addAll(anagrams);

                }
            }
        }

        Iterator<String> iter = result.iterator();

        while (iter.hasNext()) {
            String anagramword = iter.next();

            if (!isGoodWord(anagramword,word))
                iter.remove();
        }
        Set<String> res=new HashSet<>();
        res.addAll(result);
        result.clear();
        result.addAll(res);

        return result;
    }

    public String pickGoodStarterWord() {
        int randomNumber;
        String starterWord;

        do {
            randomNumber = random.nextInt(sizeToWord.get(wordLength).size());
            starterWord = sizeToWord.get(wordLength).get(randomNumber);
        } while (getAnagramsWithOneMoreLetter(starterWord).size() < MIN_NUM_ANAGRAMS);

        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }

        return starterWord;

        // return "post";
    }
}
