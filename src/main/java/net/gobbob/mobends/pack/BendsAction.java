package net.gobbob.mobends.pack;

import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.util.EnumAxis;
import net.minecraft.util.MathHelper;

public class BendsAction {
   public String anim;
   public String model;
   public List<Calculation> calculations = new ArrayList();
   public EnumBoxProperty prop;
   public EnumAxis axis;
   public float smooth;
   public EnumModifier mod;
   public float visual_DeletePopUp;

   public BendsAction(String argAnim, String argModel, EnumBoxProperty argProp, EnumAxis argAxis, float argSmooth, float argNumber) {
      this.anim = argAnim;
      this.model = argModel;
      this.prop = argProp;
      this.axis = argAxis;
      this.smooth = argSmooth;
      this.visual_DeletePopUp = 0.0F;
   }

   public BendsAction() {
   }

   public BendsAction setModifier(EnumModifier argMod) {
      this.mod = argMod;
      return this;
   }

   public float getNumber(float in) {
      return BendsAction.Calculation.calculateAll(this.mod, in, this.calculations);
   }

   public static EnumOperator getOperatorFromSymbol(String symbol) {
      return symbol.equalsIgnoreCase("+=") ? BendsAction.EnumOperator.ADD : (symbol.equalsIgnoreCase("-=") ? BendsAction.EnumOperator.SUBSTRACT : (symbol.equalsIgnoreCase("==") ? BendsAction.EnumOperator.SET : (symbol.equalsIgnoreCase("*=") ? BendsAction.EnumOperator.MULTIPLY : BendsAction.EnumOperator.DIVIDE)));
   }

   public static enum EnumOperator {
      SET,
      ADD,
      MULTIPLY,
      DIVIDE,
      SUBSTRACT;
   }

   public static enum EnumBoxProperty {
      ROT,
      SCALE,
      PREROT;
   }

   public static enum EnumModifier {
      COS,
      SIN;
   }

   public static class Calculation {
      public EnumOperator operator;
      public float number;
      public String globalVar = null;

      public Calculation(EnumOperator argOperator, float argNumber) {
         this.operator = argOperator;
         this.number = argNumber;
      }

      public Calculation setGlobalVar(String argGlobalVar) {
         this.globalVar = argGlobalVar;
         return this;
      }

      public float calculate(float in) {
         float num = this.globalVar != null ? BendsVar.getGlobalVar(this.globalVar) : this.number;
         float out = 0.0F;
         if (this.operator == BendsAction.EnumOperator.ADD) {
            out = in + num;
         }

         if (this.operator == BendsAction.EnumOperator.SET) {
            out = num;
         }

         if (this.operator == BendsAction.EnumOperator.SUBSTRACT) {
            out = in - num;
         }

         if (this.operator == BendsAction.EnumOperator.MULTIPLY) {
            out = in * num;
         }

         if (this.operator == BendsAction.EnumOperator.DIVIDE) {
            out = in / num;
         }

         return out;
      }

      public static float calculateAll(EnumModifier mod, float in, List<Calculation> argCalc) {
         float out = in;

         for(int i = 0; i < argCalc.size(); ++i) {
            out = ((Calculation)argCalc.get(i)).calculate(out);
         }

         if (mod == BendsAction.EnumModifier.COS) {
            out = MathHelper.cos(out);
         }

         if (mod == BendsAction.EnumModifier.SIN) {
            out = MathHelper.sin(out);
         }

         return out;
      }
   }
}
