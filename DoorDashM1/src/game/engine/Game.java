package game.engine;

import game.engine.cards.Card;
import game.engine.cells.Cell;
import game.engine.dataloader.DataLoader;
import game.engine.monsters.Monster;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.exceptions.InvalidMoveException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters; 
	private Monster player;
	private Monster opponent;
	private Monster current;
	private Random random;
	
	public Game(Role playerRole) throws Exception { // Or throws IOException depending on your M1 setup
        // 1. Load all data from the CSV files using DataLoader (from M1)
        ArrayList<Card> loadedCards = DataLoader.readCards();
        ArrayList<Monster> loadedMonsters = DataLoader.readMonsters();
        ArrayList<Cell> loadedCells = DataLoader.readCells(); // THIS line fixes your error
        
        this.allMonsters = loadedMonsters;
        
        // 2. Create the board with the loaded cards
        this.board = new Board(loadedCards);
        
        // 3. Select player and opponent (Your existing M1 logic)
        // (Assuming you have a helper like selectRandomMonsterByRole)
        this.player = selectRandomMonsterByRole(playerRole);
        
        Role opponentRole = (playerRole == Role.SCARER) ? Role.LAUGHER : Role.SCARER;
        this.opponent = selectRandomMonsterByRole(opponentRole);
        
        this.current = this.player;

        // --- MILESTONE 2 ADDITIONS ---
        
        // 4. Gather the remaining 6 monsters to be stationed on the board
        ArrayList<Monster> stationedMonsters = new ArrayList<>();
        for (Monster m : this.allMonsters) {
            if (m != this.player && m != this.opponent) {
                stationedMonsters.add(m);
            }
        }
        
        // 5. Call the newly required Board methods
        Board.setStationedMonsters(stationedMonsters);
        this.board.initializeBoard(loadedCells); // Now loadedCells exists and works!
    }
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Monster> getAllMonsters() {
		return allMonsters; 
	}
	
	public Monster getPlayer() {
		return player;
	}
	
	public Monster getOpponent() {
		return opponent;
	}
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}
	//methods
	
	private Monster getCurrentOpponent() {
        return (this.current == this.player) ? this.opponent : this.player; // [cite: 973]
    }

	public int rollDice() {
	    Random rand = new Random();
	    // nextInt(6) gives 0-5, so we add 1 to get 1-6
	    return rand.nextInt(6) + 1;
	}

    public void usePowerup() throws OutOfEnergyException {
        if (current.getEnergy() < Constants.POWERUP_COST) {
            throw new OutOfEnergyException(); // [cite: 976]
        }
        current.setEnergy(current.getEnergy() - Constants.POWERUP_COST);
        current.executePowerupEffect(getCurrentOpponent()); // [cite: 975]
    }

    public void playTurn() throws InvalidMoveException {
        if (current.isFrozen()) {
            current.setFrozen(false); // Unfreeze and skip turn [cite: 978]
        } else {
            int roll = rollDice(); // [cite: 979]
            board.moveMonster(current, roll, getCurrentOpponent()); // [cite: 979]
        }
        switchTurn(); // [cite: 980]
    }

    private void switchTurn() {
        this.current = getCurrentOpponent(); // [cite: 981]
    }

    private boolean checkWinCondition(Monster monster) {
        // [cite: 982, 840, 841, 842]
        return monster.getPosition() == Constants.WINNING_POSITION 
               && monster.getEnergy() >= Constants.WINNING_ENERGY;
    }

    public Monster getWinner() {
        if (checkWinCondition(this.player)) return this.player;     // [cite: 985]
        if (checkWinCondition(this.opponent)) return this.opponent; // [cite: 985]
        return null;                                                // [cite: 985]
    }
	
}