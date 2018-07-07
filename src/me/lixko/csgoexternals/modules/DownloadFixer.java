package me.lixko.csgoexternals.modules;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.offsets.PatternScanner;

public class DownloadFixer extends Module {
	public long downloadManager;
	
	@Override
	public void onEngineLoaded() {
		long TheDownloadManagerMov = PatternScanner.getAddressForPattern(Engine.engineModule(), "55 48 8D 3D ?? ?? ?? ?? 48 89 E5 5D E9 BF FF FF FF") + 1;
		downloadManager = Engine.engineModule().GetAbsoluteAddress(TheDownloadManagerMov, 3, 7);
		//this.onPreLoop();
		//System.exit(0);
	}
	
	@Override
	public void onPreLoop() {
		//System.out.println("lmao");
		long m_activeRequest = Engine.engineModule().readLong(downloadManager + 4 * 8);
		if(m_activeRequest == 0)
			return;

		// https://github.com/VSES/SourceEngine2007/blob/master/se2007/engine/download.cpp#L865
		
		// m_lastPercent - 9 * 8
		// "Downloading %s%s.\n",
		// *(_QWORD *)(a1 + 32) + 20LL,   - m_activeRequest->baseURL
		// *(_QWORD *)(a1 + 32) + 532LL); - m_activeRequest->gamePath
		
		// CUtlVector< RequestContext * > m_queuedRequests;
		long m_queuedRequests_List = Engine.engineModule().readLong(downloadManager);
		if(m_queuedRequests_List == 0)
			return;
				
		int m_queuedRequests_Count = Engine.engineModule().readInt(downloadManager + 2 * 8);
		System.out.println(m_queuedRequests_Count);
		for(int i = 0; i < m_queuedRequests_Count; i++) {
			m_activeRequest = Engine.engineModule().readLong(downloadManager + 4 * 8);
			// check if user hasn't disconnected
			if(m_activeRequest == 0)
				break;
			
			Engine.engineModule().writeInt(m_activeRequest + 8, 4);
			long curReq = Engine.engineModule().readLong(m_queuedRequests_List + i * 8); // m_queuedRequests[i]
			if(!Engine.engineModule().readBoolean(curReq + 3)) // bAsHTTP
				continue;
			
			String baseURL = Engine.engineModule().readString(curReq + 20, 256);
			String gamePath = Engine.engineModule().readString(curReq + 532, 256);
			
			System.out.println(baseURL + gamePath);
			
			try {
				final URL url = new URL(baseURL + gamePath);
				final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestProperty("User-Agent", "Half-Life 2");
				final int responseCode = conn.getResponseCode();
				// Engine.engineModule().writeInt(curReq + 1300, conn.getContentLength()); // nBytesTotal
				if(responseCode != HttpURLConnection.HTTP_OK) {
					System.err.println("[" + responseCode + "] Failed to download: " + baseURL + gamePath);
					Engine.engineModule().writeInt(curReq + 8, 4);
					continue;
				}
				
				final InputStream in = url.openStream();
				final Path path = Paths.get(Offsets.modDirectory + File.separator + gamePath);
				if(path.toFile().exists())
					path.toFile().delete();
				Files.createDirectories(path.getParent());
	            Files.copy(in, path);
			    Engine.engineModule().writeInt(curReq + 8, 2);
			} catch (IOException e) {
				Engine.engineModule().writeInt(curReq + 8, 4);
				e.printStackTrace();
			}
			
		}
		
		// https://github.com/VSES/SourceEngine2007/blob/43a5c90a5ada1e69ca044595383be67f40b33c61/src_main/engine/download_internal.h#L58
		// https://github.com/VSES/SourceEngine2007/blob/master/se2007/engine/download.cpp#L774
		//int status = Engine.engineModule().readInt(m_activeRequest + 8); //  m_activeRequest->status, HTTP_FETCH=1, HTTP_DONE=2, HTTP_ERROR=4
		//long nBytesTotal = Engine.engineModule().readInt(m_activeRequest + 1300); //  m_activeRequest->nBytesTotal
		//System.out.println(status + " / " + nBytesTotal);
	}
}
