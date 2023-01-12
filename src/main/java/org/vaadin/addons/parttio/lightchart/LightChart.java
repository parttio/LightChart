package org.vaadin.addons.parttio.lightchart;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AbstractConfigurationObject;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Series;
import com.vaadin.flow.component.charts.util.ChartSerialization;

/**
 *
 * @author mstahv
 */
public class LightChart extends Chart {

    public LightChart() {
    }

    public LightChart(ChartType type) {
        super(type);
    }

    public void addSeriesWithoutLosingMemory(Series series) {
        String seriesStr = ChartSerialization.toJSON((AbstractConfigurationObject) series);
        getElement().executeJs("const s = JSON.parse($0); const el = this; setTimeout(()=>{el.__updateOrAddSeriesInstance(s);},10);", seriesStr);
    }

}
