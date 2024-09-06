package disintegration.world.blocks.defence.turrets;

import arc.struct.EnumSet;
import arc.util.Nullable;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.entities.Effect;
import mindustry.gen.BlockUnitc;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.world.blocks.ControlBlock;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.production.Drill;
import mindustry.world.meta.BlockFlag;

public class PortableItemTurret extends ItemTurret {
    public UnitType portableUnitType;

    public Effect removalEffect;

    public PortableItemTurret(String name) {
        super(name);
        flags = EnumSet.of(BlockFlag.turret);
    }

    public class PortableItemTurretBuild extends ItemTurretBuild implements ControlBlock {
        @Nullable
        public BlockUnitc unit;
        public @Nullable Unit portableUnit;

        public Unit unit() {
            if (unit == null) {
                unit = (BlockUnitc) UnitTypes.block.create(team);
                unit.tile(this);
            }

            return (Unit) unit;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            if (unit != null && unit.isPlayer()) {
                portableUnit = portableUnitType.create(team);
                portableUnit.set(x, y);
                portableUnit.add();
                portableUnit.rotation(90);
                unit.getPlayer().unit(portableUnit);
                unit.remove();
                tile.setBlock(Blocks.air);
                removalEffect.at(x, y);
            }
        }
    }
}
