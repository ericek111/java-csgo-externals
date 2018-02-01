package me.lixko.csgoexternals.util.bsp;

public class Image {
	public int nChannels; // The channels in the image (3 = RGB, 4 = RGBA)
	public int nWidth; // The width of the image in pixels
	public int nHeight; // The height of the image in pixels
	public byte[] pData; // The image pixel data

	public Image(int nChannels, int nWidth, int nHeight) {
		if (nChannels != 3 && nChannels != 4)
			throw new IllegalArgumentException("Channels not 3 or 4!");

		this.nChannels = nChannels;
		this.nWidth = nWidth;
		this.nHeight = nHeight;
		this.pData = new byte[nWidth * nHeight * nChannels];
	}

	public Image(Image orig) {
		nChannels = orig.nChannels;
		nWidth = orig.nWidth;
		nHeight = orig.nHeight;
		pData = orig.pData;
	}
}
