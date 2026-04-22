package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	//methods
	
	@Override
    public void executePowerupEffect(Monster opponentMonster) {
        // Handled directly in Game/Board class to access all stationed monsters, 
        // or by passing the board state if permitted. As per M2, it steals from 
        // opponent and stationed monsters. We implement the helper here:
    }

    private int stealEnergyFrom(Monster target) {
        int amountToSteal = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
        target.setEnergy(target.getEnergy() - amountToSteal);
        return amountToSteal;
    }

    @Override
    public void setEnergy(int energy) {
        int change = energy - this.getEnergy();
        int bonus = (change != 0) ? 10 : 0;
        super.setEnergy(Math.max(0, this.getEnergy() + change + bonus));
    }
}
