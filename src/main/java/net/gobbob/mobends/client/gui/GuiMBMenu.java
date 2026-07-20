package net.gobbob.mobends.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.ClientProxy;
import net.gobbob.mobends.pack.BendsAction;
import net.gobbob.mobends.pack.BendsPack;
import net.gobbob.mobends.pack.BendsTarget;
import net.gobbob.mobends.pack.BendsVar;
import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.gobbob.mobends.util.Color;
import net.gobbob.mobends.util.Draw;
import net.gobbob.mobends.util.EnumAxis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class GuiMBMenu extends GuiScreen {
   public static final ResourceLocation menuTitleTexture = new ResourceLocation("mobends", "textures/gui/menuTitle.png");
   public static final ResourceLocation displayBGTexture = new ResourceLocation("mobends", "textures/gui/displayBG.png");
   public static final ResourceLocation puzzle_animation = new ResourceLocation("mobends", "textures/gui/puzzle_animation.png");
   public static final ResourceLocation puzzle_model = new ResourceLocation("mobends", "textures/gui/puzzle_model.png");
   public static final ResourceLocation puzzle_action_set = new ResourceLocation("mobends", "textures/gui/puzzle_action_set.png");
   public static final ResourceLocation puzzle_action_add = new ResourceLocation("mobends", "textures/gui/puzzle_action_add.png");
   public static final ResourceLocation puzzle_action_substract = new ResourceLocation("mobends", "textures/gui/puzzle_action_substract.png");
   public static final ResourceLocation puzzle_action_multiply = new ResourceLocation("mobends", "textures/gui/puzzle_action_multiply.png");
   public static final ResourceLocation puzzle_mod = new ResourceLocation("mobends", "textures/gui/puzzle_mod.png");
   public static final ResourceLocation puzzle_add = new ResourceLocation("mobends", "textures/gui/puzzle_add.png");
   public static final ResourceLocation puzzle_delete = new ResourceLocation("mobends", "textures/gui/puzzle_delete.png");
   public static final ResourceLocation puzzle_rot = new ResourceLocation("mobends", "textures/gui/puzzle_rot.png");
   public static final ResourceLocation puzzle_scale = new ResourceLocation("mobends", "textures/gui/puzzle_scale.png");
   public static final ResourceLocation puzzle_prerot = new ResourceLocation("mobends", "textures/gui/puzzle_prerot.png");
   public static final ResourceLocation puzzle_mod_none = new ResourceLocation("mobends", "textures/gui/puzzle_mod_none.png");
   public static final ResourceLocation puzzle_mod_cos = new ResourceLocation("mobends", "textures/gui/puzzle_mod_cos.png");
   public static final ResourceLocation puzzle_mod_sin = new ResourceLocation("mobends", "textures/gui/puzzle_mod_sin.png");
   public static final ResourceLocation puzzle_mod_none_selected = new ResourceLocation("mobends", "textures/gui/puzzle_mod_none_selected.png");
   public static final ResourceLocation puzzle_mod_cos_selected = new ResourceLocation("mobends", "textures/gui/puzzle_mod_cos_selected.png");
   public static final ResourceLocation puzzle_mod_sin_selected = new ResourceLocation("mobends", "textures/gui/puzzle_mod_sin_selected.png");
   public static final ResourceLocation puzzle_calc_add = new ResourceLocation("mobends", "textures/gui/puzzle_calc_add.png");
   public static final ResourceLocation puzzle_calc_substract = new ResourceLocation("mobends", "textures/gui/puzzle_calc_substract.png");
   public static final ResourceLocation puzzle_calc_set = new ResourceLocation("mobends", "textures/gui/puzzle_calc_set.png");
   public static final ResourceLocation puzzle_calc_multiply = new ResourceLocation("mobends", "textures/gui/puzzle_calc_multiply.png");
   public static final ResourceLocation puzzle_calc_add_selected = new ResourceLocation("mobends", "textures/gui/puzzle_calc_add_selected.png");
   public static final ResourceLocation puzzle_calc_substract_selected = new ResourceLocation("mobends", "textures/gui/puzzle_calc_substract_selected.png");
   public static final ResourceLocation puzzle_calc_set_selected = new ResourceLocation("mobends", "textures/gui/puzzle_calc_set_selected.png");
   public static final ResourceLocation puzzle_calc_multiply_selected = new ResourceLocation("mobends", "textures/gui/puzzle_calc_multiply_selected.png");
   public float titleTransitionState = 0.0F;
   public boolean titleTransition = true;
   public float[] buttonPositions;
   public float buttonRevealState;
   public float leftBgState;
   public float presetWindowState;
   public float previewRotation;
   public boolean customizeWindow;
   public boolean settingsWindow;
   public boolean packsWindow;
   public int animatedEntityID;
   public int custom_currentAction;
   public int custom_currentChange;
   public float scroll_x;
   public float scroll_y;
   public boolean scrolling_x;
   public boolean scrolling_y;
   public GuiTextField custom_AnimationNameText;
   public GuiTextField custom_PackTitle;
   public GuiTextField custom_ModelNameText;
   public GuiTextField custom_CalcValueText;

   public GuiMBMenu() {
      this.buttonPositions = new float[AnimatedEntity.animatedEntities.length];
      this.buttonRevealState = 0.0F;
      this.leftBgState = 0.0F;
      this.presetWindowState = 0.0F;
      this.previewRotation = 0.0F;
      this.custom_currentAction = 0;
      this.custom_currentChange = 0;
      this.scroll_x = 0.0F;
      this.scroll_y = 0.0F;
      this.scrolling_x = false;
      this.scrolling_y = false;
      Keyboard.enableRepeatEvents(true);
      this.titleTransition = true;
      this.titleTransitionState = 0.0F;
   }

   public void initGui() {
      super.initGui();
      this.buttonList.clear();
      if (this.customizeWindow | this.settingsWindow | this.packsWindow) {
         this.buttonList.add(new GuiButton(0, this.packsWindow ? this.width - 70 : 10, this.height - 30, 60, 20, "Back"));
      }

      if (!this.customizeWindow && !this.settingsWindow && !this.packsWindow) {
         this.buttonList.add(new GuiButton(1, -90 + (int)(this.leftBgState * 100.0F), this.height - 30, 60, 20, "Settings"));
         this.buttonList.add(new GuiButton(3, this.width - (int)(this.leftBgState * 100.0F) + 30, this.height - 30, 60, 20, "Packs"));
      }

      if (this.settingsWindow) {
         for(int i = 0; i < SettingsNode.settings.length; ++i) {
            if (SettingsNode.settings[i] instanceof SettingsBoolean) {
               this.buttonList.add((new GuiToggleButton(10 + i, (int)((float)this.width + this.presetWindowState * -500.0F + 20.0F), 50 + i * 25, ((SettingsBoolean)SettingsNode.settings[i]).data)).setTitle(SettingsNode.settings[i].displayName, 100));
            }
         }
      }

      if (this.customizeWindow) {
         this.buttonList.add((new GuiToggleButton(2, (int)((float)this.width + this.presetWindowState * -500.0F + 10.0F), 163, AnimatedEntity.animatedEntities[this.animatedEntityID].animate)).setTitle("Animate", 88));
         if (this.getCurrentAction() != null) {
            this.buttonList.add(new GuiButton(4, (int)(this.getModelSelectionLoc().x + this.getModelSelectionSize().x - 40.0F), (int)this.getModelSelectionLoc().y + 95, 20, 20, "+"));
            GuiButton minus = new GuiButton(5, (int)(this.getModelSelectionLoc().x + this.getModelSelectionSize().x - 20.0F), (int)this.getModelSelectionLoc().y + 95, 20, 20, "-");
            minus.enabled = this.getCurrentAction().calculations.size() > 0;
            this.buttonList.add(minus);
         }
      }

      for(int i = 0; i < AnimatedEntity.animatedEntities.length; ++i) {
         this.buttonList.add(new GuiButton(100 + i, (int)(this.buttonPositions[i] - 80.0F + this.presetWindowState * -100.0F), 70 + i * 25, 80, 20, AnimatedEntity.animatedEntities[i].displayName));
      }

      if (this.custom_AnimationNameText == null) {
         this.custom_AnimationNameText = new GuiTextField(this.fontRendererObj, (int)(this.getModelSelectionLoc().x + 5.0F), (int)(this.getModelSelectionLoc().y + 5.0F + 10.0F), (int)(this.getModelSelectionSize().x - 10.0F), 15);
         if (this.getCurrentAction() != null) {
            this.custom_AnimationNameText.setText(this.getCurrentAction().anim);
         }
      } else {
         this.custom_AnimationNameText.xPosition = (int)(this.getModelSelectionLoc().x + 5.0F);
         this.custom_AnimationNameText.yPosition = (int)(this.getModelSelectionLoc().y + 5.0F + 10.0F);
      }

      if (this.custom_ModelNameText == null) {
         this.custom_ModelNameText = new GuiTextField(this.fontRendererObj, (int)(this.getModelSelectionLoc().x + 5.0F), (int)(this.getModelSelectionLoc().y + 5.0F + 10.0F + 15.0F + 5.0F + 10.0F), (int)(this.getModelSelectionSize().x - 10.0F), 15);
         if (this.getCurrentAction() != null) {
            this.custom_ModelNameText.setText(this.getCurrentAction().model);
         }
      } else {
         this.custom_ModelNameText.xPosition = (int)(this.getModelSelectionLoc().x + 5.0F);
         this.custom_ModelNameText.yPosition = (int)(this.getModelSelectionLoc().y + 5.0F + 10.0F + 15.0F + 5.0F + 10.0F);
      }

      if (this.custom_CalcValueText == null) {
         this.custom_CalcValueText = new GuiTextField(this.fontRendererObj, (int)(this.getModelSelectionLoc().x + 5.0F), (int)(this.getModelSelectionLoc().y + 90.0F + 10.0F + 5.0F + 10.0F + 5.0F), (int)(this.getModelSelectionSize().x - 10.0F - 32.0F), 15);
         if (this.getCurrentAction() != null && this.getCurrentCalculation() != null) {
            this.custom_CalcValueText.setText(this.getCurrentCalculation().globalVar != null ? this.getCurrentCalculation().globalVar : String.valueOf(this.getCurrentCalculation().number));
         }

         this.custom_CalcValueText.setCursorPositionZero();
      } else {
         this.custom_CalcValueText.width = (int)(this.getModelSelectionSize().x - 10.0F - 32.0F);
         this.custom_CalcValueText.xPosition = (int)(this.getModelSelectionLoc().x + 5.0F + 32.0F);
         this.custom_CalcValueText.yPosition = (int)(this.getModelSelectionLoc().y + 90.0F + 10.0F + 5.0F + 10.0F + 5.0F);
      }

      if (this.custom_PackTitle == null) {
         this.custom_PackTitle = new GuiTextField(this.fontRendererObj, (int)(this.getActionWindowX() + 5.0F), 38, 150, 14);
         if (BendsPack.currentPack == 0) {
            this.custom_PackTitle.setText("Default");
         } else {
            this.custom_PackTitle.setText(BendsPack.getCurrentPack().displayName);
         }

         this.custom_PackTitle.setCursorPositionZero();
      } else {
         this.custom_PackTitle.width = 150;
         this.custom_PackTitle.height = 14;
         this.custom_PackTitle.xPosition = (int)(this.getActionWindowX() + 5.0F);
         this.custom_PackTitle.yPosition = 38;
      }

   }

   protected void keyTyped(char par1, int par2) {
      switch (par2) {
         case 1:
            this.close();
         default:
            if (this.customizeWindow) {
               if (this.custom_AnimationNameText.isFocused()) {
                  this.custom_AnimationNameText.textboxKeyTyped(par1, par2);
                  this.assignAnimationToCurrentAction(this.custom_AnimationNameText.getText());
               } else if (this.custom_ModelNameText.isFocused()) {
                  this.custom_ModelNameText.textboxKeyTyped(par1, par2);
                  ((BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(this.custom_currentAction)).model = this.custom_ModelNameText.getText();
               } else if (this.custom_CalcValueText.isFocused()) {
                  this.custom_CalcValueText.textboxKeyTyped(par1, par2);
                  this.assignCalcValue(this.custom_CalcValueText.getText());
               } else if (this.custom_PackTitle.isFocused()) {
                  this.custom_PackTitle.textboxKeyTyped(par1, par2);
                  if (BendsPack.currentPack == 0) {
                     this.createANewPack(this.custom_PackTitle.getText());
                  }

                  BendsPack.getCurrentPack().displayName = this.custom_PackTitle.getText();
               }
            }

      }
   }

   public void close() {
      Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      MoBends.saveConfig();
      if (BendsPack.currentPack != 0) {
         try {
            BendsPack.getCurrentPack().save();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

   }

   public void updateScreen() {
      this.initGui();
      this.previewRotation += 2.0F;
      int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
      if (this.presetWindowState > 0.0F && this.customizeWindow) {
         this.custom_AnimationNameText.updateCursorCounter();
         this.custom_ModelNameText.updateCursorCounter();
         this.custom_CalcValueText.updateCursorCounter();
         this.custom_PackTitle.updateCursorCounter();
         if (mouseY > 60 && (float)mouseY < 60.0F + this.getActionWindowHeight()) {
            String varAnim = "";
            int displayIndex = 0;
            if (BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id) != null) {
               for(int i = 0; i < BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.size(); ++i) {
                  BendsAction action = (BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(i);
                  if (!action.anim.equalsIgnoreCase(varAnim)) {
                     if (displayIndex > 0) {
                        ++displayIndex;
                     }

                     varAnim = action.anim;
                     ++displayIndex;
                  }

                  if ((float)mouseX >= this.getActionWindowX() + 10.0F && (float)mouseY >= (float)(65 + displayIndex * 18) + this.getYScrollAmount() && (float)mouseY <= (float)(65 + displayIndex * 18 + 18) + this.getYScrollAmount()) {
                     if (action.visual_DeletePopUp < 1.0F) {
                        action.visual_DeletePopUp += 0.2F;
                     }
                  } else if (action.visual_DeletePopUp > 0.0F) {
                     action.visual_DeletePopUp -= 0.2F;
                  }

                  ++displayIndex;
               }

               ++displayIndex;
            }
         }
      }

      if (this.scrolling_y) {
         this.scroll_y = (float)(mouseY - 60) / this.getActionWindowHeight() * (this.getActualActionWindowHeight() / this.getActionWindowHeight());
         if (this.scroll_y > 1.0F) {
            this.scroll_y = 1.0F;
         }

         if (this.scroll_y < 0.0F) {
            this.scroll_y = 0.0F;
         }
      }

      if (!Mouse.isButtonDown(0)) {
         this.scrolling_x = false;
         this.scrolling_y = false;
      }

   }

   protected void mouseClicked(int x, int y, int p_73864_3_) {
      if (this.packsWindow) {
         for(int i = 0; i < BendsPack.bendsPacks.size(); ++i) {
            if ((float)x > (float)this.width + this.presetWindowState * -250.0F + 10.0F && (float)x < (float)this.width + this.presetWindowState * -250.0F + 10.0F + 200.0F && y > i * 70 + 30 && y < i * 70 + 30 + 64) {
               if (i != BendsPack.currentPack && BendsPack.currentPack != 0) {
                  try {
                     BendsPack.getCurrentPack().save();
                  } catch (IOException e1) {
                     e1.printStackTrace();
                  }
               }

               BendsPack.currentPack = i;
               this.custom_PackTitle.setText(BendsPack.getCurrentPack().displayName);

               try {
                  BendsPack.getCurrentPack().apply();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }
      }

      if (this.presetWindowState > 0.0F && this.customizeWindow) {
         this.custom_AnimationNameText.mouseClicked(x, y, p_73864_3_);
         this.custom_ModelNameText.mouseClicked(x, y, p_73864_3_);
         this.custom_PackTitle.mouseClicked(x, y, p_73864_3_);
         if (this.getActualActionWindowHeight() > this.getActionWindowHeight() && (float)x > this.getActionWindowX() + 500.0F - 128.0F - 20.0F - 10.0F && (float)x < this.getActionWindowX() + 500.0F - 128.0F - 20.0F - 10.0F + 10.0F && y > 60 && (float)y < 60.0F + this.getActionWindowHeight()) {
            this.scrolling_y = true;
         }

         if (this.getCurrentAction() != null) {
            if (y > this.custom_ModelNameText.yPosition + 15 + 5 + 10 && y < this.custom_ModelNameText.yPosition + 15 + 5 + 10 + 10) {
               if ((float)x > this.getModelSelectionLoc().x + 5.0F && (float)x < this.getModelSelectionLoc().x + 5.0F + 18.0F) {
                  this.getCurrentAction().mod = null;
               }

               if ((float)x > this.getModelSelectionLoc().x + 5.0F + 18.0F && (float)x < this.getModelSelectionLoc().x + 5.0F + 36.0F) {
                  this.getCurrentAction().mod = BendsAction.EnumModifier.COS;
               }

               if ((float)x > this.getModelSelectionLoc().x + 5.0F + 36.0F && (float)x < this.getModelSelectionLoc().x + 5.0F + 54.0F) {
                  this.getCurrentAction().mod = BendsAction.EnumModifier.SIN;
               }
            }

            if (this.getCurrentCalculation() != null) {
               if (y > this.custom_ModelNameText.yPosition + 60 && y < this.custom_ModelNameText.yPosition + 60 + 10) {
                  if ((float)x > this.getModelSelectionLoc().x + 5.0F && (float)x < this.getModelSelectionLoc().x + 5.0F + 10.0F) {
                     this.getCurrentCalculation().operator = BendsAction.EnumOperator.ADD;
                  }

                  if ((float)x > this.getModelSelectionLoc().x + 5.0F + 10.0F && (float)x < this.getModelSelectionLoc().x + 5.0F + 20.0F) {
                     this.getCurrentCalculation().operator = BendsAction.EnumOperator.SUBSTRACT;
                  }

                  if ((float)x > this.getModelSelectionLoc().x + 5.0F + 20.0F && (float)x < this.getModelSelectionLoc().x + 5.0F + 30.0F) {
                     this.getCurrentCalculation().operator = BendsAction.EnumOperator.MULTIPLY;
                  }

                  if ((float)x > this.getModelSelectionLoc().x + 5.0F + 30.0F && (float)x < this.getModelSelectionLoc().x + 5.0F + 40.0F) {
                     this.getCurrentCalculation().operator = BendsAction.EnumOperator.SET;
                  }
               }

               this.custom_CalcValueText.mouseClicked(x, y, p_73864_3_);
            }
         }

         if ((BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id) == null || BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.size() <= 0) && (float)x >= this.getActionWindowX() && (float)x <= this.getActionWindowX() + 16.0F && (float)y >= 65.0F + this.getYScrollAmount() && (float)y <= 65.0F + this.getYScrollAmount() + 16.0F) {
            if (BendsPack.currentPack == 0) {
               this.createANewPack("Untitled");
            }

            this.addNewDefaultAction("all");
         }

         if (y > 60 && (float)y < 60.0F + this.getActionWindowHeight()) {
            String varAnim = "";
            int displayIndex = 0;
            if (BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id) != null) {
               for(int i = 0; i < BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.size(); ++i) {
                  BendsAction action = (BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(i);
                  if (!action.anim.equalsIgnoreCase(varAnim)) {
                     if (displayIndex > 0) {
                        if ((float)x >= this.getActionWindowX() + 10.0F && (float)x <= this.getActionWindowX() + 10.0F + 16.0F && (float)y >= (float)(65 + displayIndex * 18) + this.getYScrollAmount() && (float)y <= (float)(65 + displayIndex * 18) + this.getYScrollAmount() + 16.0F) {
                           this.addNewDefaultAction(varAnim);
                        }

                        ++displayIndex;
                     }

                     varAnim = action.anim;
                     ++displayIndex;
                  }

                  if (action != null && (float)x > this.getActionWindowX() + 10.0F && (float)y > (float)(65 + displayIndex * 18) + this.getYScrollAmount() && (float)y < (float)(65 + displayIndex * 18 + 16) + this.getYScrollAmount()) {
                     if ((float)x > this.getActionWindowX() + 10.0F && (float)x < this.getActionWindowX() + 10.0F + 20.0F) {
                        if (action.prop == BendsAction.EnumBoxProperty.PREROT) {
                           action.prop = BendsAction.EnumBoxProperty.ROT;
                        } else if (action.prop == BendsAction.EnumBoxProperty.ROT) {
                           action.prop = BendsAction.EnumBoxProperty.SCALE;
                        } else if (action.prop == BendsAction.EnumBoxProperty.SCALE) {
                           action.prop = BendsAction.EnumBoxProperty.PREROT;
                        }
                     }

                     if ((float)x > this.getActionWindowX() + 10.0F + 20.0F && (float)x < this.getActionWindowX() + 10.0F + 31.0F) {
                        if (action.axis == null) {
                           action.axis = EnumAxis.X;
                        } else if (action.axis == EnumAxis.X) {
                           action.axis = EnumAxis.Y;
                        } else if (action.axis == EnumAxis.Y) {
                           action.axis = EnumAxis.Z;
                        } else if (action.axis == EnumAxis.Z) {
                           action.axis = null;
                        }
                     }

                     this.custom_currentAction = i;
                     this.custom_currentChange = 0;
                     this.custom_AnimationNameText.setText(((BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(this.custom_currentAction)).anim);
                     this.custom_ModelNameText.setText(((BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(this.custom_currentAction)).model);
                     if (this.getCurrentCalculation() != null) {
                        this.custom_CalcValueText.setText(this.getCurrentCalculation().globalVar != null ? this.getCurrentCalculation().globalVar : String.valueOf(this.getCurrentCalculation().number));
                        this.custom_CalcValueText.setCursorPositionZero();
                     }

                     for(int s = 0; s < action.calculations.size(); ++s) {
                        BendsAction.Calculation calculation = (BendsAction.Calculation)action.calculations.get(s);
                        Minecraft.getMinecraft().renderEngine.bindTexture(calculation.operator == BendsAction.EnumOperator.ADD ? puzzle_action_add : (calculation.operator == BendsAction.EnumOperator.SUBSTRACT ? puzzle_action_substract : (calculation.operator == BendsAction.EnumOperator.SET ? puzzle_action_set : puzzle_action_multiply)));
                        if ((float)x > this.getActionWindowX() + 10.0F + 31.0F + 64.0F - 7.0F + (float)(action.mod != null ? 25 : 0) + (float)(s * 57) && (float)x < this.getActionWindowX() + 10.0F + 31.0F + 64.0F - 7.0F + (float)(action.mod != null ? 25 : 0) + (float)(s * 57) + 64.0F - 7.0F) {
                           this.custom_currentChange = s;
                           this.custom_CalcValueText.setText(this.getCurrentCalculation().globalVar != null ? this.getCurrentCalculation().globalVar : String.valueOf(this.getCurrentCalculation().number));
                           this.custom_CalcValueText.setCursorPositionZero();
                        }
                     }

                     if ((float)x > this.getActionWindowX() + 10.0F + 31.0F + 64.0F - 7.0F + (float)(action.mod != null ? 25 : 0) + (float)(action.calculations.size() * 57) && (float)x < this.getActionWindowX() + 10.0F + 31.0F + 64.0F - 7.0F + (float)(action.mod != null ? 25 : 0) + (float)(action.calculations.size() * 57) + 32.0F) {
                        BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.remove(i);
                        if (i <= this.custom_currentAction && this.custom_currentAction > 0) {
                           --this.custom_currentAction;
                        }

                        --i;
                     }
                  }

                  ++displayIndex;
               }

               if ((float)x >= this.getActionWindowX() + 10.0F && (float)x <= this.getActionWindowX() + 10.0F + 16.0F && (float)y >= (float)(65 + displayIndex * 18) + this.getYScrollAmount() && (float)y <= (float)(65 + displayIndex * 18) + this.getYScrollAmount() + 16.0F) {
                  this.addNewDefaultAction(varAnim);
               }

               ++displayIndex;
            }
         }
      }

      super.mouseClicked(x, y, p_73864_3_);
   }

   public void createANewPack(String string) {
      BendsPack newPack = new BendsPack();
      newPack.filename = null;
      newPack.displayName = string;
      newPack.author = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
      newPack.description = "A custom pack made by " + Minecraft.getMinecraft().thePlayer.getCommandSenderName() + ".";
      BendsPack.bendsPacks.add(newPack);
      BendsPack.currentPack = BendsPack.bendsPacks.size() - 1;
      this.custom_PackTitle.setText(newPack.displayName);
   }

   protected void actionPerformed(GuiButton par1GuiButton) {
      if (par1GuiButton.id >= 100) {
         this.animatedEntityID = par1GuiButton.id - 100;
         this.customizeWindow = true;
      } else if (par1GuiButton.id >= 10) {
         int s = 0;

         for(int i = 0; i < SettingsNode.settings.length; ++i) {
            if (SettingsNode.settings[i] instanceof SettingsBoolean) {
               if (s == par1GuiButton.id - 10) {
                  ((SettingsBoolean)SettingsNode.settings[i]).data = !((SettingsBoolean)SettingsNode.settings[i]).data;
                  break;
               }

               ++s;
            }
         }
      }

      switch (par1GuiButton.id) {
         case 0:
            this.customizeWindow = false;
            this.settingsWindow = false;
            this.packsWindow = false;
            break;
         case 1:
            this.settingsWindow = true;
            break;
         case 2:
            AnimatedEntity.animatedEntities[this.animatedEntityID].animate = !AnimatedEntity.animatedEntities[this.animatedEntityID].animate;
            break;
         case 3:
            this.packsWindow = true;
            break;
         case 4:
            this.getCurrentAction().calculations.add(new BendsAction.Calculation(BendsAction.EnumOperator.SET, 0.0F));
            this.custom_currentChange = this.getCurrentAction().calculations.size() - 1;
            break;
         case 5:
            this.getCurrentAction().calculations.remove(this.getCurrentCalculation());
            --this.custom_currentChange;
            if (this.custom_currentChange < 0) {
               this.custom_currentChange = 0;
            }
      }

   }

   public void drawScreen(int par1, int par2, float par3) {
      GL11.glDisable(2896);
      GL11.glEnable(3042);
      if (this.titleTransition) {
         this.titleTransitionState += (128.0F - this.titleTransitionState) / 5.0F;
      } else {
         this.titleTransitionState += (100.0F - this.titleTransitionState) / 7.0F;

         for(float i = 0.0F; i < (float)this.buttonPositions.length; ++i) {
            if (this.buttonRevealState >= i / (float)this.buttonPositions.length) {
               float[] var10000 = this.buttonPositions;
               var10000[(int)i] += (90.0F - this.buttonPositions[(int)i]) / 4.0F;
            }
         }

         this.buttonRevealState += 0.05F;
         this.leftBgState += (1.0F - this.leftBgState) / 4.0F;
      }

      if (this.titleTransitionState > 126.0F) {
         this.titleTransition = false;
      }

      if (this.customizeWindow | this.settingsWindow | this.packsWindow) {
         this.presetWindowState += (1.0F - this.presetWindowState) / 4.0F;
      } else {
         this.presetWindowState += (0.0F - this.presetWindowState) / 4.0F;
      }

      GL11.glPushMatrix();
      Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.texture_NULL);
      Draw.rectangle_xgradient(-this.leftBgState * -100.0F - 100.0F + this.presetWindowState * -100.0F, 0.0F, 100.0F, (float)this.height, new Color(0.0F, 0.0F, 0.0F, 0.7F), new Color(0.0F, 0.0F, 0.0F, 0.3F));
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
      Draw.rectangle(-this.leftBgState * -105.0F - 105.0F + this.presetWindowState * -105.0F + 100.0F, 0.0F, 5.0F, (float)this.height);
      GL11.glPopMatrix();
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture(menuTitleTexture);
      Draw.rectangle((float)(this.width / 2 - 64) + this.presetWindowState * (float)(-this.width / 2 + 80), this.titleTransitionState - 100.0F, 128.0F, 64.0F);
      if (this.presetWindowState > 0.0F) {
         GL11.glPushMatrix();
         Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.texture_NULL);
         GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
         float varWidth = this.packsWindow ? 250.0F : 500.0F;
         Draw.rectangle((float)this.width + this.presetWindowState * -varWidth, 0.0F, varWidth, 20.0F);
         Draw.rectangle((float)this.width + this.presetWindowState * -varWidth, 25.0F, varWidth, (float)this.height);
         GL11.glPopMatrix();
      }

      if (this.presetWindowState > 0.0F && this.customizeWindow) {
         this.displayCustomizeWindow();
      }

      if (this.presetWindowState > 0.0F && this.settingsWindow) {
         String title = "Settings";
         this.drawString(this.fontRendererObj, title, (int)((float)this.width + this.presetWindowState * -500.0F + 250.0F - (float)(this.fontRendererObj.getStringWidth(title) / 2)), 5, 16777215);
      }

      if (this.presetWindowState > 0.0F && this.packsWindow) {
         String title = "Packs";
         this.drawString(this.fontRendererObj, title, (int)((float)this.width + this.presetWindowState * -250.0F + 125.0F - (float)(this.fontRendererObj.getStringWidth(title) / 2)), 5, 16777215);

         for(int i = 0; i < BendsPack.bendsPacks.size(); ++i) {
            List<String> text = new ArrayList();
            text.add("");
            int var1 = 0;
            int lineLength = 0;

            for(int s = 0; s < ((BendsPack)BendsPack.bendsPacks.get(i)).description.length(); ++lineLength) {
               text.set(var1, (String)text.get(var1) + ((BendsPack)BendsPack.bendsPacks.get(i)).description.charAt(s));
               if ((float)this.fontRendererObj.getStringWidth((String)text.get(var1)) * 0.5F > 128.0F && ((BendsPack)BendsPack.bendsPacks.get(i)).description.charAt(s) == ' ') {
                  lineLength = 0;
                  ++var1;
                  text.add("");
               }

               ++s;
            }

            GL11.glPushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.texture_NULL);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
            if (i != BendsPack.currentPack) {
               Draw.rectangle((float)this.width + this.presetWindowState * -250.0F + 10.0F, (float)(i * 70 + 30), 200.0F, 64.0F);
            } else {
               Draw.rectangle_xgradient((float)this.width + this.presetWindowState * -250.0F + 10.0F, (float)(i * 70 + 30), 200.0F, 64.0F, new Color(0.0F, 0.0F, 0.0F, 0.5F), new Color(0.1F, 1.0F, 0.1F, 0.5F));
            }

            int var10003 = (int)((float)this.width + this.presetWindowState * -250.0F + 10.0F) + 5;
            this.drawString(this.fontRendererObj, ((BendsPack)BendsPack.bendsPacks.get(i)).displayName, var10003, i * 70 + 30 + 5, 16777215);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)((int)((float)this.width + this.presetWindowState * -250.0F + 10.0F) + 5), (float)(i * 70 + 30 + 5 + 10), 0.0F);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.drawString(this.fontRendererObj, "By " + ((BendsPack)BendsPack.bendsPacks.get(i)).author, 0, 0, 7829367);

            for(int s = 0; s < text.size(); ++s) {
               this.drawString(this.fontRendererObj, (String)text.get(s), 0, 20 + s * 10, 16777215);
            }

            GL11.glPopMatrix();
            GL11.glPopMatrix();
         }
      }

      int color = 47121212;
      super.drawScreen(par1, par2, par3);
   }

   public void renderLivingEntity(int argX, int argY, int scale, EntityLivingBase par5EntityLivingBase) {
      GL11.glEnable(2903);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)argX, (float)argY, 50.0F);
      GL11.glScalef((float)(-scale), (float)scale, (float)scale);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(this.previewRotation, 0.0F, 1.0F, 0.0F);
      float f2 = par5EntityLivingBase.renderYawOffset;
      float f3 = par5EntityLivingBase.rotationYaw;
      float f4 = par5EntityLivingBase.rotationPitch;
      float f5 = par5EntityLivingBase.prevRotationYawHead;
      float f6 = par5EntityLivingBase.rotationYawHead;
      GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      RenderHelper.enableStandardItemLighting();
      GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
      par5EntityLivingBase.renderYawOffset = 0.0F;
      par5EntityLivingBase.rotationYaw = 0.0F;
      par5EntityLivingBase.rotationPitch = 0.0F;
      par5EntityLivingBase.rotationYawHead = par5EntityLivingBase.rotationYaw;
      par5EntityLivingBase.prevRotationYawHead = par5EntityLivingBase.rotationYaw;
      GL11.glTranslatef(0.0F, par5EntityLivingBase.yOffset, 0.0F);
      RenderManager.instance.playerViewY = 180.0F;
      par5EntityLivingBase.moveForward = 1.0F;
      RenderManager.instance.renderEntityWithPosYaw(par5EntityLivingBase, (double)0.0F, (double)0.0F, (double)0.0F, 0.0F, 1.0F);
      par5EntityLivingBase.renderYawOffset = f2;
      par5EntityLivingBase.rotationYaw = f3;
      par5EntityLivingBase.rotationPitch = f4;
      par5EntityLivingBase.prevRotationYawHead = f5;
      par5EntityLivingBase.rotationYawHead = f6;
      GL11.glPopMatrix();
      RenderHelper.disableStandardItemLighting();
      GL11.glDisable(32826);
      OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glDisable(3553);
      OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   public boolean doesGuiPauseGame() {
      return true;
   }

   public void displayCustomizeWindow() {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      String title = "Animation Customization";
      this.drawCenteredString(this.fontRendererObj, title, (int)((float)this.width + this.presetWindowState * -500.0F + 250.0F), 5, 16777215);
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture(displayBGTexture);
      Draw.rectangle((float)this.width + this.presetWindowState * -500.0F + 10.0F, 35.0F, 128.0F, 128.0F);
      GL11.glPopMatrix();
      if (AnimatedEntity.animatedEntities[this.animatedEntityID].id == "player") {
         this.renderLivingEntity((int)((float)this.width + this.presetWindowState * -500.0F + 10.0F + 64.0F), 150, 50, Minecraft.getMinecraft().thePlayer);
      } else {
         AnimatedEntity.animatedEntities[this.animatedEntityID].entity.worldObj = Minecraft.getMinecraft().theWorld;
         this.renderLivingEntity((int)((float)this.width + this.presetWindowState * -500.0F + 10.0F + 64.0F), 150, 50, (EntityLivingBase)AnimatedEntity.animatedEntities[this.animatedEntityID].entity);
      }

      String warning = "* to see the changes after toggling the animations ON or OFF, restart your game.";
      this.drawString(this.fontRendererObj, warning, (int)((float)this.width + this.presetWindowState * -500.0F + 20.0F), this.height - 20, 16777215);
      GL11.glPushMatrix();
      Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.texture_NULL);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
      Draw.rectangle(this.getModelSelectionLoc().x, this.getModelSelectionLoc().y, this.getModelSelectionSize().x, this.getModelSelectionSize().y);
      GL11.glPopMatrix();
      if (this.getCurrentAction() != null) {
         this.drawString(this.fontRendererObj, "Animation:", this.custom_AnimationNameText.xPosition, this.custom_AnimationNameText.yPosition - 10, 16777215);
         this.drawString(this.fontRendererObj, "Model:", this.custom_ModelNameText.xPosition, this.custom_ModelNameText.yPosition - 10, 16777215);
         this.drawString(this.fontRendererObj, "Modifier:", this.custom_ModelNameText.xPosition, this.custom_ModelNameText.yPosition - 10 + 15 + 5 + 10, 16777215);
         this.custom_AnimationNameText.drawTextBox();
         this.custom_ModelNameText.drawTextBox();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         Minecraft.getMinecraft().renderEngine.bindTexture(this.getCurrentAction().mod == null ? puzzle_mod_none_selected : puzzle_mod_none);
         Draw.rectangle(this.getModelSelectionLoc().x + 5.0F, (float)(this.custom_ModelNameText.yPosition + 15 + 5 + 10), 32.0F, 16.0F);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         Minecraft.getMinecraft().renderEngine.bindTexture(this.getCurrentAction().mod == BendsAction.EnumModifier.COS ? puzzle_mod_cos_selected : puzzle_mod_cos);
         Draw.rectangle(this.getModelSelectionLoc().x + 5.0F + 18.0F, (float)(this.custom_ModelNameText.yPosition + 15 + 5 + 10), 32.0F, 16.0F);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         Minecraft.getMinecraft().renderEngine.bindTexture(this.getCurrentAction().mod == BendsAction.EnumModifier.SIN ? puzzle_mod_sin_selected : puzzle_mod_sin);
         Draw.rectangle(this.getModelSelectionLoc().x + 5.0F + 36.0F, (float)(this.custom_ModelNameText.yPosition + 15 + 5 + 10), 32.0F, 16.0F);
         GL11.glPopMatrix();
         this.drawString(this.fontRendererObj, "Calculation:", this.custom_ModelNameText.xPosition, this.custom_ModelNameText.yPosition - 10 + 60, 16777215);
         if (this.getCurrentCalculation() != null) {
            this.drawString(this.fontRendererObj, "Value:", this.custom_ModelNameText.xPosition, this.custom_ModelNameText.yPosition + 15 + 5 + 60 - 1, this.isValidCalcValue(this.custom_CalcValueText.getText()) ? 16777215 : 16711680);
            GL11.glPushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getCurrentCalculation().operator == BendsAction.EnumOperator.ADD ? puzzle_calc_add_selected : puzzle_calc_add);
            Draw.rectangle(this.getModelSelectionLoc().x + 5.0F, (float)(this.custom_ModelNameText.yPosition + 60), 16.0F, 16.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getCurrentCalculation().operator == BendsAction.EnumOperator.SUBSTRACT ? puzzle_calc_substract_selected : puzzle_calc_substract);
            Draw.rectangle(this.getModelSelectionLoc().x + 5.0F + 10.0F, (float)(this.custom_ModelNameText.yPosition + 60), 16.0F, 16.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getCurrentCalculation().operator == BendsAction.EnumOperator.MULTIPLY ? puzzle_calc_multiply_selected : puzzle_calc_multiply);
            Draw.rectangle(this.getModelSelectionLoc().x + 5.0F + 20.0F, (float)(this.custom_ModelNameText.yPosition + 60), 16.0F, 16.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getCurrentCalculation().operator == BendsAction.EnumOperator.SET ? puzzle_calc_set_selected : puzzle_calc_set);
            Draw.rectangle(this.getModelSelectionLoc().x + 5.0F + 30.0F, (float)(this.custom_ModelNameText.yPosition + 60), 16.0F, 16.0F);
            GL11.glPopMatrix();
            this.custom_CalcValueText.drawTextBox();
         }
      }

      GL11.glPushMatrix();
      GL11.glEnable(3042);
      Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.texture_NULL);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
      Draw.rectangle(this.getActionWindowX(), 35.0F, 342.0F, 20.0F);
      Draw.rectangle(this.getActionWindowX(), 60.0F, 342.0F, this.getActionWindowHeight());
      GL11.glPopMatrix();
      if (this.getActualActionWindowHeight() > this.getActionWindowHeight()) {
         GL11.glPushMatrix();
         Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.texture_NULL);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         Draw.rectangle(this.getActionWindowX() + 500.0F - 128.0F - 20.0F - 10.0F, 60.0F + this.scroll_y * (this.getActualActionWindowHeight() - this.getActionWindowHeight()) * (this.getActionWindowHeight() / this.getActualActionWindowHeight()), 10.0F, this.getActionWindowHeight() * (this.getActionWindowHeight() / this.getActualActionWindowHeight()));
         GL11.glPopMatrix();
      }

      this.custom_PackTitle.drawTextBox();
      float scale = (float)(Minecraft.getMinecraft().displayWidth / this.width);
      GL11.glViewport((int)((float)Minecraft.getMinecraft().displayWidth + (float)((int)(this.presetWindowState * -500.0F + 10.0F + 128.0F + 10.0F)) * scale), (int)(30.0F * scale), (int)(342.0F * scale), (int)((float)Minecraft.getMinecraft().displayHeight + -90.0F * scale));
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho((double)((float)this.width + this.presetWindowState * -500.0F + 10.0F + 128.0F + 10.0F), (double)((float)this.width + this.presetWindowState * -500.0F + 10.0F + 128.0F + 10.0F + 500.0F - 128.0F - 20.0F - 10.0F), (double)(60 + this.height - 25 - 40 - 25), (double)60.0F, (double)1000.0F, (double)3000.0F);
      GL11.glMatrixMode(5888);
      String varAnim = "";
      int displayIndex = 0;
      if (BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id) != null && BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.size() > 0) {
         for(int i = 0; i < BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.size(); ++i) {
            BendsAction action = (BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(i);
            if (!action.anim.equalsIgnoreCase(varAnim)) {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               if (displayIndex > 0) {
                  Minecraft.getMinecraft().renderEngine.bindTexture(puzzle_add);
                  Draw.rectangle(this.getActionWindowX() + 10.0F, (float)(65 + displayIndex * 18) + this.getYScrollAmount(), 16.0F, 16.0F);
                  ++displayIndex;
               }

               Minecraft.getMinecraft().renderEngine.bindTexture(puzzle_animation);
               Draw.rectangle(this.getActionWindowX(), (float)(65 + displayIndex * 18) + this.getYScrollAmount(), 128.0F, 16.0F);
               this.drawString(this.fontRendererObj, action.anim, (int)(this.getActionWindowX() + 5.0F), (int)((float)(69 + displayIndex * 18) + this.getYScrollAmount()), 16777215);
               varAnim = action.anim;
               ++displayIndex;
            }

            if (this.custom_currentAction == i) {
               GL11.glPushMatrix();
               GL11.glTranslatef(0.0F, 0.0F, 0.0F);
               Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.texture_NULL);
               Draw.rectangle_xgradient(this.getActionWindowX() + 10.0F - 5.0F, (float)(65 + displayIndex * 18) + this.getYScrollAmount() - 1.0F, (float)(88 + (action.mod != null ? 25 : 0) + action.calculations.size() * 57 - 16 - 16) + 23.0F * action.visual_DeletePopUp + 50.0F + 5.0F, 18.0F, new Color(1.0F, 0.7F, 0.2F, 0.2F), new Color(1.0F, 0.7F, 0.2F, 1.0F));
               GL11.glPopMatrix();
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(action.prop == BendsAction.EnumBoxProperty.ROT ? puzzle_rot : (action.prop == BendsAction.EnumBoxProperty.SCALE ? puzzle_scale : puzzle_prerot));
            Draw.rectangle(this.getActionWindowX() + 10.0F, (float)(65 + displayIndex * 18) + this.getYScrollAmount(), 32.0F, 16.0F);
            if (action != null) {
               Minecraft.getMinecraft().renderEngine.bindTexture(puzzle_delete);
               Draw.rectangle(this.getActionWindowX() + 10.0F + 31.0F + 64.0F - 7.0F + (float)(action.mod != null ? 25 : 0) + (float)(action.calculations.size() * 57) - 16.0F - 16.0F + 23.0F * action.visual_DeletePopUp, (float)(65 + displayIndex * 18) + this.getYScrollAmount(), 32.0F, 16.0F);
               this.drawString(this.fontRendererObj, action.axis == EnumAxis.X ? "x" : (action.axis == EnumAxis.Y ? "y" : (action.axis == EnumAxis.Z ? "z" : "?")), (int)(this.getActionWindowX() + 10.0F + 22.0F), (int)((float)(69 + displayIndex * 18) + this.getYScrollAmount()), 16777215);
               Minecraft.getMinecraft().renderEngine.bindTexture(puzzle_model);
               Draw.rectangle(this.getActionWindowX() + 10.0F + 31.0F, (float)(65 + displayIndex * 18) + this.getYScrollAmount(), 64.0F, 16.0F);
               this.drawString(this.fontRendererObj, action.model, (int)(this.getActionWindowX() + 5.0F + 10.0F + 31.0F), (int)((float)(69 + displayIndex * 18) + this.getYScrollAmount()), 16777215);
               if (action.mod != null) {
                  Minecraft.getMinecraft().renderEngine.bindTexture(puzzle_mod);
                  Draw.rectangle(this.getActionWindowX() + 10.0F + 31.0F + 64.0F - 7.0F, (float)(65 + displayIndex * 18) + this.getYScrollAmount(), 32.0F, 16.0F);
                  this.drawString(this.fontRendererObj, action.mod == BendsAction.EnumModifier.COS ? "cos" : "sin", (int)(this.getActionWindowX() + 10.0F + 31.0F + 64.0F - 7.0F + 5.0F), (int)((float)(69 + displayIndex * 18) + this.getYScrollAmount()), 16777215);
               }

               for(int s = 0; s < action.calculations.size(); ++s) {
                  BendsAction.Calculation calculation = (BendsAction.Calculation)action.calculations.get(s);
                  Minecraft.getMinecraft().renderEngine.bindTexture(calculation.operator == BendsAction.EnumOperator.ADD ? puzzle_action_add : (calculation.operator == BendsAction.EnumOperator.SUBSTRACT ? puzzle_action_substract : (calculation.operator == BendsAction.EnumOperator.SET ? puzzle_action_set : puzzle_action_multiply)));
                  GL11.glPushMatrix();
                  if (this.custom_currentChange != s || this.custom_currentAction != i) {
                     GL11.glColor4f(0.7F, 0.7F, 0.7F, 1.0F);
                  } else {
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  }

                  Draw.rectangle(this.getActionWindowX() + 10.0F + 31.0F + 64.0F - 7.0F + (float)(action.mod != null ? 25 : 0) + (float)(s * 57), (float)(65 + displayIndex * 18) + this.getYScrollAmount(), 64.0F, 16.0F);
                  GL11.glPopMatrix();
                  this.drawString(this.fontRendererObj, "" + (calculation.globalVar != null ? calculation.globalVar : calculation.number), (int)(this.getActionWindowX() + 10.0F + 31.0F + 64.0F - 7.0F + (float)(action.mod != null ? 25 : 0)) + s * 57 + 20, (int)((float)(69 + displayIndex * 18) + this.getYScrollAmount()), 16777215);
               }
            }

            ++displayIndex;
         }

         Minecraft.getMinecraft().renderEngine.bindTexture(puzzle_add);
         Draw.rectangle(this.getActionWindowX() + 10.0F, (float)(65 + displayIndex * 18) + this.getYScrollAmount(), 16.0F, 16.0F);
         ++displayIndex;
      } else {
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         Minecraft.getMinecraft().renderEngine.bindTexture(puzzle_add);
         Draw.rectangle(this.getActionWindowX(), (float)(65 + displayIndex * 18) + this.getYScrollAmount(), 16.0F, 16.0F);
         GL11.glPopMatrix();
      }

      GL11.glViewport(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      ScaledResolution res = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
      GL11.glOrtho((double)0.0F, (double)res.getScaledWidth(), (double)res.getScaledHeight(), (double)0.0F, (double)1000.0F, (double)3000.0F);
      GL11.glMatrixMode(5888);
   }

   public float getActualActionWindowHeight() {
      String varAnim = "";
      int displayIndex = 0;
      if (BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id) != null) {
         for(int i = 0; i < BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.size(); ++i) {
            BendsAction action = (BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(i);
            if (!action.anim.equalsIgnoreCase(varAnim)) {
               if (displayIndex > 0) {
                  ++displayIndex;
               }

               varAnim = action.anim;
               ++displayIndex;
            }

            ++displayIndex;
         }

         ++displayIndex;
      }

      ++displayIndex;
      return (float)(displayIndex * 18);
   }

   public float getActionWindowHeight() {
      return (float)(this.height - 25 - 40 - 25);
   }

   public float getYScrollAmount() {
      return -this.scroll_y * (this.getActualActionWindowHeight() - this.getActionWindowHeight());
   }

   public float getActionWindowX() {
      return this.getPropertiesWindowX() + 10.0F + 128.0F + 10.0F + 5.0F;
   }

   public float getPropertiesWindowX() {
      return (float)this.width + this.presetWindowState * -this.getPropertiesWindowWidth();
   }

   public float getPropertiesWindowWidth() {
      return this.packsWindow ? 250.0F : 500.0F;
   }

   public Vector2f getModelSelectionLoc() {
      return new Vector2f(this.getPropertiesWindowX() + 10.0F, 200.0F);
   }

   public Vector2f getModelSelectionSize() {
      return new Vector2f(128.0F, 200.0F);
   }

   public void assignAnimationToCurrentAction(String argAnim) {
      BendsTarget target = BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id);
      if (target != null) {
         BendsAction action = (BendsAction)target.actions.get(this.custom_currentAction);
         if (!argAnim.equalsIgnoreCase(action.anim)) {
            target.actions.remove(this.custom_currentAction);
            action.anim = argAnim;
            int newIndex = 0;
            List<BendsAction> newActionList = new ArrayList();
            boolean done = false;
            boolean properAnim = false;

            for(int i = 0; i < target.actions.size(); ++i) {
               if (((BendsAction)target.actions.get(i)).anim.equalsIgnoreCase(argAnim)) {
                  properAnim = true;
               } else if (properAnim && !done) {
                  newActionList.add(action);
                  done = true;
                  newIndex = i;
               }

               newActionList.add(target.actions.get(i));
            }

            if (!done) {
               newActionList.add(action);
               newIndex = newActionList.size() - 1;
            }

            target.actions = newActionList;
            this.custom_currentAction = newIndex;
         }

      }
   }

   public boolean isValidCalcValue(String value) {
      if (value == null || value.isEmpty()) {
         return false;
      } else if (NumberUtils.isNumber(value)) {
         return true;
      } else {
         return BendsVar.getGlobalVar(value) != Float.POSITIVE_INFINITY;
      }
   }

   public void assignCalcValue(String value) {
      if (this.isValidCalcValue(value)) {
         if (this.getCurrentCalculation() != null) {
            if (NumberUtils.isNumber(value)) {
               this.getCurrentCalculation().globalVar = null;
               System.out.println(value + " = " + Float.valueOf(value));
               this.getCurrentCalculation().number = Float.valueOf(value);
            } else {
               this.getCurrentCalculation().globalVar = value;
            }
         }
      } else {
         this.getCurrentCalculation().globalVar = null;
         this.getCurrentCalculation().number = 0.0F;
      }

   }

   public BendsAction getCurrentAction() {
      if (BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id) == null) {
         return null;
      } else {
         return this.custom_currentAction < BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.size() ? (BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(this.custom_currentAction) : null;
      }
   }

   public BendsAction.Calculation getCurrentCalculation() {
      return this.getCurrentAction() != null && this.getCurrentAction().calculations.size() > 0 ? (BendsAction.Calculation)this.getCurrentAction().calculations.get(this.custom_currentChange) : null;
   }

   public void addNewDefaultAction(String argAnim) {
      if (BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id) == null) {
         BendsPack.targets.add(new BendsTarget(AnimatedEntity.animatedEntities[this.animatedEntityID].id));
      }

      BendsAction action = new BendsAction(argAnim, "", BendsAction.EnumBoxProperty.ROT, (EnumAxis)null, 0.3F, 1.0F);
      BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.add(action);
      this.custom_currentAction = BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.size() - 1;
      action.anim = "NULL";
      this.assignAnimationToCurrentAction(argAnim);
      if (this.custom_currentAction > 0) {
         action.model = ((BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(this.custom_currentAction - 1)).model;
         action.prop = ((BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(this.custom_currentAction - 1)).prop;
         action.axis = ((BendsAction)BendsPack.getTargetByID(AnimatedEntity.animatedEntities[this.animatedEntityID].id).actions.get(this.custom_currentAction - 1)).axis;
      }

   }
}
