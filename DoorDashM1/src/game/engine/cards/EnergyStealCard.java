package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}
	
	public int getEnergy() {
		return energy;
	}
	//methods
	
	@Override
	public void performAction(Monster player, Monster opponent) {
	    if (opponent.isShielded()) {
	        // 1. Consume the shield
	        opponent.setShielded(false);
	        
	        // 2. Do nothing else (energy remains the same)
	        return;
	    }

	    // If NOT shielded, proceed with the steal
	    int amountToSteal = this.getEnergy();
	    
	    // Ensure we don't steal more than the opponent actually has
	    int actualStolen = Math.min(amountToSteal, opponent.getEnergy());
	    
	    // Update energies
	    opponent.setEnergy(opponent.getEnergy() - actualStolen);
	    player.setEnergy(player.getEnergy() + actualStolen);
	}

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue);
    }
	
}
