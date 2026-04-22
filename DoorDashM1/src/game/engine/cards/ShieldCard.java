package game.engine.cards;

import game.engine.monsters.Monster;

public class ShieldCard extends Card {
	
	public ShieldCard(String name, String description, int rarity) {
		super(name, description, rarity, true); 
	}
	//methods
	
	@Override
    public void performAction(Monster player, Monster opponent) {
        opponent.setShielded(false); // Removes existing shield from opponent
        player.setShielded(true);    // Grants shield to player
    }

}
