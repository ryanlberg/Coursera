import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;
import java.util.HashSet;

public class BoggleSolver {
	
	private final TrieSET words;
	private HashSet<String> wordsInSet;
	private int[][] check;
	private final int[][] helper;
	
	public BoggleSolver(String[] dictionary) {
		words = new TrieSET();
		wordsInSet = new HashSet<>();
		helper = new int[][] {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
		for(String word : dictionary) {
			words.add(word);
		}
	}
	
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		this.wordsInSet = new HashSet<>();
		check = new int[board.rows()][board.cols()];
		
		for(int i = 0; i < board.rows(); i++) {
			for(int j = 0; j < board.cols(); j++) {
				fillWordsInSet(board, i, j, new StringBuilder());
			}
		}
		
		return wordsInSet;
	}
	
	private void fillWordsInSet(BoggleBoard board, int i, int j, StringBuilder current) {
		char curlet = board.getLetter(i, j);
		if(curlet == 'Q') {
			current.append('Q');
			current.append('U');
		} else {
			current.append(curlet);
		}
		
		String curstring = current.toString();
		if(curstring.length() > 2 && words.contains(curstring)) {
			wordsInSet.add(curstring);
		}
		
		check[i][j] = 1;
		for(int[] next : helper) {
			if(isValid(i+next[0], j+next[1], board)) {
				fillWordsInSet(board, i+next[0], j+next[1], current);
			}
		}
		check[i][j] = 0;
		if(curlet == 'Q') {
			current.deleteCharAt(current.length()-1);
			current.deleteCharAt(current.length()-1);
		} else {
			current.deleteCharAt(current.length()-1);
		}
		
	}
	
	private boolean isValid(int i, int j, BoggleBoard board) {
		return i >= 0 && i < board.rows() &&  j >= 0 && j < board.cols() && this.check[i][j] == 0;
	}
	
	public int scoreOf(String word) {
		if(words.contains(word)) {
			int wordLength = word.length();
			if(wordLength <= 4) {
				return 1;
			} else if(wordLength == 5) {
				return 2;
			} else if(wordLength == 6) {
				return 3;
			} else if(wordLength == 7) {
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
