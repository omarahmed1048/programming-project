package game.engine.monsters;

import game.engine.Role;

public class Dasher extends Monster {
	private int momentumTurns;

	public Dasher(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.momentumTurns = 0;
	}
	
	public int getMomentumTurns() {
		return momentumTurns;
	}
	
	public void setMomentumTurns(int momentumTurns) {
		this.momentumTurns = momentumTurns;
	}
	//methods
	
	@Override
    public void executePowerupEffect(Monster opponentMonster) {
        this.setMomentumTurns(3);
    }

    @Override
    public void move(int distance) {
        int speedMultiplier = (this.getMomentumTurns() > 0) ? 3 : 2;
        super.move(distance * speedMultiplier);
    }

}