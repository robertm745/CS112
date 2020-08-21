package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null, null, null);
		TrieNode node = new TrieNode(null, null, null);
		String word = "";
		if (allWords.length == 0) 
			return root;
		else {
			word = allWords[0];
			root.firstChild = new TrieNode(new Indexes(0, (short) 0, (short) (word.length()-1)), null, null);
			node = root.firstChild;
			}
		boolean added = false;
		int match = 0;
		short oldEndIndex = 0;
		TrieNode tempNode = null;
		TrieNode tempChild = null;
		TrieNode tempSibling = null;
		TrieNode lastNode = null;

		for (int i = 1; i < allWords.length; i++) {
			word = allWords[i];
			node = root.firstChild;
			lastNode = node;
			while (node != null) {
				if (allWords[node.substr.wordIndex].charAt(node.substr.startIndex) == word.charAt(node.substr.startIndex)) {
					match = checkWord(allWords[node.substr.wordIndex].substring(node.substr.startIndex, node.substr.endIndex+1), word.substring(node.substr.startIndex, word.length()));
					if (match == -1) 
						node = node.firstChild;
					else {
						oldEndIndex = node.substr.endIndex;
						node.substr.endIndex = (short) (match - 1 + node.substr.startIndex);
						tempChild = node.firstChild;
						tempSibling = new TrieNode(new Indexes(i, (short) (match + node.substr.startIndex), (short) (word.length()-1)), null, null);
						tempNode = new TrieNode(new Indexes(node.substr.wordIndex, (short) (match + node.substr.startIndex), oldEndIndex), tempChild, tempSibling);
						node.firstChild = tempNode;
						added = true;
						break;
					}
				}
				else {
					lastNode = node;
					node = node.sibling;
				}
			}
			if (!added) 
				lastNode.sibling = new TrieNode(new Indexes(i, (short) lastNode.substr.startIndex, (short) (word.length()-1)), null, null);
			added = false;
		}
			
		return root;
	}
	
	private static int checkWord(String key, String word) {
		int i = 0;
		while (key.charAt(i) == word.charAt(i)) {
				i++;
				if (i == key.length())
					return -1;
		}
		return i;
	}
	
	private static String checkFor(String prefix, String key) {
		int i = 0;
		if (prefix.charAt(0) == key.charAt(0)) {
			while (prefix.charAt(i) == key.charAt(i)) {
				i++;
				if (i == prefix.length())
					// if (i <= key.length()) 
						return "A";
				if (i == key.length()) 
						return i + "";
			}
		}
		return "";
	}
	
	private static ArrayList<TrieNode> makeList(ArrayList<TrieNode> wordsList, String[] allWords, TrieNode root) {
		if (root.firstChild == null)
			return wordsList;
		String tempStr = "";
		String temp = "";
		TrieNode node = root.firstChild;
		TrieNode lastNode = root;
		while(node.firstChild != null){
			lastNode = node;
			node = node.firstChild;
			// wordsList.add(node);
			/*
			tempStr += allWords[node.substr.wordIndex].substring(node.substr.startIndex, node.substr.endIndex+1);
			lastNode = node;
			node = node.firstChild;
			*/
		}
		while(node != null){
			wordsList.add(node);
			node = node.sibling;
			/*
			temp = tempStr;
			temp += allWords[node.substr.wordIndex].substring(node.substr.startIndex, node.substr.endIndex+1);
			wordsList.add(temp);
			node = node.sibling;
			*/
		}
		lastNode.firstChild = null;
		return makeList(wordsList, allWords, root);

	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		ArrayList<TrieNode> wordsList = new ArrayList<TrieNode>(1);
		if (root == null || root.firstChild == null)
			return wordsList;
		for (int i = 0; i < 1; i++)
			wordsList.add(root.firstChild);
		TrieNode node = root.firstChild;
		TrieNode temp = null;
		TrieNode temp2 = null;
		String tempPrefix = null;
		String result = "";
		if (allWords[node.substr.wordIndex].charAt(node.substr.startIndex) == prefix.charAt(0)){
			result = checkFor(prefix, allWords[node.substr.wordIndex].substring(node.substr.startIndex, node.substr.endIndex+1));
			if (result.contentEquals("A")) {
				node = new TrieNode(node.substr, node.firstChild, null);
				wordsList = makeList(wordsList, allWords, node);
			}
			else {
				temp = new TrieNode(node.substr, null, null);
				temp2 = new TrieNode(null, node.firstChild, node.sibling);
				tempPrefix = prefix.substring(Integer.parseInt(result), prefix.length());
			}
		}
		return wordsList;
	}
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
