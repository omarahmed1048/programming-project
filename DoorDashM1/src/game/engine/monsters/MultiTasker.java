package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class MultiTasker extends Monster {
	private int normalSpeedTurns;
	
	public MultiTasker(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.normalSpeedTurns = 0;
	}

	public int getNormalSpeedTurns() {
		return normalSpeedTurns;
	}

	public void setNormalSpeedTurns(int normalSpeedTurns) {
		this.normalSpeedTurns = normalSpeedTurns;
	}
	//methods
	
	@Override
    public void executePowerupEffect(Monster opponentMonster) {
        this.setNormalSpeedTurns(2);
    }

    @Override
    public void move(int distance) {
        if (this.getNormalSpeedTurns() > 0) {
            super.move(distance);
            // Decrement the speed turns so the character eventually slows down
            this.setNormalSpeedTurns(this.getNormalSpeedTurns() - 1); 
        } else {
            // If no normal speed turns left, move at half speed
            super.move(distance / 2);
        }
    }

    @Override
    public void setEnergy(int energy) {
        int change = energy - this.getEnergy();
        int bonus = (change != 0) ? Constants.MULTITASKER_BONUS : 0;
        super.setEnergy(Math.max(0, this.getEnergy() + change + bonus));
    }

}