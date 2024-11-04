package disintegration.type.maps.planet;

import arc.math.geom.Vec2;
import disintegration.content.DTBlocks;
import disintegration.content.DTLoadouts;
import disintegration.util.WorldDef;
import mindustry.content.Blocks;
import mindustry.game.Team;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.maps.generators.BlankPlanetGenerator;
import mindustry.type.Sector;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;

import static mindustry.Vars.state;
import static mindustry.Vars.world;

public class SpaceStationGenerator extends BlankPlanetGenerator {
    public Block core = DTBlocks.spaceStationCore;

    @Override
    public void generate() {

        defaultLoadout = DTLoadouts.basicSpacestations;
        int sx = width / 2, sy = height / 2;

        Floor background = Blocks.empty.asFloor();
        Floor ground = DTBlocks.spaceStationFloorFixed.asFloor();

        tiles.eachTile(t -> t.setFloor(background));

        WorldDef.getAreaTile(new Vec2(sx, sy), core.size + 4, core.size + 4).each(t -> {
            t.setFloor(ground);
        });
        world.tile(sx + core.size / 2 + 3, sy + core.size / 2 + 3).setBlock(core, Team.sharded);

        state.rules.planetBackground = new PlanetParams() {{
            planet = sector.planet.parent;
            camPos.setZero();
            sector.planet.addParentOffset(camPos);
            camPos.scl(0.5f);
            zoom = 1f;
        }};

        state.rules.dragMultiplier = 0.7f; //yes, space actually has 0 drag but true 0% drag is very annoying
        state.rules.borderDarkness = false;
        state.rules.waves = false;
    }

    @Override
    public int getSectorSize(Sector sector) {
        return 600;
    }
}
