package me.lixko.csgoexternals.util.bsp;

import java.util.HashMap;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.FileUtil;

public class ShaderManager {
	// credits: dude719
	
	private HashMap<String, Shader> m_shaderMap = new HashMap<String, Shader>();
	private final String DEFAULT_SHADER = "DefaultShader";

	final int MAX_LINE_LENGTH = 5;

	public ShaderManager() {
		m_shaderMap.put(DEFAULT_SHADER, new Shader());
	}
	
	public Shader GetShader(String name) {
		return GetShader(name, false);
	}
	
	public Shader GetShader(String name, boolean useGeomShader) {
		Shader sh = m_shaderMap.get(name);
		if (sh != null)
			return sh;
		
		sh = Load(name + ".vert", name + ".frag", useGeomShader ? name + ".geom" : "");
		if(sh != null) {
			m_shaderMap.put(name, sh);
			return sh;
		}
		return m_shaderMap.get(DEFAULT_SHADER);
	}

	public Shader Load(String vertexFilename, String fragmentFilename, String geometryFilename) {
		int vertShader = 0, fragShader = 0, geomShader = 0, programShader = 0;
		vertShader = LoadShader(GL4.GL_VERTEX_SHADER, vertexFilename);
		fragShader = LoadShader(GL4.GL_FRAGMENT_SHADER, fragmentFilename);
		geomShader = LoadShader(GL4.GL_GEOMETRY_SHADER, geometryFilename);

		if (vertShader == -1 || fragShader == -1 || geomShader == -1) {
			System.out.println("[Shaders] Failed to compile!");
			return null;
		}

		// create a program
		programShader = DrawUtils.gl.glCreateProgram();

		// attach the vertex and fragment shader codes, and the geometric if available
		DrawUtils.gl.glAttachShader(programShader, vertShader);
		DrawUtils.gl.glAttachShader(programShader, fragShader);
		if (geomShader != 0)
			DrawUtils.gl.glAttachShader(programShader, geomShader);
		
		// link
		System.out.println("Linking...");
		DrawUtils.gl.glLinkProgram(programShader);
		
		// check link status
		final int[] statusarr = new int[1];
		DrawUtils.gl.glGetProgramiv(programShader, GL4.GL_LINK_STATUS, statusarr, 0);

		if (statusarr[0] != GL.GL_TRUE)	{
			// The link has failed, check log info
			final int[] logLength = new int[1];
			DrawUtils.gl.glGetProgramiv(programShader, GL4.GL_INFO_LOG_LENGTH, logLength, 0);

			final byte[] log = new byte[logLength[0]];
			DrawUtils.gl.glGetProgramInfoLog(programShader, logLength[0], (int[]) null, 0, log, 0);
			System.err.println("[Shaders] Failed to link the shader: " + new String(log));
			return null;
		}
		
		// check if the shader will run in the current OpenGL state
		DrawUtils.gl.glValidateProgram(programShader);
		DrawUtils.gl.glGetProgramiv(programShader, GL4.GL_VALIDATE_STATUS, statusarr, 0);
		if (statusarr[0] != GL.GL_TRUE)	{
			System.out.println("Shader program will not run in this OpenGL environment!");
			return null;
		}
		
		return new Shader(programShader, vertShader, fragShader, geomShader);
	}

	public int LoadShader(int shaderType, String fileName) {
		if (fileName == "")
			return 0;
		// TODO: Set path!
		String shaderSrc = FileUtil.readFile("/home/erik/Dokumenty/Java/linux-csgo-externals/eclipse/CSGOExternals/shaders/" + fileName);
		System.out.println("[Shaders] Compiling: " + fileName);

		int s = DrawUtils.gl.glCreateShader(shaderType);
		DrawUtils.gl.glShaderSource(s, 1, new String[] { shaderSrc }, new int[] { shaderSrc.length() }, 0);
		DrawUtils.gl.glCompileShader(s);

		final int[] statusarr = new int[1];
		DrawUtils.gl.glGetShaderiv(s, GL4.GL_COMPILE_STATUS, statusarr, 0);
		if (statusarr[0] == GL.GL_TRUE)
			return s;
		
		final int[] logLength = new int[1];
		DrawUtils.gl.glGetShaderiv(s, GL4.GL_INFO_LOG_LENGTH, logLength, 0);

		final byte[] log = new byte[logLength[0]];
		DrawUtils.gl.glGetShaderInfoLog(s, logLength[0], (int[]) null, 0, log, 0);
		DrawUtils.gl.glDeleteShader(s);
		System.err.println("[Shaders] Error compiling the shader: " + new String(log));
		
		return -1;
	}

}
