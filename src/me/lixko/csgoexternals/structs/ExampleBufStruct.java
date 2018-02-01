package me.lixko.csgoexternals.structs;

import me.lixko.csgoexternals.util.BufferStruct;

public class ExampleBufStruct extends BufferStruct {
	public int a = 2;
	public int b = 4;
	public ExampleBufChild[] bufc = new ExampleBufChild[2];
}
