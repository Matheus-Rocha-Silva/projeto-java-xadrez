package xadrez;

import tabuleiro.Posicao;

public class ChessPosition {
	private char coluna;
	private int linha;
	
	public ChessPosition(char coluna, int linha) {
		if(coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
			throw new ChessException("Erro instanciando ChessPosition. Valores validos são de a1 até a8");
		}
		this.coluna = coluna;
		this.linha = linha;
	}

	public char getColuna() {
		return coluna;
	}

	public int getLinha() {
		return linha;
	}
	
	protected Posicao toPosition() {
		return new Posicao(8 - linha, coluna - 'a');
	}
	
	protected static ChessPosition fromPosition(Posicao posicao) {
		return new ChessPosition((char)('a' + posicao.getColuna()), 8 - posicao.getLinha());
	}
	
	@Override
	public String toString() {
		return "" + coluna + linha;
	}
}
