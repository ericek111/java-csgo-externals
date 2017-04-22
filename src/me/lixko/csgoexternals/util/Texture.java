package me.lixko.csgoexternals.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jogamp.opengl.util.awt.TextureRenderer;

public class Texture {
	public BufferedImage img;
	public int width = 0;
	public int height = 0;
	public int texx = 0;
	public int texy = 0;
	public TextureRenderer mTextureRenderer;
	private static Graphics2D tTextureGraphics2D;

	public Texture(File imgfile) throws IOException {
		this.img = ImageIO.read(imgfile);
		this.width = img.getWidth();
		this.height = img.getHeight();
		mTextureRenderer = new TextureRenderer(this.width, this.height, true, true);
		tTextureGraphics2D = mTextureRenderer.createGraphics();
		tTextureGraphics2D.drawImage(this.img, 0, 0, null);
		tTextureGraphics2D.dispose();
		mTextureRenderer.markDirty(0, 0, this.width, this.height);
	}

}
