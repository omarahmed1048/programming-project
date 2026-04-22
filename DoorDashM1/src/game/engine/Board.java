package game.engine;

import game.engine.cells.*;
import game.engine.monsters.Monster;
import game.engine.cards.Card;
import game.engine.exceptions.InvalidMoveException;

import java.util.ArrayList;
import java.util.Collections;

public class Board {
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters; 
	private static ArrayList<Card> originalCards;
	private static ArrayList<Card> cards;
	
	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		originalCards = readCards;
		cards = new ArrayList<Card>();
		this.setCardsByRarity();
		reloadCards();
	}
	
	public Cell[][] getBoardCells() {
		return boardCells;
	}
	
	public static ArrayList<Monster> getStationedMonsters() {
		return stationedMonsters;
	}
	
	public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
		Board.stationedMonsters = stationedMonsters;
	}

	public static ArrayList<Card> getOriginalCards() {
		return originalCards;
	}
	
	public static ArrayList<Card> getCards() {
		return cards;
	}
	
	public static void setCards(ArrayList<Card> cards) {
		Board.cards = cards;
	}
	
	//methods
	private int[] indexToRowCol(int index) {
        // [cite: 938, 939]
        int row = index / Constants.BOARD_COLS;
        int col;
        if (row % 2 == 0) {
            col = index % Constants.BOARD_COLS; // Left-to-right
        } else {
            col = (Constants.BOARD_COLS - 1) - (index % Constants.BOARD_COLS); // Right-to-left
        }
        return new int[]{row, col};
    }

    private Cell getCell(int index) {
        int[] pos = indexToRowCol(index);
        return boardCells[pos[0]][pos[1]]; // [cite: 940]
    }

    private void setCell(int index, Cell cell) {
        int[] pos = indexToRowCol(index);
        boardCells[pos[0]][pos[1]] = cell; // [cite: 941]
    }

    public void initializeBoard(ArrayList<Cell> specialCells) {
        // Separate the special cells by type for assignment
        ArrayList<DoorCell> doors = new ArrayList<>();
        ArrayList<ConveyorBelt> conveyors = new ArrayList<>();
        ArrayList<ContaminationSock> socks = new ArrayList<>();
        
        for (Cell c : specialCells) {
            if (c instanceof DoorCell) doors.add((DoorCell) c);
            else if (c instanceof ConveyorBelt) conveyors.add((ConveyorBelt) c);
            else if (c instanceof ContaminationSock) socks.add((ContaminationSock) c);
        }

        int doorIdx = 0, conveyorIdx = 0, sockIdx = 0, cardIdx = 0, monsterIdx = 0;

        // 1. Populate basic pattern: Even = Normal, Odd = Door [cite: 943]
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            if (i % 2 == 0) {
                setCell(i, new Cell("Normal Cell " + i));
            } else {
                if (doorIdx < doors.size()) {
                    setCell(i, doors.get(doorIdx++));
                } else {
                    setCell(i, new Cell("Normal Cell " + i)); 
                }
            }
        }

        // 2. Override with specific special cells [cite: 944]
        for (int index : Constants.CONVEYOR_CELL_INDICES) {
            if (conveyorIdx < conveyors.size()) setCell(index, conveyors.get(conveyorIdx++));
        }
        for (int index : Constants.SOCK_CELL_INDICES) {
            if (sockIdx < socks.size()) setCell(index, socks.get(sockIdx++));
        }
        for (int index : Constants.CARD_CELL_INDICES) {
            setCell(index, new CardCell("Card Cell"));
        }
        
        // 3. Stationed monsters [cite: 944, 945]
        for (int index : Constants.MONSTER_CELL_INDICES) {
            if (monsterIdx < stationedMonsters.size()) {
                Monster m = stationedMonsters.get(monsterIdx++);
                m.setPosition(index);
                setCell(index, new MonsterCell("Monster Cell", m));
            }
        }
    }

    private void setCardsByRarity() {
        ArrayList<Card> expandedList = new ArrayList<>();
        for (Card card : originalCards) {
            for (int i = 0; i < card.getRarity(); i++) {
                expandedList.add(card); // [cite: 946]
            }
        }
        originalCards = expandedList;
    }

    public static void reloadCards() {
        cards = new ArrayList<>(originalCards);
        Collections.shuffle(cards); // [cite: 947]
    }

    public static Card drawCard() {
        if (cards.isEmpty()) {
            reloadCards(); // [cite: 950]
        }
        return cards.remove(0); // [cite: 949]
    }

    public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
        int initialPos = currentMonster.getPosition();
        currentMonster.move(roll); // [cite: 953]

        Cell landedCell = getCell(currentMonster.getPosition());
        landedCell.onLand(currentMonster, opponentMonster); // Trigger cell effects [cite: 953]

        // Collision check [cite: 954]
        if (currentMonster.getPosition() == opponentMonster.getPosition() && currentMonster.getPosition() != Constants.STARTING_POSITION) {
            currentMonster.setPosition(initialPos);
            throw new InvalidMoveException();
        }

        // Confusion processing [cite: 955]
        currentMonster.decrementConfusion();
        opponentMonster.decrementConfusion();

        // Sync board [cite: 957]
        updateMonsterPositions(currentMonster, opponentMonster);
    }

    private void updateMonsterPositions(Monster player, Monster opponent) {
        // Clear all cell references [cite: 959]
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            Cell c = getCell(i);
            if (!(c instanceof MonsterCell)) {
                c.setMonster(null);
            }
        }
        // Reassign [cite: 959]
        getCell(player.getPosition()).setMonster(player);
        getCell(opponent.getPosition()).setMonster(opponent);
    }

    
}

