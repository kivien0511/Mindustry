package mindustry.ai.types;

import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

public class FlyingAI extends AIController{

    @Override
    public void updateMovement(){
        if(unit.moving()){
            unit.lookAt(unit.vel.angle());
        }

        if(unit.isFlying()){
            unit.wobble();
        }

        if(target != null && unit.hasWeapons()){
            if(unit.type().weapons.first().rotate){
                moveTo(target, unit.range() * 0.8f);
                unit.lookAt(target);
            }else{
                attack(80f);
            }
        }
    }

    @Override
    protected Teamc findTarget(float x, float y, float range, boolean air, boolean ground){
        Teamc result = target(x, y, range, air, ground);
        if(result != null) return result;

        if(ground) result = targetFlag(x, y, BlockFlag.producer, true);
        if(result != null) return result;

        if(ground) result = targetFlag(x, y, BlockFlag.turret, true);
        if(result != null) return result;

        return null;
    }

    //TODO clean up

    protected void attack(float circleLength){
        vec.set(target).sub(unit);

        float ang = unit.angleTo(target);
        float diff = Angles.angleDist(ang, unit.rotation());

        if(diff > 100f && vec.len() < circleLength){
            vec.setAngle(unit.vel().angle());
        }else{
            vec.setAngle(Mathf.slerpDelta(unit.vel().angle(), vec.angle(), 0.6f));
        }

        vec.setLength(unit.type().speed * Time.delta);

        unit.moveAt(vec);
    }
}
