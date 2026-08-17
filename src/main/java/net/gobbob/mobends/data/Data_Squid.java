package net.gobbob.mobends.data;

import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsSquid;
import net.minecraft.client.Minecraft;

public class Data_Squid extends EntityData {
   public static final int TENTACLE_SECTIONS = 9;
   public static final int SECTION_HEIGHT = 18 / TENTACLE_SECTIONS;
   public static List<Data_Squid> dataList = new ArrayList();
   public ModelRendererBends squidBody;
   public ModelRendererBends[][] squidTentacles;

   public Data_Squid(int argEntityID) {
      super(argEntityID);
   }

   public void syncModelInfo(ModelBendsSquid argModel) {
      if (this.squidBody == null) {
         this.squidBody = new ModelRendererBends(argModel);
      }

      this.squidBody.sync(argModel.squidBody);
      if (this.squidTentacles == null) {
         this.squidTentacles = new ModelRendererBends[8][TENTACLE_SECTIONS];
      }

      for(int i = 0; i < 8; ++i) {
         for(int j = 0; j < TENTACLE_SECTIONS; ++j) {
            if (this.squidTentacles[i][j] == null) {
               this.squidTentacles[i][j] = new ModelRendererBends(argModel);
            }

            this.squidTentacles[i][j].sync(argModel.tentacles[i][j]);
         }
      }

   }

   public static void add(Data_Squid argData) {
      dataList.add(argData);
   }

   public static Data_Squid get(int argEntityID) {
      for(int i = 0; i < dataList.size(); ++i) {
         if (((Data_Squid)dataList.get(i)).entityID == argEntityID) {
            return (Data_Squid)dataList.get(i);
         }
      }

      Data_Squid newData = new Data_Squid(argEntityID);
      if (Minecraft.getMinecraft().theWorld.getEntityByID(argEntityID) != null) {
         dataList.add(newData);
      }

      return newData;
   }

   public void update(float argPartialTicks) {
      super.update(argPartialTicks);
   }
}
