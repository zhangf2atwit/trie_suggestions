package edu.wit.cs.comp2350;

import java.util.ArrayList;

/**
 * Implements a trie data structure
 * 
 * Wentworth Institute of Technology COMP 2350 Assignment 6
 * 
 */

//TODO: document class
/* This is a Trie class inheritance another class Speller
 * In this Trie, there are two constructors for the Trie node
 * There are insert the word, contains and get suggestions methods to help the trie 
 * to insert, check the target word, and give the spelling suggestions, 
 * note:suggestion word has the same length as the target word.
 */
public class Trie implements Speller {
	public class TrieNode {
		public String prefix;              // index from 0-current
		public char c;
		public boolean isWord;             // if reached the last charrector, if word completed?
		public TrieNode[] children;        // next node
		
		public TrieNode(char c) {
			this.c = c;
			this.prefix = "";              // initial the first prefix, root's prefix is empty
			isWord = false;
			children = new TrieNode[26];
		}

		public TrieNode(char c, String prefix) {
			this.c = c;
			this.prefix = prefix;
			isWord = false;
			children = new TrieNode[26];  
		}
	}

	private TrieNode root;
	
	public Trie() {
		root = new TrieNode('\0');          // nil
	}

	// TODO: document this method
	/* Insert word adds the word to the trie node
	 * Find the length of the word that needs to insert, go through with each letter
	 * If the method finds the last element of the string, the sign isWord to true.
	 */
	@Override
	public void insertWord(String s) {
		// TODO Implement this method
		TrieNode current = root;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i); 

			if (current.children[c - 'a'] == null) {
				current.children[c - 'a'] = new TrieNode(c, current.prefix + c); // next children = parent + curr
			}

			if (i == s.length() - 1) {                                           // i == last index of the word (s) 
				current.children[c - 'a'].isWord = true;
			}
			current = current.children[c - 'a'];
		}
	}

	private TrieNode getNode(String word) {
		TrieNode current = root;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (current.children[c - 'a'] == null) {
				return null;
			}
			current = current.children[c - 'a'];
		}
		return current;
	}

	// TODO: document this method
	/* Cotains method to check the node contains the word we need
	 * If the current node doesn't have the next node, return false
	 * After it reached to the last element for the word, return true(contain)
	 */
	@Override
	public boolean contains(String s) {
		// TODO Implement this method
		boolean contains = false;
		TrieNode current = root;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (current.children[c - 'a'] == null) {
				return false;
			}

			current = current.children[c - 'a'];

			if (i == s.length() - 1) {
				if (current.isWord) {
					contains = true;
				}
			}
		}
		return contains;
	}

	// TODO: document this method
	/* Get Suggestions is to add spelling suggestion words to the ArrayList 
	 * Firstly, find words that can replace the target word
	 * second, check the trie, if the suggested word is not in the trie, add it to the array for the result.
	 */
	@Override
	public String[] getSuggestions(String s) {
		// TODO Implement this method
		ArrayList<String> suggestions = new ArrayList<String>();
		int distanceLimit = 2;
		findWords(this.root, s, 0, 0, distanceLimit, suggestions);
		return suggestions.toArray(new String[0]);
	}
	// Find the words and also check if them in the arraylist
	// passing the pointer(to current trie node, s is word need help, cur index is the s[i], path cost, rquired 2 fix in this case, return res[]
	private void findWords(TrieNode ptr, String s, int curIndex, int distance, int distanceLimit, ArrayList<String> suggestions) {	
		if (ptr == null) {                              // null case
			return;
		}
		if (curIndex == s.length()) {                   // if the index reach the end of the word (s)
			if (ptr.isWord) {                           // trie node is completed?
				if (!suggestions.contains(ptr.prefix)) {// if the arraylist doesn't contain the prefix,												
					suggestions.add(ptr.prefix);        // add the suggestion to the res array
				}
			}
			return;
		}
		for (int i = 0; i < 26; i++) {
			if (s.charAt(curIndex) == 'a' + i) {        // a,b,c,d....
				// point to the children node, unchanged s, index moved one more position to right, unchanged distance, unchange limint, return res
				findWords(ptr.children[i], s, curIndex + 1, distance, distanceLimit, suggestions);

			} else {                                    // s.charAt(curIndex) != 'a' + i
				if (distance + 1 <= distanceLimit) {    // if the fixdistance > the distance limit(2), do nothing
					                                    // if distance <= limit,  do one more fix
					findWords(ptr.children[i], s, curIndex + 1, distance + 1, distanceLimit, suggestions);
				}
			}
		}
	}

}
