package net.gobbob.mobends.pack;

import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.util.EnumAxis;

public class BendsTarget {
   public String mob;
   public List<BendsAction> actions = new ArrayList();
   public float visual_DeletePopUp;

   public BendsTarget(String argMob) {
      this.mob = argMob;
      this.visual_DeletePopUp = 0.0F;
   }

   public void applyToModel(ModelRendererBends box, String anim, String model) {
      for(int i = 0; i < this.actions.size(); ++i) {
         if ((((BendsAction)this.actions.get(i)).anim.equalsIgnoreCase(anim) | ((BendsAction)this.actions.get(i)).anim.equalsIgnoreCase("all")) & ((BendsAction)this.actions.get(i)).model.equalsIgnoreCase(model)) {
            if (((BendsAction)this.actions.get(i)).prop == BendsAction.EnumBoxProperty.ROT) {
               box.rotation.setSmooth(((BendsAction)this.actions.get(i)).axis, ((BendsAction)this.actions.get(i)).getNumber(((BendsAction)this.actions.get(i)).axis == EnumAxis.X ? box.rotation.vFinal.x : (((BendsAction)this.actions.get(i)).axis == EnumAxis.Y ? box.rotation.vFinal.y : box.rotation.vFinal.z)), ((BendsAction)this.actions.get(i)).smooth);
            } else if (((BendsAction)this.actions.get(i)).prop == BendsAction.EnumBoxProperty.SCALE) {
               if (((BendsAction)this.actions.get(i)).axis == null || ((BendsAction)this.actions.get(i)).axis == EnumAxis.X) {
                  box.scaleX = ((BendsAction)this.actions.get(i)).getNumber(box.scaleX);
               }

               if (((BendsAction)this.actions.get(i)).axis == null || ((BendsAction)this.actions.get(i)).axis == EnumAxis.Y) {
                  box.scaleY = ((BendsAction)this.actions.get(i)).getNumber(box.scaleY);
               }

               if (((BendsAction)this.actions.get(i)).axis == null || ((BendsAction)this.actions.get(i)).axis == EnumAxis.Z) {
                  box.scaleZ = ((BendsAction)this.actions.get(i)).getNumber(box.scaleZ);
               }
            }
         }
      }

   }
}
