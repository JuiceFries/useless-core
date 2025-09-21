package org.useless.uui.uir;

import org.useless.uui.Drawing;
import org.useless.uui.template.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * <h1>层管理</h1>
 * 用以管理渲染层，<br>
 * 绘图层默认 {@code 0~199 } 层，<br>
 * 缓冲层默认 {@code 200~299} 层，<br>
 * 模板层默认 {@code 300~999} 层，<br>
 * @since 0.0.3
 * @see org.useless.uui.Drawing
 */
public class LayerManager {
    private final TreeMap<Integer, List<Layer>> layerGroups = new TreeMap<>();

    public void enrolledLayer(Layer layer) {
        layerGroups.computeIfAbsent(layer.getZIndex(), k -> new ArrayList<>()).add(layer);
    }

    // 按对象删除层（你刚才需要的）
    public boolean removeLayer(Layer layer) {
        for (List<Layer> layers : layerGroups.values()) {
            if (layers.remove(layer)) {
                return true;
            }
        }
        return false;
    }

    // 获取所有层（用于事件处理）
    public List<Layer> getAllLayers() {
        List<Layer> allLayers = new ArrayList<>();
        layerGroups.values().forEach(allLayers::addAll);
        return allLayers;
    }

    // 获取指定zIndex的所有层
    public List<Layer> getLayersAt(int zIndex) {
        return new ArrayList<>(layerGroups.getOrDefault(zIndex, List.of()));
    }

    // 清空所有层
    public void clearAllLayers() {
        layerGroups.clear();
    }

    // 检查层是否存在
    public boolean containsLayer(Layer layer) {
        return layerGroups.values().stream()
                .anyMatch(layers -> layers.contains(layer));
    }

    // 获取层数量
    public int getLayerCount() {
        return layerGroups.values().stream()
                .mapToInt(List::size)
                .sum();
    }

    public void logoutLayer(Drawing drawing) {
        layerGroups.forEach((zIndex, layers) ->
                layers.forEach(layer -> layer.draw(drawing)));
    }

    public void removeLayers(int zIndex) {
        layerGroups.remove(zIndex);
    }

    // 在LayerManager里添加
    public List<Template> getAllTemplates() {
        List<Template> templates = new ArrayList<>();
        for (List<Layer> layers : layerGroups.values()) {
            for (Layer layer : layers) {
                if (layer instanceof Template) {
                    templates.add((Template) layer);
                }
            }
        }
        return templates;
    }

}
