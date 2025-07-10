package tabuleiro;

public class Peca {
	
	protected Posicao posicao;
	private Tabuleiro board;
	
	public Peca(Tabuleiro board) {
		this.board = board;
		posicao = null;
	}
	
	protected Tabuleiro getBoard() {
		return board;
	}
}
