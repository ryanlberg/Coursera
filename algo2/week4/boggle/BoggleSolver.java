import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;

public class BoggleSolver {
	
	private final RyTrie words;
	private HashSet<String> wordsInSet;
	private boolean[][] check;
	private final int[][] helper;
	
	public BoggleSolver(String[] dictionary) {
		words = new RyTrie();
		wordsInSet = new HashSet<>();
		helper = new int[][] {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
		for (String word : dictionary) {
			words.addWord(word);
		}
	}
	
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		this.wordsInSet = new HashSet<>();
		check = new boolean[board.rows()][board.cols()];
		
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				fillWordsInSet(board, i, j, new StringBuilder(), words.getTrie());
			}
		}
		
		return wordsInSet;
	}
	
	private void fillWordsInSet(BoggleBoard board, int i, int j, StringBuilder current, RyTrieNode last) {
		
		if (current.length() >= 3 && (last.getIsWord() || last.size() == 0)) {
			wordsInSet.add(current.toString());
		}
		
		char curlet = board.getLetter(i, j);
		
		if (curlet == 'Q') {
			if (last.hasLetter(curlet) && last.getNode(curlet).hasLetter('U')) {
				current.append(curlet);
				current.append('U');
				check[i][j] = true;
				RyTrieNode next = last.getNode(curlet).getNode('U');
				if (current.length() >= 3 && (next.getIsWord() || next.size() == 0)) {
					words.addWord(current.toString());
				}
				for (int[] help : helper) {
					if (isValid(i+ help[0], j+help[1], board)) {
						this.fillWordsInSet(board, i+help[0], j+help[1], current, next);
						
					}
				}
				check[i][j] = false;
				current.setLength(current.length() -2);
			}
		} else {
			if (last.hasLetter(curlet)) {
				current.append(curlet);
				check[i][j] = true;
				RyTrieNode next = last.getNode(curlet);
				if (current.length() >= 3 && (next.getIsWord() || next.size() == 0)) {
					wordsInSet.add(current.toString());
				}
				for (int[] help : helper) {
					if (isValid(i+ help[0], j+help[1], board)) {
						
						this.fillWordsInSet(board, i+help[0], j+help[1], current, next);
					}
				}
				check[i][j] = false;
				current.setLength(current.length() -1);
			}
		}
		
		
	}
		
	
	private boolean isValid(int i, int j, BoggleBoard board) {
		return i >= 0 && i < board.rows() &&  j >= 0 && j < board.cols() && !this.check[i][j];
	}
	
	public int scoreOf(String word) {
		if (wordsInSet.contains(word)) {
			int wordLength = word.length();
			if (wordLength <= 4) {
				return 1;
			} else if (wordLength == 5) {
				return 2;
			} else if (wordLength == 6) {
				return 3;
			} else if (wordLength == 7) {
				return 5;
			}
			return 11;
		}
		return 0;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
	    String[] dictionary = in.readAllStrings();
	    BoggleSolver solver = new BoggleSolver(dictionary);
	    BoggleBoard board = new BoggleBoard(args[1]);
	    int score = 0;
	    for (String word : solver.getAllValidWords(board)) {
	        StdOut.println(word);
	        score += solver.scoreOf(word);
	    }
	    StdOut.println("Score = " + score);
	}

}
