package net.gobbob.mobends.util;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * Minimal quaternion for MoBends 1.12.2-style centerRotation (FlyingAnimationBit).
 */
public class Quaternion {
   private static final float PI = (float)Math.PI;
   private static final FloatBuffer GL_MATRIX = BufferUtils.createFloatBuffer(16);

   public float x;
   public float y;
   public float z;
   public float w;

   public Quaternion() {
      this.setIdentity();
   }

   public void setIdentity() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      this.w = 1.0F;
   }

   public void set(Quaternion other) {
      this.x = other.x;
      this.y = other.y;
      this.z = other.z;
      this.w = other.w;
   }

   public void setFromAxisAngle(float ax, float ay, float az, float angleRad) {
      float n = (float)Math.sqrt((double)(ax * ax + ay * ay + az * az));
      float s = (float)(Math.sin((double)(0.5F * angleRad)) / (double)n);
      this.x = ax * s;
      this.y = ay * s;
      this.z = az * s;
      this.w = (float)Math.cos((double)(0.5F * angleRad));
   }

   /** Left-multiply (world-space), like 1.12.2 Quaternion.rotate / SmoothOrientation.rotate*. */
   public void rotateDegrees(float angleDeg, float ax, float ay, float az) {
      float angle = angleDeg / 180.0F * PI;
      float n = (float)Math.sqrt((double)(ax * ax + ay * ay + az * az));
      float s = (float)(Math.sin((double)(0.5F * angle)) / (double)n);
      float x2 = ax * s;
      float y2 = ay * s;
      float z2 = az * s;
      float w2 = (float)Math.cos((double)(0.5F * angle));
      mul(x2, y2, z2, w2, this.x, this.y, this.z, this.w, this);
   }

   /** Right-multiply (local-space), like 1.12.2 SmoothOrientation.localRotate*. */
   public void localRotateDegrees(float angleDeg, float ax, float ay, float az) {
      float angle = angleDeg / 180.0F * PI;
      float n = (float)Math.sqrt((double)(ax * ax + ay * ay + az * az));
      float s = (float)(Math.sin((double)(0.5F * angle)) / (double)n);
      float x2 = ax * s;
      float y2 = ay * s;
      float z2 = az * s;
      float w2 = (float)Math.cos((double)(0.5F * angle));
      mul(this.x, this.y, this.z, this.w, x2, y2, z2, w2, this);
   }

   public void orientX(float angleDeg) {
      this.setFromAxisAngle(1.0F, 0.0F, 0.0F, angleDeg / 180.0F * PI);
   }

   public void orientZero() {
      this.setIdentity();
   }

   public void rotateX(float angleDeg) {
      this.rotateDegrees(angleDeg, 1.0F, 0.0F, 0.0F);
   }

   public void rotateZ(float angleDeg) {
      this.rotateDegrees(angleDeg, 0.0F, 0.0F, 1.0F);
   }

   public void localRotateY(float angleDeg) {
      this.localRotateDegrees(angleDeg, 0.0F, 1.0F, 0.0F);
   }

   public void normalise() {
      float len = (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w));
      if (len > 1.0E-6F) {
         float inv = 1.0F / len;
         this.x *= inv;
         this.y *= inv;
         this.z *= inv;
         this.w *= inv;
      }
   }

   /** Spherical lerp toward {@code to}. t in 0..1. */
   public void slerp(Quaternion to, float t) {
      if (t <= 0.0F) {
         return;
      }

      if (t >= 1.0F) {
         this.set(to);
         return;
      }

      float dot = this.x * to.x + this.y * to.y + this.z * to.z + this.w * to.w;
      float tx = to.x;
      float ty = to.y;
      float tz = to.z;
      float tw = to.w;
      if (dot < 0.0F) {
         dot = -dot;
         tx = -tx;
         ty = -ty;
         tz = -tz;
         tw = -tw;
      }

      float scale0;
      float scale1;
      if (1.0F - dot > 1.0E-4F) {
         float theta = (float)Math.acos((double)dot);
         float sinTheta = (float)Math.sin((double)theta);
         scale0 = (float)Math.sin((double)((1.0F - t) * theta)) / sinTheta;
         scale1 = (float)Math.sin((double)(t * theta)) / sinTheta;
      } else {
         scale0 = 1.0F - t;
         scale1 = t;
      }

      this.x = scale0 * this.x + scale1 * tx;
      this.y = scale0 * this.y + scale1 * ty;
      this.z = scale0 * this.z + scale1 * tz;
      this.w = scale0 * this.w + scale1 * tw;
      this.normalise();
   }

   public static void mul(float x1, float y1, float z1, float w1, float x2, float y2, float z2, float w2, Quaternion dest) {
      dest.x = x1 * w2 + w1 * x2 + y1 * z2 - z1 * y2;
      dest.y = y1 * w2 + w1 * y2 + z1 * x2 - x1 * z2;
      dest.z = z1 * w2 + w1 * z2 + x1 * y2 - y1 * x2;
      dest.w = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2;
   }

   public void applyGl() {
      this.applyGl(false);
   }

   /** @param conjugate if true, apply inverse (matches 1.7.10 Euler -X/-Y convention) */
   public void applyGl(boolean conjugate) {
      float qx = conjugate ? -this.x : this.x;
      float qy = conjugate ? -this.y : this.y;
      float qz = conjugate ? -this.z : this.z;
      float qw = this.w;
      GL_MATRIX.clear();
      float xx = qx * qx;
      float xy = qx * qy;
      float xz = qx * qz;
      float xw = qx * qw;
      float yy = qy * qy;
      float yz = qy * qz;
      float yw = qy * qw;
      float zz = qz * qz;
      float zw = qz * qw;
      GL_MATRIX.put(1.0F - 2.0F * (yy + zz));
      GL_MATRIX.put(2.0F * (xy + zw));
      GL_MATRIX.put(2.0F * (xz - yw));
      GL_MATRIX.put(0.0F);
      GL_MATRIX.put(2.0F * (xy - zw));
      GL_MATRIX.put(1.0F - 2.0F * (xx + zz));
      GL_MATRIX.put(2.0F * (yz + xw));
      GL_MATRIX.put(0.0F);
      GL_MATRIX.put(2.0F * (xz + yw));
      GL_MATRIX.put(2.0F * (yz - xw));
      GL_MATRIX.put(1.0F - 2.0F * (xx + yy));
      GL_MATRIX.put(0.0F);
      GL_MATRIX.put(0.0F);
      GL_MATRIX.put(0.0F);
      GL_MATRIX.put(0.0F);
      GL_MATRIX.put(1.0F);
      GL_MATRIX.flip();
      GL11.glMultMatrix(GL_MATRIX);
   }
}
