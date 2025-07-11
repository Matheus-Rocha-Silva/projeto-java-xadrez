package xadrez;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bishop;
import xadrez.pecas.King;
import xadrez.pecas.Knight;
import xadrez.pecas.Pawn;
import xadrez.pecas.Queen;
import xadrez.pecas.Rook;

public class PartidaXadrez {

	private int turn;
	private Cor currentPlayer;
	private Tabuleiro board;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez enPassantVulnerable;
	private PecaXadrez promoted;

	private List<Peca> piecesOnTheBoard = new ArrayList<>();
	private List<Peca> capturedPieces = new ArrayList<>();

	public PartidaXadrez() {
		board = new Tabuleiro(8, 8);
		turn = 1;
		currentPlayer = Cor.BRANCO;
		check = false;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Cor getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public PecaXadrez getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

	public PecaXadrez getPromoted() {
		return promoted;
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[board.getLinhas()][board.getColunas()];
		for (int i = 0; i < board.getLinhas(); i++) {
			for (int j = 0; j < board.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) board.peca(i, j);
			}
		}
		return mat;
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Posicao posicao = sourcePosition.toPosition();
		validateSourcePosition(posicao);
		return board.peca(posicao).possibleMoves();
	}

	public PecaXadrez performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Posicao source = sourcePosition.toPosition();
		Posicao target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Peca capturedPiece = makeMove(source, target);

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Você não pode se colocar em cheque");
		}

		PecaXadrez movedPiece = (PecaXadrez) board.peca(target);

		// Movimento especial promoção
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if ((movedPiece.getCor() == Cor.BRANCO && target.getLinha() == 0)
					|| (movedPiece.getCor() == Cor.PRETO && target.getLinha() == 7)) {
				promoted = (PecaXadrez) board.peca(target);
				promoted = replacePromotedPiece("Q");
			}
		}

		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}

		// Movimento especial en Passant
		if (movedPiece instanceof Pawn
				&& (target.getLinha() == source.getLinha() - 2 || target.getLinha() == source.getLinha() + 2)) {
			enPassantVulnerable = movedPiece;
		} else {
			enPassantVulnerable = null;
		}

		return (PecaXadrez) capturedPiece;
	}

	public PecaXadrez replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("Não há peça para ser promovida");
		}
		if(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("R")) {
			//throw new InvalidParameterException("Tipo invalido para promoção");
			return promoted;
		}
		
		Posicao pos = promoted.getChessPosition().toPosition();
		Peca p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		PecaXadrez newPiece = newPiece(type, promoted.getCor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}

	private PecaXadrez newPiece(String type, Cor cor) {
		if(type.equals("B")) return new Bishop(board, cor);
		if(type.equals("N")) return new Knight(board, cor);
		if(type.equals("Q")) return new Queen(board, cor);
		return new Rook(board, cor);
	}
	
	private Peca makeMove(Posicao source, Posicao target) {
		PecaXadrez p = (PecaXadrez) board.removePiece(source);
		p.increaseMoveCount();
		Peca capturedPiece = board.removePiece(target);
		board.placePiece(p, target);

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		// Movimento especial castling kingside rook
		if (p instanceof King && target.getColuna() == source.getColuna() + 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() + 3);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() + 1);
			PecaXadrez rook = (PecaXadrez) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		// Movimento especial castling queenside rook
		if (p instanceof King && target.getColuna() == source.getColuna() - 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() - 4);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() - 1);
			PecaXadrez rook = (PecaXadrez) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		// Movimento especial en passant
		if (p instanceof Pawn) {
			if (source.getColuna() != target.getColuna() && capturedPiece == null) {
				Posicao pawnPosition;
				if (p.getCor() == Cor.BRANCO) {
					pawnPosition = new Posicao(target.getLinha() + 1, target.getColuna());
				} else {
					pawnPosition = new Posicao(target.getLinha() - 1, target.getColuna());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}

		return capturedPiece;
	}

	private void undoMove(Posicao source, Posicao target, Peca capturedPiece) {
		PecaXadrez p = (PecaXadrez) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		// Movimento especial castling kingside rook Undo
		if (p instanceof King && target.getColuna() == source.getColuna() + 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() + 3);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() + 1);
			PecaXadrez rook = (PecaXadrez) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// Movimento especial castling queenside rook Undo
		if (p instanceof King && target.getColuna() == source.getColuna() - 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() - 4);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() - 1);
			PecaXadrez rook = (PecaXadrez) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// Movimento especial en passant Undo
		if (p instanceof Pawn) {
			if (source.getColuna() != target.getColuna() && capturedPiece == enPassantVulnerable) {
				PecaXadrez pawn = (PecaXadrez) board.removePiece(target);
				Posicao pawnPosition;
				if (p.getCor() == Cor.BRANCO) {
					pawnPosition = new Posicao(3, target.getColuna());
				} else {
					pawnPosition = new Posicao(4, target.getColuna());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
	}

	private void validateSourcePosition(Posicao posicao) {
		if (!board.thereIsAPiece(posicao)) {
			throw new ChessException("Não há posição no tabuleiro");
		}
		if (currentPlayer != ((PecaXadrez) board.peca(posicao)).getCor()) {
			throw new ChessException("A peça escolhida não é sua.");
		}
		if (!board.peca(posicao).isThereAnyPossibleMove()) {
			throw new ChessException("Não há movimentos possiveis para a peça escolhida.");
		}
	}

	private void validateTargetPosition(Posicao source, Posicao target) {
		if (!board.peca(source).possibleMove(target)) {
			throw new ChessException("A peça escolhida não pode se mover para a posição ed destino.");
		}
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private Cor opponent(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private PecaXadrez king(Cor cor) {
		List<Peca> list = piecesOnTheBoard.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof King) {
				return (PecaXadrez) p;
			}
		}
		throw new IllegalStateException("Não há nenhum rei " + cor + " no tabuleiro");
	}

	private boolean testCheck(Cor cor) {
		Posicao kingPosition = king(cor).getChessPosition().toPosition();
		List<Peca> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((PecaXadrez) x).getCor() == opponent(cor))
				.collect(Collectors.toList());

		for (Peca p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getLinha()][kingPosition.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Cor cor) {
		if (!testCheck(cor)) {
			return false;
		}
		List<Peca> list = piecesOnTheBoard.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getLinhas(); i++) {
				for (int j = 0; j < board.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao source = ((PecaXadrez) p).getChessPosition().toPosition();
						Posicao target = new Posicao(i, j);
						Peca capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(cor);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void placeNewPiece(char coluna, int linha, PecaXadrez peca) {
		board.placePiece(peca, new ChessPosition(coluna, linha).toPosition());
		piecesOnTheBoard.add(peca);
	}

	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Cor.BRANCO));
		placeNewPiece('b', 1, new Knight(board, Cor.BRANCO));
		placeNewPiece('c', 1, new Bishop(board, Cor.BRANCO));
		placeNewPiece('d', 1, new Queen(board, Cor.BRANCO));
		placeNewPiece('e', 1, new King(board, Cor.BRANCO, this));
		placeNewPiece('f', 1, new Bishop(board, Cor.BRANCO));
		placeNewPiece('g', 1, new Knight(board, Cor.BRANCO));
		placeNewPiece('h', 1, new Rook(board, Cor.BRANCO));
		placeNewPiece('a', 2, new Pawn(board, Cor.BRANCO, this));
		placeNewPiece('b', 2, new Pawn(board, Cor.BRANCO, this));
		placeNewPiece('c', 2, new Pawn(board, Cor.BRANCO, this));
		placeNewPiece('d', 2, new Pawn(board, Cor.BRANCO, this));
		placeNewPiece('e', 2, new Pawn(board, Cor.BRANCO, this));
		placeNewPiece('f', 2, new Pawn(board, Cor.BRANCO, this));
		placeNewPiece('g', 2, new Pawn(board, Cor.BRANCO, this));
		placeNewPiece('h', 2, new Pawn(board, Cor.BRANCO, this));

		placeNewPiece('a', 8, new Rook(board, Cor.PRETO));
		placeNewPiece('b', 8, new Knight(board, Cor.PRETO));
		placeNewPiece('c', 8, new Bishop(board, Cor.PRETO));
		placeNewPiece('d', 8, new Queen(board, Cor.PRETO));
		placeNewPiece('e', 8, new King(board, Cor.PRETO, this));
		placeNewPiece('f', 8, new Bishop(board, Cor.PRETO));
		placeNewPiece('g', 8, new Knight(board, Cor.PRETO));
		placeNewPiece('h', 8, new Rook(board, Cor.PRETO));
		placeNewPiece('a', 7, new Pawn(board, Cor.PRETO, this));
		placeNewPiece('b', 7, new Pawn(board, Cor.PRETO, this));
		placeNewPiece('c', 7, new Pawn(board, Cor.PRETO, this));
		placeNewPiece('d', 7, new Pawn(board, Cor.PRETO, this));
		placeNewPiece('e', 7, new Pawn(board, Cor.PRETO, this));
		placeNewPiece('f', 7, new Pawn(board, Cor.PRETO, this));
		placeNewPiece('g', 7, new Pawn(board, Cor.PRETO, this));
		placeNewPiece('h', 7, new Pawn(board, Cor.PRETO, this));
	}
}
