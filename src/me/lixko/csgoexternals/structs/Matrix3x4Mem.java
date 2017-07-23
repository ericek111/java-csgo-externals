package me.lixko.csgoexternals.structs;

public class Matrix3x4Mem extends MemStruct {
	public final StructField f00 = new StructField(this, Float.BYTES);
	public final StructField f01 = new StructField(this, Float.BYTES);
	public final StructField f02 = new StructField(this, Float.BYTES);
	public final StructField f03 = new StructField(this, Float.BYTES);

	public final StructField f10 = new StructField(this, Float.BYTES);
	public final StructField f11 = new StructField(this, Float.BYTES);
	public final StructField f12 = new StructField(this, Float.BYTES);
	public final StructField f13 = new StructField(this, Float.BYTES);

	public final StructField f20 = new StructField(this, Float.BYTES);
	public final StructField f21 = new StructField(this, Float.BYTES);
	public final StructField f22 = new StructField(this, Float.BYTES);
	public final StructField f23 = new StructField(this, Float.BYTES);

	public float[][] getMatrix() {
		float[][] matrix = new float[3][4];
		matrix[0][0] = f00.getFloat();
		matrix[0][1] = f01.getFloat();
		matrix[0][2] = f02.getFloat();
		matrix[0][3] = f03.getFloat();
		matrix[1][0] = f10.getFloat();
		matrix[1][1] = f11.getFloat();
		matrix[1][2] = f12.getFloat();
		matrix[1][3] = f13.getFloat();
		matrix[2][0] = f20.getFloat();
		matrix[2][1] = f21.getFloat();
		matrix[2][2] = f22.getFloat();
		matrix[2][3] = f23.getFloat();
		return matrix;
	}
}
