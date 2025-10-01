package org.useless.gui.uir;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.useless.gui.template.Template;
import org.useless.gui.template.container.Window;
import org.useless.gui.drawing.Drawing;

/**
 * <h1>层管理</h1>
 * 用以管理渲染层，<br>
 * 弹窗、右键菜单不属于层,<br>
 * 窗口层不变 {@code 0      } 层,<br>
 * 绘图层默认 {@code 1~299  } 层，<br>
 * 缓冲层默认 {@code 300~499} 层，<br>
 * 模板层默认 {@code 500~999} 层，<br>
 * @since 0.0.3
 * @see Drawing
 * @see Layer
 * @see Window
 */
public class LayerManager {
    private final List<Layer> layers = new ArrayList<>();
    private final Window window;
    private boolean OUT = false;

    // 层级范围常量
    private static final int DRAWING_MIN = 1, DRAWING_MAX = 299;
    private static final int BUFFER_MIN = 300, BUFFER_MAX = 499;
    private static final int TEMPLATE_MIN = 500, TEMPLATE_MAX = 999;
    private static final int MAX_LAYERS = 1000; // 防止图层爆炸

    public LayerManager(Window window) {
        if (window == null) {
            throw new IllegalArgumentException("窗口不能为null！图层管理需要绑定具体窗口");
        }
        this.window = window;
    }

    /**
     * 添加图层，自动排序
     */
    public void addLayer(Layer layer) {
        // 1. 空值检测
        if (layer == null) {
            System.err.println("错误：尝试添加空图层！");
            return;
        }

        // 2. 窗口0层保护
        if (layer.getZIndex() == 0) {
            System.err.println("拦截：图层不能为0层，这是窗口神圣不可侵犯的领域！");
            return;
        }

        // 3. zIndex范围检测
        if (layer.getZIndex() < 1 || layer.getZIndex() > 999) {
            System.err.println("拦截：图层zIndex越界！有效范围: 1-999，当前值: " + layer.getZIndex());
            return;
        }

        // 4. 图层数量限制
        if (layers.size() >= MAX_LAYERS) {
            System.err.println("警告：图层数量已达上限(" + MAX_LAYERS + ")，拒绝添加新图层");
            return;
        }

        // 5. 重复添加检测
        if (layers.contains(layer)) {
            System.out.println("提示：图层已存在，正在更新...");
            layers.remove(layer);
        }


        // 智能层级分配
        int originalZ = layer.getZIndex();
        int allocatedZ = findOptimalZIndex(layer, originalZ);
        layer.setZIndex(allocatedZ);

        layers.add(layer);
        sortLayers();

        // 6. 添加成功日志
        if (OUT) System.out.println("图层添加成功: zIndex=" + layer.getZIndex() +
                ", 当前总图层数: " + layers.size());
    }

    private int findOptimalZIndex(Layer newLayer, int preferredZ) {
        // 1. 先找同层是否有空位（不重叠）
        boolean canShareLayer = true;
        for (Layer existing : layers) {
            if (existing.getZIndex() == preferredZ) {
                if (isOverlapping(existing, newLayer)) {
                    canShareLayer = false;
                    break;
                }
            }
        }
        if (canShareLayer) {
            if (OUT) System.out.println("检测到同层有空位，共享层级: " + preferredZ);
            return preferredZ;
        }

        // 2. 找最近可用层级（向上找空位）
        for (int z = preferredZ + 1; z <= TEMPLATE_MAX; z++) {
            boolean layerAvailable = true;
            for (Layer existing : layers) {
                if (existing.getZIndex() == z && isOverlapping(existing, newLayer)) {
                    layerAvailable = false;
                    break;
                }
            }
            if (layerAvailable) {
                if (OUT)System.out.println("分配到更高空闲层级: " + z);
                return z;
            }
        }

        // 3. 实在没空位就挤一挤（传统方式）
        System.out.println("层级紧张，强制分配到: " + (preferredZ + 1));
        return preferredZ + 1;
    }

    // 判断两个图层是否重叠
    private boolean isOverlapping(Layer a, Layer b) {
        // 简单的矩形碰撞检测
        if (a instanceof Template t1 && b instanceof Template t2) return !(t1.getX() + t1.getWidth() < t2.getX() ||
                t2.getX() + t2.getWidth() < t1.getX() ||
                t1.getY() + t1.getHeight() < t2.getY() ||
                t2.getY() + t2.getHeight() < t1.getY());
        return true; // 非模板组件默认认为会重叠（保守策略）
    }

    /**
     * 移除图层
     */
    public void removeLayer(Layer layer) {
        if (layer == null) {
            System.err.println("错误：尝试移除空图层！");
            return;
        }

        boolean removed = layers.remove(layer);
        if (removed) {
            if (OUT) System.out.println("图层移除成功: zIndex=" + layer.getZIndex() +
                    ", 剩余图层数: " + layers.size());
        } else {
            System.out.println("提示：要移除的图层不存在");
        }
    }

    /**
     * 按zIndex排序图层
     */
    private void sortLayers() {
        try {
            layers.sort(Comparator.comparingInt(Layer::getZIndex));

            // 排序后检测zIndex冲突
            detectZIndexConflicts();

        } catch (Exception e) {
            System.err.println("图层排序异常: " + e.getMessage());
            // 应急处理：按添加顺序排序
            layers.sort(Comparator.comparingInt(System::identityHashCode));
        }
    }

    /**
     * 检测zIndex冲突
     */
    private void detectZIndexConflicts() {
        Map<Integer, List<Layer>> zIndexMap = new HashMap<>();

        for (Layer layer : layers) {
            zIndexMap.computeIfAbsent(layer.getZIndex(), k -> new ArrayList<>()).add(layer);
        }

        for (Map.Entry<Integer, List<Layer>> entry : zIndexMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                // 检查这些同层图层是否真的重叠（冲突）
                boolean isRealConflict = checkIfRealConflict(entry.getValue());
                if (isRealConflict) {
                    System.err.println("警告：zIndex冲突！索引 " + entry.getKey() +
                            " 被 " + entry.getValue().size() + " 个图层共享");
                }
            }
        }
    }

    private boolean checkIfRealConflict(List<Layer> layersInSameZ) {
        // 如果只有一个图层，肯定没冲突
        if (layersInSameZ.size() <= 1) return false;

        // 检查所有同层图层之间是否两两重叠
        for (int i = 0; i < layersInSameZ.size(); i++) {
            for (int j = i + 1; j < layersInSameZ.size(); j++) {
                if (isOverlapping(layersInSameZ.get(i), layersInSameZ.get(j))) {
                    return true; // 发现真实冲突
                }
            }
        }
        return false; // 所有同层图层都不重叠，是合理共享
    }

    /**
     * 渲染所有图层
     */
    public void renderLayers(Drawing drawing) {
        if (drawing == null) {
            System.err.println("错误：绘图接口为null，无法渲染图层");
            return;
        }

        int renderedCount = 0;
        for (Layer layer : layers) {
            try {
                // 跳过无效图层
                if (layer == null) {
                    System.err.println("警告：遇到空图层，已跳过");
                    continue;
                }

                // 确保zIndex有效
                if (layer.getZIndex() > 0) {
                    layer.draw(drawing);
                    renderedCount++;
                }

            } catch (Exception e) {
                if (layer != null) {
                    System.err.println("图层渲染异常 (zIndex=" + layer.getZIndex() + "): " + e.getMessage());
                }
                // 继续渲染其他图层，不因为一个图层出错而中断
            }
        }

        if (renderedCount > 0) {
            if (OUT) System.out.println("图层渲染完成: 成功渲染 " + renderedCount + "/" + layers.size() + " 个图层");
        }
    }

    /**
     * 获取指定范围内的图层
     */
    public List<Layer> getLayersInRange(int minZ, int maxZ) {
        // 参数验证
        if (minZ < 1 || maxZ > 999 || minZ > maxZ) {
            System.err.println("错误：无效的zIndex范围 [" + minZ + ", " + maxZ + "]");
            return new ArrayList<>();
        }

        List<Layer> result = new ArrayList<>();
        for (Layer layer : layers) {
            int z = layer.getZIndex();
            if (z >= minZ && z <= maxZ) {
                result.add(layer);
            }
        }
        return result;
    }

    // 快捷方法
    public List<Layer> getDrawingLayers() {
        return getLayersInRange(DRAWING_MIN, DRAWING_MAX);
    }

    public List<Layer> getBufferLayers() {
        return getLayersInRange(BUFFER_MIN, BUFFER_MAX);
    }

    public List<Layer> getTemplateLayers() {
        return getLayersInRange(TEMPLATE_MIN, TEMPLATE_MAX);
    }

    /**
     * 清空所有图层
     */
    public void clear() {
        int count = layers.size();
        layers.clear();
        if (OUT) System.out.println("已清空所有图层，共清理 " + count + " 个图层");
    }

    /**
     * 批量操作安全检测
     */
    public void addLayers(List<Layer> layerList) {
        if (layerList == null || layerList.isEmpty()) {
            System.out.println("提示：要添加的图层列表为空");
            return;
        }

        // 检测是否会超出限制
        if (layers.size() + layerList.size() > MAX_LAYERS) {
            System.err.println("错误：批量添加将超出图层数量限制(" + MAX_LAYERS + ")");
            return;
        }

        int successCount = 0;
        for (Layer layer : layerList) {
            try {
                addLayer(layer);
                successCount++;
            } catch (Exception e) {
                System.err.println("批量添加图层失败: " + e.getMessage());
            }
        }

        if (OUT) System.out.println("批量添加完成: 成功 " + successCount + "/" + layerList.size());
    }

    /**
     * 图层系统健康检查
     */
    public boolean healthCheck() {
        if (window == null) {
            System.err.println("健康检查失败：窗口引用丢失");
            return false;
        }

        if (layers.size() > MAX_LAYERS * 0.8) {
            System.out.println("性能警告：图层数量接近上限(" + layers.size() + "/" + MAX_LAYERS + ")");
        }

        // 检测空图层
        long nullLayers = layers.stream().filter(Objects::isNull).count();
        if (nullLayers > 0) {
            System.err.println("健康问题：发现 " + nullLayers + " 个空图层");
            return false;
        }

        System.out.println("图层系统健康状态：正常 (图层数: " + layers.size() + ")");
        return true;
    }

    /**
     * 获取当前所有图层（已排序）
     */
    public List<Layer> getSortedLayers() {
        // 返回副本防止外部修改内部列表
        return new ArrayList<>(layers);
    }

    /**
     * 获取图层数量
     */
    public int getLayerCount() {
        return layers.size();
    }

    /**
     * 检查图层是否存在
     */
    public boolean containsLayer(Layer layer) {
        return layers.contains(layer);
    }

    /**
     * 根据zIndex获取图层列表
     */
    public List<Layer> getLayersByZIndex(int zIndex) {
        List<Layer> result = new ArrayList<>();
        for (Layer layer : layers) {
            if (layer.getZIndex() == zIndex) {
                result.add(layer);
            }
        }
        return result;
    }

    public void setOUT(boolean OUT) {
        this.OUT = OUT;
    }
}
