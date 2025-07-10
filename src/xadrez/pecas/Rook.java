package xadrez.pecas;

import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rook extends PecaXadrez {

	public Rook(Tabuleiro board, Cor cor) {
		super(board, cor);
	}
	
	@Override
	public String toString() {
		return "R";
	}
}
