package disintegration.world.blocks.power;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arclibrary.graphics.Draw3d;
import mindustry.Vars;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.blocks.power.SolarGenerator;

public class RotateSolarGenerator extends SolarGenerator {
    public TextureRegion baseRegion;
    public TextureRegion panelRegion;

    public float levitation = 4f;
    public float thickness = 0.9f;
    public float spacing = 0.3f;

    public RotateSolarGenerator(String name) {
        super(name);
        update = true;
        solid = true;
    }

    @Override
    public void load() {
        super.load();
        baseRegion = Core.atlas.find(name + "-base");
        panelRegion = Core.atlas.find(name + "-panel");
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{baseRegion, panelRegion};
    }

    public class RotateSolarGeneratorBuild extends SolarGeneratorBuild {
        Mat3D mat = new Mat3D();
        public float rot = 0, angle = 0;

        @Override
        public void draw() {
            Draw.rect(baseRegion, x, y);
            if (Vars.state.rules.sector == null) angle = -45;
            else {
                if (timer.get(30)) {
                    angle = (Vars.state.rules.sector.getLight() * 90 - 45);
                }
            }
            rot = Mathf.lerp(rot, angle, 0.05f);
            Draw.reset();
            Draw.z(Layer.blockOver);
            //Draw.rect(panelRegion, x + Mathf.sin(rot * Mathf.degreesToRadians) * (levitation - thickness), y, panelRegion.width * Mathf.cos(rot * Mathf.degreesToRadians) / 4f, panelRegion.height / 4f);
            //Draw.rect(panelRegion, x + Mathf.sin(rot * Mathf.degreesToRadians) * levitation, y, panelRegion.width * Mathf.cos(rot * Mathf.degreesToRadians) / 4f, panelRegion.height / 4f);
            for (float i = thickness; i > 0; i -= spacing) {
                mat.idt();
                mat.rotate(Vec3.Y, rot);
                mat.translate(0, 0, -Mathf.cos(rot * Mathf.degreesToRadians) * (levitation - i) + levitation - i);

                Draw3d.rect(mat, panelRegion, x - panelRegion.width / 8f - 4 * Mathf.sin(rot * Mathf.degreesToRadians) * (levitation - i), y - panelRegion.height / 8f, panelRegion.width / 4f, panelRegion.height / 4f, 0);
            }
            mat.idt();
            mat.rotate(Vec3.Y, rot);
            mat.translate(0, 0, -Mathf.cos(rot * Mathf.degreesToRadians) * levitation + levitation);
            Draw3d.rect(mat, panelRegion, x - panelRegion.width / 8f - 4 * Mathf.sin(rot * Mathf.degreesToRadians) * levitation, y - panelRegion.height / 8f, panelRegion.width / 4f, panelRegion.height / 4f, 0);
            Draw.z(Layer.blockAdditive);
            Draw.color(Pal.shadow);
            //Draw.rect(panelRegion, x + Mathf.sin(rot * Mathf.degreesToRadians) * levitation - levitation, y - levitation, panelRegion.width * Mathf.cos(rot * Mathf.degreesToRadians) / 4f, panelRegion.height / 4f);
            Draw3d.rect(mat, panelRegion, x - panelRegion.width / 8f - Mathf.sin(rot * Mathf.degreesToRadians) * levitation - levitation, y - panelRegion.height / 8f - levitation, panelRegion.width / 4f, panelRegion.height / 4f, 0);
        }

        @Override
        public void read(Reads read, byte revisions){
            rot = read.f();
        }

        @Override
        public void write(Writes write){
            write.f(rot);
        }
    }
}
