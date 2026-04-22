package game.engine.cells;

import game.engine.monsters.*;

public class MonsterCell extends Cell {
	private Monster cellMonster;

	public MonsterCell(String name, Monster cellMonster) {
		super(name);
		this.cellMonster = cellMonster;
	}

	public Monster getCellMonster() {
		return cellMonster;
	}
	//methods
	@Override
    public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        Monster stationed = this.getCellMonster();
        
        if (landingMonster.getRole() == stationed.getRole()) {
            landingMonster.executePowerupEffect(opponentMonster);
        } else {
            if (landingMonster.getEnergy() > stationed.getEnergy()) {
                int tempEnergy = landingMonster.getEnergy();
                
                // Shield check for landing monster penalty
                if (landingMonster.isShielded()) {
                    landingMonster.setShielded(false);
                } else {
                    landingMonster.setEnergy(stationed.getEnergy());
                }
                stationed.setEnergy(tempEnergy); // Stationed always gets the increase
            }
        }
    }

}
