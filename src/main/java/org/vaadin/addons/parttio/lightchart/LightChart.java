package org.vaadin.addons.parttio.lightchart;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AbstractConfigurationObject;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.Series;
import com.vaadin.flow.component.charts.util.ChartSerialization;
import java.lang.reflect.Field;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a chart component that clears it potentially heavy Configuration
 * after the chart has been drawn. This saves a considerable amount of memory in
 * apps that both have a lot of users and use large charts.
 * <p>
 * The downside is that interactive features requiring server visits are not
 * available. Also, updates to existing charts do not work.
 * <p>
 * Also, make sure you do not reference the Configuration from your own code.
 * Otherwise this hack cannot free the configuration object for garbage 
 * collection.
 * 
 * @author mstahv
 */
public class LightChart extends Chart {

    public LightChart() {
    }

    public LightChart(ChartType type) {
        super(type);
    }

    @Override
    public void drawChart(boolean b) {
        super.drawChart(b);
        freeMemory();
    }

    /**
     * Not needed anymore as the the whole configuration object is cleared.
     * 
     * @param series s
     * @deprecated
     */
    @Deprecated
    public void addSeriesWithoutLosingMemory(Series series) {
        String seriesStr = ChartSerialization.toJSON((AbstractConfigurationObject) series);
        getElement().executeJs("const s = JSON.parse($0); const el = this; setTimeout(()=>{el.__updateOrAddSeriesInstance(s);},10);", seriesStr);
    }


    private void freeMemory() {
        try {
            Field declaredField = getClass().getSuperclass().getDeclaredField("configuration");
            declaredField.setAccessible(true);
            declaredField.set(this, new Configuration());
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException ex) {
            Logger.getLogger(LightChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
