package com.xydium.rendering;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xydium.core.Psilox;

public class LayerSet {

	private List<String> layers;
	private Map<String, Layer> layerMap;
	
	private Layer currentLayer;
	
	private BufferedImage buffer;
	
	public LayerSet() {
		layers = new ArrayList<String>();
		layerMap = new HashMap<String, Layer>();
		buffer = new BufferedImage(Psilox.windowWidth(), Psilox.windowHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		addDefaultLayer();
	}
	
	public void addLayer(String layer) {
		layers.add(layer);
		layerMap.put(layer, new Layer());
	}
	
	public void addLayers(String... layers) {
		for(String l : layers) {
			addLayer(l);
		}
	}
	
	public Layer getCurrentLayer() {
		return currentLayer;
	}
	
	public void setCurrentLayer(String layer) {
		if(!layerMap.containsKey(layer)) layer = "default";
		currentLayer = layerMap.get(layer);
	}
	
	public void dumpLayers() {
		layers.clear();
		layerMap.clear();
		addDefaultLayer();
	}
	
	public void cleanLayers() {
		for(String l : layers) {
			cleanImage(layerMap.get(l).image());
		}
	}
	
	public void compressLayers() {
		cleanImage(buffer);
		Graphics2D g = buffer.createGraphics();
		for(String l : layers) {
			g.drawImage(layerMap.get(l).image(), 0, 0, null);
		}
	}
	
	public BufferedImage getBuffer() {
		return buffer;
	}
	
	private void cleanImage(BufferedImage image) {
		int[] db;
		db = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		for(int i = 0; i < db.length; i++) {
			db[i] = 0;
		}
	}
	
	private void addDefaultLayer() {
		addLayer("default");
		setCurrentLayer("default");
	}
	
}
