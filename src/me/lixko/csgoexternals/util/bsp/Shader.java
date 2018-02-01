package me.lixko.csgoexternals.util.bsp;

import me.lixko.csgoexternals.util.DrawUtils;

public class Shader {

	public int program, vertShader, fragShader, geomShader;

	public Shader(int program, int vertShader, int fragShader, int geomShader) {
		this.program = program;
		this.vertShader = vertShader;
		this.fragShader = fragShader;
		this.geomShader = geomShader;
	}

	public Shader() {
	}

	public int GetUniformLocation(String name) {
		return GetVariableLocation(name, true);
	}
	
	public int GetAttribLocation(String name) {
		return GetVariableLocation(name, false);
	}

	public int GetVariableLocation(String name, boolean isUniform) {
		if (isUniform) // uniform variables
			return DrawUtils.gl.glGetUniformLocation(program, name);
		else // attribute variables
			return DrawUtils.gl.glGetAttribLocation(program, name);
	}
	
	public void use() {
		DrawUtils.gl.glUseProgram(program);
	}
}
