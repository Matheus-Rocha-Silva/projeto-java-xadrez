package tabuleiro;

public class Tabuleiro {
	private int linhas;
	private int colunas;
	private Peca[][] pecas;
	
	public Tabuleiro(int linhas, int colunas) {
		if(linhas < 1 || colunas < 1) {
			throw new BoardException("Erro ao criar tabuleiro, é necessário que haja pelo menos 1 linha e 1 coluna.");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public Peca peca(int linha, int coluna) {
		if(!positionExists(linha, coluna)) {
			throw new BoardException("Posição não existe no tabuleiro");
		}
		return pecas[linha][coluna];
	}
	
	public Peca peca(Posicao posicao) {
		if(!positionExists(posicao)) {
			throw new BoardException("Posição não existe no tabuleiro");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void placePiece(Peca peca, Posicao posicao) {
		if(thereIsAPiece(posicao)) {
			throw new BoardException("há uma peça nessa posição");
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	public Peca removePiece(Posicao posicao) {
		if(!positionExists(posicao)) {
			throw new BoardException("Posição não existente");
		}
		if(peca(posicao) == null) {
			return null;
		}
		Peca aux = peca(posicao);
		aux.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		return aux;
	}
	
	
	private boolean positionExists(int linha, int coluna) {
		return (linha >= 0) && (linha < linhas) && (coluna >= 0) && (coluna < colunas);
	}
	
	public boolean positionExists(Posicao posicao) {
		return positionExists(posicao.getLinha(), posicao.getColuna());
	}
	
	public boolean thereIsAPiece(Posicao posicao) {
		if(!positionExists(posicao)) {
			throw new BoardException("Posição não existe no tabuleiro");
		}
		return peca(posicao) != null;
	}
}
